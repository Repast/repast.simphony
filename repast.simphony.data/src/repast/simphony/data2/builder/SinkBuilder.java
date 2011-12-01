/**
 * 
 */
package repast.simphony.data2.builder;

import java.util.Collection;

import repast.simphony.data2.DataSink;
import repast.simphony.data2.DataSource;

/**
 * Inteface for classes that can build DataSinks.
 * 
 * @author Nick Collier
 */
public interface SinkBuilder {
  
  /**
   * Creates and returns a DataSink.
   * 
   * @param sources the DataSources the that will feed the created DataSink
   * 
   * @return the created DataSink.
   */
  DataSink create(Collection<? extends DataSource> sources);

}
