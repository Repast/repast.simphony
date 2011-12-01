package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.Within;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the length of the path from the watcher to the watchee is within (<=)
 * some value in any network in the current Context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class WithinBooleanExpression implements IBooleanExpression {

	private Context context;
	private double distance;

	public WithinBooleanExpression(double distance, Context context) {
		this.context = context;
		this.distance = distance;
	}

	public String getExpression() {
		return "within " + distance;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		Within within = new Within(watcher, watchee, distance);
		Iterable<Projection<?>> iter = context.getProjections();
		for (Projection<?> proj : iter) {
			if (proj.evaluate(within)) return true;
		}
		return false;
	}
}
