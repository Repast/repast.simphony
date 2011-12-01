/*
 * Created by JFormDesigner on Thu Jul 30 10:16:41 EDT 2009
 */

package repast.simphony.batch.gui;

import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author User #1
 */
public class ParameterListPanel extends JPanel {
  public ParameterListPanel() {
    initComponents();
  }
  
  public void initBindings(PresentationModel<BatchParameterBean> adapter) {
    ValueModel model = adapter.getBufferedModel("listItems");
    Bindings.bind(listArea, model);
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    scrollPane1 = new JScrollPane();
    listArea = new JTextArea();
    label2 = new JLabel();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new FormLayout(
      ColumnSpec.decodeSpecs("default:grow"),
      new RowSpec[] {
        FormFactory.DEFAULT_ROWSPEC,
        FormFactory.LINE_GAP_ROWSPEC,
        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
        FormFactory.LINE_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC
      }));

    //---- label1 ----
    label1.setText("Parameter List Values:");
    add(label1, cc.xy(1, 1));

    //======== scrollPane1 ========
    {

      //---- listArea ----
      listArea.setRows(5);
      scrollPane1.setViewportView(listArea);
    }
    add(scrollPane1, cc.xy(1, 3));

    //---- label2 ----
    label2.setText("Each line in the list above is an item in the parameter list");
    add(label2, cc.xy(1, 5));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JScrollPane scrollPane1;
  private JTextArea listArea;
  private JLabel label2;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
