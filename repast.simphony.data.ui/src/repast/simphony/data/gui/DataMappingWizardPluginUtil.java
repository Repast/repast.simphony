/*CopyrightHere*/
package repast.simphony.data.gui;

import org.java.plugin.PluginManager;
import org.pietschy.wizard.models.Path;
import repast.simphony.scenario.Scenario;
import repast.simphony.util.wizard.DynamicWizard;
import repast.simphony.util.wizard.DynamicWizardModel;
import repast.simphony.util.wizard.WizardModelFactory;
import repast.simphony.util.wizard.WizardPluginUtil;
import simphony.util.messages.MessageCenter;

import java.util.List;

public class DataMappingWizardPluginUtil {
	public static final String INDIVIDUAL_WIZARD_OPTIONS_ID = "wizard.options";
	
	public static final String AGGREGATE_WIZARD_OPTIONS_ID = "aggregate.wizard.options";

	public static final String PACKAGE_ID = "repast.simphony.data.ui";

	@SuppressWarnings("unused")
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(DataMappingWizardPluginUtil.class);

	private static List<MappingWizardOption> aggregateOptions;
	
	private static List<MappingWizardOption> individualOptions;

	public static DynamicWizard create(boolean isAggregate, final Class<?> agentClass) {
		String id = PACKAGE_ID;
		if (isAggregate) {
			id += AGGREGATE_WIZARD_OPTIONS_ID;
		} else {
			id += INDIVIDUAL_WIZARD_OPTIONS_ID;
		}
		
		DynamicWizard wizard = new DynamicWizard(id, null, null,
				"Choose Mapping Type", "Select a mapping type.", new FinishMappingStep(),
				new WizardModelFactory() {
					public DynamicWizardModel create(Path path, Scenario scenario,
							Object contextID) {
            DataMappingWizardModel model = new DataMappingWizardModel(path, agentClass);
            model.setLastVisible(false);
            return model;
					}
				});

    return wizard;
	}

	public static void loadWizardOptions(PluginManager manager) {
		individualOptions = WizardPluginUtil.loadWizardOptions(manager, MappingWizardOption.class,
				PACKAGE_ID, INDIVIDUAL_WIZARD_OPTIONS_ID, true);
		aggregateOptions = WizardPluginUtil.loadWizardOptions(manager, MappingWizardOption.class,
				PACKAGE_ID, AGGREGATE_WIZARD_OPTIONS_ID, true);
		for (MappingWizardOption option : aggregateOptions) {
			option.setIsAggregate(true);
		}
	}

	public static List<MappingWizardOption> getIndividualOptions() {
		return individualOptions;
	}
	
	public static List<MappingWizardOption> getAggregateOptions() {
		return aggregateOptions;
	}
}
