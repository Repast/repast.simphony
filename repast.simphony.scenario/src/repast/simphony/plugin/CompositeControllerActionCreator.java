package repast.simphony.plugin;


import repast.simphony.engine.environment.ControllerAction;

/**
 * Interface for classes that wish to create parent / composite controller actions. Parent controller actions
 * are one level below the root controller action.
 * 
 * @author Nick Collier
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
	
	/**
	 * Return true if this action creator should only create a ControllerAction for the master context.
	 * 
	 * @return
	 */
	public boolean isMasterOnly();
}
