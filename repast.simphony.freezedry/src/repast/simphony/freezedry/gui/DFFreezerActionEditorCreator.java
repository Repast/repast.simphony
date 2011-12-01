package repast.simphony.freezedry.gui;

import repast.simphony.freezedry.wizard.DFFreezerControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class DFFreezerActionEditorCreator implements ActionEditorCreator<DFFreezerControllerAction> {

	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(DFFreezerControllerAction action) {
		return new DFFreezerActionUI(action);
	}

	public Class getActionType() {
		return DFFreezerControllerAction.class;
	}
}
