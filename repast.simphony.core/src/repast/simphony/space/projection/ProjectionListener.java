package repast.simphony.space.projection;

/**
 * Inteface for classes that wish to listen for ProjectionEvent-s.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ProjectionListener<T> {

	/**
	 * Invoked when a projection event occurs.
	 * 
	 * @param evt
	 *            the object describing the event
	 */
	void projectionEventOccurred(ProjectionEvent<T> evt);
}
