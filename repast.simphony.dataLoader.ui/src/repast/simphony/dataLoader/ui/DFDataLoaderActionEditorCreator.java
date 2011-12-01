package repast.simphony.dataLoader.ui;

import repast.simphony.dataLoader.engine.DFDataLoaderControllerAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class DFDataLoaderActionEditorCreator implements ActionEditorCreator<DFDataLoaderControllerAction> {

	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(DFDataLoaderControllerAction action) {
		return new DFDataLoaderActionUI(action);
	}

	public Class getActionType() {
		return DFDataLoaderControllerAction.class;
	}
}
