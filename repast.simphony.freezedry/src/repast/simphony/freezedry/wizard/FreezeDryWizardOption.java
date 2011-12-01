/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.util.wizard.WizardOption;

public interface FreezeDryWizardOption extends WizardOption {
	DataSourceBuilder createDataSourceBuilder(FreezeDryedDataSource oldDataSource);

	ControllerActionBuilder<? extends FreezerControllerAction> createAction(
			FreezeDryWizardModel model, DataSourceBuilder builder);
}
