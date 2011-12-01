package repast.simphony.parameter;

/**
 * Maps parameter names to a type.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface Schema {

  /**
   * Gets the schema for the specified parameter.
   * 
   * @param paramName
   *          the name of the parameter
   * @return the schema for the specified parameter
   */
  ParameterSchema getDetails(String paramName);

  /**
   * Gets an iterable over a list of parameter names.
   * 
   * @return an iterable over a list of parameter names.
   */
  Iterable<String> parameterNames();

  /**
   * Validates the specified object against the type info contained for the
   * parameter name.
   * 
   * @param paramName
   *          the name of the parameter
   * @param obj
   *          the object to validate
   * @return true if the object is of a type valid for the parameter, otherwise
   *         false.
   */
  boolean validate(String paramName, Object obj);

  /**
   * Returns true if this schema contains contains the specified parameter name,
   * otherwise false.
   * 
   * @param paramName
   * @return true if this schema contains contains the specified parameter name,
   *         otherwise false.
   */
  boolean contains(String paramName);

  /**
   * Gets the number of parameters in this schema.
   * 
   * @return the number of parameters in this schema.
   */
  int size();
}
