package repast.simphony.parameter;

import org.apache.commons.lang.math.Range;

import java.util.List;

/**
 * Encapsulates the details about a particular individual parameter.
 * 
 * @author Nick Collier
 */
public interface ParameterSchema {

  /**
   * Gets the type of the parameter.
   * 
   * @return the type of the parameter.
   */
  Class getType();

  /**
   * Gets the default value of the parameter.
   * 
   * @return the default value of the parameter.
   */
  Object getDefaultValue();

  /**
   * Sets the default value of this parameter.
   * 
   * @param obj
   *          the new default value
   */
  void setDefaultValue(Object obj);

  /**
   * Gets the numeric range of values that constrain this parameter. This will
   * return null if the parameter is unconstrained by a numeric range.
   * 
   * @return the numeric range of values that constrain this parameter or null
   *         if the parameter is unconstrained by a range.
   */
  Range getConstrainingRange();

  /**
   * Gets the list of values that constrain this parameter. This will return
   * null if the parameter is unconstrained by a list.
   * 
   * @return the list of values that constrain this parameter or null if the
   *         parameter is unconstrained by a list of possible values.
   */
  List getConstrainingList();

  /**
   * Gets the string representation of the values or range that constrain this
   * parameter This will return an empty string if the parameter is not
   * constrained (by a list or range)
   * 
   * @return the string representation of the parameter constraints, if
   *         unconstrained an empty string
   */
  String getConstraintString();

  /**
   * Validates the specified object against the type and optional constraint
   * info for the parameter name.
   * 
   * @param obj
   *          the object to validate
   * @return true if the object is of a type valid for the parameter, otherwise
   *         false.
   */
  boolean validate(Object obj);

  /**
   * Gets the name of the parameter that this is the schema for.
   * 
   * @return the name of the parameter that this is the schema for.
   */
  String getName();

  /**
   * Converts the object into a String representation.
   * 
   * @param obj
   *          the object to convert
   * @return the String representation.
   * 
   */
  String toString(Object obj);

  /**
   * Creates an Object from the String representation.
   * 
   * @param str
   *          the String representation
   * @return the created Object
   */
  Object fromString(String str);

  /**
   * Gets the convertor, if any, for this ParameterSchema.
   * 
   * @return the convertor, or null if there is no covertor for this
   *         ParameterSchema.
   */
  StringConverter getConverter();
}
