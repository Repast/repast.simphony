package repast.simphony.query.space.gis;

import org.locationtech.jts.geom.Geometry;
import org.apache.commons.collections15.Predicate;
import repast.simphony.space.gis.Geography;

/**
 * Query that returns objects touched by a geometry.
 *
 */
public class TouchesQuery<T> extends AbstractGeometryQuery<T> {

  /**
   * Creates a TouchesQuery that will query
   * the specified geography for objects that are touched by the
   * geometry of the specified object.
   *
   * @param geography the geography to query
   * @param sourceObject the object whose geometry is the container
   */
  public TouchesQuery(Geography<T> geography, T sourceObject) {
    super(geography, sourceObject);
  }

  /**
   * Creates a Touches that will query
   * the specified geography for objects that are touched by the
   * specified geometry.
   *
   * @param geography the geography to query
   * @param geom the containing geometry
   */
  public TouchesQuery(Geography<T> geography, Geometry geom) {
    super(geography, geom);
  }

  /**
   * Creates a predicate that tests for containment.
   *
   * @return a predicate that tests for containment.
   */
  protected Predicate<T> createPredicate() {
    return new TouchesPredicate<T>(geom, geography);
  }

  static class TouchesPredicate<T> implements Predicate<T> {

    private Geometry geom;
    private Geography geo;

    TouchesPredicate(Geometry geom, Geography geo) {
      this.geom = geom;
      this.geo = geo;
    }

    public boolean evaluate(T o) {
      Geometry other = geo.getGeometry(o);
      return other != null && !other.equals(geom) && geom.touches(other);
    }
  }
}