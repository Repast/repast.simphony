package repast.simphony.query;

import org.apache.commons.collections15.functors.NotPredicate;
import repast.simphony.context.Context;
import repast.simphony.util.collections.FilteredIterator;

/**
 * A query whose results are the negation of its sub-query. In practice, this means
 * the results of a NotQuery are those items in a context that are not members of the
 * the sub-query results.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NotQuery<T> implements Query<T> {

	private Query<T> query;
	private Context<T> context;

	/**
	 * Creates a NotQuery whose results will contain all the members of the
	 * specified context that are not in the specified query's results.
	 *
	 * @param context
	 * @param query
	 */
	public NotQuery(Context<T> context, Query<T> query) {
		this.query = query;
		this.context = context;
	}

	/**
	 * Gets an iterable over all the objects in the context that are not in the results
	 * of the sub-query specified in the constructor.
	 *
	 * @return  an iterable over all the objects in the context that are not in the results
	 * of the sub-query specified in the constructor.
	 */
	public Iterable<T> query() {
		return new FilteredIterator<T>(context.iterator(), new NotPredicate<T>(QueryUtils.createContains(query.query())));

	}

	/**
	 * Gets an iterable over all the objects in the context that are not in the results
	 * of the sub-query specified in the constructor AND are in the iterable parameter.
	 *
	 * @param set
	 * @return an iterable over all the objects in the context that are not in the results
	 * of the sub-query specified in the constructor AND are in the iterable parameter.
	 */
	public Iterable<T> query(Iterable<T> set) {
		return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
	}
}
