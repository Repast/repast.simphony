/*CopyrightHere*/
package repast.simphony.parameter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.util.ClassUtilities;

/**
 * Default implementation of Parameters. Individual paramters are created via
 * the add method. This is intended to be used in conjuction with a
 * ParametersCreator.
 * 
 * @author Nick Collier
 */
public class DefaultParameters implements MutableParameters {

  protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  protected Map<String, Object> values = new HashMap<String, Object>();
  protected Map<String, String> nameMap = new HashMap<String, String>();
  protected DefaultSchema schema = new DefaultSchema();
  protected Set<String> readOnly = new HashSet<String>();
  protected boolean changed;

  /**
   * Creates a new DefaultParameters object.
   */
  public DefaultParameters() {
  }

  /**
   * Creates a new DefaultParameters using the parameters in the specified
   * Parameters object.
   * 
   * @param param
   *          parameters whose values we want to copy into this
   *          DefaultParameters
   */
  public DefaultParameters(Parameters params) {
    Schema schema = params.getSchema();
    for (String name : schema.parameterNames()) {
      ParameterSchema details = schema.getDetails(name);
      String displayName = params.getDisplayName(name);
      if (displayName == null)
        displayName = name;
      Object defaultValue = params.getSchema().getDetails(name).getDefaultValue();
      addParameter(name, displayName, details.getType(), defaultValue, params.isReadOnly(name));
      if (params.getValue(name) != Parameters.NULL)
        setValue(name, params.getValue(name));
      if (details.getConstrainingList() != null)
        addConstraint(name, details.getConstrainingList());
      if (details.getConstrainingRange() != null)
        addConstraint(name, details.getConstrainingRange());
      if (details.getConverter() != null)
        addConvertor(name, details.getConverter());
    }
  }

  public Parameters clone() {
    try {
      return (Parameters) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }

  }
  
  /**
   * Adds listener that will listen for parameter changes
   * on this Parameters.
   * 
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }
  
  
  /**
   * Removes the specified listener from the
   * listeners listening to this Parameters object.
   * 
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  /**
   * Removes the named parameter
   * 
   * @param name
   *          the name of the parameter
   * @return whether or not removal was successful
   */
  public boolean removeParameter(String name) {
    boolean retval = schema.removeEntry(name);
    if (retval) {
      values.remove(name);
      nameMap.remove(name);
    }
    return retval;
  }

  /**
   * Adds a parameter to this Parameters.
   * 
   * @param name
   *          the name of the parameter
   * @param displayName
   *          the display name for the parameter
   * @param type
   *          the type of the parameter
   * @param value
   *          the default value of the parameter
   * @param isReadOnly
   *          whether or not the parameter is read only.
   */
  public void addParameter(String name, String displayName, Class type, Object value,
      boolean isReadOnly) {
    addParameter(name, type, value, isReadOnly);
    nameMap.put(name, displayName);
  }

  /**
   * Constains the value of the named to parameter to those in the list.
   * 
   * @param name
   *          the name of the parameter
   * @param constrainingList
   *          a list containing the possible values for the named parameter
   */
  public void addConstraint(String name, List constrainingList) {
    schema.addConstraint(name, constrainingList);
  }

  /**
   * Adds a converter to convert the named parameter back and forth to a String.
   * 
   * @param name
   *          the name of the parameter
   * @param converter
   *          the converter
   */
  public void addConvertor(String name, StringConverter converter) {
    schema.addConvertor(name, converter);
  }

  /**
   * Constrains the value of the named parameter to those in the specified
   * range.
   * 
   * @param name
   *          the name of the parmaeter
   * @param constrainingRange
   *          a range of the possible values for the named parameter
   */
  public void addConstraint(String name, SteppedRange constrainingRange) {
    schema.addConstraint(name, constrainingRange);
  }

  /**
   * Adds a parameter to this Parameters.
   * 
   * @param name
   *          the name of the parameter
   * @param type
   *          the type of the parameter
   * @param value
   *          the default value of the parameter
   * @param isReadOnly
   *          whether or not the parameter is read only.
   */
  protected void addParameter(String name, Class type, Object value, boolean isReadOnly) {
    if (value == null) {
      value = Parameters.NULL;
    }
    schema.addEntry(name, type, value);
    values.put(name, value);
    if (isReadOnly)
      readOnly.add(name);
    nameMap.put(name, name);
  }

  /**
   * Gets the Schema for this Parameters object.
   * 
   * @return the Schema for this Parameters object.
   */
  public Schema getSchema() {
    return schema;
  }

  /**
   * Gets the value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public Object getValue(String paramName) {
    if (schema.contains(paramName)) {
      return values.get(paramName);
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not found in the schema.");
    }
  }

  /**
   * Gets the Double value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public Double getDouble(String paramName) {
    Object value = getValue(paramName);
    Class<?> paramType = schema.getDetails(paramName).getType();
    if (paramType == Double.class || paramType == double.class) {
      return (Double) value;
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not of double type.");
    }
  }

  /**
   * Gets the Integer value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public Integer getInteger(String paramName) {
    Object value = getValue(paramName);
    Class<?> paramType = schema.getDetails(paramName).getType();
    if (paramType == Integer.class || paramType == int.class) {
      return (Integer) value;
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not of integer type.");
    }
  }

  /**
   * Gets the Boolean value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public Boolean getBoolean(String paramName) {
    Object value = getValue(paramName);
    Class<?> paramType = schema.getDetails(paramName).getType();
    if (paramType == Boolean.class || paramType == boolean.class) {
      return (Boolean) value;
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not of boolean type.");
    }
  }

  /**
   * Gets the String value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public String getString(String paramName) {
    Object value = getValue(paramName);
    Class<?> paramType = schema.getDetails(paramName).getType();
    if (paramType == String.class) {
      return (String) value;
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not of String type.");
    }
  }

  /**
   * Gets the Boolean value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public Long getLong(String paramName) {
    Object value = getValue(paramName);
    Class<?> paramType = schema.getDetails(paramName).getType();
    if (paramType == Long.class || paramType == long.class) {
      return (Long) value;
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not of long type.");
    }
  }

  /**
   * Gets the Float value associated with the specified parameter name.
   * 
   * @param paramName
   *          the name of the parameter whose value we want
   * @return the value associated with the specified parameter name.
   */
  public Float getFloat(String paramName) {
    Object value = getValue(paramName);
    Class<?> paramType = schema.getDetails(paramName).getType();
    if (paramType == Float.class || paramType == float.class) {
      return (Float) value;
    } else {
      throw new IllegalParameterException("Parameter '" + paramName + "' not of float type.");
    }
  }

  /**
   * Gets a String representation of the specified parameter's value.
   * 
   * @param paramName
   *          the name of the parameter
   * @return a String representation of the specified parameters's value.
   */
  public String getValueAsString(String paramName) {
    Object obj = getValue(paramName);
    return obj.equals(Parameters.NULL) ? "null" : schema.getDetails(paramName).toString(obj);
  }

  /**
   * True if parameter is read only.
   * 
   * @param paramName
   *          the name of the parameter
   * @return true if parameter is read-only otherwise false.
   */
  public boolean isReadOnly(String paramName) {
    return readOnly.contains(paramName);
  }

  /**
   * Sets the specified parameter name to the specified value.
   * 
   * @param paramName
   *          the name of the parameter to set to the new value
   * @param val
   *          the new value
   */
  public void setValue(String paramName, Object val) {
    ParameterSchema details = schema.getDetails(paramName);
    if (val.getClass().equals(String.class) && !details.getType().equals(String.class)) {
      Object tmpVal = details.fromString((String) val);
      if (tmpVal != null)
        val = tmpVal;
    }

    
    Object oldVal;
    Object newVal = coerce(paramName, val);
    if (schema.validate(paramName, newVal)) {
      oldVal = values.get(paramName);
      values.put(paramName, newVal);
      changed = true;
    } else
      throw new IllegalParameterException("Schema violation when setting parameter '" + paramName
          + "' to " + val);
    
    pcs.firePropertyChange(paramName, oldVal, val);
  }

  private Object coerce(String paramName, Object obj) {
    Class<? extends Object> objClass = obj.getClass();
    Class<?> type = getSchema().getDetails(paramName).getType();
    if (ClassUtilities.isNumericType(type) && Number.class.isAssignableFrom(objClass)) {

      if (type.equals(Double.class) || type.equals(double.class)) {
        return ((Number) obj).doubleValue();
      }

      if (type.equals(Float.class) || type.equals(float.class)) {
        if (objClass.equals(Double.class) || objClass.equals(Long.class))
          return obj;
        return ((Number) obj).floatValue();
      }

      if (type.equals(Long.class) || type.equals(long.class)) {
        if (objClass.equals(float.class) || objClass.equals(double.class))
          return obj;
        return ((Number) obj).longValue();
      }

      if (type.equals(Integer.class) || type.equals(int.class)) {
        if (objClass.equals(Integer.class) || objClass.equals(Short.class)
            || objClass.equals(Byte.class))
          return ((Number) obj).intValue();
        return obj;
      }
    }
    return obj;
  }

  /**
   * Gets the display name for the specified parameter name.
   * 
   * @param paramName
   *          the parameter name
   * @return the display name for the specified parameter name.
   */
  public String getDisplayName(String paramName) {
    return nameMap.get(paramName);
  }
}
