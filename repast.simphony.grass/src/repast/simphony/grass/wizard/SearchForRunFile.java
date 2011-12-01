/**
 * 
 */
package repast.simphony.grass.wizard;

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
 * The step chooser for setting up Grass 6.3 run step
 * that searches for the run script file launching Grass.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class SearchForRunFile extends PanelWizardStep {
	
	private static final long serialVersionUID = -3143902886682424228L;

	/**The Grass install key*/
	public static String GRASS_INSTALL_HOME_KEY="repast.simphony.grass.GrassHomeInstall";
	
	/**The mac and non-windows default home location of Grass*/
	public static final String DEFAULT_LOCATION_MAC = "/Applications/GRASS-6.3.app/Contents/MacOS/grass.sh";
	
	/**The windows default location*/
	public static final String DEFAULT_LOCATION_WIN = "C:/Program Files/GRASS-6.3.app/Contents/WindowsOS/grass.bat";
	
	/**The text field for the home directory*/
	public static  JTextField HOME_DIR_FIELD=null;
	
	/**The default location*/
	private String defaultLocation;
	
	/**
	 * Default constructor.
	 */
	public SearchForRunFile(String defaultLocation) {	
		super("Grass 6.3 home", "<HTML>Please browse for Grass' run file script.</HTML>");
		
		this.defaultLocation=defaultLocation;
		setupPanel();
	}
	
	/**
	 * Method for setting up the panel in this step.
	 */
	private void setupPanel() {
		String home = (String) Settings.get(GRASS_INSTALL_HOME_KEY);
		if (home == null) {
			home = defaultLocation;
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
		File rHome = FileChooserUtilities.getOpenFile(new File(defaultLocation));


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
