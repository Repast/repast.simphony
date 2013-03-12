/*
 * Created by JFormDesigner on Thu Mar 06 16:24:44 EST 2008
 */

package repast.simphony.freezedry.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.util.ClassUtilities;
import saf.core.ui.util.FileChooserUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jidesoft.swing.CheckBoxList;
import com.thoughtworks.xstream.converters.Converter;

/**
 * @author User #2
 */
@SuppressWarnings("serial")
public class XMLFileChooserStep extends PanelWizardStep {

  private FreezeDryWizardModel model;

  public XMLFileChooserStep() {
    super("Set XML File", "Set the file name to freezedry to and optionally select any custom XStream " +
            "converters.");
    initComponents();

    fileFld.getDocument().addDocumentListener(new DocumentListener() {

      public void insertUpdate(DocumentEvent e) {
        setComplete(fileFld.getText().trim().length() > 0);
      }

      public void removeUpdate(DocumentEvent e) {
        setComplete(fileFld.getText().trim().length() > 0);
      }

      public void changedUpdate(DocumentEvent e) {
      }
    });

    browseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        selectFile();
      }
    });

    converterList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        value = value != null ? value.getClass().getSimpleName() : value;
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });
  }

  private void selectFile() {
    String fileText = fileFld.getText().trim();
    File file = null;
    if (fileText.length() > 0) {
      file = new File(fileText).getParentFile();
      if (file == null || !file.exists()) file = null;
    }
    file = FileChooserUtilities.getSaveFile(file);
    if (file != null) {
      if (!file.getName().endsWith(".xml")) {
        file = new File(file.getParentFile(), file.getName() + ".xml");
      }
      fileFld.setText(file.getAbsolutePath());
    }
  }

  public void init(WizardModel wizardModel) {
    super.init(wizardModel);
    this.model = (FreezeDryWizardModel) wizardModel;
    DefaultListModel listModel = new DefaultListModel();


    try {
      
      for (Class<?> clazz : model.getScenario().getContext().getClasspath().getClasses()) {
          if (Converter.class.isAssignableFrom(clazz)) {
            Converter converter = (Converter) clazz.newInstance();
            listModel.addElement(converter);
          }
        }
      
      converterList.setModel(listModel);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } finally {
      ClassUtilities.doWarn = true;
    }
  }

  public void prepare() {
    if (model.getXMLFile() != null) {
      fileFld.setText(model.getXMLFile());
    }

    java.util.List<Integer> indices = new ArrayList<Integer>();
    DefaultListModel listModel = (DefaultListModel) converterList.getModel();
    for (Converter converter : model.converters()) {
      for (int i = 0; i < listModel.size(); i++) {
        if (converter.getClass().equals(listModel.elementAt(i).getClass())) {
          indices.add(i);
        }
      }
    }

    int[] sIndices = new int[indices.size()];
    for (int i = 0; i < indices.size(); i++) {
      sIndices[i] = indices.get(i);
    }
    converterList.setCheckBoxListSelectedIndices(sIndices);
    setComplete(fileFld.getText().length() > 0);

  }

  public void applyState() throws InvalidStateException {
    model.setXMLFile(fileFld.getText());
    model.clearConverters();
    for (Object obj : converterList.getCheckBoxListSelectedValues()) {
      model.addConverter((Converter) obj);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    fileFld = new JTextField();
    browseBtn = new JButton();
    label2 = new JLabel();
    scrollPane1 = new JScrollPane();
    converterList = new CheckBoxList();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
            new ColumnSpec[]{
                    FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                    FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                    FormSpecs.DEFAULT_COLSPEC,
                    FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                    FormSpecs.DEFAULT_COLSPEC,
                    FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
                    FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                    FormSpecs.DEFAULT_COLSPEC
            },
            new RowSpec[]{
                    FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.LINE_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.LINE_GAP_ROWSPEC,
                    FormSpecs.DEFAULT_ROWSPEC,
                    FormSpecs.LINE_GAP_ROWSPEC,
                    new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
            }));

    //---- label1 ----
    label1.setText("File:");
    add(label1, cc.xywh(1, 1, 7, 1));
    add(fileFld, cc.xywh(3, 3, 5, 1));

    //---- browseBtn ----
    browseBtn.setText("Browse");
    add(browseBtn, cc.xy(9, 3));

    //---- label2 ----
    label2.setText("Converters:");
    add(label2, cc.xywh(1, 5, 7, 1));

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView(converterList);
    }
    add(scrollPane1, cc.xywh(3, 7, 7, 1));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JTextField fileFld;
  private JButton browseBtn;
  private JLabel label2;
  private JScrollPane scrollPane1;
  private CheckBoxList converterList;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
