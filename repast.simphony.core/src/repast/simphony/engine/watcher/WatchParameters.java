package repast.simphony.engine.watcher;

import java.lang.reflect.Method;

import repast.simphony.engine.schedule.CallBackAction;
import repast.simphony.engine.watcher.query.IBooleanExpression;
import simphony.util.messages.MessageCenter;

/**
 * Parameter object for setting watches
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class WatchParameters {

  private static MessageCenter msg = MessageCenter.getMessageCenter(WatchParameters.class);

  private String watcherMethodName;
  private Object watcher;
  private CallBackAction action;
  private SharedWatchParameters params;

  // Used for Unit testing.
  WatchParameters(String classToWatch, String fieldToWatch, Object watcher, Method method) {
    params = new SharedWatchParameters(classToWatch, fieldToWatch);
    this.watcher = watcher;
    this.watcherMethodName = method.getName();
    this.action = new CallBackAction(watcher, method);
  }

  public WatchParameters(SharedWatchParameters params, CallBackAction action) {
    this.params = params;
    this.action = action;
    this.watcherMethodName = action.getMethodName();
    this.watcher = action.getTarget();
  }
  
  public Class<?> getWatcheeClass() {
    return params.getWatcheeClass();
  }

  public void setTriggerSchedule(WatcherTriggerSchedule schedule, double scheduleDelta,
      double schedulePriority) {
    params.setTriggerSchedule(schedule, scheduleDelta, schedulePriority);
  }

  public WatcherTriggerSchedule getTriggerSchedule() {
    return params.getTriggerSchedule();
  }

  public double getScheduleDelta() {
    return params.getScheduleDelta();
  }

  public double getSchedulePriority() {
    return params.getSchedulePriority();
  }

  public String getWatchID() {
    return params.getWatchID();
  }

  public void setWatchID(String watchID) {
    params.setWatchID(watchID);
  }

  /**
   * Creates an id based on the watchee class and field.
   * 
   * @return an id based on the watchee class and field.
   */
  public String createWatcheeID() {
    return params.createWatcheeID();
  }

  public String getWatcheeCondition() {
    return params.getWatcheeCondition();
  }

  public void setWatcheeCondition(String watcheeCondition) {
    params.setWatcheeCondition(watcheeCondition);
  }

  public String getClassName() {
    return params.getClassName();
  }

  public String getFieldName() {
    return params.getFieldName();
  }

  public void setArgMatcher(ArgMatcher matcher) {
    params.setArgMatcher(matcher);
  }

  public ArgMatcher getArgMatcher() {
    ArgMatcher matcher = params.getArgMatcher();
    if (matcher == null) {
      try {

        matcher = new ArgMatcher(new DefaultWatchData(getWatchID()));
        matcher.match(action.getMethod(), getClassName(), getFieldName());
        params.setArgMatcher(matcher);

      } catch (NoSuchFieldException e) {
        msg.error("Error while creating watch parameters", e);
      } catch (ClassNotFoundException e) {
        msg.error("Error while creating watch parameters", e);
      }
    }
    return matcher;
  }

  public Object getWatcher() {
    return watcher;
  }

  public String getWatcherMethodName() {
    return watcherMethodName;
  }

  public CallBackAction getAction() {
    return action;
  }

  public void setQueryCondition(IBooleanExpression condition) {
    params.setQueryCondition(condition);
  }

  public IBooleanExpression getQueryCondition() {
    return params.getQueryCondition();
  }

  /**
   * Gets the amount of watchers to trigger.
   * 
   * @return the amount of watchers to trigger.
   */
  public int getWatcherCount() {
    return params.getWatcherCount();
  }

  /**
   * Gets the amount of watchers to trigger.
   * 
   * @param pick
   *          the amount of watchers to trigger
   */
  public void setWatcherCount(int pick) {
    params.setWatcherCount(pick);
  }

  /**
   * Gets whether or not to shuffle the watchers before triggering them.
   * 
   * @return whether or not to shuffle the watchers before triggering them.
   */
  public boolean doShuffleWatchers() {
    return params.doShuffleWatchers();
  }

  /**
   * Sets whether or not to shuffle the watchers before triggering them.
   * 
   * @param shuffleWatchers
   *          true to shuffle, false don't shuffle
   */
  public void setShuffleWatchers(boolean shuffleWatchers) {
    params.setShuffleWatchers(shuffleWatchers);
  }
}
