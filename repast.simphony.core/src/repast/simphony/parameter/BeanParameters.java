
package repast.simphony.parameter;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import repast.simphony.util.collections.Pair;
import simphony.util.messages.MessageCenter;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Implemenentation of Parameters that converts a bean's properties into parameters. 
 *
 * @author Nick Collier
 */
public class BeanParameters implements Parameters {

	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(BeanParameters.class);

	// key is bean property name
	private Map<String, Parameter> parameters = new HashMap<String, Parameter>();
	private Object bean;
	private PropertyChangeSupport pcs;
	private Schema schema;

	public BeanParameters(Object bean) {
		this(bean, null);
	}
	

	public BeanParameters(Object bean, Set<String> paramNames) {
		try {
			this.bean = bean;
			BeanInfo info = Introspector.getBeanInfo(bean.getClass(), Object.class);
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			@SuppressWarnings("unchecked")
			List<Pair<String, Class>> pairs = new ArrayList<Pair<String, Class>>();
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				String name = pd.getName();
				if (paramNames == null || paramNames.contains(name)) {
					FastClass fastClass = FastClass.create(bean.getClass());
					FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
					FastMethod writeMethod = pd.getWriteMethod() == null ? null : fastClass.getMethod(pd.getWriteMethod());
					parameters.put(name, new ObjectParameter(readMethod, writeMethod));
					pairs.add(new Pair<String, Class>(name, pd.getPropertyType()));
				}
			}

			schema = new DefaultSchema(pairs.toArray(new Pair[pairs.size()]));
		} catch (IntrospectionException e) {
			msgCenter.error("Unable to create feature attributes from agent", e);
			// todo error center
		}

		pcs = new PropertyChangeSupport(this);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Gets the Schema for this Parameters object.
	 *
	 * @return the Schema for this Parameters object.
	 */
	public Schema getSchema() {
		return schema;
	}
	
	public Parameters clone(){
		try{
			return (Parameters) super.clone();
		}
		catch(CloneNotSupportedException e){
			throw new InternalError(e.toString());
		}
		
	}

	/**
	 * Gets the value associated with the specified parameter name.
	 *
	 * @param paramName the name of the parameter whose value we want
	 * @return the value associated with the specified parameter name.
	 */
	public Object getValue(String paramName) {
		Parameter param = parameters.get(paramName);
		if (param != null) {
			return param.getValue(bean);
		}
		throw new IllegalParameterException("Parameter '" + paramName + "' not found");
	}


  /**
   * Gets a String representation of the specified parameter's value.
   *
   * @param paramName the name of the parameter
   *
   * @return a String representation of the specified parameters's value.
   */
  public String getValueAsString(String paramName) {
    return getValue(paramName).toString();
  }

  /**
	 * Sets the specified parameter name to the specified value.
	 *
	 * @param paramName the name of the parameter to set to the new value
	 * @param val the new value
	 */
	public void setValue(String paramName, Object val) {
		Parameter param = parameters.get(paramName);
		if (param != null) {
			Object oldVal = param.getValue(bean);
			param.setValue(bean, val);
			pcs.firePropertyChange(paramName, oldVal, val);
		}
		else throw new IllegalParameterException("Parameter '" + paramName + "' not found");

	}

	/**
	 * True if parameter is read only.
	 *
	 * @param paramName the name of the parameter
	 * @return true if parameter is read-only otherwise false.
	 */
	public boolean isReadOnly(String paramName) {
		Parameter param = parameters.get(paramName);
		if (param != null) {
			return param.isReadOnly();
		}
		else throw new IllegalParameterException("Parameter '" + paramName + "' not found");
	}

	/**
	 * Gets the bean object that this BeanParameters wraps.
	 * @return the bean object that this BeanParameters wraps.
	 */
	public Object getBean() {
		return bean;
	}


	/**
	 * Gets the display name for the specified parameter name.
	 *
	 * @param paramName the parameter name
	 * @return the display name for the specified parameter name.
	 */
	public String getDisplayName(String paramName) {
		if (schema.contains(paramName)) return paramName;
		return null;
	}
	


	/**
	 * Interface for getting and setting a parameter.
	 */
	static interface Parameter {

		public Object getValue(Object bean);
		public void setValue(Object bean, Object obj) throws IllegalParameterException;
		public boolean isReadOnly();
	}

	static class ObjectParameter implements Parameter {

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
				// todo error center
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
