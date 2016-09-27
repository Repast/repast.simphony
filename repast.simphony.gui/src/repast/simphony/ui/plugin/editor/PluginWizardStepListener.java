package repast.simphony.ui.plugin.editor;

/**
 * Listener for Repast Plugin wizard step classes
 * @author eric
 *
 */
public interface PluginWizardStepListener {

	/**
	 * Update any data in an implementing wizard step.  Useful for when the step
	 * contents are dependent on another step and need to be updated accordingly,
	 * as in the options dialog for editing an existing item in the Scenario Tree.
	 */
	public void updateStep();
	
}
