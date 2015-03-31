package repast.simphony.ui.probe;

import repast.simphony.util.ClassUtilities;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Creates ProbedProperties for use in the ProbableBeanCreator.
 * 
 * @author Nick Collier
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
  public static DefaultProbedPropertyUICreator createProbedProperty(PropertyDescriptor desc, boolean wrap) {
    Class<?> propType = desc.getPropertyType();
    if (ClassUtilities.isNumericType(propType))
      return new NumericProbedProperty(desc, wrap);
    else if (propType.equals(String.class) || 
        (desc instanceof MethodPropertyDescriptor && ((MethodPropertyDescriptor)desc).getStringConverter() != null))
      return new StringProbedProperty(desc);
    else if (propType.equals(Boolean.class) || propType.equals(boolean.class))
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
  public static DefaultProbedPropertyUICreator createProbedProperty(PropertyDescriptor desc,
      List<Object> possibleValues, boolean wrap) {
    Class<?> propType = desc.getPropertyType();
    if (ClassUtilities.isNumericType(propType))
      return new NumericProbedProperty(desc, possibleValues, wrap);
    if (propType.equals(String.class) ||
        (desc instanceof MethodPropertyDescriptor && ((MethodPropertyDescriptor)desc).getStringConverter() != null))
      return new StringProbedProperty(desc, possibleValues);
    if (propType.equals(Boolean.class) || propType.equals(boolean.class))
      return new BooleanProbedProperty(desc);

    return null;
  }
  
  /**
   * Creates a title for the specified target using ProbeInof.getIDProperty().
   * 
   * @param pbInfo
   * @param target
   * @return
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  public static String createProbedTitle(ProbeInfo pbInfo, Object target) throws IllegalAccessException, IllegalArgumentException, 
  InvocationTargetException {
  
  String title;
  if ( pbInfo.getIDProperty() == null) {
    title = target.toString();
    if (title.lastIndexOf('.') > 0) {
      title = title.substring(title.lastIndexOf(".") + 1, title.length());
    }
  } else {
    title = (String) pbInfo
        .getIDProperty().getReadMethod().invoke(target, new Object[0]);
  }
  return title;
}

}
