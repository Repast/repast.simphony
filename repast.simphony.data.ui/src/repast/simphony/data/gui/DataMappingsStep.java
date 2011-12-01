/*CopyrightHere*/
package repast.simphony.data.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.data.logging.gather.AggregateDataMapping;
import repast.simphony.data.logging.gather.DataMapping;
import repast.simphony.data.logging.gather.RunNumberMapping;
import repast.simphony.data.logging.gather.TimeDataMapping;
import repast.simphony.ui.widget.DropDownButton;
import repast.simphony.util.ToStringComparator;
import repast.simphony.util.collections.Pair;
import repast.simphony.util.wizard.DynamicWizard;
import repast.simphony.util.wizard.ModelAwarePanelStep;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author Nick Collier
 */
public class DataMappingsStep extends ModelAwarePanelStep<DataSetWizardModel> {
  private static final long serialVersionUID = 1L;

  private static int colNameNumber = 0;

  private Vector<MappingSourceRepresentation> reps;

  private DataMappingTableModel tableModel;
  private Class agentClass = void.class;
  private boolean firstTime = true;


  public DataMappingsStep() {
    super(
            "Agent Mappings",
            "Create data mappings that will generate data and map that data to a column name. These are mappings may be " +
                    "aggregate over all the agents or apply only to a single agent.");
    this.reps = new Vector<MappingSourceRepresentation>();
    initComponents();
  }

  private void removeButtonActionPerformed(ActionEvent e) {
    int row = agentTable.getSelectedRow();
    if (row != -1) {
      DataMappingTableModel model = (DataMappingTableModel) agentTable.getModel();
      model.deleteRow(row);
      setComplete(model.getRowCount() > 0);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    scrollPane1 = new JScrollPane();
    agentTable = new JTable();
    buttonBar = new JPanel();
    addButton = new DropDownButton();
    removeButton = new JButton();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
            ColumnSpec.decodeSpecs("default:grow"),
            new RowSpec[]{
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
            }));

    //======== scrollPane1 ========
    {

      //---- agentTable ----
      agentTable.setCellSelectionEnabled(true);
      agentTable.setModel(new DefaultTableModel(
              new Object[][]{
                      {null, null},
                      {null, null},
              },
              new String[]{
                      "Column Name", "Source"
              }
      ));
      agentTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
      scrollPane1.setViewportView(agentTable);
    }
    add(scrollPane1, cc.xy(1, 1));

    //======== buttonBar ========
    {
      buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
      buttonBar.setLayout(new FormLayout(
              new ColumnSpec[]{
                      FormFactory.GLUE_COLSPEC,
                      FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                      FormFactory.BUTTON_COLSPEC,
                      FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                      FormFactory.BUTTON_COLSPEC,
                      FormFactory.RELATED_GAP_COLSPEC,
                      FormFactory.GLUE_COLSPEC
              },
              RowSpec.decodeSpecs("pref")));

      //---- addButton ----
      addButton.setText("Add");
      addButton.setMnemonic('A');
      buttonBar.add(addButton, cc.xy(3, 1));

      //---- removeButton ----
      removeButton.setText("Remove");
      removeButton.setMnemonic('R');
      removeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          removeButtonActionPerformed(e);
        }
      });
      buttonBar.add(removeButton, cc.xy(5, 1));
    }
    add(buttonBar, cc.xy(1, 3));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents

    setupTable();
    setComplete(false);
    setupAddButton();
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JScrollPane scrollPane1;
  private JTable agentTable;
  private JPanel buttonBar;
  private DropDownButton addButton;
  private JButton removeButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  @Override
  public void prepare() {
    super.prepare();
    // if the agent class changes then current mappings
    // do not apply any more
    if (firstTime || !model.getAgentClass().equals(agentClass)) {
      tableModel.clearMappings();
      if (!model.getAgentClass().equals(agentClass)) {
        loadAgentClass();
      } else {
        loadMappingsFromModel();
      }
    }
    // we need to do this to handle the edit
    // rather than create situation
    if (firstTime) loadMappingsFromModel();
    firstTime = false;
    agentClass = model.getAgentClass();
  }

  private void loadMappingsFromModel() {
    for (DataMapping mapping : model.getDataMappings()) {
      MappingSourceRepresentation rep = findRep(mapping);
      if (rep != null) addMapping(model.getDescriptor().getColumnName(mapping), rep);
    }

    for (AggregateDataMapping mapping : model.getPrimaryAggregateDataMappings()) {
      String columnName = model.getDescriptor().getPrimaryAggregateColumnName(mapping);
      if (mapping instanceof TimeDataMapping || mapping instanceof RunNumberMapping)
        addMapping(columnName, findRep((DataMapping)mapping));

      else addMapping(columnName, new GenericADMRepresentation(mapping));
    }
  }

  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    model.clearMappings();
    for (Pair<String, MappingSourceRepresentation> row : tableModel.rows()) {
      row.getSecond().addMapping(row.getFirst(), model);
    }
    model.cleanMappings();
  }

  private void loadAgentClass() {
    createSourceRepsArray(getSelectedAgentClass());
  }

  private void setupAddButton() {
    final JPopupMenu addMenu = new JPopupMenu();

    JMenuItem item = new JMenuItem("Add Simple Mapping");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (reps.size() == 0) {
          return;
        }
        addMapping(getDefaultColumnName(reps.get(0).toString()), reps.get(0));
      }
    });


    item.setMnemonic('m');
    addMenu.add(item);

    item = new JMenuItem("Add Aggregate Mapping");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        AggMethodDialog dialog = new AggMethodDialog((JDialog) SwingUtilities.getWindowAncestor(DataMappingsStep.this));
        dialog.init(getSelectedAgentClass());
        dialog.pack();
        dialog.setVisible(true);
        AggregateMethodRepresentation rep = dialog.getRepresentation();
        if (rep != null) {
          addMapping("New Column", rep);
        }
      }
    });
    item.setMnemonic('a');
    addMenu.add(item);


    item = new JMenuItem("Add Using Wizard");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DynamicWizard wizard = DataMappingWizardPluginUtil.create(false, model.getAgentClass());
        wizard.showDialog(DataMappingsStep.this, "Mapping Creator");

        DataMappingWizardModel model = (DataMappingWizardModel) wizard.getModel();

        if (model.getMappingRepresentation() != null) {
          addMapping("Mapping" + colNameNumber++, model.getMappingRepresentation());
        }
      }
    });
    item.setMnemonic('c');
    addMenu.add(item);


    addButton.setPopupMenu(addMenu);
  }

  @SuppressWarnings("serial")
  private void setupTable() {
    tableModel = new DataMappingTableModel();

    agentTable.setModel(tableModel);

    agentTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    agentTable.setRowHeight(agentTable.getRowHeight() + 4);
    final JComboBox repsBox = new JComboBox(reps);

    agentTable.setDefaultEditor(MappingSourceRepresentation.class, new DefaultCellEditor(repsBox));
    agentTable.setDefaultRenderer(MappingSourceRepresentation.class, new MappingSourceRenderer());

    agentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          removeButton.setEnabled(agentTable.getSelectedRow() != -1);
        }
      }
    });

    // when the selection changes this changes the columns name to agree with the rep
    repsBox.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          int curRow = agentTable.getEditingRow();

          if (curRow < 0) {
            return;
          }

          agentTable.getModel().setValueAt(
                  getDefaultColumnName(repsBox.getSelectedItem().toString()), curRow, 0);
        }
      }
    });
  }

  private String getDefaultColumnName(String repName) {
    String name = repName;

    // remove any initial "get" or "is"
    name = name.replaceAll("\\Aget|\\Ais", "");
    if (name.equals("current run number: int")) {
      name = "Run Number";
    }

    if (name.contains("(")) {
      name = name.substring(0, name.indexOf("("));
    }

    return name;
  }

  private void addMapping(String name, MappingSourceRepresentation rep) {
    DataMappingTableModel model = (DataMappingTableModel) agentTable.getModel();
    model.addMapping(name, rep);
    agentTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
    agentTable.scrollRectToVisible(agentTable.getCellRect(model.getRowCount() - 1, 0, true));
    setComplete(true);
  }


  private MappingSourceRepresentation findRep(DataMapping mapping) {
    for (MappingSourceRepresentation rep : reps) {
      if (rep.equalsMappingSource(mapping))
        return rep;
    }

    return new GenericDMRepresentation(mapping);
  }

  private void createSourceRepsArray(Class clazz) {
    Method[] methods = clazz.getMethods();
    reps.clear();
    for (Method method : methods) {
      if (method.getParameterTypes().length == 0
              && !method.getReturnType().equals(void.class)
              && !MethodMappingStep.badMethods.contains(method.getName())) {
        reps.add(new MethodSourceRep(method));
      }
    }

    reps.add(new RunNumberRepresentation());
    Collections.sort(reps, new ToStringComparator());
    reps.add(0, new TimeSourceRepresentation());
  }

  public Class<?> getSelectedAgentClass() {
    return model.getAgentClass();
  }
}


class GenericDMRepresentation implements MappingSourceRepresentation {

  private DataMapping mapping;

  public GenericDMRepresentation(DataMapping mapping) {
    this.mapping = mapping;
  }

  public void addMapping(String columnName, DataSetWizardModel model) {
    model.getDescriptor().addMapping(columnName, mapping);
  }

  public boolean equalsMappingSource(DataMapping mapping) {
    return this.mapping.equals(mapping);
  }

  public boolean isMappingEditable() {
    return false;
  }

  public String toString() {
    return mapping.toString();
  }
}

class GenericADMRepresentation implements MappingSourceRepresentation {

  private AggregateDataMapping mapping;

  public GenericADMRepresentation(AggregateDataMapping mapping) {
    this.mapping = mapping;
  }

  public void addMapping(String columnName, DataSetWizardModel model) {
    model.getDescriptor().addPrimaryAggregateMapping(columnName, mapping);
  }

  public boolean equalsMappingSource(DataMapping mapping) {
    return this.mapping.equals(mapping);
  }

  public boolean isMappingEditable() {
    return false;
  }

  public String toString() {
    return mapping.toString();
  }
}

class MappingSourceRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 5330076317696004972L;

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                 boolean hasFocus, int row, int column) {
    if (value != null) {
      return super.getTableCellRendererComponent(table, value.toString(), isSelected,
              hasFocus, row, column);
    } else {
      return super.getTableCellRendererComponent(table, value, isSelected,
              hasFocus, row, column);
    }
  }
}