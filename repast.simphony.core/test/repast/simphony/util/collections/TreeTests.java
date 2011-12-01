package repast.simphony.util.collections;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.util.collections.NaryTree;
import repast.simphony.util.collections.Tree;
import repast.simphony.util.collections.TreeVisitor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Tests on IndexedIterables
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class TreeTests extends TestCase {

	private Tree<Integer> tree;
	private List<Integer> vals = new ArrayList<Integer>();

	public void setUp() {
		vals.clear();
		for (int i = 0; i < 11; i++) {
			vals.add(i);
		}
		tree = new NaryTree<Integer>(vals.get(0));
		for (int i = 1; i < 4; i++) {
			tree.addNode(vals.get(0), vals.get(i));
		}

		tree.addNode(vals.get(1), vals.get(4));
		tree.addNode(vals.get(1), vals.get(5));

		tree.addNode(vals.get(2), vals.get(6));
		tree.addNode(vals.get(6), vals.get(7));

		tree.addNode(vals.get(3), vals.get(8));
	}


	public void testVisit() {
		final List<Integer> ints = new ArrayList<Integer>();
		tree.preOrderTraversal(new TreeVisitor<Integer>() {
			public void visit(Integer node) {
				ints.add(node);
			}
		});

		assertEquals(vals.get(0), ints.get(0));
		assertEquals(vals.get(1), ints.get(1));
		assertEquals(vals.get(4), ints.get(2));
		assertEquals(vals.get(5), ints.get(3));
		assertEquals(vals.get(2), ints.get(4));
		assertEquals(vals.get(6), ints.get(5));
		assertEquals(vals.get(7), ints.get(6));
		assertEquals(vals.get(3), ints.get(7));
		assertEquals(vals.get(8), ints.get(8));

	}

	public void testSimple() {
		assertEquals(vals.get(0), tree.getRoot());
		assertEquals(9, tree.size());
	}

	public void testChildren() {
		Iterator<Integer> iter = tree.getChildren(vals.get(0)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(1), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(2), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(3), iter.next());
		assertTrue(!iter.hasNext());

		iter = tree.getChildren(vals.get(6)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(7), iter.next());
		assertTrue(!iter.hasNext());
	}

	public void testReplace() {
		tree.replaceNode(vals.get(2), vals.get(10));
		Iterator<Integer> iter = tree.getChildren(vals.get(0)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(1), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(10), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(3), iter.next());
		assertTrue(!iter.hasNext());

		iter = tree.getChildren(vals.get(10)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(6), iter.next());
		assertTrue(!iter.hasNext());

		boolean pass = true;
		try {
			tree.addNode(vals.get(2), vals.get(9));
			// we get here then we now the arg exception wasn't thrown and it
			// should have been
			pass = false;
		} catch (IllegalArgumentException ex) {
		}
		finally {
			assertTrue(pass);
		}
	}

	public void testSort() {

		tree.sortChildren(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				// do a reverse comparison
				return o2 - o1;
			}
		});

		Iterator<Integer> iter = tree.getChildren(vals.get(0)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(3), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(2), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(1), iter.next());
		assertTrue(!iter.hasNext());

		iter = tree.getChildren(vals.get(1)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(5), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(4), iter.next());
		assertTrue(!iter.hasNext());
	}

	public void testRemove() {
		tree.removeNode(vals.get(1));
		assertEquals(6, tree.size());

		Iterator<Integer> iter = tree.getChildren(vals.get(0)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(2), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(3), iter.next());
		assertTrue(!iter.hasNext());

		// put the 1 back and remove 3
		tree.addNode(vals.get(0), vals.get(1));
		assertEquals(7, tree.size());

		tree.removeNode(vals.get(3));
		assertEquals(5, tree.size());

		iter = tree.getChildren(vals.get(0)).iterator();
		assertTrue(iter.hasNext());
		assertEquals(vals.get(2), iter.next());
		assertTrue(iter.hasNext());
		assertEquals(vals.get(1), iter.next());
		assertTrue(!iter.hasNext());

		tree.removeNode(vals.get(7));
		assertEquals(4, tree.size());

		iter = tree.getChildren(vals.get(6)).iterator();
		assertTrue(!iter.hasNext());
	}

	public static junit.framework.Test suite() {
		return new TestSuite(TreeTests.class);
	}
}
