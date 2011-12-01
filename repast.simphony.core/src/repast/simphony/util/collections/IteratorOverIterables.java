package repast.simphony.util.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract base class for creating an Iterator that wraps object that returns
 * multiple iterables. The iterator will iterate over all the objects returned
 * by each iterable.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class IteratorOverIterables<T> implements Iterator<T> {

  protected Iterator<T> nextIter;
  protected T nextObj;

  protected T getNextObj() {
    T obj = null;
    if (nextIter == null) {
      Iterable<T> iterable = getNext();
      if (iterable == null)
        return null;
      nextIter = iterable.iterator();
    }
    while (obj == null) {
      if (nextIter.hasNext())
        obj = nextIter.next();
      else {
        Iterable<T> iterable = getNext();
        if (iterable == null)
          break;
        nextIter = iterable.iterator();
      }
    }
    return obj;
  }

  /**
   * Initializes the iterator. This needs to be called in the constructor of the
   * implementing subclass.
   */
  protected void init() {
    Iterable<T> iter = getNext();
    if (iter == null)
      nextObj = null;
    else {
      nextIter = iter.iterator();
      nextObj = getNextObj();
    }
  }

  /**
   * Gets the next Iterable from which to retrieve the next object.
   * 
   * @return the next Iterable from which to retrieve the next object.
   */
  protected abstract Iterable<T> getNext();

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other words,
   * returns <tt>true</tt> if <tt>next</tt> would return an element rather than
   * throwing an exception.)
   * 
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public boolean hasNext() {
    return nextObj != null;
  }

  /**
   * Returns the next element in the iteration. Calling this method repeatedly
   * until the {@link #hasNext()} method returns false will return each element
   * in the underlying collection exactly once.
   * 
   * @return the next element in the iteration.
   * @throws java.util.NoSuchElementException
   *           iteration has no more elements.
   */
  public T next() {
    if (nextObj == null)
      throw new NoSuchElementException();
    T tmp = nextObj;
    nextObj = getNextObj();
    return tmp;
  }

  /**
   * Removes from the underlying collection the last element returned by the
   * iterator (optional operation). This method can be called only once per call
   * to <tt>next</tt>. The behavior of an iterator is unspecified if the
   * underlying collection is modified while the iteration is in progress in any
   * way other than by calling this method.
   * 
   * @throws UnsupportedOperationException
   *           if the <tt>remove</tt> operation is not supported by this
   *           Iterator.
   * @throws IllegalStateException
   *           if the <tt>next</tt> method has not yet been called, or the
   *           <tt>remove</tt> method has already been called after the last
   *           call to the <tt>next</tt> method.
   */
  public void remove() {
    if (nextIter != null)
      nextIter.remove();
    else
      throw new IllegalStateException();
  }
}
