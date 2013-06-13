/**
 * 
 */
package repast.simphony.data2.engine;

import repast.simphony.data2.ConsoleDataSink;
import repast.simphony.data2.FormatType;
import repast.simphony.data2.ConsoleDataSink.OutputStream;

/**
 * Sink for outputting text data to the console.
 * 
 * @author Nick Collier
 */
public class ConsoleSinkDescriptor extends AbstractTextSinkDescriptor {

  private boolean enabled = true;
  private ConsoleDataSink.OutputStream outputStream = OutputStream.OUT;

  /**
   * Creates a ConsoleSinkDescriptor.
   * 
   * @param name
   */
  public ConsoleSinkDescriptor(String name) {
    super(name);
    format = FormatType.LINE;
  }

  /**
   * @return the enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param enabled
   *          the enabled to set
   */
  public void setEnabled(boolean enabled) {
    if (this.enabled != enabled) {
      this.enabled = enabled;
      scs.fireScenarioChanged(this, "enabled");
    }
  }

  /**
   * @return the outputStream
   */
  public ConsoleDataSink.OutputStream getOutputStream() {
    return outputStream;
  }

  /**
   * @param outputStream
   *          the outputStream to set
   */
  public void setOutputStream(ConsoleDataSink.OutputStream outputStream) {
    if (this.outputStream != outputStream) {
      this.outputStream = outputStream;
      scs.fireScenarioChanged(this, "outputStream");
    }
  }

}
