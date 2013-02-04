package repast.simphony.engine.schedule;

import repast.simphony.random.RandomHelper;
import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.Uniform;

/**
 * Parameters describing when an action should be scheduled. Use the static
 * <code>create*</code> methods to create the appropriate ScheduleParameter
 * object.
 * 
 * @see repast.simphony.engine.schedule.ISchedule
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class ScheduleParameters {

  static final long serialVersionUID = 474914325315658328L;

  /**
   * Constant representing no duration.
   */
  public static final double NO_DURATION = -1;

  /**
   * Constant representing end of simulation as the tick at which to execute the
   * actions.
   */
  public static final double END = Double.POSITIVE_INFINITY;

  /**
   * Constant representing random priority.
   */
  public static final double RANDOM_PRIORITY = Double.NaN;
  public static final double FIRST_PRIORITY = Double.POSITIVE_INFINITY;
  public static final double LAST_PRIORITY = Double.NEGATIVE_INFINITY;

  private double start, interval, priority;
  private PriorityType pType;
  // be explicit.
  private double duration = 0;
  private Frequency frequency = Frequency.REPEAT;
  private int hashCode = 17;

  protected ScheduleParameters(double start, Frequency frequency, double interval, double priority,
      PriorityType pType, double duration) {
    this.start = start;
    this.frequency = frequency;
    this.interval = interval;
    this.priority = priority;
    this.duration = duration;

    if (pType == null) {
      if (priority == RANDOM_PRIORITY)
        this.pType = PriorityType.RANDOM;
      else if (priority == FIRST_PRIORITY)
        this.pType = PriorityType.FIRST;
      else if (priority == LAST_PRIORITY)
        this.pType = PriorityType.LAST;
      else {
        this.pType = PriorityType.OTHER;
      }
    } else {
      this.pType = pType;
    }
    
    long l = Double.doubleToLongBits(start);
    hashCode = 31 * hashCode + (int)(l ^ (l >>> 32));
    l = Double.doubleToLongBits(interval);
    hashCode = 31 * hashCode + (int)(l ^ (l >>> 32));
    l = Double.doubleToLongBits(priority);
    hashCode = 31 * hashCode + (int)(l ^ (l >>> 32));
    l = Double.doubleToLongBits(duration);
    hashCode = 31 * hashCode + frequency.hashCode();
    hashCode = 31 * hashCode + pType.hashCode();
  }

  /**
   * Gets whether or not the specified parameters are scheduled with a Random
   * priority.
   * 
   * @param params
   * 
   * @return true if the specified parameters are scheduled with a random
   *         priority, otherwise false.
   */
  public static boolean isRandomPriority(ScheduleParameters params) {
    return params.getPriorityType() == PriorityType.RANDOM;
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a repeating action.
   * The action will start at the specified time, and repeat at the specified
   * interval.
   * 
   * @param start
   *          the start time
   * @param interval
   *          the interval at which the execution of the action should repeat
   * @return a ScheduleParameters appropriate for scheduling a repeating action
   */
  public static ScheduleParameters createRepeating(double start, double interval) {
    return new ScheduleParameters(start, Frequency.REPEAT, interval, RANDOM_PRIORITY, 
        null, NO_DURATION);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a repeating action
   * of the first, last, random, or first of last priority type. The action will
   * repeat at the specified interval. Use ScheduleParameters
   * createRepeating(double start, double interval, double priority) to schedule
   * an action with a numeric priority.
   * 
   * @param start
   *          the start time
   * @param interval
   *          the interval at which the execution of the action should repeat
   * @param priorityType
   *          the priority type of the action
   * @return a ScheduleParameters appropriate for scheduling a repeating action
   */
  public static ScheduleParameters createRepeating(double start, double interval,
      PriorityType priorityType) {
    return new ScheduleParameters(start, Frequency.REPEAT, interval, 0, priorityType, NO_DURATION);
  }

  /**
   * Creates a one time schedule event that is based on a mean start time and
   * standard deviation start time.
   * 
   * @param meanStart
   *          mean start time
   * @param sdStart
   *          standard deviation start time
   * @param priority
   *          the priority of the scheduled task
   * @return a schedule parameters object based on a mean(x) and std(x)
   *         probability .
   */
  public static ScheduleParameters createNormalProbabilityOneTime(double meanStart, double sdStart,
      double priority) {
    Normal n = RandomHelper.createNormal(meanStart, sdStart);
    return createOneTime(n.nextDouble(), priority);
  }

  /**
   * Creates a one time schedule event that is based on a uniform distribution
   * with arguments delineating the minimum and maximum of that distribution.
   * 
   * @param minStart
   *          the minimum start time, but not included in the distribution
   * @param maxStart
   *          the maximum start time, but not included in the distribution
   * @param priority
   *          the priority of the scheduled task
   * @return a schedule parameters object based on minimum and maximum uniform
   *         probability.
   */
  public static ScheduleParameters createUniformProbabilityOneTime(double minStart,
      double maxStart, double priority) {
    Uniform u = RandomHelper.createUniform(minStart, maxStart);
    return createOneTime(u.nextDouble(), priority);
  }

  /**
   * Creates a one time schedule event that is based on a Poisson distribution
   * with the input argument providing the mean to the distribution.
   * 
   * @param mean
   *          the mean of the Poisson distribution
   * @return a schedule parameters object based a Poisson probability.
   */
  public static ScheduleParameters createPoissonProbabilityOneTime(double mean, double priority) {
    Poisson p = RandomHelper.createPoisson(mean);
    return createOneTime(p.nextDouble(), priority);
  }

  /**
   * Creates a repeating schedule event that is based on a Poisson distribution
   * with the input arguments providing the mean to the distribution and
   * repeating meaning interval.
   * 
   * @param mean
   *          the mean of the Poisson distribution
   * @param meanRepeating
   *          the mean of the repeating interval
   * @return a schedule parameters object based on Poisson probabilities.
   */
  public static ScheduleParameters createPoissonProbabilityRepeating(double mean,
      double meanRepeating, double priority) {
    Poisson p = RandomHelper.createPoisson(mean);
    Poisson pRepeating = RandomHelper.createPoisson(meanRepeating);
    return createRepeating(p.nextDouble(), pRepeating.nextDouble(), priority);
  }

  /**
   * Creates a repeating schedule event that is based on a mean start/interval
   * time and standard deviation start/interval time.
   * 
   * @param meanStart
   *          mean start time
   * @param sdStart
   *          standard deviation start time
   * @param meanInterval
   *          mean interval time
   * @param sdInterval
   *          standard deviation interval time
   * @param priority
   *          the priority of the scheduled task
   * @return a schedule parameters object based on mean(x) and std(x)
   *         probabilities .
   */
  public static ScheduleParameters createNormalProbabilityrRepeating(double meanStart,
      double sdStart, double meanInterval, double sdInterval, double priority) {
    Normal n = RandomHelper.createNormal(meanStart, sdStart);
    Normal interval = RandomHelper.createNormal(meanInterval, sdInterval);
    return createRepeating(n.nextDouble(), interval.nextDouble(), priority);
  }

  /**
   * Creates a repeating schedule event that is based on a uniform distribution
   * with arguments delineating the minimum and maximum of that distribution and
   * repeating interval.
   * 
   * @param minStart
   *          the minimum start time, but not included in the distribution
   * @param maxStart
   *          the maximum start time, but not included in the distribution
   * @param minInterval
   *          the minimum interval time, but not included in the distribution
   * @param maxInterval
   *          the maximum interval time, but not included in the distribution
   * @param priority
   *          the priority of the scheduled task
   * @return a schedule parameters object based on minimum and maximum uniform
   *         probabilities.
   */
  public static ScheduleParameters createUniformProbabilityRepeating(double minStart,
      double maxStart, double minInterval, double maxInterval, double priority) {
    Uniform u = RandomHelper.createUniform(minStart, maxStart);
    Uniform uInterval = RandomHelper.createUniform(minInterval, maxInterval);
    return createRepeating(u.nextDouble(), uInterval.nextDouble(), priority);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a repeating action.
   * The action will start at the specified time, repeat at the specified
   * interval, and have the specified priority. Priority refers the order in
   * which an action is executed with respect to other actions scheduled for the
   * same clock tick.
   * 
   * @param start
   *          the start time
   * @param interval
   *          the interval at which the execution of the action should repeat
   * @param priority
   *          the execution priority of an action with respect to other actions
   *          scheduled for the same clock tick. A higher number means a higher
   *          priority. So an action with a priority of 3 will occur before an
   *          action with priority of -2. Special values are:
   *          ScheduleParameters.FIRST_PRIORITY which will schedule the action
   *          to execute first; ScheduleParameters.LAST_PRIORITY which will
   *          scheduled the action to execute last; and
   *          ScheduleParameters.RANDOM_PRIORITY which will schedule the action
   *          to execute in random order, but not before any "first" actions and
   *          not "after" any last actions. Actions whose priority is same are
   *          executed in the order they are added to the schedule.
   * 
   * @return a ScheduleParameters appropriate for scheduling a repeating action
   */
  public static ScheduleParameters createRepeating(double start, double interval, double priority) {
    return new ScheduleParameters(start, Frequency.REPEAT, interval, priority, 
        null, NO_DURATION);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a repeating action.
   * The action will start at the specified time, repeat at the specified
   * interval, and have the specified priority. Priority refers the order in
   * which an action is executed with respect to other actions scheduled for the
   * same clock tick. The action will run in the background for the specified
   * duration.
   * 
   * @param start
   *          the start time
   * @param interval
   *          the interval at which the execution of the action should repeat
   * @param priority
   *          the execution priority of an action with respect to other actions
   *          scheduled for the same clock tick. A higher number means a higher
   *          priority. So an action with a priority of 3 will occur before an
   *          action with priority of -2. Special values are:
   *          ScheduleParameters.FIRST_PRIORITY which will schedule the action
   *          to execute first; ScheduleParameters.LAST_PRIORITY which will
   *          scheduled the action to execute last; and
   *          ScheduleParameters.RANDOM_PRIORITY which will schedule the action
   *          to execute in random order, but not before any "first" actions and
   *          not "after" any last actions. Actions whose priority is same are
   *          executed in the order they are added to the schedule.
   * @param duration
   *          the number of clock ticks the action can run in the background
   *          before it behaves like a normally scheduled action
   * @return a ScheduleParameters appropriate for scheduling a repeating action
   */
  public static ScheduleParameters createRepeating(double start, double interval, double priority,
      double duration) {
    return new ScheduleParameters(start, Frequency.REPEAT, interval, priority, null,
        duration);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a non-repeating
   * action. The action will start at the specified time.
   * 
   * @param start
   *          the start time
   * @return a ScheduleParameters appropriate for scheduling a non-repeating
   *         action
   */
  public static ScheduleParameters createOneTime(double start) {
    return new ScheduleParameters(start, Frequency.ONE_TIME, 0, RANDOM_PRIORITY, 
        null, NO_DURATION);
  }

  /**
   * Creates a ScheduleParameters for scheduling a non-repeating action of the
   * specified PriorityType. This method is appropriate for scheduling one time
   * with a priority of first, last, random, and first of last. Use
   * createOneTime(double, double) to schedule a one time action with numeric
   * priority.
   * 
   * @param start
   *          the start type
   * @param type
   *          the type of priority the action should have.
   * @return the created ScheduleParameters.
   */
  public static ScheduleParameters createOneTime(double start, PriorityType type) {
    return new ScheduleParameters(start, Frequency.ONE_TIME, 0, 0, type, NO_DURATION);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a non-repeating
   * action. The action will start at the specified time and have the specified
   * priority. Priority refers the order in which an action is executed with
   * respect to other actions scheduled for the same clock tick.
   * 
   * @param start
   *          the start time
   * @param priority
   *          the execution priority of an action with respect to other actions
   *          scheduled for the same clock tick. A higher number means a higher
   *          priority. So an action with a priority of 3 will occur before an
   *          action with priority of -2. Special values are:
   *          ScheduleParameters.FIRST_PRIORITY which will schedule the action
   *          to execute first; ScheduleParameters.LAST_PRIORITY which will
   *          scheduled the action to execute last; and
   *          ScheduleParameters.RANDOM_PRIORITY which will schedule the action
   *          to execute in random order, but not before any "first" actions and
   *          not "after" any last actions. Actions whose priority is same are
   *          executed in the order they are added to the schedule.
   * @return a ScheduleParameters appropriate for scheduling a non-repeating
   *         action
   */
  public static ScheduleParameters createOneTime(double start, double priority) {
    return new ScheduleParameters(start, Frequency.ONE_TIME, 0, priority, null,
        NO_DURATION);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling a non-repeating
   * action. The action will start at the specified time, and have the specified
   * priority. Priority refers the order in which an action is executed with
   * respect to other actions scheduled for the same clock tick. The action will
   * run in the background for the specified duration.
   * 
   * @param start
   *          the start time
   * @param priority
   *          the execution priority of an action with respect to other actions
   *          scheduled for the same clock tick. A higher number means a higher
   *          priority. So an action with a priority of 3 will occur before an
   *          action with priority of -2. Special values are:
   *          ScheduleParameters.FIRST_PRIORITY which will schedule the action
   *          to execute first; ScheduleParameters.LAST_PRIORITY which will
   *          scheduled the action to execute last; and
   *          ScheduleParameters.RANDOM_PRIORITY which will schedule the action
   *          to execute in random order, but not before any "first" actions and
   *          not "after" any last actions. Actions whose priority is same are
   *          executed in the order they are added to the schedule.
   * @param duration
   *          the number of clock ticks the action can run in the background
   *          before it behaves like a normally scheduled action
   * @return a ScheduleParameters appropriate for scheduling a repeating action
   */
  public static ScheduleParameters createOneTime(double start, double priority, double duration) {
    return new ScheduleParameters(start, Frequency.ONE_TIME, 0, priority,  null, duration);
  }

  /**
   * Creates a ScheduleParameters appropriate for scheduling an action to occur
   * at the end of the simulation.
   * 
   * @param priority
   *          the execution priority of an action with respect to other actions
   *          scheduled for the same clock tick. A higher number means a higher
   *          priority. So an action with a priority of 3 will occur before an
   *          action with priority of -2. Special values are:
   *          ScheduleParameters.FIRST_PRIORITY which will schedule the action
   *          to execute first; ScheduleParameters.LAST_PRIORITY which will
   *          scheduled the action to execute last; and
   *          ScheduleParameters.RANDOM_PRIORITY which will schedule the action
   *          to execute in random order, but not before any "first" actions and
   *          not "after" any last actions. Actions whose priority is same are
   *          executed in the order they are added to the schedule.
   * 
   * @return a ScheduleParameters appropriate for scheduling an action to occur
   *         at the end of the simulation.
   */
  public static ScheduleParameters createAtEnd(double priority) {
    return new ScheduleParameters(END, Frequency.ONE_TIME, 0, priority, null, NO_DURATION);
  }

  /**
   * Gets the scheduled starting time for an action.
   * 
   * @return the scheduled starting time.
   */
  public double getStart() {
    return start;
  }

  /**
   * Gets the priority type of this ScheduleParameter.
   * 
   * @return the priority type of this ScheduleParameter.
   */
  public PriorityType getPriorityType() {
    return pType;
  }

  /**
   * Gets the priority to schedule an action with.
   * 
   * @return the priority to schedule an action with.
   */
  public double getPriority() {
    return priority;
  }

  /**
   * Gets the interval at which to repeat the execution of an action. A
   * non-repeating action will return 0.
   * 
   * @return the interval at which to repeat the execution of an action. A
   *         non-repeating action will return 0.
   */
  public double getInterval() {
    return interval;
  }

  /**
   * Gets the scheduled Frequency of execution.
   * 
   * @return the scheduled Frequency of execution.
   */
  public Frequency getFrequency() {
    return frequency;
  }

  /**
   * Gets the scheduled duration.
   * 
   * @return the scheduled duration.
   */
  public double getDuration() {
    return duration;
  }

  /**
   * Gets the hash code for this schedule parameters.
   * 
   * @return this schedule parameters hash code
   */
  @Override
  public int hashCode() {
    return hashCode;
  }

  /**
   * Checks if this ScheduleParamters equals the passed in object. The passed
   * object is equal to this one if all its parameters (start, end, etc.)
   * are equal to this one.
   * 
   * @return if the object is equal to the current one as defined above
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ScheduleParameters)) {
      return false;
    }

    ScheduleParameters otherParams = (ScheduleParameters) obj;

    return otherParams.start == this.start && otherParams.frequency.equals(this.frequency)
        && otherParams.interval == this.interval
        && ((Double) otherParams.priority).equals(this.priority)
        && otherParams.duration == this.duration && otherParams.pType == this.pType;
  }

  public String toString() {
    return String
        .format(
            "ScheduleParameters[start: %f, frequency: %s, interval: %f, priority: %f, priorityType: %s, duration: %f]",
            start, frequency, interval, priority, pType, duration);
  }
}
