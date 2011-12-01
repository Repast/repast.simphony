package repast.simphony.query;

import repast.simphony.util.collections.FilteredIterator;

/**
 * A query whose result is the intersection of its sub queries.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class AndQuery<T> implements Query<T>{

	private Query<T> query1, query2;

	/**
	 * Creates an AndQuery from the two sub-queries.
	 *
	 * @param query1
	 * @param query2
	 */
	public AndQuery(Query<T> query1, Query<T> query2) {
		this.query1 = query1;
		this.query2 = query2;
	}

	/**
	 * Gets the intersection of the two queries specified in the constructor.
	 *
	 * @return an iterable over the objects in the intersection of the two queries
	 * specified in the constructor.
	 */
	public Iterable<T> query() {
		return query1.query(query2.query());
	}

	/**
	 * Returns an iterable over the intersection of the two queries specified in the constructor
	 * where the results of that intersection are in the iterable parameter.
	 *
	 * @param set
	 * @return an iterable over the intersection of the two queries specified in the constructor
	 * where the results of that intersection are in the iterable parameter.
	 */
	public Iterable<T> query(Iterable<T> set) {
		return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
	}
}
