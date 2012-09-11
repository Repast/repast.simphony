/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;
import repast.simphony.batch.RunningStatus;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Encapsulates info about a Remote ssh reachable host that simphony models can
 * be run on.
 * 
 * @author Nick Collier
 */
public class RemoteSession implements Session {

  private static Logger logger = Logger.getLogger(RemoteSession.class);

  String host, user, input = "";
  int instances = 0;

  private File modelArchive = null;
  private String remoteDirectory = null;

  private Map<Integer, RunningStatus> stati = new HashMap<Integer, RunningStatus>();

  public int getInstances() {
    return instances;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.Remote#getId()
   */
  @Override
  public String getId() {
    return user + "@" + host;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.Remote#getHost()
   */
  @Override
  public String getHost() {
    return host;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.Remote#getUser()
   */
  @Override
  public String getUser() {
    return user;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Remote#getInput()
   */
  @Override
  public String getInput() {
    return input;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.batch.ssh.Remote#setInput(java.lang.String)
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

  /**
   * Initializes the model for running. This configures the model archive for
   * this Session and copies it over to the remote host.
   * 
   * @throws ModelArchiveConfiguratorException
   * @throws SessionException
   */
  public void initModelArchive(Configuration config, String directory)
      throws ModelArchiveConfiguratorException, SessionException {
    remoteDirectory = directory;
    ModelArchiveConfigurator archConfig = new ModelArchiveConfigurator();
    modelArchive = archConfig.configure(this, config);

    logger.info(String.format("%n\tCopying %s to %n\t%s@%s:~/%s/%s ...",
        modelArchive.getAbsolutePath(), getUser(), getHost(), directory, modelArchive.getName()));
    copyFile(modelArchive, directory);
    logger.info("Copying Finished.");
  }

  private void copyFile(File file, String remoteDir) throws SessionException {
    SSHSession session = null;
    try {
      session = SSHSessionFactory.getInstance().create(this);
      // mkdir
      String cmd = String.format("mkdir %s", remoteDir);
      int exitStatus = session.executeCmd(cmd, Level.ERROR);

      if (exitStatus != 0) {
        String msg = String.format("Error executing '%s' on remote %s. See log for details.", cmd,
            getId());
        throw new SessionException(msg);
      }
      
      session.copyFileToRemote(file, remoteDir);

    } catch (JSchException e) {
      String msg = String.format("Error while creating connection to %s", getId());
      throw new SessionException(msg, e);

    } catch (SftpException e) {
      String msg = String.format("Error while copying %s via sftp to %s", file.getPath(), getId());
      throw new SessionException(msg, e);

    } finally {
      if (session != null)
        session.disconnect();
    }
  }

  /**
   * Runs the model for this Session.
   * 
   * @throws SessionException
   */
  public void runModel() throws SessionException {
    if (modelArchive == null) {
      throw new SessionException(
          "Model has not been initialized for running. Call initModel first.");
    }

    SSHSession session = null;
    try {
      session = SSHSessionFactory.getInstance().create(this);
      
      logger.info(String.format("Unzipping model on %s@%s", getUser(), getHost()));
      String cmd = String.format("cd %s; unzip -n %s", remoteDirectory, modelArchive.getName());

      int exitStatus = session.executeCmd(cmd, Level.ERROR);
      if (exitStatus != 0) {
        String msg = String.format("Error executing '%s' on remote %s. See log for details.", cmd,
            getId());
        throw new SessionException(msg);
      }
      
      // check if java exists on the remote machine
      logger.info(String.format("Checking for java on %s@%s", getUser(), getHost()));
      exitStatus = session.executeCmd("java -version", Level.INFO);
      if (exitStatus != 0) {
        String msg = String.format("Error executing java on remote %s. Is it installed?", getId());
        throw new SessionException(msg);
      }

      logger.info(String.format("Running model on %s@%s ...", getUser(), getHost()));
      cmd = String.format("cd %s; nohup java -cp \"./lib/*\" repast.simphony.batch.LocalDriver "
          + BatchConstants.LOCAL_RUN_PROPS_FILE + " ", remoteDirectory);
      // executes in the background, this session will disconnect
      try {
        session.executeBackgroundCommand(cmd);
        
      } catch (JSchException e) {
        String msg = String.format("Error executing '%s' on remote %s.", cmd, getId());
        throw new SessionException(msg, e);
      }

    } catch (JSchException e) {
      String msg = String.format("Error while creating connection to %s", getId());
      throw new SessionException(msg, e);

    } finally {
      if (session != null)
        session.disconnect();
    }
  }

  /**
   * Retrieves the run completion status (e.g. FAILURE) and sets it for this
   * Session.
   * 
   * @throws StatusException
   */
  public void retrieveRunCompletionStatus() throws StatusException {
    RemoteStatusGetter getter = new RemoteStatusGetter();
    getter.run(this, remoteDirectory);
  }

  /**
   * Copies the remote output to local temporary location and returns the
   * location of the copied output.
   * 
   * @return the location of the copied output.
   * @throws StatusException
   */
  public List<File> findOutput() throws StatusException {
    String tempDir = System.getProperty("java.io.tmpdir");

    RemoteOutputFinderCopier copier = new RemoteOutputFinderCopier();
    File outDir = new File(tempDir, getUser() + "_" + getHost());
    outDir.mkdir();
    logger.info(String.format("Finding and copying remote output from %s to %s", getUser() + "@" + getHost()
        + ":" + remoteDirectory, outDir.getPath()));

    return copier.run(this, remoteDirectory, outDir.getAbsolutePath());
  }

  /**
   * Copies the completion status from the host and directory where the model
   * was run to the specified directory.
   * 
   * @param outDirectory
   * @throws StatusException
   */
  public void copyCompletionStatus(String outDirectory) throws StatusException {
    RemoteStatusCopier copier = new RemoteStatusCopier();
    copier.run(this, remoteDirectory, outDirectory);
  }

  /**
   * Create a callable that returns with the model run by this Session is
   * finished.
   * 
   * @return a callable that returns with the model run by this Session is
   *         finished.
   */
  public Callable<Void> createDonePoller(long frequency) {
    return new RemoteDonePoller(this, remoteDirectory, frequency);
  }
}
