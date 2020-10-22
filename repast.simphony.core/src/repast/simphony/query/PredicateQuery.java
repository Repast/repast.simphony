package repast.simphony.query;

import org.apache.commons.collections15.Predicate;

import repast.simphony.context.Context;

/**
 * Iterface marks a Query as dependent on an org.apache.commons.collections.Predicate.
 * This Predicate can then be returned. Certain queries can use this information to
 * do some query optimization.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 * 
 * @deprecated Use {@link Context#getObjectsAsStream(Class)} and the Java 8+ streaming API {@link java.util.stream.Stream} instead. 
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
