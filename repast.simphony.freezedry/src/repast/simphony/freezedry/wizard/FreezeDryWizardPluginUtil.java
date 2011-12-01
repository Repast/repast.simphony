/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import java.util.List;

import org.java.plugin.PluginManager;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Path;

import repast.simphony.scenario.Scenario;
import repast.simphony.util.wizard.DefaultFinishStep;
import repast.simphony.util.wizard.DynamicWizard;
import repast.simphony.util.wizard.WizardModelFactory;
import repast.simphony.util.wizard.WizardPluginUtil;

public class FreezeDryWizardPluginUtil {
	public static final String WIZARD_OPTIONS_ID = "wizard.options";

	public static final String PACKAGE_ID = "repast.simphony.freezedry";

	private static List<FreezeDryWizardOption> wizardOptions;

	// private static final MessageCenter msgCenter = MessageCenter
	// .getMessageCenter(DataLoaderWizardPluginUtil.class);

	public static DynamicWizard create(Scenario scenario, Object contextID) {
		return create(scenario, contextID, true, "The freeze drying action is ready to be used.");
	}
	
	public static DynamicWizard create(Scenario scenario, Object contextID, boolean showScheduleParams, String finishText) {
		WizardStep[] firstSteps;
		if (showScheduleParams) {
			firstSteps = new WizardStep[] { new ChooseContextStep(), new FreezeDryScheduleStep() };
		} else {
			firstSteps = new WizardStep[] { new ChooseContextStep() };
		}
		DynamicWizard wiz = new DynamicWizard(PACKAGE_ID + WIZARD_OPTIONS_ID, scenario, contextID,
				"Select Data Source Type", "Choose a type for your data source.",
				new DefaultFinishStep("Finished", finishText), new WizardModelFactory() {
					public FreezeDryWizardModel create(Path path, Scenario scenario, Object contextID) {
						return new FreezeDryWizardModel(path, scenario, contextID);
					}
				}, firstSteps);
		return wiz;
	}

	public static void loadWizardOptions(PluginManager manager) {
		wizardOptions = WizardPluginUtil.loadWizardOptions(manager, FreezeDryWizardOption.class,
				PACKAGE_ID, WIZARD_OPTIONS_ID, true);
	}

	public static List<FreezeDryWizardOption> getWizardOptions() {
		return wizardOptions;
	}
}
