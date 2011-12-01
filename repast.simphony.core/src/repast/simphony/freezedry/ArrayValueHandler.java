/*CopyrightHere*/
package repast.simphony.freezedry;

import simphony.util.messages.MessageCenter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This is an enum for working with string representation of primitive arrays.
 * 
 * @author Jerry Vos
 */
public enum ArrayValueHandler {
	DOUBLE(double[].class.getName(), Double[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return Double.valueOf(value.trim());
		}
	}, FLOAT(float[].class.getName(), Float[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return Integer.valueOf(value.trim());
		}
	}, INTEGER(int[].class.getName(), Integer[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return Integer.valueOf(value.trim());
		}
	}, BOOLEAN(boolean[].class.getName(), Boolean[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return Boolean.valueOf(value.trim());
		}
	}, CHARACTER(char[].class.getName(), Character[].class.getName()) {
		@Override
		public Object getValue(String value) {
			if (value.length() > 1) {
				LOG
						.warn("Reading in a character and the string representing it '" + value
						+ "' is longer than 1, only the first character will be used.");
			}
			if (value.length() == 0) {
				LOG
						.warn("Reading in a character and the string representing it has length 0, returning null.");
				return null;
			}
			return value.charAt(0);
		}
	}, LONG(long[].class.getName(), Long[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return Long.valueOf(value.trim());
		}
	}, SHORT(short[].class.getName(), Short[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return Short.valueOf(value.trim());
		}
	}, STRING(String[].class.getName()) {
		@Override
		public Object getValue(String value) {
			return value;
		}
	};
	
	/**
	 * Converts the string value to a value for the type of array. So for a double[] this would
	 * convert it to a Double.
	 * 
	 * @param value
	 *            the string value of the array element
	 * @return the converted value
	 */
	public abstract Object getValue(String value);
	
	/**
	 * Creates an array of the specified type and size.
	 * 
	 * @param arrayType
	 *            the type of the array
	 * @param size
	 *            the size of the array
	 * @return an array of the specified type and size or null if the creation failed
	 */
	public Object createArray(String arrayType, int size) {
		try {
			return Array.newInstance(FieldUtilities.INSTANCE.getClassFromString(arrayType)
					.getComponentType(), size);
		} catch (NegativeArraySizeException e) {
			LOG.warn("Cannot pass in a negative array size, returning null.", e);
			return null;
		} catch (ClassNotFoundException e) {
			LOG.warn("Unsupported array type '" + arrayType + "' specified, returning null.", e);
		}
		return null;
	}
	
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(ArrayValueHandler.class);
	
	private String[] arrayTypes;
	
	@SuppressWarnings("static-access")
	private ArrayValueHandler(String... arrayTypes) {
		this.arrayTypes = arrayTypes;
	}
	
	/**
	 * Retrieves if this handler handles the specified type of array.
	 * 
	 * @param arrayType
	 *            the type of the array (the toString of the array's class)
	 * @return if it handles the type
	 */
	public boolean handles(String arrayType) {
		for (int i = 0; i < arrayTypes.length; i++) {
			if (arrayTypes[i].equals(arrayType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves a handler for the specified array type.
	 * 
	 * @param arrayType
	 *            the type of the array
	 * @return a handler that handles the specified type, or null if no handler handles that type
	 */
	public static ArrayValueHandler getHandler(String arrayType) {
		for (ArrayValueHandler handler : values()) {
			if (handler.handles(arrayType)) {
				return handler;
			}
		}
		return null;
	}
	
	/**
	 * Reads in an array from the given array string and returns it. This uses the given delimiter
	 * to differentiate between elements of the array. <p/>
	 * 
	 * For string arrays, this expects that each element of the array be enclosed in qoutation marks.
	 * So if your array is <code>new String[] { "a", "b" }</code> the passed in string should be like
	 * <code>""a", "b""</code>. 
	 * 
	 * @param arrayType
	 *            the type of the array, given by type[].class.getName() (for example
	 *            double[].class.getName())
	 * @param arrayString
	 *            the string representing the array
	 * @param delimiter
	 *            the delimiter between array elements
	 * @return the read in array of the given type
	 */
	public static Object readArray(String arrayType, String arrayString, char delimiter) {
		ArrayList<Object> values = new ArrayList<Object>();

		boolean isStringArray = STRING.handles(arrayType);
		boolean inString = false;
		
		ArrayValueHandler handler = getHandler(arrayType);
		if (handler == null) {
			LOG.warn("Unsupported array type specified '" + arrayType + "', returning null.");
			return null;
		}
		
		boolean escaped = false;
		
		int start = 0;
		int i = 0;
		try {
			for (i = 0; i < arrayString.length(); i++) {
				if (isStringArray) {
					if (i == 0) {
						start = arrayString.indexOf('"') + 1;
						i = start - 1;
						inString = true;
						continue;
					}
					if (inString && !escaped && arrayString.charAt(i) == '"') {
						values.add(handler.getValue(arrayString.substring(start, i).replace("\\\"", "\"")));
						inString = false;
						i = arrayString.indexOf(delimiter, i);
						if (i < 0) {
							break;
						}
					} else if (!escaped && arrayString.charAt(i) == '"') {
						escaped = false;
						inString = true;
						start = i + 1;
					}
					
					if (!escaped && arrayString.charAt(i) == '\\') {
						escaped = true;
					} else {
						escaped = false;
					}
					
				} else {
					if (arrayString.charAt(i) == delimiter) {
						values.add(handler.getValue(arrayString.substring(start, i)));
						start = i + 1;
					}
				}
			}
			if (!isStringArray) {
				values.add(handler.getValue(arrayString.substring(start)));
			}
		} catch (RuntimeException ex) {
			LOG.error("Error while reading string near char " + i + " in string '" + arrayString + "'", ex);
			throw ex;
		}
		
		Object array = handler.createArray(arrayType, values.size());
		for (i = 0; i < values.size(); i++) {
			Array.set(array, i, values.get(i));
		}
		
		return array;
	}
	
	/**
	 * Writes an array of a type to a String. This will use the toString method on the array elements 
	 * to get the value for each element, and will insert between elements the given delimiter 
	 * followed by a space. <p/>
	 * 
	 * For string arrays, this will enclose each element of the array in qoutation marks.
	 * So if your array is <code>new String[] { "a", "b" }</code> the resultant string will be like
	 * <code>""a", "b""</code>. 
	 * 
	 * @param array
	 *            the array to turn into a string
	 * @param delimiter
	 *            the delimiter between array elements
	 * @return a string representation of the specified array
	 */
	public static String writeArray(Object array, char delimiter) {
		boolean isString = array instanceof String[];
		
		int len = Array.getLength(array);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			if (i != 0) {
				builder.append(delimiter + " ");
			}
			if (isString) {
				builder.append("\"");
				builder.append(((String) Array.get(array, i)).replace("\"", "\\\""));
				builder.append("\"");
			} else {
				builder.append(Array.get(array, i));
			}
		}
		
		return builder.toString();
	}
}