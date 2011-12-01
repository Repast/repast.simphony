/*CopyrightHere*/
package repast.simphony.integration;

import org.jdom.Element;

/**
 * Class that holds the elements and attributes of those elements in a data file descriptor. The
 * elements are specified by the enum values in this enumeration.  The attributes are specified as 
 * inner classes of the name TAG_ATTRS.  This unfortunate way of doing this is necessary since enum
 * values cannot have nested static values inside of them.
 * 
 * @author Jerry Vos
 */
public enum DataFileElements {
	FILE_DEF("fileDef"), DATA("data"), RECORD("record"), ARRAY("array"), BRANCH("branch"), TABLE("table");

	public static final class FILE_DEF_ATTRS {
		public static final String NEW_LINE_TYPE = "newlineType";

		public static String getNewLineType(Element nodeToHandle) {
			if (nodeToHandle.getAttribute(NEW_LINE_TYPE) == null) {
				return NewLinePatternConverter.NONE.getType();
			}
			return NewLinePatternConverter.getNewLineConverter(nodeToHandle.getAttribute(
					NEW_LINE_TYPE).getValue()).getType();
		}
	}

	public static final class DATA_ATTRS {
		public static final String TARGET = "target";

		public static final String DELIMITER = "delimiter";

		public static final String LENGTH = "length";

		public static final String TYPE = "type";

		public static final String PATTERN = "pattern";

		public static final String DEFAULT_STRING_DELIMITER = "\\n";

		public static String getType(Queryable outWriter, Element nodeToHandle) {
			return DataFileUtils.findBestValue(outWriter, nodeToHandle, TYPE);
		}

		public static String getTarget(Queryable outWriter, Element nodeToHandle) {
			return DataFileUtils.findBestValue(outWriter, nodeToHandle,
					DataFileElements.DATA_ATTRS.TARGET, false);
		}

		public static int getLength(Queryable outWriter, Element nodeToHandle) {
			if (nodeToHandle.getAttribute(LENGTH) == null) {
				return -1;
			}

			String bestValue = DataFileUtils.findBestValue(outWriter, nodeToHandle, LENGTH);
			return Integer.valueOf(bestValue);
		}

		public static String getDelimiter(Queryable outWriter, Element nodeToHandle, String type) {
			String delimiter;
			if (nodeToHandle.getAttribute(DELIMITER) == null) {
				delimiter = getDefaultDelimiter(type);
			} else {
				delimiter = DataFileUtils.findBestValue(outWriter, nodeToHandle, DELIMITER);
			}
			return delimiter;
		}

		public static String getDefaultDelimiter(String type) {
			if (DataTypeHandler.getDataType(type) == null) {
				return null;
			}
			switch (DataTypeHandler.getDataType(type)) {
			case STRING:
				return DEFAULT_STRING_DELIMITER;
			default:
				return null;
			}
		}

		public static String getPattern(Queryable outWriter, Element nodeToHandle/* , String type */) {
			if (nodeToHandle.getAttribute(PATTERN) == null) {
				return null;
			}
			return DataFileUtils.findBestValue(outWriter, nodeToHandle, PATTERN);
		}
	}

	public static final class RECORD_ATTRS {
		public static final String TARGET = DATA_ATTRS.TARGET;

		public static final String COUNT = "count";

		public static int getCount(Queryable outWriter, Element node) {
			return getInt(outWriter, node, COUNT);
		}

		public static String getTarget(Queryable outWriter, Element node) {
			return DataFileUtils.findBestValue(outWriter, node, TARGET, false);
		}
	}

	public static final class ARRAY_ATTRS {
		public static final String TARGET = DATA_ATTRS.TARGET;

		public static final String TYPE = DATA_ATTRS.TYPE;

		public static final String ROWS = "rows";
		
		public static final String COLS = "cols";
		
		public static final String COL_DELIMITER = "colDelimiter";
		
		public static final String ROW_DELIMITER = "rowDelimiter";

		public static String getTarget(Queryable outWriter, Element node) {
			return DataFileUtils.findBestValue(outWriter, node, TARGET, false);
		}

		public static String getType(Queryable outWriter, Element node) {
			return DataFileUtils.findBestValue(outWriter, node, TYPE, true);
		}
		
		public static String getColDelimiter(Queryable outWriter, Element node) {
			return DataFileUtils.findBestValue(outWriter, node, COL_DELIMITER, true);
		}
		
		public static String getRowDelimiter(Queryable outWriter, Element node) {
			return DataFileUtils.findBestValue(outWriter, node, ROW_DELIMITER, true);
		}

		public static int getRows(Queryable outWriter, Element node) {
			return getInt(outWriter, node, ROWS);
		}

		public static int getCols(Queryable outWriter, Element node) {
			return getInt(outWriter, node, COLS);
		}
	}

	public static final class TABLE_ATTRS {
		public static final String ROWS = ARRAY_ATTRS.ROWS;
		
		public static final String COLUMNS = "columns";
		
		public static int getRows(Queryable outWriter, Element node) {
			return getInt(outWriter, node, ROWS);
		}

		public static Element getColumns(Element node) {
			return node.getChild(COLUMNS);
		}
	}

	public static final class BRANCH_ATTRS {
	}

	private String tag;

	DataFileElements(String tag) {
		this.tag = tag;
	}

	/**
	 * Retrieves the tag for this type of element.
	 * 
	 * @return the element's tag
	 */
	public String getTag() {
		return tag;
	}
	

	
	public static int getInt(Queryable queryable, Element node, String target) {
		if (node.getAttribute(target) == null) {
			return -1;
		}

		String bestValue = DataFileUtils.findBestValue(queryable, node, target);
		return (Integer) DataTypeHandler.INT.convert(bestValue);
	}
}
