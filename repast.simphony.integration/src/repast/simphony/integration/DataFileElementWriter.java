/*CopyrightHere*/
package repast.simphony.integration;

import static repast.simphony.integration.DataFileElements.DATA_ATTRS.getDelimiter;
import static repast.simphony.integration.DataFileElements.DATA_ATTRS.getLength;
import static repast.simphony.integration.DataFileElements.DATA_ATTRS.getPattern;
import static repast.simphony.integration.DataFileElements.DATA_ATTRS.getType;
import static repast.simphony.integration.DataFileElements.FILE_DEF_ATTRS.getNewLineType;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import repast.simphony.integration.ArrayDataTypeHandler.ArrayHandler;
import simphony.util.messages.MessageCenter;

/**
 * 
 * 
 * @author Jerry Vos
 */
public enum DataFileElementWriter {
	FILE_DEF(DataFileElements.FILE_DEF.getTag()) {
		@Override
		public void handle(DataFileWriter writerInst, Object parentContext, Element nodeToHandle) {
			writerInst.setNewLineConverter(EscapeConverter
					.getNewLineConverter(getNewLineType(nodeToHandle)));
		}

	},
	DATA(DataFileElements.DATA.getTag()) {

		@Override
		public void handle(DataFileWriter writerInst, Object parentContext, Element dataDescriptor) {
			writeData(writerInst, parentContext, dataDescriptor, null);
		}

	},
	RECORD(DataFileElements.RECORD.getTag()) {
		@Override
		public void handle(DataFileWriter writerInst, Object parentContext, Element dataDescriptor)
				throws IOException {
			int count = DataFileElements.RECORD_ATTRS.getCount(writerInst, dataDescriptor);
			String target = DataFileElements.RECORD_ATTRS.getTarget(writerInst, dataDescriptor);
			
			Iterable<?> targetNodes = null;
			try {
				
				targetNodes = writerInst.selectNodes(parentContext, target);
			} catch (Exception e) {
				msgCenter.warn("Could not find any data to write, skipping record with target" +
						"(" + target + ").", e);
				return;
			}

			if (targetNodes == null) {
				msgCenter.warn("Could not find any data to write, skipping record with target" +
						"(" + target + ").");
				return;
			}

			if (count > 0) {
				repeatCount(writerInst, parentContext, dataDescriptor, targetNodes, count);
			} else {
				repeatOnAll(writerInst, parentContext, dataDescriptor, targetNodes);
			}
		}

		private void repeatCount(DataFileWriter writerInst, Object dataParent,
				Element dataDescriptor, Iterable<?> targetNodes, int count) throws IOException {

			Iterator<?> iter = targetNodes.iterator();
			for (int i = 0; i < count; i++) {
				// Pass along the sub elements
				executeRepeat(writerInst, iter.next(), dataDescriptor);
			}
		}

		private void repeatOnAll(DataFileWriter writerInst, Object dataParent,
				Element dataDescriptor, Iterable<?> targetNodes) throws IOException {

			for (Object target : targetNodes) {
				// Pass along the sub elements
				executeRepeat(writerInst, target, dataDescriptor);
			}
		}

		private void executeRepeat(DataFileWriter writerInst, Object repeatDataObject,
				Element repeatDescriptor) throws IOException {
			@SuppressWarnings("unchecked")
			List<Element> childDescriptors = repeatDescriptor.getChildren();

			for (Element subDescriptor : childDescriptors) {
				getElementHandler(subDescriptor).handle(writerInst, repeatDataObject,
						subDescriptor);
			}
		}
	},
	ARRAY(DataFileElements.ARRAY.getTag()) {
		@Override
		public void handle(DataFileWriter writerInst, Object parentContext, Element dataDescriptor)
			throws IOException {
			
			int rows = DataFileElements.ARRAY_ATTRS.getRows(writerInst, dataDescriptor);
			int cols = DataFileElements.ARRAY_ATTRS.getCols(writerInst, dataDescriptor);
			String colDelimiter = DataFileElements.ARRAY_ATTRS.getColDelimiter(writerInst, dataDescriptor);
			String rowDelimiter = DataFileElements.ARRAY_ATTRS.getRowDelimiter(writerInst, dataDescriptor);
			
			String target = DataFileElements.ARRAY_ATTRS.getTarget(writerInst, dataDescriptor);
			String type = DataFileElements.ARRAY_ATTRS.getType(writerInst, dataDescriptor);
			
			Object data = null;
			try {
				data = writerInst.getValue(writerInst.selectNode(parentContext, target));
			} catch (Exception e) {
				msgCenter.warn("Could not find any data to write, skipping array with target" +
						"(" + target + ").", e);
				return;
			}

			if (data == null) {
				msgCenter.warn("Could not find any data to write, skipping array with target" +
						"(" + target + ").");
				return;
			}
			
			ArrayHandler handler = ArrayDataTypeHandler.getDataType(type).getHandler(rows, cols, data);

			if (rows <= 0) {
				rows = handler.getRowCount();
			}
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols - 1; j++) {
					writerInst.writeValueDelimiter(handler.getValueString(i, j), colDelimiter);
				}
				writerInst.writeValueDelimiter(handler.getValueString(i, cols - 1), rowDelimiter);
			}
		}
	},
	TABLE(DataFileElements.TABLE.getTag()) {
		@SuppressWarnings("unchecked")
		@Override
		public void handle(DataFileWriter writerInst, Object parentContext, Element dataDescriptor)
			throws IOException {
			
			int rows = DataFileElements.TABLE_ATTRS.getRows(writerInst, dataDescriptor);
			Element columns = DataFileElements.TABLE_ATTRS.getColumns(dataDescriptor);
			
			try {
				Map<Element, ArrayHandler> handlerMap = new Hashtable<Element, ArrayHandler>();
				List<Element> dataElements = (List<Element>) columns.getChildren();
				
				int minLength = Integer.MAX_VALUE;
				for (Element dataElement : dataElements) {
					String type = DataFileElements.DATA_ATTRS.getType(writerInst, dataElement);
					String target = DataFileElements.DATA_ATTRS.getTarget(writerInst, dataElement);
					
					Object value = writerInst.getValue(writerInst.selectNode(parentContext, target));;

					handlerMap.put(dataElement, ArrayDataTypeHandler.getDataType(type).getHandler(0, 1, value));
					
					minLength = Math.min(minLength, handlerMap.get(dataElement).getRowCount());
				}
				
				if (rows <= 0) {
					rows = minLength;
				}
				
				for (int i = 0; i < rows; i++) {
					for (Element dataElement : dataElements) {
						writeData(writerInst, parentContext, dataElement, handlerMap.get(
								dataElement).getValueString(i, 0));
					}
				}
				
			} catch (Exception e) {
				msgCenter.warn("Exception while working with table, skipping table", e);
				return;
			}
		}
	},
	BRANCH(DataFileElements.BRANCH.getTag()) {
		@Override
		public void handle(DataFileWriter writerInst, Object parentContext, Element dataDescriptor)
				throws IOException {

			writerInst.mark();
			try {
				@SuppressWarnings("unchecked")
				List<Element> childDescriptors = dataDescriptor.getChildren();

				for (Element subDescriptor : childDescriptors) {
					getElementHandler(subDescriptor).handle(writerInst, parentContext, subDescriptor);
				}

				writerInst.popMark();
			} catch (Exception e) {
				writerInst.reset();
				// branch failed
			}
		}

	};

	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(DataFileElementWriter.class);

	public final String tag;

	DataFileElementWriter(String descriptor) {
		this.tag = descriptor;
	}

	public static DataFileElementWriter getElementHandler(Element nodeToHandle) {
		String nodeName = nodeToHandle.getName();
		for (DataFileElementWriter handler : values()) {
			if (handler.tag.equalsIgnoreCase(nodeName)) {
				return handler;
			}
		}
		RuntimeException ex = new RuntimeException(
				"Error reading node's type. Could not interpret nodeName(" + nodeName + ").");
		msgCenter.error(ex.getMessage(), ex);
		throw ex;
	}
	
	private static void writeData(DataFileWriter writerInst, Object parentContext, Element dataDescriptor, Object data) {
		try {
			String type = getType(writerInst, dataDescriptor);
			String target = DataFileElements.DATA_ATTRS.getTarget(writerInst, dataDescriptor);

			if (target == null) {
				RuntimeException ex = new RuntimeException("Target cannot be null.\n\t"
						+ "type(" + type + "), target(" + target + ")");

				msgCenter.error(ex.getMessage(), ex);
				throw ex;
			}

			int length = getLength(writerInst, dataDescriptor);
			String delimiter = getDelimiter(writerInst, dataDescriptor, type);
			String pattern = getPattern(writerInst, dataDescriptor);

			if (data == null && target.equals(DataFileUtils.IGNORE_DATA_TARGET)) {

				switch (DataTypeHandler.getDataType(type)) {
				case STRING:
					if (pattern != null) {
						data = getPattern(writerInst, dataDescriptor);
					} else if (length > 0) {
						data = getDefaultDataLength(length, type);
					} else {
						data = "";
						msgCenter
								.warn("Writing a data value and found no pattern nor length.  Defaulting to empty data.");
					}

					break;
				default:
					if (length > 0) {
						data = getDefaultDataLength(length, type);
					} else {
						// TODO: error
					}
				}
			} else if (data == null) {
				data = getData(writerInst, parentContext, target);

			}

			if (pattern != null) {
				writeDataPattern(writerInst, data, pattern, type);
			} else {
				if (delimiter == null) {
					delimiter = "";
				}
				writeDataDelimiter(writerInst, data, delimiter);
			}

		} catch (RuntimeException ex) {
			throw ex;
		}
	}
	
	private static Object getDefaultDataLength(int length, String type) {
		switch (DataTypeHandler.getDataType(type)) {
		case STRING:
			return getStringLength(length, ' ');
		case DOUBLE:
			return getStringLength(length, '0');
		case INT:
			return getStringLength(length, '0');
		}
		return null;
	}

	private static Object getStringLength(int length, char charToUse) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			builder.append(charToUse);
		}
		return builder.toString();
	}
	
	private static Object getData(Queryable dataSource, Object curContext, String target) {
		return dataSource.getValue(dataSource.selectNode(curContext, target));
	}

	private static void writeDataDelimiter(DataFileWriter writerInst, Object data, String delimiter) {
		writerInst.writeValueDelimiter(data, delimiter);
	}

	private static void writeDataPattern(DataFileWriter writerInst, Object data, String pattern,
			String type) {
		writerInst.writeValuePattern(data, type, pattern);
	}
	
	/**
	 * Handles the specified nodeToParse. Any elements to be added to the data tree should be added
	 * in this method and also returned by this method.
	 * 
	 * @param writerInst
	 *            the instance of the parser that this is working on
	 * @param dataParent
	 *            the parent Element in the data tree that new data should be appended too
	 * @param nodeToHandle
	 *            the node providing data
	 * @return any elements added to the data tree
	 * @throws Exception
	 */
	public abstract void handle(DataFileWriter writerInst, Object parentContext,
			Element dataDescriptor) throws IOException;
}