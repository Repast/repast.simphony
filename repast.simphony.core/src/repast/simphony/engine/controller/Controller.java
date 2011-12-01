/*CopyrightHere*/
package repast.simphony.engine.controller;

import java.io.Serializable;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.environment.Runner;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.parameter.Parameters;

/**
 * Interface representing the Controller for the simulation. This will handle the setup, execution,
 * and teardown of the model. This contains a set of
 * {@link repast.simphony.engine.environment.ControllerAction}s that perform the actual work, allowing this
 * to perform primarily as a container and orderer of the actions.
 * 
 * @author Jerry Vos
 */
public interface Controller extends IAction {

	/**
	 * Sets the registry of {@link ControllerAction}s that the controller will run.
	 * 
	 * @param controllerRegistry
	 *            a registry used to find actions to run
	 */
	void setControllerRegistry(ControllerRegistry controllerRegistry);

	/**
	 * Retrieves the registry of {@link ControllerAction}s that the controller will run.
	 * 
	 * @return a registry used to find actions to run
	 */
	ControllerRegistry getControllerRegistry();

	/**
	 * Retrieves the current RunState the controller is using.
	 * 
	 * @return the RunState the controller is currently using
	 */
	RunState getCurrentRunState();

	/**
	 * Sets the ScheduleRunner that will be used to execute the model's schedule and found through
	 * the RunState. This is used in the execute() phase of the controller.
	 * 
	 * @param scheduleRunner
	 *            the ScheduleRunner that will execute the model's schedule
	 */
	void setScheduleRunner(Runner scheduleRunner);

	/**
	 * Initializes the action for a batch run (a set of runs).
	 */
	void batchInitialize();

	/**
	 * Initializes the parameters for the current run.
	 * 
	 * @param params
	 *            an optional parameters object that will have the parameters loaded into it
	 * 
	 * @return the parameters object that the parameters were loaded into
	 */
	Parameters runParameterSetters(Parameters params);

	/**
	 * Initializes the action for a single run (possibly one of many).
	 */
	RunState runInitialize(Parameters params);

	/**
	 * Cleans up the action after a run just occurred.
	 */
	void runCleanup();

	/**
	 * Cleans up the action after a batch run (a set of runs).
	 */
	void batchCleanup();

	/**
	 * Gets the ScheduleRunner that will be used to execute the model's schedule found through the
	 * RunState. This is used in the execute() phase of the controller.
	 * 
	 * @return the ScheduleRunner that will be used to execute the model's schedule.
	 */
	Runner getScheduleRunner();

	/**
	 * Gets the next run number. The next run number is the number assigned to the next run when
	 * runInitialize is called.
	 * 
	 * @return the next run number. The next run number is the number assigned to the next run when
	 *         runInitialize is called.
	 */
	int getNextRunNumber();

	/**
	 * Gets the next batch number. The next batch number is the number assigned to the next batch
	 * when batchInitialize is called.
	 * 
	 * @return the next batch number. The next batch number is the number assigned to the next batch
	 *         when batchInitialize is called.
	 */
	int getNextBatchNumber();
	
	public void setup();
}
