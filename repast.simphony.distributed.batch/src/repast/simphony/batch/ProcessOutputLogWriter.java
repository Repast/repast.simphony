/**
 * 
 */
package repast.simphony.batch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * Writes the output of a Process to a Logger.
 * 
 * @author Nick Collier
 */
public class ProcessOutputLogWriter {

  private Logger logger;

  public ProcessOutputLogWriter(Logger logger) {
    this.logger = logger;
  }

  public void captureOutput(Process process) throws IOException {
    InputStreamReader tempReader = new InputStreamReader(new BufferedInputStream(
        process.getInputStream()));
    BufferedReader reader = new BufferedReader(tempReader);
    String line = "";
    while ((line = reader.readLine()) != null) {
      logger.info(line);
    }
  }
}
