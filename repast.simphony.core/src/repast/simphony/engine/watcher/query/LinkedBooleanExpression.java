package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.Linked;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watcher is linked to the watchee, or watchee is linked to watcher in any network in
 * the current Context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class LinkedBooleanExpression implements IBooleanExpression {

	private Context context;

	public LinkedBooleanExpression(Context context) {
		this.context = context;
	}

	public String getExpression() {
		return "linked";
	}

	/**
	 * Returns true if the watcher is a precedecessor of the watchee, or vice-versa.
	 *
	 * @param watcher
	 * @param watchee
	 * @param field
	 * @return true if the watcher is a precedecessor of the watchee, or vice-versa, otherwise false.
	 */
	public boolean execute(Object watcher, Object watchee, Object field) {
		Linked linked = new Linked(watcher, watchee);
		Iterable<Projection<?>> iter = context.getProjections();
		for (Projection<?> proj : iter) {
			if (proj.evaluate(linked)) return true;
		}

		return false;
	}
}
