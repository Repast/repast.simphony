package repast.simphony.integration;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import simphony.util.messages.MessageCenter;

public class DataFileWriter implements Writer, Queryable {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(DataFileWriter.class);
	
	private Document descriptorDocument;
	
	private String descriptorFileName;

	private Queryable dataSource; 
	
	private RandomAccessWriter writer;

	private String destFileName;

	private EscapeConverter newLineConverter;

	
	public DataFileWriter() {
		this(null);
	}
	
	public DataFileWriter(Object source) {
		this(new BeanQueryer(source));
	}
	
	public DataFileWriter(Queryable dataSource) {
		this.dataSource = dataSource;
	}
	
	public void write() throws JDOMException, IOException {
		intializeForWrite();

		handleElement(dataSource.getRoot(), descriptorDocument.getRootElement());
		handleChildren(dataSource.getRoot(), descriptorDocument.getRootElement());
		
		shutdownWrite();
	}

	private void loadDescriptorDoc() throws JDOMException, IOException {
		descriptorDocument = DataFileUtils.loadDescriptor(descriptorFileName);
	}

	private void handleElement(Object dataParent, Element dataDescriptor) throws IOException {
		DataFileElementWriter.getElementHandler(dataDescriptor).handle(this, dataParent, dataDescriptor);
	}

	private void handleChildren(Object dataParent, Element dataDescriptor) throws IOException {
		@SuppressWarnings("unchecked")
		List<Element> childNodes = dataDescriptor.getChildren();

		for (int i = 0; i < childNodes.size(); i++) {
			try {
				handleElement(dataParent, childNodes.get(i));
			} catch (RuntimeException ex) {
				msgCenter.error("Error working on node" + childNodes.get(i), ex);
				throw ex;
			}
		}
	}

	private void intializeForWrite() throws JDOMException, IOException {
		writer = new RandomAccessWriter(destFileName);
		loadDescriptorDoc();
	}
	
	private void shutdownWrite() {
		writer.close();
	}


	public void setNewLineConverter(EscapeConverter newLineConverter) {
		this.newLineConverter = newLineConverter;
	}

	public EscapeConverter getNewLineConverter() {
		return newLineConverter;
	}

	public void writeValueDelimiter(Object data, String delimiter) {
		writer.write(convertNewLines(data.toString()));
		writer.write(convertNewLines(delimiter));
	}

	private String convertNewLines(String string) {
		return newLineConverter.convert(string);
	}

	public void writeValuePattern(Object data, String type, String pattern) {
		// TODO: cache/pool these formatters, make sure to clear their buffer though
		Formatter formatter = new Formatter();
		formatter.format(pattern, DataTypeHandler.getDataType(type).convert(data.toString()));
		writer.write(convertNewLines(formatter.toString()));
	}
	
	public void setDestFileName(String fileName) {
		this.destFileName = fileName;
	}

	public String getDestFileName() {
		return destFileName;
	}
	
	public void setDescriptorFileName(String descriptorFileName) {
		this.descriptorFileName = descriptorFileName;
	}
	
	public String getDescriptorFileName() {
		return descriptorFileName;
	}

	public void mark() throws IOException {
		writer.mark();
	}

	public void popMark() {
		writer.popMark();
	}

	public void reset() throws IOException {
		writer.reset();
	}

	public Object selectNode(String path) {
		return dataSource.selectNode(path);
	}

	public Object selectNode(Object curContext, String path) {
		return dataSource.selectNode(curContext, path);
	}

	public List<?> selectNodes(String path) {
		return dataSource.selectNodes(path);
	}

	public List<?> selectNodes(Object curContext, String path) {
		return dataSource.selectNodes(curContext, path);
	}

	public Object getRoot() {
		return dataSource.getRoot();
	}

	public Object getValue(Object o) {
		return dataSource.getValue(o);
	}

	public void setWrittenObject(Queryable queryable) {
		this.dataSource = queryable;
	}
}
