package repast.simphony.relogo;

import java.util.Collection;

import javax.swing.JComponent;

import repast.simphony.context.Context;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.controller.NullAbstractControllerAction;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.GUIRegistry;
import repast.simphony.engine.environment.GUIRegistryType;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.ModelInitializer;
import repast.simphony.scenario.Scenario;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.IDisplay;
import simphony.util.messages.MessageCenter;

public class ReLogoInitializer implements ModelInitializer {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(ReLogoInitializer.class);
	
	private static final String DEFAULT_RELOGO_DISPLAY_NAME = "ReLogo: ReLogo Default Display";
	private static final String DEFAULT_RELOGO_CONTEXT_NAME = "default_observer_context";

	private  IDisplay getDefaultDisplay(GUIRegistry guiRegistry) {
		
		Collection<Pair<GUIRegistryType, Collection<JComponent>>> typesAndComponents = guiRegistry
				.getTypesAndComponents();
		
		for (Pair<GUIRegistryType, Collection<JComponent>> p : typesAndComponents) {

			if (p.getFirst().equals(GUIRegistryType.DISPLAY)) {

				for (JComponent j : p.getSecond()) {

					String componentName = guiRegistry.getName(j);

					if (componentName != null) {
						if (guiRegistry.getName(j).equals(
								DEFAULT_RELOGO_DISPLAY_NAME)) {
							return guiRegistry
									.getDisplayForComponent(j);
						}
					}
				}
			}
		}
		
	return null;
}
	public void initialize(Scenario scen, RunEnvironmentBuilder builder) {
		builder.getScheduleRunner().addRunListener(ReLogoModel.getInstance());
//		ReLogoModel.getInstance().checkEMCEnabledGlobally();
		// adding the "of" method to the Closure class
//		UtilityG.dressClosure();
		
		ControllerRegistry registry = scen.getControllerRegistry();
		ControllerAction parent = registry.findAction(DEFAULT_RELOGO_CONTEXT_NAME, ControllerActionConstants.VIZ_ROOT);
		registry.addAction(DEFAULT_RELOGO_CONTEXT_NAME, parent, new NullAbstractControllerAction() {

		public void runInitialize(RunState runState, Context context,
				Parameters runParams) {
			
			if (!RunEnvironment.getInstance().isBatch()){
				GUIRegistry guiRegistry = runState.getGUIRegistry();
				IDisplay defDisplay = getDefaultDisplay(guiRegistry);
				ReLogoModel.getInstance().setDefaultDisplay(defDisplay);
			}
		}
		
		public void runCleanup(RunState runState, Context context) {
			ReLogoModel.getInstance().setDefaultDisplay(null);
		}





			public String toString() {
				return "Create a handle to default display.";
			}
		});
	}

}
