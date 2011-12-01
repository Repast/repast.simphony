package repast.simphony.visualization;


/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface LayoutUpdater {

	/**
	 * Conditions that determine whether not the updater should update.
	 */
	public enum Condition {
		ADDED, REMOVED, MOVED
	}

	/**
	 * Gets the layout whose updates this updater manages.
	 * 
	 * @return the layout whose updates this updater manages.
	 */
	Layout getLayout();

	/**
	 * Sets the layout whose updates this updater manages.
	 * 
	 * @param layout
	 */
	void setLayout(Layout layout);

	/**
	 * Adds a trigger condition to the current set. This layout updater will
	 * update or not in response to whatever its current condition is.
	 * 
	 * @param condition
	 */
	void addTriggerCondition(LayoutUpdater.Condition condition);

	/**
	 * 
	 * @return true if the visual items need to have the location updated during
	 *         this update cycle, otherwise false.
	 */
	boolean getUpdateItemsLocation();

	/**
	 * @return true if new items should get their location set when they are
	 *         added to the display, otherwise false.
	 */
	boolean getDoSetLocationForAdded();

	/**
	 * Update the layout. The layout will be updated or not depedending on the
	 * current trigger condition.
	 */
	void update();
}
