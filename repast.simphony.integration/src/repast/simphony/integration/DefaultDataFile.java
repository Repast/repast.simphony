/*CopyrightHere*/
package repast.simphony.integration;

import java.io.IOException;

import org.jdom.JDOMException;

import simphony.util.messages.MessageCenter;

public class DefaultDataFile implements DataFile {
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(DefaultDataFile.class);
	
	private DataFileReader parser;
	
	private DataFileWriter writer;
	
	
	public DefaultDataFile() {
		parser = new DataFileReader(new BeanBuilder());
		writer = new DataFileWriter();
	}	
	
	public String getReadDescriptorFileName() {
		return parser.getDescriptorFileName();
	}

	public void setReadDescriptorFileName(String readDescriptorFileName) {
		parser.setDescriptorFileName(readDescriptorFileName);
	}

	public void write() throws JDOMException, IOException {
		writer.write();
	}

	public void read() throws IOException, JDOMException {
		parser.read();
	}

	public String getWriteDescriptorFileName() {
		return writer.getDescriptorFileName();
	}

	public void setWriteDescriptorFileName(String descriptorFileName) {
		writer.setDescriptorFileName(descriptorFileName);
	}

	public void setReadFileName(String fileName) {
		parser.setFileToParseName(fileName);
	}

	public Object getReadObject() {
		return parser.getParseResult();
	}

	public void setWriteFileName(String fileName) {
		writer.setDestFileName(fileName);
	}

	public void setWriter(Writer writer) {
		if (!(writer instanceof DataFileWriter)) {
			RuntimeException ex = new RuntimeException("DefaultDataFile only works with DataFileWriters");
			msgCenter.error("Wrong class type given(" + writer.getClass().getName() + ")", ex);
			throw ex;
		}
		this.writer = (DataFileWriter) writer;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setReader(Reader reader) {
		if (!(reader instanceof DataFileReader)) {
			RuntimeException ex = new RuntimeException("DefaultDataFile only works with DataFileReaders");
			msgCenter.error("Wrong class type given(" + reader.getClass().getName() + ")", ex);
			throw ex;
		}
		this.parser = (DataFileReader) reader;
	}

	public Reader getReader() {
		return parser;
	}

	public void setWrittenObject(Queryable queryable) {
		writer.setWrittenObject(queryable);
	}

}
