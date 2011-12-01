package repast.simphony.userpanel.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.JPopupMenu;

import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.tree.ScenarioTreeEvent;

public class UserPanelActionUI implements ActionUI {

	private DefaultUserPanelDescriptorAction action;
	
	public UserPanelActionUI(DefaultUserPanelDescriptorAction action){
		this.action = action;
	}
	@Override
	public Editor getEditor(ScenarioTreeEvent evt) {
		List<Class<?>> clazzes = new ImplementingClassesFinder(evt.getScenario(), UserPanelCreator.class).findClasses();
		UserPanelEditorWizard wizard = new UserPanelEditorWizard(clazzes,action.getDescriptor());
		return new UserPanelEditor(wizard,evt,action);
	}

	public String getLabel() {
		return action.getDescriptor().getName();
	}

	public JPopupMenu getPopupMenu(ScenarioTreeEvent evt) {
		return null;
	}

}
