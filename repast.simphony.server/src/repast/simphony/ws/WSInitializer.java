package repast.simphony.ws;

import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.scenario.ModelInitializer;
import repast.simphony.scenario.Scenario;
import repast.simphony.ws.ChartServerControllerAction;
import repast.simphony.ws.DisplayServerControllerAction;

public class WSInitializer implements ModelInitializer {


	/**
	 * This is ran after the model has been loaded. This is only ran once, but the
	 * settings set through the {@link repast.simphony..scenario.Scenario} will
	 * apply to every run of the simulation.
	 * 
	 * @param scenario the {@link repast.simphony..scenario.Scenario} object that
	 *                 hold settings for the run
	 */
	public void initialize(Scenario scenario, RunEnvironmentBuilder builder) {

		// System.out.println("WSInitializer.initialize()");

		ChartServerControllerAction chartAction = new ChartServerControllerAction(scenario.getScenarioDirectory().toPath());
		DisplayServerControllerAction displayAction = new DisplayServerControllerAction(scenario.getScenarioDirectory().toPath());
		scenario.addMasterControllerAction(chartAction);
		scenario.addMasterControllerAction(displayAction);
	}
}
