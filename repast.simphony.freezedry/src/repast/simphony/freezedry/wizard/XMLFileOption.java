/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import org.pietschy.wizard.models.SimplePath;
import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.util.wizard.AbstractWizardOption;
import repast.simphony.util.wizard.DynamicWizard;

public class XMLFileOption extends AbstractWizardOption implements FreezeDryWizardOption {
  public XMLFileOption() {
    super("XML File", "Write data to XML.");
  }

  public DataSourceBuilder createDataSourceBuilder(FreezeDryedDataSource oldDataSource) {
    return null;
  }

  public static SimplePath getPath() {
    SimplePath path = new SimplePath();
    path.addStep(new XMLFileChooserStep());
    return path;
  }

  public SimplePath getWizardPath() {
    return getPath();
  }

  public ControllerActionBuilder<FreezerControllerAction> createAction(final FreezeDryWizardModel model, DataSourceBuilder builder) {
    /*
    final DelimitedFileDataSource dataSource = (DelimitedFileDataSource) builder.getDataSource();

		return new ControllerActionBuilder<FreezerControllerAction>() {
			public FreezerControllerAction createAction() {
				if (model.useRoot()) {
					return new DFFreezerControllerAction(model.getScheduleParams(), dataSource);
				} else {
					return new DFFreezerControllerAction(model.getScheduleParams(), model.getFreezeDryedContextId(), dataSource);
				}
			}
		};
		*/
    return null;
  }

  public static void main(String[] args) {
    DynamicWizard.registerWizardOption(FreezeDryWizardPluginUtil.PACKAGE_ID
            + FreezeDryWizardPluginUtil.WIZARD_OPTIONS_ID, new DelimitedFileOption());

    FreezeDryWizardPluginUtil.create(null, null).showDialog(null, "a");
  }
}