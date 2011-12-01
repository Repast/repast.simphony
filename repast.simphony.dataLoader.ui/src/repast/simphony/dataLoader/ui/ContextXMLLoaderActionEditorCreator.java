package repast.simphony.dataLoader.ui;

import repast.simphony.dataLoader.engine.ContextXMLDataLoaderAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

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
		return null;
	}

	public Class<?> getActionType() {
		return ContextXMLDataLoaderAction.class;
	}
}