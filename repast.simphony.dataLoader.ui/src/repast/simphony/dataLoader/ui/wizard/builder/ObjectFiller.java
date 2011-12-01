package repast.simphony.dataLoader.ui.wizard.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import repast.simphony.annotate.PropertyAnnot;

public class ObjectFiller {

	public ObjectFiller() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Object fillObject(Object o, Map<String, Object> propMap)
			throws Exception {
		Class<?> agentClass = o.getClass();
		for (Entry<String, Object> fieldEntry : propMap.entrySet()) {
			try {				
				Field f = agentClass.getDeclaredField(fieldEntry.getKey());
				Class currentSearchClass = agentClass;
				while(f == null){
					//TODO: search the parents
					
					//currentSearchClass = current
				}
				PropertyEntry entry = null;
				if (f.isAnnotationPresent(PropertyAnnot.class)) {
					PropertyAnnot prop = f.getAnnotation(PropertyAnnot.class);
					if (prop.setMethod().length() > 0) {
						Method m = null;
						try {
							m = agentClass.getMethod(prop.setMethod(),
									new Class[] { f.getType() });
							entry = new MethodEntry(m, fieldEntry.getValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						entry = new FieldEntry(f, fieldEntry.getValue());
					}
				}
				entry.update(o);

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return null;
	}

	abstract class PropertyEntry {
		Class type;

		public PropertyEntry(Class type) {
			this.type = type;
		}

		abstract void update(Object o) throws Exception;

		public Object getValue(String toString) {
			if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
				return Integer.parseInt(toString);
			} else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
				return Boolean.parseBoolean(toString);
			} else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
				return Short.parseShort(toString);
			} else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
				return Long.parseLong(toString);
			} else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
				return Float.parseFloat(toString);
			} else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
				return Double.parseDouble(toString);
			} else if (type.equals(String.class)
					|| String.class.isAssignableFrom(type)) {
				return toString;
			} else if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
				return Byte.parseByte(toString);
			} else if (type.equals(Character.TYPE)
					|| type.equals(Character.class)) {
				return Character.toChars(Integer.parseInt(toString));
			}
			return null;
		}
	}

	class MethodEntry extends PropertyEntry {
		Method m;

		Object value;

		public MethodEntry(Method m, Object o) {
			super(m.getParameterTypes()[0]);
			this.m = m;
			this.value = o;
		}

		@Override
		public void update(Object o) throws Exception {
			m.invoke(o, new Object[] { value });
		}
	}

	class FieldEntry extends PropertyEntry {
		Field f;

		Object value;

		public FieldEntry(Field f, Object o) {
			super(f.getType());
			f.setAccessible(true);
			this.f = f;
			this.value = o;
		}

		@Override
		public void update(Object o) throws Exception {
			f.set(o, value);
		}
	}
}
