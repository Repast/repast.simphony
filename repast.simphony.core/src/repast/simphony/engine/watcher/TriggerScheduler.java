package repast.simphony.engine.watcher;

/**
 * Interface for classes that schedule a NotifierTrigger for execution.
 *
 * @author Nick Collier
 */
public interface TriggerScheduler {

  /**
   * Schedules the trigger for execution.
   *
   * @param notifier the notifier responsible for this particular watch
   * @param watchee  the watchee
   * @param value    the new field value.
   */
  public void scheduleExecution(Notifier2 notifier, Object watchee, Object value);

}
