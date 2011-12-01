package repast.simphony.engine.watcher;

/**
 * Optional arg passed to a watcher's method containing details of the Watch that triggered
 * the method.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */

public interface WatchData {

	/**
	 * Gets the id string for the Watch this is associated with.
	 *
	 * @return the id string for the Watch this is associated with.
	 */
	String getID();
}
