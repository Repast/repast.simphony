/*CopyRightHere*/
package repast.simphony.engine.environment;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;



/**
 * A registry containing schedule related objects. This is used by RunState and therefore can be
 * used by Controller actions to retrieve schedules and store actions that are to occur.
 * 
 * @see repast.simphony.engine.environment.RunState
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface ScheduleRegistry {
	// TODO: think about the objects in this, the controller related ones are
	// really only related to the current way the controller works, and not to
	// the Controller interface.
	// /**
	// * Retrieves the schedule that is used by the Controller to run itself.<p/>
	// *
	// * This is used internally by Repast and most users should not use this
	// * method.
	// *
	// * @see #getModelSchedule()
	// *
	// * @return the controller's schedule
	// */
	// ISchedule getControllerSchedule();
	//
	// /**
	// * Sets the schedule that is used by the Controller to run itself.<p/>
	// *
	// * Note: this method should not be used while the Controller is running or
	// * in all likelihood the Controller will fail.<p/>
	// *
	// * This is used internally by Repast and most users should not use this
	// * method.
	// *
	// * @see #getModelSchedule()
	// *
	// * @param controllerSchedule
	// * the controller's schedule
	// */
	// void setControllerSchedule(ISchedule controllerSchedule);

	/**
	 * Retrieves the schedule that models should use to schedule their actions.
	 * 
	 * @return the schedule to be used used by models
	 */
	ISchedule getModelSchedule();

	/**
	 * Sets the schedule users should use to schedule their actions. Users should not use this
	 * method as it is intended for internal Repast use. If a user would like a custom schedule to
	 * be used they should specify a ScheduleFactory to the Controller.
	 * 
	 * @param modelSchedule
	 *            the schedule used by models
	 */
	void setModelSchedule(ISchedule modelSchedule);

	// /**
	// * Retrieves the graph representing the actions the controller will perform.
	// *
	// * @return the graph representing the actions the controller will perform
	// */
	// NetworkTopology getControllerGraph();
	//
	// /**
	// * Ses the graph representing the actions the controller will perform.<p/>
	// *
	// * This is used internally by Repast and most users should not use this
	// * method.
	// *
	// * @param controllerGraph
	// * the graph representing the actions the controller will perform
	// */
	// void setControllerGraph(NetworkTopology controllerGraph);
	//
	/**
	 * Retrieves the actions that are to occur before a run of the model schedule.
	 * 
	 * @return the actions that are to occur before a run of the model schedule
	 */
	Iterable<IAction> getPreRunActions();

	/**
	 * Adds an action that should occur before a run of the model schedule.
	 * 
	 * @param action
	 *            an action that should occur before a run of the model schedule
	 */
	void addPreRunAction(IAction action);

	/**
	 * Removes an action from the actions that should occur before a run of the model schedule.
	 * 
	 * @param action
	 *            an action that should occur before a run of the model schedule
	 */
	void removePreRunAction(IAction action);

	/**
	 * Retrieves the actions that are to occur after a run of the model schedule.
	 * 
	 * @return the actions that are to occur after a run of the model schedule
	 */
	Iterable<IAction> getPostRunActions();

	/**
	 * Adds an action that should occur after a run of the model schedule.
	 * 
	 * @param action
	 *            an action that should occur after a run of the model schedule
	 */
	void addPostRunAction(IAction action);

	/**
	 * Removes an action from the actions that should occur after a run of the model schedule.
	 * 
	 * @param action
	 *            an action that should occur after a run of the model schedule
	 */
	void removePostRunAction(IAction action);

	/**
	 * Sets the runner used for executing the schedule.
	 * 
	 * @param runner
	 *            the schedule runner
	 */
	void setScheduleRunner(Runner runner);

	/**
	 * Retrieves the runner used for executing the schedule.
	 * 
	 * @param runner
	 *            the schedule runner
	 */
	Runner getScheduleRunner();

	// RunManager getControllerRunManager();
	// void setControllerRunManager(RunManager runManager);
}