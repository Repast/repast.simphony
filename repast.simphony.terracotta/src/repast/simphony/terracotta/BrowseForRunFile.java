/**
 * 
 */
package repast.simphony.terracotta;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import javax.swing.JButton;
import javax.swing.JTextField;

import org.pietschy.wizard.PanelWizardStep;


import repast.simphony.util.Settings;
import repast.simphony.util.SystemConstants;
import saf.core.ui.util.FileChooserUtilities;

/**
 * The step chooser for setting up Terracotta run step
 * that searches for the run script file launching Terracotta.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 *
 */
public class BrowseForRunFile extends PanelWizardStep {
	private static final long serialVersionUID = -3143902886682424228L;

	/**The default install key*/
	public static String T_INSTALL_HOME_KEY="repast.simphony.terracotta.TerracottaHomeInstall";;
	
	/**The default install location*/
	private static final String DEFAULT_LOCATION = "C:\\Program Files\\Terracotta\\terracotta-2.4.5\\workspace\run.bat";
	
	/**The text field home directory*/
	public static  JTextField HOME_DIR_FIELD=null;
	
	/**
	 * Default constructor.
	 */
	public BrowseForRunFile() {
		super("Terracotta home", "<HTML>Please browse to Terracotta's run file script.<BR>" +
				"The default run file script location for Terracotta is " + DEFAULT_LOCATION);
		
		setupPanel();
	}
	
	/**
	 * Method for setting up the panel in this step.
	 */
	private void setupPanel() {
		String home = (String) Settings.get(T_INSTALL_HOME_KEY);
		if (home == null) {
			home = DEFAULT_LOCATION;
		}
		
		HOME_DIR_FIELD = new JTextField(home);
		HOME_DIR_FIELD.setPreferredSize(new Dimension(400, 20));
		add(HOME_DIR_FIELD);
		
		JButton browseButton = new JButton("Browse");
		browseButton.setMnemonic('b');
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HOME_DIR_FIELD.setText(browseForTerracottaRunFile());
			}
		});
		
		add(browseButton);
		
	
		setComplete(true);
	}
	
	/**
	 * Method to browse for the Terracotta run file.
	 * @return a string of the path for the Terracotta run file
	 */
	public String browseForTerracottaRunFile() {
		File rHome = FileChooserUtilities.getOpenFile(new File(DEFAULT_LOCATION));


		String directory;
		if (rHome != null) {
			directory = rHome.getAbsolutePath();
		} else {
			directory = HOME_DIR_FIELD.getText();
		}
		
		if (!directory.endsWith(SystemConstants.DIR_SEPARATOR)) {
			directory += SystemConstants.DIR_SEPARATOR;
		}
		
		return directory;
	}
	
	public String getTerracottaInstallHome() {
		return HOME_DIR_FIELD.getText();
	}
}
