package repast.simphony.ui.plugin;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.engine.environment.ControllerAction;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public class ActionEditorExtensions {

	// key: for child actions the class name
	// key: for parent actions the id returned by 
	private Map<String, ActionEditorCreator> creatorMap = new HashMap<String, ActionEditorCreator>();

	public ActionEditorCreator getCreator(ControllerAction action) {
		return creatorMap.get(action.getClass().getName());
	}

	public ActionEditorCreator getCreator(String key) {
		return creatorMap.get(key);
	}
}
