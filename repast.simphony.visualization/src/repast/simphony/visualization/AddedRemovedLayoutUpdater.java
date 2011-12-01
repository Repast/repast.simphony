package repast.simphony.visualization;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Updates the layout whenever an object has been added or removed, but NOT when they have been
 * moved.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class AddedRemovedLayoutUpdater extends AbstractLayoutUpdater {

	private boolean updateItems = false;
	private Lock lock = new ReentrantLock();

	/**
	 * Creates an AddedRemovedLayoutUpdater that manages the updates on the specified layout.
	 *
	 * @param layout
	 */
	public AddedRemovedLayoutUpdater(Layout layout) {
		super(layout);
	}

	/**
	 * @return true if the trigger condition is removed or added, otherwise false. 
	 */
	public boolean getUpdateItemsLocation() {
		return updateItems;
	}

	/**
	 * @return true if new items should get their location set when they are added to the display, otherwise false.
	 */
	public boolean getDoSetLocationForAdded() {
		return true;
	}

	/**
	 * Updates the layout if the trigger condition is removed or added.
	 */
	public void update() {
		try {
			lock.lock();
			if (hasCondition(Condition.REMOVED, Condition.ADDED)) {
				layout.update();
				updateItems = true;
			} else {
				updateItems = false;
			}
			conditions.clear();
		} finally {
			lock.unlock();
		}
	}
}
