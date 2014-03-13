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

/**
 * Wizard for building a display from scratch.
 * 
 * @author Nick Collier
 */
public class DisplayConfigurationWizard {

	private Wizard wizard;

	private DisplayWizardModel model;

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

	protected List<Pair<WizardStep, Condition>> getIntermediateSteps() {
		ArrayList<Pair<WizardStep, Condition>> steps = new ArrayList<Pair<WizardStep, Condition>>();

		// TODO Projections: Get the wizard steps to add from the display registry
		//
		//   ...don't need the conditions if the provider class from the registry
		//   can just provide the steps that are supported.
		//
		//		model.add(step);

		model.add(new AgentSelectionStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;

				// TODO Projections: for GIS
				return model.getDescriptor().getProjectionCount() > 0
						&& !model.getDescriptor().getDisplayType().equals(DisplayType.GIS);
			}
		});

		model.add(new StyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;

				// TODO Projections: for GIS
				return model.getDescriptor().getProjectionCount() > 0
						&& !model.getDescriptor().getDisplayType().equals(DisplayType.GIS);
			}
		});

		steps.add(pair(new GridStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				return model.containsProjectionType(ProjectionData.GRID_TYPE);
			}
		}));

		steps.add(pair(new ContinuousStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				return model.containsProjectionType(ProjectionData.CONTINUOUS_SPACE_TYPE);
			}
		}));

	// TODO Projections: for GIS
		steps.add(pair(new GISStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				return model.getDescriptor().getDisplayType().equals(DisplayType.GIS);
			}
		}));

		steps.add(pair(new NetLayoutStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				return model.containsOnlyProjectionType(ProjectionData.NETWORK_TYPE);
			}
		}));

		steps.add(pair(new EdgeStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				
			// TODO Projections: for GIS
				return model.containsProjectionType(ProjectionData.NETWORK_TYPE)
						&& !model.getDescriptor().getDisplayType().equals(DisplayType.GIS);
			}
		}));

		return steps;
	}

	protected Pair<WizardStep, Condition> pair(WizardStep step, Condition condition) {
		return new Pair<WizardStep, Condition>(step, condition);
	}

	protected void addPaths() {
		model.add(new GeneralStep());

		for (Pair<WizardStep, Condition> step : getIntermediateSteps()) {
			model.add(step.getFirst(), step.getSecond());
		}

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
