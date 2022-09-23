package repast.simphony.engine.schedule;

import java.util.List;

import javax.measure.Quantity;

import tech.units.indriya.function.Calculus;
import tech.units.indriya.function.DefaultNumberSystem;
import tech.units.indriya.quantity.Quantities;


/**
 * Manages the execution of IAction-s according to a simulation clock. The clock, measured in "ticks", is
 * incremeneted at the completion of the execution of all the IActions scheduled for execution at that clock tick.
 * The parameters of a scheduled IAction are set using a ScheduledParameters object which specifies the start time,
 * interval, priority and so forth of the action to be scheduled. The actual actions that a Schedule ultimately
 * schedules are produced by a ISchedulableActionFactory interface. By implementing this interface and creating a
 * Schedule that uses it, users can customize the type of actions that get scheduled. By default a Schedule uses a
 * DefaultSchedulableActionFactory.<p>
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 * @see repast.simphony.engine.schedule.IAction
 * @see repast.simphony.engine.schedule.ScheduleParameters
 * @see repast.simphony.engine.schedule.ISchedulableActionFactory
 * @see repast.simphony.engine.schedule.DefaultSchedulableActionFactory
 */
public class Schedule implements ISchedule {

	/*
	 * The new java units api implementation "indriya" uses a java ServiceLoader that unfortunately
	 * uses the default classloader instead of the java plugin classloader, so it can't find
	 * the services.  Fortunately there is only one NumberSystem implemented, so there is no
	 * reason to use the ServiceLoader approach and we can just mannually set it here.
	 * 
	 * TODO Check if this is fixed in future units release then we can remove it.
	 */
	static {
		Calculus.setCurrentNumberSystem(new DefaultNumberSystem());
	}
	
  static final long serialVersionUID = 7686585829552316670L;

  protected ActionQueue actionQueue;
  protected double tickCount = -1;
  protected ScheduleGroup groupToExecute;
  protected ScheduleGroup endActions;
  protected ISchedulableActionFactory actionFactory;
  
  /**
   * User time quantity converts some abstract quantity to a schedule tick value, and
   * visa versa.  For example the user might want 1.0 tick to represent 2 time seconds.
   * Java Quantity is generic, and we do not restrict the type to a *Time* quantity, so
   * the tick can represent any quantity.
   */
  protected Quantity<?> userTimeQuantity;

  /**
   * Creates a Schedule that by default uses a DefaultSchedulableActionFactory to create its
   * scheduled actions.
   */
  public Schedule() {
    this(new DefaultSchedulableActionFactory());
  }

  /**
   * Creates a Schedule that will use the specified ISchedulableActionFactory to create its scheduled actions.
   *
   * @param factory the factory to use to create the actions that the Schedule will schedule
   */
  public Schedule(ISchedulableActionFactory factory) {
    actionFactory = factory;
    actionQueue = new ActionQueue();
    groupToExecute = new ScheduleGroup();
    endActions = new ScheduleGroup();
  }

  // sets the tick count
  private void setTickCount(double ticks) {
    this.tickCount = Math.max(ticks, 0.0);
  }

  /**
   * Gets the current tick count. This is the current value of the simulation clock.
   *
   * @return the current tick count.
   */
  public double getTickCount() {
    return tickCount;
  }

  /**
   * Gets the current tick count in user time units. This is the current value of the simulation
   * clock in user time units.
   *
   * @return the current tick count in user time units.
   */
  public Quantity<?> getTickCountInTimeQuantity() {
    if (this.userTimeQuantity == null) {
      return Quantities.getQuantity(this.tickCount, tech.units.indriya.AbstractUnit.ONE);
    } else {
      return this.userTimeQuantity.multiply(this.tickCount);
    }
  }

  /**
   * Converters the given time units into ticks using the current time units setting.
   *
   * @param timeUnitsToConvert the time units to convert.
   * @return the converted ticks.
   */
  public Double convertTimeQuantityToTicks(Quantity<?> timeUnitsToConvert) {
    if ((this.userTimeQuantity == null || (this.userTimeQuantity.getValue().doubleValue() == 0.0))) {
      return timeUnitsToConvert.getValue().doubleValue();
    } 
    else {
      return timeUnitsToConvert.divide(this.userTimeQuantity).getValue().doubleValue();
    }
  }

  /**
   * Converters the given time units into ticks using the current time units setting.
   *
   * @param ticks time units to convert.
   * @return the converted ticks.
   */
  public Quantity<?> convertTicksToTimeQuantity(double ticks) {
    if (this.userTimeQuantity == null) {
      return Quantities.getQuantity(ticks, tech.units.indriya.AbstractUnit.ONE);
    } else {
      return this.userTimeQuantity.multiply(ticks);
    }
  }

  /**
   * Gets the current user time units.
   *
   * @return the current user time units.
   */
  public Quantity<?> getTimeQuantity() {
    return this.userTimeQuantity;
  }

  /**
   * Sets the current user time units.
   *
   * @param newUnits the new user time units.
   */
  public void setTimeQuantity(Quantity<?> newUnits) {
    this.userTimeQuantity = newUnits;
  }

  /**
   * Gets the number of currently scheduled actions.
   *
   * @return the number of currently scheduled actions.
   */
  public int getActionCount() {
    return actionQueue.size();
  }

  /**
   * Returns the number of actions that do not have a {@link NonModelAction} annotation, attached
   * to them along with the number of {@link ISchedulableAction}s whose
   * {@link ISchedulableAction#isNonModelAction()} method returns false.
   *
   * @return the number of actions that are model actions, not back-end actions
   */
  public int getModelActionCount() {
    return actionQueue.getModelActionCount();
  }

  /**
   * @return next action on schedule queue
   */
  public ISchedulableAction peekNextAction() {
    return actionQueue.peekMin();
  }

  /**
   * Schedules the specified IAction for execution according to the specified schedule parameters.
   *
   * @param scheduleParams the scheduling parameters specifying start time etc.
   * @param action         the action to schedule for execution
   * @return the actual action that was scheduled for execution.
   */
  public ISchedulableAction schedule(ScheduleParameters scheduleParams, IAction action) {
    ISchedulableAction sAction = actionFactory.createAction(scheduleParams, action);
    scheduleAction(sAction);
    return sAction;
  }

  private void scheduleAction(ISchedulableAction sAction) {
    if (sAction.getNextTime() >= this.getTickCount()) {
      if (sAction.getNextTime() == ScheduleParameters.END) endActions.addAction(sAction);
      else if (sAction.getNextTime() == this.getTickCount()) {
        groupToExecute.addAction(sAction);
      } else {
        actionQueue.toss(sAction);
      }
    }
  }

  /**
   * Schedules the named method call on the specified target with the specified parameters.
   *
   * @param scheduleParams the scheduling parameters specifying start time etc.
   * @param target         the object on which to call the named method
   * @param methodName     the name of the method to call
   * @param methodParams   the parameters of the method named for execution
   * @return the actual action that was scheduled for execution.
   */
  public ISchedulableAction schedule(ScheduleParameters scheduleParams, Object target, String methodName, Object... methodParams) {
    ISchedulableAction sAction = actionFactory.createAction(scheduleParams, target, methodName, methodParams);
    scheduleAction(sAction);
    return sAction;
  }

  /**
   * Schedules the named method call on each object returned by specified target with the specified parameters. Note
   * that if the Iterable is not a list shuffling is not optimized can potentialy be quite slow.
   *
   * @param scheduleParams the scheduling parameters specifying start time etc.
   * @param target         the Iterable containing the objects to call the method on
   * @param methodName     the name of the method to call
   * @param shuffle        whether to shuffle the items in the iterable before calling the method on the objects therein
   * @param methodParams   the parameters of the method named for execution
   * @return the actual action that was scheduled for execution.
   */
  public ISchedulableAction scheduleIterable(ScheduleParameters scheduleParams, Iterable target, String methodName,
                                             boolean shuffle, Object... methodParams) {
    ISchedulableAction sAction = actionFactory.createActionForIterable(scheduleParams, target, methodName,
            shuffle, methodParams);
    scheduleAction(sAction);
    return sAction;
  }

  /**
   * Schedules for execution any methods in the specified object that have been annotated with the
   * ScheduledMethod annotation. The ScheduledMethod annotation tags the method as one that should be
   * scheduled for execution and also provides the scheduling parameters for the scheduling of that
   * method.
   *
   * @param obj the object whose annotated methods should scheduled
   * @return a List of the actual action scheduled for execution. An object may have more than one
   *         of its methods annotated for scheduling execution and so a list is returned.
   */
  public List<ISchedulableAction> schedule(Object obj) {
    List<ISchedulableAction> actions = actionFactory.createActions(obj);
    for (ISchedulableAction action : actions) {
      scheduleAction(action);
    }

    return actions;
  }

  /**
   * Schedules the best matching ScheduleMethod annotated method for execution. The ScheduledMethod annotation
   * tags the method as one that should be scheduled for execution and also provides the scheduling parameters
   * for the scheduling of that method. The specified parameters will be passed to that method on execution.
   * The method will be selected by choosing the annotated method whose parameters best match the specified
   * parameters.
   *
   * @param annotatedObj the object whose annotated method should scheduled
   * @param parameters   the parameters to pass to the method call and to use to
   *                     find the method itself
   * @return the actual action scheduled for execution.
   * @see ScheduledMethod
   */
  public ISchedulableAction schedule(Object annotatedObj, Object... parameters) {
    ISchedulableAction sAction;
    // hack to fix problem with varargs and appropriate method not being called
    if (annotatedObj instanceof ScheduleParameters) {
      Object[] fixParams = new Object[parameters.length - 1];
      if (fixParams.length > 0)
        System.arraycopy(parameters, 1, fixParams, 0, parameters.length - 1);
      sAction = actionFactory.createAction((ScheduleParameters) annotatedObj, parameters[0], fixParams);
    } else {
      sAction = actionFactory.createAction(annotatedObj, parameters);
    }
    scheduleAction(sAction);
    return sAction;
  }

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
  public ISchedulableAction createAction(ScheduleParameters scheduleParams, Object annotatedObj, Object... parameters) {
    ISchedulableAction sAction = actionFactory.createAction(scheduleParams, annotatedObj, parameters);
    scheduleAction(sAction);
    return sAction;
  }


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
  public boolean removeAction(ISchedulableAction action) {
    if (action.getNextTime() == ScheduleParameters.END) return endActions.remove(action);
    // removing at current time will always return false
    else if (action.getNextTime() == this.getTickCount()) return false;
    else return actionQueue.voidAction(action);

  }

  /**
   * Collects the actions to be executed at the current clock tick. The current clock
   * is the "nextTime" of the action at the front of the action queue. All the actions
   * set to execute at that time are gathered into the groupToExecute and the simulation
   * tick count is set to that time.
   */
  protected void preExecute() {
    double queueMin = Double.POSITIVE_INFINITY;
    if (actionQueue.size() > 0) queueMin = actionQueue.peekMin().getNextTime();
    if (queueMin == Double.POSITIVE_INFINITY) {
      // nothing to execute so just clear this group and return
      groupToExecute.clear();
      return;
    }

    double newTick = queueMin;
    groupToExecute.clear();
    // we call addToGroup rather than adding the popped action directly to
    // the group so that "Removed" actions do not add themselves to the 
    // group. See ActionQueue.EmptyAction
    actionQueue.popMin().addForExecution(groupToExecute);

    if (actionQueue.size() > 0) {
      // only try and keep adding if there are BasicActions left in the
      // queue.
      queueMin = actionQueue.peekMin().getNextTime();
      while (queueMin == newTick) {
        // newTick is our original minimum nextTime we don't want to
        // add any BasicActions whose nextTime is not equal to this.
        actionQueue.popMin().addForExecution(groupToExecute);
        if (actionQueue.size() == 0) break;
        queueMin = actionQueue.peekMin().getNextTime();
      }
    }

    // there may have been empty removed actions in the queue. These don't
    // add themselves to group on addGroup so we only update the true tick count
    // if there are actual DefaultActions to execute.
    if (groupToExecute.size() > 0) setTickCount(newTick);
  }

  /**
   * Executes the schedule. Schedule execution consists of determining the current
   * clock tick, executing all the actions scheduled for execution at that tick,
   * and then rescheduling any actions that have a repeating frequency.
   */
  public void execute() {
    preExecute();
    groupToExecute.sort();

    while (groupToExecute.hasMoreToExecute()) {
      groupToExecute.execute();
    }

    groupToExecute.reschedule(actionQueue);
  }

  /**
   * Executes all the actions scheduled to execute at the end of the model run.
   */
  public void executeEndActions() {
    endActions.sort();
    endActions.execute();
  }

  /**
   * Sets whether or not the simultation is finishing, which means that this schedule will no
   * longer allow actions to be scheduled or rescheduled. This is used to execute the back-end
   * actions one final time after the model actions have been completed.
   *
   * @param finishing if the schedule is to be in finishing mode
   * @see NonModelAction
   * @see ISchedulableAction#isNonModelAction()
   */
  public void setFinishing(boolean finishing) {
    this.groupToExecute.setFinishing(finishing);
  }

  /**
   * Returns whether or not the simultation is finishing, which means that this schedule will no
   * longer allow actions to be scheduled or rescheduled. This is used to execute the back-end
   * actions one final time after the model actions have been completed.
   *
   * @return if the schedule is in finishing mode
   * @see NonModelAction
   * @see ISchedulableAction#isNonModelAction()
   */
  public boolean isFinishing() {
    return this.groupToExecute.isFinishing();
	}
}
