package repast.simphony.parameter;

/**
 * Type info combined with parameter related methods.
 *
 * @author Nick Collier
 */
public interface ParameterType<T> {

  /**
   * Gets the Java class associated with this type.
   *
   * @return the Java class associated with this type.
   */
  Class<T> getJavaClass();

  /**
   * Gets an initial or default value for a parameter of this type from
   * the string. If the string is space separated list of
   * values the first value will be used.
   *
   * @param val a string representation of default value
   * @return the default value of the string.
   * @throws ParameterFormatException if string cannot be converted
   *                                  into an object of the appropriate type
   */
  T getValue(String val) throws ParameterFormatException;

  /**
   * Gets a StringConverter that can be used to convert
   * objects of this ParameterType to and from strings.
   *
   * @return a StringConverter for objects of this parameter type.
   */
  StringConverter<T> getConverter();

}
