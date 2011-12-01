package repast.simphony.query;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.collections15.Predicate;
import repast.simphony.context.Context;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Query that returns any objects in a context with a specified property
 * whose value is equal to some specified value.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class PropertyEquals<T> extends AbstractPropertyQuery<T> {

  private class Equals<T> implements Predicate<T> {

    Map<Class, FastMethod> methodMap;

    public Equals(Map<Class, FastMethod> methodMap) {
      this.methodMap = methodMap;
    }

    public boolean evaluate(T t) {
      FastMethod method = methodMap.get(t.getClass());
      if (method != null) {
        try {
          Object obj = method.invoke(t, AbstractPropertyQuery.EMPTY);
          if (obj != null && propertyValue != null)
            return pred.evaluate(method.invoke(t, AbstractPropertyQuery.EMPTY), (propertyValue));
          return obj == null && propertyValue == null;
        } catch (InvocationTargetException e) {
          AbstractPropertyQuery.msgCenter.error("Error evaluting property equals predicate", e);
        }
      }
      return false;
    }
  }

  private PropertyEqualsPredicate pred = new PropertyEqualsPredicate() {

    public boolean evaluate(Object o, Object o1) {
      return o.equals(o1);
    }
  };

  /**
   * Creates a PropertyEquals query to query the specified context for objects with
   * the specified property where that property is equal to the specified value.
   *
   * @param context
   * @param propertyName
   * @param propertyValue
   */
  public PropertyEquals(Context<T> context, String propertyName, Object propertyValue) {
    super(context, propertyName, propertyValue);
  }

  /**
   * Creates a PropertyEquals query to query the specified context for objects with
   * the specified property where that property is equal to the specified value.
   *
   * @param context
   * @param propertyName
   * @param propertyValue
   * @param predicate     a custom predicate used to do the equals evaluation
   */
  public PropertyEquals(Context<T> context, String propertyName, Object propertyValue, PropertyEqualsPredicate
          predicate) {
    super(context, propertyName, propertyValue);
    this.pred = predicate;
  }


  /**
   * Creates a Predicate that tests for property equality.
   *
   * @param context
   * @param propertyName
   * @return a Predicate that tests for property equality.
   */
  protected Predicate<T> createPredicate(Context<T> context, String propertyName) {
    try {
      Map<Class, FastMethod> methodMap = new HashMap<Class, FastMethod>();
      for (Class clazz : context.getAgentTypes()) {
        PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
          if (pd.getName().equalsIgnoreCase(propertyName)) {
            Method readMethod = pd.getReadMethod();
            if (readMethod != null) {
              methodMap.put(clazz, FastClass.create(readMethod.getDeclaringClass()).getMethod(readMethod));
            }
            break;
          }
        }
      }

//      if (methodMap.size() == 0) propertyNotFound(propertyName);

      return new Equals<T>(methodMap);
    } catch (IntrospectionException e) {
      e.printStackTrace();
      AbstractPropertyQuery.msgCenter.error("Error while creating Property query", e);
    }

    return null;
	}
}
