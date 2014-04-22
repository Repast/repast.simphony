package repast.simphony.dataLoader.ui;

import repast.simphony.dataLoader.engine.MIContextXMLDataLoaderAction;
import repast.simphony.ui.DefaultActionUI;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class MIContextXMLLoaderActionEditorCreator implements ActionEditorCreator<MIContextXMLDataLoaderAction> {

	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(MIContextXMLDataLoaderAction action) {
		return new DefaultActionUI(action.toString());
	}

	public Class<?> getActionType() {
		return MIContextXMLDataLoaderAction.class;
	}
}