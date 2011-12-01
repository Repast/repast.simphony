package repast.simphony.engine.watcher.query;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class OrBooleanExpression implements IBooleanExpression {

	private IBooleanExpression lhs;
	private IBooleanExpression rhs;

	public OrBooleanExpression(IBooleanExpression lhs, IBooleanExpression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		return lhs.execute(watcher, watchee, field) || rhs.execute(watcher, watchee, field);
	}

	public String getExpression() {
		return lhs.getExpression() + " or " + rhs.getExpression();
	}
}
