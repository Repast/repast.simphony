package repast.simphony.engine.watcher.query;


/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultBooleanExpression implements IBooleanExpression {

  public String getExpression() {
    return "true";
  }

  public boolean execute(Object watcher, Object watchee, Object field) {
    return true;
  }
}
