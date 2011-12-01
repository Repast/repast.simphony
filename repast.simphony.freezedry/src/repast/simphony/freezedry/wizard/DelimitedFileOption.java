/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.freezedry.datasource.DelimitedFileDataSource;
import repast.simphony.util.wizard.AbstractWizardOption;
import repast.simphony.util.wizard.DynamicWizard;

public class DelimitedFileOption extends AbstractWizardOption implements FreezeDryWizardOption {
	public DelimitedFileOption() {
		super("Delimited File", "Write data to a delimited file.");
	}

	public DataSourceBuilder createDataSourceBuilder(FreezeDryedDataSource oldDataSource) {
		return new DFDataSourceBuilder((DelimitedFileDataSource) oldDataSource);
	}
	
	public static SimplePath getPath() {
		SimplePath path = new SimplePath();
		path.addStep(new DFFreezerDirectoryChooserStep()); 
		return path;
	}

	public SimplePath getWizardPath() {
		return getPath();
	}

	public ControllerActionBuilder<FreezerControllerAction> createAction(final FreezeDryWizardModel model, DataSourceBuilder builder) {
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
	}
	
	public static void main(String[] args) {
		DynamicWizard.registerWizardOption(FreezeDryWizardPluginUtil.PACKAGE_ID
				+ FreezeDryWizardPluginUtil.WIZARD_OPTIONS_ID, new DelimitedFileOption());
		
		FreezeDryWizardPluginUtil.create(null, null).showDialog(null, "a");
	}
}
