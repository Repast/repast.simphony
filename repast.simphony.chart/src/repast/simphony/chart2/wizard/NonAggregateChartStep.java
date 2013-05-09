package repast.simphony.chart2.wizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.data2.engine.CustomDataSourceDefinition;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;
import repast.simphony.data2.util.AggregateFilter;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.ui.widget.ListSelector;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Step for configuring non-aggregate data to be displayed by a chart.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class NonAggregateChartStep extends PanelWizardStep {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(NonAggregateChartStep.class);

  private TimeSeriesWizardModel model;
  private JComboBox seriesBox = new JComboBox(new DefaultComboBoxModel());
  //private JComboBox dataBox = new JComboBox(new DefaultComboBoxModel());
  private ListSelector<String> dataList = new ListSelector<String>();
  
  // we use an aggregate filter because it allows for only numeric
  // and possibly numeric objects.
  private AggregateFilter filter = new AggregateFilter();

  public NonAggregateChartStep() {
    super("Configure Chart Data Properites", "Please select the data to be displayed by the chart.");
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    FormLayout layout = new FormLayout("pref, 3dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.append("Series ID:", seriesBox);
    builder.nextLine();
    builder.append("Data To Display:");
    builder.nextLine();
    builder.append(dataList, 3);
    add(builder.getPanel(), BorderLayout.CENTER);
    
    dataList.addActionListeners(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        setComplete(dataList.getTargetListModel().size() > 0 && seriesBox.getModel().getSize() > 0);
      }
    });
  }

  public void init(WizardModel wizardModel) {
    this.model = (TimeSeriesWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
   
    DataSetDescriptor data = model.getDataSet();
    DefaultComboBoxModel boxModel = (DefaultComboBoxModel) seriesBox.getModel();
    boxModel.removeAllElements();

    for (MethodDataSourceDefinition def : data.methodDataSources()) {
      boxModel.addElement(def.getId());
    }

    for (CustomDataSourceDefinition def : data.customNonAggDataSources()) {
      boxModel.addElement(def.getId());
    }

    if (descriptor.getSeriesIds().size() > 0) {
      boxModel.setSelectedItem(descriptor.getSeriesIds().get(0));
    }

    dataList.getSourceListModel().removeAllElements();
    dataList.getTargetListModel().removeAllElements();

    for (MethodDataSourceDefinition def : data.methodDataSources()) {
      if (isNumeric(def))
        dataList.getSourceListModel().addElement(def.getId());
    }

    for (CustomDataSourceDefinition def : data.customNonAggDataSources()) {
      if (isNumeric(def))
        dataList.getSourceListModel().addElement(def.getId());
    }
    
    for (String id : descriptor.dataValueIds()) {
      dataList.getSourceListModel().removeElement(id);
      dataList.getTargetListModel().addElement(id);
    }
    
    setComplete(dataList.getTargetListModel().size() > 0 && seriesBox.getModel().getSize() > 0);
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

  public void applyState() throws InvalidStateException {
    super.applyState();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    descriptor.clearSeriesIds();
    descriptor.setDataValueId(null);

    descriptor.addSeriesId(seriesBox.getSelectedItem().toString(), "", null);
    for (String id : dataList.getSelectedItems()) {
      descriptor.addDataValueId(id);
    }
  }
}
