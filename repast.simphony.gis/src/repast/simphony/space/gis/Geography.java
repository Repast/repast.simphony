/**
 *
 */
package repast.simphony.space.gis;

import java.util.Collection;

import javax.measure.unit.Unit;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import repast.simphony.space.projection.Projection;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Space that locates objects in a geographic gis-type space.
 *
 * @param <T> Object type contained by the geography.
 */
public interface Geography<T> extends Projection<T>{

  /**
   * Gets the names of the layers in this geography. There will be one layer
   * for each type (by Java class) in a geography.
   *
   * @return the names of the layers in this geography.
   */
  Collection<String> getLayerNames();

  /**
   * Moves the specified object to the specified location. If the location
   * is null then the object remains "in" this projection but without
   * a location. The type of geometry must match the type currently associated
   * with the layer. For example, an object cannot be located at a Point if
   * the layer geometery type is a Polygon. A layer gets its geometry type
   * from the first object that is moved in it.
   *
   * @param object the object to move
   * @param geom   the location to move the object to
   */
  void move(T object, Geometry geom);

  /**
   * Gets the layer for the specified type.
   *
   * @param clazz the type associated with the desired layer
   * @return the layer for the specified type.
   */
  Layer getLayer(Class clazz);

  /**
   * Gets the named layer.
   *
   * @param name the layer name
   * @return the named Layer.
   */
  Layer getLayer(String name);

  /**
   * Gets the geometric location of the specified object.
   *
   * @param object the object
   * @return the geometric location of the specified object.
   */
  Geometry getGeometry(Object object);

  /**
   * Gets an iterable over all the objects within the specified envelope.
   *
   * @param envelope the bounding envelope
   * @return an iterable over all the objects within the specified location.
   */
  Iterable<T> getObjectsWithin(Envelope envelope);


  /**
   * Queries this geography for objects that MAY intersect the
   * the specified envelope. This provides a first level
   * filter for range rectangle queries. A second level
   * filter SHOULD be applied to test for exact intersection.
   *
   * @param envelope the envelope to query for
   * @return an iterable over items whose extents MAY intersect the
   *         given search envelope.
   */
  Iterable<T> queryInexact(Envelope envelope);

  /**
   * Sets the coordinate reference system for this Geometry. For example,
   * "EPSG:4326".  All the locations of the objects in this Geometry will be
   * appropriately transformed.
   *
   * @param crsCode the coordinate reference system code
   */
  void setCRS(String crsCode);

  /**
   * Sets the coordinate reference system for this Geometry. All
   * the locations of the objects in this Geometry will be
   * appropriately transformed.
   *
   * @param crs the coordinate reference system
   */
  void setCRS(CoordinateReferenceSystem crs);

  /**
   * Moves the specified object the specified distance along the specified angle.
   *
   * @param object         the object to move
   * @param distance       the distance to move in meters
   * @param angleInRadians the angle along which to move
   * @return the geometric location the object was moved to
   */
  Geometry moveByVector(T object, double distance,
                        double angleInRadians);

  /**
   * Moves the specified object the specified distance along the specified angle.
   *
   * @param object         the object to move
   * @param distance       the distance to move
   * @param unit           the distance units. This must be convertable to meters
   * @param angleInRadians the angle along which to move
   * @return the geometric location the object was moved to
   */
  Geometry moveByVector(T object, double distance, Unit unit,
                        double angleInRadians);

  /**
   * Displaces the specified object by the specified lon and lat amount.
   *
   * @param object   the object to move
   * @param lonShift the amount to move longitudinaly
   * @param latShift the amount to move latitudinaly
   * @return the new geometry of the object
   */
  Geometry moveByDisplacement(T object, double lonShift,
                              double latShift);

  /**
   * Gets the current coordinate reference system for this geometry.
   *
   * @return the current coordinate reference system for this geometry.
   */
  CoordinateReferenceSystem getCRS();

  /**
   * Gets the current GISAdder that determines how objects are added to the
   * geometry when added to the containing context.
   *
   * @return the current GISAdder
   */
  GISAdder<T> getAdder();

  /**
   * Sets the current GISAdder that determines how objects are added to the
   * geometry when added to the containing context.
   *
   * @param adder the new GISAdder
   */
  void setAdder(GISAdder<T> adder);


  /**
   * Gets the coordinate reference system's axis units.
   *
   * @param axis the axis index.
   * @return the coordinate reference system's axis units.
   */
  Unit getUnits(int axis);

  /**
   * Gets an iterable over all the objects within the specified envelope
   * that are of the specified type and only the specified type. Subclasses
   * are excluded.
   *
   * @param envelope the bounding envelope
   * @param type     the type of objects to return
   * @return an iterable over all the objects within the specified location.
   */
  <X> Iterable<X> getObjectsWithin(Envelope envelope, Class<X> type);

  /**
   * Queries this geography for objects that MAY intersect the
   * the specified envelope and are of the specified type and only the specified type.
   * Subclasses are excluded.This provides a first level
   * filter for range rectangle queries. A second level
   * filter SHOULD be applied to test for exact intersection.
   *
   * @param envelope the envelope to query for
   * @param type     the type of objects to return
   * @return an iterable over items whose extents MAY intersect the
   *         given search envelope.
   */
  <X> Iterable<X> queryInexact(Envelope envelope, Class<X> type);

  /**
   * Gets all the objects that are in this geography.
   *
   * @return an iterable over all the objects in this geography.
   */
  Iterable<T> getAllObjects();

  /**
   * Gets the number of objects in the geography.
   *
   * @return the number of objects in the geography
   */
  int size();
}