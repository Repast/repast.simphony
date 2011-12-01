/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import org.pietschy.wizard.models.SimplePath;

import repast.simphony.freezedry.datasource.JDBCDataSource;
import repast.simphony.freezedry.engine.JDBCFreezerControllerAction;
import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.freezedry.wizard.ControllerActionBuilder;
import repast.simphony.freezedry.wizard.DataSourceBuilder;
import repast.simphony.freezedry.wizard.FreezeDryWizardModel;
import repast.simphony.freezedry.wizard.FreezeDryWizardOption;
import repast.simphony.freezedry.wizard.FreezeDryWizardPluginUtil;
import repast.simphony.freezedry.wizard.FreezerControllerAction;
import repast.simphony.util.wizard.AbstractWizardOption;
import repast.simphony.util.wizard.DynamicWizard;

public class JDBCFreezerOption extends AbstractWizardOption implements
		FreezeDryWizardOption {
	
	public JDBCFreezerOption() {
		super("Database", "Write data to a database.");
	}

	public SimplePath getWizardPath() {
		SimplePath path = new SimplePath();
		path.addStep(new JDBCFreezerDataChooserStep());
		return path;
	}

	public DataSourceBuilder createDataSourceBuilder(FreezeDryedDataSource oldDataSource) {
		return new JDBCDSBuilder((JDBCDataSource) oldDataSource);
	}

	public ControllerActionBuilder<FreezerControllerAction> createAction(final FreezeDryWizardModel model, DataSourceBuilder builder) {
		final JDBCDataSource dataSource = (JDBCDataSource) builder.getDataSource();
		
		return new ControllerActionBuilder<FreezerControllerAction>() {
			public FreezerControllerAction createAction() {
				if (model.useRoot()) {
					return new JDBCFreezerControllerAction(model.getScheduleParams(), dataSource);					
				} else {
					return new JDBCFreezerControllerAction(model.getScheduleParams(), model.getFreezeDryedContextId(), dataSource);
				}
			}
		};
	}

	public static void main(String[] args) {
		DynamicWizard.registerWizardOption(FreezeDryWizardPluginUtil.PACKAGE_ID
				+ FreezeDryWizardPluginUtil.WIZARD_OPTIONS_ID, new JDBCFreezerOption());
		
		FreezeDryWizardPluginUtil.create(null, null).showDialog(null, "a");
	}
}
