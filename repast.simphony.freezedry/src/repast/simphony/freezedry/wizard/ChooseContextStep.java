/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.util.wizard.ModelAwarePanelStep;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class ChooseContextStep extends ModelAwarePanelStep<FreezeDryWizardModel> {
  private static final long serialVersionUID = 1L;

  public ChooseContextStep() {
    super("Context selection", "Select the context whose data you would like to freeze dry.");
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    rootButton = new JRadioButton();
    specificButton = new JRadioButton();
    label2 = new JLabel();
    contextBox = new JComboBox();
    CellConstraints cc = new CellConstraints();

    // ======== this ========
    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) }, new RowSpec[] {
        FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
        FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));

    // ---- rootButton ----
    rootButton.setText("Use the Root Context");
    rootButton.setSelected(true);
    add(rootButton, cc.xywh(1, 1, 3, 1));

    // ---- specificButton ----
    specificButton.setText("Use a Specific Context");
    add(specificButton, cc.xywh(1, 3, 3, 1));

    // ---- label2 ----
    label2.setText("Context Name");
    add(label2, cc.xy(1, 5));
    add(contextBox, cc.xy(3, 5));

    // ---- buttonGroup1 ----
    ButtonGroup buttonGroup1 = new ButtonGroup();
    buttonGroup1.add(rootButton);
    buttonGroup1.add(specificButton);
    // JFormDesigner - End of component initialization //GEN-END:initComponents

    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        updateComplete();
      }
    };
    contextBox.addItemListener(itemListener);

    contextModel = new DefaultComboBoxModel();
    contextBox.setModel(contextModel);

    rootButton.addItemListener(itemListener);
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JRadioButton rootButton;
  private JRadioButton specificButton;
  private JLabel label2;
  private JComboBox contextBox;
  // JFormDesigner - End of variables declaration //GEN-END:variables
  private DefaultComboBoxModel contextModel;

  @Override
  public void prepare() {
    super.prepare();

    contextModel.removeAllElements();

    if (model != null && model.getScenario().getContext() != null) {
      for (ContextData context : model.getScenario().getContext().getAllContexts()) {
        contextModel.addElement(context.getId());
      }
    }
  }

  @Override
  public void applyState() throws InvalidStateException {
    if (rootButton.isSelected()) {
      model.setUseRoot(true);
    } else {
      model.setUseRoot(false);
      model.setFreezeDryedContextId(contextModel.getSelectedItem());
    }
  }

  protected void updateComplete() {
    contextBox.setEnabled(!rootButton.isSelected());
    // this should pretty much always set it true
    setComplete(rootButton.isSelected() || contextBox.getSelectedIndex() > -1);
  }
}
