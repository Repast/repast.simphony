package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gui.DisplayWizardModel;
import repast.simphony.visualization.gui.GISStyleStep;

public class GIS3DDisplayWizardStepCreator {

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

		// TODO GIS: need an agent selection step?
//		steps.add(new Pair<WizardStep, Condition>(new AgentSelectionStep(), new Condition() {
//			public boolean evaluate(WizardModel wizardModel) {
//				DisplayWizardModel model = (DisplayWizardModel) wizardModel;
//				DisplayDescriptor descriptor = model.getDescriptor();
//
//				return descriptor.getProjectionCount() > 0 && isGIS(descriptor);
//			}
//		}));

		steps.add(new Pair<WizardStep, Condition>(new GISStyleStep(), new Condition() {
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