package repast.simphony.scenario;

import repast.simphony.engine.environment.ControllerAction;


public abstract class AbstractControllerActionIO<T extends ControllerAction> implements ControllerActionIO {

	protected Class<T> actionClass;

	public AbstractControllerActionIO(Class<T> actionClass) {
		this.actionClass = actionClass;
	}
	
	public String getSerializationID() {
		return actionClass.getName();
	}
	
	public Class<T> getActionClass() {
		return actionClass;
	}

}
