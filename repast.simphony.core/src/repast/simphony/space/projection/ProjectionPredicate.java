/**
 * 
 */
package repast.simphony.space.projection;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;


/**
 * Interface for predicates that are evaluated against projections. Primiarly used by
 * the watcher query mechanism.
 * 
 * @author milesparker
 */
public interface ProjectionPredicate {

	/**
	 * Evaluates the projection against this predicate. This is the default for any
	 * Projections not specifically named below.
	 *
	 * @param projection the projection to evaluate against.
	 * @return true if this predicate is true for the specified projection otherwise false.
	 */
	boolean evaluate(Projection projection);

	/**
	 * Evaluates the Network against this predicate.
	 *
	 * @param network the projection to evaluate against.
	 * @return false
	 */
	boolean evaluate(Network network);

	/**
	 * Evaluates the Grid against this predicate.
	 *
	 * @param grid the Grid to evaluate against.
	 * @return false
	 */
	boolean evaluate(Grid grid);

	/**
	 * Evaluates the ContinuousSpace against this predicate.
	 *
	 * @param space the continuous space to evaluate against.
	 * @return false
	 */
	boolean evaluate(ContinuousSpace space);

	/**
	 * Evaluates the Geography against this predicate.
	 *
	 * @param geography the geography to evaluate against.
	 * @return true if this predicate is true for the specified projection otherwise false.
	 */
	boolean evaluate(Geography geography);
}