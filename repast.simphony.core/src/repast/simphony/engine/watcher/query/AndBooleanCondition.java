package repast.simphony.engine.watcher.query;


/**
 * IBooleanExpression that when evaluated returns the logical 'and' of two child expressions.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class AndBooleanCondition implements IBooleanExpression {

	private IBooleanExpression first, second;
	private String exp;

	public AndBooleanCondition(IBooleanExpression queryCondition, IBooleanExpression condition) {
		first = queryCondition;
		second = condition;
		exp = queryCondition.getExpression() + " and " + condition.getExpression();

	}

	public String getExpression() {
		return exp;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		return (first.execute(watcher, watchee, field) && second.execute(watcher, watchee, field));
	}
}
