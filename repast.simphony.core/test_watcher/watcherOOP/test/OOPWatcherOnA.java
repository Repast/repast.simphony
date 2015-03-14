/**
 * 
 */
package watcherOOP.test;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;

/**
 * @author Nick Collier
 */
public class OOPWatcherOnA {
  
  private int val = -1;
  private String str = "";
  
  
  @Watch(watcheeClassName="watcherOOP.test.WatcheeChildA", watcheeFieldNames="val", 
      whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
  public void fire2(WatcheeChildA watchee) {
    val = watchee.getVal();
  }
  
  public int getVal() {
    return val;
  }
  
  public void reset() {
    val = -1;
    str = "";
  }
  
  public String getString() {
    return str;
  }

}
