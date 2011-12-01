package repast.simphony.engine.schedule;


/**
 * An ISchedulableAction that will run in the background for some specificable number of ticks (the duration).
 * If the ThreadedAction is still running after that number of ticks, the ThreadedAction will 
 * behave like a normal foreground action, that is, the the schedule will wait until the ThreadedAction
 * completes before executing the next scheduled actions. 
 * 
 * @author Nick Collier
 */
public class ThreadedAction extends AbstractAction {
  
  static final long serialVersionUID = -2127288227326049810L;
  
  private IAction action;
  private boolean done = false;
  private Thread runner;
  private double duration;

  /**
   * An AbstractAction.Rescheduler for rescheduling non-repeating ThreadedActions.
   */ 
  class ThreadedAtRescheduler implements Rescheduler {

    private boolean updated = false;

    /**
     * This gets called after the ThreadedAction has been executed for the first time.
     * We then reschedule the action to be executed when the duration has been finished. The
     * call to execute that happens at this time will wait until the thread has finished.
     * 
     * @param queue
     */ 
    public void reschedule(ActionQueue queue) {
      if (!updated) {
        nextTime += duration;
        queue.toss(ThreadedAction.this);
        updated = true;
      }
    }
  }

  /**
   * An AbstractAction.Rescheduler for rescheduling repeating ThreadedActions.
   */ 
  class ThreadedIntervalRescheduler implements Rescheduler {
    
    private boolean updateForEnd = true;

    /**
     * This will get called in two different circumstances. In the first, the action has been
     * executed and is running in the background. In that case, the action is scheduled to be executed when the
     * amount of time specified by the duration has been finished. When the action executes at that point, it will
     * wait until the background thread has completed before finishing execution. In the second circumstance,
     * the thread is not running in the background, and we schedule the next execution of the background thread. 
     * 
     * @param queue
     */ 
    public void reschedule(ActionQueue queue) {
      if (updateForEnd) {
        // this will schedule it to block at now + duration.
        // executing at this time should wait until the run thread 
        // finishes
        nextTime += duration;
        queue.toss(ThreadedAction.this);
        updateForEnd = false;
      } else {
        // this will schedule it for the next execution in the repeat
        // cycle.
        nextTime += interval;
        queue.toss(ThreadedAction.this);
        updateForEnd = true;
      }
    }
  }

  /**
   * Runner that performs the actual execution of the wrapped IAction in its run() method.
   */ 
  static class ActionRunner implements Runnable {

    IAction iAction;
    ThreadedAction owner;

    /**
     * Creates an ActionRunner that will execute the specified IAction and whose owner is the
     * specified TheadedAction. 
     * 
     * @param action the IAction to execute
     * @param owner the ThreadedAction within which this ActionRunner runs
     */ 
   public ActionRunner(IAction action, ThreadedAction owner) {
     iAction = action;
     this.owner = owner;
   }

    /**
     * Executes the IAction specified in the constructor
     */ 
    public void run() {
      iAction.execute();
      owner.done();
    }
  }

  /**
   * Creates a ThreadedAction to execute the specified IAction according to the specified
   * scheduling parameters. 
   *  
   * @param param the scheduling data
   * @param action the action to execute
   * @param orderIndex the index when this was added
   */ 
  public ThreadedAction(ScheduleParameters param, IAction action, long orderIndex) {
    super(param, orderIndex);
    this.duration = param.getDuration();
    this.action = action;

    setIsNonModelAction(action);
  }

  // called by the ActionRunner when it is done -- this releases the wait() in
  // the excecute below.
  private synchronized void done() {
    done = true;
    // releases the wait() in the execute below
    notifyAll();
  }

  /**
   * Sets the execution frequency.
   * @param type the execution frequency.
   */ 
  protected void setFrequency(Frequency type) {
    if (type == Frequency.REPEAT) rescheduler = new ThreadedIntervalRescheduler();
    else if (type == Frequency.ONE_TIME) rescheduler = new ThreadedAtRescheduler();
  }

  /**
   * Executes this ThreadedAction. If the action has not yet started, this will
   * create the background thread and start to run it. If the thread has started this will
   * wait for the thread to complete.
   */ 
  public void execute() {
    if (runner == null) {
      runner = new Thread(new ActionRunner(action, this));
      runner.setName("ThreadedAction Thread");
      runner.start();
    } else {
      try {
        synchronized (this) {
          while (!done) wait();
        }

        // set runner to null, so if this gets rescheduled by the rescheduler
        // then it will run the background thread again.
        runner = null;
        done = false;
      } catch (InterruptedException ex) {
        runner.interrupt();
      }
    }
  }
}
