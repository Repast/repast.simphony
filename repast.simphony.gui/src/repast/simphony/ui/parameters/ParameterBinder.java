/**
 * 
 */
package repast.simphony.ui.parameters;

import javax.swing.JComponent;

import repast.simphony.parameter.Parameters;

/**
 * Interface for classes that can create a JComponent for a particular parameter
 * and "bind" that component to a parameter.
 * 
 * @author Nick Collier
 */
public interface ParameterBinder {

  /**
   * Gets the name of the parameter for which this is the binder.
   * 
   * @return the name of the parameter for which this is the binder.
   */
  String getName();

  /**
   * Creates a JComponent for a parameter in the parameters.
   * 
   * @param params
   * 
   * @return the created JComponent.
   */
  JComponent getComponent(Parameters params);

  /**
   * Gets the label for the component created by this CompCreator.
   * 
   * @return the label for the component created by this CompCreator.
   */
  String getLabel();

  /**
   * Updates the parameter that this is a binder for with the latest value from
   * the created component.
   * 
   */
  void toParameter();

  /**
   * Resets the JComponent to the default value of its parameter.
   */
  void resetToDefault();

  /**
   * Gets the parameter xml representation of this ParameterBinder.
   * 
   * @return the parameter xml representation of this ParameterBinder.
   */
  String toXML();
  
  /**
   * Sets the order of the created component in the display.
   * 
   * @param val
   */
  void setDisplayOrder(double val);
 
  /**
   * Gets the order of the created component in the display.
   */
  double getDisplayOrder();
}
