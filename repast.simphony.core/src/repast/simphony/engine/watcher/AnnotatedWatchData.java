package repast.simphony.engine.watcher;

/**
 * WatchData created from the Watch annotation
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class AnnotatedWatchData implements WatchData {

	private Watch watch;

	public AnnotatedWatchData(Watch watch) {
		this.watch = watch;
	}

	/**
	 * Gets the id string for the Watch this is associated with.
	 *
	 * @return the id string for the Watch this is associated with.
	 */
	public String getID() {
		return watch.id();
	}
}
