package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;
import repast.simphony.query.space.projection.Within;
import repast.simphony.space.projection.Projection;

/**
 * Returns true if the length of the path from the watcher to the watchee is within (<=)
 * some value in the named network in the current Context.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NamedWithinBooleanExpression implements IBooleanExpression {

	private Context context;
	private double distance;
	private String name;


	public NamedWithinBooleanExpression(double distance, String name, Context context) {
		this.context = context;
		this.distance = distance;
		this.name = name;
	}

	public String getExpression() {
		return "within " + distance + " '" + name + "'";
	}

	public boolean execute(Object watcher, Object watchee, Object field) {
		// todo reuse a created shortest path if the network(s) do not change
		Within within = new Within(watcher, watchee, distance);
		Projection proj = context.getProjection(name);
		if (proj != null) {
			return proj.evaluate(within);
		}
		return false;
	}
}
