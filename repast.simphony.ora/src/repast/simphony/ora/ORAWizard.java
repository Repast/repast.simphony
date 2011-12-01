package repast.simphony.ora;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.data.analysis.NetworkAnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.ORANetWriter;

/**
 * A wizard for executing ORA on a file outputter's output.
 * 
 * @author Michael J. North
 * @author Eric Tatara
 * 
 */
public class ORAWizard extends NetworkAnalysisPluginWizard {

	public ORAWizard() {
		super(".xml");
	}

	public ORAWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForHome, String name, String installHome,
			String defaultLocation, String licenseFileName) {

		super(loggingRegistry, showCopyright, browseForHome, name, installHome,
				defaultLocation, licenseFileName, ".xml");

	}

	private String createORAFile(Network network, String baseFileName)
			throws FileNotFoundException, IOException {

		String preferencesFileName = null;
		ORANetWriter writer = new ORANetWriter();

		if (network instanceof JungNetwork) {
			JungNetwork jungNetwork = (JungNetwork) network;
			preferencesFileName = writer.save(jungNetwork.getName(),
					jungNetwork.getGraph(), baseFileName);
		} else if (network instanceof ContextJungNetwork) {
			ContextJungNetwork contextJungNetwork = (ContextJungNetwork) network;
			preferencesFileName = writer.save(contextJungNetwork.getName(),
					contextJungNetwork.getGraph(), baseFileName);
		}

		return preferencesFileName;

	}

	@Override
	public String[] getExecutionCommand() {

		List<String> commands = new ArrayList<String>();
		commands.add(getExecutableLoc());

		List<Network> networks = networkStep.getChosenNetworks();
		for (int i = 0; i < networks.size(); i++) {
			try {
				String preferencesFileName = createORAFile(networks.get(i),
						this.getFileName());
				if (preferencesFileName != null) {
					commands.add(preferencesFileName);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error" + e.getMessage());
			}

		}

		return commands.toArray(new String[commands.size()]);

	}

	private String getExecutableLoc() {
		String home = getInstallHome();
		if (!home.endsWith(File.separator))
			home += File.separator;
		return home + "ORA";
	}

	public String getCannotRunMessage() {
		return "An output file or files have been created, but *ORA itself can only be executed under Windows.";
	}

	public String getDefaultFileExtension() {
		return "xml";
	}

}
