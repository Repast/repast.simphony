/*CopyrightHere*/
package repast.simphony.data.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class FileChooserStep extends PanelWizardStep {
	private static final long serialVersionUID = 2818149899413910157L;

	public String getFileName() {
		System.out.println(this.getDefaultFileExtension());
		System.out.println(this.fileNameField.getText());
		if (this.fileNameField.getText().endsWith(
				this.getDefaultFileExtension())) {
			System.out.println("10");
			return this.fileNameField.getText();
		} else {
			System.out.println("20");
			return this.fileNameField.getText()
					+ this.getDefaultFileExtension();
		}
	}

	public String defaultFileExtension = "";

	public String getDefaultFileExtension() {
		return defaultFileExtension;
	}

	public void setDefaultFileExtension(String defaultExtension) {
		this.defaultFileExtension = defaultExtension;
	}

	public FileChooserStep(String title, String subTitle,
			String defaultFileExtension) {
		super(title, subTitle);
		this.defaultFileExtension = defaultFileExtension;
		initComponents();
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				File f = new File(fileNameField.getText());
				JFileChooser chooser;
				if (f.exists())
					chooser = new JFileChooser(f);
				else if (f.getParent() != null && f.getParentFile().exists())
					chooser = new JFileChooser(f.getParentFile());
				else
					chooser = new JFileChooser();
				chooser.showSaveDialog(FileChooserStep.this);
				File file = chooser.getSelectedFile();
				if (file != null) {
					String fileName = file.getAbsolutePath();
					if (!fileName.endsWith(getDefaultFileExtension())) {
						fileName = fileName + getDefaultFileExtension();
					}
					fileNameField.setText(fileName);
				}

			}
		});
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory
				.getInstance();
		separator1 = compFactory.createSeparator("Output File Properties");
		label1 = new JLabel();
		fileNameField = new JTextField();
		browseBtn = new JButton();
		panel1 = new JPanel();
		warningLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
						FormSpec.DEFAULT_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC }));
		add(separator1, cc.xywh(1, 1, 5, 1));

		// ---- label1 ----
		label1.setText("Main File Name:");
		add(label1, cc.xy(1, 3));

		// ---- fileNameField ----
		fileNameField.setText(" ");
		add(fileNameField, cc.xy(3, 3));

		// ---- browseBtn ----
		browseBtn.setText("Browse");
		add(browseBtn, cc.xy(5, 3));

		// ======== panel1 ========
		{
			panel1.setLayout(new FormLayout("default:grow, default:grow",
					"fill:default:grow, fill:default:grow"));
		}
		add(panel1, cc.xywh(1, 5, 5, 1));

		// ---- warningLabel ----
		warningLabel.setText(" ");
		add(warningLabel, cc.xywh(1, 7, 3, 1));
		// //GEN-END:initComponents

		fileNameField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateComplete();
			}

			public void insertUpdate(DocumentEvent e) {
				updateComplete();
			}

			public void removeUpdate(DocumentEvent e) {
				updateComplete();
			}
		});

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComponent separator1;
	private JLabel label1;
	private JTextField fileNameField;
	private JButton browseBtn;
	private JPanel panel1;
	private JLabel warningLabel;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	@Override
	public void init(WizardModel wizardModel) {
		super.init(wizardModel);

	}

	@Override
	public void prepare() {
		super.prepare();
		updateComplete();
	}

	protected void updateComplete() {
		setComplete(!fileNameField.getText().equals(""));
	}
}
