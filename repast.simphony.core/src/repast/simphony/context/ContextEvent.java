/*CopyrightHere*/
package repast.simphony.context;

import repast.simphony.space.projection.Projection;

/**
 * Represents a change occuring to a Context. This includes when objects are
 * added to and removed from a Context (with the {@link #getTarget()} being valid) or when
 * projections are added and removed (with the {@link #getProjection()} being valid).
 * 
 * @author Tom Howe
 */
public class ContextEvent<T> {
	/**
	 * Enum of the types of events.
	 */
	public static enum EventType {
		AGENT_ADDED, AGENT_REMOVED, AGENT_MOVED, PROJECTION_ADDED, PROJECTION_REMOVED,
		SUBCONTEXT_ADDED, SUBCONTEXT_REMOVED
	};

	/**
	 * Represents an event where an object is added to the context.
	 */
	public static EventType ADDED = EventType.AGENT_ADDED;

	public static EventType MOVED = EventType.AGENT_MOVED;

	/**
	 * Represents an event where an object is removed from the context.
	 */
	public static EventType REMOVED = EventType.AGENT_REMOVED;

	private EventType type;

	private Context<T> context;

	private T target;
	private Projection<? super T> projection;
	private Context<? extends T> subContext;
	
	/**
	 * Creates a new instance of ContextEvent with a given type, context and
	 * target.
	 * 
	 * @param type
	 *            The type of event which occured.
	 * @param context
	 *            The context to which the event occured.
	 * @param target
	 *            The object which was the target of the event.
	 */
	public ContextEvent(EventType type, Context<T> context, T target) {
		this.type = (type);
		this.context = (context);
		this.target = (target);
	}
	
	/**
	 * Creates a new instance of ContextEvent with a given type, context and
	 * Projection.
	 * 
	 * @param type
	 *            The type of event which occured.
	 * @param context
	 *            The context to which the event occured.
	 * @param projection
	 *            The projection which was the target of the event.
	 */
	public ContextEvent(EventType type, Context<T> context, Projection<? super T> projection) {
		this.type = (type);
		this.context = (context);
		this.projection = projection;
	}

	/**
		 * Creates a new instance of ContextEvent with a given type, context and
		 * sub-Context.
		 *
		 * @param type
		 *            The type of event which occured.
		 * @param context
		 *            The context to which the event occured.
		 * @param subContext
		 *            The subcontext which was the target of the event.
		 */
		public ContextEvent(EventType type, Context<T> context, Context<? extends T> subContext) {
			this.type = (type);
			this.context = (context);
			this.subContext = subContext;
		}


	/**
	 * Get the type of event this represents.
	 * 
	 * @return The type of event.
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Get the context to which the event occured.
	 * 
	 * @return The effected Context.
	 */
	public Context<T> getContext() {
		return context;
	}

	/**
	 * Get the target of the event.
	 * 
	 * @return The object which was the target of the event.
	 */
	public T getTarget() {
		return target;
	}
	
	/**
	 * Gets the project that was the target of the event.
	 * 
	 * @return a projection that was added or removed
	 */
	public Projection<? super T> getProjection() {
		return projection;
	}

	/**
	 * Gets the subcontext that was the target of the event, assuming
	 * this was a subcontext event.
	 *
	 * @return the subcontext that was the target of the event, assuming
	 * this was a subcontext event.
	 */
	public Context<? extends T> getSubContext() {
		return subContext;
	}
}
