package repast.simphony.data2.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.data2.engine.AbstractTextSinkDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.ui.plugin.editor.ListSelector;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Data configure step for creating sinks.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class SinkDataStep<D extends AbstractTextSinkDescriptor, 
		T extends DataSetModel<D>> extends PluginWizardStep {

  private T model;
  
  private JTextField nameFld ;
  private JComboBox dataSetBox ;
  private ListSelector<String> dsList;

  public SinkDataStep(String title, String summary) {
    super(title, summary);
  }
  
  @Override
	protected JPanel getContentPanel(){ 
  	nameFld = new JTextField();
  	dataSetBox = new JComboBox(new DefaultComboBoxModel());
  	dsList = new ListSelector<String>();
  	
    FormLayout layout = new FormLayout(
    		"left:pref, 3dlu, pref:grow", 
    		"");
    DefaultFormBuilder fBuilder = new DefaultFormBuilder(layout);
    fBuilder.append("Name:", nameFld);

    layout = new FormLayout(
    		"4dlu, left:pref, 3dlu, pref:grow", 
    		"pref, 5dlu, pref, 3dlu, pref, 4dlu, top:pref:grow");
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
    builder.add(fBuilder.getPanel(), cc.xyw(1, 1, 4));
    builder.addSeparator("Data Properties", cc.xyw(1, 3, 4));
    builder.addLabel("Data Set ID:", cc.xy(2, 5));
    builder.add(dataSetBox, cc.xy(4, 5));
    builder.add(dsList, cc.xyw(2, 7, 3));
 
    dataSetBox.setRenderer(new DataSetRenderer());
    addListeners();
    
    return builder.getPanel();
  }

  private void addListeners() {
    dataSetBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
          updateDataSources();
          dsList.getTargetListModel().clear();
        }
      }
    });
    
    dsList.getTargetListModel().addListDataListener(new ListDataListener() {
      @Override
      public void contentsChanged(ListDataEvent arg0) {
        checkCompleted();
      }

      @Override
      public void intervalAdded(ListDataEvent arg0) {
        checkCompleted();
      }

      @Override
      public void intervalRemoved(ListDataEvent arg0) {
        checkCompleted();
      }
    });
    
    nameFld.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent arg0) {
        checkCompleted();
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
        checkCompleted();
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        checkCompleted();
      }
    });
  }
  
  private void checkCompleted() {
    setComplete(nameFld.getText().trim().length() > 0 && dsList.getTargetListModel().size() > 0);
  }
  
  private void updateDataSources() {
    DataSetDescriptor dsDescriptor = (DataSetDescriptor) dataSetBox.getSelectedItem();
    DefaultListModel listModel = dsList.getSourceListModel();
    listModel.clear();
    for (String id : dsDescriptor.dataSourceIds()) {
      listModel.addElement(id);
    }
  }
  
  @SuppressWarnings("unchecked")
  public void init(WizardModel wizardModel) {
    this.model = (T) wizardModel;
    List<DataSetDescriptor> list = model.getDataSets();
    DefaultComboBoxModel boxModel = (DefaultComboBoxModel) dataSetBox.getModel();
    boxModel.removeAllElements();
    for (DataSetDescriptor dd : list) {
      boxModel.addElement(dd);
    }
  }
  
  private DataSetDescriptor findDSByName(String name) {
    for (DataSetDescriptor ds : model.getDataSets()) {
      if (ds.getName().equals(name)) {
        return ds;
      }
    }
    
    return null;
  }

  public void prepare() {
    super.prepare();
    D descriptor = (D)model.getDescriptor();
    String dsName = descriptor.getDataSet();
    if (dsName != null) {
      dataSetBox.setSelectedItem(findDSByName(dsName));
    }
    
    DefaultListModel selectedModel = dsList.getTargetListModel();
    DefaultListModel unselectedModel = dsList.getSourceListModel();
    selectedModel.clear();
    for (String source : descriptor.getSourceIds()) {
      selectedModel.addElement(source);
      unselectedModel.removeElement(source);
    }
    
    nameFld.setText(descriptor.getName());
    checkCompleted();
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    D descriptor = (D)model.getDescriptor();
    descriptor.setName(nameFld.getText().trim());
    descriptor.setDataSet(((DataSetDescriptor)dataSetBox.getSelectedItem()).getName());
    DefaultListModel unselectedModel = dsList.getSourceListModel();
    for (int i = 0; i < unselectedModel.getSize(); ++i) {
      descriptor.removeSourceId(unselectedModel.getElementAt(i).toString());
    }
    for (String id : dsList.getSelectedItems()) {
      descriptor.addSourceId(id);
    }
  }
}

@SuppressWarnings("serial")
class DataSetRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
    
    if (value != null) {
      value = ((DataSetDescriptor)value).getName();
    }
    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
  }
  
}
