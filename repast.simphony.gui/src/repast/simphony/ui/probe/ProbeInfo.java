/**
 * 
 */
package repast.simphony.ui.probe;

import java.beans.PropertyDescriptor;

/**
 * Bundles together the PropertyDescriptors used to
 * get and set the probed properties for a particular class.  
 * 
 * @author Nick Collier
 */
public interface ProbeInfo {
  
  /**
   * Gets the class that this is the ProbeInfo for. 
   * 
   * @return the class that this is the ProbeInfo for. 
   */
  Class<?> getProbedClass();
  
  /**
   * Gets the PropertyDescriptor for the ID property for 
   * the class. This may be null if such a property descriptor doesn't exist.
   * 
   * @return the ID property descriptor or null if one doesn't exist.
   */
  PropertyDescriptor getIDProperty();
  
  /**
   * Gets an iterable over the MethodPropertyDescriptors for 
   * the properties in this ProbeInfo. 
   * 
   * @return an iterable over the MethodPropertyDescriptors for 
   * the properties in this ProbeInfo.
   */
  Iterable<MethodPropertyDescriptor> methodPropertyDescriptors();
  
  /**
   * Gets an iterable over the FieldPropertyDescriptors in this ProbeInfo.
   * 
   * @return an iterable over the FieldPropertyDescriptors in this ProbeInfo.
   */
  Iterable<FieldPropertyDescriptor> fieldPropertyDescriptor();

}
