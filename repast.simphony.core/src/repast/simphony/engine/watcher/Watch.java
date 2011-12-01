package repast.simphony.engine.watcher;

import repast.simphony.engine.schedule.ScheduleParameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for creating a watcher.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Watch {

  /**
   * Identifier for this Watch.
   */
  String id() default "";

  /**
   * <ul>
   * <li>colocated - true if the watcher and the watchee are in the same context.</li>
   * <li>linked_to ['network name'] - true if the watcher is linked to the watchee in any network, or
   * optionally in the named network</li>
   * <li>linked_from ['network name'] - true if the watcher is linked from the watchee in any
   * network, or optionally in the named network</li>
   * <li>within X ['network name'] - true if the path from the watcher to the watchee is less
   * than or equal to X where X is a double precision number. This is either for any network in the context
   * or in the named network.</li>
   * <li>within_vn X ['grid name'] - true if the watchee is in the watcher's von neumann neighborhood
   * in any grid projection or in the named grid. X is the extent of the neighborhood in the x, y,
   * [z] dimensions.</li>
   * <li>within_moore X ['grid name'] - true if the watchee is in the watcher's moore neighborhood in
   * any grid projection or in the named grid. X is the extent of the neighborhood in the x, y, [z]
   * dimensions.</li>
   * <li>within X ['geography name'] - true if the orthodromic distance from the watcher to
   * the watchee is less than or equal to X meters, otherwise false.</li>
   * </ul>
   */
  String query() default "";

  /**
   * The fully qualified class name of the watchee..
   */
  String watcheeClassName();

  /**
   * A comma separated list of the field names to watch in the watchee class.
   */
  String watcheeFieldNames();

  /**
   * The trigger condition for triggering the method on which this is an annotation. The trigger condition
   * is some arbitary boolean expression. The following variables are available to the expression $watcher, $watchee,
   * and $context. These correspond to the watcher, watchee and context. Methods can be called on these variables.
   * For example, $watcher.getAge() > 3, will call getAge on the watcher. The operators && and || are also
   * supported. For example, $watcher.getAge() > 3 && watcher.getAge() > $watchee.getAge()
   */
  String triggerCondition() default "";

  /**
   * Gets when to trigger the watch: immediately or later.
   *
   * @return Gets when to trigger the watch: immediately or later.
   */
  WatcherTriggerSchedule whenToTrigger();

  /**
   * Defaults to 1
   */
  double scheduleTriggerDelta() default 1;

  /**
   * Defaults to ScheduleParameters.RANDOM_PRIORITY
   */
  double scheduleTriggerPriority() default ScheduleParameters.RANDOM_PRIORITY;

  /**
   * The number of watchers to be triggered. The default is all
   * watchers.
   *
   * @return The number of watchers to be triggered.
   */
  int pick() default Integer.MAX_VALUE;


  /**
   * Gets whether or not the list of watchers to be triggered is shuffled prior
   * to triggering. The default is true.
   *
   * @return whether or not the list of watchers to be triggered is shuffled prior
   *         to triggering.
   */
  boolean shuffle() default true;
}
