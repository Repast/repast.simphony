package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.LinkedFrom;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watcher is linked from the watchee in the named network in
 * the current Context. Note that for an undirected network, "linked from" is identical
 * to "linked."
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NamedLinkedFromBooleanExpression implements IBooleanExpression {

	private Context context;
	private String name;

	public NamedLinkedFromBooleanExpression(String name, Context context) {
		this.context = context;
		this.name = name;
	}

	public String getExpression() {
		return "linked from " + name;
	}

	/**
	 * Returns true if the watchee is a linked from  the watcher in the named projection.
	 *
	 * @param watcher
	 * @param watchee
	 * @param field
	 * @return true if the watchee is a precedecessor of the watcher.
	 */
	public boolean execute(Object watcher, Object watchee, Object field) {
		Projection proj = context.getProjection(name);
		if (proj != null) {
			LinkedFrom linked = new LinkedFrom(watcher, watchee);
			return proj.evaluate(linked);
		}

		return false;
	}
}
