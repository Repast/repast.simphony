package repast.simphony.query;

import java.util.HashSet;
import java.util.Set;

import repast.simphony.util.collections.Contains;

/**
 * Utility functions for working with queries.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class QueryUtils {

	/**
	 * Creates a Contains predicate using the items in the specified iterable.
	 * The predicate will true if passed an object that is in the iterable.
	 *
	 * @param iterable
	 * @return a Contains predicate using the items in the specified iterable.
	 */
	public static <T> Contains<T> createContains(Iterable<T> iterable) {
		Set<T> set = null;
		if (iterable instanceof Set) {
			set = (Set<T>) iterable;
		} else {
			set = new HashSet<T>();
			for (T item : iterable) {
				set.add(item);
			}
		}

		return new Contains<T>(set);
	}
}
