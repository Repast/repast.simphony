package repast.simphony.relogo;

import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.scenario.ModelInitializer;
import repast.simphony.scenario.Scenario;
import simphony.util.messages.MessageCenter;

public class ReLogoInitializer implements ModelInitializer {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(ReLogoInitializer.class);

	public void initialize(Scenario scen, RunEnvironmentBuilder builder) {
		builder.getScheduleRunner().addRunListener(ReLogoModel.getInstance());
		ReLogoModel.getInstance().checkEMCEnabledGlobally();
		// adding the "of" method to the Closure class
		UtilityG.dressClosure();

	}

}
