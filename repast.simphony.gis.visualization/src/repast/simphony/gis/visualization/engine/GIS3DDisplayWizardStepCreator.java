package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gui.AgentSelectionStep;
import repast.simphony.visualization.gui.CoverageStyleClassStep;
import repast.simphony.visualization.gui.DisplayWizardModel;
import repast.simphony.visualization.gui.EdgeStyleStep;
import repast.simphony.visualization.gui.GIS3DOptionStep;
import repast.simphony.visualization.gui.LayerOrderStep;
import repast.simphony.visualization.gui.StyleClassStep;

/**
 * Adds wizard step panels to the Display editor for GIS3D displays.
 * 
 * @author Eric Tatara
 *
 */
public class GIS3DDisplayWizardStepCreator {

	/**
	 * Evaluates a DisplayScriptor and returns true if its a GIS 3D
	 * 
	 * @param descriptor
	 * @return
	 */
	public static boolean isGIS3D(DisplayDescriptor descriptor){
		return descriptor.getDisplayType().equals(GIS3DVisualizationRegistryData.TYPE);
	}

	public static List<Pair<WizardStep, Condition>> getDisplayWizardSteps() {
		ArrayList<Pair<WizardStep, Condition>> steps = new ArrayList<Pair<WizardStep, Condition>>();

		PluginWizardStep agentSelectionStep = new AgentSelectionStep();
		
		steps.add(new Pair<WizardStep, Condition>(agentSelectionStep, new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();

				return descriptor.getProjectionCount() > 0 && isGIS3D(descriptor);
			}
		}));

		// The style step init is dependent on the agent selection step, so it 
		//   will listen for any changes that occur.
		StyleClassStep styleClassStep = new StyleClassStep();
		styleClassStep.setShowBackgroundButton(false);
		
		agentSelectionStep.addStepListener(styleClassStep);
		
		// Use the built-in Repast Style step for agents
		steps.add(new Pair<WizardStep, Condition>(styleClassStep, new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return isGIS3D(descriptor);
			}
		}));
		
		PluginWizardStep coverageStyleClassStep = new CoverageStyleClassStep();
		
		// GIS Coverage styles
		// TODO GIS need something similar for the general step projections?
//		agentSelectionStep.addStepListener(coverageStyleClassStep);
		steps.add(new Pair<WizardStep, Condition>(coverageStyleClassStep, new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return isGIS3D(descriptor);
			}
		}));
		
		// GIS 3D options step
		steps.add(new Pair<WizardStep, Condition>(new GIS3DOptionStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return isGIS3D(descriptor);
			}
		}));


		steps.add(new Pair<WizardStep, Condition>(new EdgeStyleStep(), new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();

				return model.containsProjectionType(ProjectionData.NETWORK_TYPE) 
						&& isGIS3D(descriptor);
			}
		}));
		
		LayerOrderStep layerOrderStep = new LayerOrderStep();
		steps.add(new Pair<WizardStep, Condition>(layerOrderStep, new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();

				return isGIS3D(descriptor);
			}
		}));
	
		// The layer order step init is dependent on the agent selection step, so it 
		//   will listen for any changes that occur.
		agentSelectionStep.addStepListener(layerOrderStep);
		coverageStyleClassStep.addStepListener(layerOrderStep);
		
		return steps;
	}
}