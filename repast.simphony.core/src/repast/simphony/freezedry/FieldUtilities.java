package repast.simphony.freezedry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;

public class FieldUtilities {
//	Set<Class> numberTypes = new HashSet<Class>();

	public static FieldUtilities INSTANCE = new FieldUtilities();

	private HashMap<String, Class> primitiveMap;

	private FieldUtilities() {
		primitiveMap = new HashMap<String, Class>();
		addPrimitiveType(Integer.TYPE);
		addPrimitiveType(Byte.TYPE);
		addPrimitiveType(Double.TYPE);
		addPrimitiveType(Float.TYPE);
		addPrimitiveType(Long.TYPE);
		addPrimitiveType(Short.TYPE);
		addPrimitiveType(Character.TYPE);
		addPrimitiveType(Boolean.TYPE);
		addPrimitiveType(String.class);
		addPrimitiveType(Integer.class);
		addPrimitiveType(Byte.class);
		addPrimitiveType(Double.class);
		addPrimitiveType(Float.class);
		addPrimitiveType(Long.class);
		addPrimitiveType(Short.class);
		addPrimitiveType(Character.class);
		addPrimitiveType(Boolean.class);
//		numberTypes.add(Integer.class);
//		numberTypes.add(Byte.class);
//		numberTypes.add(Double.class);
//		numberTypes.add(Float.class);
//		numberTypes.add(Long.class);
//		numberTypes.add(Short.class);
	}

	private void addPrimitiveType(Class clazz) {
		primitiveMap.put(clazz.getName(), clazz);
	}
	
	public boolean isPrimitive(Field field) {
		return primitiveMap.containsValue(field.getType());
	}

	public boolean isPrimitive(String className) {
		return primitiveMap.containsKey(className);
	}

	public boolean isPrimitive(Class<?> clazz) {
		return primitiveMap.containsValue(clazz);
	}
	
	/**
	 * Checks if the specified field's value represents a primitive array of rank 1. So this would
	 * return true if the field represents a double[], but not a double[][] or a Object[].
	 * 
	 * @param field
	 *            the field to check
	 * @return if the specified field represents a primitive array
	 */
	public boolean isPrimitiveArray(Field field) {
		return isPrimitiveArray(field.getType());
	}

	/**
	 * Checks if the specified string representation of a class represents a primitive array of rank 1. So this would
	 * return true if the string represents a double[], but not a double[][] or a Object[].
	 * 
	 * @param className
	 *            the name of the class to check
	 * @return if the specified class name represents a primitive array
	 */
	public boolean isPrimitiveArray(String className) {
		return className.length() == 2 && className.startsWith("[");
	}
	
	/**
	 * Checks if the specified class represents a primitive array of rank 1. So this would return
	 * true if the class represents a double[], but not a double[][] or a Object[].
	 * 
	 * @param clazz
	 *            the class to check
	 * @return if the specified class represents a primitive array
	 */
	public boolean isPrimitiveArray(Class<?> clazz) {
		return clazz.isArray() && primitiveMap.containsValue(clazz.getComponentType());
	}


	public String getTypeAsString(Object o) {
		String type;
		if (o.getClass().isArray()) {
			type = o.getClass().getComponentType().getName() + "_ARRAY";
		} else {
			type = o.getClass().getName();
		}
		return type;
	}

	public Class getClassFromString(String string)
			throws ClassNotFoundException {
		if (string.endsWith("_ARRAY")) {
			String componentName = string.substring(0, string
					.indexOf("_ARRAY"));
			
			Class componentType = getClassFromString(componentName);
			 
			Object o = Array.newInstance(componentType, 0);
			return o.getClass();
		}
		
		if (isPrimitive(string)) {
			return primitiveMap.get(string);
		}
		
		return Class.forName(string);
	}

//	public boolean isNumber(Object o) {
//		return numberTypes.contains(o.getClass());
//	}
}
