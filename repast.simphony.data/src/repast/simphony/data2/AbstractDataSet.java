/**
 * 
 */
package repast.simphony.data2;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract DataSet implementation.
 * 
 * @author Nick Collier
 * @param <T>
 */
public abstract class AbstractDataSet implements DataSet {

  protected List<DataSink> sinks = new ArrayList<DataSink>();
  protected String id;

  public AbstractDataSet(String id) {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSet#getId()
   */
  @Override
  public String getId() {
    return id;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSet#sinks()
   */
  @Override
  public Iterable<DataSink> sinks() {
    return sinks;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSet#init()
   */
  @Override
  public void init() {
    List<String> sourceIds = getSourceIds();
    for (DataSink sink : sinks) {
      sink.open(sourceIds);
    }
  }

  protected abstract List<String> getSourceIds();
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSet#flush()
   */
  @Override
  public void flush() {
    for (DataSink sink : sinks) {
      sink.flush();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSet#close()
   */
  @Override
  public void close() {
    for (DataSink sink : sinks) {
      sink.close();
    }
  }
}
