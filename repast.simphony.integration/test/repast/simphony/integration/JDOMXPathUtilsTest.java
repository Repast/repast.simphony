/*CopyrightHere*/
package repast.simphony.integration;

import junit.framework.TestCase;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Tests for the {@link repast.simphony.integration.JDOMXPathUtils) class.
 * 
 * @author Jerry Vos
 */
public class JDOMXPathUtilsTest extends TestCase {

	private Element subElement;

	private Element rootElement;

	private Document document;

	private DataContent subContent;

	private DataContent rootContent;

	@Override
	protected void setUp() throws Exception {
		document = new Document();
		rootElement = new Element("root");
		rootContent = new DataContent("rootContent");
		rootElement.addContent(rootContent);

		document.setRootElement(rootElement);
		subElement = new Element("sub");
		subContent = new DataContent("subContent");
		subElement.addContent(subContent);
		rootElement.addContent(subElement);
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMXPathUtils.getValue(Object)'
	 */
	public void testGetValue() {
		assertNull(JDOMXPathUtils.getValue(null));
		Object val = "val";
		assertEquals(val, JDOMXPathUtils.getValue(val));

		Element element = new Element("element");
		element.addContent(new DataContent(val));
		assertEquals(val, JDOMXPathUtils.getValue(element));

		Element emptyElement = new Element("element");
		assertNull(JDOMXPathUtils.getValue(emptyElement));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMXPathUtils.evalXPaths(String, Element)'
	 */
	public void testEvalXPaths() throws JDOMException {
		assertEquals(subElement, JDOMXPathUtils.evalXPaths("/root/sub", rootElement).get(0));
		assertEquals(rootElement, JDOMXPathUtils.evalXPaths("/root", rootElement).get(0));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMXPathUtils.evalXPath(String, Element)'
	 */
	public void testEvalXPath() throws JDOMException {
		assertEquals(subElement, JDOMXPathUtils.evalXPath("/root/sub", rootElement));
		assertEquals(rootElement, JDOMXPathUtils.evalXPath("/root", rootElement));
		assertNull(JDOMXPathUtils.evalXPath("/rootasdfasf", rootElement));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMXPathUtils.evalXPathStrings(String,
	 * Element)'
	 */
	public void testEvalXPathStrings() throws JDOMException {
		assertEquals(subContent.getValue(), JDOMXPathUtils.evalXPathStrings("/root/sub", rootElement).get(0));
		assertEquals(rootContent.getValue(), JDOMXPathUtils.evalXPathStrings("/root", rootElement).get(0));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMXPathUtils.evalXPathString(String, Element)'
	 */
	public void testEvalXPathString() throws JDOMException {
		assertEquals(subContent.getValue(), JDOMXPathUtils.evalXPathString("/root/sub", rootElement));
		assertEquals(rootContent.getValue(), JDOMXPathUtils.evalXPathString("/root", rootElement));
	}

}
