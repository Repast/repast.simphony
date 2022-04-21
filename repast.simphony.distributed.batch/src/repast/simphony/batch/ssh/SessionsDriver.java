package repast.simphony.batch.ssh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.BatchConstants;
import repast.simphony.batch.ssh.BaseOutputNamesFinder.FinderOutput;
import repast.simphony.data2.engine.FileSinkControllerActionIO;
import repast.simphony.scenario.ScenarioLoadException;
import simphony.util.messages.MessageCenter;

/**
 * Distributes parameters to remote ssh'able hosts and locally. Then runs a
 * configurable number of repast instances on those hosts. This then gathers the
 * output and any errors from the hosts and concatenates it.
 * 
 * @author Nick Collier
 */
public class SessionsDriver {

    private static Logger logger = Logger.getLogger(SessionsDriver.class);

    private Configuration config;

    public SessionsDriver(String propsFile) throws IOException {
        this.config = new Configuration(propsFile);
        SSHSessionFactory.init(config.getSSHKeyDir());
    }

    public void run() {
        try {
            long start = System.currentTimeMillis();
            BatchParameterChunker chunker = new BatchParameterChunker(config);
            chunker.run();

            String directory = "simphony_model_" + System.currentTimeMillis();

            long time = System.currentTimeMillis();
            for (Session session : config.sessions()) {
                session.initModelArchive(config, directory);
            }

            String msg = String.format("Initialization Time: %.4f", (System.currentTimeMillis() - time) / 1000f / 60f);
            logger.info(msg);

            time = System.currentTimeMillis();
            for (Session session : config.sessions()) {
                session.runModel();
            }

            pollForDone(directory);
            msg = String.format("Run Time: %.4f", (System.currentTimeMillis() - time) / 1000f / 60f);
            logger.info(msg);

            time = System.currentTimeMillis();
            getRemoteRunStatus();
            copyRemoteRunStatus();
            writeRemoteRunStatus();
            concatenateOutput(directory);

            msg = String.format("Get Output Time: %.4f", (System.currentTimeMillis() - time) / 1000f / 60f);
            logger.info(msg);

            long duration = System.currentTimeMillis() - start;
            msg = String.format("Finished. Elapsed Time: %.4f", duration / 1000f / 60f);
            logger.info(msg);

        } catch (StatusException ex) {
            logError(ex.getMessage(), ex);

        } catch (SessionException ex) {
            logError(ex.getMessage(), ex);

        } catch (BatchParameterChunkerException ex) {
            logError("Error while processing batch parameters for distribution", ex);

        } catch (ModelArchiveConfiguratorException ex) {
            logError("Error while preparing zip file for distributions", ex);
        }
    }

    private void logError(String msg, Throwable t) {
        Throwable cause = t.getCause() == null ? t : t.getCause();
        logger.error(msg, cause);
        System.err.println(msg);
    }

    private void writeRemoteRunStatus() throws SessionException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new FileWriter(config.getOutputDir() + "/" + BatchConstants.STATUS_OUTPUT_FILE));
            for (Session session : config.sessions()) {
                String prefix = session.getHost() + "." + session.getUser() + ".";
                for (int i = 1; i <= session.getInstances(); i++) {
                    writer.append(prefix);
                    writer.append(String.valueOf(i));
                    writer.append(" = ");
                    writer.append(session.getStatus(i).toString());
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            throw new SessionException("Error while writing remote status file to " + config.getOutputDir(), ex);
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException ex) {
                    throw new SessionException("Error while closing remote status file.", ex);
                }
        }
    }

    private void copyRemoteRunStatus() throws StatusException {
        for (Session session : config.sessions()) {
            session.copyCompletionStatus(config.getOutputDir());
        }
    }

    private void getRemoteRunStatus() throws StatusException {
        for (Session session : config.sessions()) {
            session.retrieveRunCompletionStatus();
        }
    }

    private void concatenateOutput(String remoteDir) throws StatusException, SessionException {
        List<BaseOutputNamesFinder.FinderOutput> foundFS = new ArrayList<BaseOutputNamesFinder.FinderOutput>();

        try (ZipFile zip = new ZipFile(config.getModelArchive())) {
            // look in the scenario directory in the zip file to find the
            // base output names.
            BaseOutputNamesFinder finder = new BaseOutputNamesFinder();

            for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
                ZipEntry entry = iter.nextElement();
                if (entry.getName().startsWith("scenario.rs/" + FileSinkControllerActionIO.SERIALIZATION_ID)) {
                    foundFS.add(finder.find(zip.getInputStream(entry)));
                }
            }
        } catch (IOException | XMLStreamException ex) {
            throw new SessionException("Error while finding default output file names", ex);
        }

        List<OutputPattern> patterns = new ArrayList<>();
        for (FinderOutput fo : foundFS) {
            DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator(fo.getFileName(), fo.hasTimestamp());
            // this has to be first, otherwise the non param map pattern will catch it.
            patterns.add(creator.getParamMapPattern());
            patterns.add(creator.getFileSinkOutputPattern());
        }

        patterns.addAll(config.getOutputPatterns());

        // merge the MatchedFiles from all the sessions.
        // MatchedFiles with the same output path are
        // combined.
        Map<String, MatchedFiles> matches = new HashMap<>();
        for (Session session : config.sessions()) {
            for (MatchedFiles match : session.findOutput(patterns)) {
                String output = match.getPattern().getPath();
                MatchedFiles files = matches.get(output);
                if (files == null) {
                    matches.put(output, match);
                } else {
                    files.addAllFiles(match.getFiles());
                }
            }
        }

        try {
            logger.info("Aggregating output into " + config.getOutputDir());
            for (MatchedFiles file : matches.values()) {
                file.aggregateOutput(config.getOutputDir());
            }
        } catch (IOException ex) {
            throw new SessionException("Error while aggregating output", ex);
        }
    }

    private void pollForDone(String remoteDir) throws SessionException {

        List<Future<Void>> futures = new ArrayList<Future<Void>>();
        ExecutorService executor = null;
        try {
            executor = Executors.newFixedThreadPool(config.getRemoteCount());
            for (Session session : config.sessions()) {
                Callable<Void> poller = session.createDonePoller((long) (config.getPollFrequency() * 1000));
                futures.add(executor.submit(poller));
            }

            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception ex) {
                    String msg = String.format("Error while polling a remote for finish");
                    throw new SessionException(msg, ex);
                }
            }

        } finally {
            if (executor != null)
                executor.shutdown();
        }
    }

    public static void main(String[] args) {
        try {
            Properties props = MessageCenter.updateProperties("./config/SSH.MessageCenter.log4j.properties");
            PropertyConfigurator.configure(props);

            try {
                new SessionsDriver(args[0]).run();
            } catch (IOException ex) {
                logger.error("Error reading ssh driver configuration file", ex);
            }

        } catch (IOException ex) {
            logger.error("Error reading log configuration properties file", ex);
        }

    }

}
