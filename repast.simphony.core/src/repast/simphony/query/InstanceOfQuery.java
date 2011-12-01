package repast.simphony.query;

import repast.simphony.context.Context;
import repast.simphony.util.collections.FilteredIterator;

/**
 * Query that returns objects in a given context that are
 * instances of the specified type.
 *
 * @author Nick Collier
 */
public class InstanceOfQuery<T> implements Query<T> {

  private Context<T> context;
  private Class type;

  /**
   * Creates an InstanceofQuery to query the specified context for objects of
   * the specified type.
   *
   * @param context the context to query
   * @param type the type to query for
   */
  public InstanceOfQuery(Context<T> context, Class type) {
    this.context = context;
    this.type = type;
  }

  /**
   * Returns the result of the query.
   *
   * @return an iterable over the objects that are the result of the query.
   */
  public Iterable<T> query() {
    return context.getObjects(type);
  }

  /**
   * Returns an iterable over the objects that are the result of the query
   * and are in the passed in iterable. This allows queries to be chained
   * together where the result of one query is passed into another.
   *
   * @param set
   * @return an iterable over the objects that are the result of the query
   *         and are in the passed in iterable.
   */
  public Iterable<T> query(Iterable set) {
    return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
  }
}
