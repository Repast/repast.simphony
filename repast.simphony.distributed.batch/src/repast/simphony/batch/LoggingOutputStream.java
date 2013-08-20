/**
 * 
 */
package repast.simphony.batch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * An OutputStream that writes contents to a Logger upon each call to flush().
 * Code from
 * https://blogs.oracle.com/nickstephen/entry/java_redirecting_system_out_and
 */
public class LoggingOutputStream extends ByteArrayOutputStream {

  private String lineSeparator;

  private Logger logger;
  private Level level;

  /**
   * @param logger
   *          Logger to write to
   * @param level
   *          Level at which to write the log message
   */
  public LoggingOutputStream(Logger logger, Level level) {
    this.logger = logger;
    this.level = level;
    lineSeparator = System.getProperty("line.separator");
  }

  /**
   * upon flush() write the existing contents of the OutputStream to the logger
   * as a log record.
   * 
   * @throws java.io.IOException
   *           in case of error
   */
  public void flush() throws IOException {

    String record;
    synchronized (this) {
      super.flush();
      record = this.toString();
      super.reset();

      if (record.length() == 0 || record.equals(lineSeparator)) {
        // avoid empty records
        return;
      }

      logger.log(level, record);
    }
  }
}