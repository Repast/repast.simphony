package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.Linked;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watcher is adjacent to the watchee in the named projection in
 * the current Context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NamedLinkedBooleanExpression implements IBooleanExpression {

	private Context context;
	private String name;

	public NamedLinkedBooleanExpression(String name, Context context) {
		this.context = context;
		this.name = name;
	}

	public String getExpression() {
		return "linked " + name;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		Projection proj = context.getProjection(name);
		if (proj != null) {
			Linked linked = new Linked(watcher, watchee);
			return proj.evaluate(linked);
		}

		return false;
	}
}
