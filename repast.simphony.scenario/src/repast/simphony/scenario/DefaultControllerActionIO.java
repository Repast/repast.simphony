package repast.simphony.scenario;

import java.io.File;

import repast.simphony.engine.environment.ControllerAction;

public class DefaultControllerActionIO<T extends ControllerAction> extends AbstractControllerActionIO<T> {
	private String actionRoot;

	public DefaultControllerActionIO(Class<T> actionClass, String actionRoot) {
		super(actionClass);
		this.actionRoot = actionRoot;
	}

	public ActionLoader getActionLoader(File actionFile, Object contextID) {
		return new DefaultActionLoader<T>(actionFile, contextID, actionClass, actionRoot);
	}

	public ActionSaver getActionSaver() {
		return new DefaultActionSaver<T>();
	}
}
