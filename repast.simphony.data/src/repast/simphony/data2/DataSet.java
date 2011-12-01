/**
 * 
 */
package repast.simphony.data2;

import java.util.Map;

/**
 * Records data from DataSources and passes that data to DataSinks.
 * 
 * @author Nick Collier
 */
public interface DataSet {
  
  /**
   * Gets the id of this DataSet.
   * 
   * @return the id of this DataSet.
   */
  String getId();

  /**
   * Gets the type of object that this dataset will collect data from.
   * 
   * @return
   */
  Iterable<Class<?>> getSourceTypes();
  
  /**
   * Gets the sinks associated with this DataSet. 
   * 
   * @return the sinks associated with this DataSet. 
   */
  Iterable<DataSink> sinks();

  /**
   * Initializes this DataSet.
   */
  void init();

  /**
   * Records data from the specified ObjectListMap of objects.
   * 
   * @param objs
   *          the objs to record the data from
   */
  void record(Map<Class<?>, SizedIterable<?>> objs);
  
  /**
   * Notifies the sinks associated with this DataSet to
   * flush their buffered data (if any).
   */
  void flush();

  /**
   * Closes this DataSet.
   */
  void close();

}
