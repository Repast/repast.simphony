/*CopyrightHere*/
package repast.simphony.engine.controller;

import java.util.ArrayList;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import repast.simphony.TestUtils;
import repast.simphony.context.Context;
import repast.simphony.engine.controller.ContextTraverser;
import repast.simphony.util.collections.IterableAdaptor;

/**
 * Tests for {@link repast.simphony.engine.controller.ContextTraverser}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class ContextTraverserTest extends MockObjectTestCase {

	@SuppressWarnings("unchecked")
	public void testGetSuccessors() {
		Mock mockContext = mock(Context.class);

		ContextTraverser traverser = new ContextTraverser();

		ArrayList list = new ArrayList();
		list.add(mock(Context.class).proxy());

		mockContext.stubs().method("getSubContexts").will(returnValue(list));

		TestUtils.collectionsContentsEqual(list, new IterableAdaptor(traverser
				.getSuccessors(null, (Context) mockContext.proxy())));
	}

	/*
	 * Test method for
	 * 'repast.simphony.engine.graph.ContextTraverser.getDistance(Context, Context)'
	 */
	public void testGetDistance() {
		ContextTraverser traverser = new ContextTraverser();

		assertEquals(0.0, traverser.getDistance(null, null), 0);
	}

}
