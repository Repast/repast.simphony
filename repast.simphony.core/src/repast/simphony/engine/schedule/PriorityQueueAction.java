/**
 * 
 */
package repast.simphony.engine.schedule;

/**
 * Interface for classes that can be queued
 * in the action priority queue. 
 * 
 * @author Nick Collier
 */
public interface PriorityQueueAction {
  
  /**
   * Gets the next time this PriorityQueueAction should be executed.
   * 
   * @return the next time this PriorityQueueAction should be executed.
   */
  double getNextTime();
  
  /**
   * Gets whether or not this PriorityQueueAction is a non-model action.
   * 
   * @return true if this {@link PriorityQueueAction} is not a model action,
   * otherwise false.
   */
  boolean isNonModelAction();
  
}
