package repast.simphony.query.space.projection;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionPredicate;

/**
 * Abstract implementation of Predicate interface. All the implemented
 * methods return false. Particular predicate implementations can
 * override one or more of these methods as appropriate. For example,
 * "within" only applies to networks, so the Within predicate would
 * override evaluate(Network).
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class SpatialPredicate implements ProjectionPredicate {
	
	/**
	 * Evaluates the projection against this predicate. This is the default for any
	 * Projections not specifically named below.
	 *
	 * @param projection the projection to evaluate against.
	 * @return false
	 */
	public boolean evaluate(Projection projection) {
		return false;
	}

	/**
	 * Evaluates the Network against this predicate.
	 *
	 * @param network the projection to evaluate against.
	 * @return false
	 */
	public boolean evaluate(Network network) {
		return false;
	}

	/**
	 * Evaluates the Grid against this predicate.
	 *
	 * @param grid the Grid to evaluate against.
	 * @return false
	 */
	public boolean evaluate(Grid grid) {
		return false;
	}

	/**
	 * Evaluates the ContinuousSpace against this predicate.
	 *
	 * @param space the continuous space to evaluate against.
	 * @return false
	 */
	public boolean evaluate(ContinuousSpace space) {
		return false;
	}
}