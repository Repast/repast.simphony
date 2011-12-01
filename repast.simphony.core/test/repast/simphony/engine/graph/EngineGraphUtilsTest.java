/*CopyrightHere*/
package repast.simphony.engine.graph;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.collections15.Predicate;

import repast.simphony.TestUtils;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkFactoryFinder;

/**
 * Tests for {@link repast.simphony.util.collections.CollectionUtils}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
@SuppressWarnings("unchecked")
public class EngineGraphUtilsTest extends TestCase {
	DefaultContext context;

	repast.simphony.space.graph.Network top;

	Object a = "a";

	Object b = "b";

	Object c = "c";

	Object d = "d";

	Object e = "e";

	Object f = "f";

	Object g = "g";

	ArrayList expectedOrder;

	@Override
	protected void setUp() throws Exception {
		// build a tree like so:
		/*
		 * a / \ b c / / \ d e f / g
		 */

		context = new DefaultContext();
		context.add(a);
		context.add(b);
		context.add(c);
		context.add(e);
		context.add(f);
		context.add(d);
		context.add(g);
		top = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
						"", context, true);

		top.addEdge(a, b);
		top.addEdge(a, c);
		top.addEdge(b, d);
		top.addEdge(c, e);
		top.addEdge(c, f);
		top.addEdge(e, g);

		// the order of the nodes technically can be anything as long as all
		// the same level is checked before the next level, but this order is
		// what the alg originally gave
		expectedOrder = new ArrayList();
		expectedOrder.add(a);
		expectedOrder.add(c);
		expectedOrder.add(b);
		expectedOrder.add(e);
		expectedOrder.add(f);
		expectedOrder.add(d);
		expectedOrder.add(g);
	}

	/*
	 * Test method for
	 * 'repast.simphony.util.collections.CollectionUtils.breadthFirstMap(Executor<T>,
	 * Traverser<T>, T) <T>'
	 */
	public void testBreadthFirstMapCollection() {
		final ArrayList finalValues = new ArrayList();

		ArrayList<Integer> originalValues = new ArrayList<Integer>();
		originalValues.add(0);
		originalValues.add(1);
		originalValues.add(2);
		originalValues.add(3);

		IterableTraverser traverser = new IterableTraverser<Integer>(
				originalValues);

		EngineGraphUtilities.breadthFirstMap(new Executor<Integer>() {
			public void execute(Integer toExecuteOn) {
				finalValues.add(toExecuteOn * 2);
			}
		}, traverser, originalValues.get(0));

		ArrayList expectedValues = new ArrayList();
		expectedValues.add(0);
		expectedValues.add(2);
		expectedValues.add(4);
		expectedValues.add(6);

		assertTrue(TestUtils.collectionsContentsEqualOrdered(expectedValues,
				finalValues));

	}

	/*
	 * Test method for
	 * 'repast.simphony.util.collections.CollectionUtils.breadthFirstMap(Executor<T>,
	 * Traverser<T>, T) <T>'
	 */
	public void testBreadthFirstMapTree() {
		final ArrayList finalValues = new ArrayList();

		EngineGraphUtilities.breadthFirstMap(new Executor<Object>() {
			public void execute(Object toExecuteOn) {
				finalValues.add(toExecuteOn);
			}
		}, new NetworkTraverser(top), a);

		assertTrue(TestUtils.collectionsContentsEqual(expectedOrder,
				finalValues));

	}

	/*
	 * Test method for
	 * 'repast.simphony.util.collections.CollectionUtils.breadthFirstSearch(Rule<T>,
	 * Traverser<T>, T) <T>'
	 */
	@SuppressWarnings("all")
	public void testBreadthFirstSearch() {
		final ArrayList resultantOrder = new ArrayList();

		Object result = EngineGraphUtilities.breadthFirstSearch(new Predicate() {
			public boolean evaluate(Object toCheck) {
				resultantOrder.add(toCheck);

				return toCheck == g;
			}
		}, new NetworkTraverser(top), a);

		assertTrue(TestUtils.collectionsContentsEqual(expectedOrder,
				resultantOrder));
		// assertTrue(expectedOrder.size() == resultantOrder.size());
		// for (int i = 0; i < expectedOrder.size(); i++) {
		// assertSame(expectedOrder.get(i), resultantOrder.get(i));
		// }

		assertSame(g, result);
	}

}
