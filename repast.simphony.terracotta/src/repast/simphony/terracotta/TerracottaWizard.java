/**
 * 
 */
package repast.simphony.terracotta;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.engine.controller.Controller;
import repast.simphony.ui.RSApplication;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;

/**
 * Main Terracotta wizard for setting up user inputs.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */

@SuppressWarnings("serial")
public class TerracottaWizard<T> extends AbstractAction implements ISAFAction<RSApplication>{
	
	@SuppressWarnings("unused")
	/**The logging registry used in the object*/
	private DataSetRegistry loggingRegistry;
	
	/**The wizard model object*/
	private StaticModel wizardModel;
	
	/**boolean to skip a step or not*/
	private boolean skipFirstStep;
	
	/**The workspace repast object*/
	private Workspace<RSApplication> workspace;
	
	/**The finish step for the plugin wizard*/
	private TerracottaFinishStep finalStep;
	
	/**
	 * Method to initialize logging and other settings for plugin.
	 * @param loggingRegistry a logging registry object
	 * @param showCopyright the true/fasle for the copyright presence
	 */
	public void init(DataSetRegistry loggingRegistry, boolean showCopyright) {
		

		this.loggingRegistry = loggingRegistry;
		this.wizardModel = new StaticModel();

		setupWizard(showCopyright);
	}
	
	/**
	 * Method to setup the wizard.
	 * @param showCopyright
	 */
	private void setupWizard(boolean showCopyright) {
		if (showCopyright) {
			addCopyRightStep();
		}
//		addBrowseForTerracottaHomeStep();
		addSelectChoicesStep();
		addFinalStep();
		if (!showCopyright) {
			
			skipFirstStep = true;
		}
	}
	
	/**
	 * The final step in the wizard for launching terracotta application.
	 */
	private void addFinalStep(){
		finalStep=new TerracottaFinishStep();
		wizardModel.add(finalStep);
	}
	
	/**
	 * Method to allow the step for browsing for the run script file.
	 */
	private void addSelectChoicesStep(){
		wizardModel.add(new BrowseForRunFile());
	}
	
	/**
	 * The copyright step finding the terracotta copyright file.
	 */
	private void addCopyRightStep() {
		wizardModel.add(new CopyRightStep());
	}
	
	
	/**
	 * Shows the wizard in a modal dialog.
	 * @return if the wizard was was completed (true) or canceled (false).
	 */
	public boolean showDialogModal() {
		Wizard wizard = new Wizard(wizardModel);
		wizard.setOverviewVisible(false);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		
		if (skipFirstStep) {
			// advance past the browse for Terracotta home step
			wizardModel.nextStep();
		}
		
		wizard.showInDialog("Terracotta Wizard", null, true);
		
		if (!wizard.wasCanceled()) {
	//		Settings.put(BrowseForTerracottaHomeStep.T_INSTALL_HOME_KEY, homeStep.getTerracottaInstallHome());
			finalStep.fireCommand();
		}
		
		return !wizard.wasCanceled();
	}

	public void initialize(Workspace<RSApplication> workspace) {
		this.workspace = workspace;	
	}

	
	public void actionPerformed(ActionEvent e) {
		if (workspace.getApplicationMediator().getController() == null
				|| workspace.getApplicationMediator().getController()
						.getCurrentRunState() == null) {
			
			return;
		}

		Controller controller = workspace.getApplicationMediator()
				.getController();
		DataSetRegistry registry = (DataSetRegistry) controller.getCurrentRunState().getFromRegistry(DataConstants.REGISTRY_KEY);

		init(registry,true);
		showDialogModal();
		
	}

}
