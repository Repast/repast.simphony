package repast.simphony.util.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;

/**
 * An iterable that iterates over a collection of objects at random.
 * The "length" of the iterable may be less than the number of objects
 * in the collection.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class RandomIterable<T> implements Iterator<T>, Iterable<T> {

	protected int index;
	protected List<T> list;
	protected boolean[] returned;
	protected int numToReturn;
  protected int numReturned;

	/**
	 * Creates a RandomIterable that will iterate over count number of objects 
	 * in the specified IndexedIterable.
	 *
	 * @param iter
	 * @param count
	 *
	 */
	public RandomIterable(IndexedIterable<T> iter, long count) {
		
		list = new ArrayList<T>(iter.size());
		
		for (T obj : iter)
			list.add(obj);

//		Collections.shuffle(list);
		SimUtilities.shuffle(list, RandomHelper.getUniform());

		if (count != Long.MAX_VALUE){
			int size = (int)count > iter.size() ? iter.size() : (int)count;

			list = list.subList(0, size);
		}
				
		numToReturn = list.size();
		returned = new boolean[numToReturn];  // record of returned objects
		index = 0;                            // current list index
		numReturned = 0;                      // num returned so far
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other
	 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
	 * rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
	  return numReturned < numToReturn; 
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
		int i;
		
		// increment the index and check if the next object has already been
		// marked as returned - which can happen if the next object in the list
		// was removed before it was returned.
		do{
			i = index;
			index++;
		}
		while (returned[i]);
		
		returned[i] = true;   // mark object as returned
		numReturned++;
		
		return list.get(i);
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
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * Marks the target as returned if it exists in the object list.
	 * 
	 * @param target the object to remove from the list.
	 */
	protected void removeEvent(T target){
		int i  = list.indexOf(target);

		// if the target object is in the list and is hasn't already been returned,
		// record the returned flag as true and decrement the num to return
		if (i != -1 && !returned[i]){
		  returned[i] = true;
		  numToReturn--;
		}
	}
}