package repast.simphony.ui.plugin;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.ui.DefaultActionUI;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public class ComponentActionEditorExtensions {

	// key: action class name
	// value: the creator for that type of class
	private Map<String, ActionEditorCreator> creatorMap = new HashMap<String, ActionEditorCreator>();

	// key: action class name
	// value: the label.
	// this is used to create a default editor for default actions
	private Map<String, String> labelMap = new HashMap<String, String>();

	public void addActionEditorCreator(String className, ActionEditorCreator creator) {
		creatorMap.put(className, creator);
	}

	/**
	 * Adds a default label only ui for the specified class.
	 *
	 * @param actionClass the Class of the controller action
	 * @param label the label for the action in the ui
	 */
	public void addDefaultUI(Class actionClass, String label) {
		labelMap.put(actionClass.getName(), label);
	}

	public ActionUI getUI(ControllerAction action) {
		String name = action.getClass().getName();
		ActionEditorCreator creator = creatorMap.get(name);
		if (creator == null) {
			String label = labelMap.get(name);
			if (label == null) label = action.toString();
			return new DefaultActionUI(label);
		}

		return creator.createEditor(action);
	}
}
