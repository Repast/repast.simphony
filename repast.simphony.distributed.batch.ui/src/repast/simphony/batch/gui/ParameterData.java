/**
 * 
 */
package repast.simphony.batch.gui;

/**
 * Interface for classes that carry ParameterData.
 * 
 * @author Nick Collier
 */
public interface ParameterData {
  
  /**
   * Gets the name of the parameter.
   * 
   * @return the name of the parameter.
   */
  String getName();
  
  /**
   * Gets the specified attribute of the Parameter.
   * 
   * @param attributeName
   * 
   * @return the specified attribute of the Parameter.
   */
  String getAttribute(ParameterAttribute attribute);
  
  /**
   * Gets the parameter type.
   * 
   * @return the parameter type.
   */
  ParameterType getType();

}
