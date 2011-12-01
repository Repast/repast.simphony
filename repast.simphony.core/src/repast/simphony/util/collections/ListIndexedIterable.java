package repast.simphony.util.collections;

import java.util.Iterator;
import java.util.List;

/**
 * Adapts a List to the IndexedIterable interface.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ListIndexedIterable<T> implements IndexedIterable<T> {

	private List<T> list;

	/**
	 * Creates a ListIndexedIterable that adapts the specified list to the
	 * IndexedIterable interface.
	 *
	 * @param list
	 */
	public ListIndexedIterable(List<T> list) {
		this.list = list;
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
		return list.get(index);
	}

	/**
	 * Gets the number of elements in this IndexedIterable.
	 *
	 * @return the number of elements in this IndexedIterable.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		return list.iterator();
	}
}
