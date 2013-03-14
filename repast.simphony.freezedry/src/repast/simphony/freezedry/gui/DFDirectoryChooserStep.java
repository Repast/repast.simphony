/*CopyrightHere*/
package repast.simphony.freezedry.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import repast.simphony.freezedry.datasource.DFClassLister;
import repast.simphony.util.wizard.DynamicWizardModel;
import repast.simphony.util.wizard.ModelAwarePanelStep;
import saf.core.ui.util.FileChooserUtilities;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class DFDirectoryChooserStep<T extends DynamicWizardModel> extends ModelAwarePanelStep<T> implements ActionListener, ClassRetrievable {
  private static final long serialVersionUID = 8299821807954637639L;

  private static final MessageCenter LOG = MessageCenter
          .getMessageCenter(DFDirectoryChooserStep.class);

  private boolean read = false;

  public DFDirectoryChooserStep(String title, String caption, boolean read) {
    super(title, caption);
    this.read = read;

    initComponents();
  }

  public DFDirectoryChooserStep() {
    this("", "", false);
  }

  private void otherButtonStateChanged(ChangeEvent e) {
    otherField.setEnabled(otherButton.isSelected());
  }

  private void browseButtonActionPerformed(ActionEvent e) {
    File dir = FileChooserUtilities.getFile("Choose FreezeDry zip", "Choose", new File(""),
            new FileFilter() {
              public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".zip");
              }

              public String getDescription() {
                return "Zip Archive (.zip)";
              }
            });
    if (dir != null) {
      dirNameField.setText(dir.getAbsolutePath());
    }
  }

  @SuppressWarnings("serial")
  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    separator3 = compFactory.createSeparator("Data Loader Details");
    label2 = new JLabel();
    dirNameField = new JTextField();
    browseButton = new JButton();
    separator2 = compFactory.createSeparator("Data File Details");
    panel1 = new JPanel();
    commaButton = new JRadioButton();
    colonButton = new JRadioButton();
    semiColonButton = new JRadioButton();
    tabButton = new JRadioButton();
    spaceButton = new JRadioButton();
    panel2 = new JPanel();
    otherButton = new JRadioButton();
    otherField = new JTextField();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout(new FormLayout(
    	new ColumnSpec[] {
    		new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
    		FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    		new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, FormSpec.DEFAULT_GROW),
    		FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    		new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.NO_GROW)
    	},
    	new RowSpec[] {
    		FormSpecs.DEFAULT_ROWSPEC,
    		FormSpecs.LINE_GAP_ROWSPEC,
    		FormSpecs.DEFAULT_ROWSPEC,
    		FormSpecs.LINE_GAP_ROWSPEC,
    		FormSpecs.DEFAULT_ROWSPEC,
    		FormSpecs.LINE_GAP_ROWSPEC,
    		FormSpecs.DEFAULT_ROWSPEC,
    		FormSpecs.LINE_GAP_ROWSPEC,
    		FormSpecs.DEFAULT_ROWSPEC
    	}));
    add(separator3, cc.xywh(1, 1, 5, 1));

    //---- label2 ----
    label2.setText("Data File:");
    add(label2, cc.xy(1, 3));

    //---- dirNameField ----
    dirNameField.setText("c:\\program files\\example\\exampel32\\data.csv");
    add(dirNameField, cc.xy(3, 3));

    //---- browseButton ----
    browseButton.setText("Browse...");
    browseButton.setMnemonic('B');
    browseButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		browseButtonActionPerformed(e);
    	}
    });
    add(browseButton, cc.xy(5, 3));
    add(separator2, cc.xywh(1, 5, 5, 1));

    //======== panel1 ========
    {
    	panel1.setLayout(new GridBagLayout());
    	((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
    	((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
    	((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 0.0, 1.0E-4};
    	((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

    	//---- commaButton ----
    	commaButton.setText("Comma (,)");
    	commaButton.setSelected(true);
    	panel1.add(commaButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    		GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));

    	//---- colonButton ----
    	colonButton.setText("Colon (:)");
    	panel1.add(colonButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
    		GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));

    	//---- semiColonButton ----
    	semiColonButton.setText("Semicolon (;)");
    	panel1.add(semiColonButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
    		GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));

    	//---- tabButton ----
    	tabButton.setText("Tab");
    	panel1.add(tabButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
    		GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));

    	//---- spaceButton ----
    	spaceButton.setText("Space");
    	panel1.add(spaceButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
    		GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));

    	//======== panel2 ========
    	{
    		panel2.setLayout(new GridBagLayout());
    		((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 25, 0};
    		((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
    		((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
    		((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

    		//---- otherButton ----
    		otherButton.setText("Other:");
    		otherButton.addChangeListener(new ChangeListener() {
    			public void stateChanged(ChangeEvent e) {
    				otherButtonStateChanged(e);
    			}
    		});
    		panel2.add(otherButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    			new Insets(0, 0, 0, 0), 0, 0));

    		//---- otherField ----
    		otherField.setEnabled(false);
    		panel2.add(otherField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    			new Insets(0, 0, 0, 0), 0, 0));
    	}
    	panel1.add(panel2, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
    		GridBagConstraints.CENTER, GridBagConstraints.BOTH,
    		new Insets(0, 0, 0, 0), 0, 0));
    }
    add(panel1, cc.xywh(1, 7, 5, 1));

    //---- delimiterGroup ----
    ButtonGroup delimiterGroup = new ButtonGroup();
    delimiterGroup.add(commaButton);
    delimiterGroup.add(colonButton);
    delimiterGroup.add(semiColonButton);
    delimiterGroup.add(tabButton);
    delimiterGroup.add(spaceButton);
    delimiterGroup.add(otherButton);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents

    Enumeration<AbstractButton> enumer = delimiterGroup.getElements();
    while (enumer.hasMoreElements()) {
      enumer.nextElement().addActionListener(this);
    }

    dirNameField.getDocument().addDocumentListener(new DocumentListener() {

      public void insertUpdate(DocumentEvent e) {
        setComplete(isFileNameValid()
                && !(otherButton.isSelected() && otherField.getText().equals("")));
      }

      public void removeUpdate(DocumentEvent e) {
        setComplete(isFileNameValid()
                && !(otherButton.isSelected() && otherField.getText().equals("")));
      }

      public void changedUpdate(DocumentEvent e) {
      }
    });

    dirNameField.setText("");
    dirNameField.grabFocus();

    otherField.setDocument(new PlainDocument() {
      @Override
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (offs != 0) {
          return;
        }
        if (str.length() > 0) {
          super.insertString(offs, str.substring(0, 1), a);
        }
      }
    });
  }

  private boolean isFileNameValid() {
    if (!dirNameField.getText().equals("")) {
      if (read) return new File(dirNameField.getText()).exists();
      return true;
    }
    return false;
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JComponent separator3;
  private JLabel label2;
  private JTextField dirNameField;
  private JButton browseButton;
  private JComponent separator2;
  private JPanel panel1;
  private JRadioButton commaButton;
  private JRadioButton colonButton;
  private JRadioButton semiColonButton;
  private JRadioButton tabButton;
  private JRadioButton spaceButton;
  private JPanel panel2;
  private JRadioButton otherButton;
  private JTextField otherField;
  // JFormDesigner - End of variables declaration  //GEN-END:variables


  @SuppressWarnings("unchecked")
  public List<Class<?>> retrieveClasses() {
    try {
      setBusy(true);
      DFClassLister lister = new DFClassLister(dirNameField.getText().trim());
      return lister.getClasses();
      } catch (ClassNotFoundException ex) {
        LOG.warn("Error loading classes", ex);
      } catch (IOException ex) {
        LOG.error("Error reading zip file", ex);

      } finally {
      setBusy(false);
    }

    return new ArrayList<Class<?>>();
  }


  public void setDir(String directoryName) {
    this.dirNameField.setText(directoryName);
  }

  public void selectDelimiter(char delimiter) {
    if (delimiter == ',') {
      commaButton.setSelected(true);
    } else if (delimiter == ';') {
      semiColonButton.setSelected(true);
    } else if (delimiter == ':') {
      colonButton.setSelected(true);
    } else if (delimiter == '\t') {
      tabButton.setSelected(true);
    } else if (delimiter == ' ') {
      spaceButton.setSelected(true);
    } else {
      otherButton.setSelected(true);
      otherField.setText("" + delimiter);
    }
  }

  public char getDelimiter() {
    if (commaButton.isSelected()) {
      return ',';
    } else if (semiColonButton.isSelected()) {
      return ';';
    } else if (colonButton.isSelected()) {
      return ':';
    } else if (tabButton.isSelected()) {
      return '\t';
    } else if (spaceButton.isSelected()) {
      return ' ';
    } else /*if (otherButton.isSelected())*/ {
      return otherField.getText().charAt(0);
    }
  }

  public String getFileName() {
    return dirNameField.getText();
  }

  public static void main(String[] args) {
    DFDirectoryChooserStep step = new DFDirectoryChooserStep();
    JFrame frame = new JFrame();
    frame.add(step);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == otherButton) {
      setComplete(!otherField.getText().equals("") && isFileNameValid());
    } else {
      setComplete(isFileNameValid());
    }
  }
}
