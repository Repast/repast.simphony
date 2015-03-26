/*CopyrightHere*/
package repast.simphony.ui.plugin.editor;

import java.util.Collection;
import java.util.HashSet;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Path;

/**
 * An extension of {@link repast.simphony.ui.plugin.editor.OptionsDialog} that adapts it to a
 * {@link repast.simphony.ui.plugin.editor.Editor} and adds some helper methods for using wizard steps in the
 * editor.
 * 
 * @author Jerry Vos
 */
public class OptionsEditorDialog extends OptionsDialog implements Editor {
	private static final long serialVersionUID = 1L;

	protected String defaultTitle;
	
	protected HashSet<Wizard> wizards;
	
	public OptionsEditorDialog() {
		this("Options Editor");
	}

	public OptionsEditorDialog(String title) {
		this.defaultTitle = title;
		
		wizards = new HashSet<Wizard>();
	}

	/**
	 * Display this Editor with the specified component as a parent. <b>The resulting display should
	 * be modal.</b>
	 * 
	 * @param parent
	 *            the parent of the dialog
	 */
	public void display(JDialog parent) {
		showDialog(parent, defaultTitle);
	}

	/**
	 * Display this Editor with the specified component as a parent. <b>The resulting display should
	 * be modal.</b>
	 * 
	 * @param parent
	 *            the parent of the dialog
	 */
	public void display(JFrame parent) {
		showDialog(parent, defaultTitle);
	}

	/**
	 * @return true if the editing was canceled, otherwise false.
	 */
	public boolean wasCanceled() {
		return super.isCanceled();
	}

	/**
	 * Adds a wizard step as content for this wizard. This is the same as
	 * <code>step.init(wizard.getModel());
	 * addContent(name, icon, new OptionsContentWizardStepAdapter(wizard, step));</code>
	 * 
	 * @param name
	 *            the name of the content
	 * @param icon
	 *            the icon for the content
	 * @param step
	 *            the step for the options
	 * @param wizard
	 *            the wizard the step is in
	 */
	public void addContent(String name, Icon icon, PanelWizardStep step, Wizard wizard) {
		step.init(wizard.getModel());
		this.addContent(name, icon, new OptionsContentWizardStepAdapter(wizard, step));
		wizards.add(wizard);
	}

	/**
	 * Adds a wizard step as content for this wizard. This is the same as
	 * <code>
	 * addContent(step.getName(), icon, new OptionsContentWizardStepAdapter(wizard, step));</code>
	 * 
	 * @param icon
	 *            the icon for the content
	 * @param step
	 *            the step for the options
	 * @param wizard
	 *            the wizard the step is in
	 */
	public void addContent(Icon icon, PanelWizardStep step, Wizard wizard) {
		this.addContent(step.getName(), icon, new OptionsContentWizardStepAdapter(wizard, step));
	}
	
	@Override
	protected void ok() {
		super.ok();
		
		for (Wizard wiz : wizards) {
			wiz.close();
		}
	}
	
	@Override
	protected void cancel() {
		super.cancel();
		
		for (Wizard wiz : wizards) {
			wiz.cancel();
		}
	}
	
	/**
	 * Creates a dialog whose content is specified by the steps in the given path. The path must
	 * only contain {@link PanelWizardStep}s, otherwise a {@link ClassCastException} will be
	 * thrown.
	 * 
	 * @param model
	 *            the model for the wizard and steps
	 * @param steps
	 *            the steps that will be the content
	 * @param title
	 *            the title of the dialog
	 * @return a new editor dialog
	 */
	public static OptionsEditorDialog create(WizardModel model, Path steps, String title) {
		Wizard wizard = new PluginWizard(model);

		OptionsEditorDialog dialog = new OptionsEditorDialog(title);

		for (PanelWizardStep step : (Collection<PanelWizardStep>) steps.getSteps()) {
			dialog.addContent(null, step, wizard);
		}

		return dialog;
	}
}
