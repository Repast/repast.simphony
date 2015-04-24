package repast.simphony.data.analysis;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.data2.gui.FileChooserStep;
import repast.simphony.data2.gui.NetworkChooserStep;
import repast.simphony.engine.environment.RunState;
import repast.simphony.space.graph.Network;
import simphony.util.messages.MessageCenter;

/**
 * A wizard for executing a third party analysis tool on a file outputter's
 * output.
 * 
 * @author Michael J. North
 * @author Eric Tatara
 * @author Jerry Vos
 */
public abstract class NetworkAnalysisPluginWizard extends AnalysisPluginWizard {
	protected static final MessageCenter LOG = MessageCenter
			.getMessageCenter(AnalysisPluginWizard.class);

	protected String defaultFileExtension = "";

	public abstract String getDefaultFileExtension();

	public String getFileName() {
		if (this.fileStep == null) {
			return "";
		} else {
			return this.fileStep.getFileName();
		}
	}

	protected NetworkChooserStep networkStep;
	protected FileChooserStep fileStep;

	@Override
	protected void setupWizard(boolean showCopyright, boolean browseForHome) {		
		if (showCopyright) {
			addCopyRightStep();
		}
		addBrowseForHomeStep();

		addSelectNetworkStep();
		if (!browseForHome && !showCopyright) {
			// advance past the browse for home step
			skipFirstStep = true;
		}
		addSelectFileNameStep();
	}

	private void addSelectNetworkStep() {
		networkStep = new NetworkChooserStep(getNetworks(), false,
				"Select the network to pass to " + name,
				"<HTML>Please select which network's data you would "
						+ "like to send to " + name + ".<BR>");
		wizardModel.add(networkStep);
	}

	private void addSelectFileNameStep() {
		fileStep = new FileChooserStep("Main File Name",
				"Enter the main file name", getDefaultFileExtension());
		wizardModel.add(fileStep);
	}

	private List<Network> getNetworks() {
		ArrayList<Network> networks = new ArrayList<Network>();
		Context currentContext = RunState.getInstance().getMasterContext();
		this.getNetworks(networks, currentContext);
		return networks;
	}

	private void getNetworks(List<Network> networks, Context currentContext) {
		for (Object projection : currentContext.getProjections()) {
			if (projection instanceof Network) {
				networks.add((Network) projection);
			}
		}
		for (Object context : currentContext.getSubContexts()) {
			if (context instanceof Context) {
				this.getNetworks(networks, (Context) context);
			}
		}
	}
}