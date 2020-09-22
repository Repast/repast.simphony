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
 * whose value is greater than or equal to some specified value.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 * 
 * @deprecated Use {@link Context#getObjectsAsStream(Class)} and the Java 8+ streaming API {@link java.util.stream.Stream} instead. 
 */
public class PropertyGreaterThanEquals<T> extends AbstractPropertyQuery<T> {

	private class GreaterThanEquals<T> implements Predicate<T> {

		Map<Class, FastMethod> methodMap;

		public GreaterThanEquals(Map<Class, FastMethod> methodMap) {
			this.methodMap = methodMap;
		}

		public boolean evaluate(T t) {
			FastMethod method = methodMap.get(t.getClass());
			if (method != null) {
				try {
					Object obj = method.invoke(t, AbstractPropertyQuery.EMPTY);
					if (obj == null || propertyValue == null) return false;
					return ((Number) method.invoke(t, AbstractPropertyQuery.EMPTY)).doubleValue() >=
									((Number) propertyValue).doubleValue();
				} catch (InvocationTargetException e) {
					AbstractPropertyQuery.msgCenter.error("Error evaluting property great than or equals predicate", e);
				}
			}
			return false;
		}
	}

	/**
	 * Creates a PropertyGreaterThan query to query the specified context for objects with
	 * the specified property where that property is greater than the specified value.
	 *
	 * @param context
	 * @param propertyName
	 * @param propertyValue
	 * @throws IllegalArgumentException if the specified property is not numeric.
	 */
	public PropertyGreaterThanEquals(Context<T> context, String propertyName, Number propertyValue) {
		super(context, propertyName, propertyValue);
	}

	/**
	 * Creates a Predicate that tests if a numeric property is greater than some value.
	 * @param context
	 * @param propertyName
	 *
	 * @return a Predicate that tests if a numeric property is greater than some value.
	 */
	protected Predicate<T> createPredicate(Context<T> context, String propertyName) {
		try {
			Map<Class, FastMethod> methodMap = new HashMap<Class, FastMethod>();
			for (Class clazz : context.getAgentTypes()) {
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					if (pd.getName().equalsIgnoreCase(propertyName)) {
						Method readMethod = pd.getReadMethod();
						if (readMethod != null) {
							Class<?> returnType = readMethod.getReturnType();
							if (Number.class.isAssignableFrom(returnType) || primNums.contains(returnType)) {
								methodMap.put(clazz, FastClass.create(readMethod.getDeclaringClass()).getMethod(readMethod));
							} else {
								IllegalArgumentException ex = new IllegalArgumentException("Property '" +
								 propertyName + "' must be a numeric property");
								AbstractPropertyQuery.msgCenter.error("Error while creating Property query", ex);
								throw ex;
							}
						}
						break;
					}
				}
			}

			if (methodMap.size() == 0) propertyNotFound(propertyName);

			return new GreaterThanEquals<T>(methodMap);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			AbstractPropertyQuery.msgCenter.error("Error while creating Property query", e);
		}
		return null;
	}
}
