/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.wizard.WizardOption;

public interface DataLoaderWizardOption extends WizardOption {
	ContextActionBuilder createBuilder(ContextBuilder baseLoader);
}
