package repast.simphony.ui.newscenario;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import saf.core.ui.util.DirectoryChooser;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ScenarioSelectionPanel extends PanelWizardStep {

	private DirectoryChooser chooser;
	private NewWizardModel model;

	public ScenarioSelectionPanel() {
		super("Scenario Directory", "Select a scenario directory. The model specification file" +
						" will be copied into this directory and a scenario created.");
		this.setLayout(new BorderLayout());
		chooser = new DirectoryChooser(".");
		chooser.setControlButtonsAreShown(false);
		this.add(chooser.getPanel(), BorderLayout.NORTH);
		chooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
					File file = (File) evt.getNewValue();
					setComplete(file != null);
				}
			}
		});
	}

	public void init(WizardModel model) {
		this.model = (NewWizardModel) model;
	}

	@Override
	public void prepare() {
		super.prepare();
		File f = model.getScenarioPath();
		if (model.getScorePath() != null) f = model.getScorePath();
		if (f == null) f = new File(".");
		chooser.setSelectedFile(f);
	}

	@Override
	public void applyState() throws org.pietschy.wizard.InvalidStateException {
		super.applyState();
		model.setScenarioPath(chooser.getSelectedFile());
	}
}
