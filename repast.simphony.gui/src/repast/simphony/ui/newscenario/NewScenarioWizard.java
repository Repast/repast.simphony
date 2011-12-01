package repast.simphony.ui.newscenario;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.apache.velocity.exception.ParseErrorException;
import org.pietschy.wizard.Wizard;

import repast.simphony.scenario.ScenarioCreator;

/**
 * Wizard for creating a scenario file. The user selects a modelspec.xml and a
 * scenario directory. The modelspec is then copied into this scenario directory.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NewScenarioWizard {

	private NewWizardModel model = new NewWizardModel();
	private Wizard wizard;

	public NewScenarioWizard() {
		model.add(new FileSelectionPanel());
		model.add(new ModelInitPanel());
		model.add(new ScenarioSelectionPanel());
		model.add(new NewScenarioFinalPanel());
		model.setLastVisible(false);
		wizard = new Wizard(model);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		wizard.setOverviewVisible(false);
	}

	/**
	 * Gets the path to the scenario directory.
	 *
	 * @return the path to the scenario directory.
	 */
	public File getScenarioPath() {
		return model.getScenarioPath();
	}

	/**
	 * Creates the scenario.
	 *
	 * @throws IOException if an error was encountered during scenario creation.
	 */
	public void createScenario() throws Exception, ParseErrorException {

		File modelPath = model.getSpecPath();
		File scenarioPath = model.getScenarioPath();
		ScenarioCreator creator;
		if (model.getModelInit() != null && model.getModelInit().length() > 0) {
			creator = new ScenarioCreator(modelPath, scenarioPath, model.getModelInit(), model.doUseModelPlugin());
		} else {
			creator = new ScenarioCreator(modelPath, scenarioPath, model.doUseModelPlugin());
		}
		creator.createScenario();
	}

	/**
	 * Returns true if the wizard was canceled, otherwise false.
	 *
	 * @return true if the wizard was canceled, otherwise false.
	 */
	public boolean wasCanceled() {
		return wizard.wasCanceled();
	}

	/**
	 * Displays the scenario creation wizard relative to the specified component.
	 *
	 * @param comp
	 */
	public void display(Component comp) {
		wizard.showInDialog("Create New Scenario", comp, true);
	}
}
