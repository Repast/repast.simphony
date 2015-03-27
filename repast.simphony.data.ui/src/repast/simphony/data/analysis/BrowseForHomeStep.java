package repast.simphony.data.analysis;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import repast.simphony.ui.plugin.editor.PluginWizardStep;
import saf.core.ui.util.FileChooserUtilities;

/**
 * Wizard step that provides the location of the data analysis plugin executable.
 * 
 * @author Eric Tatara
 *
 */
public class BrowseForHomeStep extends PluginWizardStep {

	private String defaultLocation;
	
	public JTextField homeDirField;

	public BrowseForHomeStep(String name, String installHome, String defaultLocation) {
		super(name + " home", "<HTML>Please select " + name + "'s executable file.  " +
				"The default installation location for " + name+ " is " + defaultLocation + ".");

		this.defaultLocation = defaultLocation;
		
		if (installHome == null)
		  installHome = defaultLocation;
		
		homeDirField.setText(installHome);
		
		setComplete(true);
	}

	@Override
	protected JPanel getContentPanel(){
		JPanel panel = new JPanel(new FlowLayout());
			
		homeDirField = new JTextField();
		homeDirField.setPreferredSize(new Dimension(400, 20));
		panel.add(homeDirField);

		JButton browseButton = new JButton("Browse");
		browseButton.setMnemonic('b');
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				homeDirField.setText(browseForExecutable());
			}
		});

		panel.add(browseButton);

		return panel;
	}
	
	private String browseForExecutable() {
		File home = FileChooserUtilities.getOpenFile(new File(defaultLocation));

		String directory;
		if (home != null) {
			directory = home.getAbsolutePath();
		} else {
			directory = homeDirField.getText();
		}

		return directory;
	}

	public String getInstallHome() {
		return homeDirField.getText();
	}

	public String getDefaultLocation() {
		return defaultLocation;
	}
}
