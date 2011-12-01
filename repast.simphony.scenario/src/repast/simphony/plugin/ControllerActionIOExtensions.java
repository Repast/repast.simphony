package repast.simphony.plugin;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.scenario.ControllerActionIO;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public class ControllerActionIOExtensions {

	private Map<String, ControllerActionIO> xmlIORegistry = new HashMap<String, ControllerActionIO>();
	private Map<String, ControllerActionIO> actionIORegistry = new HashMap<String, ControllerActionIO>();

	public void addControllerActionIO(ControllerActionIO io) {
		xmlIORegistry.put(io.getSerializationID(), io);
		actionIORegistry.put(io.getActionClass().getName(), io);
	}

	public ControllerActionIO getControllerActionIO(String xmlElementID) {
		return xmlIORegistry.get(xmlElementID);
	}

	public ControllerActionIO getControllerActionIO(Class actionClass) {
		return actionIORegistry.get(actionClass.getName());
	}

	public Iterable<ControllerActionIO> controllerActionsIOs() {
		return xmlIORegistry.values();
	}
}
