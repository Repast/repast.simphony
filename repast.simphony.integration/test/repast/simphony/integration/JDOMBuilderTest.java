/*CopyrightHere*/
package repast.simphony.integration;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;

public class JDOMBuilderTest extends TestCase {

	private JDOMBuilder builder;
	private Document doc;

	@Override
	protected void setUp() throws Exception {
		builder = new JDOMBuilder();
		builder.initialize();
		doc = builder.getWrittenObject();
	}
	
	void assertAt(String name) throws NoSuchFieldException {
		assertEquals(name, ((Element) PrivateAccessor.getField(builder, "curElement")).getName());
	}
	
	void assertAt(Element element) throws NoSuchFieldException {
		assertEquals(element, PrivateAccessor.getField(builder, "curElement"));
	}
	
	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.createAndGoInto(String)'
	 */
	public void testCreateAndGoInto() throws NoSuchFieldException {
		builder.createAndGoInto("subElement");
		assertAt("subElement");
		assertTrue(doc.getRootElement().getChildren().size() == 1);
		assertEquals("subElement", ((Element) doc.getRootElement().getChildren().get(0)).getName());
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.goUp()'
	 */
	public void testGoUp() throws NoSuchFieldException {
		builder.createAndGoInto("subElement");
		builder.goUp();
		assertAt(doc.getRootElement());
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.goRoot()'
	 */
	public void testGoRoot() throws NoSuchFieldException {
		builder.createAndGoInto("subElement");
		builder.goRoot();
		assertAt(doc.getRootElement());
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.writeValue(String, Object)'
	 */
	public void testWriteValue() {
		builder.writeValue("valName", "value");
		assertTrue(doc.getRootElement().getChildren().size() == 1);
		assertEquals("value", ((Content) ((Element) doc.getRootElement().getChild("valName")).getContent().get(0)).getValue());

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.getWrittenObject()'
	 */
	public void testGetWrittenObject() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.detach()'
	 */
	public void testDetach() {
		builder.createAndGoInto("subElement");
		builder.detach();
		assertTrue(doc.getRootElement().getChildren().size() == 0);
	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.detach(Iterable<Element>)'
	 */
	public void testDetachIterableOfElement() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.selectNode(String)'
	 */
	public void testSelectNodeString() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.selectNode(Object, String)'
	 */
	public void testSelectNodeObjectString() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.selectNodes(String)'
	 */
	public void testSelectNodesString() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.selectNodes(Object, String)'
	 */
	public void testSelectNodesObjectString() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.getRoot()'
	 */
	public void testGetRoot() {

	}

	/*
	 * Test method for 'repast.simphony.integration.JDOMBuilder.getValue(Object)'
	 */
	public void testGetValue() {

	}

}
