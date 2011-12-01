/**
 * 
 */
package repast.simphony.data2;

import java.io.PrintStream;
import java.util.List;

/**
 * 
 * @author Nick Collier
 */
public class ConsoleDataSink implements DataSink {

  public enum OutputStream {
    OUT, ERR
  }

  private PrintStream stream;
  private Formatter formatter;

  public ConsoleDataSink(OutputStream outputStream, Formatter formatter) {
    this.formatter = formatter;
    if (outputStream == OutputStream.OUT)
      stream = System.out;
    else
      stream = System.err;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#open()
   */
  @Override
  public void open(List<String> sourceIds) {
    String header = formatter.getHeader();
    if (header.length() > 0)
      stream.println(header);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#flush()
   */
  @Override
  public void flush() {
    stream.flush();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#rowStarted()
   */
  @Override
  public void rowStarted() {
    formatter.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#append(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public void append(String key, Object value) {
    formatter.addData(key, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#rowEnded()
   */
  @Override
  public void rowEnded() {
    stream.println(formatter.formatData());
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#recordEnded()
   */
  @Override
  public void recordEnded() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#close()
   */
  @Override
  public void close() {
    stream.flush();
  }
}
