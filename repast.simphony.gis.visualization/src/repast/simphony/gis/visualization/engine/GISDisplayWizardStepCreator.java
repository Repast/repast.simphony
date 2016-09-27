package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gui.AgentSelectionStep;
import repast.simphony.visualization.gui.DisplayWizardModel;
import repast.simphony.visualization.gui.GISStyleStep;
import repast.simphony.visualization.gui.StyleClassStep;

/**
 * Adds wizard step panels to the Display editor for GIS displays.
 * 
 * @author Eric Tatara
 *
 */
public class GISDisplayWizardStepCreator {

	/**
	 * Evaluates a DisplayScriptor and returns true if its a GIS 2D
	 * 
	 * @param descriptor
	 * @return
	 */
	public static boolean isGIS(DisplayDescriptor descriptor){
		return descriptor.getDisplayType().equals(GISVisualizationRegistryData.TYPE);
	}

	public static List<Pair<WizardStep, Condition>> getDisplayWizardSteps() {
		ArrayList<Pair<WizardStep, Condition>> steps = new ArrayList<Pair<WizardStep, Condition>>();

		// TODO Instead of using a specific agent selection step for GIS, the viz 
		//      registry data should specify if background/foreground layer index is zero.
		PluginWizardStep agentSelectionStep = new GISAgentSelectionStep();
		steps.add(new Pair<WizardStep, Condition>(agentSelectionStep, new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();

				return descriptor.getProjectionCount() > 0 && isGIS(descriptor);
			}
		}));

		// The style step init is dependent on the agent selection step, so it 
		//   will listen for any changes that occur.
		PluginWizardStep styleStep = new GISStyleStep();
		agentSelectionStep.addStepListener(styleStep);
		
		steps.add(new Pair<WizardStep, Condition>(styleStep, new Condition() {
			public boolean evaluate(WizardModel wizardModel) {
				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
				DisplayDescriptor descriptor = model.getDescriptor();
				
				return isGIS(descriptor);
			}
		}));

		// TODO GIS: network visualization 
//		steps.add(new Pair<WizardStep, Condition>(new NetLayoutStep(), new Condition() {
//			public boolean evaluate(WizardModel wizardModel) {
//				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
//				DisplayDescriptor descriptor = model.getDescriptor();
//
//				return model.containsOnlyProjectionType(ProjectionData.NETWORK_TYPE) 
//						&& isGIS(descriptor);
//			}
//		}));
//
//		steps.add(new Pair<WizardStep, Condition>(new EdgeStyleStep(), new Condition() {
//			public boolean evaluate(WizardModel wizardModel) {
//				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
//				DisplayDescriptor descriptor = model.getDescriptor();
//
//				return model.containsProjectionType(ProjectionData.NETWORK_TYPE) 
//						&& isGIS(descriptor);
//			}
//		}));
	
		return steps;
	}
}