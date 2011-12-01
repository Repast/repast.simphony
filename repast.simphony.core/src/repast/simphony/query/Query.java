package repast.simphony.query;

/**
 * Interface for classes that query a collection such as a projection or context
 * and returns an iterable over the objects that are the result of that query.
 */
public interface Query<T> {

	/**
	 * Returns the result of the query.
	 *
	 * @return an iterable over the objects that are the result of the query.
	 */
	Iterable<T> query();

	/**
	 * Returns an iterable over the objects that are the result of the query
	 * and are in the passed in iterable. This allows queries to be chained
	 * together where the result of one query is passed into another.
	 *
	 * @param set
	 * @return an iterable over the objects that are the result of the query
	 * and are in the passed in iterable.
	 */
	Iterable<T> query(Iterable<T> set);

}
