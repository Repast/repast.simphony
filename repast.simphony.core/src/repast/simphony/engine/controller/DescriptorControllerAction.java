/*CopyrightHere*/
package repast.simphony.engine.controller;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.schedule.Descriptor;

/**
 * A Controller action that executes based on a
 * {@link repast.simphony.engine.schedule.Descriptor}.
 * 
 * @author Jerry Vos
 */
public interface DescriptorControllerAction<T extends Descriptor> extends ControllerAction {
	T getDescriptor();
}
