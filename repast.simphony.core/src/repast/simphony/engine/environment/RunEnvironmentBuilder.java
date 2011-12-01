/*CopyrightHere*/
package repast.simphony.engine.environment;


import repast.simphony.engine.schedule.IScheduleFactory;
import repast.simphony.parameter.Parameters;

/**
 * Builder for creating a RunEnvironment object. The user can customize the RunEnv used
 * for each run using this object.
 *
 * @author Nick Collier
 */
public interface RunEnvironmentBuilder  {

	/**
	 * Gets the schedule runner used to run each run of the simulation.
	 *
	 * @return the schedule runner used to run each run of the simulation.
	 */
	Runner getScheduleRunner();

	/**
	 * Gets the schedule factory used to create the schedule for simulation runs.
	 *
	 * @return the schedule factory used to create the schedule for simulation runs.
	 */
	IScheduleFactory getScheduleFactory();

	/**
	 * Sets the schedule factory used to create the schedule for simulation runs.
	 *
	 * @param scheduleFactory the new schedule factory
	 */
	void setScheduleFactory(IScheduleFactory scheduleFactory);

	/**
	 * Gets the parameters to be used in the next simulation run.
	 *
	 * @return the parameters to be used in the next simulation run.
	 */
	Parameters getParameters();

	/**
	 * Sets the parameters to be used in the next simulation run.
	 *
	 * @param parameters the parameters to be used in the next simulation run.
	 */
	void setParameters(Parameters parameters);

	/**
	 * Creates the Runtime to be used for the next simulation run. Each
	 * simulation run gets a new runtime.
	 *
	 * @return the Runtime to be used for the next simulation run.
	 */
	RunEnvironment createRunEnvironment();
}