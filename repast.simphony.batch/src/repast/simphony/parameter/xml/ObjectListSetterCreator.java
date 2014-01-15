package repast.simphony.parameter.xml;

import java.util.Map;

import org.xml.sax.Attributes;

import repast.simphony.parameter.ListParameterSetter;
import repast.simphony.parameter.ListTokenizer;
import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.StringConverter;

/**
 * Creator for ObjectListParameterSteppers. Parameter lists of
 * non-primitive object types where the user specifies
 * a converter.
 * 
 * @author Nick Collier
 */
public class ObjectListSetterCreator extends AbstractParameterSetterCreator {

  // <parameter name="list_val" type="list" value_type="String"
  // values="foo bar baz" />

  private Object[] vals;
  private Class<?> type;
  private StringConverter<?> converter;

  /**
   * Adds the parameter to the specified creator based on the attributes added
   * in init.
   * 
   * @param creator
   */
  public void addParameter(ParametersCreator creator) {
    creator.addParameter(name, type, vals[0], false);
    creator.addConvertor(name, converter);
  }

  /**
   * Initializes this ParameterSetterCreator with the specified attributes. Any
   * following calls to addParameter or createSetter will use this attributes.
   * 
   * @param attributes
   */
  @Override
  public void init(Attributes attributes) throws ParameterFormatException {
    super.init(attributes);
    doInit();
  }

  /**
   * Initializes this ParameterSetterCreator with the specified attributes. Any
   * following calls to addParameter or createSetter will use this attributes.
   * 
   * @param attributes
   */
  @Override
  public void init(Map<String, String> attributes) throws ParameterFormatException {
    super.init(attributes);
    doInit();
  }

  private void doInit() throws ParameterFormatException {
    String sType = attributes.getValue(SetterConstants.LIST_VALUE_TYPE_NAME);
    try {
      type = Class.forName(sType);
    }  catch (ClassNotFoundException ex) {
      throw new ParameterFormatException("Error parsing batch parameter '" + name + "'", ex);
    }
    
    String clz = attributes.getValue(SetterConstants.CONVERTER_ATTRIBUTE_NAME);
    if (clz == null)
      throw new ParameterFormatException("Invalid parameter format. Parameter '" + name
          + "' is missing " + SetterConstants.CONVERTER_ATTRIBUTE_NAME + " attribute");
    try {
      Class<?> clazz = Class.forName(clz);
      converter = (StringConverter<?>) clazz.newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
      throw new ParameterFormatException("Error while creating converter '" + clz + "' for batch parameter '" + name + "'", ex);
    }
    
    String[] sVals = ListTokenizer.parseStringValues(attributes.getValue(SetterConstants.LIST_VALUES_NAME));
    vals = new Object[sVals.length];
    int i = 0; 
    for (String val : sVals) {
      vals[i++] = converter.fromString(val);
    }
  }

  /**
   * Creates a parameter setter from the specified attributes
   * 
   * @return a parameter setter created from the specified attributes
   */
  public ParameterSetter createSetter() {
    return new ListParameterSetter<Object>(name, vals);
  }
  
}
