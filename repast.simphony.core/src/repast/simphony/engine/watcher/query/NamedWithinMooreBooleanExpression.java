package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.WithinMoore;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watchee is within the watcher's
 * Moore neighborhood in any grid projection in the
 * context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NamedWithinMooreBooleanExpression implements IBooleanExpression {

	private Context context;
	private double distance;
	private String name;

	public NamedWithinMooreBooleanExpression(double distance, String name, Context context) {
		this.context = context;
		this.distance = distance;
		this.name = name;
	}

	public String getExpression() {
		return "within_moore " + distance + " " + name;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		WithinMoore within = new WithinMoore(watcher, watchee, distance);
		Projection proj = context.getProjection(name);
		if (proj != null) return proj.evaluate(within);
		return false;
	}
}
