/*
 * Created by JFormDesigner on Thu Aug 16 15:18:27 EDT 2007
 */

package repast.simphony.ui.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.ui.Imageable;
import repast.simphony.ui.ImageableJComponentAdapter;
import saf.core.ui.util.DoubleDocument;
import saf.core.ui.util.FileChooserUtilities;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #2
 */
public class MovieMakerDialog extends JDialog {

  private class DocListener implements DocumentListener {

    private JTextField fld;

    public DocListener(JTextField fld) {
      this.fld = fld;
    }


    public void insertUpdate(DocumentEvent e) {
      okButton.setEnabled(fld.getText().trim().length() != 0);
    }

    public void removeUpdate(DocumentEvent e) {
      okButton.setEnabled(fld.getText().trim().length() != 0);
    }

    public void changedUpdate(DocumentEvent e) {
      okButton.setEnabled(fld.getText().trim().length() != 0);
    }
  }

  private File file;
  private MovieMakerConfig config;

  public MovieMakerDialog(Frame owner) {
    super(owner);
    initComponents();
    addListeners();
  }

  public MovieMakerDialog(Dialog owner) {
    super(owner);
    initComponents();
    addListeners();
  }

  private void addListeners() {
    intervalFld.setDocument(new DoubleDocument());
    intervalFld.getDocument().addDocumentListener(new DocListener(intervalFld));
    intervalFld.setText("1.0");
    startingFld.setDocument(new DoubleDocument());
    startingFld.getDocument().addDocumentListener(new DocListener(startingFld));
    startingFld.setText("1.0");

    fileFld.setBackground(dialogPane.getBackground());

    browseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        getFile();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MovieMakerDialog.this.dispose();
      }
    });

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MovieMakerDialog.this.dispose();
        config = new MovieMakerConfig(Double.parseDouble(startingFld.getText()),
                Double.parseDouble(intervalFld.getText()), file);
      }
    });
  }

  private void getFile() {
    File aFile = FileChooserUtilities.getSaveFile(file, new FileFilter() {
      public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith(".mov");
      }

      public String getDescription() {
        return "Quicktime movie (*.mov)";
      }
    });

    if (aFile != null) {
      if (!aFile.getName().endsWith(".mov")) {
        aFile = new File(aFile.getParentFile(), aFile.getName() + ".mov");
      }
      fileFld.setText(aFile.getAbsolutePath());
      file = aFile;
    }

    okButton.setEnabled(file != null);
  }

  public MovieMakerConfig getConfiguration() {
    return config;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    MovieMakerDialog dialog = new MovieMakerDialog(frame);
    dialog.pack();
    dialog.setVisible(true);
    frame.dispose();
  }

  /**
   * Gets an action that will show the movie maker dialog over the specified frame and
   * set up frame capture of the specified component.
   * @param parentFrame the dialog's parent
   * @param comp the component to use as the frame source
   * @return the created action.
   */
  public static Action getButtonAction(JFrame parentFrame, JComponent comp) {
    return new MovieMakerAction(parentFrame, comp);

  }

  private static class MovieMakerAction extends AbstractAction {

    private JFrame frame;
    private Imageable imageable;

    public MovieMakerAction(JFrame frame, JComponent comp) {
      this.frame = frame;
      if (comp instanceof Imageable) imageable = (Imageable) comp;
      else imageable = new ImageableJComponentAdapter(comp);
    }


    public void actionPerformed(ActionEvent e) {
      MovieMakerDialog dialog = new MovieMakerDialog(frame);
      dialog.setVisible(true);
      MovieMakerConfig config = dialog.getConfiguration();
      if (config != null) {
        config.schedule(RunEnvironment.getInstance().getCurrentSchedule(), imageable);
      }
    }
  }


  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    dialogPane = new JPanel();
    contentPanel = new JPanel();
    panel1 = new JPanel();
    title1 = compFactory.createTitle("<html><b>Export Movie</b><br>\nPlease specify a file and the frame capture rate.\n");
    label1 = new JLabel();
    fileFld = new JTextField();
    browseBtn = new JButton();
    label2 = new JLabel();
    intervalFld = new JTextField();
    panel2 = new JPanel();
    label3 = new JLabel();
    startingFld = new JTextField();
    buttonBar = new JPanel();
    okButton = new JButton();
    cancelButton = new JButton();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setModal(true);
    setTitle("Movie Export");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
    	dialogPane.setBorder(Borders.DIALOG);
    	dialogPane.setLayout(new BorderLayout());

    	//======== contentPanel ========
    	{
    		contentPanel.setLayout(new FormLayout(
    			new ColumnSpec[] {
    				FormSpecs.RELATED_GAP_COLSPEC,
    				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    				ColumnSpec.decode("max(default;50dlu)"),
    				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
    				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
    				FormSpecs.DEFAULT_COLSPEC
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

    		//======== panel1 ========
    		{
    			panel1.setBackground(Color.white);
    			panel1.setBorder(LineBorder.createBlackLineBorder());
    			panel1.setLayout(new FlowLayout(FlowLayout.LEFT));

    			//---- title1 ----
    			title1.setHorizontalAlignment(SwingConstants.LEFT);
    			title1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
    			panel1.add(title1);
    		}
    		contentPanel.add(panel1, cc.xywh(1, 1, 7, 1));

    		//---- label1 ----
    		label1.setText("File Name:");
    		contentPanel.add(label1, cc.xywh(1, 3, 5, 1));

    		//---- fileFld ----
    		fileFld.setEditable(false);
    		contentPanel.add(fileFld, cc.xywh(2, 5, 4, 1));

    		//---- browseBtn ----
    		browseBtn.setText("Browse");
    		browseBtn.setToolTipText("Click to choose file");
    		contentPanel.add(browseBtn, cc.xy(7, 5));

    		//---- label2 ----
    		label2.setText("Capture Frame Every:");
    		contentPanel.add(label2, cc.xywh(1, 7, 7, 1));
    		contentPanel.add(intervalFld, cc.xy(3, 9));

    		//======== panel2 ========
    		{
    			panel2.setLayout(new FormLayout(
    				"default, default:grow",
    				"default"));

    			//---- label3 ----
    			label3.setText("ticks, starting at ");
    			panel2.add(label3, cc.xy(1, 1));
    			panel2.add(startingFld, cc.xy(2, 1));
    		}
    		contentPanel.add(panel2, cc.xywh(5, 9, 3, 1));
    	}
    	dialogPane.add(contentPanel, BorderLayout.CENTER);

    	//======== buttonBar ========
    	{
    		buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
    		buttonBar.setLayout(new FormLayout(
    			new ColumnSpec[] {
    				FormSpecs.GLUE_COLSPEC,
    				FormSpecs.BUTTON_COLSPEC,
    				FormSpecs.RELATED_GAP_COLSPEC,
    				FormSpecs.BUTTON_COLSPEC
    			},
    			RowSpec.decodeSpecs("pref")));

    		//---- okButton ----
    		okButton.setText("OK");
    		okButton.setEnabled(false);
    		buttonBar.add(okButton, cc.xy(2, 1));

    		//---- cancelButton ----
    		cancelButton.setText("Cancel");
    		buttonBar.add(cancelButton, cc.xy(4, 1));
    	}
    	dialogPane.add(buttonBar, BorderLayout.SOUTH);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JPanel dialogPane;
  private JPanel contentPanel;
  private JPanel panel1;
  private JLabel title1;
  private JLabel label1;
  private JTextField fileFld;
  private JButton browseBtn;
  private JLabel label2;
  private JTextField intervalFld;
  private JPanel panel2;
  private JLabel label3;
  private JTextField startingFld;
  private JPanel buttonBar;
  private JButton okButton;
  private JButton cancelButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
