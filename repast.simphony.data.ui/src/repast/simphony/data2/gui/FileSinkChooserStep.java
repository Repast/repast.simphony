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

import repast.simphony.data2.FileDataSink;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * Wizard step for selecting file sink entries that are passed to data analysis plugins.
 * 
 * @author Eric Tatara
 *
 */
public class FileSinkChooserStep extends PluginWizardStep {
  private static final long serialVersionUID = 1L;

  private List<FileDataSink> sinks;
  private JList outputterList;

  @SuppressWarnings("unchecked")
  public FileSinkChooserStep(Iterable<FileDataSink> sinks, boolean multiSelect,
      String title, String message) {
    super(title, message);

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

    Vector<String> names = new Vector<String>();

    for (FileDataSink fs : sinks) {
      names.add(fs.getName());
    }
    
    outputterList.setListData(names);
    if (!multiSelect) {
      outputterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    } else {
      outputterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    if (this.sinks.size() > 0) {
      outputterList.setSelectedIndex(0);
    }
  }

  @Override
	protected JPanel getContentPanel(){
		JPanel panel = new JPanel(new BorderLayout());
    outputterList = new JList();
    outputterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane scrollPane = new JScrollPane(outputterList);
    panel.add(scrollPane);

    outputterList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (outputterList.getSelectedIndex() != -1) {
          setComplete(true);
        } else {
          setComplete(false);
        }
      }
    });
    
    return panel;
  }

  public ArrayList<FileDataSink> getChosenOutputters() {
    ArrayList<FileDataSink> chosenOutputters = new ArrayList<FileDataSink>();

    for (int index : outputterList.getSelectedIndices()) {
      chosenOutputters.add(sinks.get(index));
    }

    return chosenOutputters;
  }

}