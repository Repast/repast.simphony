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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.ValueModel;
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
  private JTextField vmFld = new JTextField(20);
  private JTextField keyDirFld = new JTextField(System.getProperty("user.home") + "/.ssh", 20);
  private JSpinner pollSpn = new JSpinner();
  
  private JButton modelBrowseBtn = new JButton("Browse");
  private JButton scenarioBrowseBtn = new JButton("Browse");
  private JButton outBrowseBtn = new JButton("Browse");
  private JButton kdBrowseBtn = new JButton("Browse");
  
  public ModelPanel(PresentationModel<BatchRunConfigBean> pModel) {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("5dlu, left:pref, 3dlu, pref:grow, 3dlu, left:pref", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.appendSeparator("Model Properties");
    formBuilder.nextLine();
    
    formBuilder.setLeadingColumnOffset(1);
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
    formBuilder.append("VM Arguments:", vmFld);
    formBuilder.nextLine();
    
    formBuilder.append("Poll Frequency (minutes):");
    formBuilder.append(pollSpn, 1);
    
    add(formBuilder.getPanel(), BorderLayout.CENTER);
    bindComponents(pModel);
    initListeners();
  }
  
  private void bindComponents(PresentationModel<BatchRunConfigBean> pModel) {
    ValueModel vModel = pModel.getBufferedModel("modelDirectory");
    Bindings.bind(modelFld, vModel);
    
    vModel = pModel.getBufferedModel("scenarioDirectory");
    Bindings.bind(scenarioFld, vModel);
    
    vModel = pModel.getBufferedModel("outputDirectory");
    Bindings.bind(outFld, vModel);
    
    vModel = pModel.getBufferedModel("keyDirectory");
    Bindings.bind(keyDirFld, vModel);
    
    vModel = pModel.getBufferedModel("VMArguments");
    Bindings.bind(vmFld, vModel);
    
    vModel = pModel.getBufferedModel("pollFrequency");
    pollSpn.setModel(SpinnerAdapterFactory.createNumberAdapter(vModel, 5, .1, 10000.0, .5));
  }
  
  private void initListeners() {
    modelBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(modelFld, "Select Model Directory", false);
      }
    });
    
    scenarioBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(scenarioFld, "Select Scenario Directory", false);
      }
    });
    
    outBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(outFld, "Select Model Output Directory", false);
      }
    });
    
    kdBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(keyDirFld, "Select SSH Key Directory", true);
      }
    });
  }
  
  public String getModelDirectory() {
    return modelFld.getText().trim();
  }
  
  public String getScenarioDirectory() {
    return scenarioFld.getText().trim();
  }
  
  public void update(File modelDir) {
    modelFld.setText(modelDir.getAbsolutePath());
    for (File file : modelDir.listFiles()) {
      if (file.getName().endsWith(".rs")) {
        scenarioFld.setText(file.getAbsolutePath());
        break;
      }
    }
    
    outFld.setText(new File(modelDir, "output").getAbsolutePath());
  }
  
  private boolean browse(JTextField fld, String title, boolean showHidden) {
    String file = fld.getText().trim();
    JFileChooser chooser;
    if (file.length() > 0 && new File(file).getParentFile().exists()) {
      chooser = new JFileChooser(new File(file).getParentFile());
    } else {
      chooser = new JFileChooser(System.getProperty("user.home"));
    }
    
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setFileHidingEnabled(!showHidden);
    chooser.setDialogTitle(title);
    
    int retVal = chooser.showOpenDialog(this);
    if (retVal == JFileChooser.APPROVE_OPTION) {
      fld.setText(chooser.getSelectedFile().getAbsolutePath());
      return true;
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public void init(BatchRunConfigBean model) {
    modelFld.setText(model.getModelDirectory());
    modelFld.setCaretPosition(0);
    scenarioFld.setText(model.getScenarioDirectory());
    scenarioFld.setCaretPosition(0);
    outFld.setText(model.getOutputDirectory());
    outFld.setCaretPosition(0);
    keyDirFld.setText(model.getKeyDirectory());
    keyDirFld.setCaretPosition(0);
    pollSpn.setValue(model.getPollFrequency());
    vmFld.setText(model.getVMArguments());
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.Commitable#commit(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunConfigBean model) {
    // TODO make sure these are valid
    model.setModelDirectory(modelFld.getText().trim());
    model.setOutputDirectory(outFld.getText().trim());
    model.setScenarioDirectory(scenarioFld.getText().trim());
    model.setKeyDirectory(keyDirFld.getText().trim());
    model.setPollFrequency((Double)pollSpn.getValue());
    model.setVMArguments(vmFld.getText().trim());
    
    return CommitResult.SUCCESS;
  }
  
}
