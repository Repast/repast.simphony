/**
 * 
 */
package repast.simphony.data2.builder;

import java.util.Collection;

import repast.simphony.data2.DataSink;
import repast.simphony.data2.DataSource;

/**
 * Adapts an already created DataSink to the the SinkBuilder
 * interface.
 * 
 * @author Nick Collier
 */
public class DummySinkBuilder implements SinkBuilder {
  
  private DataSink sink;
  
  public DummySinkBuilder(DataSink sink) {
    this.sink = sink;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.builder.SinkBuilder#create(java.util.Collection)
   */
  @Override
  public DataSink create(Collection<? extends DataSource> sources) {
    return sink;
  }
}
