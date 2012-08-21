package repast.simphony.batch;

import repast.simphony.engine.controller.TickListener;
import repast.simphony.engine.environment.AbstractRunner;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;

/**
 * @author Nick Collier
 */
public class BatchScheduleRunner extends AbstractRunner {

  private static final MessageCenter msgCenter = MessageCenter
      .getMessageCenter(BatchScheduleRunner.class);

  private TickListener tickListener = null;

  // runnable that implements that actual execution of actions
  // on the schedule. This will loop until stop() is called on
  // this BatchScheduleRunner
  class ScheduleLoopRunnable implements Runnable {

    private ISchedule schedule;

    public ScheduleLoopRunnable(ISchedule schedule) {
      this.schedule = schedule;
    }

    public void run() {
      fireStartedMessage();
      try {
        while (go() && schedule.getActionCount() > 0) {
          if (schedule.getModelActionCount() == 0) {
            schedule.setFinishing(true);
          }
          schedule.execute();
          if (tickListener != null) {
            tickListener.tickCountUpdated(schedule.getTickCount());
          }
        }
        schedule.executeEndActions();
      } catch (RuntimeException ex) {
        msgCenter.fatal("GUIScheduleRunner.execute: RunTimeException when running the schedule\n"
            + "Current tick ("
            + (schedule != null ? String.valueOf(schedule.getTickCount()) : "unavailable") + ")",
            ex);
      }
      fireStoppedMessage();
      // System.out.println("Schedule Completed");
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
    ISchedule schedule = toExecuteOn.getScheduleRegistry().getModelSchedule();
    Runnable runnable = new ScheduleLoopRunnable(schedule);
    Thread thread = new Thread(runnable);
    thread.start();
  }

}