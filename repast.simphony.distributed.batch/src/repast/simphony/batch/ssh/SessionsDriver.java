package repast.simphony.batch.ssh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
import repast.simphony.data2.engine.FileSinkControllerActionIO;

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
      BatchParameterChunker chunker = new BatchParameterChunker(config);
      chunker.run();

      String directory = "simphony_model_" + System.currentTimeMillis();

      for (Session session : config.sessions()) {
        session.initModelArchive(config, directory);
      }

      for (Session session : config.sessions()) {
        session.runModel();
      }

      pollForDone(directory);
      getRemoteRunStatus();
      copyRemoteRunStatus();
      writeRemoteRunStatus();
      concatenateOutput(directory);

      logger.info("Finished");

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
      writer = new BufferedWriter(new FileWriter(config.getOutputDir() + "/"
          + BatchConstants.STATUS_OUTPUT_FILE));
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
      throw new SessionException("Error while writing remote status file to "
          + config.getOutputDir(), ex);
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

    List<File> copiedFiles = new ArrayList<File>();
    for (Session session : config.sessions()) {
      copiedFiles.addAll(session.findOutput());
    }

    ZipFile zip = null;
    try {
      // look in the scenario directory in the zip file to find the 
      // base output names.
      BaseOutputNamesFinder finder = new BaseOutputNamesFinder();
      List<String> baseNames = new ArrayList<String>();
      zip = new ZipFile(config.getModelArchive());
      for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
        ZipEntry entry = iter.nextElement();
        if (entry.getName().startsWith("scenario.rs/" + FileSinkControllerActionIO.SERIALIZATION_ID)) {
          baseNames.add(finder.find(zip.getInputStream(entry)));
        }
      }
      
      OutputAggregator aggregator = new OutputAggregator();
      aggregator.run(baseNames, copiedFiles, config.getOutputDir());
      logger.info("Aggregating output into " + config.getOutputDir());
    } catch (IOException ex) {
      throw new SessionException("Error while aggregating remote output", ex);
    } catch (XMLStreamException ex) {
      throw new SessionException("Error while aggregating remote output", ex);
    } finally {
      if (zip != null)
        try {
          zip.close();
        } catch (IOException ex) {
          throw new SessionException("Error while aggregating remote output", ex);
        }
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
      Properties props = new Properties();
      File in = new File("./config/SSH.MessageCenter.log4j.properties");
      props.load(new FileInputStream(in));
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
