/**
 * 
 */
package watcherOOP.test;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;

/**
 * @author Nick Collier
 */
public class SimpleWatcher {
  
  private String str;
  private int val; 
  
  
  @Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="val, str", 
      whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
  public void fire(SimpleWatchee watchee) {
    str = watchee.getStr();
    val = watchee.getVal();
  }

  public String getStr() {
    return str;
  }

  public int getInt() {
    return val;
  }
}
