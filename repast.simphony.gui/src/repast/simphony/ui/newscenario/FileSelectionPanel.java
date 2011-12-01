package repast.simphony.ui.newscenario;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class FileSelectionPanel extends PanelWizardStep {

	private JFileChooser chooser;
	private NewWizardModel model;

	public FileSelectionPanel() {
		super("Model Specification Selection", "Select your model.score file");
		this.setLayout(new BorderLayout());
		chooser = new JFileChooser();
		chooser.setControlButtonsAreShown(false);
		this.add(chooser, BorderLayout.CENTER);
		chooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
					File file = (File) evt.getNewValue();
					setComplete(file != null && file.getName().endsWith(".score"));
				}
			}
		});

		chooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith("score");
			}

			public String getDescription() {
				return "Model Specification .score file";
			}
		});
		
	}


	public void init(WizardModel model) {
		this.model = (NewWizardModel) model;
	}

	@Override
	public void prepare() {
		super.prepare();
		chooser.setSelectedFile(model.getSpecPath());
	}

	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		model.setSpecPath(chooser.getSelectedFile());
		model.setScorePath(chooser.getSelectedFile().getParentFile());
	}


	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.setControlButtonsAreShown(false);
		JPanel p = new JPanel(new BorderLayout());
		p.add(chooser, BorderLayout.CENTER);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}


}
