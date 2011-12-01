/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.engine.schedule.DefaultScheduleFactory;
import repast.simphony.engine.schedule.IScheduleFactory;
import repast.simphony.parameter.EmptyParameters;
import repast.simphony.parameter.Parameters;

/**
 * Builder for creating a RunEnvironment object. The user can customize the RunEnv used
 * for each run using this object.
 *
 * @author Nick Collier
 */
public class DefaultRunEnvironmentBuilder implements RunEnvironmentBuilder {

	private IScheduleFactory scheduleFactory = new DefaultScheduleFactory();
	private Runner runner;
	private boolean isBatch;
	private Parameters parameters = new EmptyParameters();

	/**
	 * Creates a RunEnvironmentBuilder that will use the specified schedule runner to run
	 * each run of the simulation.
	 *
	 * @param runner
	 * @param isBatch whether or not this is operating in batch mode.
	 */
	public DefaultRunEnvironmentBuilder(Runner runner, boolean isBatch) {
		this.runner = runner;
		this.isBatch = isBatch;
	}


	/**
	 * Gets the schedule runner used to run each run of the simulation.
	 *
	 * @return the schedule runner used to run each run of the simulation.
	 */
	public Runner getScheduleRunner() {
		return runner;
	}

	/**
	 * Gets the schedule factory used to create the schedule for simulation runs.
	 *
	 * @return the schedule factory used to create the schedule for simulation runs.
	 */
	public IScheduleFactory getScheduleFactory() {
		return scheduleFactory;
	}

	/**
	 * Sets the schedule factory used to create the schedule for simulation runs.
	 *
	 * @param scheduleFactory the new schedule factory
	 */
	public void setScheduleFactory(IScheduleFactory scheduleFactory) {
		this.scheduleFactory = scheduleFactory;
	}

	/**
	 * Gets the parameters to be used in the next simulation run.
	 *
	 * @return the parameters to be used in the next simulation run.
	 */
	public Parameters getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters to be used in the next simulation run.
	 *
	 * @param parameters the parameters to be used in the next simulation run.
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * Creates the Runtime to be used for the next simulation run. Each
	 * simulation run gets a new runtime.
	 *
	 * @return the Runtime to be used for the next simulation run.
	 */
	public RunEnvironment createRunEnvironment() {
		RunEnvironment.init(scheduleFactory.createSchedule(), runner, parameters, isBatch);
		return RunEnvironment.getInstance();
	}
}
