package repast.simphony.engine.watcher;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

import repast.simphony.engine.schedule.CallBackAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.watcher.query.AndBooleanCondition;
import repast.simphony.engine.watcher.query.BooleanExpressionCreator;
import repast.simphony.engine.watcher.query.DefaultBooleanExpression;
import repast.simphony.engine.watcher.query.IBooleanExpression;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ClassUtilities;
import repast.simphony.util.SimUtilities;
import simphony.util.messages.MessageCenter;

/**
 * Notifies watchers that their watchee state has changed. This includes evaluating
 * any queries etc. and scheduling the actual method call on the watcher.  A Notifier
 * has a one to one relationship with a @Watch annotation. All instances of the class
 * containing that annotation are managed by a single notifier.
 *
 * @author Nick Collier
 */
public class Notifier2 {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(Notifier2.class);

  static class SetupTrigger implements TriggerScheduler {

    public void scheduleExecution(Notifier2 notifier, Object watchee, Object value) {
      try {
        notifier.createAction();
        IBooleanExpression condition = new DefaultBooleanExpression();
        String watcheeCondition = notifier.watchParams.getWatcheeCondition();
        if (watcheeCondition != null && watcheeCondition.length() > 0) {
          BooleanExpressionCreator creator = new BooleanExpressionCreator();
          // using this class, means that the boolean expression will cast the value arg
          // to the watchee field type.
          Class<?> clazz = ClassUtilities.deepFindField(watchee.getClass(), notifier.watchParams.getFieldName()).getType();
          // need to convert to wrapper so as the signature of the BooleanExpression method takes an object
          // and the body then casts that object to the actual type. This won't work if we try to cast
          // Integer to int, for example.
          clazz = ClassUtils.primitiveToWrapper(clazz);
          //Class<?> clazz = value.getClass();
          condition = creator.create(notifier.id, watcheeCondition, notifier.watchParams.getWatcher().getClass(),
                  watchee.getClass(), clazz);
        }

        NotifierTrigger trigger = notifier.watchParams.getArgMatcher().createTrigger(notifier.action);
        notifier.triggerScheduler = notifier.createTriggerScheduler(condition, trigger);
        notifier.triggerScheduler.scheduleExecution(notifier, watchee, value);

      } catch (Exception ex) {
        msg.error("Error while initializing watcher triggerScheduler", ex);
      }
    }
  }


  private Set<Object> watchers = new LinkedHashSet<Object>();
  private NotifierID id;
  private ISchedule schedule;
  private WatchParameters watchParams;
  private TriggerScheduler triggerScheduler = new SetupTrigger();
  private CallBackAction action;

  /**
   * Creates a Notifier from the specified parameters and schedule.
   *
   * @param id       a notifierID the ids this Notifier2
   * @param params   the parameters used in the notification
   * @param schedule the schedule on which any method calls with be scheduled
   */
  public Notifier2(NotifierID id, WatchParameters params, ISchedule schedule) {
    this.id = id;
    this.schedule = schedule;
    watchers.add(params.getWatcher());
    this.watchParams = params;
  }

  private void createAction() {
    action = watchParams.getAction();
    if (action == null) {
      String methodName = watchParams.getWatcherMethodName();
      Object toNotify = watchParams.getWatcher();
      //int pCount = 0;
      //ArgMatcher argMatcher = watchParams.getArgMatcher();
      //if (argMatcher != null) pCount = argMatcher.getArgCount();

      action = new CallBackAction(toNotify, methodName);
    }
  }

  /**
   * Gets an iterable over all the watchers managed by this
   * Notifier.
   *
   * @param shuffle whether or not the iterable should be over shuffled
   *                list of the watchers.
   * @return an iterable over all the watchers managed by this
   *         Notifier.
   */
  public Iterable<Object> watchers(boolean shuffle) {
    if (shuffle) {
      List<Object> list = new ArrayList<Object>(watchers);
      SimUtilities.shuffle(list, RandomHelper.getUniform());
      return list;
    } else {
      return watchers;
    }
  }

  /**
   * Gets the number watchers managed by this Notifier.
   *
   * @return the number watchers managed by this Notifier.
   */
  public int getWatcherCount() {
    return watchers.size();
  }

  /**
   * Adds a watcher to be notified by this Notifier. It is the
   * responsibility of the caller to insure that the watch parameters
   * of the added watcher are the same as those associated with this
   * Notifier.
   *
   * @param watcher the watcher to add
   */
  public void addWatcher(Object watcher) {
    watchers.add(watcher);
  }

  /**
   * Called when the watchee state (i.e. a field set) has changed
   * and the watchers need to be notified.
   *
   * @param watchee the watchee
   * @param value   the new value of the field
   */
  public void triggered(Object watchee, Object value) {
    // trigger if the watchee class is sames the value class or is a parent
    // of it.
    if (watchParams.getWatcheeClass().isAssignableFrom(watchee.getClass())) {
      triggerScheduler.scheduleExecution(this, watchee, value);
    }
  }

  /**
   * Removes the specified watcher from this notifier. The
   * specified watcher will no longer have the relevant method
   * called when the watchee changes.
   *
   * @param watcher the watcher to remove
   */
  public void removeWatcher(Object watcher) {
    watchers.remove(watcher);
  }

  private TriggerScheduler createTriggerScheduler(IBooleanExpression condition, NotifierTrigger trigger) {
    AndBooleanCondition andCondition = new AndBooleanCondition(watchParams.getQueryCondition(), condition);
    if (watchParams.getTriggerSchedule() == WatcherTriggerSchedule.IMMEDIATE) {
      return new ImmediateExecutor(trigger, andCondition, watchParams.getWatcherCount(),
              watchParams.doShuffleWatchers());
    }

    return new DefaultTriggerScheduler(trigger, schedule, watchParams, andCondition);
  }
}
