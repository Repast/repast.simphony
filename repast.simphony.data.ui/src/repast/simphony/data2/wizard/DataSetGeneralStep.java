package repast.simphony.data2.wizard;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.DataSetDescriptor.DataSetType;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * General Step for creating data sets.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class DataSetGeneralStep extends PluginWizardStep {

  private static final String NON_AGGREGATE = "Non-Aggregate";
  private static final String AGGREGATE = "Aggregate";

  private DataSetWizardModel model;

  private JTextField idField ;
  private JComboBox typeBox;
  private JLabel typeLabel;

  public DataSetGeneralStep() {
    super("General Settings",
        "Please enter a unique id for that data set and select the data set type.");
  }
  
  @Override
	protected JPanel getContentPanel(){
  	idField = new JTextField();
  	
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setBorder(WizardUtils.createDefaultEmptyBorder());
    builder.append("Data Set Id:", idField);
    builder.nextLine();

    typeBox = new JComboBox(new String[] { AGGREGATE, NON_AGGREGATE });
    typeLabel = builder.append("Data Set Type:", typeBox);

    addListeners();
    return builder.getPanel();
  }
  
  public void disableTypeSelection() {
    typeBox.setEnabled(false);
    typeLabel.setEnabled(false);
    
  }

  private void addListeners() {
    idField.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void changedUpdate(DocumentEvent arg0) {
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
        setComplete(idField.getText().trim().length() > 0);
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
        setComplete(idField.getText().trim().length() > 0);
      }
    });

    typeBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          DataSetType dsType = stringToType(typeBox.getSelectedItem().toString());
          if (dsType != model.getDescriptor().getType()) {
            model.getDescriptor().clearCountDataSources();
            model.getDescriptor().clearMethodDataSources();
            model.getDescriptor().clearCustomDataSources();
          }
        }
      }
    });
  }

  private DataSetType stringToType(String val) {
    if (val.equals(AGGREGATE))
      return DataSetType.AGGREGATE;
    else if (val.equals(NON_AGGREGATE))
      return DataSetType.NON_AGGREGATE;
    return null;
  }

  public void init(WizardModel wizardModel) {
    this.model = (DataSetWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    DataSetDescriptor descriptor = model.getDescriptor();
    idField.setText(descriptor.getName());
    DataSetDescriptor.DataSetType type = descriptor.getType();
    DataSetType boxType = stringToType(typeBox.getSelectedItem().toString());
    if (boxType != descriptor.getType()) {
      if (type == DataSetType.AGGREGATE)
        typeBox.setSelectedItem(AGGREGATE);
      else
        typeBox.setSelectedItem(NON_AGGREGATE);
    }
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    DataSetDescriptor descriptor = model.getDescriptor();
    descriptor.setName(idField.getText().trim());
    if (typeBox.getSelectedItem().equals(AGGREGATE))
      descriptor.setType(DataSetType.AGGREGATE);
    else
      descriptor.setType(DataSetType.NON_AGGREGATE);
  }
}
