/*CopyrightHere*/
package repast.simphony.util.wizard;

import org.pietschy.wizard.models.Path;

import repast.simphony.scenario.Scenario;

public class DynamicWizardModelFactory implements WizardModelFactory {
	public DynamicWizardModel create(Path path, Scenario scenario, Object contextID) {
		return new DynamicWizardModel(path, scenario, contextID);
	}

}
