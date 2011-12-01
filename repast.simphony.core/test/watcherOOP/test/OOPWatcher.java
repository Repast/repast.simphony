/**
 * 
 */
package watcherOOP.test;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;

/**
 * @author Nick Collier
 */
public class OOPWatcher {
  
  @Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="val, str", 
      whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
  public void fire() {
    
  }
  
  @Watch(watcheeClassName="watcherOOP.test.WatcheeChildA", watcheeFieldNames="val", 
      whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
  public void fire2() {
    
  }

}
