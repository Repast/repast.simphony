package repast.simphony.scenario;

import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.schedule.Descriptor;

public abstract class AbstractDescriptorControllerActionIO<T extends DescriptorControllerAction, U extends Descriptor>
		extends AbstractControllerActionIO<T> {
	
	public AbstractDescriptorControllerActionIO(Class<T> actionClass, Class<U> descriptorClass) {
		super(actionClass);
	}

	public ActionSaver getActionSaver() {
		return new DescriptorActionSaver();
	}
}
