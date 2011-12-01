package repast.simphony.space.projection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DefaultProjection<T> implements Projection<T> {

	protected String name;

	protected List<ProjectionListener> listeners = new ArrayList<ProjectionListener>();

	/**
	 * Creates a projection with the specified name.
	 * 
	 * @param name
	 *            the name of the projection
	 */
	public DefaultProjection(String name) {
		this.name = name;
	}

	/**
	 * Adds listener for this projection.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addProjectionListener(ProjectionListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener from the this projection.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @return true if the listener was succesfully removed, otherwise false
	 */
	public boolean removeProjectionListener(ProjectionListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Sends the specified evt to all the projection listeners.
	 * 
	 * @param evt
	 *            the evt to send
	 */
	protected void fireProjectionEvent(ProjectionEvent evt) {
		// OPTIMIZED: code this out for speed because this gets called a lot (potentially)
		int listenerSize = listeners.size();
		for (int i = 0; i < listenerSize; i++) {
			listeners.get(i).projectionEventOccurred(evt);
		}
	}

	/**
	 * Gets all the listeners for this projection.
	 * 
	 * @return an iterable over all the listeners for this projection.
	 */
	public Collection<ProjectionListener> getProjectionListeners() {
		return listeners;
	}

	/**
	 * Evaluate this Projection against the specified Predicate. This typically
	 * involves a double dispatch where the Projection calls back to the
	 * predicate, passing itself.
	 * 
	 * @param predicate
	 * @return true if the predicate evaluates to true, otherwise false. False
	 *         can also mean that the predicate is not applicable to this
	 *         Projection. For example, a linked type predicate evaluated
	 *         against a grid projection.
	 */
	public boolean evaluate(ProjectionPredicate predicate) {
		return predicate.evaluate(this);
	}

	/**
	 * Gets the name of this projection.
	 * 
	 * @return the name of this Projection.
	 */
	public String getName() {
		return name;
	}

}
