package repast.simphony.dataLoader.ui;

import repast.simphony.dataLoader.engine.ClassNameDataLoaderAction;
import repast.simphony.ui.plugin.ActionEditorCreator;
import repast.simphony.ui.plugin.ActionUI;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class CNDataLoaderActionEditorCreator implements ActionEditorCreator<ClassNameDataLoaderAction> {

	public ActionUI createEditor(ClassNameDataLoaderAction action) {
		return new CNDataLoaderActionUI(action);
	}

	public Class getActionType() {
		return ClassNameDataLoaderAction.class;
	}
}
