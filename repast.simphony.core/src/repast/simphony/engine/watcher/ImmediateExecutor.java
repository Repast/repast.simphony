package repast.simphony.engine.watcher;

import repast.simphony.engine.watcher.query.IBooleanExpression;

import java.util.Iterator;

/**
 * A trigger scheduler that executes the trigger
 * immediately.
 *
 * @author Nick Collier
 */
public class ImmediateExecutor extends AbstractTriggerScheduler {

  public ImmediateExecutor(NotifierTrigger trigger, IBooleanExpression condition, int amtToTrigger, boolean shuffle) {
    super(trigger, condition, amtToTrigger, shuffle);
  }

  /**
   * Executes the trigger immediately for all watchers managed by
   * the notifier.
   *
   * @param notifier the notifier responsible for this particular watch
   * @param watchee  the watchee
   * @param value    the new field value.
   */
  public void scheduleExecution(Notifier2 notifier, Object watchee, Object value) {
    if (notifier.getWatcherCount() <= amtToTrigger) {
      for (Object watcher : notifier.watchers(shuffle)) {
        if (condition.execute(watcher, watchee, value))
          trigger.execute(watcher, watchee, value);
      }
    } else {
      int i = 0;
      for (Iterator iter = notifier.watchers(shuffle).iterator(); iter.hasNext() && i < amtToTrigger;) {
        Object watcher = iter.next();
        if (condition.execute(watcher, watchee, value)) {
          trigger.execute(watcher, watchee, value);
          i++;
        }
      }
    }
  }
}
