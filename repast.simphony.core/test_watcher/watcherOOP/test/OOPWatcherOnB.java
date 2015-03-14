/**
 * 
 */
package watcherOOP.test;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;

/**
 * @author Nick Collier
 */
public class OOPWatcherOnB {
  
  private double val = -1;
  
  @Watch(watcheeClassName="watcherOOP.test.SimpleWatchee", watcheeFieldNames="val, str", 
      whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
  public void fire() {
    
  }
  
  @Watch(watcheeClassName="watcherOOP.test.WatcheeChildB", watcheeFieldNames="iVal, dVal", 
      whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
  public void fire2(WatcheeChildB watchee) {
    val = watchee.getdVal();
  }
  
  public double getVal() {
    return val;
  }
  
  public void reset() {
    val = -1;
  }

}
