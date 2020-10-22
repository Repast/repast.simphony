package repast.simphony.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Range;

/**
 * Default implementation of ParameterSchema.
 *
 * @author Nick Collier
 */
public class DefaultParameterSchema implements ParameterSchema {

  static Map<Class, Class> primitives = new HashMap<Class, Class>();
  private boolean isANumber;

  static {
    primitives.put(int.class, Integer.class);
    primitives.put(long.class, Long.class);
    primitives.put(float.class, Float.class);
    primitives.put(double.class, Double.class);
    primitives.put(byte.class, Byte.class);
    primitives.put(short.class, Short.class);
    primitives.put(boolean.class, Boolean.class);
  }


  private List list;
  private SteppedRange range;
  private Class type;
  private Object defValue;
  private String name;
  private StringConverter converter;

  public DefaultParameterSchema(ParameterSchema schema) {
    this(schema.getName(), schema.getType(), schema.getDefaultValue());
    range = schema.getConstrainingRange();
    list = schema.getConstrainingList();
    converter = schema.getConverter();
  }

  public DefaultParameterSchema(String name, Class type, Object defValue) {
    this.type = type;
    this.defValue = defValue;
    this.name = name;
    isANumber = Number.class.isAssignableFrom(type) || primitives.containsKey(type);
    converter = StringConverterFactory.instance().getConverter(type);
  }

  public DefaultParameterSchema(String name, Class type, Object defValue, List possibleValues) {
    this(name, type, defValue);
    setConstrainingList(possibleValues);
  }

  public DefaultParameterSchema(String name, Class type, Object defValue, SteppedRange constrainingRange) {
    this(name, type, defValue);
    setConstrainingRange(constrainingRange);
  }

  /**
   * Gets the StringConverter if any.
   *
   * @return the StringConverter
   */
  public StringConverter getConverter() {
    return converter;
  }


  /**
   * Sets the converter for this schema.
   *
   * @param converter the converter
   */
  public void setConvertor(StringConverter converter) {
    this.converter = converter;
  }

  /**
   * Gets the name of the parameter that this is the schema for.
   *
   * @return the name of the parameter that this is the schema for.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the type of the parameter.
   *
   * @return the type of the parameter.
   */
  public Class getType() {
    return type;
  }

  /**
   * Gets the default value of the parameter.
   *
   * @return the default value of the parameter.
   */
  public Object getDefaultValue() {
    return defValue;
  }


  /**
   * Sets the default value of this parameter.
   *
   * @param obj the new default value
   */
  public void setDefaultValue(Object obj) {
    if (obj == null) obj = Parameters.NULL;
    if (!defValue.equals(obj)) {
      if (validate(obj)) {
        defValue = obj;
      } else {
        throw new IllegalParameterException("Schema violation when setting default value of parameter '" + name +
                "' to " + obj);
      }
    }
  }


  /**
   * Sets the constraining list.
   *
   * @param list the constraining list
   */
  public void setConstrainingList(List list) {
    this.list = list;
    if (defValue != null && !validate(defValue)) {
      throw new IllegalParameterException("Constraining list '" + list + "' for parameter '" +
              name + "' does not contain default value '" + defValue + "'");
    }
  }

  /**
   * Sets the constraining range.
   *
   * @param range the constraining range.
   */
  public void setConstrainingRange(SteppedRange range) {
    this.range = range;
    if (defValue != null && !validate(defValue)) {
      throw new IllegalParameterException("Constraining range '" + range + "' for parameter '" +
              name + "' does not contain default value '" + defValue + "'");
    }
  }

  /**
   * Gets the numeric range of values that constrain this parameter.
   * This will return null if the parameter is unconstrained
   * by a numeric range.
   *
   * @return the numeric range of values that constrain this parameter or null
   *         if the parameter is unconstrained by a range.
   */
  public SteppedRange getConstrainingRange() {
    return range;
  }

  /**
   * Gets the list of values that constrain this parameter.
   * This will return null if the parameter is unconstrained by a list.
   *
   * @return the list of values that constrain this parameter or null
   *         if the parameter is unconstrained by a list of possible values.
   */
  public List getConstrainingList() {
    return list == null ? null : new ArrayList(list);
  }

  /**
   * Gets the string representation of the values or range that constrain this parameter
   * This will return an empty string if the parameter is not constrained (by a list or range)
   * @return the string representation of the parameter constraints, if unconstrained an empty string
   */
  public String getConstraintString() {
	  String val = "";
	  if (list != null) {
		  val = ParameterUtils.getStringValue(type, converter, list);
	  } else if (range != null) {
		  val = range.getMin() + " " + range.getMax()  + " " + range.getStep();
	  }
	  
	  return val;
  }
  /**
   * Validates the specified object against the type and optional constraint
   * info for the parameter name.
   *
   * @param obj the object to validate
   * @return true if the object is of a type valid for the parameter, otherwise false.
   */
  public boolean validate(Object obj) {
    Class<? extends Object> setType = obj.getClass();
    Class testingType = primitives.get(type);
    if (testingType == null) testingType = type;
    if (testingType.isAssignableFrom(setType)) {
      if (isANumber) {
        if (range != null) return range.contains(((Number) obj).doubleValue());
      }

      if (list != null) return list.contains(obj);
      return true;
    }

    return false;
  }

  public String toString(Object obj) {
    return converter == null ? obj.toString() : converter.toString(obj);
  }

  public Object fromString(String str) {
    return converter == null ? null : converter.fromString(str);
  }
}
