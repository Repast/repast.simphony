/*
 * Created by JFormDesigner on Thu Jul 30 10:20:53 EDT 2009
 */

package repast.simphony.batch.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

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
public class ParameterConstantPanel extends JPanel {
  public ParameterConstantPanel() {
    initComponents();
  }
  
  public void setValueFld(String val) {
    valueFld.setText(val);
  }
  
  public void initBindings(PresentationModel<BatchParameterBean> adapter) {
    ValueModel model = adapter.getBufferedModel("value");
    Bindings.bind(valueFld, model);
  }
  
  public void resetDocumentModel(Class<?> clazz) {
    if (clazz.equals(Double.class) || clazz.equals(double.class) || clazz.equals(Float.class) ||
        clazz.equals(float.class)) {
      valueFld.setDocument(new DoubleDocument());
    } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
     valueFld.setDocument(new LongDocument());
    } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
     valueFld.setDocument(new IntegerDocument());
    } else {
      valueFld.setDocument(new PlainDocument());
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    valueFld = new JTextField();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setLayout(new FormLayout(
      new ColumnSpec[] {
        FormFactory.DEFAULT_COLSPEC,
        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
      },
      RowSpec.decodeSpecs("default")));

    //---- label1 ----
    label1.setText("Value:");
    add(label1, cc.xy(1, 1));
    add(valueFld, cc.xy(3, 1));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JTextField valueFld;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
