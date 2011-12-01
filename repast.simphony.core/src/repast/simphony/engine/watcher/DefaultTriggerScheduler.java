package repast.simphony.engine.watcher;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.watcher.query.IBooleanExpression;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Schedules the execution of trigger on a list of watchers at
 * the time defined by the Watch.
 *
 * @author Nick Collier
 */
public class DefaultTriggerScheduler extends AbstractTriggerScheduler {

  private ISchedule schedule;
  private double delta, priority;

  public DefaultTriggerScheduler(NotifierTrigger trigger, ISchedule schedule, WatchParameters watchParams,
                                 IBooleanExpression condition) {
    super(trigger, condition, watchParams.getWatcherCount(), watchParams.doShuffleWatchers());
    this.schedule = schedule;
    this.delta = watchParams.getScheduleDelta();
    this.priority = watchParams.getSchedulePriority();
  }

  /**
   * Scheduled the execution of the trigger for all watchers managed by
   * the notifier.
   *
   * @param notifier the notifier responsible for this particular watch
   * @param watchee  the watchee
   * @param value    the new field value.
   */
  public void scheduleExecution(Notifier2 notifier, Object watchee, Object value) {
    List<Object> watchers = new ArrayList<Object>();
    // create a list of the watchers that pass the condition.
    for (Object watcher : notifier.watchers(false)) {
      if (condition.execute(watcher, watchee, value)) watchers.add(watcher);
    }

    if (watchers.size() > 0) {
      double start = schedule.getTickCount() + delta;
      if (watchers.size() > amtToTrigger) watchers = new ArrayList<Object>(watchers.subList(0, amtToTrigger));
      if (shuffle) SimUtilities.shuffle(watchers, RandomHelper.getUniform());
      schedule.schedule(ScheduleParameters.createOneTime(start, priority),
              new TriggerAction(watchers, watchee, value, trigger));
    }
  }
}

class TriggerAction implements IAction {

  private List<Object> watchers;
  private Object watchee, value;
  private NotifierTrigger trigger;

  public TriggerAction(List<Object> watchers, Object watchee, Object value, NotifierTrigger trigger) {
    this.watchers = watchers;
    this.watchee = watchee;
    this.value = value;
    this.trigger = trigger;
  }

  public void execute() {
    for (Object watcher : watchers) {
      trigger.execute(watcher, watchee, value);
    }
  }
}
