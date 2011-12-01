/*CopyrightHere*/
package repast.simphony.engine.graph;

import java.util.ArrayList;

import junitx.util.PrivateAccessor;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import repast.simphony.TestUtils;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

/**
 * Tests for {@link repast.simphony.engine.graph.NetworkTraverser}.
 *
 * @author Jerry Vos
 */
public class NeighborhooderTraverserTest extends MockObjectTestCase {
	Mock mockNeighborhooder;
	NetworkTraverser visitor;
	
	@Override
	protected void setUp() throws Exception {
		mockNeighborhooder = mock(Network.class);
		visitor = new NetworkTraverser((Network) mockNeighborhooder.proxy());
	}
	
	/*
	 * Test method for 'repast.simphony.engine.graph.NeighborhooderVisitor.NeighborhooderVisitor(NeighborhooderNeighborhooder)'
	 */
	public void testNeighborhooderVisitor() throws NoSuchFieldException {
		Network net = (Network) mock(Network.class).proxy();
		
		NetworkTraverser tempVisitor = new NetworkTraverser(net);
		
		assertSame(net, PrivateAccessor.getField(tempVisitor, "network"));
	}

	/*
	 * Test method for 'repast.simphony.engine.graph.NeighborhooderVisitor.getSuccessors(GraphParams<Object>)'
	 */
	@SuppressWarnings("unchecked")
	public void testGetSuccessorsSimple() {
		ArrayList successors = new ArrayList();
		successors.add(new Object());
		successors.add(new Object());
		
		GraphParams graphParams = new GraphParams(new Object(), new Object(), 100);
		
		mockNeighborhooder.expects(atLeastOnce()).method("getSuccessors").with(eq(graphParams.getCurrentNode())).will(returnValue(successors));
		
		assertTrue(TestUtils.collectionsContentsEqual(successors.iterator(), visitor.getSuccessors(graphParams.getPreviousNode(), graphParams.getCurrentNode())));
	}

	/*
	 * Test method for 'repast.simphony.engine.graph.NeighborhooderVisitor.getSuccessors(GraphParams<Object>)'
	 */
	@SuppressWarnings("unchecked")
	public void testGetSuccessorsCyclic() {
		ArrayList successors = new ArrayList();
		successors.add(new Object());
		successors.add(new Object());
		
		Object first = new Object();

		successors.add(first);
		
		ArrayList expected = new ArrayList();
		expected.add(successors.get(0));
		expected.add(successors.get(1));
		
		GraphParams graphParams = new GraphParams(first, new Object(), 100);
		
		mockNeighborhooder.expects(atLeastOnce()).method("getSuccessors").with(eq(graphParams.getCurrentNode())).will(returnValue(successors));
		
		assertTrue(TestUtils.collectionsContentsEqual(expected.iterator(), visitor.getSuccessors(graphParams.getPreviousNode(), graphParams.getCurrentNode())));
	}

	/*
	 * Test method for 'repast.simphony.engine.graph.NeighborhooderVisitor.getDistance(GraphParams<Object>)'
	 */
	@SuppressWarnings("unchecked")
	public void testGetDistance() {
		GraphParams graphParams = new GraphParams(new Object(), new Object(), 100);
		
		mockNeighborhooder.expects(atLeastOnce()).method("getEdge").with(
				eq(graphParams.getPreviousNode()), eq(graphParams.getCurrentNode())).will(
				returnValue(new RepastEdge(graphParams.getPreviousNode(), graphParams
						.getCurrentNode(), true, 100.2)));
		
		assertEquals(100.2, visitor.getDistance(graphParams.getPreviousNode(), graphParams.getCurrentNode()));
	}

}
