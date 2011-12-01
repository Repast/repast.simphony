/*
 * Created by JFormDesigner on Thu Jul 30 10:57:00 EDT 2009
 */

package repast.simphony.batch.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import repast.simphony.parameter.ParameterSchema;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #1
 */
@SuppressWarnings("serial")
public class ParameterEditPanel extends JPanel {

  private static final String CONSTANT_PANEL_INDEX = "0";
  private static final String NUMERIC_PANEL_INDEX = "1";
  private static final String LIST_PANEL_INDEX = "2";

  private Trigger trigger = new Trigger();
  private PresentationModel<BatchParameterBean> adapter;

  private ParameterConstantPanel constantPanel = new ParameterConstantPanel();
  private NumericParameterPanel numPanel = new NumericParameterPanel();
  private ParameterListPanel listPanel = new ParameterListPanel();
  private ParameterEditorMediator mediator;

  private class ParamsRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      if (value != null)
        value = mediator.getParameters().getDisplayName(value.toString());
      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
  }

  public ParameterEditPanel() {
    initComponents();

    pTypePanel.add(constantPanel, CONSTANT_PANEL_INDEX);
    pTypePanel.add(numPanel, NUMERIC_PANEL_INDEX);
    pTypePanel.add(listPanel, LIST_PANEL_INDEX);

    addListeners();
    pTypeBox.setSelectedIndex(0);
  }

  private void addListeners() {
    pTypeBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        CardLayout layout = (CardLayout) pTypePanel.getLayout();
        layout.show(pTypePanel, String.valueOf(pTypeBox.getSelectedIndex()));
      }
    });

    nameBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (mediator != null && nameBox.getSelectedIndex() != -1) {
          ParameterSchema schema = mediator.getParameters().getSchema().getDetails(
              nameBox.getSelectedItem().toString());
          Class<?> type = schema.getType();
          typeFld.setText(type.getSimpleName());
          numPanel.resetDocumentModel(type);
          constantPanel.resetDocumentModel(type);
          
          if (pTypeBox.getSelectedItem() == ParameterType.CONSTANT) {
            constantPanel.setValueFld(mediator.getParameters().getValue(nameBox.getSelectedItem().toString()).toString());
          }
        }
      }
    });

    applyBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        commit();
      }
    });
  }

  public void init(ParameterEditorMediator mediator) {
    // dummy bean to set up the binding
    BatchParameterBean bean = new BatchParameterBean();
    this.mediator = mediator;
    adapter = new PresentationModel<BatchParameterBean>(bean, trigger);
    ValueModel model = adapter.getBufferedModel("name");
    List<String> names = new ArrayList<String>();
    for (String name : mediator.getParameters().getSchema().parameterNames()) {
      names.add(name);
    }

    Collections.sort(names, new Comparator<String>() {
      public int compare(String o1, String o2) {
        return ParameterEditPanel.this.mediator.getParameters().getDisplayName(o1).compareTo(
            ParameterEditPanel.this.mediator.getParameters().getDisplayName(o2));
      }
    });

    ComboBoxAdapter<String> boxAdapter = new ComboBoxAdapter<String>(names, model);
    nameBox.setModel(boxAdapter);
    nameBox.setRenderer(new ParamsRenderer());

    model = adapter.getBufferedModel("parameterType");
    List<ParameterType> vals = new ArrayList<ParameterType>();
    vals.add(ParameterType.CONSTANT);
    vals.add(ParameterType.NUMERIC_RANGE);
    vals.add(ParameterType.LIST);
    ComboBoxAdapter<ParameterType> pTypeAdapter = new ComboBoxAdapter<ParameterType>(vals, model);
    pTypeBox.setModel(pTypeAdapter);
    pTypeBox.setSelectedIndex(0);

    model = adapter.getBufferedModel("type");
    Bindings.bind(typeFld, model);

    numPanel.initBindings(adapter);
    constantPanel.initBindings(adapter);
    listPanel.initBindings(adapter);

    adapter.addPropertyChangeListener("buffering", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        applyBtn.setEnabled((Boolean) evt.getNewValue());
      }
    });

    applyBtn.setEnabled(false);
    nameBox.setSelectedIndex(0);
    // flush buffering
    cancel();
  }

  public void resetBean(BatchParameterBean bean, boolean isNew) {
    if (!isEnabled())
      setEnabled(true);
    adapter.setBean(bean);
    // this clears the buffering on the adapter
    cancel();
    System.out.println(adapter.getBean());
    if (isNew)
      applyBtn.setEnabled(true);
    if (bean.isConstant()) {
      pTypeBox.setSelectedItem(ParameterType.CONSTANT);
      pTypeBox.setEnabled(false);
    } else {
      pTypeBox.setEnabled(true);
    }
    
    listPanel.validate();
    listPanel.invalidate();
    listPanel.repaint();
  }

  public boolean saveChanges() {
    if (adapter.isBuffering()) {
      int result = JOptionPane.showConfirmDialog(this, "The parameter '"
          + mediator.getDisplayNameFor(nameBox.getSelectedItem().toString())
          + "' has unsaved changes. Do you wish to save them?", "", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        return commit();
      } else {
        cancel();
      }
    }
    return true;
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    pTypeBox.setSelectedIndex(0);
    for (int i = 0; i < panel2.getComponentCount(); i++) {
      panel2.getComponent(i).setEnabled(enabled);
    }

    for (int i = 0; i < constantPanel.getComponentCount(); i++) {
      constantPanel.getComponent(i).setEnabled(enabled);
    }
  }

  private boolean validateBean() {
    if (adapter.isBuffering()) {
      // set a tmp bean to the buffered values and validate it
      BatchParameterBean bean = new BatchParameterBean();
      BufferedValueModel model = adapter.getBufferedModel("name");
      bean.setName(model.getValue().toString());
      
      model = adapter.getBufferedModel("type");
      bean.setType(model.getValue().toString());
      
      model = adapter.getBufferedModel("parameterType");
      bean.setParameterType((ParameterType)model.getValue());
      
      model = adapter.getBufferedModel("listItems");
      bean.setListItems(model.getValue().toString());
      
      model = adapter.getBufferedModel("start");
      bean.setStart(model.getValue().toString());
      
      model = adapter.getBufferedModel("end");
      bean.setEnd(model.getValue().toString());
      
      model = adapter.getBufferedModel("step");
      bean.setStep(model.getValue().toString());
      
      model = adapter.getBufferedModel("value");
      bean.setValue(model.getValue().toString());
      
      ValidationResult result = bean.validate(mediator.getParameters());
      if (!result.isValid()) {
        JOptionPane.showMessageDialog(this, "Parameter Error: " + result.getMsg(),
            "Parameter Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }

  public boolean commit() {
    if (validateBean()) {
      trigger.triggerCommit();
      mediator.nodeChanged();
      System.out.println(adapter.getBean());
      return true;
    }
    return false;
  }

  public void cancel() {
    trigger.triggerFlush();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    separator1 = compFactory.createSeparator("Parameter Details");
    panel2 = new JPanel();
    label1 = new JLabel();
    nameBox = new JComboBox();
    label2 = new JLabel();
    typeFld = new JTextField();
    label3 = new JLabel();
    pTypeBox = new JComboBox();
    pTypePanel = new JPanel();
    panel1 = new JPanel();
    applyBtn = new JButton();
    CellConstraints cc = new CellConstraints();

    // ======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new FormLayout(ColumnSpec.decodeSpecs("default:grow"), new RowSpec[] {
        FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) }));
    add(separator1, cc.xy(1, 1));

    // ======== panel2 ========
    {
      panel2.setBorder(new EmptyBorder(5, 5, 5, 5));
      panel2.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
          FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
          new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) }, new RowSpec[] {
          FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
          FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
          new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
          FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));

      // ---- label1 ----
      label1.setText("Name:");
      panel2.add(label1, cc.xy(1, 1));
      panel2.add(nameBox, cc.xy(3, 1));

      // ---- label2 ----
      label2.setText("Type:");
      panel2.add(label2, cc.xy(1, 3));

      // ---- typeFld ----
      typeFld.setEditable(false);
      typeFld.setEnabled(false);
      panel2.add(typeFld, cc.xy(3, 3));

      // ---- label3 ----
      label3.setText("Parameter Type:");
      panel2.add(label3, cc.xy(1, 5));

      // ---- pTypeBox ----
      pTypeBox.setModel(new DefaultComboBoxModel(
          new String[] { "Constant", "Numeric Range", "List" }));
      panel2.add(pTypeBox, cc.xy(3, 5));

      // ======== pTypePanel ========
      {
        pTypePanel.setLayout(new CardLayout());
      }
      panel2.add(pTypePanel, cc.xywh(1, 7, 3, 1));

      // ======== panel1 ========
      {
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // ---- applyBtn ----
        applyBtn.setText("Apply");
        panel1.add(applyBtn);
      }
      panel2.add(panel1, cc.xy(3, 9));
    }
    add(panel2, cc.xy(1, 3));
    // JFormDesigner - End of component initialization //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JComponent separator1;
  private JPanel panel2;
  private JLabel label1;
  private JComboBox nameBox;
  private JLabel label2;
  private JTextField typeFld;
  private JLabel label3;
  private JComboBox pTypeBox;
  private JPanel pTypePanel;
  private JPanel panel1;
  private JButton applyBtn;
  // JFormDesigner - End of variables declaration //GEN-END:variables
}
