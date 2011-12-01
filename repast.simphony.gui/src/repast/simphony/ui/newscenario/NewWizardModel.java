package repast.simphony.ui.newscenario;

import java.io.File;

import org.pietschy.wizard.models.StaticModel;

/**
 * Wizard model for the create new scenario wizard.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NewWizardModel extends StaticModel {

	private File specPath = new File(".");
	private File scenarioPath;
	private File scorePath;
	private String modelInit = null;
	private boolean usePlugin = false;

	/**
	 * Gets the path to the scenario dir.
	 * @return the path to the scenario dir.
	 */
	public File getScenarioPath() {
		return scenarioPath;
	}


	/**
	 * Sets the path to the score dir.
	 *
	 * @param scorePath the new path
	 */
	public void setScorePath(File scorePath) {
		this.scorePath = scorePath;
	}

	/**
	 * Gets the path to the score dir.
	 * @return the path to the score dir.
	 */
	public File getScorePath() {
		return scorePath;
	}


	/**
	 * Sets the path to the scenario dir.
	 *
	 * @param scenarioPath the new path
	 */
	public void setScenarioPath(File scenarioPath) {
		this.scenarioPath = scenarioPath;
	}

	/**
	 * Gets the path to the model spec file.
	 *
	 * @return Gets the path to the model spec file.
	 */
	public File getSpecPath() {
		return specPath;
	}

	/**
	 * Sets the path to the model spec file.
	 *
	 * @param specPath the new path
	 */
	public void setSpecPath(File specPath) {
		this.specPath = specPath;
	}

	/**
	 * Gets the name of an optional model initializer class.
	 *
	 * @return the name of an optional model initializer class.
	 */
	public String getModelInit() {
		return modelInit;
	}

	/**
	 * Sets the name of an optional model initializer class.
	 */
	public void setModelInit(String modelInit) {
		this.modelInit = modelInit;
	}

	/**
	 * Gets whether or not a model plugin will be used.
	 * @return whether or not a model plugin will be used.
	 */
	public boolean doUseModelPlugin() {
		return usePlugin;
	}

	/**
	 * Sets whether or not a model plugin will be used.
	 */
	public void setUseModelPlugin(boolean useModel) {
		this.usePlugin = useModel;
	}
}
