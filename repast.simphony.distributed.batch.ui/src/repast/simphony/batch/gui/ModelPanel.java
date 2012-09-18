/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ModelPanel extends JPanel implements BatchRunPanel {
  
  private JTextField modelFld = new JTextField(20);
  private JTextField scenarioFld = new JTextField(20);
  private JTextField outFld = new JTextField(20);
  private JTextField keyDirFld = new JTextField(System.getProperty("user.home") + "/.ssh", 20);
  private JSpinner pollSpn = new JSpinner();
  
  private JButton modelBrowseBtn = new JButton("Browse");
  private JButton scenarioBrowseBtn = new JButton("Browse");
  private JButton outBrowseBtn = new JButton("Browse");
  private JButton kdBrowseBtn = new JButton("Browse");
  
  public ModelPanel() {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow, 3dlu, left:pref", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.appendSeparator("Model Properties");
    formBuilder.nextLine();
    formBuilder.append("Model Project:", modelFld, modelBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("Scenario:", scenarioFld, scenarioBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("Output Directory:", outFld, outBrowseBtn);
    formBuilder.nextLine();
    formBuilder.appendUnrelatedComponentsGapRow();
    formBuilder.nextLine();
    
    formBuilder.appendSeparator("Run Properties");
    formBuilder.nextLine();
    formBuilder.append("SSH Key Directory:", keyDirFld, kdBrowseBtn);
    formBuilder.nextLine();
    pollSpn.setModel(new SpinnerNumberModel(5, .1, 10000, .5));
    formBuilder.append("Poll Frequency (minutes):");
    formBuilder.append(pollSpn, 1);
    
    add(formBuilder.getPanel(), BorderLayout.CENTER);
    initListeners();
  }
  
  private void initListeners() {
    modelBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(modelFld, "Select Model Directory", true, false);
      }
    });
    
    scenarioBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(scenarioFld, "Select Scenario Directory", true, false);
      }
    });
    
    outBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(outFld, "Select Model Output Directory", true, false);
      }
    });
    
    kdBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(keyDirFld, "Select SSH Key Directory", true, true);
      }
    });
  }
  
  private void browse(JTextField fld, String title, boolean directoryChooser, boolean showHidden) {
    String file = fld.getText().trim();
    JFileChooser chooser;
    if (file.length() > 0 && new File(file).getParentFile().exists()) {
      chooser = new JFileChooser(new File(file).getParentFile());
    } else {
      chooser = new JFileChooser(System.getProperty("user.home"));
    }
    if (directoryChooser)chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setFileHidingEnabled(!showHidden);
    chooser.setDialogTitle(title);
    
    int retVal = chooser.showOpenDialog(this);
    if (retVal == JFileChooser.APPROVE_OPTION) fld.setText(chooser.getSelectedFile().getAbsolutePath());
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public void init(BatchRunModel model) {
    modelFld.setText(model.getModelDirectory());
    modelFld.setCaretPosition(0);
    scenarioFld.setText(model.getScenarioDirectory());
    scenarioFld.setCaretPosition(0);
    outFld.setText(model.getOutputDirectory());
    outFld.setCaretPosition(0);
    keyDirFld.setText(model.getKeyDirectory());
    keyDirFld.setCaretPosition(0);
    pollSpn.setValue(model.getPollFrequency());
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.Commitable#commit(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunModel model) {
    // TODO make sure these are valid
    model.setModelDirectory(modelFld.getText().trim());
    model.setOutputDirectory(outFld.getText().trim());
    model.setScenarioDirectory(scenarioFld.getText().trim());
    model.setKeyDirectory(keyDirFld.getText().trim());
    model.setPollFrequency((Double)pollSpn.getValue());
    
    return CommitResult.SUCCESS;
  }
  
  
}
