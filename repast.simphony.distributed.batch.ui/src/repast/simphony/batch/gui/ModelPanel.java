/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ModelPanel extends JPanel implements BatchRunPanel {
  
  private JTextField modelFld = new JTextField();
  private JTextField scenarioFld = new JTextField();
  private JTextField outFld = new JTextField();
  
  private JButton modelBrowseBtn = new JButton("Browse");
  private JButton scenarioBrowseBtn = new JButton("Browse");
  private JButton outBrowseBtn = new JButton("Browse");
  
  public ModelPanel() {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow, 3dlu, left:pref", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.appendSeparator("Model Details");
    formBuilder.nextLine();
    formBuilder.append("Model Project:", modelFld, modelBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("Scenario:", scenarioFld, scenarioBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("Output Directory:", outFld, outBrowseBtn);
    
    add(formBuilder.getPanel(), BorderLayout.CENTER);
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public void init(BatchRunModel model) {
    modelFld.setText(model.getModelDirectory());
    scenarioFld.setText(model.getScenarioDirectory());
    outFld.setText(model.getOutputDirectory());
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.Commitable#commit(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public void commit(BatchRunModel model) {
    // TODO commit the changes
  }
  
  
}
