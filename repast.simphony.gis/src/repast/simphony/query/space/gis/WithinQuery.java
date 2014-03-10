package repast.simphony.query.space.gis;

import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.collections15.Predicate;
import repast.simphony.space.gis.Geography;

/**
 * Query that returns objects a specific a geometry is within.
 *
 */
public class WithinQuery<T> extends AbstractGeometryQuery<T> {

  /**
   * Creates a WithinQuery that will query
   * the specified geography for objects that  contain the
   * geometry of the specified object, that is, objects that the
   * geometry is within.
   *
   * @param geography the geography to query
   * @param sourceObject the object whose geometry is the container
   */
  public WithinQuery(Geography<T> geography, T sourceObject) {
    super(geography, sourceObject);
  }

  /**
   * Creates a WithinQuery that will query
   * the specified geography for objects that contain the
   * specified geometry, that is, objects that the
   * geometry is within.
   *
   * @param geography the geography to query
   * @param geom the containing geometry
   */
  public WithinQuery(Geography<T> geography, Geometry geom) {
    super(geography, geom);
  }

  /**
   * Creates a predicate that tests for containment.
   *
   * @return a predicate that tests for containment.
   */
  protected Predicate<T> createPredicate() {
    if (sourceObject == null) return new NoSourceWithinPredicate<T>();
    else return new WithinPredicate<T>();
  }

  private class NoSourceWithinPredicate<T> implements Predicate<T> {

    public boolean evaluate(T o) {
      Geometry other = geography.getGeometry(o);
      return other != null && geom.within(other);
    }
  }

  private class WithinPredicate<T> implements Predicate<T> {

    public boolean evaluate(T o) {
      if (!sourceObject.equals(o)) {
      Geometry other = geography.getGeometry(o);
      return other != null && geom.within(other);
      }
      return false;
    }
  }
}