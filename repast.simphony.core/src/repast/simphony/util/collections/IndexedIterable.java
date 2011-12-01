package repast.simphony.util.collections;

/**
 * An iterable that also provides indexed access to the underlying collection.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface IndexedIterable<T> extends Iterable<T> {

	/**
	 * Gets the element at the specified position in this IndexedIterable. Note that
	 * this method is should be used for random access to the collection. iterator()
	 * is the preferred way to iterate through this IndexedIterable. 
	 *
	 * @param index the index of the element to return
	 * @return the element at the specified position in this IndexedIterable.
	 * @throws IndexOutOfBoundsException if the given index is out of range
   * 		  (<tt>index &lt; 0 || index &gt;= size()</tt>).
	 */
	T get(int index);

	/**
	 * Gets the number of elements in this IndexedIterable.
	 * @return the number of elements in this IndexedIterable.
	 */
	int size();

}
