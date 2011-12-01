/*
 * Created by JFormDesigner on Tue Sep 04 16:11:37 EDT 2007
 */

package repast.simphony.visualization.editor;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.Projection;
import static repast.simphony.visualization.editor.DisplayEditor.Mode.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author User #2
 * @deprecated 2D piccolo based code is being removed
 */
public class VizEditorForm extends JPanel {
  private ButtonGroup buttonGroup1;


  class ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JToggleButton btn = (JToggleButton) e.getSource();
      DisplayEditor.Mode mode = btn.isSelected() ? (DisplayEditor.Mode) btn.getClientProperty(MODE_KEY) : NONE;
      editor.modeSwitched(mode);
    }
  }

  private static final String SELECT_ICON = "select.png";
  private static final String MOVE_ICON = "move.png";
  private static final String LINK_ICON = "link_16.png";
  private static final String MODE_KEY = "MODE_KEY";

  private DisplayEditor editor;
  private DisplayEditor.Mode mode;
  private Map<DisplayEditor.Mode, JToggleButton> modeMap = new HashMap<DisplayEditor.Mode, JToggleButton>();

  public VizEditorForm() {
    initComponents();
    selectBtn.setIcon(new ImageIcon(VizEditorForm.class.getClassLoader().getResource(SELECT_ICON)));
    selectBtn.setToolTipText("Select agent or link");
    moveBtn.setIcon(new ImageIcon(VizEditorForm.class.getClassLoader().getResource(MOVE_ICON)));
    moveBtn.setToolTipText("Move agent");
    addEdgeBtn.setIcon(new ImageIcon(VizEditorForm.class.getClassLoader().getResource(LINK_ICON)));
    addEdgeBtn.setToolTipText("Add link");
    addEdgeBtn.setText(null);

    modeMap.put(SELECT, selectBtn);
    selectBtn.putClientProperty(MODE_KEY, SELECT);

    modeMap.put(MOVE, moveBtn);
    moveBtn.putClientProperty(MODE_KEY, MOVE);

    modeMap.put(ADD_EDGE, addEdgeBtn);
    addEdgeBtn.putClientProperty(MODE_KEY, ADD_EDGE);

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    toolBar1 = new JToolBar();
    selectBtn = new JToggleButton();
    moveBtn = new JToggleButton();
    separator1 = new JSeparator();
    addEdgeBtn = new JToggleButton();
    netBox = new JComboBox();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
            "59dlu:grow",
            "top:default:grow"));

    //======== toolBar1 ========
    {
      toolBar1.setFloatable(false);
      toolBar1.add(selectBtn);
      toolBar1.add(moveBtn);

      //---- separator1 ----
      separator1.setMaximumSize(new Dimension(2, 1));
      separator1.setPreferredSize(new Dimension(2, 0));
      toolBar1.add(separator1);

      //---- addEdgeBtn ----
      addEdgeBtn.setText("e");
      addEdgeBtn.setEnabled(false);
      toolBar1.add(addEdgeBtn);
      toolBar1.add(netBox);
    }
    add(toolBar1, cc.xy(1, 1));

    //---- buttonGroup1 ----
    buttonGroup1 = new ButtonGroup();
    buttonGroup1.add(selectBtn);
    buttonGroup1.add(moveBtn);
    buttonGroup1.add(addEdgeBtn);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  public ButtonGroup getButtonGroup() {
    return buttonGroup1;
  }


  public void init(DisplayEditor editor, java.util.List<Network> networks) {
    this.editor = editor;
    if (networks.size() > 0) {
      DefaultComboBoxModel model = new DefaultComboBoxModel(networks.toArray());
      netBox.setModel(model);
      netBox.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
          if (value != null) value = ((Projection) value).getName();
          return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
      });

      netBox.setSelectedIndex(0);
      netBox.setPrototypeDisplayValue(new DefaultProjection("XXXXXXXXXXXXXXXXXXXX"));
      netBox.setMaximumSize(netBox.getPreferredSize());

    } else {
      netBox.setEnabled(false);
    }

    addListeners();
    selectBtn.setSelected(true);
    editor.modeSwitched(SELECT);
    mode = SELECT;

  }

  public DisplayEditor.Mode getMode() {
    return mode;
  }

  public void setAddEdgeEnabled(boolean enabled) {
    addEdgeBtn.setEnabled(enabled);
  }

  public void setMoveEnabled(boolean enabled) {
    moveBtn.setEnabled(enabled);
  }

  public void setMode(DisplayEditor.Mode mode) {
    this.mode = mode;
    modeMap.get(mode).setSelected(true);
    editor.modeSwitched(mode);
  }

  private void addListeners() {
    ButtonListener bListener = new ButtonListener();
    selectBtn.addActionListener(bListener);
    moveBtn.addActionListener(bListener);
    addEdgeBtn.addActionListener(bListener);


    netBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        editor.netSelected((Network) netBox.getSelectedItem());
      }
    });

  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JToolBar toolBar1;
  private JToggleButton selectBtn;
  private JToggleButton moveBtn;
  private JSeparator separator1;
  private JToggleButton addEdgeBtn;
  private JComboBox netBox;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
