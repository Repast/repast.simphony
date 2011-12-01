package repast.simphony.engine.watcher;


/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class Watcher3 {

  int triggerCount = 0;

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter",
          triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerDelta = 0,
          scheduleTriggerPriority = 0)
  public void trigger() {
    triggerCount++;
  }
}
