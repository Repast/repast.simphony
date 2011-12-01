/**
 * 
 */
package repast.simphony.data2;

import java.util.Collection;

/**
 * Map type interface for the type passed to DataSets for recording. 
 * 
 * @author Nick Collier
 */
public interface ObjectListMap {
  
  /**
   * Gets the collection of objects in this map of the specified type. 
   * 
   * @param type
   * 
   * @return the collection of objects in this map of the specified type. 
   */
  Collection<?> getObjects(Class<?> type);

}
