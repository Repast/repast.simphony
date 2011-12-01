package repast.simphony.plugin;


import repast.simphony.engine.environment.ControllerAction;

/**
 * Interface for classes that wish to create parent / composite controller actions. Parent controller actions
 * are one level below the root controller action.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public interface CompositeControllerActionCreator {

	/**
	 * Gets the unique id for this action. Using this id, the action may found
	 * using {@link repast.simphony.engine.controller.ControllerRegistry.findAction(Object, String)}.
	 *
	 * @return the id for this action
	 */
	String getID();

	/**
	 * Creates the composite controller action.
	 *
	 * @return the created ControllerAction.
	 */
	ControllerAction createControllerAction();
}
