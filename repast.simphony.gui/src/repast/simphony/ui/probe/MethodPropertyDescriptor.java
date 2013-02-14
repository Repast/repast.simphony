/**
 * 
 */
package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import repast.simphony.parameter.StringConverter;

/**
 * Property descriptor for properties that are accessed via methods. This extends
 * PropertyDescriptor, adding support for converting to and from a String. 
 * 
 * @author Nick Collier
 */
public class MethodPropertyDescriptor extends PropertyDescriptor {
  
  private StringConverter<?> converter = null;
  
  /**
   * @param propertyName
   * @param beanClass
   * @throws IntrospectionException
   */
  public MethodPropertyDescriptor(String propertyName, Class<?> beanClass)
      throws IntrospectionException {
    super(propertyName, beanClass);
  }

  /**
   * @param propertyName
   * @param readMethod
   * @param writeMethod
   * @throws IntrospectionException
   */
  public MethodPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod)
      throws IntrospectionException {
    super(propertyName, readMethod, writeMethod);
  }
 
  /**
   * Sets the StringConverter.
   * 
   * @param converter
   */
  public void setStringConverter(StringConverter<?> converter) {
    this.converter = converter;
  }
  
  /**
   * Gets the StringConverter, if any.
   * @return
   */
  public StringConverter<?> getStringConverter() {
    return converter;
  }
}
