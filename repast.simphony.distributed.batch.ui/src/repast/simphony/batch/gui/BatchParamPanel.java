/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class BatchParamPanel extends JPanel implements BatchRunPanel {

  private JTextField bpFileFld = new JTextField(20);
  private JTextField pFileFld = new JTextField(20);

  private JButton bpBrowseBtn = new JButton("Browse");
  private JButton pBrowseBtn = new JButton("Browse");
  private JPanel bpConfigPanel = new JPanel(new BorderLayout());

  private List<ParameterInputPanel> inputPanels = new ArrayList<ParameterInputPanel>();

  public BatchParamPanel(PresentationModel<BatchRunConfigBean> pModel) {
    super(new BorderLayout());
    FormLayout layout = new FormLayout("5dlu, left:pref, 3dlu, fill:pref:grow, 3dlu, fill:pref", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.appendSeparator("Parameters");
    formBuilder.nextLine();
    formBuilder.setLeadingColumnOffset(1);
    formBuilder.append("Parameter File:", pFileFld, pBrowseBtn);
    formBuilder.appendSeparator("Batch Parameters");
    formBuilder.nextLine();
    formBuilder.append("Batch Parameter File:", bpFileFld, bpBrowseBtn);
    formBuilder.appendRow("10dlu");
    formBuilder.nextLine();
    formBuilder.appendRow("fill:default:grow");
    formBuilder.nextLine();
    JScrollPane sp = new JScrollPane(bpConfigPanel);
    sp.setBorder(BorderFactory.createEmptyBorder());
    formBuilder.append(sp, 5);
    formBuilder.nextLine();
    formBuilder.appendRow("default");
    JButton btn = new JButton("Generate");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        generate();
      }
    });

    CellConstraints cc = new CellConstraints();
    formBuilder.add(btn, cc.xy(6, 10));

    add(formBuilder.getPanel(), BorderLayout.CENTER);

    bindComponents(pModel);
    initListeners();
  }
  
  private void bindComponents(PresentationModel<BatchRunConfigBean> pModel) {
    ValueModel vm = pModel.getBufferedModel("batchParameterFile");
    Bindings.bind(bpFileFld, vm);
    
    vm = pModel.getBufferedModel("parameterFile");
    Bindings.bind(pFileFld, vm);
  }

  private void initListeners() {
    bpBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(bpFileFld, "Select Batch Parameter File", false);
      }
    });

    pBrowseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        browse(pFileFld, "Select Parameter File", false);
      }
    });

    bpFileFld.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent arg0) {
        updateInputPanelsFromBP();
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
        updateInputPanelsFromBP();
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        updateInputPanelsFromBP();
      }
    });

    pFileFld.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent arg0) {
        updateInputPanelsFromP();
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
        updateInputPanelsFromP();
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        updateInputPanelsFromP();
      }
    });
  }

  private void updateInputPanelsFromP() {
    bpConfigPanel.removeAll();
    inputPanels.clear();

    String pFile = pFileFld.getText().trim();
    if (new File(pFile).exists()) {
      loadParameters(pFile, bpFileFld.getText().trim());
    }

    bpConfigPanel.invalidate();
    bpConfigPanel.revalidate();
    bpConfigPanel.repaint();
  }

  private void updateInputPanelsFromBP() {
    String bpFile = bpFileFld.getText().trim();
    if (new File(bpFile).exists()) {
      BatchParameterParser bpParser = new BatchParameterParser(bpFile);
      try {
        for (ParameterData data : bpParser.parse()) {
          String name = data.getName();
          for (ParameterInputPanel panel : inputPanels) {
            if (panel.getName().equals(name)) {
              panel.update(data);
              break;
            }
          }
        }
        bpFileFld.setForeground(Color.BLACK);
      } catch (Exception ex) {
        bpFileFld.setForeground(Color.RED);
      }
    } else {
      bpFileFld.setForeground(Color.RED);
    }
  }

  /**
   * Updates the batch parameters given the specified scenario.
   * 
   * @param scenarioDir
   */
  public void update(File scenarioDir) {
    for (File file : scenarioDir.listFiles()) {
      if (file.getName().equals("parameters.xml")) {
        pFileFld.setText(file.getAbsolutePath());
        File bparams = new File(scenarioDir.getParent(), "batch");
        bparams = new File(bparams, "batch_params.xml");
        bpFileFld.setText(bparams.getAbsolutePath());
        loadParameters(file.getAbsolutePath(), bparams.getAbsolutePath());
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.
   * BatchRunModel)
   */
  @Override
  public void init(BatchRunConfigBean model) {
    pFileFld.setText(model.getParameterFile());
    pFileFld.setCaretPosition(0);
    bpFileFld.setText(model.getBatchParameterFile());
    bpFileFld.setCaretPosition(0);

    bpConfigPanel.removeAll();
    inputPanels.clear();
    if (model.getParameterFile().length() > 0)
      loadParameters(model.getParameterFile(), model.getBatchParameterFile());

    bpConfigPanel.invalidate();
    bpConfigPanel.revalidate();
    bpConfigPanel.repaint();
  }

  private void loadParameters(String parameterFile, String bpFile) {
    bpConfigPanel.removeAll();
    inputPanels.clear();

    try {
      ParametersParser parser = new ParametersParser(new File(parameterFile));
      Parameters params = parser.getParameters();

      Map<String, ParameterData> pdMap = new HashMap<String, ParameterData>();
      // on first open data might be empty
      if (bpFile.length() > 0) {
        BatchParameterParser bpParser = new BatchParameterParser(bpFile);
        // we catch this in here because regardless of the state
        // of the batch param file we want to show the parameters
        // in the panel.
        try {
          for (ParameterData data : bpParser.parse()) {
            pdMap.put(data.getName(), data);
          }
        } catch (IOException ex) {
          // this means error parsing the bpFile so its bad data
          // so make foreground red.
          bpFileFld.setForeground(Color.RED);
        }
      }

      FormLayout layout = new FormLayout("fill:default:grow", "");
      DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
      for (String name : params.getSchema().parameterNames()) {
        ParameterData pd = pdMap.get(name);
        // parameter file has field that bp file does not
        // so flag that
        if (pd == null) bpFileFld.setForeground(Color.RED);
        ParameterInputPanel inputPanel = new ParameterInputPanel(name, params, pd);
        formBuilder.append(inputPanel);
        inputPanels.add(inputPanel);
      }

      bpConfigPanel.add(formBuilder.getPanel(), BorderLayout.CENTER);

    } catch (Exception ex) {
      // TODO better error.
      ex.printStackTrace();
    }
  }

  private void browse(JTextField fld, String title, boolean showHidden) {
    String file = fld.getText().trim();
    JFileChooser chooser;
    if (file.length() > 0 && new File(file).getParentFile().exists()) {
      chooser = new JFileChooser(new File(file).getParentFile());
    } else {
      chooser = new JFileChooser(System.getProperty("user.home"));
    }

    chooser.setFileHidingEnabled(!showHidden);
    chooser.setDialogTitle(title);

    int retVal = chooser.showOpenDialog(this);
    if (retVal == JFileChooser.APPROVE_OPTION)
      fld.setText(chooser.getSelectedFile().getAbsolutePath());
  }

  private void generate() {
    List<ParameterInputPanel> constants = new ArrayList<ParameterInputPanel>();
    List<ParameterInputPanel> nonConsts = new ArrayList<ParameterInputPanel>();

    for (ParameterInputPanel panel : inputPanels) {
      if (panel.getType() == ParameterType.CONSTANT || panel.getType() == ParameterType.RANDOM) {
        constants.add(panel);
      } else {
        nonConsts.add(panel);
      }
    }

    File file = new File(bpFileFld.getText());
    if (!file.getParentFile().exists())
      file.getParentFile().mkdirs();

    XMLStreamWriter writer = null;
    try {
      writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(file),
          "UTF-8");
      writer.writeStartDocument();
      writer.writeStartElement("sweep");
      writer.writeAttribute("runs", "1");
      for (ParameterInputPanel panel : constants) {
        panel.writeXML(writer);
      }

      for (ParameterInputPanel panel : nonConsts) {
        panel.writeXML(writer);
      }

      for (int i = 0; i < nonConsts.size(); i++) {
        writer.writeEndElement();
      }
      // for sweep
      writer.writeEndElement();
      writer.writeEndDocument();
    } catch (XMLStreamException e) {

      e.printStackTrace();
    } catch (IOException e) {

      e.printStackTrace();
    } catch (FactoryConfigurationError e) {

      e.printStackTrace();
    } finally {
      try {
        writer.close();
      } catch (XMLStreamException ex) {
      }
    }
    bpFileFld.setForeground(Color.BLACK);

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.batch.gui.BatchRunPanel#commit(repast.simphony.batch.gui
   * .BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunConfigBean model) {
    model.setBatchParameterFile(bpFileFld.getText().trim());
    model.setParameterFile(pFileFld.getText().trim());
    return CommitResult.SUCCESS;
  }
}
