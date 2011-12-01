package repast.simphony.data2.wizard;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.data2.FormatType;
import repast.simphony.data2.ConsoleDataSink.OutputStream;
import repast.simphony.data2.engine.ConsoleSinkDescriptor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 */

/**
 * Formatting step for console sinks.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ConsoleSinkPropertiesStep extends PanelWizardStep {

  private ConsoleSinkWizardModel model;
  
  private JCheckBox enabledChk = new JCheckBox("Enabled (Turn Console Writing On / Off)");
  private JTextField delimiterFld = new JTextField();
  private JComboBox formatBox = new JComboBox(FormatType.values());
  private JComboBox outputBox = new JComboBox(new String[]{"Standard", "Error"});

  public ConsoleSinkPropertiesStep() {
    super("Configure Console Properites", "Please enter the formatting properties");
    this.setLayout(new BorderLayout());
   
    FormLayout layout = new FormLayout("4dlu, left:pref, 3dlu, pref:grow", "pref, 5dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
    builder.add(enabledChk, cc.xyw(1, 1, 4));
    builder.addSeparator("Format Properties", cc.xyw(1, 3, 4));
    builder.addLabel("Delimiter:", cc.xy(2, 5));
    builder.add(delimiterFld, cc.xy(4, 5));
    builder.addLabel("Format Type:", cc.xy(2, 7));
    builder.add(formatBox, cc.xy(4, 7));
    builder.addLabel("Output Stream:", cc.xy(2, 9));
    builder.add(outputBox, cc.xy(4, 9));
    
    add(builder.getPanel(), BorderLayout.CENTER);
    addListeners();
  }
  
  private void addListeners() {
    delimiterFld.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void changedUpdate(DocumentEvent arg0) {
        checkComplete();
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
        checkComplete();
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        checkComplete();
      }
    });
  }

  public void init(WizardModel wizardModel) {
    this.model = (ConsoleSinkWizardModel) wizardModel;
  }
  
  private void checkComplete() {
    setComplete(delimiterFld.getText().length() > 0);
  }

  public void prepare() {
    super.prepare();
    ConsoleSinkDescriptor descriptor = model.getDescriptor();
    enabledChk.setSelected(descriptor.isEnabled());
    delimiterFld.setText(descriptor.getDelimiter());
    formatBox.setSelectedItem(descriptor.getFormat());
    if (descriptor.getOutputStream() == OutputStream.OUT) outputBox.setSelectedIndex(0);
    else outputBox.setSelectedIndex(1);
    checkComplete();
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    ConsoleSinkDescriptor descriptor = model.getDescriptor();
    descriptor.setEnabled(enabledChk.isSelected());
    descriptor.setDelimiter(delimiterFld.getText());
    descriptor.setFormat((FormatType) formatBox.getSelectedItem());
    if (outputBox.getSelectedItem().equals("Standard")) descriptor.setOutputStream(OutputStream.OUT);
    else descriptor.setOutputStream(OutputStream.ERR);
  }
}
