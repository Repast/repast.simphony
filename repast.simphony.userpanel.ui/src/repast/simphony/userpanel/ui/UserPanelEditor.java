package repast.simphony.userpanel.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

/**
 * Adaptor for UserPanelEditorWizard to Editor interface.
 * @author jozik
 *
 */
public class UserPanelEditor implements Editor {

	private UserPanelEditorWizard wizard;
	private ScenarioTreeEvent evt;
	private DefaultUserPanelDescriptorAction action;
	
	public UserPanelEditor(UserPanelEditorWizard wizard, ScenarioTreeEvent evt, DefaultUserPanelDescriptorAction action){
		this.wizard = wizard;
		this.evt = evt;
		this.action = action;
	}
	
	@Override
	public void display(JFrame parent) {
		wizard.showDialog(parent, "User Panel Creator");
	}

	@Override
	public void display(JDialog parent) {
		wizard.showDialog(parent, "User Panel Creator");
	}

	@Override
	public boolean wasCanceled() {
		
		return wizard.wasCancelled();
	}

}
