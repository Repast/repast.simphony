/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import repast.simphony.batch.ssh.OutputPattern;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

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
  private PatternTableModel tableModel;
  
  private JButton modelBrowseBtn = new JButton("Browse");
  private JButton scenarioBrowseBtn = new JButton("Browse");
  private JButton outBrowseBtn = new JButton("Browse");
  private JButton kdBrowseBtn = new JButton("Browse");
  private JButton delButton = new JButton("Delete");
 
  public ModelPanel(PresentationModel<BatchRunConfigBean> pModel) {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("5dlu, left:pref, 3dlu, pref:grow, 3dlu, left:pref", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.border(Borders.DIALOG);
    formBuilder.appendSeparator("Model Properties");
    formBuilder.nextLine();
    
    formBuilder.leadingColumnOffset(1);
    formBuilder.append("Model Project:", modelFld, modelBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("Scenario:", scenarioFld, scenarioBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("Output Directory:", outFld, outBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append(new JLabel("Optional Output File Patterns:"), 4);
    formBuilder.nextLine();
    formBuilder.defaultRowSpec(RowSpec.decode("fill:pref:grow"));
    
    formBuilder.append(createPatternPanel(), 5);
    formBuilder.defaultRowSpec(FormSpecs.PREF_ROWSPEC);
    
    formBuilder.nextLine();
    //formBuilder.appendUnrelatedComponentsGapRow();
    //formBuilder.nextLine();
    
    formBuilder.appendSeparator("Run Properties");
    formBuilder.nextLine();
    formBuilder.append("SSH Key Directory:", keyDirFld, kdBrowseBtn);
    formBuilder.nextLine();
    formBuilder.append("VM Arguments:", vmFld);
    formBuilder.nextLine();
    
    formBuilder.append("Poll Frequency (minutes):");
    formBuilder.append(pollSpn, 1);

    JScrollPane sp = new JScrollPane(formBuilder.getPanel());
    sp.getVerticalScrollBar().setUnitIncrement(8);
    sp.getViewport().setOpaque(false);
    add(sp, BorderLayout.CENTER);
    bindComponents(pModel);
    initListeners();
  }
  
  private JPanel createPatternPanel() {
    JPanel tablePanel = new JPanel(new BorderLayout());
    tableModel = new PatternTableModel();
    final JTable table = new JTable(tableModel);
    
    JScrollPane scrollPane = new JScrollPane(table);
    table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, 80));
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    table.setRowHeight(table.getRowHeight() + 4);
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          delButton.setEnabled(table.getSelectedRow() != -1);
        }
      }
    });
  
    tablePanel.add(scrollPane, BorderLayout.CENTER);
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton addButton = new JButton("Add");
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        OutputPattern pattern = new OutputPattern();
        tableModel.addOutputPattern(pattern);
        if (table.editCellAt(tableModel.getRowCount() - 1, 0)) {
          table.getEditorComponent().requestFocus();
        }
      }
    });
    buttonPanel.add(addButton);
    
    delButton.setEnabled(false);
    delButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int row = table.getSelectedRow();
        while (row != -1) {
          tableModel.deleteRow(row);
          row = table.getSelectedRow();
        }
      }
    });
    buttonPanel.add(delButton);
    
    tablePanel.add(buttonPanel, BorderLayout.SOUTH);
    
    table.getColumnModel().getColumn(2).setPreferredWidth(10);
    table.getColumnModel().getColumn(3).setPreferredWidth(10);
    
    return tablePanel;
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
    
    final BufferedValueModel model = pModel.getBufferedModel("outputPatterns");
    tableModel.addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        model.setValue(tableModel.getOutputPatterns());
      }
    });
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
  
  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#validateInput()
   */
  @Override
  public ValidationResult validateInput() {
    if (modelFld.getText().trim().isEmpty()) return new ValidationResult("Model directory is missing.");
    if (scenarioFld.getText().trim().isEmpty()) return new ValidationResult("Scenario directory is missing.");
    if (outFld.getText().trim().isEmpty()) return new ValidationResult("Ouput directory is missing.");
    if (keyDirFld.getText().trim().isEmpty()) return new ValidationResult("SSH key directory is missing.");
    
    for (OutputPattern pattern : tableModel.getOutputPatterns()) {
      if (pattern.getPattern().trim().length() == 0 || pattern.getPath().trim().length() == 0)
        return new ValidationResult("Invalid output file pattern: pattern is missing path or pattern");
    }
    
    return ValidationResult.SUCCESS;
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
    tableModel.setOutputPatterns(model.getOutputPatterns());
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.Commitable#commit(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunConfigBean model) {
    model.setModelDirectory(modelFld.getText().trim());
    model.setOutputDirectory(outFld.getText().trim());
    model.setScenarioDirectory(scenarioFld.getText().trim());
    model.setKeyDirectory(keyDirFld.getText().trim());
    model.setPollFrequency((Double)pollSpn.getValue());
    model.setVMArguments(vmFld.getText().trim());
    model.setOutputPatterns(tableModel.getOutputPatterns());
    
    return CommitResult.SUCCESS;
  }
}
