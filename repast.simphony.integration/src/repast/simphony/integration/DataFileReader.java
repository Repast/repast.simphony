/*CopyrightHere*/
package repast.simphony.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import repast.simphony.util.RandomAccessScanner;
import simphony.util.messages.MessageCenter;

/**
 * This is the primary class for data file reading. This takes in a descriptor, a file name for the
 * data, and an output writer and will parse the data file according to the descriptor, giving a
 * final result.
 * 
 * @see repast.simphony.integration.OutputBuilder
 * 
 * @author Jerry Vos
 */
public class DataFileReader implements Reader {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(DataFileReader.class);

	private NewLinePatternConverter newLineConverter;

	private String descriptorFileName;

	private String fileToParseName;

	protected Document descriptorDocument;

	private RandomAccessScanner scanner;

	private OutputBuilder<?, ?> outputBuilder;

	/**
	 * Instantiates this reader with the specified {@link OutputBuilder} used to build the object
	 * generated from the file.
	 * 
	 * @param outputBuilder
	 *            used to build the result of the parsing
	 */
	public DataFileReader(OutputBuilder<?, ?> outputBuilder) {
		this.outputBuilder = outputBuilder;
	}
	
	
	/**
	 * Instantiates this reader with the specified {@link OutputBuilder} used to build the object
	 * generated from the file.
	 * 
	 * @param outputBuilder
	 *            used to build the result of the parsing
	 */
	public DataFileReader(Object target) {
		this.outputBuilder = new BeanBuilder(target);
	}

	protected void restore() throws IOException {
		scanner.reset();
		scanner.popMark();
	}

	protected void mark() throws IOException {
		scanner.mark();
	}

	/**
	 * Loads in the xml descriptor file and causes the file to be parsed. After calling this method
	 * (assuming it doesn't throw an exception), the {@link #getParseResult()} method will return a
	 * value built from the read in file.
	 * 
	 * @see #getDescriptorFileName()
	 * @see #getFileToParseName()
	 * @see #getParseResult()
	 */
	public void read() throws IOException, JDOMException {
		loadDescriptor();
		parseFile();
	}

	private void loadDescriptor() throws IOException, JDOMException {
		descriptorDocument = DataFileUtils.loadDescriptor(descriptorFileName);
	}

	private void parseFile() throws IOException {
		initializeForParse();

		handleElement(outputBuilder, descriptorDocument.getRootElement());
		handleChildren(outputBuilder, descriptorDocument.getRootElement());
	}

	private void initializeForParse() throws IOException {
		File fileToParse = new File(fileToParseName);
		if (!fileToParse.exists()) {
			RuntimeException ex = new RuntimeException(
					"DataFileParser.parseFile: Error parsing file (" + fileToParseName + "), "
							+ "the file does not exist.");
			msgCenter.error(ex.getMessage(), ex);
			throw ex;
		}

		FileChannel channel = new FileInputStream(fileToParse).getChannel();
		scanner = new RandomAccessScanner(channel);

		Element rootElement = descriptorDocument.getRootElement();
		if (rootElement.getName() != DataFileElements.FILE_DEF.getTag()) {
			RuntimeException ex = new RuntimeException(
					"DataFileParser.parseFile: Error parsing file (" + fileToParseName + "), "
							+ "the root node's name wasn't '" + DataFileElements.FILE_DEF.getTag() + "'.");
			msgCenter.error(ex.getMessage(), ex);
			throw ex;
		}

		outputBuilder.initialize();
	}

	private void handleElement(OutputBuilder outWriter, Element node) throws IOException {
		getElementHandler(node).handle(this, outWriter, node);
	}

	private void handleChildren(OutputBuilder outWriter, Element node) throws IOException {
		@SuppressWarnings("unchecked")
		List<Element> childNodes = node.getChildren();

		for (int i = 0; i < childNodes.size(); i++) {
			handleElement(outWriter, childNodes.get(i));
		}
	}

	String getValueDelimiter(String delimiter) {
		scanner.useDelimiter(delimiter);

		String scannerNext = scanner.next();

		return scannerNext;
	}

	String getValuePattern(String pattern) {
		String scannerNext = scanner.getNextPattern(pattern);

		return scannerNext;
	}

	String getValueLength(int length) {
		if (length <= 0) {
			// TODO: handle this error
			return null;
		}
		return scanner.getNextLength(length);
	}

	private DataFileElementReader getElementHandler(Element node) {
		return DataFileElementReader.getElementHandler(node);
	}

	public OutputBuilder<?, ?> getoutputBuilder() {
		return outputBuilder;
	}

	public void setoutputBuilder(OutputBuilder<?, ?> outputBuilder) {
		this.outputBuilder = outputBuilder;
	}

	public Object getParseResult() {
		return outputBuilder.getWrittenObject();
	}

	public String getDescriptorFileName() {
		return descriptorFileName;
	}

	public void setDescriptorFileName(String descriptorFileName) {
		this.descriptorFileName = descriptorFileName;
	}

	public String getFileToParseName() {
		return fileToParseName;
	}

	public void setFileToParseName(String fileToParseName) {
		this.fileToParseName = fileToParseName;
	}

	public NewLinePatternConverter getNewLineConverter() {
		return newLineConverter;
	}

	public void setNewLineConverter(NewLinePatternConverter newLineConverter) {
		this.newLineConverter = newLineConverter;
	}

	public void popMark() {
		scanner.popMark();
	}
}
