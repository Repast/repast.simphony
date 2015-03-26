package repast.simphony.chart2.wizard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.data2.TickCountDataSource;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 */

/**
 * General Step for creating time series.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class TimeSeriesGeneralStep extends PluginWizardStep {

  private TimeSeriesWizardModel model;

  private JTextField idField;
  private JComboBox dataSetBox ;
  private JLabel dataSetLabel;

  public TimeSeriesGeneralStep() {
    super("General Settings",
        "Please enter a chart name and the data set to provide the chart's data");
  }
  
  @Override
	protected JPanel getContentPanel(){
  	idField = new JTextField();
  	dataSetBox = new JComboBox(new DefaultComboBoxModel());
  	
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    builder.append("Name:", idField);
    builder.nextLine();

    dataSetBox.setRenderer(new DataSetRenderer());
    dataSetLabel = builder.append("Data Set:", dataSetBox);
    
    addListeners();
    return builder.getPanel();
  }
  
  public void disableDataSelection() {
    dataSetBox.setEnabled(false);
    dataSetLabel.setEnabled(false);
  }

  private void addListeners() {
    idField.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void changedUpdate(DocumentEvent arg0) {
        setComplete(idField.getText().trim().length() > 0);
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
        setComplete(idField.getText().trim().length() > 0);
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        setComplete(idField.getText().trim().length() > 0);
      }
    });

  
  }
  
  private boolean includesTickDataSource(DataSetDescriptor dd) {
    for (String sourceId : dd.dataSourceIds()) {
      if (sourceId.equals(TickCountDataSource.ID)) return true;
    }
    
    return false;
  }

  public void init(WizardModel wizardModel) {
    this.model = (TimeSeriesWizardModel) wizardModel;
    List<DataSetDescriptor> list = model.getDataSets();
    DefaultComboBoxModel boxModel = (DefaultComboBoxModel) dataSetBox.getModel();
    boxModel.removeAllElements();
    for (DataSetDescriptor dd : list) {
      if (dd.includeTick() || includesTickDataSource(dd)) boxModel.addElement(dd);
    }
    
    dataSetBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED &&
            !((DataSetDescriptor)dataSetBox.getSelectedItem()).getName().equals(model.getDescriptor().getDataSet())) {
          
          TimeSeriesChartDescriptor descriptor = model.getDescriptor();
          descriptor.clearSeriesIds();
          descriptor.setDataValueId(null);
        }
      }
    });
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
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    idField.setText(descriptor.getName());
    String dsName = descriptor.getDataSet();
    if (dsName != null) {
      dataSetBox.setSelectedItem(findDSByName(dsName));
    } else {
      if (dataSetBox.getModel().getSize() > 0) dataSetBox.setSelectedIndex(0);
    }
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    descriptor.setName(idField.getText().trim());
    descriptor.setDataSet(((DataSetDescriptor)dataSetBox.getSelectedItem()).getName());
  }
}
