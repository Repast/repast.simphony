package repast.simphony.engine.schedule;

import java.util.NoSuchElementException;

/**
 * A priority queue containing ISchedulableAction-s. The actions are ordered
 * according to the return value of their <code>getNextTime()</code> method
 * call.
 * <p>
 * 
 * This priority queue uses a binary heap algorithm as described in Mark Allen
 * Weis, _Algorithms, Data Structures, and Problem Solving with C++_, chapter
 * 20.
 * 
 * @see repast.simphony.engine.schedule.ISchedulableAction
 * 
 * @author Nick Collier
 */
public class ActionQueue {

  static final long serialVersionUID = -5594830901668228513L;

  private int maxSize, origMax;
  private int currentSize = 0;
  private boolean orderOk = true;

  // the number of actions that are in this queue that are model actions
  private int modelActionCount = 0;

  // the array functions as a binary tree where for any element at i, then left
  // child is
  // at 2i and right child is at 2i+1, and parent at i / 2.
  private ISchedulableAction[] array;

  // this is used when an action is removed. We replace the removed
  // AbstractAction
  // with this one. By replacing we don't need to fix the heap which may be time
  // consuming
  static class EmptyAction extends AbstractAction {
    private static final long serialVersionUID = -830235028902826597L;

    EmptyAction(double nextTime) {
      super(ScheduleParameters.createOneTime(nextTime));
    }

    // this should never get called because it never gets added to
    // a group for exection
    public void execute() {
    }

    public void addForExecution(ScheduleGroup group) {
    }

    public boolean isNonModelAction() {
      return true;
    }

    public void setPriority(double priority) {
    }
  }

  // our top most root AbstractAction
  static class DummyAction extends AbstractAction {
    private static final long serialVersionUID = -5139789622934288912L;

    DummyAction() {
      super(ScheduleParameters.createOneTime(Double.NEGATIVE_INFINITY));
    }

    public void execute() {
    }

    public void setPriority(double priority) {
    }
  }

  /**
   * Creates an ActionQueue with a default initial size of 13.
   */
  public ActionQueue() {
    this(13);
  }

  /**
   * Creates an ActionQueue with the specified initial size.
   * 
   * @param size
   *          the intial size of the queue
   */
  public ActionQueue(int size) {
    maxSize = size;
    origMax = size;
    allocateArray(size);
    AbstractAction root = new DummyAction();
    array[0] = root;
  }

  // checks whether or not we need to resize the
  // array backing store
  private void checkSize() {
    if (currentSize == maxSize) {
      ISchedulableAction[] old = array;
      allocateArray(maxSize * 2);
      System.arraycopy(old, 0, array, 0, old.length);
      // just to be sure
      old = null;
      maxSize *= 2;
    }
  }

  // move the hold down the tree
  // in order to maintain heap order.
  private void percolateDown(int hole) {
    if (currentSize > 0) {
      int child;
      ISchedulableAction tmp = array[hole];
      for (; hole * 2 <= currentSize; hole = child) {
        child = hole * 2;
        if (child != currentSize && array[child + 1].getNextTime() < array[child].getNextTime())
          child++;

        if (array[child].getNextTime() < tmp.getNextTime())
          array[hole] = array[child];
        else
          break;
      }

      array[hole] = tmp;
    }
  }

  /**
   * Find the specified Action in the queue and void it. Voiding means replace
   * that action with an empty action that does nothing.
   * 
   * @return returns true if the action is found and voided, false if this
   *         ActionQueue does not contain the specified AbstractAction.
   * 
   */
  public boolean voidAction(ISchedulableAction action) {
    boolean found = false;
    for (int i = 0, n = array.length; i < n; i++) {
      if (action.equals(array[i])) {
        array[i] = new EmptyAction(action.getNextTime());
        if (!action.isNonModelAction()) {
          modelActionCount--;
        }
        found = true;
      }
    }

    return found;
  }

  /**
   * Insert the specified action into the queue.
   */
  public void insert(ISchedulableAction action) {
    // if the order is messed up already don't
    // worry about fixing it, just toss the action
    if (!orderOk) {
      toss(action);
      return;
    }

    checkSize();
    // percolate the hold up through the tree to maintain heap order
    int hole = ++currentSize;
    if (hole != 1) {
      for (; action.getNextTime() < array[hole / 2].getNextTime(); hole /= 2)
        array[hole] = array[hole / 2];
    }
    array[hole] = action;

    if (!action.isNonModelAction()) {
      modelActionCount++;
    }
  }

  /**
   * Get the minimum element from the queque without removing it from the queue.
   * If heap order is not currently correct, the heap will be fixed before the
   * minimum is returned.
   * 
   * @return the minimum element from the queque
   */
  public ISchedulableAction peekMin() {
    if (currentSize == 0)
      throw new NoSuchElementException("Queue is Empty");
    if (!orderOk)
      fixHeap();
    return array[1];
  }

  /**
   * Remove the minimum element from the queque and return it. If heap order is
   * not currently correct, the heap will be fixed before the minimum is
   * returned.
   * 
   * @return the minimum element from the queque
   */
  public ISchedulableAction popMin() {
    ISchedulableAction a = peekMin();
    ISchedulableAction tmp = array[currentSize];
    array[currentSize] = null;
    currentSize--;
    if (currentSize > 0) {
      array[1] = tmp;
      percolateDown(1);
    }

    if (!a.isNonModelAction()) {
      modelActionCount--;
    }

    return a;
  }

  /**
   * Insert the specified action into the queue without maintain heap order.
   * 
   * @param action
   *          the action to insert into the queue
   */
  public void toss(ISchedulableAction action) {
    checkSize();
    array[++currentSize] = action;

    if (currentSize != 1) {
      // is action < its parent node.
      if (action.getNextTime() < array[currentSize / 2].getNextTime())
        orderOk = false;
    }

    if (!action.isNonModelAction()) {
      modelActionCount++;
    }
  }

  /**
   * Removes all elements from the queue.
   */
  public void clear() {
    currentSize = 0;
    orderOk = true;
    allocateArray(origMax);
    maxSize = origMax;
    AbstractAction root = new DummyAction();
    array[0] = root;

    modelActionCount = 0;
  }

  /**
   * Reinstate the heap order. This will fix the tree ordering it correctly.
   */
  public void fixHeap() {
    for (int i = currentSize / 2; i > 0; i--) {
      percolateDown(i);
    }
    orderOk = true;
  }

  /**
   * Returns true if the queue is empty, otherwise false.
   * 
   * @return true if the queue is empty, otherwise false.
   */
  public boolean isEmpty() {
    return currentSize == 0;
  }

  /**
   * Gets the number of elements in the queue.
   * 
   * @return the number of elements in the queue.
   */
  public int size() {
    return currentSize;
  }

  /**
   * Retrieves the number of actions whose
   * {@link ISchedulableAction#isNonModelAction()} method returns false.
   * 
   * @return the number of model actions
   */
  public int getModelActionCount() {
    return modelActionCount;
  }

  // resize the backing store array to the new size.
  private void allocateArray(int newMaxSize) {
    array = new AbstractAction[newMaxSize + 1];
  }
}
