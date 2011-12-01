package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ColocatedBooleanExpression implements IBooleanExpression {

	private Context context;

	public ColocatedBooleanExpression(Context context) {
		this.context = context;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		return context.contains(watcher) && context.contains(watchee);
	}

	public String getExpression() {
		return "colocated";
	}
}
