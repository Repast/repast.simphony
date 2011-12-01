package repast.simphony.engine.schedule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for scheduling a method. 
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ScheduledMethod {
  
  public static final int NO_PARAMETERS = -1;
  public static final double END = Double.POSITIVE_INFINITY;
  public static final long ALL = Long.MAX_VALUE;

	/**
	 * Specifies the number of objects that implement to this method to schedule this method on. The
	 * objects will be drawn randomly from a Context. Default is to schedule all instances.
	 */
	long pick() default ALL;

  /**
   * Specifies the start time
   */ 
  double start() default Double.POSITIVE_INFINITY;
  
  /**
   * Specifies the interval. If > 0, then the method is scheduled to repeat at the specified
   * interval. Otherwise, method is scheduled for one time execution. Default is 0 meaning
   * by default the method will only execute once.
   */ 
  double interval() default 0;
  
  /**
   * Priority w/r to other actions scheduled for the same tick. Default is
   * ScheduleParameters.RANDOM_PRIORITY.
   */ 
  double priority() default ScheduleParameters.RANDOM_PRIORITY;
  
  /**
   * How many ticks the action can execute in the background while the tick count
   * progresses. Default is no duration meaning the method must finish execution
   * before the next scheduled action can execute.
   */ 
  double duration() default ScheduleParameters.NO_DURATION;
  
  /**
   * Whether or not to randomize the scheduling of collections of agent
   * methods, if more than one agent is being scheduled by this annonation.
   */
  boolean shuffle() default true;
}
