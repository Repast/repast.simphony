package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.WithinMoore;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watchee is within the watcher's
 * Moore neighborhood in any grid projection contained by the
 * context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class WithinMooreBooleanExpression implements IBooleanExpression {

	private Context context;
	private double distance;

	public WithinMooreBooleanExpression(double distance, Context context) {
		this.context = context;
		this.distance = distance;
	}

	public String getExpression() {
		return "within_moore " + distance;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		WithinMoore within = new WithinMoore(watcher, watchee, distance);
		Iterable<Projection<?>> iter = context.getProjections();
		for (Projection<?> proj : iter) {
			if (proj.evaluate(within)) return true;
		}
		return false;
	}
}
