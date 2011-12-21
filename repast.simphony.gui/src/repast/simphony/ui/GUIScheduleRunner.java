/*CopyrightHere*/
package repast.simphony.ui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import repast.simphony.engine.controller.TickListener;
import repast.simphony.engine.environment.AbstractRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.render.Renderer;
import repast.simphony.render.RendererManager;
import simphony.util.messages.MessageCenter;

/**
 * This class executes the model schedule found in the RunState's
 * ScheduleRegistry.
 * 
 * @author Jerry Vos
 */
public class GUIScheduleRunner extends AbstractRunner {

  private static final MessageCenter msgCenter = MessageCenter
      .getMessageCenter(GUIScheduleRunner.class);

  private EventQueue eventQueue;
  private final Object monitor = new Object();
  private boolean step = false;
  private RendererManager rendererManager = new RendererManager();
  private TickListener tickListener = null;
  private Thread executingThread;

  class ScheduleLoopRunnable implements Runnable {

    private ISchedule schedule;

    public ScheduleLoopRunnable(ISchedule schedule) {
      this.schedule = schedule;
    }

    public void run() {
      // 3D Display will pause after init
      // to avoid max'ing cpu, so we unpause here.
      rendererManager.setPause(false);
      fireStartedMessage();
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

          rendererManager.render();
          if (tickListener != null) {
            tickListener.tickCountUpdated(schedule.getTickCount());
          }
        }
        schedule.executeEndActions();
      } catch (RuntimeException ex) {
        rendererManager.clear();
        msgCenter.fatal("RunTimeException when running the schedule\n" + "Current tick ("
            + (schedule != null ? String.valueOf(schedule.getTickCount()) : "unavailable") + ")",
            ex);
      }

      // this will stop the j3d thread from executing a tight, resource hogging
      // loop.
      // so stop and pause will not use much if any cpu resources
      rendererManager.setPause(true);
      rendererManager.clear();
      fireStoppedMessage();
    }
  }

  public void addRenderer(Renderer renderer) {
    rendererManager.addRenderer(renderer);
  }

  public void clearRenderers() {
    rendererManager.clear();
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
    eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
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

  public long lastSleepTime = 0;

  /**
   * @return true if the runner should keep going
   */
  @Override
  public boolean go() {
    int delay = RunEnvironment.getInstance().getScheduleTickDelay();

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

    // check for and pause if necessary
    synchronized (monitor) {
      while (pause) {
        rendererManager.setPause(true);
        firePausedMessage();
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          msgCenter.warn("Caught InterruptedException when running simulation, unpausing.", e);
          rendererManager.setPause(false);
          fireRestartedMessage();
          // I think we need to break otherwise
          // we will be stuck in the loop
          break;
          // TODO: decide if we have to unpause in this condition
        }
        rendererManager.setPause(false);
        fireRestartedMessage();
      }
    }

    return !stop;
  }

  @Override
  public void setPause(boolean pause) {
    this.pause = pause;
    if (!pause) {
      notifyMonitor();
    }
  }

  @Override
  public void step() {
    step = true;
    if (pause) {
      pause = false;
      notifyMonitor();
    }
  }

  @Override
  public void stop() {
    stop = true;
    if (pause) {
      pause = false;
      notifyMonitor();
    }

    try {
      if (executingThread != null) {
        // wait at most 20 seconds.
        executingThread.join(20000);
        
        if (executingThread.isAlive())
          executingThread.interrupt();
      }
    } catch (InterruptedException ex) {
      msgCenter.warn("Error while waiting for sim thread to stop", ex);
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

}
