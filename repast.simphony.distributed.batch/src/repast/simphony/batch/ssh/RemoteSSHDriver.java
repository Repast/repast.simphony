package repast.simphony.batch.ssh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.BatchConstants;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Distributes a parameters to remote ssh'able hosts and runs a configurable
 * number of repast instances on those hosts. This then gathers the output and
 * any errors from the hosts and concatenates it.
 * 
 * @author Nick Collier
 */
public class RemoteSSHDriver {

  private static Logger logger = Logger.getLogger(RemoteSSHDriver.class);
  private static final String LOCAL_RUN_PROPS_FILE = "local_batch_run.properties";

  private Configuration config;

  RemoteSSHDriver(String propsFile) throws IOException {
    this.config = new Configuration(propsFile);
    SSHSessionFactory.init(config.getSSHKeyDir());
  }

  public void run() {
    try {
      BatchParameterChunker chunker = new BatchParameterChunker(config);
      chunker.run();

      String remoteDir = "simphony_model_" + System.currentTimeMillis();
      Map<String, File> archiveMap = new HashMap<String, File>();
      for (Remote remote : config.remotes()) {
        ModelArchiveConfigurator archConfig = new ModelArchiveConfigurator();
        File file = archConfig.configure(remote, config);
        archiveMap.put(remote.getId(), file);
        logger.info(String.format("%n\tCopying %s to %n\t%s@%s:~/%s ...", file.getAbsolutePath(),
            remote.getUser(), remote.getHost(), file.getName()));
        copyFile(remote, file);
        logger.info("Copying Finished.");
      }

      for (Remote remote : config.remotes()) {
        runModel(remote, remoteDir, archiveMap.get(remote.getId()));
      }

      pollForDone(remoteDir);
      getRemoteRunStatus(remoteDir);
      copyRemoteRunStatus(remoteDir);
      writeRemoteRunStatus();
      concatenateOutput(remoteDir);

      logger.info("Finished");

    } catch (RemoteStatusException ex) {
      logError(ex.getMessage(), ex);

    } catch (SSHDriverException ex) {
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

  private void writeRemoteRunStatus() throws SSHDriverException {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(config.getOutputDir() + "/"
          + BatchConstants.STATUS_OUTPUT_FILE));
      for (Remote remote : config.remotes()) {
        String prefix = remote.getHost() + "." + remote.getUser() + ".";
        for (int i = 1; i <= remote.getInstances(); i++) {
          writer.append(prefix);
          writer.append(String.valueOf(i));
          writer.append(" = ");
          writer.append(remote.getStatus(i).toString());
          writer.newLine();
        }
      }
    } catch (IOException ex) {
      throw new SSHDriverException("Error while writing remote status file to "
          + config.getOutputDir(), ex);
    } finally {
      if (writer != null)
        try {
          writer.close();
        } catch (IOException ex) {
          throw new SSHDriverException("Error while closing remote status file.", ex);
        }
    }
  }

  private void copyRemoteRunStatus(String remoteDir) throws RemoteStatusException {
    RemoteStatusCopier copier = new RemoteStatusCopier();
    for (Remote remote : config.remotes()) {
      copier.run(remote, remoteDir, config.getOutputDir());
    }
  }

  private void getRemoteRunStatus(String remoteDir) throws RemoteStatusException {
    RemoteStatusGetter getter = new RemoteStatusGetter();
    for (Remote remote : config.remotes()) {
      getter.run(remote, remoteDir);
    }
  }

  private void concatenateOutput(String remoteDir) throws RemoteStatusException, SSHDriverException {
    String tempDir = System.getProperty("java.io.tmpdir");

    List<File> copiedFiles = new ArrayList<File>();
    for (Remote remote : config.remotes()) {

      RemoteOutputCopier copier = new RemoteOutputCopier();
      File outDir = new File(tempDir, remote.getUser() + "_" + remote.getHost());
      outDir.mkdir();
      copiedFiles.addAll(copier.run(remote, remoteDir, outDir.getAbsolutePath()));

      logger.info(String.format("Copying remote output from %s to %s", remote.getUser() + "@"
          + remote.getHost() + ":" + remoteDir, outDir.getPath()));
    }

    try {
      OutputAggregator aggregator = new OutputAggregator();
      aggregator.run(copiedFiles, config.getOutputDir());
      logger.info("Aggregating remote output into " + config.getOutputDir());
    } catch (IOException ex) {
      throw new SSHDriverException("Error while aggregating remote output", ex);
    }

  }

  private void pollForDone(String remoteDir) throws SSHDriverException {

    List<Future<Void>> futures = new ArrayList<Future<Void>>();
    ExecutorService executor = null;
    try {
      executor = Executors.newFixedThreadPool(config.getRemoteCount());
      for (Remote remote : config.remotes()) {
        RemoteDonePoller poller = new RemoteDonePoller(remote, remoteDir);
        futures.add(executor.submit(poller));
      }

      for (Future<Void> future : futures) {
        try {
          future.get();
        } catch (Exception ex) {
          String msg = String.format("Error while polling a remote for finish");
          throw new SSHDriverException(msg, ex);
        }
      }

    } finally {
      if (executor != null)
        executor.shutdown();
    }
  }

  public void runModel(Remote remote, String remoteDir, File modelArchive)
      throws SSHDriverException {
    SSHSession session = null;
    try {
      session = SSHSessionFactory.getInstance().create(remote);
      // mkdir and unzip
      String cmd = String.format("mkdir %s", remoteDir);
      int exitStatus = session.executeCmd(cmd);

      if (exitStatus != 0) {
        String msg = String.format("Error executing '%s' on remote %s. See log for details.", cmd,
            remote.getId());
        throw new SSHDriverException(msg);
      }

      logger
          .info(String.format("Unzipping model on %s@%s ...", remote.getUser(), remote.getHost()));
      cmd = String.format("mv %s %s;cd %s; unzip -n %s", modelArchive.getName(), remoteDir,
          remoteDir, modelArchive.getName());

      session.executeCmd(cmd);
      if (exitStatus != 0) {
        String msg = String.format("Error executing '%s' on remote %s. See log for details.", cmd,
            remote.getId());
        throw new SSHDriverException(msg);
      }

      logger.info(String.format("Running model on %s@%s ...", remote.getUser(), remote.getHost()));
      cmd = String.format("cd %s; nohup java -cp \"./lib/*\" repast.simphony.batch.LocalDriver "
          + LOCAL_RUN_PROPS_FILE + " ", remoteDir);
      // executes in the background, this session will disconnect
      try {
        session.executeBackgroundCommand(cmd);
      } catch (JSchException e) {
        String msg = String.format("Error executing '%s' on remote %s.", cmd, remote.getId());
        throw new SSHDriverException(msg, e);
      }

    } catch (JSchException e) {
      String msg = String.format("Error while creating connection to %s", remote.getId());
      throw new SSHDriverException(msg, e);

    } finally {
      if (session != null)
        session.disconnect();
    }
  }

  private void copyFile(Remote remote, File file) throws SSHDriverException {
    SSHSession session = null;
    try {
      session = SSHSessionFactory.getInstance().create(remote);
      session.copyFileToRemote(file);

    } catch (JSchException e) {
      String msg = String.format("Error while creating connection to %s", remote.getId());
      throw new SSHDriverException(msg, e);

    } catch (SftpException e) {
      String msg = String.format("Error while copying %s via sftp to %s", file.getPath(),
          remote.getId());
      throw new SSHDriverException(msg, e);

    } finally {
      if (session != null)
        session.disconnect();
    }
  }

  public static void main(String[] args) {
    try {
      Properties props = new Properties();
      File in = new File("./config/SSH.MessageCenter.log4j.properties");
      props.load(new FileInputStream(in));
      PropertyConfigurator.configure(props);

      try {
        new RemoteSSHDriver(args[0]).run();
      } catch (IOException ex) {
        logger.error("Error reading ssh driver configuration file", ex);
      }

    } catch (IOException ex) {
      logger.error("Error reading log configuration properties file", ex);
    }

  }

  private static class SSHDriverException extends Exception {

    private static final long serialVersionUID = 1L;

    public SSHDriverException(String msg, Throwable t) {
      super(msg, t);
    }

    public SSHDriverException(String msg) {
      super(msg);
    }
  }

}
