/*
 * Created by JFormDesigner on Wed Aug 05 14:44:17 EDT 2009
 */

package repast.simphony.batch.gui;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author User #1
 */
public class ConstantInfoPanel extends JPanel {
  public ConstantInfoPanel() {
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    label2 = new JLabel();
    label3 = new JLabel();
    label4 = new JLabel();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
      ColumnSpec.decodeSpecs("default:grow"),
      new RowSpec[] {
        FormFactory.DEFAULT_ROWSPEC,
        FormFactory.LINE_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC,
        FormFactory.LINE_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC,
        FormFactory.UNRELATED_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC
      }));

    //---- label1 ----
    label1.setText("Configure constant parameter values from this node:");
    add(label1, cc.xy(1, 1));

    //---- label2 ----
    label2.setText("- Press the 'Add Parameter' Button to add a constant parameter");
    add(label2, cc.xy(1, 3));

    //---- label3 ----
    label3.setText("- Press the 'Delete Parameter' button to delete a selected parameter");
    add(label3, cc.xy(1, 5));

    //---- label4 ----
    label4.setText("Or right-click on this node or other tree nodes to see a menu of options.");
    add(label4, cc.xy(1, 7));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JLabel label2;
  private JLabel label3;
  private JLabel label4;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
