package repast.simphony.engine.schedule;

import org.jscience.physics.amount.Amount;

import java.util.List;


/**
 * Interface for objects that manage the execution of actions according to a simulation clock. The clock, 
 * measured in "ticks", is incremeneted at the completion of the execution of all the IActions scheduled for 
 * execution at that clock tick. Incrementing the clock consists of setting the clock to that time at which
 * the next set of IAction-s are to be executed. The entire list of IAction-s is managed as a priority queue.
 * 
 * @see repast.simphony.engine.schedule.IAction
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface ISchedule extends IAction {
  
  /**
   * Schedules the specified IAction for execution according to the specified schedule parameters.
   * 
   * @param scheduleParams the scheduling parameters specifying start time etc.
   * @param action the action to schedule for execution
   * @return the actual action that was scheduled for execution.
   */
  ISchedulableAction schedule(ScheduleParameters scheduleParams, IAction action);

  /**
   * Schedules the named method call on each object returned by specified target with the specified parameters. 
   * 
   * @param scheduleParams the scheduling parameters specifying start time etc.
   * @param target the Iterable containing the objects to call the method on
   * @param methodName the name of the method to call
   * @param shuffle whether to shuffle the items in the iterable before calling the method on the objects therein
   * @param methodParams the parameters of the method named for execution
   * @return the actual action that was scheduled for execution.
   */ 
  ISchedulableAction scheduleIterable(ScheduleParameters scheduleParams, Iterable target, String methodName, 
                                      boolean shuffle, Object... methodParams);
  
  
  /**
   * Schedules the named method call on the specified target with the specified parameters. 
   * 
   * @param scheduleParams the scheduling parameters specifying start time etc.
   * @param target the object on which to call the named method
   * @param methodName the name of the method to call
   * @param methodParams the parameters of the method named for execution
   * @return the actual action that was scheduled for execution.
   */ 
  ISchedulableAction schedule(ScheduleParameters scheduleParams, Object target, String methodName, Object... methodParams);
  
  /**
   * Schedules the best matching ScheduleMethod annotated method for execution. The ScheduledMethod annotation 
   * tags the method as one that should be scheduled for execution and also provides the scheduling parameters 
   * for the scheduling of that method. The specified parameters will be passed to that method on execution.
   * The method will be selected by choosing the annotated method whose parameters best match the specified
   * parameters.
   *
   * @param annotatedObj the object whose annotated method should scheduled
   * @param parameters the parameters to pass to the method call and to use to
   * find the method itself
   * @return the actual action scheduled for execution.
   * 
   * @see repast.simphony.engine.schedule.ScheduledMethod
   */ 
  ISchedulableAction schedule(Object annotatedObj, Object... parameters);
  
  /**
   * Schedules the best matching ScheduleMethod annotated method for execution. The ScheduledMethod annotation 
   * tags the method as one that should be scheduled for execution. The specified parameters will be passed 
   * to that method on execution. The method will be selected by choosing the annotated method whose parameters 
   * best match the specified parameters. Any ScheduleParameters derived from the ScheduledMethod annotation
   * will be ignored and replaced by the specified ScheduleParameters.
   *
   * @param scheduleParams the scheduling parameters describing start time etc.
   * @param annotatedObj   the object containing the annotated methods
   * @param parameters     the parameters to pass to the method call and to use to
   *                       find the method itself
   * @return the actual action scheduled for execution.
   * @see ScheduledMethod
   */
  public ISchedulableAction createAction(ScheduleParameters scheduleParams, Object annotatedObj, Object... parameters); 

  /**
   * Schedules for execution any methods in the specified object that have been annotated with the
   * ScheduledMethod annotation. The ScheduledMethod annotation tags the method as one that should be
   * scheduled for execution and also provides the scheduling parameters for the scheduling of that
   * method.
   * 
   * @param obj the object whose annotated methods should scheduled
   * @return a List of the actual action scheduled for execution. An object may have more than one 
   * of its methods annotated for scheduling execution and so a list is returned.
   */ 
  List<ISchedulableAction> schedule(Object obj);

  /**
   * Removes the specified action from this Schedule. Actions
   * should not be scheduled for removal at the same tick
   * in which they are executing. In those cases,
   * removeAction will always return false.
   * 
   * @param action the ISchedulableAction to remove
   *
   * @return whether or not the action was removed.
   */ 
  boolean removeAction(ISchedulableAction action);

  /**
   * Executes the schedule by, at the very least, executing all the actions scheduled for
   * execution at the current clock tick.
   */ 
  void execute();
  
  /**
   * Gets the current tick count. This is the current value of the simulation clock.
   * 
   * @return the current tick count.
   */ 
  double getTickCount();

  /**
   * Gets the current tick count in user time units. This is the current value of the simulation
   * clock in user time units.
   * 
   * @return the current tick count in user time units.
   */ 
  Amount getTickCountInTimeUnits();
  
  /**
   * Gets the current user time units.
   * 
   * @return the current user time units.
   */ 
  Amount getTimeUnits();
  
  /**
   * Sets the current user time units.
   * 
   * @param newUnits the new user time units.
   */ 
  void setTimeUnits(Amount newUnits);
  
  /**
   * Gets the number of currently scheduled actions.
   * 
   * @return the number of currently scheduled actions.
   */ 
  int getActionCount();

	/**
	 * Returns the number of actions that do not have a {@link NonModelAction} annotation, attached
	 * to them along with the number of {@link ISchedulableAction}s whose
	 * {@link ISchedulableAction#isNonModelAction()} method returns false.
	 * 
	 * @return the number of actions that are model actions, not back-end actions
	 */
	int getModelActionCount();
  
	/**
	 * Executes all the actions scheduled to execute at the end of the model run.
	 */
	void executeEndActions();

	/**
	 * Sets whether or not the simultation is finishing, which means that this schedule will no
	 * longer allow actions to be scheduled or rescheduled. This is used to execute the back-end
	 * actions one final time after the model actions have been completed.
	 * 
	 * @see NonModelAction
	 * @see ISchedulableAction#isNonModelAction()
	 * 
	 * @param finishing
	 *            if the schedule is to be in finishing mode
	 */
	void setFinishing(boolean finishing);
	
	/**
	 * Returns whether or not the simultation is finishing, which means that this schedule will no 
	 * longer allow actions to be scheduled or rescheduled. This is used to execute the back-end
	 * actions one final time after the model actions have been completed.
	 * 
	 * @see NonModelAction
	 * @see ISchedulableAction#isNonModelAction()
	 * 
	 * @return if the schedule is in finishing mode
	 */
	boolean isFinishing();
}
