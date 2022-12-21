package repast.simphony.query.space.gis;

import org.locationtech.jts.geom.Geometry;
import org.apache.commons.collections15.Predicate;
import repast.simphony.space.gis.Geography;

/**
 * Query that determines what objects are intersected by a geometry.
 */
public class IntersectsQuery<T> extends AbstractGeometryQuery<T> {

  /**
   * Creates an IntersectsQuery that will query
   * the specified geography for objects that are intersected by the
   * geometry of the specified object.
   *
   * @param geography    the geography to query
   * @param sourceObject the object whose geometry is intersected
   */
  public IntersectsQuery(Geography<T> geography, T sourceObject) {
    super(geography, sourceObject);
  }

  /**
   * Creates a IntersectsQuery that will query
   * the specified geography for objects that are intersected by the
   * specified geometry.
   *
   * @param geography the geography to query
   * @param geom      the intersected geometry
   */
  public IntersectsQuery(Geography<T> geography, Geometry geom) {
    super(geography, geom);
  }

  /**
   * Creates a predicate that tests for intersection.
   *
   * @return a predicate that tests for intersection.
   */
  protected Predicate<T> createPredicate() {
    if (sourceObject != null) return new IntersectsPredicate<T>();
    return new NoSourceIntersectsPredicate();
  }

  private class NoSourceIntersectsPredicate<T> implements Predicate<T> {

    public boolean evaluate(T o) {
      Geometry other = geography.getGeometry(o);
      return other != null && other.intersects(geom);
    }
  }

  private class IntersectsPredicate<T> implements Predicate<T> {

    public boolean evaluate(T o) {
      if (!o.equals(sourceObject)) {
        Geometry other = geography.getGeometry(o);
        return other != null && other.intersects(geom);
      }
      return false;
    }
  }
}