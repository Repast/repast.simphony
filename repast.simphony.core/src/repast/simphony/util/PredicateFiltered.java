/*CopyrightHere*/
package repast.simphony.util;

import org.apache.commons.collections15.Predicate;

/**
 * Interface for an object that performs filtering based on a rule.
 * 
 * @author Jerry Vos
 */
public interface PredicateFiltered<T> {
	/**
	 * Sets the predicate used for filtering.
	 * 
	 * @param filter
	 *            the predicate used for filtering
	 */
	void setFilter(Predicate<T> filter);

	/**
	 * Retrieves the predicate used for filtering.
	 * 
	 * @return the predicate used for filtering
	 */
	Predicate<T> getFilter();
}
