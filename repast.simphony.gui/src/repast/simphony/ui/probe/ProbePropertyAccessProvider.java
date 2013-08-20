/**
 * 
 */
package repast.simphony.ui.probe;

import java.lang.reflect.Method;

import com.jgoodies.binding.beans.PropertyAccessor;
import com.jgoodies.binding.beans.PropertyAccessors.IntrospectionPropertyAccessorProvider;
import com.jgoodies.binding.beans.PropertyAccessors.PropertyAccessorProvider;
import com.jgoodies.binding.beans.PropertyNotFoundException;

/**
 * PropertyAcessProvider implementation that does not rely on bean type propertyes for getting and setting.
 * 
 * @author Nick Collier
 */
public class ProbePropertyAccessProvider implements PropertyAccessorProvider {
  
  private IntrospectionPropertyAccessorProvider introspectProvider = new IntrospectionPropertyAccessorProvider();

  /* (non-Javadoc)
   * @see com.jgoodies.binding.beans.PropertyAccessors.PropertyAccessorProvider#getAccessor(java.lang.Class, java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public PropertyAccessor getAccessor(Class<?> beanClass, String propertyName, String getterName,
      String setterName) {
    
    if (getterName == null && setterName == null) {
      return introspectProvider.getAccessor(beanClass, propertyName, getterName, setterName);
    }
    
    try {
      Method read = null, write = null;
      Class<?> propType = null;
      
      if (getterName != null) {
        read = beanClass.getMethod(getterName, new Class<?>[0]);
        propType = read.getReturnType();
        if (propType.equals(void.class)) {
          throw new PropertyNotFoundException(propertyName, beanClass);
        }
      }
      
      if (setterName != null) {
        write = beanClass.getMethod(setterName, propType);
      }
      
      return new PropertyAccessor(propertyName, read, write);
      
    } catch (NoSuchMethodException ex) {
      throw new PropertyNotFoundException(propertyName, beanClass, ex);
    }
  }
}
