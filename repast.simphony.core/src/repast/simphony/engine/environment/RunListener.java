package repast.simphony.engine.environment;

/**
 * Interface for those classes that want listen for Run events: started, stopped and so on.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface RunListener {

	/**
	 * Invoked when the current run has been stopped.
	 *
	 */
	void stopped();

	/**
	 * Invoked when the current run has been paused.
	 */
	void paused();

	/**
	 * Invoked when the current run has been started.
	 */
	void started();

	/**
	 * Invoked when the current run has been restarted after a pause.
	 */
	void restarted();
}
