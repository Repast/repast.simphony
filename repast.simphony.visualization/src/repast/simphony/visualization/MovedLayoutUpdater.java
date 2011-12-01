package repast.simphony.visualization;


/**
 * Updates the layout whenever an object has been added, removed, or moved.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class MovedLayoutUpdater extends AbstractLayoutUpdater {

	private boolean updateItems = false;

	/**
	 * Creates a MovedLayoutUpdater that manages the updates on the specified layout.
	 *
	 * @param layout
	 */
	public MovedLayoutUpdater(Layout layout) {
		super(layout);
	}

	/**
	 * @return true if the trigger condition is added, removed, moved, otherwise false.
	 */
	public boolean getUpdateItemsLocation() {
		return updateItems;
	}

	/**
	 * @return true if new items should get their location set when they are added to the display, otherwise false.
	 */
	public boolean getDoSetLocationForAdded() {
		return false;
	}

	/**
	 * Updates the layout if the trigger condition is removed, added, or moved.
	 */
	public void update() {
		if (hasCondition(Condition.REMOVED, Condition.ADDED, Condition.MOVED)) {
			layout.update();
			updateItems = true;
		} else {
			updateItems = false;
		}

		conditions.clear();
	}
}
