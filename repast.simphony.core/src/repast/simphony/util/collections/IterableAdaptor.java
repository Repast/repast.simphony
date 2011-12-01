package repast.simphony.util.collections;

import java.util.Iterator;

/**
 * Adapts an iterator into an iterable.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class IterableAdaptor<T> implements Iterable<T> {

	private Iterator<T> iter;

	/**
	 * Creates an IterableAdaptor for the specified iterator.
	 * 
	 * @param iter
	 */
	public IterableAdaptor(Iterator<T> iter) {
		this.iter = iter;
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		return iter;
	}
}
