package repast.simphony.data2.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import repast.simphony.space.graph.Network;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * Wizard step for selecting network entries that are passed to data analysis plugins.
 * 
 * @author Eric Tatara
 *
 */
public class NetworkChooserStep extends PluginWizardStep {
	private List<Network> networks;
	private JList networkList;

	public List<Network> getNetworks() {
		return networks;
	}

	@SuppressWarnings("unchecked")
	public NetworkChooserStep(Iterable<Network> networks, boolean multiSelect,
			String title, String message) {
		super(title, message);

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
		
		Vector<String> names = new Vector<String>();

		for (Network network : networks) {
			names.add(network.getName());
		}
		networkList.setListData(names);

		if (!multiSelect) {
			networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		} else {
			networkList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		if (this.networks.size() > 0) {
			networkList.setSelectedIndex(0);
		}
	}

	@Override
	protected JPanel getContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		
		networkList = new JList();
		networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(networkList);
		panel.add(scrollPane);

		networkList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (networkList.getSelectedIndex() != -1) {
					setComplete(true);
				} else {
					setComplete(false);
				}
			}
		});
		
		return panel;
	}

	public ArrayList<Network> getChosenNetworks() {
		ArrayList<Network> chosenNetworks = new ArrayList<Network>();

		for (int index : networkList.getSelectedIndices()) {
			chosenNetworks.add(networks.get(index));
		}

		return chosenNetworks;
	}

}