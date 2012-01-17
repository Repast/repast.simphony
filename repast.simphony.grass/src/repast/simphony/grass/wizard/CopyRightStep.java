/**
 * 
 */
package repast.simphony.grass.wizard;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.pietschy.wizard.PanelWizardStep;

import simphony.util.messages.MessageCenter;

/**
 * The step used for copyright and finding Grass installation.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */

public class CopyRightStep extends PanelWizardStep{
	
	/**The error message log for the object*/
	private MessageCenter LOG = MessageCenter.getMessageCenter(CopyRightStep.class);
	
	private static final long serialVersionUID = -1322640934707656871L;
	
	/**
	 * Default constructor.
	 */
	public CopyRightStep() {
		super("Grass 6.3 Wizard for Repast Simphony",
				"This plugin wizard will only work if you have Grass 6.3 / 6.4 installed");
		
		setupPanel();
	}

	/**
	 * Setup the copyright display panel.
	 */
	private void setupPanel() {
		JTextArea license = buildLicenseArea();
		
		JScrollPane scrollPane = new JScrollPane(license);
		scrollPane.setPreferredSize(new Dimension(560, 200));
		
		add(scrollPane);
		
		setComplete(true);
	}
	
	/**
	 * Show the license text.
	 * @return a text area
	 */
	private JTextArea buildLicenseArea() {
		JTextArea area = new JTextArea();
		area.setEditable(false);
		
		area.setText(getLicenseText());
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		area.setCaretPosition(0);

		return area;
	}
	

	
	/**
	 * Method to get the license file used. 
	 * @return a string text file
	 */
	private String getLicenseText() {
	  InputStream stream = this.getClass().getResourceAsStream("License.txt");
		StringBuffer buffer = new StringBuffer();
		
		try {
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line = reader.readLine();

			while (line != null) {
				buffer.append(line).append("\n");
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			buffer.append("There was an error loading the license file");
			LOG.error("CopyRightStep.getLicenseText: Error loading the Grass license file", e);
			
		}
		
		return buffer.toString();
	}

}
