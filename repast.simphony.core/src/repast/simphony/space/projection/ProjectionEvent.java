package repast.simphony.space.projection;

/**
 * A semantic event indicating some sort of event occured in a projection. The
 * type of the event is captured via the type property.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProjectionEvent<T> {

	public enum Type {
		OBJECT_ADDED, OBJECT_REMOVED, OBJECT_ROTATED, OBJECT_MOVED, EDGE_ADDED, EDGE_REMOVED
	};

	public static Type OBJECT_ADDED = Type.OBJECT_ADDED;
	public static Type OBJECT_REMOVED = Type.OBJECT_REMOVED;
	public static Type OBJECT_MOVED = Type.OBJECT_MOVED;
	public static Type OBJECT_ROTATED = Type.OBJECT_ROTATED;
	public static Type EDGE_ADDED = Type.EDGE_ADDED;
	public static Type EDGE_REMOVED = Type.EDGE_REMOVED;

	private Type type;
	private Projection<T> projection;
	private Object subject;

	/**
	 * Creates a ProjectionEvent for the specified projection, specified object
	 * and of the specified type.
	 * 
	 * @param projection
	 * @param subject
	 * @param type
	 */
	public ProjectionEvent(Projection<T> projection, Object subject, Type type) {
		this.projection = projection;
		this.subject = subject;
		this.type = type;
	}

	/**
	 * Gets the projection associated with this event.
	 * 
	 * @return the projection associated with this event.
	 */
	public Projection<T> getProjection() {
		return projection;
	}

	/**
	 * Gets the type of this event.
	 * 
	 * @return the type of this event.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the subject of this event. This is the object that was added,
	 * removed, and so forth.
	 * 
	 * @return the subject of this event.
	 */
	public Object getSubject() {
		return subject;
	}
}
