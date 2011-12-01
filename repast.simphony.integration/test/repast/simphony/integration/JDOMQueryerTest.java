/*CopyrightHere*/
package repast.simphony.integration;

import java.util.ArrayList;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

import org.jdom.Document;
import org.jdom.Element;

public class JDOMQueryerTest extends TestCase {

	private Element rootElement;
	private Document rootDocument;
	private JDOMQueryer queryer;
	private Element subElement;
	private String subElementData;

	@Override
	protected void setUp() throws Exception {
		rootElement = new Element("root");
		rootDocument = new Document(rootElement);
		
		queryer = new JDOMQueryer(rootDocument);
		
		subElement = new Element("sub");
		subElementData = "subElementData";
		subElement.addContent(new DataContent(subElementData));
		rootElement.addContent(subElement);
	}
	
	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.JDOMQueryer(Document)'
	 */
	public void testJDOMQueryer() throws NoSuchFieldException {
		assertEquals(PrivateAccessor.getField(queryer, "document"), rootDocument);
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.selectNode(String)'
	 */
	public void testSelectNodeString() {
		assertEquals(subElement, queryer.selectNode("/root/sub"));
		assertEquals(rootElement, queryer.selectNode("/root"));
		assertEquals(rootDocument, queryer.selectNode("/"));
		assertNull(queryer.selectNode("asdf/asdf/asdf/"));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.selectNode(Object, String)'
	 */
	public void testSelectNodeObjectString() {
		assertEquals(subElement, queryer.selectNode(rootElement, "/root/sub"));
		assertEquals(subElement, queryer.selectNode(subElement, "/root/sub"));
		assertEquals(subElement, queryer.selectNode(subElement, "."));
		assertEquals(subElement, queryer.selectNode(rootElement, "sub"));
		assertNull(queryer.selectNode(rootDocument, "sub"));
		
		assertNull(queryer.selectNodes("blah", "sub"));
		
		assertNull(queryer.selectNode(rootDocument, "/[]"));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.selectNodes(String)'
	 */
	@SuppressWarnings("unchecked")
	public void testSelectNodesString() {
		ArrayList values = new ArrayList();
		values.add(subElement);
		assertEquals(values, queryer.selectNodes("//sub"));
		
		values = new ArrayList();
		values.add(rootElement);
		
		assertEquals(values, queryer.selectNodes("/root"));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.selectNodes(Object, String)'
	 */
	@SuppressWarnings("unchecked")
	public void testSelectNodesObjectString() {
		ArrayList values = new ArrayList();
		values.add(subElement);
		assertEquals(values, queryer.selectNodes(rootElement, "//sub"));
		assertEquals(values, queryer.selectNodes(rootElement, "sub"));
		
		values = new ArrayList();
		values.add(rootElement);
		
		assertEquals(values, queryer.selectNodes(rootElement, "/root"));
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.getRoot()'
	 */
	public void testGetRoot() {
		assertEquals(rootElement, queryer.getRoot());
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMQueryer.getValue(Object)'
	 */
	public void testGetValue() {
		assertEquals(subElementData, queryer.getValue(subElement));
	}
	
	public void testGetValue2() {
		assertEquals(subElementData, queryer.getValue(queryer.selectNode("/root/sub")));
	}

}
