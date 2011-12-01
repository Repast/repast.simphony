package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.LinkedTo;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watcher is linked to the watchee in the named network in
 * the current Context. Note that for an undirected network, "linked to" is identical
 * to "linked."
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NamedLinkedToBooleanExpression implements IBooleanExpression {

	private Context context;
	private String name;

	public NamedLinkedToBooleanExpression(String name, Context context) {
		this.context = context;
		this.name = name;
	}

	public String getExpression() {
		return "linked to " + name;
	}

	/**
	 * Returns true if the watchee is linked to the watcher in the named projection.
	 *
	 * @param watcher
	 * @param watchee
	 * @param field
	 * @return true if the watchee is a precedecessor of the watcher.
	 */
	public boolean execute(Object watcher, Object watchee, Object field) {
		Projection proj = context.getProjection(name);
		if (proj != null) {
			LinkedTo linked = new LinkedTo(watcher, watchee);
			return proj.evaluate(linked);
		}

		return false;
	}
}
