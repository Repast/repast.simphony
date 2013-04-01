package repast.simphony.chart2.wizard;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.data2.engine.CustomDataSourceDefinition;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.data2.engine.MethodDataSourceDefinition;
import repast.simphony.data2.util.AggregateFilter;
import simphony.util.messages.MessageCenter;

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
public class HistogramDataStep extends PanelWizardStep {

  private static MessageCenter msgCenter = MessageCenter.getMessageCenter(HistogramDataStep.class);

  private HistogramWizardModel model;

  private JTextField idField = new JTextField();
  private JComboBox dataSetBox = new JComboBox(new DefaultComboBoxModel());
  private JComboBox dataSourceBox = new JComboBox(new DefaultComboBoxModel());
  private JLabel dataSetLabel, dataLabel;

  private Map<String, List<String>> dataSources = new HashMap<String, List<String>>();
  // we use an aggregate filter because it allows for only numeric
  // and possibly numeric objects.
  private AggregateFilter filter = new AggregateFilter();

  public HistogramDataStep() {
    super("Data Settings", "Please enter a chart name and select the data to histogram");
    this.setLayout(new BorderLayout());

    FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.border(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    builder.append("Name:", idField);
    builder.nextLine();

    dataSetLabel = builder.append("Data Set:", dataSetBox);

    builder.nextLine();
    dataLabel = builder.append("Data To Histogram:", dataSourceBox);
    add(builder.getPanel(), BorderLayout.CENTER);

    addListeners();
  }
  
  public void disableDataSelection() {
    dataSetBox.setEnabled(false);
    dataSetLabel.setEnabled(false);
    dataLabel.setEnabled(false);
    dataSourceBox.setEnabled(false);
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

  private void addDataSource(String dataSet, String dataSource) {
    List<String> sources = dataSources.get(dataSet);
    if (sources == null) {
      sources = new ArrayList<String>();
      dataSources.put(dataSet, sources);
    }
    sources.add(dataSource);
  }

  private void initDataSources() {
    List<DataSetDescriptor> list = model.getDataSets();
    for (DataSetDescriptor data : list) {
      if (data.getType() == DataSetType.NON_AGGREGATE) {

        for (MethodDataSourceDefinition def : data.methodDataSources()) {
          if (isNumeric(def)) {
            addDataSource(data.getName(), def.getId());
          }
        }

        for (CustomDataSourceDefinition def : data.customNonAggDataSources()) {
          if (isNumeric(def)) {
            addDataSource(data.getName(), def.getId());
          }
        }
      }
    }

    for (List<String> sList : dataSources.values()) {
      Collections.sort(sList);
    }
  }

  public void init(WizardModel wizardModel) {
    this.model = (HistogramWizardModel) wizardModel;
    initDataSources();

    DefaultComboBoxModel boxModel = (DefaultComboBoxModel) dataSetBox.getModel();
    boxModel.removeAllElements();
    List<String> list = new ArrayList<String>(dataSources.keySet());
    Collections.sort(list);
    for (String ds : list) {
      boxModel.addElement(ds);
    }

    dataSetBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
          updateDataSourceBox();
        }
      }
    });

    if (dataSources.size() > 0) {
      updateDataSourceBox();
    }
  }

  private void updateDataSourceBox() {
    String dataSet = dataSetBox.getSelectedItem().toString();
    List<String> sources = dataSources.get(dataSet);
    DefaultComboBoxModel boxModel = (DefaultComboBoxModel) dataSourceBox.getModel();
    boxModel.removeAllElements();
    for (String source : sources) {
      boxModel.addElement(source);
    }
  }

  private boolean isNumeric(CustomDataSourceDefinition def) {
    try {
      return filter.check(def);
    } catch (Exception ex) {
      msgCenter.warn("Error while performing isNumeric check on custom data source", ex);
      return false;
    }
  }

  private boolean isNumeric(MethodDataSourceDefinition def) {
    try {
      return filter.check(def);
    } catch (Exception ex) {
      msgCenter.warn("Error while performing isNumeric check on method data source", ex);
      return false;
    }
  }

  public void prepare() {
    super.prepare();
    HistogramChartDescriptor descriptor = model.getDescriptor();
    idField.setText(descriptor.getName());
    String dsName = descriptor.getDataSet();
    if (dsName != null) {
      dataSetBox.setSelectedItem(dsName);
    } else {
      if (dataSetBox.getModel().getSize() > 0) dataSetBox.setSelectedIndex(0);
    }

    String sourceId = descriptor.getSourceId();
    if (sourceId != null) {
      dataSourceBox.setSelectedItem(sourceId);
    }

    setComplete(dataSources.size() > 0);
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    HistogramChartDescriptor descriptor = model.getDescriptor();
    descriptor.setName(idField.getText().trim());
    descriptor.setDataSet(dataSetBox.getSelectedItem().toString());
    descriptor.setSourceId(dataSourceBox.getSelectedItem().toString());
  }
}
