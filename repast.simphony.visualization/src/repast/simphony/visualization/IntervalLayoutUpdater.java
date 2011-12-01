package repast.simphony.visualization;


/**
 * A layout updater that will update the layout at some specified interval, or if
 * objects have been added, removed or moved. <code>updateItemsLocation()<code> will return true
 * if the layout has been updated.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class IntervalLayoutUpdater extends AbstractLayoutUpdater {

  private int interval = 0;
  private int counter = 0;
	private boolean updatePerformed = true;

	/**
	 * Creates an IntervalLayoutUpdater to manage the specified layout and update at the specified interval.
	 *
	 * @param layout
	 * @param interval
	 */
  public IntervalLayoutUpdater(Layout layout, int interval) {
    super(layout);
    this.interval = interval;
    updatePerformed = false;
  }

	/**
	 * @return true if new items should get their location set when they are added to the display, otherwise false.
	 */
	public boolean getDoSetLocationForAdded() {
		return false;
	}

	/**
	 * @return true if the visual items need to have the location updated during this update cycle, otherwise
	 *         false.
	 */
	public boolean getUpdateItemsLocation() {
		return updatePerformed;
	}

	/**
	 * Update the layout. The layout will be updated if the interval has been reached or if the
	 * an added, moved, or removed condition has been triggered.
	 */
	public void update() {
    counter++;
    if (hasCondition(Condition.ADDED , Condition.MOVED, Condition.REMOVED)) {
      layout.update();
      updatePerformed = true;
      if (counter == interval) counter = 0;
    } else {
      if (counter == interval) {
        layout.update();
        updatePerformed = true;
        counter = 0;
      } else {
        updatePerformed = false;
      }
    }
		conditions.clear();
  }
}
