/*CopyrightHere*/
package repast.simphony.engine.schedule;


/**
 * Default implementation of a
 * {@link repast.simphony.engine.schedule.SchedulableDescriptor}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultSchedulableDescriptor extends DefaultDescriptor implements
				SchedulableDescriptor {
	private ScheduleParameters scheduleParameters;

	private IAction action;

	public DefaultSchedulableDescriptor(String name) {
		super(name);
	}

	public DefaultSchedulableDescriptor() {
		super();
	}

	/**
	 * Retrieves the parameters for when the action should occur.
	 * 
	 * @return parameters for the action's execution
	 */
	public ScheduleParameters getScheduleParameters() {
		return scheduleParameters;
	}

	/**
	 * Sets the parameters for when the action should occur.
	 * 
	 * @param scheduleParameters
	 *            parameters for the action's execution
	 */
	public void setScheduleParameters(ScheduleParameters scheduleParameters) {
		this.scheduleParameters = scheduleParameters;
	}

	/**
	 * Retrieves the action to be scheduled.
	 * 
	 * @return the action to be scheduled
	 */
	public IAction getAction() {
		return action;
	}

	/**
	 * Sets the action to be scheduled.
	 * 
	 * @param action
	 *            the action to be scheduled
	 */
	public void setAction(IAction action) {
		this.action = action;
	}
}
