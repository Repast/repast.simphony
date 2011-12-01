/*CopyrightHere*/
package repast.simphony.engine.schedule;


/**
 * A descriptor for something that will get scheduled.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface SchedulableDescriptor extends Descriptor {

	/**
	 * Retrieves the parameters for when the action should occur.
	 * 
	 * @return parameters for the action's execution
	 */
	ScheduleParameters getScheduleParameters();

	/**
	 * Sets the parameters for when the action should occur.
	 * 
	 * @param scheduleParameters
	 *            parameters for the action's execution
	 */
	void setScheduleParameters(ScheduleParameters scheduleParameters);

	/**
	 * Retrieves the action to be scheduled.
	 * 
	 * @return the action to be scheduled
	 */
	IAction getAction();

	/**
	 * Sets the action to be scheduled.
	 * 
	 * @param action
	 *            the action to be scheduled
	 */
	void setAction(IAction action);
}