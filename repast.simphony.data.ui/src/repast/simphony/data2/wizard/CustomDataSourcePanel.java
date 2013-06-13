/**
 * 
 */
package repast.simphony.data2.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import repast.simphony.data2.AggregateDataSource;
import repast.simphony.data2.DataSource;
import repast.simphony.data2.NonAggregateDataSource;
import repast.simphony.data2.engine.CustomDataSourceDefinition;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Displays a GUI for creating custom data sources, via a class name.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class CustomDataSourcePanel extends JPanel {

  private JButton addBtn = new JButton("Add");
  private JTextField classFld = new JTextField();
  private JList list;
  private DataSetType dsType;
  private CompletableStep step;

  public CustomDataSourcePanel() {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("6dlu, pref:grow, 3dlu, pref, 6dlu",
        "3dlu, pref, 3dlu, pref:grow, 6dlu");
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
    builder.add(classFld, cc.xy(2, 2));
    builder.add(addBtn, cc.xy(4, 2));
    addBtn.setEnabled(false);
    list = new JList(new DefaultListModel());
    list.setCellRenderer(new CustomDSRenderer());
    JScrollPane sp = new JScrollPane(list);
    builder.add(sp, cc.xyw(2, 4, 3));
    add(builder.getPanel(), BorderLayout.CENTER);

    addListeners();
  }

  private String validateAndGetId(String name) {
    try {
      DefaultListModel model = (DefaultListModel) list.getModel();
      if (model.contains(name)) {
        JOptionPane.showMessageDialog(this, "Duplicate custom data source.", "Data Source Error",
            JOptionPane.ERROR_MESSAGE);
        return null;
      }
      Class<?> clazz = Class.forName(name);
      if (dsType == DataSetType.AGGREGATE) {
        if (!AggregateDataSource.class.isAssignableFrom(clazz)) {
          JOptionPane.showMessageDialog(this,
              "Custom data source must implement AggregateDataSource.", "Data Source Error",
              JOptionPane.ERROR_MESSAGE);
          return null;
        }

      } else {
        if (!NonAggregateDataSource.class.isAssignableFrom(clazz)) {
          JOptionPane.showMessageDialog(this,
              "Custom data source must implement NonAggregateDataSource.", "Data Source Error",
              JOptionPane.ERROR_MESSAGE);
          return null;
        }
      }

      DataSource ds = (DataSource) clazz.newInstance();
      return ds.getId();

    } catch (ClassNotFoundException ex) {
      JOptionPane.showMessageDialog(this, "Custom data source class not found.",
          "Data Source Error", JOptionPane.ERROR_MESSAGE);
      return null;
    } catch (IllegalAccessException ex) {
      JOptionPane.showMessageDialog(this, "Error instantiating data source.", "Data Source Error",
          JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      return null;
    } catch (InstantiationException ex) {
      JOptionPane.showMessageDialog(this, "Error instantiating data source.", "Data Source Error",
          JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      return null;
    }
  }

  private void addListeners() {
    classFld.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent arg0) {
        addBtn.setEnabled(classFld.getText().trim().length() > 0);
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        addBtn.setEnabled(classFld.getText().trim().length() > 0);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        addBtn.setEnabled(classFld.getText().trim().length() > 0);
      }
    });

    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String name = classFld.getText().trim();
        String id = validateAndGetId(name);
        if (id != null) {
          DefaultListModel model = (DefaultListModel) list.getModel();
          model.addElement(new CustomDataSourceDefinition(id, name));
          step.complete(CustomDataSourcePanel.this, true);
        }
      }
    });

    list.addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_DELETE && list.getSelectedIndex() != -1) {
          DefaultListModel model = (DefaultListModel) list.getModel();
          model.remove(list.getSelectedIndex());
          step.complete(CustomDataSourcePanel.this, model.size() > 0);
        }
      }
    });

    list.setToolTipText("Use delete key to delete an entry");
  }

  public void init(CompletableStep step, DataSetDescriptor descriptor) {
    this.step = step;
    DefaultListModel model = (DefaultListModel) list.getModel();
    model.removeAllElements();
    dsType = descriptor.getType();
    if (dsType == DataSetType.NON_AGGREGATE) {
      for (CustomDataSourceDefinition val : descriptor.customNonAggDataSources()) {
        model.addElement(new CustomDataSourceDefinition(val));
      }
    } else {
      for (CustomDataSourceDefinition val : descriptor.customAggDataSources()) {
        model.addElement(new CustomDataSourceDefinition(val));
      }
    }
    step.complete(this, model.size() > 0);
  }

  public void apply(DataSetDescriptor descriptor) {
    List<CustomDataSourceDefinition> toAdd = new ArrayList<>();
    DefaultListModel model = (DefaultListModel) list.getModel();
    for (int i = 0; i < model.size(); i++) {
      toAdd.add((CustomDataSourceDefinition) model.getElementAt(i));
    }

    Iterable<CustomDataSourceDefinition> iter = dsType == DataSetType.AGGREGATE ? descriptor
        .customAggDataSources() : descriptor.customNonAggDataSources();

    List<String> toRemove = new ArrayList<>();
    for (CustomDataSourceDefinition def : iter) {
      if (!toAdd.remove(def)) {
        toRemove.add(def.getId());
      }
    }
    
    for (String id : toRemove) {
      descriptor.removeCustomDataSource(id);
    }

    if (dsType == DataSetType.AGGREGATE) {
      for (CustomDataSourceDefinition def : toAdd) {
        descriptor.addAggregateDataSource(def.getId(), def.getDataSourceClassName());
      }
    } else {
      for (CustomDataSourceDefinition def : toAdd) {
        descriptor.addNonAggregateDataSource(def.getId(), def.getDataSourceClassName());
      }
    }
  }
}

@SuppressWarnings("serial")
class CustomDSRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
      boolean arg4) {
    if (arg1 != null) {
      arg1 = ((CustomDataSourceDefinition) arg1).getId() + " - "
          + ((CustomDataSourceDefinition) arg1).getDataSourceClassName();
    }
    return super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
  }

}
