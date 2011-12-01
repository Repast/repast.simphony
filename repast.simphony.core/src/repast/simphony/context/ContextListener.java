/*
 * ContextListener.java
 *
 * Created on March 10, 2005, 8:59 AM
 */

package repast.simphony.context;

import java.io.Serializable;

/**
 * A listener that waits for changes to the Context.
 * 
 * @author Tom Howe
 * @version $revision$
 */
public interface ContextListener<T> {

	/**
	 * Called to nofify the listener of a change to a context.
	 * 
	 * @param ev
	 *            The event of which to notify the listener.
	 */
	public void eventOccured(ContextEvent<T> ev);
}
