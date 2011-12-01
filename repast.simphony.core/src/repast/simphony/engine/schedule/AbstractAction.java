package repast.simphony.engine.schedule;



/**
 * Abstract base action used by the default scheduling mechanism.
 * 
 * @author Nick Collier
 */
public abstract class AbstractAction implements ISchedulableAction {
  
  static final long serialVersionUID = -7327096371080222392L;
  
  /**
   * The Frequency of this AbstractAction.
   */ 
  protected Frequency frequency = Frequency.REPEAT;
  
  /**
   * The next clock tick that this AbstractAction should be executed.
   */ 
  protected double nextTime = Double.NaN;

  /**
   * The interval at which to execute this action.
   */
  protected double interval = 1;
  
  /**
   * The priority of this AbstractAction with respect to other
   * actions scheduled for the same clock tick.
   */ 
  protected double priority = ScheduleParameters.RANDOM_PRIORITY;
  
  /**
   * If the action is a back-end action (true) or if it is a model action (false).
   */
  protected boolean nonModelAction;
  
  /**
   * The order index
   */ 
  private long index = 0;

  /**
   * An rescheduler is responsible for rescheduling an action in the ActionQueue.
   */ 
  protected static interface Rescheduler {
    public void reschedule(ActionQueue queue);
  }

  /**
   * Reschedules this AbstractAction for execution by resetting
   * its nextTime to the last time it executed plus the interval
   * of execution. Then, tossing this action into the queue.
   */ 
  protected class IntervalRescheduler implements Rescheduler {
	private static final long serialVersionUID = 6596470668793932788L;

	public void reschedule(ActionQueue queue) {
      nextTime += interval;
      queue.toss(AbstractAction.this);
    }
  }

  /**
   * Empty implementation because an action scheduled not to
   * repeat does not need to be rescheduled.
   */ 
  protected static class OneTimeRescheduler implements Rescheduler {
	private static final long serialVersionUID = 6926888495853469259L;

	public void reschedule(ActionQueue queue) {}
  }

  /**
   * The Rescheduler used by this AbstractAction to reschedule
   * it for execution.
   */ 
  protected Rescheduler rescheduler = new IntervalRescheduler();
  
  public AbstractAction(ScheduleParameters params, long orderIndex) {
    this(params);
    this.index = orderIndex;
  }
  
  /**
   * Creates an AbstractAction using the specified ScheduleParameters. The
   * AbstractAction's scheduling data is set from the ScheduleParameters argument.
   * 
   * @param params the scheduling data used by this AbstractAction
   */ 
  public AbstractAction(ScheduleParameters params) {
    setFrequency(params.getFrequency());
    nextTime = params.getStart();
    priority = params.getPriority();
    interval = params.getInterval();
  }

  /**
   * Returns the next time this AbstractAction should be executed.
   * 
   * @return the next time this AbstractAction should be executed.
   */ 
  public double getNextTime() {
    return nextTime;
  }

  protected void setIsNonModelAction(IAction action) {
	  nonModelAction = action.getClass().isAnnotationPresent(NonModelAction.class);
  }
  
  protected void setIsNonModelAction(boolean nonModelAction) {
	  this.nonModelAction = nonModelAction;
  }
  
  public boolean isNonModelAction() {
	return nonModelAction;
  }
  
  /**
   * Sets the frequency of this AbstractAction.
   * 
   * @param freq the new Frequency of execution of this AbstractAction.
   */ 
  protected void setFrequency(Frequency freq) {
    this.frequency = freq;
    if (freq == Frequency.REPEAT) rescheduler = new IntervalRescheduler();
    else if (freq == Frequency.ONE_TIME) rescheduler = new OneTimeRescheduler();
  }

  /**
   * Reschedule this AbstractAction for execution.
   */
  public void reschedule(ActionQueue queue) {
    rescheduler.reschedule(queue);
  }

  /**
   * Add this ScheduableAction to the specified group for execution. 
   * 
   * @param group the group to add the ISchedulableAction to.
   */ 
  public void addForExecution(ScheduleGroup group) {
    group.addAction(this);
  }

  /**
   * Gets the priority of this action. The priority refers to this AbstractAction's place
   * in the order of execution with respect to all the other actions that will execute at the
   * same clock tick. The range is from Double.NEGATIVE_INFINITY (execute first) to Double.POSITIVE_INFINITY
   * (execute last). The default is ScheduleParameters.RANDOM_PRIORITY which means the priority will be
   * randomly assigned from a uniform distribution. 
   * 
   * @return the priority of this AbstractAction.
   */
  public double getPriority() {
    return priority;
  }
  
  /**
   * Gets an index indicating where this IAction was added to a schedule w/r to all other
   * actions added to a schedule.
   *
   * @return an index indicating where this IAction was added to a schedule w/r to all other
   *         actions added to a schedule.
   */
  public long getOrderIndex() {
    return index;
  }
}
