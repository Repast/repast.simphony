package repast.simphony.space.projection;

import java.util.Collection;


public interface Projection<T> {

	String getName();

	/**
	 * Evaluate this Projection against the specified Predicate. This typically involves
	 * a double dispatch where the Projection calls back to the predicate, passing itself.
	 *
	 * @param predicate
	 * @return true if the predicate evaluates to true, otherwise false. False can also mean that the
	 * predicate is not applicable to this Projection. For example, a linked type predicate evaluated
	 * against a grid projection.
	 */
	boolean evaluate(ProjectionPredicate predicate);

	/**
	 * Adds listener for this projection.
	 *
	 * @param listener the listener to add.
	 */
	void addProjectionListener(ProjectionListener listener);

	/**
	 * Removes a listener from the this projection.
	 *
	 * @param listener the listener to remove
	 * @return true if the listener was succesfully removed, otherwise false
	 */
	boolean removeProjectionListener(ProjectionListener listener);

	/**
	 * Gets all the listeners for this projection.
	 *
	 * @return an iterable over all the listeners for this projection.
	 */
	Collection<ProjectionListener> getProjectionListeners();
}
