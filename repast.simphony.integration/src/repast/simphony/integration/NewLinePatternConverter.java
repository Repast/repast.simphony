/*CopyrightHere*/
package repast.simphony.integration;

import repast.simphony.util.SystemConstants;


/**
 * Handles converting newline patterns into different newline patterns. This means it will convert
 * patterns matching "\n" to a pattern that matches Windows newlines "\r\n", unix newlines "\n",
 * system dependent (currently only windows or unix types), or do no conversion.<p/>
 * 
 * This is <b>NOT</b> for doing "\n" -> "\r\n" type conversions, but is for converting regular
 * expressions that match newline characters from matching "\n" to matching some other type.
 * 
 * @author Jerry Vos
 */
public enum NewLinePatternConverter {
	/**
	 * Performs no conversion
	 */
	NONE("none") {
		@Override
		public String convert(String string) {
			return string;
		}
	},
	/**
	 * If the system's line separator is "\r\n" it will do the same as a WINDOWS converter, if the
	 * pattern is "\n" it will return a UNIX one, otherwise it will just return the string.
	 */
	SYSTEM("system") {
		@Override
		public String convert(String string) {
			// TODO: find a better way to do this
			if (SystemConstants.LINE_SEPARATOR.equals("\r\n")) {
				return WINDOWS.convert(string);
			} else if (SystemConstants.LINE_SEPARATOR.equals("\n")) {
				return UNIX.convert(string);
			} else {
				// TODO: handle this
				return string;
			}
		}
	},
	/**
	 * Replaces "\n" matches with "\r\n" matches.
	 */
	WINDOWS("windows") {
		@Override
		public String convert(String string) {
			return string.replaceAll("\\\\n", "\\\\r\\\\n");
		}
	},
	/**
	 * Returns the string as it is. <b>This does NOT convert "\r\n" matchers to "\n".</b>
	 */
	UNIX("unix") {
		@Override
		public String convert(String string) {
			return string;
		}
	};

	private final String type;

	NewLinePatternConverter(String descriptor) {
		this.type = descriptor;
	}

	/**
	 * Returns the converter that has the specified type as it type.
	 * 
	 * @param handleType
	 *            the type to find a converter for.
	 * @return a NewLinePatternConverter or NewLinePatternConverter.NONE if no matching one can be
	 *         found
	 */
	public static NewLinePatternConverter getNewLineConverter(String handleType) {
		for (NewLinePatternConverter handler : values()) {
			if (handler.type.equalsIgnoreCase(handleType)) {
				return handler;
			}
		}

		return NONE;
	}

	public String getType() {
		return this.type;
	}

	/**
	 * Converts the newline patterns to those specified by its type.
	 * 
	 * @param string
	 *            a string with newline matching patterns
	 * @return a converted string
	 */
	public abstract String convert(String string);
}