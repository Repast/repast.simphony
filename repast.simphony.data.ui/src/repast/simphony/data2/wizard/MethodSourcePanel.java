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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;
import repast.simphony.data2.util.NonAggregateFilter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * GUI for selecting method data sources.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class MethodSourcePanel extends JPanel {

  private static class ClassItem {

    Class<?> clazz;

    public ClassItem(Class<?> clazz) {
      this.clazz = clazz;
    }

    public String toString() {
      String str = clazz.getSimpleName();
      if (str.length() == 0) {
        str = clazz.getName();
        int index = str.lastIndexOf(".");
        if (index != -1) {
          str = str.substring(index + 1, str.length());
        }
      }
      return str;
    }
  }

  private static Set<String> BAD_METHODS = new HashSet<String>();

  static {
    BAD_METHODS.add("getClass");
    BAD_METHODS.add("hashCode");
  }

  private JComboBox sourceTypeBox;
  private JTable table;
  private Class<?> sourceClass;
  private SimpleMethodTableModel tableModel;
  private JButton removeButton, addButton;
  private List<MethodDataSourceDefinition> mVec;
  private DataSetDescriptor descriptor;
  private NonAggregateFilter filter = new NonAggregateFilter();

  private CompletableStep step;

  public MethodSourcePanel() {
    super(new BorderLayout());
    FormLayout layout = new FormLayout(
    		"3dlu, pref, 3dlu, pref:grow, 3dlu", 
    		"3dlu, pref, 3dlu, pref, 3dlu, pref:grow");
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
   
    table = new JTable();
    table.setPreferredScrollableViewportSize(new Dimension(450, 175));
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    table.setRowHeight(table.getRowHeight() + 4);
    
    sourceTypeBox = new JComboBox(new DefaultComboBoxModel());
    builder.addLabel("Source Class:", cc.xy(2, 2));
    builder.add(sourceTypeBox, cc.xy(4, 2));

    JScrollPane scrollPane = new JScrollPane(table);
    builder.add(scrollPane, cc.xyw(2, 4, 3));
    builder.nextLine();

    JPanel panel = new JPanel(new FlowLayout());
    addButton = new JButton("Add");
    removeButton = new JButton("Remove");
    panel.add(addButton);
    panel.add(removeButton);
    builder.add(panel, cc.xyw(2, 6, 3));

    add(builder.getPanel(), BorderLayout.CENTER);

    addListeners();
  }

  private void addListeners() {
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        tableModel.addMethodDataSource(mVec.get(0));
        step.complete(MethodSourcePanel.this, true);
      }
    });

    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int row = table.getSelectedRow();
        tableModel.removeItem(row);
        step.complete(MethodSourcePanel.this, tableModel.getRowCount() > 0);
      }
    });

    sourceTypeBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt) {
        if (sourceClass != null && evt.getStateChange() == ItemEvent.SELECTED) {
          Class<?> clazz = ((ClassItem) sourceTypeBox.getSelectedItem()).clazz;
          if (!clazz.equals(sourceClass)) {
            sourceClass = clazz;
            createSourceRepsArray(sourceClass);
            prepareTable();
          }
        }
      }
    });
  }

  public void init(CompletableStep step, DataSetWizardModel wizModel) {
    this.step = step;
    this.descriptor = wizModel.getDescriptor();
    DefaultComboBoxModel model = (DefaultComboBoxModel) sourceTypeBox.getModel();
    model.removeAllElements();
    String type = descriptor.getSourceType();

    for (Class<?> clazz : wizModel.getAgentClasses()) {
      ClassItem item = new ClassItem(clazz);
      model.addElement(item);
      if (type != null && clazz.getName().equals(type))
        sourceTypeBox.setSelectedItem(item);
    }

    if (sourceTypeBox.getSelectedIndex() == -1) {
      sourceTypeBox.setSelectedIndex(0);
    }

    sourceClass = ((ClassItem) sourceTypeBox.getSelectedItem()).clazz;
    createSourceRepsArray(sourceClass);
    prepareTable();
  }

  private void createSourceRepsArray(Class<?> clazz) {
    Method[] methods = clazz.getMethods();
    mVec = new ArrayList<MethodDataSourceDefinition>();
    for (Method method : methods) {
      if (isValidMethod(method)) {
        mVec.add(new MethodDataSourceDefinition(getDefaultColumnName(method.getName()), clazz
            .getName(), method.getName()));
      }
    }
    Collections.sort(mVec, new Comparator<MethodDataSourceDefinition>() {
      @Override
      public int compare(MethodDataSourceDefinition one, MethodDataSourceDefinition two) {
        return one.getMethodName().compareTo(two.getMethodName());
      }

     
    });
  }

  private boolean isValidMethod(Method method) {
    return method.getParameterTypes().length == 0 && filter.check(method.getReturnType())
        && !BAD_METHODS.contains(method.getName()) && !method.getName().startsWith("super$");
  }

  private void prepareTable() {
    if (descriptor.getSourceType() != null
        && descriptor.getSourceType().equals(sourceClass.getName())) {
      tableModel = new SimpleMethodTableModel(descriptor.methodDataSources());
    } else {
      tableModel = new SimpleMethodTableModel(new ArrayList<MethodDataSourceDefinition>());
    }
    table.setModel(tableModel);

    JComboBox repsBox = new JComboBox(mVec.toArray(new MethodDataSourceDefinition[mVec.size()]));
    repsBox.setRenderer(new MethodDataSourceDefinitionRenderer());

    DefaultCellEditor cellEditor = new DefaultCellEditor(repsBox);
    cellEditor.setClickCountToStart(2);
    table.setDefaultEditor(MethodDataSourceDefinition.class, cellEditor);

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          removeButton.setEnabled(table.getSelectedRow() != -1);
        }
      }
    });
    step.complete(this, tableModel.getRowCount() > 0);
  }

  private String getDefaultColumnName(String repName) {
    String name = repName;

    // remove any initial "get" or "is"
    name = name.replaceAll("\\Aget|\\Ais", "");
    if (name.contains("(")) {
      name = name.substring(0, name.indexOf("("));
    }

    return name;
  }

  public void apply(DataSetDescriptor descriptor) throws InvalidStateException {
    descriptor.setSourceType(sourceClass.getName());
    try {
      tableModel.apply(descriptor);
    } catch (IllegalArgumentException ex) {
      throw new InvalidStateException(ex.getMessage());
    }
  }
}

@SuppressWarnings("serial")
class MethodDataSourceDefinitionRenderer extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
      boolean arg4) {

    if (arg1 != null)
      arg1 = ((MethodDataSourceDefinition) arg1).getMethodName();
    return super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
  }

}
