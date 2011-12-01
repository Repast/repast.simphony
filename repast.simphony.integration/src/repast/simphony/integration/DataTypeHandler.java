/*CopyrightHere*/
package repast.simphony.integration;

/**
 * Handler for different types of data.  These are the values that are acceptable for the data
 * element's (in a data file descriptor) type field.
 *  
 * @author Jerry Vos
 */
public enum DataTypeHandler {
	STRING("string") {
		@Override
		public String convert(String string) {
			return string;
		}
	},
	INT("int") {
		@Override
		public Integer convert(String string) {
			string = string.trim();
			if (string.endsWith(".0")) {
				string = string.substring(0, string.length() - 2);
			}
			return Integer.valueOf(string.trim());
		}

	},
	DOUBLE("double") {
		@Override
		public Double convert(String string) {
			return Double.valueOf(string);
		}

	};

	private String typeName;

	DataTypeHandler(String typeName) {
		this.typeName = typeName;
	}

	public static DataTypeHandler getDataType(String typeName) {
		for (DataTypeHandler handler : values()) {
			if (handler.typeName.equalsIgnoreCase(typeName)) {
				return handler;
			}
		}
		return null;
	}

	public abstract Object convert(String string);
	
	public String getTypeName() {
		return typeName;
	}
}