package repast.simphony.data.analysis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import repast.simphony.ui.plugin.editor.PluginWizardStep;
import simphony.util.messages.MessageCenter;

/**
 * Wizard step for optionally showing copyright and licensing information
 * for third party command line plugins
 * 
 * @author Eric Tatara
 * @author Jerry Vos
 */

public class CopyRightStep extends PluginWizardStep {
	protected static final MessageCenter LOG	= MessageCenter.getMessageCenter(AnalysisPluginWizard.class);

  protected JTextArea license;
	
	public CopyRightStep(String name, InputStream stream) {
		super(name + "'s license", name + " is a tool external to Repast and is " +
		"under a different license which is shown below.");
		
		buildLicenseArea(stream);
		
		setComplete(true);
	}

	@Override
	protected  JPanel getContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		license = new JTextArea();

		JScrollPane scrollPane = new JScrollPane(license);
		panel.add(scrollPane);

		return panel;
	}

	private void buildLicenseArea(InputStream stream) {
		license.setEditable(false);

		license.setText(getLicenseText(stream));
		license.setFont(new Font("Monospaced", Font.PLAIN, 12));

		license.setCaretPosition(0);
	}

	private String getLicenseText(InputStream stream) {
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