/*CopyrightHere*/
package repast.simphony.util.wizard;

import org.pietschy.wizard.models.Path;

import repast.simphony.scenario.Scenario;

public interface WizardModelFactory {
	DynamicWizardModel create(Path path, Scenario scenario, Object contextID);
}
