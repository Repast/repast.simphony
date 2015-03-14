package repast.simphony.engine.watcher;

/**
 * @author Nick Collier
 *         Date: Mar 28, 2008 9:50:47 AM
 */
public class PickTestAgent {

  private Counter counter;

  public PickTestAgent(Counter counter) {
    this.counter = counter;
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter",
          whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 1, scheduleTriggerPriority = 0,
          pick = 10, shuffle = true)
  public void incrementCounterB() {
    counter.incrementB();
  }

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter",
          whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, pick = 10, shuffle = false)
  public void incrementCounterA() {
    counter.incrementA();
  }
}
