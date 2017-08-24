/*CopyrightHere*/
package repast.simphony.ws;

import repast.simphony.engine.controller.Controller;
import repast.simphony.engine.controller.TickListener;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.environment.Runner;
import repast.simphony.engine.schedule.ISchedule;

/**
 * This class executes the model schedule found in the RunState's
 * ScheduleRegistry.
 * 
 * @author Nick Collier
 */
public class ScheduleRunner implements Runner {
  
  private RunnerSupport runSupport = new RunnerSupport();

 // private static final MessageCenter msgCenter = MessageCenter
 //     .getMessageCenter(ScheduleRunner.class);
  
  
  protected boolean stop = false;
  protected boolean pause = false;
  protected boolean step = false;

  private final Object monitor = new Object();
  private TickListener tickListener = null;
  private Thread executingThread;

  class ScheduleLoopRunnable implements Runnable {

    private ISchedule schedule;

    public ScheduleLoopRunnable(ISchedule schedule) {
      this.schedule = schedule;
    }

    public void run() {
      runSupport.fireStartedMessage();
      try {
        while (go() && schedule.getActionCount() > 0) {
          if (schedule.getModelActionCount() == 0) {
            schedule.setFinishing(true);
          }
          schedule.execute();
          if (step) {
            pause = true;
            step = false;
          }
          
          if (tickListener != null) {
            tickListener.tickCountUpdated(schedule.getTickCount());
          }
        }
        schedule.executeEndActions();
      } catch (RuntimeException ex) {
        ex.printStackTrace();
        //msgCenter.fatal("RunTimeException when running the schedule\n" + "Current tick ("
        //    + (schedule != null ? String.valueOf(schedule.getTickCount()) : "unavailable") + ")",
        //    ex);
      }

      runSupport.fireStoppedMessage();
    }
  }

  /**
   * This executes the given RunState object's schedule. It will continue
   * executing the schedule until there are no more actions schedule or the run
   * manager tells it to stop.
   * 
   * @param toExecuteOn
   *          the RunState to execute on
   */
  public void execute(RunState toExecuteOn) {
    ISchedule schedule = getSchedule(toExecuteOn);
    Runnable runnable = new ScheduleLoopRunnable(schedule);
    executingThread = new Thread(runnable);
    executingThread.start();
  }

  private void notifyMonitor() {
    synchronized (monitor) {
      monitor.notify();
    }
  }
  
  /**
   * @return true if the runner should keep going
   */
  public boolean go() {
   // int delay = RunEnvironment.getInstance().getScheduleTickDelay();

    /*
    if (delay > 0) {
      try {
        while (eventQueue.peekEvent() != null && !stop) {
          Thread.sleep(delay * 5);
        }
      } catch (InterruptedException ex4) {
        // TODO: decide if we have to stop in this condition
        stop = true;
        pause = false;
      }
    }
    */

    // check for and pause if necessary
    synchronized (monitor) {
      while (pause) {
        runSupport.firePausedMessage();
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          //msgCenter.warn("Caught InterruptedException when running simulation, unpausing.", e);
          runSupport.fireRestartedMessage();
          // I think we need to break otherwise
          // we will be stuck in the loop
          break;
          // TODO: decide if we have to unpause in this condition
        }
        runSupport.fireRestartedMessage();
      }
    }

    return !stop;
  }

  public void setPause(boolean pause) {
    this.pause = pause;
    if (!pause) {
      notifyMonitor();
    }
  }
  
  public void step() {
    step = true;
    if (pause) {
      pause = false;
      notifyMonitor();
    }
  }

  public void stop() {
    stop = true;
    if (pause) {
      pause = false;
      notifyMonitor();
    }

    try {
      if (executingThread != null && !Thread.currentThread().equals(executingThread)) {
        // wait at most 20 seconds.
        executingThread.join(20000);
        
        if (executingThread.isAlive())
          executingThread.interrupt();
      }
    } catch (InterruptedException ex) {
      //msgCenter.warn("Error while waiting for sim thread to stop", ex);
    }

  }

  public TickListener getTickListener() {
    return tickListener;
  }

  public void setTickListener(TickListener tickListener) {
    this.tickListener = tickListener;
  }

  protected ISchedule getSchedule(RunState toExecuteOn) {
    return toExecuteOn.getScheduleRegistry().getModelSchedule();
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setEnvironmentBuilder(RunEnvironmentBuilder environment) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addRunListener(RunListener listener) {
    runSupport.addRunListener(listener);
  }

  @Override
  public void removeRunListener(RunListener listener) {
    runSupport.removeRunListener(listener);
  }

  @Override
  public void setController(Controller controller) {
   
  }

}
