package repast.simphony.engine.watcher;

/**
 * WatchData created from the Watch annotation
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DefaultWatchData implements WatchData {

	private String id;

	public DefaultWatchData(String id) {
		this.id = id;
	}

	/**
	 * Gets the id string for the Watch this is associated with.
	 *
	 * @return the id string for the Watch this is associated with.
	 */
	public String getID() {
		return id;
	}
}
