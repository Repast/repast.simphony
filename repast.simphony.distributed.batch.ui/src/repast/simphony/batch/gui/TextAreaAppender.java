/**
 * 
 */
package repast.simphony.batch.gui;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Nick Collier
 */
public class TextAreaAppender extends AppenderSkeleton {

  private ConsolePanel console;
  private Layout layout;

  public TextAreaAppender(ConsolePanel console, Layout layout) {
    this.console = console;
    this.layout = layout;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.Appender#close()
   */
  @Override
  public void close() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.log4j.Appender#requiresLayout()
   */
  @Override
  public boolean requiresLayout() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
   */
  @Override
  protected void append(LoggingEvent evt) {
    String text = layout.format(evt);
    boolean error = evt.getLevel().isGreaterOrEqual(Level.WARN);
    console.update(text, error);
  }
}
