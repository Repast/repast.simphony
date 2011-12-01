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

import repast.simphony.data2.FileDataSink;

public class FileSinkChooserStep extends PanelWizardStep {
  private static final long serialVersionUID = 1L;

  private List<FileDataSink> sinks;

  private JList outputterList;

  private boolean multiSelect;

  @SuppressWarnings("unchecked")
  public FileSinkChooserStep(Iterable<FileDataSink> sinks, boolean multiSelect,
      String title, String message) {
    super(title, message);

    this.multiSelect = multiSelect;
    if (sinks instanceof List) {
      this.sinks = (List<FileDataSink>) sinks;
    } else {
      this.sinks = new ArrayList<FileDataSink>();
      // Need a temporary way to copy the outputters so I use this handle
      List tmpHandle = this.sinks;
      for (FileDataSink fs : sinks) {
        tmpHandle.add(fs);
      }
    }

    setupPanel();
  }

  private void setupPanel() {
    Vector<String> names = new Vector<String>();

    for (FileDataSink fs : sinks) {
      names.add(fs.getName());
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
    if (sinks.size() > 0) {
      outputterList.setSelectedIndex(0);
    }
  }

  public ArrayList<FileDataSink> getChosenOutputters() {
    ArrayList<FileDataSink> chosenOutputters = new ArrayList<FileDataSink>();

    for (int index : outputterList.getSelectedIndices()) {
      chosenOutputters.add(sinks.get(index));
    }

    return chosenOutputters;
  }

}