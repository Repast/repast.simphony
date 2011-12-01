/*CopyrightHere*/
package repast.simphony.integration;

import junit.framework.TestCase;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Some simple tests to keep the unit test % up. Just make sure they don't throw exceptions or
 * anything wierd.
 * 
 * @author Jerry Vos
 */
public class MessageCenterHandlerTest extends TestCase {

	private MessageCenterHandler handler1;

	private MessageCenterHandler handler2;

	@Override
	protected void setUp() throws Exception {
		handler1 = new MessageCenterHandler("filename");
		handler2 = new MessageCenterHandler();
	}

	/*
	 * Test method for 'repast.simphony.integration.MessageCenterHandler.MessageCenterHandler()'
	 */
	public void testMessageCenterHandler() {
		handler1 = new MessageCenterHandler();
	}

	/*
	 * Test method for
	 * 'repast.simphony.integration.MessageCenterHandler.MessageCenterHandler(String)'
	 */
	public void testMessageCenterHandlerString() {
		handler1 = new MessageCenterHandler("filename");
	}

	/*
	 * Test method for 'repast.simphony.integration.MessageCenterHandler.error(SAXParseException)'
	 */
	public void testErrorSAXParseException() throws SAXException {
		handler1.error(new SAXParseException("asdf", null));
		handler2.error(new SAXParseException("asdf", null));
	}

	/*
	 * Test method for
	 * 'repast.simphony.integration.MessageCenterHandler.fatalError(SAXParseException)'
	 */
	public void testFatalErrorSAXParseException() throws SAXException {
		handler1.fatalError(new SAXParseException("asdf", null));
		handler2.fatalError(new SAXParseException("asdf", null));
	}

	/*
	 * Test method for 'repast.simphony.integration.MessageCenterHandler.warning(SAXParseException)'
	 */
	public void testWarningSAXParseException() throws SAXException {
		handler1.warning(new SAXParseException("asdf", null));
		handler2.warning(new SAXParseException("asdf", null));
	}

}
