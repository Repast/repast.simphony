package repast.simphony.engine.watcher;

import java.lang.reflect.Method;

/**
 * Encapsulates the unique identifying data of
 * a notifier. This includes the annotated method
 * and the annotation parameters.
 *
 * @author Nick Collier
 */
public class NotifierID {


  private Class<?> watcherClass;
  private Method watcherMethod;
  private String watcheeID, query, triggerExp, watchID;
  private double schDelta, schPriority;
  private WatcherTriggerSchedule triggerSchedule;
  private int hashCode;
  private boolean shuffle;


  /**
   * Creates a new NotifierID out of the specified WatchParameters.
   *
   * @param params the parameters to create the ID out of.
   */

  public NotifierID(WatchParameters params) {
    this.watcheeID = params.createWatcheeID();
    this.watcherClass = params.getWatcher().getClass();
    this.watcherMethod = params.getAction().getMethod();
    this.query = params.getQueryCondition().getExpression();
    this.triggerExp = params.getWatcheeCondition();
    this.schDelta = params.getScheduleDelta();
    this.triggerSchedule = params.getTriggerSchedule();
    this.schPriority = params.getSchedulePriority();
    this.shuffle = params.doShuffleWatchers();
    this.watchID = params.getWatchID();

    hashCode = 17;
    hashCode = 37 * hashCode + watcherClass.hashCode();
    hashCode = 37 * hashCode + watcherMethod.hashCode();
    hashCode = 37 * hashCode + watcheeID.hashCode();
    hashCode = 37 * hashCode + query.hashCode();
    hashCode = 37 * hashCode + triggerExp.hashCode();
    hashCode = 37 * hashCode + triggerSchedule.hashCode();
    hashCode = 37 * hashCode + watchID.hashCode();
    hashCode = 37 * hashCode + (shuffle ? 1 : 0);
    if (!Double.isNaN(schPriority)) {
      long l = Double.doubleToLongBits(schPriority);
      hashCode = 37 * hashCode + (int) (l ^ (l >>> 32));
    }
    long l = Double.doubleToLongBits(schDelta);
    hashCode = 37 * hashCode + (int) (l ^ (l >>> 32));

  }

  public boolean equals(Object obj) {
    if (!(obj instanceof NotifierID)) return false;
    NotifierID other = (NotifierID) obj;
    if (Double.isNaN(schPriority) && !Double.isNaN(other.schPriority)) return false;
    if (!Double.isNaN(schPriority) && Double.isNaN(other.schPriority)) return false;
    if (!Double.isNaN(schPriority) && !Double.isNaN(other.schPriority) && schPriority != other.schPriority)
      return false;
    return watcherClass.equals(other.watcherClass) && watcherMethod.equals(other.watcherMethod) &&
            watcheeID.equals(other.watcheeID) && schDelta == other.schDelta && query.equals(other.query) &&
            watchID.equals(other.watchID) &&
            
            // trigger schedule is an enum so can do ==
            triggerExp.equals(other.triggerExp) && triggerSchedule == other.triggerSchedule &&
            this.shuffle == other.shuffle;
  }

  public int hashCode() {
    return hashCode;
  }
}
