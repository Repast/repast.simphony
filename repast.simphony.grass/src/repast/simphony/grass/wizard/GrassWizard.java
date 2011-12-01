/**
 * 
 */
package repast.simphony.grass.wizard;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.commons.lang.SystemUtils;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.engine.controller.Controller;
import repast.simphony.ui.RSApplication;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;

/**
 * Main Grass wizard for setting up user inputs.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
@SuppressWarnings("serial")
public class GrassWizard extends AbstractAction implements ISAFAction<RSApplication>{
	
	/**The logging imported into the object*/
	@SuppressWarnings("unused")
	private DataSetRegistry loggingRegistry;
	
	/**The Wizard model for the plugin*/
	private StaticModel wizardModel;
	
	/**A boolean for skipping the first step*/
	private boolean skipFirstStep;
	
	/**The Workspace object for the scenario run*/
	private Workspace<RSApplication> workspace;
	
	/**The final step in the wizard*/
	private GrassFinishStep finalStep;
	
	/**
	 * Method to initialize logging and other settings for plugin.
	 * @param loggingRegistry a logging registry object
	 * @param showCopyright the true/false for the copyright presence
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
//		addBrowseForGrassHomeStep();
		addSelectChoicesStep();
		addFinalStep();
		if (!showCopyright) {
			
			skipFirstStep = true;
		}
	}
	
	/**
	 * The final step in the wizard for launching Grass application.
	 */
	private void addFinalStep(){
		finalStep=new GrassFinishStep();
		wizardModel.add(finalStep);
	}
	
	/**
	 * Method to allow the step for browsing for the run script file.
	 */
	private void addSelectChoicesStep(){
		String defaultLocation=null;
		if(SystemUtils.IS_OS_WINDOWS){
			defaultLocation=SearchForRunFile.DEFAULT_LOCATION_WIN;
		}
		else{
			defaultLocation=SearchForRunFile.DEFAULT_LOCATION_MAC;
		}
		
		wizardModel.add(new SearchForRunFile(defaultLocation));
	}
	
	/**
	 * The copyright step finding the Grass copyright file.
	 */
	private void addCopyRightStep() {
		wizardModel.add(new CopyRightStep());
	}
	
	
	/**
	 * Shows the wizard in a modal dialog.
	 * @return if the wizard was completed (true) or canceled (false).
	 */
	public boolean showDialogModal() {
		Wizard wizard = new Wizard(wizardModel);
		wizard.setOverviewVisible(false);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		
		if (skipFirstStep) {
			// advance past the browse for Grass home step
			wizardModel.nextStep();
		}
		
		wizard.showInDialog("Grass 6.3 Wizard", null, true);
		
		if (!wizard.wasCanceled()) {
	//		Settings.put(BrowseForGrassHomeStep.T_INSTALL_HOME_KEY, homeStep.getGrassInstallHome());
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
