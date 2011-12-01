package repast.simphony.data.analysis;

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
 * Wizard step for optionally showing copyright and licensing information
 * for third party command line plugins
 * 
 * @author Eric Tatara
 * @author Jerry Vos
 */

public class CopyRightStep extends PanelWizardStep {
	protected static final MessageCenter LOG	= MessageCenter.getMessageCenter(AnalysisPluginWizard.class);

	protected InputStream stream;

	public CopyRightStep(String name, InputStream stream) {
		super(name + "'s license", name + " is a tool external to Repast and is " +
		"under a different license which is shown below.");

		this.stream = stream;

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
		JTextArea licenseArea = new JTextArea();
		licenseArea.setEditable(false);

		licenseArea.setText(getLicenseText());
		licenseArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		licenseArea.setCaretPosition(0);

		return licenseArea;
	}

	private String getLicenseText() {
		StringBuffer buffer = new StringBuffer(18000);

		if (stream != null){
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String line = reader.readLine();

				while (line != null) {
					buffer.append(line).append("\n");
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				buffer.append("There was an error loading the license file. ");
				LOG.error("CopyRightStep.getLicenseText: Error loading the license file", e);
			}

		}
		else{
			buffer.append( "License information not found.  Please check with the third party software \n" +
			"documentation for license information.");
			LOG.error("CopyRightStep.getLicenseText: Error loading the license file",null);
		}
		return buffer.toString();
	}
}
