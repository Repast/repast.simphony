package repast.simphony.freezedry.gui;

import repast.simphony.freezedry.wizard.DFFreezerControllerAction;
import repast.simphony.freezedry.wizard.DelimitedFileOption;

/**
 * @author Nick Collier
 */
public class DFFreezerActionUI extends FreezerActionUI {

	public DFFreezerActionUI(DFFreezerControllerAction action) {
		super(action, new DelimitedFileOption(), "Delimited File Freeze Dryer");
	}
}
