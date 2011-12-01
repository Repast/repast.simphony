/**
 * 
 */
package repast.simphony.engine.schedule;


/**
 * @author milesparker
 *
 */
public interface ScheduledStatistic {

	/**
	 * computes some statistic on the passed in values
	 * 
	 * @param values
	 *            the values to use in the statistic
	 * 
	 * @return a double derived from the numbers
	 */
	@ScheduledMethod
	public double computeStatistic(Iterable<? extends Number> values);

}