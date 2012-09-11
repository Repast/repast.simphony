/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;
import repast.simphony.batch.ProcessOutputLogWriter;
import repast.simphony.batch.RunningStatus;
import repast.simphony.util.FileUtils;

/**
 * @author Nick Collier
 */
public class LocalSession implements Session {

  private static Logger logger = Logger.getLogger(LocalSession.class);

  private String input = "";
  int instances = 0;
  String workingDir;

  private File modelArchive = null;
  private String localRunningDirectory = null;

  private Map<Integer, RunningStatus> stati = new HashMap<Integer, RunningStatus>();

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#getHost()
   */
  @Override
  public String getHost() {
    return "localhost";
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#getUser()
   */
  @Override
  public String getUser() {
    return System.getProperty("user.name");
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#getId()
   */
  @Override
  public String getId() {
    return getUser() + "@" + getHost();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#getInstances()
   */
  @Override
  public int getInstances() {
    return instances;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#getInput()
   */
  @Override
  public String getInput() {
    return input;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#setInput(java.lang.String)
   */
  @Override
  public void setInput(String input) {
    this.input = input;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Remote#setRunStatus(int,
   * repast.simphony.batch.RunningStatus)
   */
  @Override
  public void setRunStatus(int instance, RunningStatus status) {
    stati.put(instance, status);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Remote#getStatus(int)
   */
  @Override
  public RunningStatus getStatus(int instance) {
    return stati.get(instance);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#createDonePoller(long)
   */
  @Override
  public Callable<Void> createDonePoller(long frequency) {
    File localDir = new File(workingDir, localRunningDirectory);
    return new LocalDonePoller(localDir.getAbsolutePath(), frequency);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.batch.ssh.Session#copyCompletionStatus(java.lang.String)
   */
  @Override
  public void copyCompletionStatus(String outDirectory) throws StatusException {
    LocalStatusCopier copier = new LocalStatusCopier();
    File localDir = new File(workingDir, localRunningDirectory);
    copier.run(this, localDir, outDirectory);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#copyOutput()
   */
  @Override
  public List<File> findOutput() throws StatusException {
    LocalOutputFinder finder = new LocalOutputFinder();
    File localDir = new File(workingDir, localRunningDirectory);
    logger.info(String.format("Finding output on localhost in %s", localDir.getPath()));
    return finder.run(localDir);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#retrieveRunCompletionStatus()
   */
  @Override
  public void retrieveRunCompletionStatus() throws StatusException {
    File statusFile = new File(workingDir, localRunningDirectory);
    statusFile = new File(statusFile, BatchConstants.STATUS_OUTPUT_FILE);
    Properties props = new Properties();
    try {
      props.load(new FileReader(statusFile));
      for (String key : props.stringPropertyNames()) {
        // key should be int and value is status
        setRunStatus(Integer.valueOf(key), RunningStatus.valueOf(props.getProperty(key)));
      }
    } catch (IOException ex) {
      String msg = String.format("Error reading local status file '%s'", statusFile.getPath());
      throw new StatusException(msg, ex);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Session#runModel(java.lang.String)
   */
  @Override
  public void runModel() throws SessionException {
    if (modelArchive == null) {
      throw new SessionException(
          "Model has not been initialized for running. Call initModel first.");
    }

    File localDir = new File(workingDir, localRunningDirectory);
    unzip(localDir);

    logger.info("Running model on localhost ...");
    ProcessBuilder builder = new ProcessBuilder();
    builder.directory(localDir);
    builder.command("java", "-cp", "./lib/*", "repast.simphony.batch.LocalDriver",
        BatchConstants.LOCAL_RUN_PROPS_FILE);
    builder.redirectErrorStream(true);

    ProcessRunner runner = new ProcessRunner(builder);
    new Thread(runner).start();
  }

  private void unzip(File localDir) throws SessionException {
    File zipFile = new File(localDir, modelArchive.getName());

    logger.info(String.format("Unzipping model %s", zipFile.getPath()));

    ZipFile zip = null;
    try {
      zip = new ZipFile(zipFile);
      for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
        ZipEntry entry = iter.nextElement();
        File file = new File(localDir, entry.getName());
        if (entry.isDirectory()) {
          file.mkdirs();
        } else {
          ReadableByteChannel source = Channels.newChannel(zip.getInputStream(entry));
          if (!file.exists())
            file.createNewFile();
          FileChannel dstChannel = new FileOutputStream(file).getChannel();
          dstChannel.transferFrom(source, 0, entry.getSize());
          dstChannel.close();
          source.close();
        }
      }
    } catch (ZipException ex) {
      String msg = String.format("Error while unzipping model archive %s", zipFile.getPath());
      throw new SessionException(msg, ex);

    } catch (IOException ex) {
      String msg = String.format("Error while unzipping model archive %s", zipFile.getPath());
      throw new SessionException(msg, ex);

    } finally {
      if (zip != null)
        try {
          zip.close();
        } catch (IOException ex) {
          logger.error(ex);
        }
    }

  }

  /**
   * Initializes the model for running. This configures the model archive for
   * this Session and copies it over to the remote host.
   * 
   * @param directory
   *          the directory to copy the model archive into
   * 
   * @throws ModelArchiveConfiguratorException
   * @throws SessionException
   */
  @Override
  public void initModelArchive(Configuration config, String directory)
      throws ModelArchiveConfiguratorException, SessionException {
    localRunningDirectory = directory;
    ModelArchiveConfigurator archConfig = new ModelArchiveConfigurator();
    modelArchive = archConfig.configure(this, config);

    File dest = new File(workingDir, directory);
    if (!dest.exists()) {
      dest.mkdirs();
    }

    logger.info(String.format("%n\tCopying locally %s to %n%s ...", modelArchive.getAbsolutePath(),
        dest.getAbsolutePath()));

    dest = new File(dest, modelArchive.getName());

    try {
      FileUtils.copyFile(modelArchive, dest);
    } catch (IOException ex) {
      String msg = String.format("Error while copying %s to %s", modelArchive.getAbsolutePath(),
          dest.getAbsolutePath());
      throw new SessionException(msg, ex);
    }
    logger.info("Copying Finished.");
  }

  private static class ProcessRunner implements Runnable {

    ProcessBuilder builder;

    public ProcessRunner(ProcessBuilder builder) {
      this.builder = builder;
    }

    public void run() {
      ProcessOutputLogWriter logWriter = new ProcessOutputLogWriter(logger);
      try {
        Process process = builder.start();
        logWriter.captureOutput(process);
      } catch (IOException ex) {
        logger.error(ex);
      }
    }
  }

}
