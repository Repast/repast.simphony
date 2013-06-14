package repast.simphony.query.space.gis;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import repast.simphony.query.Query;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeometryUtils;
import repast.simphony.space.gis.UTMFinder;
import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Query that returns items in a geography that are some specified distance from
 * another item or a geometry.  The distance is either calculated from the
 * center of a point, or from the buffer zone which is created around the line
 * or polygon feature, see {@link com.vividsolutions.jts.geom.Geometry} .buffer() 
 *
 * @author Nick Collier
 * @author Eric Tatara
 */
public class GeographyWithin<T> implements Query<T> {

  private static DefaultCoordinateOperationFactory cFactory = new DefaultCoordinateOperationFactory();
  private static final MessageCenter center = MessageCenter.getMessageCenter(GeographyWithin.class);

  private IntersectsQuery query;
  private Object sourceObject;


  /**
   * Creates GeographyWithinQuery that returns items in a specified geography
   * that are within the specified distance from the centroid of the source object.
   *
   * @param geography the containing geography
   * @param distance the distance in METERS
   * @param sourceObject the object whose neighbors we want
   */
  public GeographyWithin(Geography geography, double distance, Object sourceObject) {
    this.sourceObject = sourceObject;
    init(geography, distance, geography.getGeometry(sourceObject));
  }

  /**
   * Creates GeographyWithinQuery that returns items in a specified geography
   * that are within the specified distance from the centroid of the source object.
   *
   * @param geography the containing geography
   * @param distance the distance in the specified units
   * @param units the distance units. These must be convertable to meters.
   * @param sourceObject the object whose neighbors we want
   */
  public GeographyWithin(Geography geography, double distance, Unit units, Object sourceObject) {
    this.sourceObject = sourceObject;
    init(geography, units.getConverterTo(SI.METER).convert(distance), geography.getGeometry(sourceObject));

  }

  /**
   * Creates GeographyWithinQuery that returns items in a specified geography
   * that are within the specified distance from the centroid of the source object.
   *
   * @param geography the containing geography
   * @param distance the distance in the specified units
   * @param units the distance units. These must be convertable to meters.
   * @param geom the source geom whose surrounding neighbors we want
   */
  public GeographyWithin(Geography geography, double distance, Unit units, Geometry geom) {
    init(geography, units.getConverterTo(SI.METER).convert(distance), geom);
  }


  /**
   * Creates GeographyWithinQuery that returns items in a specified geography
   * that are within the specified distance from the centroid of the specified geom.
   *
   * @param geography the containing geography
   * @param distance the distance in METERS
   * @param geom the source geom whose surrounding neighbors we want
   */
  public GeographyWithin(Geography geography, double distance, Geometry geom) {
    init(geography, distance, geom);
  }

  private void init(Geography geography, double distance, Geometry geom) {
   
  	Geometry buffer = GeometryUtils.generateBuffer(geography, geom, distance);
  	 
    query = new IntersectsQuery(geography, buffer);
    query.sourceObject = sourceObject;
    query.predicate = query.createPredicate();
  }

  /**
   * Returns the result of the query.
   *
   * @return an iterable over the objects that are the result of the query.
   */
  public Iterable<T> query() {
    return query.query();
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
  public Iterable<T> query(Iterable<T> set) {
    return query.query(set);
  }
}
