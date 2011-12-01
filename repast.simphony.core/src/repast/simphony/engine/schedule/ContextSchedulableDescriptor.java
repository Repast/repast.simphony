/*CopyrightHere*/
package repast.simphony.engine.schedule;

import repast.simphony.util.PredicateFiltered;

/**
 * Descriptor for a schedulable action that will occur on a filtered set of
 * objects. An example usage of this would be to filter a Context based on the
 * filter contained in this descriptor.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface ContextSchedulableDescriptor<T> extends PredicateFiltered<T>, Descriptor {
	// TODO: decide if this should extend SchedulableDescriptor

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
	 * Sets the name of the method to call on the filtered objects.
	 * 
	 * @param methodName
	 *            name of the method to call on the filtered objects.
	 */
	void setMethodName(String methodName);

	/**
	 * Retrieves the name of the method to call on the filtered objects.
	 * 
	 * @return name of the method to call on the filtered objects.
	 */
	String getMethodName();

	/**
	 * Sets whether or not to shuffle the list of objects before executing them
	 * or not.
	 * 
	 * @see repast.simphony.engine.schedule.ISchedule#scheduleIterable(ScheduleParameters,
	 *      Iterable, String, boolean, Object[])
	 * 
	 * @param shuffle
	 *            whether or not to shuffle the list of objects before executing
	 *            them or not
	 */
	void setShuffle(boolean shuffle);

	/**
	 * Retrieves whether or not to shuffle the list of objects before executing
	 * them or not.
	 * 
	 * @see repast.simphony.engine.schedule.ISchedule#scheduleIterable(ScheduleParameters,
	 *      Iterable, String, boolean, Object[])
	 * 
	 * @return whether or not to shuffle the list of objects before executing
	 *         them or not
	 */
	boolean getShuffle();
}
