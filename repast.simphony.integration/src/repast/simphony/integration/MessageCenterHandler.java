package repast.simphony.integration;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import simphony.util.messages.MessageCenter;

/**
 * A handler for XML parsing errors that forwards the errors on to the MessageCenter.
 * 
 * @author Jerry Vos
 */
public class MessageCenterHandler extends DefaultHandler {
	private static MessageCenter msgCenter = MessageCenter
			.getMessageCenter(MessageCenterHandler.class);

	private String fileName;

	/**
	 * Constructs a default MessageCenterHandler. This will not include a file name in its messages.
	 */
	public MessageCenterHandler() {
		this(null);
	}

	/**
	 * Constructs a MessageCenterHandler that will include the specified file name in the messages
	 * it outputs.
	 * 
	 * @param fileName
	 *            the name of the file this handler is working on, to be included in any messages
	 *            output
	 */
	public MessageCenterHandler(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		if (fileName == null) {
			msgCenter.error("Error while parsing XML file", e);
		} else {
			msgCenter.error("Error while parsing XML file (" + fileName + ")", e);
		}

	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		if (fileName == null) {
			msgCenter.error("Fatal error while parsing XML file", e);
		} else {
			msgCenter.error("Fatal error while parsing XML file (" + fileName + ")", e);
		}
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		if (fileName == null) {
			msgCenter.warn("Warning while parsing XML file", e);
		} else {
			msgCenter.error("Warning while parsing XML file (" + fileName + ")", e);
		}
	}

}
