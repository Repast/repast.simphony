package repast.simphony.engine.watcher.query;

/**
 * Flips the result of a passed in IBooleanExpression.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NotBooleanExpression implements IBooleanExpression {

	private IBooleanExpression exp;

	public NotBooleanExpression(IBooleanExpression exp) {
		this.exp = exp;
	}

	public String getExpression() {
		return "not " + exp.getExpression();
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		return !exp.execute(watcher, watchee, field);
	}
}
