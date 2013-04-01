/*CopyrightHere*/
package repast.simphony.parameter;

/**
 * Encapsulates simulation time model parameters.
 * 
 * @author Nick Collier
 */
public interface Parameters extends Cloneable{

  static final Object NULL = new Object();

  /**
   * Gets the Schema for this Parameters object.
   * 
   * @return the Schema for this Parameters object.
   */
  Schema getSchema();
  
  /**
   * Method to clone a Parameter object.
   * @return a Parameter clone.
   */
  public Parameters clone();

  /**
   * Gets the value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  Object getValue(String paramName);
  
  /**
   * Gets the Double value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  Double getDouble(String paramName);

  /**
   * Gets the Integer value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  Integer getInteger(String paramName);
  
  /**
   * Gets the Boolean value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  Boolean getBoolean(String paramName);
  
  /**
   * Gets the String value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  String getString(String paramName);
  
  /**
   * Gets the Long value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  Long getLong(String paramName);
  
  /**
   * Gets the Float value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * 
   * @return the value associated with the specified parameter name.
   */
  Float getFloat(String paramName);
  
  /**
   * Gets a String representation of the specified parameter's value.
   * 
   * @param paramName
   *          the name of the parameter
   * 
   * @return a String representation of the specified parameters's value.
   */
  String getValueAsString(String paramName);

  /**
   * Sets the specified parameter name to the specified value.
   * 
   * @param paramName
   *          the name of the parameter to set to the new value
   * @param val
   *          the new value
   */
  void setValue(String paramName, Object val);

  /**
   * True if parameter is read only.
   * 
   * @param paramName
   *          the name of the parameter
   * @return true if parameter is read-only otherwise false.
   */
  boolean isReadOnly(String paramName);

  /**
   * Gets the display name for the specified parameter name.
   * 
   * @param paramName
   *          the parameter name
   * @return the display name for the specified parameter name.
   */
  String getDisplayName(String paramName);
}
