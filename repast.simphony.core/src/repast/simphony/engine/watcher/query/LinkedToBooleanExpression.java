package repast.simphony.engine.watcher.query;


import repast.simphony.context.Context;
import repast.simphony.query.space.projection.LinkedTo;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watcher is linked to the watchee any network in
 * the current Context. Note that for an undirected network, "linked to" is identical
 * to "linked."
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class LinkedToBooleanExpression implements IBooleanExpression {

	private Context context;

	public LinkedToBooleanExpression(Context context) {
		this.context = context;
	}

	public String getExpression() {
		return "linked to";
	}

	/**
	 * Returns true if the watcher is linked to the watchee.
	 *
	 * @param watcher
	 * @param watchee
	 * @param field
	 * @return true if the watcher is a precedecessor of the watchee.
	 */
	public boolean execute(Object watcher, Object watchee, Object field) {
		LinkedTo linkedTo = new LinkedTo(watcher, watchee);
		Iterable<Projection<?>> iter = context.getProjections();
		for (Projection<?> proj : iter) {
			if (proj.evaluate(linkedTo)) return true;
		}

		return false;
	}
}
