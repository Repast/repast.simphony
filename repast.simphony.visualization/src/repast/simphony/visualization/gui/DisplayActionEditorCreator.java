package repast.simphony.visualization.gui;

import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;

/**
 * Creates a DisplayActionUI for editing display component actions.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DisplayActionEditorCreator implements ActionEditorCreator<DisplayComponentControllerAction> {

	public Class getActionType() {
		return DisplayComponentControllerAction.class;
	}

	public ActionUI createEditor(DisplayComponentControllerAction action) {
		return new DisplayActionUI(action);
	}
}
