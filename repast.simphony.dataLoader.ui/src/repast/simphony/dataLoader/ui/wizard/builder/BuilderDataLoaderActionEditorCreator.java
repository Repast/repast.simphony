package repast.simphony.dataLoader.ui.wizard.builder;

import repast.simphony.ui.DefaultActionUI;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 */
public class BuilderDataLoaderActionEditorCreator implements ActionEditorCreator<BuilderDataLoaderControllerAction> {

	public ActionUI createEditor(BuilderDataLoaderControllerAction action) {
		return new DefaultActionUI("Generate " + ((BuilderContextBuilder) action.getBuilder()).getDescriptor().getName());
	}

	public Class getActionType() {
		return BuilderContextBuilder.class;
	}
}
