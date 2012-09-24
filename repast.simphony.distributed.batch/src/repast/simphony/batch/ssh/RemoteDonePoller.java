package repast.simphony.batch.ssh;

import java.util.concurrent.Callable;

import org.apache.log4j.Level;
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

  private long frequency;
  private RemoteSession remote;

  public RemoteDonePoller(RemoteSession remote, String directory, long frequency) {
    this.remote = remote;
    this.directory = directory;
    this.frequency = frequency;
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
          Thread.sleep(frequency);
        } catch (InterruptedException ex) {
        }
        
        exitStatus = session.executeCmd(cmd, Level.ERROR);
        logger.info(String.format("Polled %s on %s for %s: %s", directory, remote.getHost(),
              BatchConstants.DONE_FILE_NAME, exitStatus == 0 ? "yes" : "no"));
      }

    } finally {
      if (session != null)
        session.disconnect();
    }
    return null;
  }
}
