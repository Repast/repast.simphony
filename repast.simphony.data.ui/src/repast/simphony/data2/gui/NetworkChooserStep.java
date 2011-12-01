/*CopyrightHere*/
package repast.simphony.data2.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.PanelWizardStep;

import repast.simphony.space.graph.Network;

public class NetworkChooserStep extends PanelWizardStep {
	private static final long serialVersionUID = 1L;

	private List<Network> networks;

	private JList networkList;

	public List<Network> getNetworks() {
		return networks;
	}

	private boolean multiSelect;

	@SuppressWarnings("unchecked")
	public NetworkChooserStep(Iterable<Network> networks, boolean multiSelect,
			String title, String message) {
		super(title, message);

		this.multiSelect = multiSelect;
		if (networks instanceof List) {
			this.networks = (List<Network>) networks;
		} else {
			this.networks = new ArrayList<Network>();
			// Need a temporary way to copy the networks so I use this handle
			List tmpHandle = this.networks;
			for (Network network : networks) {
				tmpHandle.add(network);
			}
		}

		setupPanel();
	}

	private void setupPanel() {
		Vector<String> names = new Vector<String>();

		for (Network network : networks) {
			names.add(network.getName());
		}

		networkList = new JList(names);
		networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(networkList);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		add(scrollPane);

		networkList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (networkList.getSelectedIndex() != -1) {
					setComplete(true);
				} else {
					setComplete(false);
				}
			}
		});
		if (!multiSelect) {
			networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		} else {
			networkList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		if (networks.size() > 0) {
			networkList.setSelectedIndex(0);
		}
	}

	public ArrayList<Network> getChosenNetworks() {
		ArrayList<Network> chosenNetworks = new ArrayList<Network>();

		for (int index : networkList.getSelectedIndices()) {
			chosenNetworks.add(networks.get(index));
		}

		return chosenNetworks;
	}

}