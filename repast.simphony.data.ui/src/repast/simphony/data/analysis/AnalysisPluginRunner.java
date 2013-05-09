package repast.simphony.data.analysis;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.engine.controller.Controller;
import repast.simphony.ui.RSApplication;
import repast.simphony.util.Settings;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;
import simphony.settings.SettingsRegistry;
import simphony.util.messages.MessageCenter;

/**
 * The AnalysisPluginRunner is a helper class that should be extended by simple
 * plugin runner classes for running command line applications from the gui
 * 
 * @author Eric Tatara
 * 
 */

public class AnalysisPluginRunner extends AbstractAction implements
		ISAFAction<RSApplication> {

	protected String name;
	protected String licenseFileName;
	protected String installHomeKey;
	protected String licenseAcceptedKey;

	protected String defaultLocation;

	protected MessageCenter LOG = MessageCenter
			.getMessageCenter(AnalysisPluginRunner.class);

	protected Workspace<RSApplication> workspace;
	protected SettingsRegistry settingsRegistry;

	protected AnalysisPluginWizard wizard;

	public AnalysisPluginRunner(String name, String defaultLocation,
			String licenseFileName, AnalysisPluginWizard wizard) {

		this.name = name;
		this.licenseFileName = licenseFileName;
		this.defaultLocation = defaultLocation;
		this.wizard = wizard;

		installHomeKey = name + ".home";
		licenseAcceptedKey = name + ".licenseAccepted";
	}

	public void initialize(Workspace<RSApplication> workspace) {
		this.workspace = workspace;
		settingsRegistry = workspace.getApplicationMediator()
				.getSettingsRegistry();
	}

	public void actionPerformed(ActionEvent e) {
		if (workspace.getApplicationMediator().getController() == null
				|| workspace.getApplicationMediator().getController()
						.getCurrentRunState() == null) {
			JOptionPane.showMessageDialog(null,
					"Please load a model and initialize the simulation "
							+ "before attempting to invoke " + name);
			return;
		}

		Controller controller = workspace.getApplicationMediator()
				.getController();
		DataSetRegistry registry = (DataSetRegistry) controller
				.getCurrentRunState().getFromRegistry(
						DataConstants.REGISTRY_KEY);

		boolean showCopyright = true;
		Boolean settingsLicenseAccepted = (Boolean) settingsRegistry
				.get(licenseAcceptedKey);
		if (settingsLicenseAccepted != null
				&& settingsLicenseAccepted == Boolean.TRUE) {
			showCopyright = false;
		}

		boolean showBrowseForRHome = true;
		String settingsHome = (String) settingsRegistry.get(installHomeKey);
		if (settingsHome != null) {
			showBrowseForRHome = false;
		}
		String installHome = (String) Settings.get(installHomeKey);
		;

		wizard.init(registry, showCopyright, showBrowseForRHome, name,
				installHome, defaultLocation, licenseFileName);

		if (settingsHome != null) {
			wizard.setHome(settingsHome);
		}
		if (wizard.showDialogModal()) {
			// update settings
			storeSettings(wizard);

			run(wizard);
		}
	}

	protected void storeSettings(AnalysisPluginWizard wizard) {
		settingsRegistry.put(licenseAcceptedKey, true);
		settingsRegistry.put(installHomeKey, wizard.getInstallHome());
	}

	public void run(AnalysisPluginWizard wizard) {
		String[] command = null;
		try {
			command = wizard.getExecutionCommand();
			Map<String, String> envVars = wizard.getEnvVars();

			if (command == null) {

				String message = wizard.getCannotRunMessage();
				if (message != null) {
					JOptionPane.showMessageDialog(null, message);
				}

			} else {

				Process process;

				for (String s : command)
					System.out.print(s + " ");
				System.out.print("\n");

				ProcessBuilder builder;
				PluginOutputStream pos;

				if (SystemUtils.IS_OS_WINDOWS) {
					process = Runtime.getRuntime().exec(command);

					pos = new PluginOutputStream(process.getInputStream());
					pos.start();
				} else {

					builder = new ProcessBuilder(command)
							.redirectErrorStream(true);
					System.out.println(envVars);
					builder.environment().putAll(envVars);
					System.out.println(builder.directory(new File(".").getAbsoluteFile()));
					System.out.println(builder.directory());
					process = builder.start();

					pos = new PluginOutputStream(process.getInputStream());
					pos.start();
				}

				PluginOutputStream pos2 = new PluginOutputStream(process
						.getErrorStream());
				pos2.start();

			}

		} catch (Exception e) {

			System.out.println(e);
			
//			LOG.error("Wizard.run: Error executing " + name
//					+ ".  Command is: '" + arrayToString(command) + "'",
//					e);
		}
	}

  public static String arrayToString(String[] input){
  	StringBuffer buf = new StringBuffer();
  	
  	for (String s : input)
  		buf.append(s);

  	return buf.toString();
  }
  
  public static void main (String [] args){
  	String[] command = new String[2];
  	
//  	command[0] = "C:\\Program Files (x86)\\Microsoft Office\\OFFICE14\\Excel.exe";
//  	command[1] = "C:\\Users\\tatara\\Repast-Simphony\\Schelling\\ModelOutput.2013.May.01.17_12_19.txt.csv ";
  	
//  	command[0] = "C:/R-3.0.0/bin/x64/RGui.exe";
//  	command[1] = "--sdi";
//  	command[2] = "HOME=C:/Users/eric/repast-simphony/Schelling/RHome";
//  	command[3] = "LOG_FILE0=C:/Users/eric/repast-simphony/Schelling/ModelOutput.2013.May.02.17_35_41.txt.csv";
//  	command[4] = "DELIMITER0=,";
  	
  	command[0] = "C:\\Program Files\\ORA-NetScenes-cst-iw-64\\ORA-NetScenes-cst-iw-64.exe";
  	command[1] = "C:\\Users\\eric\\repast-simphony\\JZombies_Demo\\test7.xml";
//  	command[1] = "test7.xml";
  	
//  	command[0] = "C:\\Program Files (x86)\\JasperSoft\\iReport-5.1.0\\bin\\ireport.exe";
//  	command[1] = "C:\\Users\\eric\\Desktop\\ireport connection.xml";
//  	command[1] = "--open file";
//  	command[1] = "C:\\Users\\eric\\Desktop\\report1.jrxml";
  	
  	for (String s : command)
			System.out.print(s + " ");
		System.out.print("\n");
  	
  	try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}
