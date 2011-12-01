package repast.simphony.scenario;

import java.io.File;

/**
 * Provides  {@link ActionSaver} and {@link ActionLoader} classes for saving and loading
 * a ControllerAction.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public interface ControllerActionIO {

	/**
	 * Gets the Class of the ControllerAction that this ControllerActionIO can save and load.
	 *
	 * @return the Class of the ControllerAction that this ControllerActionIO can save and load.
	 */
	Class getActionClass();

	/**
	 * Gets the id for this ControllerActionIO to be used in loading and saving a ControllerAction. This id
	 * associates the saved action with this ControllerActionIO.
	 *
	 * @return the id for this ControllerActionIO to be used in loading and saving a ControllerAction
	 */
	String getSerializationID();

	/**
	 * Gets the ActionSaver used to save the ControllerAction.
	 *
	 * @return the ActionSaver used to save the ControllerAction.
	 */
	ActionSaver getActionSaver();

	/**
	 * Gets the ActionLoader used to load the saved action.
	 *
	 * @param actionFile the File containing the serialized data
	 * @param contextID the context the context to load the action into
	 * @return the ActionLoader used to load the saved action.
	 */
	ActionLoader getActionLoader(File actionFile, Object contextID);
}
