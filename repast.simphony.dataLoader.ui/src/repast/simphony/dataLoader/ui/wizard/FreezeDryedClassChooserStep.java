/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.freezedry.gui.ClassRetrievable;
import repast.simphony.util.wizard.ModelAwarePanelStep;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class FreezeDryedClassChooserStep extends ModelAwarePanelStep<DataLoaderWizardModel> {

  private static final long serialVersionUID = -1586259091800864443L;
  private ClassRetrievable classSource;

  public FreezeDryedClassChooserStep(ClassRetrievable classSource) {
    super("Context Information", "Set the id of the context to load.");
    this.classSource = classSource;
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    separator2 = compFactory.createSeparator("Context Details");
    label1 = new JLabel();
    idField = new JTextField();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
    	new ColumnSpec[] {
    		FormSpecs.RELATED_GAP_COLSPEC,
    		FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    		FormSpecs.DEFAULT_COLSPEC,
    		FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    		new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
    	},
    	new RowSpec[] {
    		FormSpecs.DEFAULT_ROWSPEC,
    		FormSpecs.LINE_GAP_ROWSPEC,
    		FormSpecs.DEFAULT_ROWSPEC
    	}));
    add(separator2, cc.xywh(1, 1, 5, 1));

    //---- label1 ----
    label1.setText("Context ID:");
    add(label1, cc.xy(3, 3));
    add(idField, cc.xy(5, 3));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
    super.addCompleteListener(idField);
  }

  protected void updateComplete() {
    // if we're creating a context from a saved one
    setComplete(!idField.getText().equals(""));
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JComponent separator2;
  private JLabel label1;
  private JTextField idField;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
  public DataLoaderWizardModel getModel() {
    return model;
  }

  @Override
  public void prepare() {
    super.prepare();
    ContextActionBuilder builder = model.getBuilder();
    if (builder != null && builder instanceof FreezeDryerContextActionBuilder) {
      FreezeDryerContextActionBuilder fdBuilder = (FreezeDryerContextActionBuilder) builder;
      if (fdBuilder.getFreezeDryedContextId() != null) {
        this.idField.setText(fdBuilder.getFreezeDryedContextId().toString());
      } else {
        this.idField.setText(model.getContextID().toString());
      }
    }
  }

  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    FreezeDryerContextActionBuilder loader = (FreezeDryerContextActionBuilder) model.getBuilder();
    loader.setClassesToLoad(classSource.retrieveClasses());
    loader.setFreezeDryedContextId(getContextId());
    loader.setCreateContextFromData(true);
  }

  public String getContextId() {
    return idField.getText();
  }
}
