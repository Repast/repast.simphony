/*CopyrightHere*/
package repast.simphony.engine.schedule;

import org.apache.commons.collections15.Predicate;


/**
 * Default implementation of a
 * {@link repast.simphony.engine.schedule.ContextSchedulableDescriptor}.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultContextSchedulableDescriptor<T> extends DefaultDescriptor
		implements ContextSchedulableDescriptor<T> {

	private ScheduleParameters scheduleParameters;

	private Predicate<T> filterPredicate;

	private boolean shuffle;

	private String methodName;

	/**
	 * Sets the filter to use on the context to pull agents out and execute
	 * them.
	 * 
	 * @param filter
	 *            the filter to apply to the context
	 */
	public void setFilter(Predicate<T> filter) {
		this.filterPredicate = filter;
	}

	/**
	 * Retrieves the filter to use on the context to pull agents out and execute
	 * them.
	 * 
	 * @return the filter to apply to the context
	 */
	public Predicate<T> getFilter() {
		return filterPredicate;
	}

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
	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

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
	public boolean getShuffle() {
		return shuffle;
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
	 * Sets the name of the method to call on the filtered objects.
	 * 
	 * @param methodName
	 *            name of the method to call on the filtered objects.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Retrieves the name of the method to call on the filtered objects.
	 * 
	 * @return name of the method to call on the filtered objects.
	 */
	public String getMethodName() {
		return methodName;
	}
}
