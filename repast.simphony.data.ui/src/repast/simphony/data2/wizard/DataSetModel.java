/**
 * 
 */
package repast.simphony.data2.wizard;

import java.util.List;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.engine.schedule.Descriptor;

/**
 * Interface for wizard models that provides access to datasets.
 * 
 * @author Nick Collier
 */
public interface DataSetModel<T extends Descriptor> {
  
  /**
   * Gets the descriptor.
   * 
   * @return
   */
  T getDescriptor();
  
  /**
   * Gets the datasets.
   * 
   * @return
   */
  List<DataSetDescriptor> getDataSets();

}
