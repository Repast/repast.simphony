/**
 * 
 */
package repast.simphony.data2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry that allows various parts of the data collection system to
 * be shared among controller actions.
 * 
 * @author Nick Collier
 */
public class DataSetRegistry {
  
  private Map<Object, DataSetManager> map = new HashMap<Object, DataSetManager>();
  private List<FileDataSink> fileSinks = new ArrayList<FileDataSink>();
  
  /**
   * Adds a DataSetManager for the specified context. 
   * 
   * @param contextId
   * @param manager
   */
  public void addDataSetManager(Object contextId, DataSetManager manager) {
    map.put(contextId, manager);
  }
  
  /**
   * Gets the DataSetManager associated with the specified context.
   * 
   * @param contextId
   * 
   * @return the DataSetManager associated with the specified context.
   */
  public DataSetManager getDataSetManager(Object contextId) {
    return map.get(contextId);
  }
  
  /**
   * Adds a FileDataSink to this DataSetRegistry.
   * 
   * @param sink
   */
  public void addFileDataSink(FileDataSink sink) {
    fileSinks.add(sink);
  }
  
  /**
   * Gets all the FileDataSinks that have been added to this
   * DataSetRegistry.
   * 
   * @return all the FileDataSinks that have been added to this
   * DataSetRegistry.
   */
  public Iterable<FileDataSink> fileSinks() {
    return fileSinks;
  }

}
