/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;

/**
 * Provides access to the environment in which a particular model run executes.
 * For example, the schedule is made available to running simulation code in
 * this way.
 * 
 * @author Nick Collier
 */
public class RunEnvironment {

  public static int DEFAULT_SPARKLINE_LENGTH = 50;

  public static boolean DEFAULT_SPARKLINE_TYPE = true;

  private int sparklineLength = DEFAULT_SPARKLINE_LENGTH;

  private boolean sparklineLineType = DEFAULT_SPARKLINE_TYPE;

  /**
   * A delay that the schedule will wait between ticks.
   */
  private int scheduleTickDelay = 0;

  public int getScheduleTickDelay() {
    return scheduleTickDelay;
  }

  /**
   * Set the delay between schedule executions.
   * 
   * @param scheduleTickDelay
   *          the delay between schedule executions.
   */
  public void setScheduleTickDelay(int scheduleTickDelay) {
    this.scheduleTickDelay = scheduleTickDelay;
  }

  public boolean getSparklineType() {
    return sparklineLineType;
  }

  public void setSparklineType(boolean sparklineType) {
    this.sparklineLineType = sparklineType;
  }

  public int getSparklineLength() {
    return sparklineLength;
  }

  public void setSparklineLength(int sparklineLength) {
    this.sparklineLength = sparklineLength;
  }

  private class StopAction implements IAction {
    private static final long serialVersionUID = 198464767L;

    public void execute() {
      scheduleRunner.stop();
    }
  }

  private class PauseAction implements IAction {
    private static final long serialVersionUID = 98732418679L;

    public void execute() {
      scheduleRunner.setPause(true);
    }
  }

  // singleton
  private static RunEnvironment instance;

  /**
   * Gets the current RunEnvironment.
   * 
   * @return the current RunEnvironment.
   */
  public static RunEnvironment getInstance() {
    return instance;
  }

  private ISchedule schedule;
  private Runner scheduleRunner;
  private ISchedulableAction pauseAction, stopAction;
  private boolean isBatch = false;
  private Parameters parameters;

  // creates a new RunEnviroment
  private RunEnvironment(ISchedule schedule, Runner scheduleRunner, Parameters parameters,
      boolean isBatch) {
    this.schedule = schedule;
    this.scheduleRunner = scheduleRunner;
    this.isBatch = isBatch;
    this.parameters = parameters;
  }

  /**
   * Initializes a RunEnvironment. RunEnvironment.getInstance() will return null
   * until this is called.
   * 
   * @param schedule
   * @param scheduleRunner
   */
  public static void init(ISchedule schedule, Runner scheduleRunner, Parameters parameters,
      boolean isBatch) {
    instance = new RunEnvironment(schedule, scheduleRunner, parameters, isBatch);
  }

  /**
   * Gets the schedule on which the current run's events are scheduled for
   * execution.
   * 
   * @return the schedule on which the current run's events are scheduled for
   *         execution.
   */
  public ISchedule getCurrentSchedule() {
    return schedule;
  }

  /**
   * Sets the run to pause at the specified tick.
   * 
   * @param tick
   *          the tick at which to pause
   */
  public void pauseAt(double tick) {
    if (pauseAction != null) {
      schedule.removeAction(pauseAction);
    }
    pauseAction = schedule.schedule(ScheduleParameters.createOneTime(tick), new PauseAction());
  }

  /**
   * Pauses the current run as soon as possible.
   */
  public void pauseRun() {
    scheduleRunner.setPause(true);
  }

  /**
   * Resumes the current run.
   */
  public void resumeRun() {
    scheduleRunner.setPause(false);
  }

  /**
   * Sets the run to end at the specified tick.
   * 
   * @param tick
   *          the tick at which to end.
   */
  public void endAt(double tick) {
    if (stopAction != null) {
      schedule.removeAction(stopAction);
    }
    stopAction = schedule.schedule(ScheduleParameters.createOneTime(tick), new StopAction());
  }

  /**
   * Ends the current run as soon as possible
   */
  public void endRun() {
    scheduleRunner.stop();
  }

  /**
   * Gets whether or not this run is part of a series of batch runs.
   * 
   * @return true if this run is part of a series of batch runs, otherwise
   *         false.
   */
  public boolean isBatch() {
    return isBatch;
  }

  /**
   * Gets the parameters for the current run.
   * 
   * @return the parameters for the current run.
   */
  public Parameters getParameters() {
    return parameters;
  }

  /**
   * Sets the parameters for the current run. In general this should only be
   * used before a run has been runInitialized.
   * 
   * @param params
   *          the runs parameters
   */
  public void setParameters(Parameters params) {
    this.parameters = params;
  }
}
