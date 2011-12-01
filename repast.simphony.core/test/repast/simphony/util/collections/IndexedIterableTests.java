package repast.simphony.util.collections;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.engine.environment.RunState;

import java.util.*;

/**
 * Tests on IndexedIterables
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class IndexedIterableTests extends TestCase {

	public void setUp() {
		// initializes the RunState so the random helper can use it.
		RunState.init();
	}

	public void testListIndexedIterable() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}

		IndexedIterable<Integer> iter = new ListIndexedIterable<Integer>(list);
		assertEquals(list.size(), iter.size());
		for (int i = 0; i < list.size(); i++) {
			assertEquals(list.get(i), iter.get(i));
		}

		int i = 0;
		for (Integer iVal : iter) {
			assertEquals(new Integer(i++), iVal);
		}
	}

	public void testCompositeIterable() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
		}
		IndexedIterable<Integer> iter = new ListIndexedIterable<Integer>(list);

		List<Integer> list2 = new ArrayList<Integer>();
		for (int i = 20; i < 100; i++) {
			list2.add(i);
		}

		CompositeIndexedIterable<Integer> comp = new CompositeIndexedIterable<Integer>();
		comp.addIndexedIterable(iter);
		comp.addList(list2);

		assertEquals(list.size() + list2.size(), comp.size());

		for (int i = 0; i < 100; i++) {
			assertEquals(new Integer(i), comp.get(i));
		}

		Iterator<Integer> cIter = comp.iterator();
		for (int i = 0; i < 100; i++) {
			assertEquals(new Integer(i), comp.get(i));
			assertEquals(new Integer(i), cIter.next());
		}
	}

	public void testRandomIterable() {
		List<Integer> list = new ArrayList<Integer>();
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 20; i++) {
			list.add(i);
			set.add(i);
		}
		IndexedIterable<Integer> iter = new ListIndexedIterable<Integer>(list);

		List<Integer> list2 = new ArrayList<Integer>();
		for (int i = 20; i < 100; i++) {
			list2.add(i);
			set.add(i);
		}

		CompositeIndexedIterable<Integer> comp = new CompositeIndexedIterable<Integer>();
		comp.addIndexedIterable(iter);
		comp.addList(list2);

		RandomIterable<Integer> rIter = new RandomIterable<Integer>(comp, comp.size());
		int count = 0;
		for (Integer val : rIter) {
			assertTrue(set.remove(val));
			count++;
		}
		assertEquals(comp.size(), count);
		assertEquals(0, set.size());

		for (int i = 0; i < 100; i++) {
			set.add(i);
		}

		rIter = new RandomIterable<Integer>(comp, 30);
		for (Integer val : rIter) {
			assertTrue(set.remove(val));
		}
		assertEquals(70, set.size());

		set.clear();
		for (int i = 0; i < 100; i++) {
			set.add(i);
		}

		// ask for more than comp contains, should just get comp amount.
		rIter = new RandomIterable<Integer>(comp, comp.size() + 1);
		count = 0;
		for (Integer val : rIter) {
			count++;
		}
		assertEquals(comp.size(), count);

	}

	public static junit.framework.Test suite() {
		return new TestSuite(IndexedIterableTests.class);
	}
}
