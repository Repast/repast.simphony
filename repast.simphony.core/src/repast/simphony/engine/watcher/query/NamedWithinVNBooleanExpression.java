package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.WithinVN;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the watchee is within the watcher's
 * Von Neumann neighborhood in any grid projection in the
 * context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NamedWithinVNBooleanExpression implements IBooleanExpression {

	private Context context;
	private double distance;
	private String name;

	public NamedWithinVNBooleanExpression(double distance, String name, Context context) {
		this.context = context;
		this.distance = distance;
		this.name = name;
	}

	public String getExpression() {
		return "within_vn " + distance  + " " + name;
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		WithinVN within = new WithinVN(watcher, watchee, distance);
		Projection proj = context.getProjection(name);
		if (proj != null) return proj.evaluate(within);
		return false;
	}
}
