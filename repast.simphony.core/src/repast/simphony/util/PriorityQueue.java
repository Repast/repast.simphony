package repast.simphony.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A priority queue where the items are ordered according to a
 * specified comparator.
 *
 * This priority queue uses a binary heap algorithm as described in Mark Allen Weis,
 * _Algorithms, Data Structures, and Problem Solving with C++_, chapter 20.
 *
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class PriorityQueue<T>  {

  private int maxSize, origMax;
  private int currentSize = 0;
  private boolean orderOk = true;
	private T minValue;

  // the array functions as a binary tree where for any element at i, then left child is
  // at 2i and right child is at 2i+1, and parent at i / 2.
  private Object[] array;
	private Comparator<T> comp;


  /**
   * Creates a PriorityQueue with a default initial size of 13.
   * The minValue param is expected to less than any value put in the
   * queue.
   *
   * @param minValue the absolute minimum value put in the
   * queue.
   * @param comp the comparator used to order the items
   * in the queue.
   */
  public PriorityQueue (T minValue, Comparator<T> comp) {
    this (minValue, comp, 13);
  }

  /**
   * Creates a PriorityQueue with the specified initial size. The
   * minValue param is expected to less than any value put in the
   * queue.
   *
   * @param minValue the absolute minimum value put in the
   * queue.
   * @param comp the comparator used to order the items
   * in the queue.
   * @param size the intial size of the queue
   */
  public PriorityQueue (T minValue, Comparator<T> comp, int size) {
    maxSize = size;
    origMax = size;
    allocateArray (size);
	  this.comp = comp;
	  this.minValue = minValue;
    array[0] = minValue;
  }

  // checks whether or not we need to resize the
  // array backing store
  private void checkSize () {
    if (currentSize == maxSize) {
      Object[] old = array;
      allocateArray (maxSize * 2);
      System.arraycopy (old, 0, array, 0, old.length);
      // just to be sure
      old = null;
      maxSize *= 2;
    }
  }

  // move the hold down the tree
  // in order to maintain heap order.
  private void percolateDown (int hole) {
    if (currentSize > 0) {
      int child;
      T tmp = (T)array[hole];
      for (; hole * 2 <= currentSize; hole = child) {
        child = hole * 2;
        if (child != currentSize && comp.compare((T)array[child + 1], (T)array[child]) < 0)
          child++;

        if (comp.compare((T)array[child], tmp) < 0)
          array[hole] = array[child];
        else
          break;
      }

      array[hole] = tmp;
    }
  }

  /**
   * Insert the specified item into the queue.
   */
  public void insert (T obj) {
    // if the order is messed up already don't
    // worry about fixing it, just toss the action
    if (!orderOk) {
      toss (obj);
      return;
    }

    checkSize ();
    // percolate the hold up through the tree to maintain heap order
    int hole = ++currentSize;
    if (hole != 1) {

      for (; comp.compare((T)obj, (T)array[hole / 2]) < 0; hole /= 2)
        array[hole] = array[hole / 2];
    }
    array[hole] = obj;
  }

  /**
   * Get the minimum element from the queque without
   * removing it from the queue. If heap order is not currently
   * correct, the heap will be fixed before the minimum is returned.
   *
   * @return the minimum element from the queque
   */
  public T peekMin () {
    if (currentSize == 0)
      throw new NoSuchElementException ("Queue is Empty");
    if (!orderOk) fixHeap ();
    return (T)array[1];
  }

  /**
   * Remove the minimum element from the queque and return it.
   * If heap order is not currently correct, the heap will be fixed before the
   * minimum is returned.
   *
   * @return the minimum element from the queque
   */
  public T popMin () {
    T a = peekMin ();
    array[1] = array[currentSize--];
    percolateDown (1);
    return a;
  }

  /**
   * Insert the specified item into the queue without maintain heap order.
   *
   * @param obj the item to insert into the queue
   */
  public void toss (T obj) {
    checkSize ();
    array[++currentSize] = obj;

    if (currentSize != 1) {
      // is action < its parent node.
      if (comp.compare(obj, (T)array[currentSize / 2]) < 0) orderOk = false;
    }
  }

  /**
   * Removes all elements from the queue.
   */
  public void clear () {
    currentSize = 0;
    orderOk = true;
    allocateArray (origMax);
    maxSize = origMax;
    array[0] = minValue;
  }

  /**
   * Reinstate the heap order. This will fix the tree,
   * ordering it correctly.
   */
  public void fixHeap () {
    for (int i = currentSize / 2; i > 0; i--) {
      percolateDown (i);
    }
    orderOk = true;
  }

  /**
   * Returns true if the queue is empty, otherwise false.
   *
   * @return true if the queue is empty, otherwise false.
   */
  public boolean isEmpty () {
    return currentSize == 0;
  }

  /**
   * Gets the number of elements in the queue.
   * @return the number of elements in the queue.
   */
  public int size () {
    return currentSize;
  }

  // resize the backing store array to the new size.
  private void allocateArray (int newMaxSize) {
    array = new Object[newMaxSize + 1];
  }
}
