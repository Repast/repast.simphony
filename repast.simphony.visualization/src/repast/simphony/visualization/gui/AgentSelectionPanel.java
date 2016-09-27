/*
 * Created by JFormDesigner on Tue Jul 28 11:11:42 EDT 2009
 */

package repast.simphony.visualization.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import repast.simphony.scenario.data.AgentData;
import repast.simphony.ui.widget.IconUtils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #1
 */
@SuppressWarnings("serial")
public class AgentSelectionPanel extends JPanel {
  
  
  protected static class AgentList extends JList {

    @Override
    public String getToolTipText(MouseEvent event) {
      Point p = event.getPoint();
      int index = locationToIndex(p);
      if (index > -1 && index < getModel().getSize())
        return ((AgentData)getModel().getElementAt(index)).getClassName();
      else return "";
    }

  }
 
  
  
  public AgentSelectionPanel() {
    initComponents();
    initButtons();
    addListeners();
  }
  
  protected void initButtons() {
    addBtn.setIcon(IconUtils.loadIcon("forward.png"));
    removeBtn.setIcon(IconUtils.loadIcon("back.png"));
    upBtn.setIcon(IconUtils.loadIcon("up.png"));
    downBtn.setIcon(IconUtils.loadIcon("down.png"));
  }

  protected void addListeners() {
    source.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        addBtn.setEnabled(source.getSelectedValues().length > 0);
        target.setSelectedIndex(-1);
      }
    });

    target.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        removeBtn.setEnabled(target.getSelectedValues().length > 0);
        upBtn.setEnabled(target.getSelectedIndex() > 0);
        downBtn.setEnabled(target.getSelectedIndex() > -1
            && target.getSelectedIndex() < target.getModel().getSize() - 1);
        source.setSelectedIndex(-1);
      }
    });
    
    

    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Object[] objs = source.getSelectedValues();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        DefaultListModel sModel = (DefaultListModel) source.getModel();
        for (Object obj : objs) {
          sModel.removeElement(obj);
          tModel.addElement(obj);
        }
        if (sModel.size() > 0)
          source.setSelectedIndex(0);
      }
    });

    removeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Object[] objs = target.getSelectedValues();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        DefaultListModel sModel = (DefaultListModel) source.getModel();
        for (Object obj : objs) {
          tModel.removeElement(obj);
          sModel.addElement(obj);
        }
        if (tModel.size() > 0)
          target.setSelectedIndex(0);
      }
    });

    upBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int index = target.getSelectedIndex();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        Object obj = tModel.remove(index);
        tModel.add(index - 1, obj);
        target.setSelectedIndex(index - 1);
      }
    });

    downBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int index = target.getSelectedIndex();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        Object obj = tModel.remove(index);
        tModel.add(index + 1, obj);
        target.setSelectedIndex(index + 1);
      }
    });
    boolean sourceSize = source.getModel().getSize() > 0;
    if (sourceSize) {
      source.setSelectedIndex(0);
    }
    
    target.setSelectedIndex(-1);
  }

  public void init(List<AgentData> sourceList, List<AgentData> targetList) {
    DefaultListModel model = new DefaultListModel();
    for (AgentData item : sourceList) {
      model.addElement(item);
    }
    source.setModel(model);
    
    model = new DefaultListModel();
    for (Object item : targetList) {
      model.addElement(item);
    }

    target.setModel(model);
    source.setVisibleRowCount(8);
    target.setVisibleRowCount(8);
    
    upBtn.setEnabled(false);
    downBtn.setEnabled(false);
  }

  protected void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    panel5 = new JPanel();
    hSpacer1 = new JPanel(null);
    label2 = new JLabel();
    scrollPane1 = new JScrollPane();
    source = new AgentList();
    panel1 = new JPanel();
    vSpacer1 = new JPanel(null);
    addBtn = new JButton();
    removeBtn = new JButton();
    vSpacer2 = new JPanel(null);
    scrollPane2 = new JScrollPane();
    target = new AgentList();
    panel4 = new JPanel();
    upBtn = new JButton();
    downBtn = new JButton();
    label1 = new JLabel();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new FormLayout(
      new ColumnSpec[] {
        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, 0.75),
        FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
        FormSpecs.DEFAULT_COLSPEC,
        FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, 0.25),
        FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
        FormSpecs.DEFAULT_COLSPEC
      },
      new RowSpec[] {
        FormSpecs.DEFAULT_ROWSPEC,
        FormSpecs.LINE_GAP_ROWSPEC,
        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        FormSpecs.LINE_GAP_ROWSPEC,
        FormSpecs.DEFAULT_ROWSPEC
      }));

    //======== panel5 ========
    {
      panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));
      panel5.add(hSpacer1);

      //---- label2 ----
      label2.setText("Foreground");
      label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() - 2f));
      label2.setHorizontalAlignment(SwingConstants.RIGHT);
      panel5.add(label2);
    }
    add(panel5, cc.xy(5, 1));

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView(source);
    }
    add(scrollPane1, cc.xy(1, 3));

    //======== panel1 ========
    {
      panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
      panel1.add(vSpacer1);
      panel1.add(addBtn);
      panel1.add(removeBtn);
      panel1.add(vSpacer2);
    }
    add(panel1, cc.xy(3, 3));

    //======== scrollPane2 ========
    {
      scrollPane2.setViewportView(target);
    }
    add(scrollPane2, cc.xy(5, 3));

    //======== panel4 ========
    {
      panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
      panel4.add(upBtn);
      panel4.add(downBtn);
    }
    add(panel4, cc.xy(7, 3));

    //---- label1 ----
    label1.setText("Background");
    label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() - 2f));
    label1.setHorizontalAlignment(SwingConstants.RIGHT);
    add(label1, cc.xy(5, 5));
    // JFormDesigner - End of component initialization //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  protected JPanel panel5;
  protected JPanel hSpacer1;
  protected JLabel label2;
  protected JScrollPane scrollPane1;
  JList source;
  protected JPanel panel1;
  protected JPanel vSpacer1;
  protected JButton addBtn;
  protected JButton removeBtn;
  protected JPanel vSpacer2;
  protected JScrollPane scrollPane2;
  JList target;
  protected JPanel panel4;
  JButton upBtn;
  JButton downBtn;
  protected JLabel label1;
  // JFormDesigner - End of variables declaration //GEN-END:variables
}
