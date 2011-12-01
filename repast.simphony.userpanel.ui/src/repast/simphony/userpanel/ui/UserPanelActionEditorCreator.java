package repast.simphony.userpanel.ui;

import repast.simphony.ui.DefaultActionUI;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

public class UserPanelActionEditorCreator implements ActionEditorCreator<DefaultUserPanelDescriptorAction>{

	@Override
	public ActionUI createEditor(DefaultUserPanelDescriptorAction action) {

		return new UserPanelActionUI(action);
	}

	@Override
	public Class getActionType() {
		return DefaultUserPanelDescriptorAction.class;
	}

}
