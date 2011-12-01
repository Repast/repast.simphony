package repast.simphony.data2.wizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.data2.FormatType;
import repast.simphony.data2.engine.FileSinkDescriptor;
import saf.core.ui.util.FileChooserUtilities;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 */

/**
 * Wizard step for configuring the file related properties of a file sink.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class FileSinkPropertiesStep extends PanelWizardStep{
  
  private DocumentListener completionListener = new DocumentListener() {
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
  };


  private FileSinkWizardModel model;
  private JTextField fnameFld = new JTextField();
  private JButton browseBtn = new JButton("Browse");
  private JCheckBox timeChk = new JCheckBox("Insert Current Time into File Name");
  private JTextField delimiterFld = new JTextField();
  private JComboBox formatBox = new JComboBox(FormatType.values());

  public FileSinkPropertiesStep() {
    super("Configure File Properties",
        "Please enter the file and format properties");
    this.setLayout(new BorderLayout());
    
    FormLayout layout = new FormLayout("4dlu, left:pref, 3dlu, pref:grow, 3dlu, pref", "pref, 5dlu, pref, 3dlu, pref, 5dlu, pref, " +
    		"5dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    
    CellConstraints cc = new CellConstraints();
    builder.addSeparator("File Properties", cc.xyw(1, 1, 6));
    builder.addLabel("File Name:", cc.xy(2, 3));
    builder.add(fnameFld, cc.xy(4, 3));
    builder.add(browseBtn, cc.xy(6, 3));
    
    builder.add(timeChk, cc.xyw(2, 5, 5));
    
    builder.addSeparator("Format Properties", cc.xyw(1, 7, 6));
    builder.addLabel("Delimiter:", cc.xy(2, 9));
    builder.add(delimiterFld, cc.xyw(4, 9, 3));
    
    builder.addLabel("Format Type:", cc.xy(2, 11));
    builder.add(formatBox, cc.xyw(4, 11, 3));
   
    add(builder.getPanel(), BorderLayout.CENTER);
    addListeners();
  }
  
  private void addListeners() {
    browseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        findFile();
      }
    });
    
    fnameFld.getDocument().addDocumentListener(completionListener);
    delimiterFld.getDocument().addDocumentListener(completionListener);
  }
  
  private void checkComplete() {
    setComplete(fnameFld.getText().trim().length() > 0 && delimiterFld.getText().length() > 0);
  }
  
  private void findFile() {
    File dir = new File(".");
    if (fnameFld.getText().trim().length() > 0) {
      dir = new File(fnameFld.getText()).getParentFile();
    }
    
    File file = FileChooserUtilities.getSaveFile(this, dir);
    if (file != null) {
      fnameFld.setText(file.getAbsolutePath());
    }  
  }

  public void init(WizardModel wizardModel) {
    this.model = (FileSinkWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    FileSinkDescriptor descriptor = model.getDescriptor();
    fnameFld.setText(descriptor.getFileName());
    timeChk.setSelected(descriptor.isAddTimeStamp());
    delimiterFld.setText(descriptor.getDelimiter());
    formatBox.setSelectedItem(descriptor.getFormat());
    checkComplete();
  }
  
  public void applyState() throws InvalidStateException {
    super.applyState();
    FileSinkDescriptor descriptor = model.getDescriptor();
    descriptor.setFileName(fnameFld.getText().trim());
    descriptor.setAddTimeStamp(timeChk.isSelected());
    descriptor.setDelimiter(delimiterFld.getText());
    descriptor.setFormat((FormatType) formatBox.getSelectedItem());
  }
}
