package repast.simphony.engine.watcher;

import java.util.List;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class Watcher2 {

  @Watch(watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter",
          triggerCondition = "", whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 2,
          scheduleTriggerPriority = 0)
  public void invalidMethod(List foo) {
    System.out.println("foo");
  }
}
