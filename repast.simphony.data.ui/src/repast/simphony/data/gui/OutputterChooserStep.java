/*CopyrightHere*/
package repast.simphony.data.gui;

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

import repast.simphony.data.logging.outputter.Outputter;

public class OutputterChooserStep<T extends Outputter> extends PanelWizardStep {
	private static final long serialVersionUID = 1L;

	private List<T> outputters;

	private JList outputterList;

	private boolean multiSelect;

	@SuppressWarnings("unchecked")
	public OutputterChooserStep(Iterable<T> outputters, boolean multiSelect,
			String title, String message) {
		super(title, message);

		this.multiSelect = multiSelect;
		if (outputters instanceof List) {
			this.outputters = (List<T>) outputters;
		} else {
			this.outputters = new ArrayList<T>();
			// Need a temporary way to copy the outputters so I use this handle
			List tmpHandle = this.outputters;
			for (Outputter outputter : outputters) {
				tmpHandle.add(outputter);
			}
		}

		setupPanel();
	}

	private void setupPanel() {
		Vector<String> names = new Vector<String>();

		for (Outputter outputter : outputters) {
			names.add(outputter.getOutputterName());
		}

		outputterList = new JList(names);
		outputterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(outputterList);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		add(scrollPane);

		outputterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (outputterList.getSelectedIndex() != -1) {
					setComplete(true);
				} else {
					setComplete(false);
				}
			}
		});
		if (!multiSelect) {
			outputterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		} else {
			outputterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		if (outputters.size() > 0) {
			outputterList.setSelectedIndex(0);
		}
	}

	public ArrayList<T> getChosenOutputters() {
		ArrayList<T> chosenOutputters = new ArrayList<T>();

		for (int index : outputterList.getSelectedIndices()) {
			chosenOutputters.add(outputters.get(index));
		}

		return chosenOutputters;
	}

}