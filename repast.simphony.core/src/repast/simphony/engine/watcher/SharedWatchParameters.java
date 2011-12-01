/**
 * 
 */
package repast.simphony.engine.watcher;

import java.lang.reflect.Field;

import repast.simphony.engine.watcher.query.DefaultBooleanExpression;
import repast.simphony.engine.watcher.query.IBooleanExpression;

/**
 * Contains the invariant part of a WatchParamter that can be shared among
 * WatchParameters for different objects.
 * 
 * @author Nick Collier
 */
public class SharedWatchParameters {

  private String className, fieldName;
  private String watcheeCondition = "";
  private double schedulePriority, scheduleDelta;
  private WatcherTriggerSchedule triggerSchedule = WatcherTriggerSchedule.IMMEDIATE;
  private IBooleanExpression queryCondition = new DefaultBooleanExpression();
  private String watchID = "";
  private int pick = Integer.MAX_VALUE;
  private boolean shuffleWatchers = true;
  private ArgMatcher matcher = null;
  
  private String declaringClass;
  private Class<?> watcheeClass;
  
  public SharedWatchParameters(String classToWatch, String fieldToWatch) {
    this.className = classToWatch;
    this.declaringClass = null;
    this.fieldName = fieldToWatch;

    try {
      watcheeClass = Class.forName(classToWatch);
      Class<?> clazz = watcheeClass;
      while (clazz != null) {
        
          Field[] fields = clazz.getDeclaredFields();
          for (Field field : fields) {
            if (field.getName().equals(fieldToWatch)) { 
              declaringClass = clazz.getName();
              break;
            }
          }
          if (declaringClass == null) clazz = clazz.getSuperclass();
          else break;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public Class<?> getWatcheeClass() {
    return watcheeClass;
  }
  
  public void setArgMatcher(ArgMatcher matcher) {
    this.matcher = matcher;
  }
  
  public ArgMatcher getArgMatcher() {
    return matcher;
  }
  
  public void setTriggerSchedule(WatcherTriggerSchedule schedule, double scheduleDelta,
      double schedulePriority) {
    this.scheduleDelta = scheduleDelta;
    this.schedulePriority = schedulePriority;
    this.triggerSchedule = schedule;
  }

  public WatcherTriggerSchedule getTriggerSchedule() {
    return triggerSchedule;
  }

  public double getScheduleDelta() {
    return scheduleDelta;
  }

  public double getSchedulePriority() {
    return schedulePriority;
  }

  public String getWatchID() {
    return watchID;
  }

  public void setWatchID(String watchID) {
    this.watchID = watchID;
  }

  /**
   * Creates an id based on the watchee class and field.
   * 
   * @return an id based on the watchee class and field.
   */
  public String createWatcheeID() {
    return declaringClass + "." + fieldName;
  }

  public String getWatcheeCondition() {
    return watcheeCondition;
  }

  public void setWatcheeCondition(String watcheeCondition) {
    this.watcheeCondition = watcheeCondition;
  }

  public String getClassName() {
    return className;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setQueryCondition(IBooleanExpression condition) {
    this.queryCondition = condition;
  }

  public IBooleanExpression getQueryCondition() {
    return this.queryCondition;
  }

  /**
   * Gets the amount of watchers to trigger.
   * 
   * @return the amount of watchers to trigger.
   */
  public int getWatcherCount() {
    return pick;
  }

  /**
   * Gets the amount of watchers to trigger.
   * 
   * @param pick
   *          the amount of watchers to trigger
   */
  public void setWatcherCount(int pick) {
    this.pick = pick;
  }

  /**
   * Gets whether or not to shuffle the watchers before triggering them.
   * 
   * @return whether or not to shuffle the watchers before triggering them.
   */
  public boolean doShuffleWatchers() {
    return shuffleWatchers;
  }

  /**
   * Sets whether or not to shuffle the watchers before triggering them.
   * 
   * @param shuffleWatchers
   *          true to shuffle, false don't shuffle
   */
  public void setShuffleWatchers(boolean shuffleWatchers) {
    this.shuffleWatchers = shuffleWatchers;
  }

}
