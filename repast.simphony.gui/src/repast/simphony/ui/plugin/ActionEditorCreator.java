package repast.simphony.ui.plugin;

import repast.simphony.engine.environment.ControllerAction;

/**
 * Interface for classes that create action editors.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public interface ActionEditorCreator<T extends ControllerAction> {

	/**
	 * Creates an editor for the specfied action.
	 *
	 * @param action the action to create the editor for
	 * @return an editor for the specified action.
	 */
	ActionUI createEditor(T action);

	/**
	 * Gets the Class of the ControllerAction for which this creates an editor.
	 *
	 * @return the Class of the ControllerAction for which this creates an editor.
	 */
	Class getActionType();
}
