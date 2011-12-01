/*CopyrightHere*/
package repast.simphony.integration;

import repast.simphony.util.SystemConstants;


public enum EscapeConverter {
	NONE("none") {
		@Override
		public String convert(String string) {
			return string;
		}
	},
	SYSTEM("system") {
		@Override
		public String convert(String string) {
			String retVal;
			
			// TODO: find a better way to do this
			if (SystemConstants.LINE_SEPARATOR.equals("\r\n")) {
				retVal = WINDOWS.convert(string);
			} else if (SystemConstants.LINE_SEPARATOR.equals("\n")) {
				retVal = UNIX.convert(string);
			} else {
				retVal = string.replace("\\\\n", SystemConstants.LINE_SEPARATOR);
			}
			
			return convertNonNewlineEscapes(retVal);
		}
	},
	WINDOWS("windows") {
		@Override
		public String convert(String string) {
			return convertNonNewlineEscapes(string.replaceAll("\\\\n", "\r\n"));
//			return string.replaceAll("\\\\n", "\r\n");
		}
	},
	UNIX("unix") {
		@Override
		public String convert(String string) {
			return convertNonNewlineEscapes(string.replaceAll("\\\\n", "\n"));
		}
	};

	public final String tag;

	EscapeConverter(String descriptor) {
		this.tag = descriptor;
	}

	public static EscapeConverter getNewLineConverter(String handleType) {
		for (EscapeConverter handler : values()) {
			if (handler.tag.equalsIgnoreCase(handleType)) {
				return handler;
			}
		}

		return NONE;
	}
	
	public static String convertNonNewlineEscapes(String string) {
		return string.replaceAll("\\\\t", "\t");
	}

	public abstract String convert(String string);
}