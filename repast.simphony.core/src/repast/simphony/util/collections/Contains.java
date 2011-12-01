package repast.simphony.util.collections;

import org.apache.commons.collections15.Predicate;

import java.util.Set;

/**
 * Predicate that returns true if the specified object is contained by
 * a Set.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class Contains<T> implements Predicate<T> {

	private Set<T> set;

	public Contains(Set<T> set) {
		this.set = set;
	}

	/**
	 * Evaluates to true if the specified object is in the set given in the constructor.
	 *
	 * @param obj
	 * @return true if the specified object is in the set given in the constructor, otherwise
	 * false.
	 */
	public boolean evaluate(T obj) {
		return set.contains(obj);
	}
}
