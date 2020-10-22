/**
 * 
 */
package repast.simphony.ui.parameters;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;

/**
 * Abstract implementation of a ParameterBinder, implementing display name and
 * name.
 * 
 * @author Nick Collier
 */
public abstract class AbstractParameterBinder implements ParameterBinder {

  protected String name, displayName;
  protected Parameters params;
  protected double order = Double.MAX_VALUE;

  protected static Map<Class<?>, String> typeMap = new HashMap<Class<?>, String>();

  static {
    typeMap.put(int.class, "int");
    typeMap.put(double.class, "double");
    typeMap.put(long.class, "long");
    typeMap.put(float.class, "float");
    typeMap.put(boolean.class, "boolean");
    typeMap.put(byte.class, "byte");
    typeMap.put(short.class, "short");
    typeMap.put(String.class, "string");
  }

  /**
   * @param name
   * @param displayName
   */
  public AbstractParameterBinder(String name, String displayName) {
    this.name = name;
    this.displayName = displayName;
  }
  
  public void setDisplayOrder(double val) {
	  order = val;
  }
  
  public double getDisplayOrder() {
	  return order;
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.parameter.ParameterBinder#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Gets the label for the component created by this CompCreator.
   * 
   * @return the label for the component created by this CompCreator.
   */
  public String getLabel() {
    return displayName;
  }

  protected String toXML(Parameters params, String defaultValue, String suffix) {
    ParameterSchema schema = params.getSchema().getDetails(name);
    Class<?> cType = schema.getType();
    String type = cType.getCanonicalName();
    if (typeMap.containsKey(cType))
      type = typeMap.get(cType);

    String converter = schema.getConverter().getClass().getName();

    return String.format(
        "<parameter name = \"%s\" displayName = \"%s\" type=\"%s\" defaultValue=\"%s\" isReadOnly=\"%b\" "
            + "converter=\"%s\" %s />", name, displayName, type, defaultValue,
        params.isReadOnly(name), converter, suffix);
  }
}
