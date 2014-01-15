package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;

import repast.simphony.parameter.ConstantSetter;
import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.StringConverter;

/**
 * Creates a constant setter for custom types using a StringConverter.
 * 
 * @author Nick Collier
 */
public class ConstantObjectSetterCreator extends AbstractParameterSetterCreator {

  private Object value;
  private Class<?> type;
  private StringConverter<?> converter;

  /**
   * Initializes this ParameterSetterCreator with the specified attributes. Any
   * following calls to addParameter or createSetter will use this attributes.
   * 
   * @param attributes
   */
  @Override
  public void init(Attributes attributes) throws ParameterFormatException {
    super.init(attributes);
    String sValue = attributes.getValue(SetterConstants.CONSTANT_VALUE);
    String sType = attributes.getValue(SetterConstants.CONSTANT_TYPE_NAME);
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
    
    value = converter.fromString(sValue);
  }

  /**
   * Adds the parameter to the specified creator based on the attributes added
   * in init.
   * 
   * @param creator
   */
  public void addParameter(ParametersCreator creator) {
    creator.addParameter(name, type, value, false);
    creator.addConvertor(name, converter);
  }

  /**
   * Creates a parameter setter from the attributes added in init.
   * 
   * @return a parameter setter from the attributes added in init.
   */
  public ParameterSetter createSetter() {
    return new ConstantSetter<Object>(name, value);
  }
}
