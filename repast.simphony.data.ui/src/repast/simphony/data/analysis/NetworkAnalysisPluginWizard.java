package repast.simphony.data.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import repast.simphony.context.Context;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.gui.FileChooserStep;
import repast.simphony.data2.gui.NetworkChooserStep;
import repast.simphony.engine.environment.RunState;
import repast.simphony.space.graph.Network;
import repast.simphony.util.Settings;
import simphony.util.messages.MessageCenter;

/**
 * A wizard for executing a third party analysis tool on a file outputter's
 * output.
 * 
 * @author Michael J. North
 * @author Eric Tatara
 * @author Jerry Vos
 */
public class NetworkAnalysisPluginWizard extends AnalysisPluginWizard {
	protected static final MessageCenter LOG = MessageCenter
			.getMessageCenter(AnalysisPluginWizard.class);

	private StaticModel wizardModel;

	private boolean skipFirstStep;

	private boolean runProgram = true;

	public String defaultFileExtension = "";

	public String getDefaultFileExtension() {
		return defaultFileExtension;
	}

	public void setDefaultFileExtension(String defaultExtension) {
		this.defaultFileExtension = defaultExtension;
	}

	public String getFileName() {
		if (this.fileStep == null) {
			return "";
		} else {
			return this.fileStep.getFileName();
		}
	}

	public boolean isRunProgram() {
		return runProgram;
	}

	public void setRunProgram(boolean runProgram) {
		this.runProgram = runProgram;
	}

	private String defaultLocation;
	private String installHome;
	private String name;
	private String licenseFileName;

	private BrowseForHomeStep homeStep;
	protected NetworkChooserStep networkStep;
	protected FileChooserStep fileStep;

	public NetworkAnalysisPluginWizard(String defaultFileExtension ) {
		this.defaultFileExtension = defaultFileExtension;
	}

	public NetworkAnalysisPluginWizard(DataSetRegistry loggingRegistry,
			boolean showCopyright, boolean browseForHome, String name,
			String installHome, String defaultLocation, String licenseFileName,
			String defaultFileExtension) {

		this.wizardModel = new StaticModel();
		this.defaultLocation = defaultLocation;
		this.installHome = installHome;
		this.name = name;
		this.licenseFileName = licenseFileName;
		this.defaultFileExtension = defaultFileExtension;

		setupWizard(showCopyright, browseForHome);
	}

	public void init(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForHome, String name, String installHome,
			String defaultLocation, String licenseFileName) {

		this.wizardModel = new StaticModel();
		this.defaultLocation = defaultLocation;
		this.installHome = installHome;
		this.name = name;
		this.licenseFileName = licenseFileName;

		setupWizard(showCopyright, browseForHome);
	}

	private void setupWizard(boolean showCopyright, boolean browseForHome) {

		if (runProgram) {
			if (showCopyright) {
				addCopyRightStep();
			}
			addBrowseForHomeStep();
		}

		addSelectNetworkStep();
		if (!browseForHome && !showCopyright) {
			// advance past the browse for home step
			skipFirstStep = true;
		}

		addSelectFileNameStep();

	}

	private void addCopyRightStep() {
		wizardModel.add(new CopyRightStep(name, this.getClass()
				.getResourceAsStream(licenseFileName)));
	}

	private void addBrowseForHomeStep() {
		homeStep = new BrowseForHomeStep(name, installHome, defaultLocation);
		wizardModel.add(homeStep);
	}

	private void addSelectNetworkStep() {
		networkStep = new NetworkChooserStep(getNetworks(), false,
				"Select the network to pass to " + name,
				"<HTML>Please select which network's data you would "
						+ "like to send to " + name + ".<BR>");
		wizardModel.add(networkStep);
	}

	private void addSelectFileNameStep() {
		fileStep = new FileChooserStep("Main File Name",
				"Enter the main file name", this.defaultFileExtension);
		wizardModel.add(fileStep);
	}

	private List<Network> getNetworks() {
		ArrayList<Network> networks = new ArrayList<Network>();
		Context currentContext = RunState.getInstance().getMasterContext();
		this.getNetworks(networks, currentContext);
		return networks;
	}

	private void getNetworks(List<Network> networks, Context currentContext) {
		for (Object projection : currentContext.getProjections()) {
			if (projection instanceof Network) {
				networks.add((Network) projection);
			}
		}
		for (Object context : currentContext.getSubContexts()) {
			if (context instanceof Context) {
				this.getNetworks(networks, (Context) context);
			}
		}
	}

	protected String prepFileNameFor(String fileName) {
		// return fileName.replace('\\', '/');

		String pass1 = fileName.replace('\\', File.separatorChar);
		String pass2 = pass1.replace('/', File.separatorChar);

		return pass2;

	}

	/**
	 * Gets the home directory of the application.
	 * 
	 * @return String path of the application directory
	 */
	public String getInstallHome() {
		String home;
		if (homeStep == null) {
			home = homeStep.getDefaultLocation();
		} else {
			home = homeStep.getInstallHome();
		}
		// if (!home.endsWith(SystemConstants.DIR_SEPARATOR)) {
		// return home.concat(SystemConstants.DIR_SEPARATOR);
		// } else {
		// return home;
		// }
		return home;
	}

	/**
	 * Shows the wizard in a modal dialog.
	 * 
	 * @return if the wizard was was completed (true) or canceled (false).
	 */
	public boolean showDialogModal() {
		Wizard wizard = new Wizard(wizardModel);
		wizard.setOverviewVisible(false);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);

		if (skipFirstStep) {
			// advance past the browse for home step
			wizardModel.nextStep();
		}

		wizard.showInDialog(name, null, true);

		if (!wizard.wasCanceled()) {
			Settings.put(name + ".home", homeStep.getInstallHome());
		}
		return !wizard.wasCanceled();
	}

	public void setHome(String settingsRHome) {
		homeStep.homeDirField.setText(settingsRHome);
	}

	@Override
	public String[] getExecutionCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCannotRunMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
