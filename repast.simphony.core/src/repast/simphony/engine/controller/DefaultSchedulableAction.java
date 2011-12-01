/*CopyrightHere*/
package repast.simphony.engine.controller;

import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.SchedulableDescriptor;
import repast.simphony.parameter.Parameters;

/**
 * An action that takes a {@link repast.simphony.engine.schedule.SchedulableDescriptor} and will
 * schedule it to the model's schedule.
 * 
 * @author Jerry Vos
 */
public class DefaultSchedulableAction extends DefaultControllerAction implements
		DescriptorControllerAction<SchedulableDescriptor> {
	private SchedulableDescriptor descriptor;

	public DefaultSchedulableAction(SchedulableDescriptor descriptor) {
		super();

		this.descriptor = descriptor;
	}

	public SchedulableDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * Schedules on the model schedule the descriptor's IAction with the descriptor's
	 * ScheduleParameters.
	 * 
	 * @param RunState
	 *            the RunState to grab the model schedule from
	 * @param contextId
	 *            ignored
	 * @param runParams
	 *            ignored
	 */
	@Override
	public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
		ISchedule schedule = runState.getScheduleRegistry().getModelSchedule();

		schedule.schedule(descriptor.getScheduleParameters(), descriptor.getAction());
	}

	/**
	 * Accepts the specified visitor.
	 * 
	 * @param visitor
	 *            the visitor to accept
	 */
	public void accept(ControllerActionVisitor visitor) {
		visitor.visitSchedulableAction(this);
	}
}
