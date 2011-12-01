package repast.simphony.data.gui;

import repast.simphony.data.engine.DefaultDataGathererDescriptorAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DataSetActionEditorCreator implements ActionEditorCreator<DefaultDataGathererDescriptorAction> {

	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(DefaultDataGathererDescriptorAction action) {
		return new DataGathererDescriptorActionUI(action);
	}

	public Class getActionType() {
		return DefaultDataGathererDescriptorAction.class;
	}
}
