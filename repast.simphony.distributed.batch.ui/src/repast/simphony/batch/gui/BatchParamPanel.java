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
public class BatchParamPanel extends JPanel implements BatchRunPanel {
  
  private JTextField paramFileFld = new JTextField();
  private JButton paramFileBrowseBtn = new JButton("Browse");
  
  
  public BatchParamPanel() {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow, 3dlu, left:pref", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.appendSeparator("Batch Parameters");
    formBuilder.nextLine();
    formBuilder.append("Batch Parameter File:", paramFileFld, paramFileBrowseBtn);
    paramFileFld.setColumns(20);
    
    add(formBuilder.getPanel(), BorderLayout.CENTER);
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public void init(BatchRunModel model) {
    paramFileFld.setText(model.getBatchParameterFile());
    paramFileFld.setCaretPosition(0);
    
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#commit(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunModel model) {
    // TODO check for validity
    model.setBatchParameterFile(paramFileFld.getText().trim());
    return CommitResult.SUCCESS;
  }
}
