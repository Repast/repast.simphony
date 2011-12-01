package repast.simphony.context;

import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.util.collections.RandomIterable;

/**
 * An iterable that iterates over a collection of objects at random.
 * The "length" of the iterable may be less than the number of objects
 * in the collection.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ContextIterable<T> extends RandomIterable<T> implements ContextListener<T> {

	private Context<T> context;


	/**
	 * Creates a RandomIterable that will iterate over count number of objects in the
	 * specified IndexedIterable.
	 *
	 * @param iter
	 * @param count
	 * @param context the context associated with this iterable. This will listen
	 * on the context and respond appropriately to remove events
	 *
	 */
	public ContextIterable(IndexedIterable<T> iter, long count, Context<T> context) {
		super(iter, count);

		if (context != null) {
			this.context = context;
			context.addContextListener(this);
		}
	}
	
	/**
	 * Returns the next element in the iteration.  Calling this method
	 * repeatedly until the {@link #hasNext()} method returns false will
	 * return each element in the underlying collection exactly once.
	 *
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	public T next() {	
		if ((numToReturn - numReturned) == 1 && context != null)
			context.removeContextListener(this);
		
		return super.next();
	}

	/**
	 * Notify this event of a change to a context.
	 *
	 * @param ev The event of which to notify the listener.
	 */
	public void eventOccured(ContextEvent<T> ev) {
		if (ev.getType() == ContextEvent.EventType.AGENT_REMOVED) 
			removeEvent(ev.getTarget());
	}
}