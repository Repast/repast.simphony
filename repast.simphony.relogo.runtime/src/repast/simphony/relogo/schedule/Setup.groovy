package repast.simphony.relogo.schedule;

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import org.codehaus.groovy.transform.GroovyASTTransformationClass


/**
 * Setup method scheduler. By default schedules the method to run at tick 0.
 * <p>
 * Can optionally set different values for the following members (default values in parentheses):
 * 	<li>start (0d)
 * 	<li>priority (ScheduleParameters.RANDOM_PRIORITY)
 * 	<li>duration (ScheduleParameters.NO_DURATION)
 * 	<li>shuffle (true)
 * 	<li>pick (ScheduledMethod.ALL)
 *
 * @author jozik
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(["repast.simphony.relogo.ast.SetupASTTransformation"])
public @interface Setup {
	
	/**
	 * Specifies the start time
	 */
	double start() default 0d;
		
	/**
	 * Priority w/r to other actions scheduled for the same tick. Default is
	 * ScheduleParameters.RANDOM_PRIORITY.
	 */
	double priority() default 0d; // place holder, this actually becomes ScheduleParameters.RANDOM_PRIORITY
	
	
	/**
	 * How many ticks the action can execute in the background while the tick count
	 * progresses. Default is no duration meaning the method must finish execution
	 * before the next scheduled action can execute.
	 */
	double duration() default 0d; // place holder, this actually becomes ScheduleParameters.NO_DURATION
	
	/**
	 * Whether or not to randomize the scheduling of collections of agent
	 * methods, if more than one agent is being scheduled by this annonation.
	 */
	boolean shuffle() default true;
	
	/**
	 * Specifies the number of objects that implement to this method to schedule this method on. The
	 * objects will be drawn randomly from a Context. Default is to schedule all instances.
	 */
	long pick() default 0l; // place holder, this actually becomes repast.simphony.engine.schedule.ScheduledMethod.ALL

}
