package repast.simphony.parameter;

import org.apache.commons.lang3.Range;
import repast.simphony.util.collections.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of Schema.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DefaultSchema implements Schema {

  private Map<String, ParameterSchema> schemas = new HashMap<String, ParameterSchema>();

  /**
   * Creates an empty Schema.
   */
  public DefaultSchema() {

  }

  /**
   * Creates a schema from an array of String, Class pairs.
   *
   * @param pairs first item in pair is parameter name, second is parameter type
   */
  public DefaultSchema(Pair<String, Class>[] pairs) {
    for (Pair<String, Class> pair : pairs) {
      schemas.put(pair.getFirst(), new DefaultParameterSchema(pair.getFirst(), pair.getSecond(), null));
    }
  }

  /**
   * Removes a schema entry from this Schema
   * 
   * @param name the name of the parameter
   * @return whether or not the removal was successful
   */
  public boolean removeEntry(String name) {
	  ParameterSchema ps = schemas.remove(name);
	  
	  return ps != null;
  }
  /**
   * Adds a schema entry to this Schema.
   *
   * @param name     the name of the parameter
   * @param type     the type of the parameter
   * @param defValue the default value of hte parameter.
   */
  public void addEntry(String name, Class type, Object defValue) {
    // Class aType = primitives.get(type);
    // if (aType == null) aType = type;
    schemas.put(name, new DefaultParameterSchema(name, type, defValue));
  }

  /**
   * Constains the value of the named to parameter to those in the list.
   *
   * @param name             the name of the parameter
   * @param constrainingList a list containing the possible values for
   *                         the named parameter
   */
  public void addConstraint(String name, List constrainingList) {
    ParameterSchema schema = schemas.get(name);
    DefaultParameterSchema newSchema = new DefaultParameterSchema(schema);
    newSchema.setConstrainingList(constrainingList);
    schemas.put(name, newSchema);
  }

  /**
   * Adds a converter to convert the named parameter back and forth to a String.
   *
   * @param name      the name of the parameter
   * @param converter the converter
   */
  public void addConvertor(String name, StringConverter converter) {
    ParameterSchema schema = schemas.get(name);
    DefaultParameterSchema newSchema = new DefaultParameterSchema(schema);
    newSchema.setConvertor(converter);
    schemas.put(name, newSchema);
  }


  /**
   * Constrains the value of the named parameter to those in the
   * specified range.
   *
   * @param name              the name of the parmaeter
   * @param constrainingRange a range of the possible values for the named parameter
   */
  public void addConstraint(String name, Range constrainingRange) {
    ParameterSchema schema = schemas.get(name);
    DefaultParameterSchema newSchema = new DefaultParameterSchema(schema);
    newSchema.setConstrainingRange(constrainingRange);
    schemas.put(name, newSchema);
  }

  /**
   * Gets the schema for the specified parameter.
   *
   * @param paramName the name of the parameter
   * @return the schema for the specified parameter
   */
  public ParameterSchema getDetails(String paramName) {
    return schemas.get(paramName);
  }


  /**
   * Gets an iterable over a list of parameter names.
   *
   * @return an iterable over a list of parameter names.
   */
  public Iterable<String> parameterNames() {
    return schemas.keySet();
  }

  /**
   * Validates the specified object against the type info contained for the parameter name.
   *
   * @param paramName the name of the parameter
   * @param obj       the object to validate
   * @return true if the object is of a type valid for the parameter, otherwise false.
   */
  public boolean validate(String paramName, Object obj) {
    ParameterSchema schema = schemas.get(paramName);
    if (schema == null) {
      throw new IllegalParameterException("Schema for parameter '" + paramName + "' not found.");
    }
    return schema.validate(obj);
  }

  /**
   * Returns true if this schema contains contains the specified parameter name, otherwise false.
   *
   * @param paramName the name of the parameter
   * @return true if this schema contains contains the specified parameter name, otherwise false.
   */
  public boolean contains(String paramName) {
    return schemas.containsKey(paramName);
  }

  /**
   * Gets the number of parameters in this schema.
   *
   * @return the number of parameters in this schema.
   */
  public int size() {
    return schemas.size();
  }
}
