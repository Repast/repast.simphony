package repast.simphony.query.space.gis;

import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.collections15.Predicate;
import repast.simphony.space.gis.Geography;

/**
 * Query that determines what objects are contained by a geometry.
 */
public class ContainsQuery<T> extends AbstractGeometryQuery<T> {

  /**
   * Creates a ContainsQuery that will query
   * the specified geography for objects that are contained by the
   * geometry of the specified object.
   *
   * @param geography    the geography to query
   * @param sourceObject the object whose geometry is the container
   */
  public ContainsQuery(Geography<T> geography, T sourceObject) {
    super(geography, sourceObject);
  }

  /**
   * Creates a ContainsQuery that will query
   * the specified geography for objects that are contained by the
   * specified geometry.
   *
   * @param geography the geography to query
   * @param geom      the containing geometry
   */
  public ContainsQuery(Geography<T> geography, Geometry geom) {
    super(geography, geom);
  }

  /**
   * Creates a predicate that tests for containment.
   *
   * @return a predicate that tests for containment.
   */
  protected Predicate<T> createPredicate() {
    if (sourceObject == null) return new NoSourceContainsPredicate<T>();
    else return new ContainsPredicate<T>();
  }

  private class NoSourceContainsPredicate<T> implements Predicate<T> {

    public boolean evaluate(T o) {
      Geometry other = geography.getGeometry(o);
      return other != null && geom.contains(other);
    }
  }

  private class ContainsPredicate<T> implements Predicate<T> {

    public boolean evaluate(T o) {
      if (!sourceObject.equals(o)) {
        Geometry other = geography.getGeometry(o);
        return other != null && geom.contains(other);
      }

      return false;
    }
  }
}
