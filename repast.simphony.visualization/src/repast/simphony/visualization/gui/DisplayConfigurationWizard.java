package repast.simphony.visualization.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import repast.simphony.visualization.engine.VisualizationRegistry;
import repast.simphony.visualization.engine.VisualizationRegistryData;

/**
 * Wizard for building a display from scratch.  Wizard steps for the Cartesian
 *   displays are included here.  Additional display plugin steps are optionally
 *   loaded from the visualization registry.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class DisplayConfigurationWizard {

	protected Wizard wizard;
	protected DisplayWizardModel model;

	// contextID is not necessarily that of the rootContext, but rather the
	// context that display configuration is for.
	public DisplayConfigurationWizard(Object contextID, DisplayDescriptor descriptor,
			ContextData rootContext) {

		model = new DisplayWizardModel(contextID, descriptor, rootContext);
		model.setLastVisible(false);

		addPaths();

		wizard = new Wizard(model);
		wizard.addWizardListener(model);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		wizard.setOverviewVisible(false);
	}

	public Wizard getWizard() {
		return wizard;
	}

	public DisplayWizardModel getModel() {
		return model;
	}

	/**
	 * Create the intermediate wiard steps for displays defined in the registry
	 * 
	 * @return
	 */
	protected List<Pair<WizardStep, Condition>> getPluginSteps() {
		ArrayList<Pair<WizardStep, Condition>> steps = new ArrayList<Pair<WizardStep, Condition>>();
		
		for (VisualizationRegistryData data : VisualizationRegistry.getRegistryData()){
			steps.addAll(data.getDisplayWizardSteps());
		}
		
		return steps;
	}
	
	/**
	 * Evaluates a DisplayScriptor and returns true if its a Cartesian 2D/3D
	 * 
	 * @param descriptor
	 * @return
	 */
	public static boolean isCartesian(DisplayDescriptor descriptor){
		
		return descriptor.getDisplayType().equals(DisplayType.TWO_D) || 
				descriptor.getDisplayType().equals(DisplayType.THREE_D);
		
	}
	
	/**
	 * Create the intermediate wizard steps for the 2D/3D displays.
	 * 
	 * @return
	 */
	protected List<Pair<WizardStep, Condition>> getCartesianSteps() {
		ArrayList<Pair<WizardStep, Condition>> steps = new ArrayList<Pair<WizardStep, Condition>>();

		steps.add(pair(new AgentSelectionStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();

				return descriptor.getProjectionCount() > 0 && isCartesian(descriptor);
			}
		}));

		steps.add(pair(new StyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return descriptor.getProjectionCount() > 0 && isCartesian(descriptor);
			}
		}));

		steps.add(pair(new GridStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return model.containsProjectionType(ProjectionData.GRID_TYPE) 
						&& isCartesian(descriptor);
			}
		}));

		steps.add(pair(new ContinuousStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return model.containsProjectionType(ProjectionData.CONTINUOUS_SPACE_TYPE) 
						&& isCartesian(descriptor);
			}
		}));

		steps.add(pair(new NetLayoutStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return model.containsOnlyProjectionType(ProjectionData.NETWORK_TYPE) 
						&& isCartesian(descriptor);
			}
		}));

		steps.add(pair(new EdgeStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return model.containsProjectionType(ProjectionData.NETWORK_TYPE) 
						&& isCartesian(descriptor);
			}
		}));

		return steps;
	}
	
	protected Pair<WizardStep, Condition> pair(WizardStep step, Condition condition) {
		return new Pair<WizardStep, Condition>(step, condition);
	}

	protected void addPaths() {
		model.add(new GeneralStep());

		// TODO Projections: move 2D/3D to the display registry 
		for (Pair<WizardStep, Condition> step : getCartesianSteps()) {
			model.add(step.getFirst(), step.getSecond());
		}
		
		for (Pair<WizardStep, Condition> step : getPluginSteps()) {
			model.add(step.getFirst(), step.getSecond());
		}

		// TODO Projections: does value layer belong here?
		
		model.add(new ValueLayerStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				return model.containsValueLayer();
			}
		});

		model.add(new ScheduleStep());
	}

	public void showDialog(Component component) {
		wizard.showInDialog("Display Configuration", component, true);
	}

	public DisplayDescriptor getDescriptor() {
		return model.getDescriptor();
	}
}