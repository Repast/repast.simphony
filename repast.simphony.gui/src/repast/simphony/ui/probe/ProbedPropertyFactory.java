package repast.simphony.ui.probe;

import repast.simphony.util.ClassUtilities;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * Creates ProbedProperties for use in the ProbableBeanCreator.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProbedPropertyFactory {

  /**
   * Creates a ProbedProperty using the specified PropertyDescriptor.
   * 
   * @param desc
   * @param wrap
   *          whether or not to wrap with a sparkline button
   * @return the created ProbedProperty.
   */
  public static AbstractProbedProperty createProbedProperty(PropertyDescriptor desc, boolean wrap) {
    Class<?> propType = desc.getPropertyType();
    if (ClassUtilities.isNumericType(propType))
      return new NumericProbedProperty(desc, wrap);
    if (propType.equals(String.class))
      return new StringProbedProperty(desc);
    if (propType.equals(Boolean.class) || propType.equals(boolean.class))
      return new BooleanProbedProperty(desc);

    return null;
  }

  /**
   * Creates a ProbedProperty using the specified PropertyDescriptor.
   * 
   * @param desc
   *          the propert descriptor
   * @param possibleValues
   *          a list of possible values for the property
   * @return the created ProbedProperty.
   */
  public static AbstractProbedProperty createProbedProperty(PropertyDescriptor desc,
      List<?> possibleValues, boolean wrap) {
    Class<?> propType = desc.getPropertyType();
    if (ClassUtilities.isNumericType(propType))
      return new NumericProbedProperty(desc, possibleValues, wrap);
    if (propType.equals(String.class))
      return new StringProbedProperty(desc, possibleValues);
    if (propType.equals(Boolean.class) || propType.equals(boolean.class))
      return new BooleanProbedProperty(desc);

    return null;
  }
}
