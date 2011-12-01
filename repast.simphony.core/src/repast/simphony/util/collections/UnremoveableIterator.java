package repast.simphony.util.collections;

import java.util.Iterator;

/**
 * An iterator that delegates its iterator operations to another iterator, but
 * disallows removal.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class UnremoveableIterator<T> implements Iterator<T>, Iterable<T> {

	private Iterator<T> iter;

	public UnremoveableIterator(Iterator<T> iter) {
		this.iter = iter;
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other
	 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
	 * rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		return iter.hasNext();
	}

	/**
	 * Returns the next element in the iteration.  Calling this method
	 * repeatedly until the {@link #hasNext()} method returns false will
	 * return each element in the underlying collection exactly once.
	 *
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	public T next() {
		return iter.next();
	}

	/**
	 * Removes from the underlying collection the last element returned by the
	 * iterator (optional operation).  This method can be called only once per
	 * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
	 * the underlying collection is modified while the iteration is in
	 * progress in any way other than by calling this method.
	 *
	 * @throws UnsupportedOperationException if the <tt>remove</tt>
	 *                                       operation is not supported by this Iterator.
	 * @throws IllegalStateException         if the <tt>next</tt> method has not
	 *                                       yet been called, or the <tt>remove</tt> method has already
	 *                                       been called after the last call to the <tt>next</tt>
	 *                                       method.
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove is not supported on this iterator");
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		return this;
	}
}
