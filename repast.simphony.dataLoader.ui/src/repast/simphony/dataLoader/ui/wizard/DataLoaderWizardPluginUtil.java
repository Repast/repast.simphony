/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.java.plugin.PluginManager;
import org.pietschy.wizard.models.Path;

import repast.simphony.scenario.Scenario;
import repast.simphony.util.wizard.DynamicWizard;
import repast.simphony.util.wizard.DynamicWizardModel;
import repast.simphony.util.wizard.WizardModelFactory;
import repast.simphony.util.wizard.WizardPluginUtil;

public class DataLoaderWizardPluginUtil {
  public static final String WIZARD_OPTIONS_ID = "wizard.options";

  public static final String PACKAGE_ID = "repast.simphony.dataloader.ui";

  private static List<DataLoaderWizardOption> wizardOptions;

  // private static final MessageCenter msgCenter = MessageCenter
  // .getMessageCenter(DataLoaderWizardPluginUtil.class);

  public static DynamicWizard create(Scenario scenario, Object contextID) {
    return new DynamicWizard(PACKAGE_ID + WIZARD_OPTIONS_ID, scenario, contextID,
        "Select Data Source Type", "Choose a type for your data source.",
        new DataLoadingSetupFinish(), new WizardModelFactory() {
          public DynamicWizardModel create(Path path, Scenario scenario, Object contextID) {
            return new DataLoaderWizardModel(path, scenario, contextID);
          }
        });
  }

  public static void loadWizardOptions(PluginManager manager) {
    wizardOptions = WizardPluginUtil.loadWizardOptions(manager, DataLoaderWizardOption.class,
        PACKAGE_ID, WIZARD_OPTIONS_ID, true);
  }

  public static List<DataLoaderWizardOption> getWizardOptions() {
    return wizardOptions;
  }
}
