package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;

/**
 * Adapts a PanelWizardStep to OptionsDialogContent. This allows a panel wizard step
 * to be used in an options dialog.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class OptionsContentWizardStepAdapter implements OptionsDialogContent {

	private PanelWizardStep step;
	private JPanel panel = new JPanel(new BorderLayout());
	private TitleComponent title;
	
	/**
	 * Creates an OptionsContentWizardStepAdapter for the specified wizard step.
	 * @param step
	 */
	public OptionsContentWizardStepAdapter(Wizard wizard, PanelWizardStep step) {
		this.step = step;
		panel.add(step, BorderLayout.CENTER);
		title = new TitleComponent(wizard);
		panel.add(title, BorderLayout.NORTH);
	}

	/**
	 * Invoked when the apply button is pressed. This calls step.applyState()
	 */
	public void apply() throws InvalidStateException {
		try {
			step.applyState();
		} catch (org.pietschy.wizard.InvalidStateException e) {
			throw new repast.simphony.ui.plugin.editor.InvalidStateException(e);
		}
	}

	/**
	 * Invoked when the cancel button is pressed.
	 */
	public void cancel() {

	}

	/**
	 * Gets the panel to be shown in the dialog.
	 *
	 * @return the panel to be shown in the dialog.
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Invoked when ok button is pressed.
	 */
	public void ok() throws InvalidStateException {
		try {
			step.applyState();
		} catch (org.pietschy.wizard.InvalidStateException e) {
			throw new repast.simphony.ui.plugin.editor.InvalidStateException(e);
		}

	}

	/**
	 * Invoked when this OptionsDialogContent is selected.
	 */
	public void selected() {
		step.prepare();
		title.setTitle(step.getName());
		title.setSummary(step.getSummary());
	}
}
