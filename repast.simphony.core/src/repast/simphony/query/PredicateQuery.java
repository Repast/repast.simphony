package repast.simphony.query;

import org.apache.commons.collections15.Predicate;

/**
 * Iterface marks a Query as dependent on an org.apache.commons.collections.Predicate.
 * This Predicate can then be returned. Certain queries can use this information to
 * do some query optimization.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface PredicateQuery<T> extends Query<T> {

	/**
	 * Gets the Predicate that will be used in this Predicate's
	 * next call to query().
	 *
	 * @return the Predicate that will be used in this Predicate's
	 * next call to query().
	 */
	Predicate<T> getQueryPredicate();
}
