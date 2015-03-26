package repast.simphony.ui.plugin.editor;

import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardListener;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.Scenario;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * This is an abstract {@link Editor} implementation that is to be used for ui's where the editor is
 * based on a set of {@link PanelWizardStep}s. This uses a {@link OptionsEditorDialog} for its
 * editor, and steps from implementations paths for the content.
 * 
 * @author Jerry Vos
 */
public abstract class AbstractWizardEditor<T extends ControllerAction> extends OptionsEditorDialog {

	protected T action;
	protected Scenario scenario;
	protected Object contextId;
	
	private boolean displayBuilt;
	private Wizard wizard;

	/**
	 * Constructs this with the specified action to work on, the title for any created editors and
	 * the label for displaying for actions.
	 * 
	 * @param action
	 *            the action this UI represents
	 * @param title
	 *            the title for any editors
	 * @param scenario
	 *            the scenario being worked on
	 * @param contextId
	 *            the id of the context the action is in
	 */
	public AbstractWizardEditor(T action, Scenario scenario, Object contextId, String title) {
		super(title);
		this.scenario = scenario;
		this.contextId = contextId;
		this.action = action;
		
		this.displayBuilt = false;
	}

	/**
	 * Retrieves the path that contains steps that will be the content for the dialog. This path
	 * will be the one passed to the
	 * {@link #getWizardModel(SimplePath, repast.simphony.scenario.Scenario , Object, ScenarioTreeEvent)} method.
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
	 *            the scenario
	 * @param contextId
	 *            the context id
	 * @return the model to be used when creating the editor
	 */
	protected abstract WizardModel getWizardModel(SimplePath path, Scenario scenario,
			Object contextId);

	/**
	 * Display this Editor with the specified component as a parent. <b>The resulting display should
	 * be modal.</b><p/>
	 * 
	 * If this editor's steps have not yet been added they will be added at this time.
	 * 
	 * @param parent
	 *            the parent of the dialog
	 */
	@Override
	public void display(JDialog parent) {
		buildEditor(scenario, contextId);
		super.display(parent);
	}
	
	/**
	 * Display this Editor with the specified component as a parent. <b>The resulting display should
	 * be modal.</b><p/>
	 * 
	 * If this editor's steps have not yet been added they will be added at this time.
	 * 
	 * @param parent
	 *            the parent of the dialog
	 */
	@Override
	public void display(JFrame parent) {
		buildEditor(scenario, contextId);
		super.display(parent);
	}
	
	/**
	 * Creates and returns an {@link OptionsEditorDialog} whose content is the steps from
	 * {@link #getPath()} whose model is from
	 * {@link #getWizardModel(SimplePath, Scenario, Object, ScenarioTreeEvent)}, and whose title is
	 * this wizard's title.
	 * 
	 * @see OptionsEditorDialog#create(WizardModel, org.pietschy.wizard.models.Path, String)
	 * 
	 * @return a {@link OptionsEditorDialog}
	 */
	protected void buildEditor(Scenario scenario, Object contextId) {
		if (displayBuilt) {
			return;
		}
		displayBuilt = true;
		SimplePath path = getPath();

		WizardModel wizardModel = getWizardModel(path, scenario, contextId);
		wizard = new PluginWizard(wizardModel);
		if (wizardModel instanceof WizardListener) {
			wizard.addWizardListener((WizardListener) wizardModel);			
		}
		for (PanelWizardStep step : (Collection<PanelWizardStep>) path.getSteps()) {
			addContent(null, step, wizard);
		}
	}

	@Override
	protected void ok() {
		super.ok();
		wizard.close();
	}
	
	@Override
	protected void cancel() {
		super.cancel();
		wizard.cancel();
	}
}
