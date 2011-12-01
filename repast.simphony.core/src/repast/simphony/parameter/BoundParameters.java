package repast.simphony.parameter;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import simphony.util.messages.MessageCenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Parameters implementation that binds a set of parameters to
 * those in some bean. 
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BoundParameters extends DefaultParameters {

	private Map<String, ObjectParameter> bindMap = new HashMap<String, ObjectParameter>();
	private Object bean;


	/**
	 * Creates a new BoundParameters using the parameters in the specified Parameters object.
	 *
	 * @param param parameters whose values we want to copy into this BoundParameters
	 */
	public BoundParameters(Parameters param) {
		super(param);
	}

	/**
	 * Gets the bean that is bound the contained parameters.
	 *
	 * @return the bean that is bound the contained parameters.
	 */
	public Object getBean() {
		return bean;
	}

	/**
	 * Sets the bean to bind to the contained parameters. This
	 * will also set the bean's corresponding properties to the
	 * current parameter values.
	 *
	 * @param bean the bean to bind.
	 */
	public void setBean(Object bean) {
		this.bean = bean;
		for (String name : bindMap.keySet()) {
			ObjectParameter param = bindMap.get(name);
			if(!param.isReadOnly()) param.setValue(bean, super.getValue(name));
		}
	}


	/**
	 * Sets the specified parameter name to the specified value.
	 *
	 * @param paramName the name of the parameter to set to the new value
	 * @param val       the new value
	 */
	@Override
	public void setValue(String paramName, Object val) {
		super.setValue(paramName, val);
		ObjectParameter param = bindMap.get(paramName);
		if(bean != null && param != null && !param.isReadOnly()) param.setValue(bean, super.getValue(paramName));
	}

	public void addParameter(String name, String displayName, Class<?> paramClass, Object val, Method readMethod,
	                         Method writeMethod) {
		addParameter(name, paramClass, val, readMethod, writeMethod);
		nameMap.put(name, displayName);
	}

	public void addParameter(String name, Class<?> paramClass, Object val, Method readMethod, Method writeMethod) {
		super.addParameter(name, paramClass, val, writeMethod == null);
		FastClass fastClass = FastClass.create(readMethod.getDeclaringClass());
		FastMethod fReadMethod = fastClass.getMethod(readMethod);
		FastMethod fWriteMethod = writeMethod == null ? null : fastClass.getMethod(writeMethod);
		bindMap.put(name, new ObjectParameter(fReadMethod, fWriteMethod));
	}

	static class ObjectParameter {

		private static final Object[] EMPTY_ARRAY = new Object[0];
		private FastMethod readMethod;
		private FastMethod writeMethod;

		public ObjectParameter(FastMethod readMethod, FastMethod writeMethod) {
			this.readMethod = readMethod;
			this.writeMethod = writeMethod;
		}

		public Object getValue(Object bean) {
			try {
				return readMethod.invoke(bean, EMPTY_ARRAY);
			} catch (InvocationTargetException e) {
				MessageCenter.getMessageCenter(getClass()).error("Unable to get feature attribute from object", e);
			}
			return Parameters.NULL;
		}

		public void setValue(Object bean, Object value) throws IllegalParameterException {
			if (writeMethod == null) {
				throw new IllegalParameterException("Unable to set attribute: write method does not exist");
			}
			try {
				writeMethod.invoke(bean, new Object[]{value});
			} catch (InvocationTargetException e) {
				IllegalParameterException ex = new IllegalParameterException("Unable to set attribute: " + e.getMessage());
				ex.initCause(e);
				throw ex;
			}
		}

		public boolean isReadOnly() {
			return writeMethod == null;
		}
	}
}
