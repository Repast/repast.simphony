package repast.simphony.util.collections;

import org.apache.commons.collections15.iterators.IteratorChain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An IndexedIterable that adapts multiple IndexedItebles to behave as a single IndexedIterable.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class CompositeIndexedIterable<T> implements IndexedIterable<T> {

	private static class Range<T> {
		private int start, end;
		private IndexedIterable<T> list;

		public Range(int start, IndexedIterable<T> list) {
			this.list = list;
			this.start = start;
			this.end = start + list.size();
		}

		public T get(int index) {
			if (index >= start && index < end) return list.get(index - start);
			return null;
		}
	}



	private List<Range<T>> ranges = new ArrayList<Range<T>>();
	private Range<T> currentRange = new Range<T>(-1, new ListIndexedIterable<T>(new ArrayList<T>()));
	private int start = 0;

	/**
	 * Add an indexed iterable to this CompositeIndexedIterable.
	 * @param iter
	 */
	public void addIndexedIterable(IndexedIterable<T> iter) {
		ranges.add(new Range<T>(start, iter));
		start += iter.size();
	}

	/**
	 * Add a List to this CompositeIndexedIterable.
	 * @param list
	 */
	public void addList(List<T> list) {
		ranges.add(new Range<T>(start, new ListIndexedIterable<T>(list)));
		start += list.size();
	}

	/**
	 * Gets the element at the specified position in this IndexedIterable. Note that
	 * this method is should be used for random access to the collection. iterator()
	 * is the preferred way to iterate through this IndexedIterable.
	 *
	 * @param index the index of the element to return
	 * @return the element at the specified position in this IndexedIterable.
	 * @throws IndexOutOfBoundsException if the given index is out of range
	 *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>).
	 */
	public T get(int index) {
		if (index < 0 || index > start - 1)
			throw new IndexOutOfBoundsException("Index: " + index + " is out of bounds [0, " + (start - 1) + "]");
		T obj = currentRange.get(index);
		if (obj != null) return obj;

		for (int i = 0, n = ranges.size(); i < n; i++) {
			Range<T> range = ranges.get(i);
			obj = range.get(index);
			if (obj != null) {
				currentRange = range;
				return obj;
			}
		}

		// should never get here
		return null;
	}

	/**
	 * Gets the number of elements in this IndexedIterable.
	 *
	 * @return the number of elements in this IndexedIterable.
	 */
	public int size() {
		return start;
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		IteratorChain<T> iter = new IteratorChain<T>();
		for (int i = 0, n = ranges.size(); i < n; i++) {
			Range<T> range = ranges.get(i);
			iter.addIterator(range.list.iterator());
		}
		return iter;
	}
}
