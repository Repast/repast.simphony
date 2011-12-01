/*
 * Created by JFormDesigner on Thu Jul 30 10:22:02 EDT 2009
 */

package repast.simphony.batch.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueModel;
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
public class NumericParameterPanel extends JPanel {
  public NumericParameterPanel() {
    initComponents();
  }
  
  public void initBindings(PresentationModel<BatchParameterBean> adapter) {
    ValueModel model = adapter.getBufferedModel("start");
    Bindings.bind(startFld, model);
    
    model = adapter.getBufferedModel("end");
    Bindings.bind(endFld, model);
    
    model = adapter.getBufferedModel("step");
    Bindings.bind(stepFld, model);
  }
  
  public void resetDocumentModel(Class<?> clazz) {
    if (clazz.equals(Double.class) || clazz.equals(double.class) || clazz.equals(Float.class) ||
        clazz.equals(float.class)) {
      stepFld.setDocument(new DoubleDocument());
      endFld.setDocument(new DoubleDocument());
      startFld.setDocument(new DoubleDocument());
    } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
      stepFld.setDocument(new LongDocument());
      endFld.setDocument(new LongDocument());
      startFld.setDocument(new LongDocument());
    } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
      stepFld.setDocument(new IntegerDocument());
      endFld.setDocument(new IntegerDocument());
      startFld.setDocument(new IntegerDocument());
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    startFld = new JTextField();
    label2 = new JLabel();
    endFld = new JTextField();
    label3 = new JLabel();
    stepFld = new JTextField();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new FormLayout(
      new ColumnSpec[] {
        FormFactory.DEFAULT_COLSPEC,
        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
      },
      new RowSpec[] {
        FormFactory.DEFAULT_ROWSPEC,
        FormFactory.LINE_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC,
        FormFactory.LINE_GAP_ROWSPEC,
        FormFactory.DEFAULT_ROWSPEC
      }));

    //---- label1 ----
    label1.setText("Start:");
    add(label1, cc.xy(1, 1));
    add(startFld, cc.xy(3, 1));

    //---- label2 ----
    label2.setText("End:");
    add(label2, cc.xy(1, 3));
    add(endFld, cc.xy(3, 3));

    //---- label3 ----
    label3.setText("Step:");
    add(label3, cc.xy(1, 5));
    add(stepFld, cc.xy(3, 5));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JTextField startFld;
  private JLabel label2;
  private JTextField endFld;
  private JLabel label3;
  private JTextField stepFld;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
