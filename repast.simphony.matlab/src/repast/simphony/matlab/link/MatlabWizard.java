package repast.simphony.matlab.link;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jmatlink.CoreJMatLink;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.gui.FileSinkChooserStep;
import repast.simphony.ui.plugin.editor.PluginWizard;
import repast.simphony.util.Settings;
import repast.simphony.util.SystemConstants;
import simphony.util.messages.MessageCenter;

/**
 * Wizard tool for using MatLab for data analysis
 * @author Mark Altaweel
 */
public class MatlabWizard {
	
	
	private class CopyRightStep extends PanelWizardStep {
		private static final long serialVersionUID = -1322640934707656871L;
		public CopyRightStep() {
			super("Matlab Plugin Wizard for Repast Simphony",
					"This plugin wizard will only work if you have Matlab installed on a Windows machine");
			
			setupPanel();
		}

		private void setupPanel() {
			JTextArea license = buildLicenseArea();
			
			JScrollPane scrollPane = new JScrollPane(license);
			scrollPane.setPreferredSize(new Dimension(560, 200));
			
			add(scrollPane);
			
			setComplete(true);
		}
		
		private JTextArea buildLicenseArea() {
			JTextArea area = new JTextArea();
			area.setEditable(false);
			
			area.setText(getLicenseText());
			area.setFont(new Font("Monospaced", Font.PLAIN, 12));
			
			area.setCaretPosition(0);
	
			return area;
		}
		
	
		
		/**
		 * Method to get the license file used 
		 * @return a string text file
		 */
		private String getLicenseText() {
			StringBuffer buffer = new StringBuffer();
			
			try {
				String path = getClass().getProtectionDomain().getCodeSource().
                	getLocation().toString().substring(6,getClass().getProtectionDomain().getCodeSource().
                        	getLocation().toString().length()-5);
				
				if(path.contains("%20")) {
					String split[]=path.split("%20");
					path="";	
						for (int i=0; i < split.length; i++) {
							
							if(i==0) {
								path=path+split[0];
							}
							else {
								path=path+" "+split[i];
							}
						}
				}
				
				BufferedReader reader = new BufferedReader(new FileReader(path+"\\licenses\\license.txt"));
				String line = reader.readLine();

				while (line != null) {
					buffer.append(line).append("\n");
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				buffer.append("There was an error loading the license file");
				LOG.error("CopyRightStep.getLicenseText: Error loading the MatLab license file", e);
				
			}
			
			return buffer.toString();
		}
	}
	
	private class BrowseForMatLabHomeStep extends PanelWizardStep {
		private static final long serialVersionUID = -3143902886682424228L;

		public static final String M_INSTALL_HOME_KEY = "repast.simphony.matlab.MatLabInstallHome";
		
		private static final String DEFAULT_LOCATION = "C:\\Program Files\\MATLAB\\R2006b\\bin\\win32";//C:\\Program Files\\R\\rw2011\\";
		
		private JTextField homeDirField;
		
		public BrowseForMatLabHomeStep() {
			super("MatLab home", "<HTML>Please browse to MatLab's home directory.<BR>" +
					"The default installation location for MatLab is " + DEFAULT_LOCATION);
			
			setupPanel();
		}
		
		private void setupPanel() {
			String home = (String) Settings.get(M_INSTALL_HOME_KEY);
			if (home == null) {
				home = DEFAULT_LOCATION;
			}
			
			homeDirField = new JTextField(home);
			homeDirField.setPreferredSize(new Dimension(400, 20));
			add(homeDirField);
			
			JButton browseButton = new JButton("Browse");
			browseButton.setMnemonic('b');
			browseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					homeDirField.setText(browseForMatLabDirectory());
				}
			});
			
			add(browseButton);
			
			// TODO: verify the directory
//			homeDirField.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if valid setComplete(true);
//					else setComplete(false);
//				}
//			});
			
			setComplete(true);
		}
		
		/**
		 * Method to browse for the matlab directory
		 * @return a string of the path for the matlab directory
		 */
		public String browseForMatLabDirectory() {
			JFileChooser chooser = new JFileChooser(new File(DEFAULT_LOCATION));	
			chooser.showOpenDialog(this);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			File homeDir = chooser.getSelectedFile();
			
			String directory;
			if (homeDir != null) {
				directory = homeDir.getAbsolutePath();
			} else {
				directory = homeDirField.getText();
			}
			CoreJMatLink.setLibPath(directory);
			if (!directory.endsWith(SystemConstants.DIR_SEPARATOR)) {
				directory += SystemConstants.DIR_SEPARATOR;
			}
			
			return directory;
		}
		
		public String getMatLabInstallHome() {
			return homeDirField.getText();
		}
	}
	
	private static final String MatLab_HOME = "./MATLAB/";
	
	private StaticModel wizardModel;
	private DataSetRegistry loggingRegistry;
	private MessageCenter LOG = MessageCenter.getMessageCenter(MatlabWizard.class);
	private FileSinkChooserStep outputterStep;
	private BrowseForMatLabHomeStep homeStep;

	private boolean skipFirstStep;
	
	/**
	 * Constructor for MatlabWizard
	 * @param loggingRegistry the loggin registry
	 * @param showCopyright to show copyright or not
	 * @param browseForMatLabHome to browse for matlab home or not
	 */
	public MatlabWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForMatLabHome) {
		super();

		this.loggingRegistry = loggingRegistry;
		this.wizardModel = new StaticModel();

		setupWizard(showCopyright, browseForMatLabHome);
	}

	private void setupWizard(boolean showCopyright, boolean browseForRHome) {
		if (showCopyright) {
			addCopyRightStep();
		}
		addBrowseForMatLabHomeStep();
		
		addSelectOutputterStep();
		if (!browseForRHome && !showCopyright) {
			// advance past the browse for R home step
			skipFirstStep = true;
		}
	}

	private void addCopyRightStep() {
		wizardModel.add(new CopyRightStep());
	}

	private void addBrowseForMatLabHomeStep() {
		homeStep = new BrowseForMatLabHomeStep();
		wizardModel.add(homeStep);
	}

	private void addSelectOutputterStep() {
		outputterStep = new FileSinkChooserStep(loggingRegistry.fileSinks(), true,
				"Select the outputter(s) to pass to MatLab",
				"<HTML>Please select which file sinks' data you would "
						+ "like to send to MatLab<BR>");
		wizardModel.add(outputterStep);
		
	}

	
	
	/**
	 * Method to get prepare file name for matlab
	 * @param fileName the string file name
	 * @return a string with the file name
	 */
	public String prepFileNameForMatLab(String fileName) {
		char[] newFName=new char[fileName.length()];
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if(c==' ') {
				c='_';
				
			}
			newFName[i]=c;
		}
		fileName=String.copyValueOf(newFName);
		return fileName;
	}
	

	/**
	 * Method to get the location of matlab
	 * @return matlab's home directory
	 */
	public String getMatLabInstallHome() {
		String home;
		if (homeStep == null) {
			home = BrowseForMatLabHomeStep.DEFAULT_LOCATION;
		} else {
			home = homeStep.getMatLabInstallHome();
		}
		if (!home.endsWith(SystemConstants.DIR_SEPARATOR)) {
			return home.concat(SystemConstants.DIR_SEPARATOR);
		} else {
			return home;
		}
	}
	
	/**
	 * Method to get the matlab outputter
	 * @return a string with the filename
	 */
	public String getMatLabOutputter() {
		List<FileDataSink> outputters = outputterStep.getChosenOutputters();
		String fNames="";
		
		FileDataSink fo = outputters.get(0);
		String delimiter = fo.getFormatter().getDelimiter();
		delimiter="\t";
		fo.getFormatter().setDelimiter(delimiter);
		fNames=System.getProperty("user.dir")+"\\"+fo.getFile().getName();
		fNames=prepFileNameForMatLab(fNames);
		
		return fNames;
	}

	/**
	 * Shows the wizard in a modal dialog.
	 * 
	 * @return if the wizard was was completed (true) or canceled (false).
	 */
	public boolean showDialogModal() {
		Wizard wizard = new PluginWizard(wizardModel);
		wizard.setOverviewVisible(false);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		
		if (skipFirstStep) {
			// advance past the browse for MatLab home step
			wizardModel.nextStep();
		}
		
		wizard.showInDialog("MatLab", null, true);
		
		if (!wizard.wasCanceled()) {
			Settings.put(BrowseForMatLabHomeStep.M_INSTALL_HOME_KEY, homeStep.getMatLabInstallHome());
		}
		
		return !wizard.wasCanceled();
	}

	public void setMatlabHome(String settingsRHome) {
		homeStep.homeDirField.setText(settingsRHome);
	}
	
	public void clearOutputters() {
		outputterStep.getChosenOutputters().clear();
	}
	
	
}
