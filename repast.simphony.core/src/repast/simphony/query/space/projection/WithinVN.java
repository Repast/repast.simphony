package repast.simphony.query.space.projection;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.VNContains;

import java.util.Arrays;

/**
 * Predicate that evalutes whether one object is within the Von Neumann
 * neighborhood of another in a grid projection.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class WithinVN extends SpatialPredicate {

	private Object obj1, obj2;
	private double distance;

	/**
	 * Creates a Within predicate that will evaluate whether or not the second object is
	 * a member of the Von Neumann neighborhood of the first object.
	 *
	 * @param obj1
	 * @param obj2
	 */
	public WithinVN(Object obj1, Object obj2, double distance) {
		this.obj1 = obj1;
		this.obj2 = obj2;
		this.distance = distance;
	}

	/**
	 * Evaluates the Grid against this predicate.
	 *
	 * @param grid the Grid to evaluate against.
	 * @return false
	 */
	@Override
	public boolean evaluate(Grid grid) {
		VNContains<Object> contains = new VNContains<Object>(grid);
		int[] extent = new int[grid.getDimensions().size()];
		
		for (int i=0; i<extent.length; i++)
			extent[i] = (int)distance;

		return contains.isNeighbor(obj1, obj2, extent);
	}
}
