/**
 * 
 */
package repast.simphony.engine.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;

/**
 * A list of IActions.
 * 
 * @author Nick Collier
 */
public class ActionList implements Iterable<ISchedulableAction> {

  private List<ISchedulableAction> list = new ArrayList<ISchedulableAction>();
  private boolean dirty = false;

  /**
   * Clears this list of any elements it contains.
   */
  public void clear() {
    list.clear();
    dirty = false;
  }

  /**
   * Adds the specified action.
   * 
   * @param action
   *          the action to add
   */
  public void add(ISchedulableAction action) {
    dirty = true;
    list.add(action);
  }

  /**
   * Adds the specified action without marking the list as dirty and needing to
   * be sorted.
   * 
   * @param action
   *          the action to add
   */
  public void addNoSort(ISchedulableAction action) {
    list.add(action);
  }

  /**
   * Adds the specified action at the specifed index without marking the list as
   * dirty and needing to be sorted.
   * 
   * @param action
   *          the action to add
   */
  public void addNoSort(int index, ISchedulableAction action) {
    list.add(index, action);
  }

  /**
   * Adds all the elements in the other list to this one without marking this as
   * dirty and needing to be sorted.
   * 
   * @param other
   *          the list to add
   */
  public void addAllNoSort(ActionList other) {
    list.addAll(other.list);
  }

  /**
   * Removes the specified action.
   * 
   * @param action
   * @return true if the action was removed, otherwise false.
   */
  public boolean remove(ISchedulableAction action) {
    return list.remove(action);
  }

  /**
   * Gets the size of this list.
   * 
   * @return the size of this list.
   */
  public int size() {
    return list.size();
  }

  /**
   * Shuffles the elements of this list.
   */
  public void shuffle() {
    SimUtilities.shuffle(list, RandomHelper.getUniform());
  }

  /**
   * Sorts this list using the specified Comparator.
   * 
   * @param comp
   * @param force if true this will always sort regardless.
   */
  public void sort(Comparator<ISchedulableAction> comp, boolean force) {
    if (dirty || force) {
      Collections.sort(list, comp);
      dirty = false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  public Iterator<ISchedulableAction> iterator() {
    return list.iterator();
  }

  /**
   * Deletes the first N number of elements from this list.
   * 
   * @param n
   *          the number of elements to delete.
   */
  public void delete(int n) {
    if (n == list.size())
      list.clear();
    else
      list.subList(0, n).clear();
  }
}
