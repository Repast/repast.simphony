package repast.simphony.visualization;


/**
 * Updates the layout every time the display is updated, regardless of the current condition.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class UpdateLayoutUpdater extends AbstractLayoutUpdater {


	/**
	 * Creates a UpdateLayoutUpdater to manage the updates of the specified layout.
	 *
	 * @param layout
	 */
	public UpdateLayoutUpdater(Layout layout) {
		super(layout);
	}

	/**
	 *
	 * @return true if the visual items need to have the location updated during this update cycle, otherwise
	 * false.
	 */
	public boolean getUpdateItemsLocation() {
    return true;
  }

	/**
	 * @return true if new items should get their location set when they are added to the display, otherwise false.
	 */
	public boolean getDoSetLocationForAdded() {
		return false;
	}

	/**
	 * Update the layout. The layout will be updated or not depedending on the current trigger condition.
	 */
  public void update() {
    layout.update();
  }
}



