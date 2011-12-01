package repast.simphony.engine.watcher;

import repast.simphony.engine.watcher.query.IBooleanExpression;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public abstract class AbstractTriggerScheduler implements TriggerScheduler {

  protected NotifierTrigger trigger;
  protected IBooleanExpression condition, queryCondition;
  protected int amtToTrigger;
  protected boolean shuffle = true;

  public AbstractTriggerScheduler(NotifierTrigger trigger, IBooleanExpression condition, int amtToTrigger,
                                  boolean shuffle) {
    this.trigger = trigger;
    this.condition = condition;
    this.amtToTrigger = amtToTrigger;
    this.shuffle = shuffle;
  }
}
