package repast.simphony.query;

import repast.simphony.util.collections.FilteredIterator;

import java.util.HashSet;
import java.util.Set;

/**
 * A query whose result is the union of its sub queries.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class OrQuery<T> implements Query<T>{

	private Query<T> query1, query2;

	/**
	 * Creates an OrQuery from the two sub-queries.
	 *
	 * @param query1
	 * @param query2
	 */
	public OrQuery(Query<T> query1, Query<T> query2) {
		this.query1 = query1;
		this.query2 = query2;
	}



	/**
	 * Gets the union of the two queries specified in the constructor.
	 *
	 * @return an iterable over the objects in the union of the two queries
	 * specified in the constructor.
	 */
	public Iterable<T> query() {
		Set<T> set = new HashSet<T>();
		makeSet(set, query1.query());
		makeSet(set, query2.query());
		return set;
	}

	private void makeSet(Set<T> set, Iterable<T> iter) {
		if (iter instanceof Set) {
			set.addAll((Set<T>)iter);
		} else {
			for (T obj : iter) {
				set.add(obj);
			}
		}
	}

	/**
	 * Returns an iterable over the union of the two queries specified in the constructor
	 * where the results of that union are in the iterable parameter.
	 *
	 * @param set
	 * @return an iterable over the union of the two queries specified in the constructor
	 * where the results of that union are in the iterable parameter.
	 */
	public Iterable<T> query(Iterable<T> set) {
		return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
	}
}
