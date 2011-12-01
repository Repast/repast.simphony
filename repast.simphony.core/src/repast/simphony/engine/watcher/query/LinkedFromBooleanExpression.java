package repast.simphony.engine.watcher.query;


import repast.simphony.context.Context;
import repast.simphony.query.space.projection.LinkedFrom;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watcher is linked from the watchee any network in
 * the current Context. Note that for an undirected network, "linked from" is identical
 * to "linked."
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class LinkedFromBooleanExpression implements IBooleanExpression {

	private Context context;

	public LinkedFromBooleanExpression(Context context) {
		this.context = context;
	}

	public String getExpression() {
		return "linked from";
	}

	/**
	 * Returns true if the watchee is linked from the watcher.
	 *
	 * @param watcher
	 * @param watchee
	 * @param field
	 * @return true if the watchee is a precedecessor of the watcher.
	 */
	public boolean execute(Object watcher, Object watchee, Object field) {
		LinkedFrom linkedFrom = new LinkedFrom(watcher, watchee);
		Iterable<Projection<?>> iter = context.getProjections();
		for (Projection<?> proj : iter) {
			if (proj.evaluate(linkedFrom)) return true;
		}

		return false;
	}
}
