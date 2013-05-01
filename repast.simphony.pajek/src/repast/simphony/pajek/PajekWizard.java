package repast.simphony.pajek;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.data.analysis.NetworkAnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;

/**
 * A wizard for executing Pajek on a file outputter's output.
 * 
 * @author Michael J. North
 * @author Eric Tatara
 * 
 */
public class PajekWizard extends NetworkAnalysisPluginWizard {

	public PajekWizard() {
		super(".net");
		if (!SystemUtils.IS_OS_WINDOWS) {
			this.setRunProgram(false);
		}
	}

	public PajekWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForHome, String name, String installHome,
			String defaultLocation, String licenseFileName) {

		super(loggingRegistry, showCopyright, browseForHome, name, installHome,
				defaultLocation, licenseFileName, ".net");
		if (!SystemUtils.IS_OS_WINDOWS) {
			this.setRunProgram(false);
		}

	}

	private String createPajekFile(Network network, String fileName)
			throws FileNotFoundException, IOException {

		repast.simphony.space.graph.PajekNetWriter writer = new repast.simphony.space.graph.PajekNetWriter();

		if (network instanceof JungNetwork) {
			writer.save(((JungNetwork) network).getGraph(), fileName);
		}
		if (network instanceof ContextJungNetwork) {
			writer.save(((ContextJungNetwork) network).getGraph(), fileName);
		}

		return fileName;

	}

	@Override
	public String[] getExecutionCommand() {

		List<String> commands = new ArrayList<String>();
		commands.add(getExecutableLoc());

		List<Network> networks = networkStep.getChosenNetworks();
		for (int i = 0; i < networks.size(); i++) {
			try {
				createPajekFile(networks.get(i), this.getFileName());
				commands.add(this.getFileName());
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
		return home + "pajek.exe";
	}

	public String getCannotRunMessage() {
		return "An output file or files have been created, but Pajek itself can only be executed under Windows.";
	}
	
}
