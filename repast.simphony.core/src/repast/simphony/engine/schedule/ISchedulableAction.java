package repast.simphony.engine.schedule;


/**
 * Interface for actions that can be scheduled and executed by a Schedule.
 * 
 * @see repast.simphony.engine.schedule.Schedule
 * 
 * @author Nick Collier
 */
public interface ISchedulableAction extends IAction {
  /**
   * Gets the next time this ScheduledAction is to execute.
   * 
   * @return the next time this ScheduledAction is to execute.
   */ 
  double getNextTime();
  
  /**
   * Reschedule this ScheduableAction using the specified queue. If an action
   * is non-repeating then this method would typically do nothing. 
   * 
   * @param queue
   */ 
  void reschedule(ActionQueue queue);
  
  /**
   * Add this ScheduableAction to the specified group for execution. 
   * 
   * @param group the group to add the ISchedulableAction to.
   */ 
  void addForExecution(ScheduleGroup group);
  
  /**
   * Gets the priority of this action. The priority refers to this ISchedulableAction's place
   * in the order of execution with respect to all the other actions that will execute at the
   * same clock tick. The range is from Double.NEGATIVE_INFINITY (execute first) to Double.POSITIVE_INFINITY
   * (execute last). The default is ScheduleParameters.RANDOM_PRIORITY which means the priority will be
   * randomly assigned from a uniform distribution. 
   * 
   * @return the priority of this ScheduableAction.
   */ 
  double getPriority();
  
  /**
   * Gets the PriorityType of this action. 
   * 
   * @return the PriorityType of this action.
   */
  PriorityType getPriorityType();
  
  /**
   * Gets an index indicating where this IAction was added to a schedule w/r to all other
   * actions added to a schedule. 
   * 
   * @return an index indicating where this IAction was added to a schedule w/r to all other
   * actions added to a schedule.
   */ 
  long getOrderIndex();
  
  /**
   * This is used by the schedule to determine if the action is a model related action or a back-end action.
   * Normal {@link IAction}s use {@link NonModelAction} to specify this, but {@link ISchedulableAction}s should
   * use this method.
   * 
   * @return if this is a back-end type action or not
   */
  boolean isNonModelAction();
}
