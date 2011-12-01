/*CopyrightHere*/
package repast.simphony.integration;

import static repast.simphony.integration.DataFileElements.FILE_DEF_ATTRS.getNewLineType;
import static repast.simphony.integration.DataFileElements.RECORD_ATTRS.getCount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Element;

import repast.simphony.integration.ArrayDataTypeHandler.ArrayHandler;
import simphony.util.messages.MessageCenter;

/**
 * This handles the reading of elements of a data file. Each element of the enum will handle a
 * different type of element (the element with the same tag as the enum element).
 * 
 * @author Jerry Vos
 */
public enum DataFileElementReader {
	/**
	 * Handler for FileDef tags
	 */
	FILE_DEF(DataFileElements.FILE_DEF.getTag()) {
		@Override
		public Object[] handle(DataFileReader parserInst, OutputBuilder<?, ?> outWriter,
				Element nodeToHandle) {
			parserInst.setNewLineConverter(NewLinePatternConverter
					.getNewLineConverter(getNewLineType(nodeToHandle)));

			return null;
		}

	},
	/**
	 * Handler for data tags.
	 */
	DATA(DataFileElements.DATA.getTag()) {
		@Override
		public Object[] handle(DataFileReader parserInst, OutputBuilder<?, ?> outWriter, Element node) {
			// Create the data node and add it to the data node's parent. Have to do it
			// first so that we can use xpath
			// Element dataNode = new Element("tempName");

			// dataParent.addContent(dataNode);
			try {
				String type = DataFileElements.DATA_ATTRS.getType(outWriter, node);
				String target = DataFileElements.DATA_ATTRS.getTarget(outWriter, node);
				int length = DataFileElements.DATA_ATTRS.getLength(outWriter, node);
				String delimiter = getDelimiter(parserInst.getNewLineConverter(), outWriter, node,
						type);
				String pattern = getPattern(parserInst.getNewLineConverter(), outWriter, node);

				if (target == null) {
					RuntimeException ex = new RuntimeException("Target cannot be null.\n\t"
							+ "type(" + type + "), target(" + target + "), length(" + length
							+ "), delimiter(" + delimiter + "), pattern(" + pattern + ")");

					// outWriter.detach();
					msgCenter.error(ex.getMessage(), ex);
					throw ex;
				}

				if (delimiter == null && pattern == null && length <= 0) {
					pattern = getDefaultPattern(type);
				}
				
				Object data;
				if (length <= 0 && delimiter == null && pattern == null) {
					// NOTE: as of right now this can't happen because of default delimiters.
					msgCenter
							.warn("DATA.handle: found an invalid element. An element must contain a length, delimiter, or pattern. \n\t"
									+ "type("
									+ type
									+ "), target("
									+ target
									+ "), length("
									+ length
									+ "), delimiter("
									+ delimiter
									+ "), pattern("
									+ pattern + ")");

					// outWriter.detach();
					return null;
				} else if (length > 0) {
					data = getDataWithLength(parserInst, type, length);
				} else if (pattern != null) {
					data = getDataWithPattern(parserInst, type, pattern);
				} else {
					data = getDataWithDelimiter(parserInst, type, delimiter);
				}

				if (target.equals(DataFileUtils.IGNORE_DATA_TARGET)) {
					return null;
				}
				Object output = outWriter.writeValue(target, data);

				// System.out.println(element + ((DataContent)
				// element.getContent().get(0)).getValue());
				msgCenter.debug("Store (" + target + ", " + data + "): " + data.getClass() + " ");
				return new Object[] { output };
			} catch (RuntimeException ex) {
				// dataNode.detach();
				throw ex;
			}
		}

		private String getDefaultPattern(String type) {
			if (type == null) {
				return null;
			} else if (type.equals("int")) {
				return INT_PATTERN;
			} else if (type.equals("double")) {
				return DOUBLE_PATTERN;
			}
			return null;
		}

		private String getPattern(NewLinePatternConverter newLineConverter, Queryable outWriter,
				Element node) {
			String pattern = DataFileElements.DATA_ATTRS.getPattern(outWriter, node);
			if (pattern != null) {
				return newLineConverter.convert(pattern);
			}

			return null;
		}

		private String getDelimiter(NewLinePatternConverter newLineConverter, Queryable outWriter,
				Element node, String type) {
			String delimiter = DataFileElements.DATA_ATTRS.getDelimiter(outWriter, node, type);
			
			if (delimiter == null) {
				delimiter = getDefaultDelimiter(type);
			}
			
			if (delimiter != null) {
				return newLineConverter.convert(delimiter);
			}

			return null;
		}
		
		private String getDefaultDelimiter(String type) {
			if (type == null) {
				return null;
			} else if (type.equals("string")) {
				return STRING_DELIMITER;
			}
			return null;
		}

		private Object getDataWithDelimiter(DataFileReader parserInst, String type, String delimiter) {
			return DataTypeHandler.getDataType(type).convert(
					parserInst.getValueDelimiter(delimiter));
		}

		private Object getDataWithPattern(DataFileReader parserInst, String type, String pattern) {
			return DataTypeHandler.getDataType(type).convert(parserInst.getValuePattern(pattern));
		}

		private Object getDataWithLength(DataFileReader parserInst, String type, int length) {
			return DataTypeHandler.getDataType(type).convert(parserInst.getValueLength(length));
		}

	},
	/**
	 * Handler for record elements.
	 */
	RECORD(DataFileElements.RECORD.getTag()) {
		@Override
		public Object[] handle(DataFileReader parserInst, OutputBuilder<?, ?> outWriter,
				Element nodeToHandle) throws IOException {
			// create a temporary element that we'll stick in the tree to provide a context for
			// xpath'ing
			// Element repeatForXPathPurposes = new Element("tempNameRepeat");
			// dataParent.addContent(repeatForXPathPurposes);

			int count = getCount(outWriter, nodeToHandle);
			String target = DataFileElements.RECORD_ATTRS.getTarget(outWriter, nodeToHandle);

			// now we can remove the temp element
			// repeatForXPathPurposes.detach();

			ArrayList<Object> repeatNodes = null;
			try {
				if (count > 0) {
					repeatNodes = repeatCount(parserInst, outWriter, nodeToHandle, target, count);
				} else {
					repeatNodes = repeatTilFail(parserInst, outWriter, nodeToHandle, target);
				}

				return repeatNodes.toArray(new Object[repeatNodes.size()]);
			} catch (RuntimeException ex) {
				throw ex;
			} catch (IOException ex) {
				throw ex;
			}
		}

		private ArrayList<Object> repeatCount(DataFileReader parserInst, OutputBuilder<?, ?> outWriter,
				Element nodeToHandle, String target, int count) throws IOException {
			ArrayList<Object> records = new ArrayList<Object>(count);

			@SuppressWarnings("unchecked")
			List<Element> subContent = nodeToHandle.getChildren();
			
			for (int i = 0; i < count; i++) {
				Object created = outWriter.createAndGoInto(target);

				// Pass along the sub elements
				for (Element subNode : subContent) {
					getElementHandler(subNode).handle(parserInst, outWriter, subNode);
				}

				outWriter.goUp();
				records.add(created);
			}

			return records;
		}

		private ArrayList<Object> repeatTilFail(DataFileReader parserInst, OutputBuilder<?, ?> outWriter,
				Element nodeToHandle, String target) throws IOException {
			ArrayList<Object> records = new ArrayList<Object>();

			Element branchNode = new Element(BRANCH.tag);

			// move the children to the branch node
			@SuppressWarnings("unchecked")
			List<Element> children = new ArrayList<Element>(nodeToHandle.getChildren());
			for (Element child : children) {
				child.detach();
				branchNode.addContent(child);
			}

			do {
				Object created = outWriter.createAndGoInto(target);

				Object[] subData = getElementHandler(branchNode).handle(parserInst, outWriter,
						branchNode);
				if (subData == null) {
					// the nested branch failed, so the branch is over and we're done repeating
					outWriter.detach();
					break;
				}

				outWriter.goUp();
				records.add(created);
			} while (true);

			// restore the tree
			for (Element child : children) {
				child.detach();
				nodeToHandle.addContent(child);
			}

			if (records.size() == 0) {
				return null;
			}
			return records;
		}

	},
	TABLE(DataFileElements.TABLE.getTag()) {
		@Override
		public Object[] handle(DataFileReader parserInst, OutputBuilder<?, ?> outWriter, Element node) throws IOException {
			Element columnsElement = DataFileElements.TABLE_ATTRS.getColumns(node);
			@SuppressWarnings("unchecked")
			// avoid concurrent modification errors
			List<Element> dataElements = new ArrayList<Element>((List<Element>) columnsElement.getChildren());
			
			try {
				// have to detach so I can add them to the record element
				for (Element dataElement : dataElements) {
					dataElement.detach();
				}
				
				Element recordElement = getRecordElement(parserInst, outWriter, node, dataElements);
				
				// delegate off to the RECORD handler
				List<Element> rows = fakeRecord(parserInst, outWriter, recordElement);
				
				Hashtable<Element, ArrayHandler> table = new Hashtable<Element, ArrayHandler>(rows.size());

				// setup the array builders
				for (Element dataElement : dataElements) {
					String type = DataFileElements.DATA_ATTRS.getType(outWriter, dataElement);
					table.put(dataElement, ArrayDataTypeHandler.getDataType(type).getHandler(rows.size(), 1));
				}
				
				// retrieve the values from the DOM and load it into arrays
				for (int i = 0; i < rows.size(); i++) {
					Element row = rows.get(i);
					
					for (Element dataElement : dataElements) {
						String value = JDOMXPathUtils.getValue(row.getChild(DataFileElements.DATA_ATTRS.getTarget(outWriter, dataElement)));
						table.get(dataElement).setValue(i, 0, value);
					}
				}
				
				Object[] returnVal = new Object[dataElements.size()];
				
				for (int i = 0; i < dataElements.size(); i++) {
					Element dataElement = dataElements.get(i);
					
					// write out the array and store it
					returnVal[i] = outWriter.writeValue(DataFileElements.DATA_ATTRS.getTarget(
							outWriter, dataElement), table.get(dataElement).getValue());
					
					// restore the descriptor tree
					dataElement.detach();
					columnsElement.addContent(dataElement);
				}
				
				return returnVal;
			} catch (RuntimeException ex) {
				for (Element dataElement : dataElements) {
					dataElement.detach();
				}
				columnsElement.addContent(dataElements);
				throw ex;
			}
		}
		
		private Element getRecordElement(DataFileReader parserInst, OutputBuilder<?, ?> outWriter,
				Element tableElement, List<Element> dataElements) {
			Element element = new Element(DataFileElements.RECORD.getTag());
			
			int rows = DataFileElements.TABLE_ATTRS.getRows(outWriter, tableElement);
			
			element.setAttribute(DataFileElements.RECORD_ATTRS.TARGET, FAKE_TARGET);
			if (rows >= 0) {
				element.setAttribute(DataFileElements.RECORD_ATTRS.COUNT, Integer.toString(rows));
			}

			element.addContent(dataElements);
			
			return element;
		}
	},
	ARRAY(DataFileElements.ARRAY.getTag()) {
		@Override
		public Object[] handle(DataFileReader parserInst, OutputBuilder<?, ?> outWriter, Element node) throws IOException {
			String type = DataFileElements.ARRAY_ATTRS.getType(outWriter, node);
			String target = DataFileElements.ARRAY_ATTRS.getTarget(outWriter, node);
			int cols = DataFileElements.ARRAY_ATTRS.getCols(outWriter, node);
			
			Element recordElement = getRecordElement(parserInst, outWriter, node);
			// delegate off to the RECORD handler
			List<Element> rows = fakeRecord(parserInst, outWriter, recordElement);
			
			// grab the data from the JDOM and load it into the real writer
			ArrayHandler handler = ArrayDataTypeHandler.getDataType(type).getHandler(rows.size(), cols);
			
			for (int row = 0; row < rows.size(); row++) {
				Element curRow = rows.get(row);
				@SuppressWarnings("unchecked")
				List<Element> curCols = curRow.getChildren();
				
				for (int j = 0; j < curCols.size(); j++) {
					handler.setValue(row, j, ((DataContent) curRow.getChild(getColName(j))
							.getContent(0)).getValue());
				}
			}
			
			return new Object[] { outWriter.writeValue(target, handler.getValue()) };
		}
		
		private String getColName(int col) {
			return "col" + col;
		}
		
		private Element getRecordElement(DataFileReader parserInst, OutputBuilder<?, ?> outWriter, Element arrayElement) {
			Element element = new Element(DataFileElements.RECORD.getTag());
			
			String target = DataFileElements.ARRAY_ATTRS.getTarget(outWriter, arrayElement);
			String type = DataFileElements.ARRAY_ATTRS.getType(outWriter, arrayElement);
			String rowDelimiter = DataFileElements.ARRAY_ATTRS.getRowDelimiter(outWriter, arrayElement);
			String colDelimiter = DataFileElements.ARRAY_ATTRS.getColDelimiter(outWriter, arrayElement);
			int rows = DataFileElements.ARRAY_ATTRS.getRows(outWriter, arrayElement);
			
			element.setAttribute(DataFileElements.RECORD_ATTRS.TARGET, target);
			if (rows >= 0) {
				element.setAttribute(DataFileElements.RECORD_ATTRS.COUNT, Integer.toString(rows));
			}
			
			int cols = DataFileElements.ARRAY_ATTRS.getCols(outWriter, arrayElement);
			
			int i = 0;
			for (; i < cols - 1; i++) {
				element.addContent(getDataElement(getColName(i), type, colDelimiter));
			}
			element.addContent(getDataElement(getColName(cols - 1), type, rowDelimiter));
			
			return element;
		}
		
		private Element getDataElement(String target, String type, String delimiter) {
			Element element = new Element(DataFileElements.DATA.getTag());
			
			element.setAttribute(DataFileElements.DATA_ATTRS.TARGET, target);
			element.setAttribute(DataFileElements.DATA_ATTRS.DELIMITER, delimiter);
			element.setAttribute(DataFileElements.DATA_ATTRS.TYPE, type);
			
			return element;
		}
	},
	/**
	 * Handler for branch elements.
	 */
	BRANCH(DataFileElements.BRANCH.getTag()) {
		@Override
		public Object[] handle(DataFileReader parserInst, OutputBuilder outWriter,
				Element nodeToHandle) throws IOException {
			ArrayList<Object> subData = new ArrayList<Object>();
			try {
				parserInst.mark();

				@SuppressWarnings("unchecked")
				List<Element> subContent = new ArrayList(nodeToHandle.getChildren());

				for (Element subNode : subContent) {
					Object[] data = getElementHandler(subNode).handle(parserInst, outWriter,
							subNode);
					if (data != null) {
						// add the resultant data
						for (Object sub : data) {
							subData.add(sub);
						}
					}
				}

				parserInst.popMark();

				return subData.toArray(new Object[subData.size()]);
			} catch (Exception ex) {
				// remove any branch stuff
				outWriter.detach(subData);
				// ex.printStackTrace();
				parserInst.restore();
				msgCenter.debug("BRANCH.handle: branch failed", ex);
				return null;
			}
		}

	};

	public static String STRING_DELIMITER = "\n";

	public static String DOUBLE_PATTERN = "(\\+|\\-|)\\d*(\\.\\d*|\\.|)(E(\\+|-|)\\d+|)";
	public static String INT_PATTERN = "(\\+|\\-|)\\d+";
	
	private static final String FAKE_TARGET = "FAKE_TARGET";
	
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(DataFileElementReader.class);

	public final String tag;

	DataFileElementReader(String descriptor) {
		this.tag = descriptor;
	}

	public static DataFileElementReader getElementHandler(Element nodeToHandle) {
		String nodeName = nodeToHandle.getName();
		for (DataFileElementReader handler : values()) {
			if (handler.tag.equalsIgnoreCase(nodeName)) {
				return handler;
			}
		}
		RuntimeException ex = new RuntimeException(
				"Error reading node's type. Could not interpret nodeName(" + nodeName + ").");
		msgCenter.error(ex.getMessage(), ex);
		throw ex;
	}

	/**
	 * Handles the specified node. Any elements to be added to the data tree should be added in this
	 * method and also returned by this method.
	 * 
	 * @param parserInst
	 *            the instance of the reader that this is working with
	 * @param OutputBuilder
	 *            the writer to use for writing
	 * @param nodeToHandle
	 *            the node providing data
	 * @return any elements added to the data tree
	 * @throws IOException
	 *             when there is an error reading
	 */
	public abstract Object[] handle(DataFileReader parserInst, OutputBuilder<?, ?> outWriter,
			Element nodeToHandle) throws IOException;
	
	
	@SuppressWarnings("unchecked")
	public static List<Element> fakeRecord(DataFileReader parserInst, OutputBuilder<?, ?> outWriter, Element recordElement)  throws IOException {
		JDOMBuilder jdomWriter = new JDOMBuilder();
		jdomWriter.initialize();

		// delegate off to the RECORD handler
		getElementHandler(recordElement).handle(parserInst, jdomWriter, recordElement);
		
		return (List<Element>) jdomWriter.selectNodes("/fileDef/*");
	}
}