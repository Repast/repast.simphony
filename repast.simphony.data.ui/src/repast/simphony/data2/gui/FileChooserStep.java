package repast.simphony.data2.gui;

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

import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * Wizard step for choosing a file to pass to a data analysis plugin.
 * 
 * @author Jerry Vos
 * @author Eric Tatara
 */
public class FileChooserStep extends PluginWizardStep {
	private static final long serialVersionUID = 2818149899413910157L;

	private JComponent separator1;
	private JLabel label1;
	private JTextField fileNameField;
	private JButton browseBtn;
	private JPanel panel1;
	private JLabel warningLabel;
	
	public String defaultFileExtension = "";

	public FileChooserStep(String title, String subTitle,
			String defaultFileExtension) {
		super(title, subTitle);
		
		this.defaultFileExtension = defaultFileExtension;
		
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

	@Override
	protected  JPanel getContentPanel(){
		JPanel panel = new JPanel();
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		separator1 = compFactory.createSeparator("Output File Properties");
		label1 = new JLabel();
		fileNameField = new JTextField();
		browseBtn = new JButton();
		panel1 = new JPanel();
		warningLabel = new JLabel();
		CellConstraints cc = new CellConstraints();
	
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
						FormSpec.DEFAULT_GROW),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC }, new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.UNRELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC }));
		panel.add(separator1, cc.xywh(1, 1, 5, 1));

		label1.setText("Main File Name:");
		panel.add(label1, cc.xy(1, 3));

		fileNameField.setText(" ");
		panel.add(fileNameField, cc.xy(3, 3));

		browseBtn.setText("Browse");
		panel.add(browseBtn, cc.xy(5, 3));

		
		panel1.setLayout(new FormLayout("default:grow, default:grow",
					"fill:default:grow, fill:default:grow"));
		panel.add(panel1, cc.xywh(1, 5, 5, 1));

		warningLabel.setText(" ");
		panel.add(warningLabel, cc.xywh(1, 7, 3, 1));
		

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

		return panel;
	}

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
	
	public String getFileName() {
		if (this.fileNameField.getText().endsWith(
				this.getDefaultFileExtension())) {
			return this.fileNameField.getText();
		} else {
			return this.fileNameField.getText()
					+ this.getDefaultFileExtension();
		}
	}
	
	public String getDefaultFileExtension() {
		return defaultFileExtension;
	}

	public void setDefaultFileExtension(String defaultExtension) {
		this.defaultFileExtension = defaultExtension;
	}
}
