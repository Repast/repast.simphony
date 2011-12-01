package repast.simphony.ui.plugin.editor;

import javax.swing.JPopupMenu;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.Scenario;
import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * This is a simple abstract {@link ActionUI} implementation that is to be used for ui's where the
 * editor is based on a set of {@link PanelWizardStep}s. This uses a {@link OptionsEditorDialog} for
 * its editor, and steps from implementations paths for the content.
 * 
 * @author Jerry Vos
 */
public abstract class AbstractWizardActionUI<T extends ControllerAction> implements ActionUI {

	protected T action;

	protected String title;

	protected String label;

	/**
	 * Constructs this with the specified action to work on, the title for any created editors and
	 * the label for displaying for actions.
	 * 
	 * @param action
	 *            the action this UI represents
	 * @param title
	 *            the title for any editors
	 * @param label
	 *            the label for the action
	 */
	public AbstractWizardActionUI(T action, String title, String label) {
		super();
		this.action = action;
		this.title = title;
		this.label = label;
	}

	/**
	 * Retrieves the path that contains steps that will be the content for the dialog. This path
	 * will be the one passed to the
	 * {@link #getWizardModel(SimplePath, Scenario, Object, ScenarioTreeEvent)} method.
	 * 
	 * @return a path of PanelWizardSteps
	 */
	protected abstract SimplePath getPath();

	/**
	 * Retrieves the {@link WizardModel} to be used when creating the wizard used when adding steps
	 * to the created dialog.
	 * 
	 * @param path
	 *            the path for the wizard (created with {@link #getPath()})
	 * @param scenario
	 *            the scenario (retrieved from the passed in event)
	 * @param contextId
	 *            the context id (retrieved from the passed in event)
	 * @param evt
	 *            the event that initiated the editor request (passed in in case extra info is
	 *            needed)
	 * @return the model to be used when creating the editor
	 */
	protected abstract WizardModel getWizardModel(SimplePath path, Scenario scenario,
			Object contextId, ScenarioTreeEvent evt);

	/**
	 * Creates and returns an {@link OptionsEditorDialog} whose content is the steps from
	 * {@link #getPath()} whose model is from
	 * {@link #getWizardModel(SimplePath, repast.simphony.scenario.Scenario , Object, ScenarioTreeEvent)}, and whose title is
	 * this wizard's title.
	 * 
	 * @see OptionsEditorDialog#create(WizardModel, org.pietschy.wizard.models.Path, String)
	 * 
	 * @return a {@link OptionsEditorDialog}
	 */
	public OptionsEditorDialog getEditor(ScenarioTreeEvent evt) {
		Object contextId = evt.getContextID();
		Scenario scenario = evt.getScenario();

		SimplePath path = getPath();

		return OptionsEditorDialog.create(getWizardModel(path, scenario, contextId, evt), path,
				title);
	}

	/**
	 * Returns the label specified in the construction.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Always returns null.
	 */
	public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
		return null;
	}
}
