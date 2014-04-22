package repast.simphony.dataLoader.ui;

import repast.simphony.dataLoader.engine.ContextXMLDataLoaderAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.DefaultActionUI;

/**
 * @author Nick Collier
 */
public class ContextXMLLoaderActionEditorCreator implements ActionEditorCreator<ContextXMLDataLoaderAction> {

	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	public ActionUI createEditor(ContextXMLDataLoaderAction action) {
		return new DefaultActionUI(action.toString());
	}

	public Class<?> getActionType() {
		return ContextXMLDataLoaderAction.class;
	}
}