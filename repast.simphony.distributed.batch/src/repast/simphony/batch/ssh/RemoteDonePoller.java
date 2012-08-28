package repast.simphony.batch.ssh;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;

/**
 * Polls a remote machine via SSH looking for a "DONE" file in a specified
 * directory.
 * 
 * @author Nick Collier
 */
public class RemoteDonePoller implements Callable<Void> {

  Logger logger = Logger.getLogger(RemoteDonePoller.class);

  private String directory;

  private Remote remote;

  public RemoteDonePoller(Remote remote, String directory) {
    this.remote = remote;
    this.directory = directory;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.Callable#call()
   */
  @Override
  public Void call() throws Exception {
    SSHSession session = null;
    try {

      session = SSHSessionFactory.getInstance().create(remote);
      String cmd = String.format("cd %s;test -e %s", directory, BatchConstants.DONE_FILE_NAME);

      int exitStatus = 1;
      while (exitStatus != 0) {
        try {
          // TODO make configurable
          Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        
        exitStatus = session.executeCmd(cmd);
        logger.info(String.format("Polled %s in %s for %s with %s", remote.getHost(), directory,
              BatchConstants.DONE_FILE_NAME, exitStatus == 0 ? "success" : "failure"));
      }

    } finally {
      if (session != null)
        session.disconnect();
    }
    return null;
  }
}
