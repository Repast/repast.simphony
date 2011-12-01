package repast.simphony.parameter;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.util.ClassUtilities;

/**
 * Utility methods for working with Parameters.
 *
 * @author Nick Collier
 */
public class ParameterUtils {

  /**
   * Parses and converts string representations of primitive values, their Object
   * representations (ie. int and Integer) and Strings into an array. If the val parameter consists
   * of a single string then the array will contain a single element. If the val parameter
   * consists of more than one space delimited elements then the array will contain that many
   * elements.
   *
   * @param type the type of the object
   * @param name the name of the parameter
   * @param val  the list of string values to convert
   * @return an array of the converted values.
   * @throws ParameterFormatException if there is a parsing or converting error
   * @throws IllegalArgumentException if the type is not a primitive, a primitive Object or a String.
   */
  public static Object[] parseDefaultValue(Class type, String name, String val) throws ParameterFormatException {
    if (val == null || val.trim().length() == 0) {
      if (ClassUtilities.isNumericType(type)) val = "0";
      else if (type.equals(String.class)) val = "";
      else if (type.equals(Boolean.class) || type.equals(boolean.class)) val = "false";
    }
    if (type.equals(int.class) || type.equals(Integer.class)) return ListTokenizer.parseIntValues(val, name);
    if (type.equals(double.class) || type.equals(Double.class)) return ListTokenizer.parseDoubleValues(val, name);
    if (type.equals(long.class) || type.equals(Long.class)) return ListTokenizer.parseLongValues(val, name);
    if (type.equals(float.class) || type.equals(Float.class)) return ListTokenizer.parseFloatValues(val, name);
    if (type.equals(boolean.class) || type.equals(Boolean.class)) return ListTokenizer.parseBooleanValues(val, name);
    if (type.equals(byte.class) || type.equals(Byte.class)) return ListTokenizer.parseByteValues(val, name);
    if (type.equals(short.class) || type.equals(Short.class)) return ListTokenizer.parseShortValues(val, name);
    if (type.equals(String.class)) return ListTokenizer.parseStringValues(val);
    throw new IllegalArgumentException("invalid type");
  }
  
  /**
   * Converts a list of object values to the string list representation
   * 
   */
  public static String getStringValue(Class type, StringConverter converter, List values) {
	  StringBuilder retval = new StringBuilder();
	  if (values != null && values.size() > 0) {
		  List<String> strVals = new ArrayList<String>(values.size());
		  boolean valContainsSpace = false;
		  for (Object o : values) {
			  String s;
			  if (converter != null) {
				  s = converter.toString(o);
			  } else {
				  s =o.toString();
			  }
			  strVals.add(s);
			  valContainsSpace |= s.indexOf(' ') != -1;
		  }
		  
		  //now build the String
		  for (String s : strVals) {
			  if (valContainsSpace) {
				  retval.append('\'');
			  }
			  retval.append(s);
			  if (valContainsSpace) {
				  retval.append('\'');
			  }
			  retval.append(' ');
		  }
	  
	  }
	  return retval.toString();
  }
}
