package repast.simphony.query;

import repast.simphony.context.Context;
import repast.simphony.util.collections.FilteredIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for queries whose results contains all the objects within some distance of a
 * specified object.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class WithinDistance<T> implements Query<T> {

  public static final List EMPTY = new ArrayList();

  private Context<T> context;
  protected T obj;
  protected double distanceSq, distance;

  /**
   * Creates a WithinDistance query whose results will be all
   * the objects in all the projections of a particular type in the
   * context within some distance of the specified object. Subclasses
   * will determine the projection type.
   *
   * @param context
   * @param distance
   * @param obj
   */
  protected WithinDistance(Context<T> context, double distance, T obj) {
    this.context = context;
    this.distanceSq = distance * distance;
    this.distance = distance;
    this.obj = obj;
  }

  /**
   * Returns the result of the query.
   *
   * @return an iterable over the objects that are the result of the query.
   */
  public Iterable<T> query() {
    if (context != null) {
      return createIterable(context);
    } else {
      return createIterable();
    }
  }

  /**
   * Creates an iterable over all the relevant objects in the context.
   *
   * @param context
   * @return an iterable over all the relevant objects in the context.
   */
  protected abstract Iterable<T> createIterable(Context<T> context);

  /**
   * Creates an iterable over all the relevant objects in a space. The actual
   * space will depend on subclass implementations.
   *
   * @return an iterable over all the relevant objects in a space. The actual
   *         space will depend on subclass implementations.
   */
  protected abstract Iterable<T> createIterable();

  /**
   * Returns an iterable over the objects that are the result of the query
   * and are in the passed in iterable. This allows queries to be chained
   * together where the result of one query is passed into another.
   *
   * @param set
   * @return an iterable over the objects that are the result of the query
   *         and are in the passed in iterable.
   */
  public Iterable<T> query(Iterable<T> set) {
    return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
  }


}
