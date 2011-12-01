/*CopyrightHere*/
package repast.simphony.integration;

import java.util.Iterator;

import junit.framework.TestCase;

import org.jdom.Element;

/**
 * Simple tests for the {@link repast.simphony.integration.DOMTraverser} class.
 * 
 * @author Jerry Vos
 */
public class DOMTraverserTest extends TestCase {
	private DOMTraverser traverser;

	private Element top;

	private Element bottom;

	@Override
	protected void setUp() throws Exception {
		traverser = new DOMTraverser();
		top = new Element("top");
		bottom = new Element("bottom");
		top.addContent(bottom);
	}

	/*
	 * Test method for 'repast.simphony.integration.DOMTraverser.getSuccessors(Element, Element)'
	 */
	public void testGetSuccessors() {
		Iterator iter = traverser.getSuccessors(null, top);
		assertEquals(bottom, iter.next());
		assertFalse(iter.hasNext());

		iter = traverser.getSuccessors(null, bottom);
		assertFalse(iter.hasNext());
	}

	/*
	 * Test method for 'repast.simphony.integration.DOMTraverser.getDistance(Element, Element)'
	 */
	public void testGetDistance() {
		assertEquals(1.0, traverser.getDistance(top, bottom));
		assertTrue(Double.POSITIVE_INFINITY == traverser.getDistance(new Element("blah"),
				new Element("blah")));
	}

}
