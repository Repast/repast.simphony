/**
 * 
 */
package repast.simphony.data2.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.data2.AggregateOp;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;
import repast.simphony.data2.util.AggregateFilter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * GUI for defining aggregate data sources.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class AggMethodSourcePanel extends JPanel implements TableModelListener {

  private static Set<String> BAD_METHODS = new HashSet<String>();

  static {
    BAD_METHODS.add("getClass");
    BAD_METHODS.add("hashCode");
  }

  private JTable table;
  private AggMethodTableModel tableModel;
  private JButton removeButton, addButton;
  private DataSetDescriptor descriptor;
  private List<Class<?>> agentClasses;
  private Map<Class<?>, List<String>> methodMap = new HashMap<Class<?>, List<String>>();
  private JComboBox methodBox;
  private AggregateFilter filter = new AggregateFilter();

  private CompletableStep step;

  public AggMethodSourcePanel() {
    super(new BorderLayout());
    FormLayout layout = new FormLayout(
    		"3dlu, pref:grow, 3dlu", 
    		"3dlu, pref, pref, 3dlu");
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();

    table = new JTable();
    table.setPreferredScrollableViewportSize(new Dimension(450, 200));
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    table.setRowHeight(table.getRowHeight() + 4);

    DefaultComboBoxModel opModel = new DefaultComboBoxModel(AggregateOp.values());
    opModel.removeElement(AggregateOp.NONE);
    DefaultCellEditor cellEditor = new DefaultCellEditor(new JComboBox(opModel));
    cellEditor.setClickCountToStart(2);
    table.setDefaultEditor(AggregateOp.class, cellEditor);

    JScrollPane scrollPane = new JScrollPane(table);
    builder.add(scrollPane, cc.xy(2, 2));
    builder.nextLine();

    JPanel panel = new JPanel(new FlowLayout());
    addButton = new JButton("Add");
    removeButton = new JButton("Remove");
    panel.add(addButton);
    panel.add(removeButton);
    builder.add(panel, cc.xy(2, 3));

    add(builder.getPanel(), BorderLayout.CENTER);

    addListeners();
  }
  
  

  private void addListeners() {
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String method = AggMethodTableModel.NO_METHOD_VAL;
        if (getMethodsFor(agentClasses.get(0)).size() > 0) method = getMethodsFor(agentClasses.get(0)).get(0);
        MethodDataSourceDefinition mds = new MethodDataSourceDefinition("", agentClasses.get(0)
            .getName(), method);
        if (method.equals(AggMethodTableModel.NO_METHOD_VAL)) mds.setAggregateOp(AggregateOp.COUNT);
        else mds.setAggregateOp(AggregateOp.SUM);
        
        tableModel.addMethodDataSource(mds);
        step.complete(AggMethodSourcePanel.this, true);
      }
    });

    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int row = table.getSelectedRow();
        tableModel.removeItem(row);
        step.complete(AggMethodSourcePanel.this, tableModel.getRowCount() > 0);
      }
    });

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          removeButton.setEnabled(table.getSelectedRow() != -1);
        }
      }
    });

  }

  public void init(CompletableStep step, DataSetWizardModel wizModel) {
    this.step = step;
    this.descriptor = wizModel.getDescriptor();
    prepareTable();

    if (agentClasses == null) {
      agentClasses = new ArrayList<Class<?>>(wizModel.getAgentClasses());
    }
    JComboBox agentBox = new JComboBox(agentClasses.toArray());
    TableColumn agentCol = table.getColumnModel().getColumn(1);
    agentBox.setRenderer(new ClassNameRenderer());
    DefaultCellEditor cellEditor = new DefaultCellEditor(agentBox);
    cellEditor.setClickCountToStart(2);
    agentCol.setCellEditor(cellEditor);
    agentCol.setCellRenderer(new ClassNameTCRenderer());

    TableColumn methodCol = table.getColumnModel().getColumn(2);
    methodBox = new JComboBox(new DefaultComboBoxModel());
    cellEditor = new MethodCellEditor(methodBox);
    cellEditor.setClickCountToStart(2);
    methodCol.setCellEditor(cellEditor);
  }

  private List<String> getMethodsFor(Class<?> clazz) {
    List<String> sMethods = methodMap.get(clazz);
    if (sMethods == null) {
      sMethods = new ArrayList<String>();
      Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        if (isValidMethod(method)) {
          sMethods.add(method.getName());
        }
      }
      Collections.sort(sMethods);
      methodMap.put(clazz, sMethods);
    }
    return sMethods;
  }
  
  private boolean isValidMethod(Method method) {
    return method.getParameterTypes().length == 0 && filter.check(method.getReturnType())
        && !BAD_METHODS.contains(method.getName()) && !method.getName().startsWith("super$");
  }

  private void prepareTable() {

    table.getModel().removeTableModelListener(this);
    tableModel = new AggMethodTableModel(descriptor);
    table.setModel(tableModel);
    tableModel.addTableModelListener(this);

    step.complete(this, tableModel.getRowCount() > 0);
  }

  public void apply(DataSetDescriptor descriptor) throws InvalidStateException {
    try {
      tableModel.apply(descriptor);
    } catch (IllegalArgumentException ex) {
      throw new InvalidStateException(ex.getMessage());
    }
  }

  private Class<?> findAgentClass(String name) {
    for (Class<?> clazz : agentClasses) {
      if (clazz.getName().equals(name))
        return clazz;
    }
    return null;
  }

  @Override
  public void tableChanged(TableModelEvent evt) {
    // System.out.println(evt.getFirstRow() + "," + evt.getLastRow() + "," +
    // evt.getColumn());
    if (evt.getColumn() == AggMethodTableModel.SOURCE_TYPE_COL) {
      // agent type has changed so need to change the method value for that row.
      String cName = tableModel.getValueAt(evt.getFirstRow(), AggMethodTableModel.SOURCE_TYPE_COL)
          .toString();
      Class<?> clazz = findAgentClass(cName);
      String method = AggMethodTableModel.NO_METHOD_VAL;
      if (getMethodsFor(clazz).size() > 0) method = getMethodsFor(clazz).get(0);
      if (method.equals(AggMethodTableModel.NO_METHOD_VAL)) tableModel.setValueAt(AggregateOp.COUNT, evt.getFirstRow(), 
          AggMethodTableModel.OP_COL);
      else if (table.getValueAt(evt.getFirstRow(), AggMethodTableModel.OP_COL) == AggregateOp.COUNT) {
        tableModel.setValueAt(AggregateOp.SUM, evt.getFirstRow(), 
            AggMethodTableModel.OP_COL);
      }
      tableModel.setValueAt(method, evt.getFirstRow(), AggMethodTableModel.METHOD_COL);
    } else if (evt.getColumn() == AggMethodTableModel.OP_COL) {
      AggregateOp op = (AggregateOp) tableModel.getValueAt(evt.getFirstRow(),
          AggMethodTableModel.OP_COL);
      if (op == AggregateOp.COUNT) {
        tableModel.setValueAt(AggMethodTableModel.NO_METHOD_VAL, evt.getFirstRow(),
            AggMethodTableModel.METHOD_COL);
      } else {
        String methodName = tableModel
            .getValueAt(evt.getFirstRow(), AggMethodTableModel.METHOD_COL).toString();
        if (methodName.equals(AggMethodTableModel.NO_METHOD_VAL)) {
          String cName = tableModel.getValueAt(evt.getFirstRow(),
              AggMethodTableModel.SOURCE_TYPE_COL).toString();
          Class<?> clazz = findAgentClass(cName);
          String method = getMethodsFor(clazz).get(0);
          tableModel.setValueAt(method, evt.getFirstRow(), AggMethodTableModel.METHOD_COL);
        }
      }
    }
  }

  private class MethodCellEditor extends DefaultCellEditor {

    private String sourceClass;

    public MethodCellEditor(JComboBox box) {
      super(box);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object arg1, boolean isSelected,
        int row, int col) {

      String cName = tableModel.getValueAt(row, AggMethodTableModel.SOURCE_TYPE_COL).toString();
      if (sourceClass == null || !sourceClass.equals(cName)) {
        DefaultComboBoxModel model = (DefaultComboBoxModel) ((JComboBox) editorComponent)
            .getModel();
        Class<?> clazz = findAgentClass(cName);
        model.removeAllElements();
        for (String val : getMethodsFor(clazz)) {
          model.addElement(val);
        }
      }

      return super.getTableCellEditorComponent(table, arg1, isSelected, row, col);
    }

  }
}

@SuppressWarnings("serial")
class ClassNameRenderer extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
      boolean arg4) {

    if (arg1 != null)
      arg1 = ((Class<?>) arg1).getSimpleName();
    return super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
  }
}

@SuppressWarnings("serial")
class ClassNameTCRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2,
      boolean arg3, int arg4, int arg5) {
    if (arg1 != null)
      arg1 = arg1.toString().subSequence(arg1.toString().lastIndexOf(".") + 1,
          arg1.toString().length());
    return super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
  }
}
