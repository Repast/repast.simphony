package repast.simphony.batch.ssh;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import repast.simphony.batch.BatchConstants;

/**
 * Polls a directory on a local machine for a "DONE" file in a specified
 * directory.
 * 
 * @author Nick Collier
 */
public class LocalDonePoller implements Callable<Void> {

  Logger logger = Logger.getLogger(LocalDonePoller.class);

  private String directory;
  private long frequency;

  public LocalDonePoller(String directory, long frequency) {
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

    File doneFile = new File(directory, BatchConstants.DONE_FILE_NAME);
    while (!doneFile.exists()) {
      try {
        Thread.sleep(frequency);
      } catch (InterruptedException ex) {
      }

      logger.info(String.format("Polled %s for %s:  %s", directory,
          BatchConstants.DONE_FILE_NAME, doneFile.exists() ? "yes" : "no"));
    }
    return null;
  }
}
