package repast.simphony.query;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.apache.commons.collections15.Predicate;

import repast.simphony.context.Context;

/**
 * Query that returns any objects in a context with a specified property
 * whose value is not equal to some specified value.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 * 
 * @deprecated Use {@link Context#getObjectsAsStream(Class)} and the Java 8+ streaming API {@link java.util.stream.Stream} instead. 
 */
public class PropertyNotEquals<T> extends AbstractPropertyQuery<T> {

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
						return !(method.invoke(t, AbstractPropertyQuery.EMPTY).equals(propertyValue));
					return obj == null && propertyValue == null;
				} catch (InvocationTargetException e) {
					AbstractPropertyQuery.msgCenter.error("Error evaluting property equals predicate", e);
				}
			}
			return false;
		}
	}

	/**
	 * Creates a PropertyEquals query to query the specified context for objects with
	 * the specified property where that property is equal to the specified value.
	 *
	 * @param context
	 * @param propertyName
	 * @param propertyValue
	 */
	public PropertyNotEquals(Context<T> context, String propertyName, Object propertyValue) {
		super(context, propertyName, propertyValue);
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

			if (methodMap.size() == 0) propertyNotFound(propertyName);

			return new Equals<T>(methodMap);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			AbstractPropertyQuery.msgCenter.error("Error while creating Property query", e);
		}

		return null;
	}
}
