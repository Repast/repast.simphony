package repast.simphony.freezedry.engine;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.freezedry.wizard.DFFreezerControllerAction;
import repast.simphony.scenario.DefaultControllerActionIO;

/**
 * @author Nick Collier
 */
public class DelimitedFileFreezerActionIO extends DefaultControllerActionIO<DFFreezerControllerAction> {
	public DelimitedFileFreezerActionIO() {
		super(DFFreezerControllerAction.class, ControllerActionConstants.MISC_ROOT);
	}
}
