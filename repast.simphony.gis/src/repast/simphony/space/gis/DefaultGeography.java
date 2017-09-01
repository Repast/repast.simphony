package repast.simphony.space.gis;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.commons.collections15.Predicate;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;

import repast.simphony.query.space.projection.Within;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionEvent.Type;
import repast.simphony.space.projection.ProjectionPredicate;
import repast.simphony.util.collections.FilteredIterator;
import simphony.util.messages.MessageCenter;

/**
 * Default implementation of Geography, a geographic GIS type space.
 */

public class DefaultGeography<T> extends DefaultProjection<T> implements Geography<T> {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(DefaultGeography.class);
  private static GeometryFactory fac = new GeometryFactory();

  // stores an objects current geometry
  // and the envelope of the geometry
  // last passed to move.
  static class GeomData {

    Envelope envelope;
    Geometry geom;

  }

  private Map<T, GeomData> geomMap = new HashMap<T, GeomData>();

  private SpatialIndex index = new Quadtree();

  private CoordinateReferenceSystem crs;

  private Map<String, Layer<T>> layerMap = new HashMap<String, Layer<T>>();

  protected Map<String, GridCoverage2D> coverageMap = new HashMap<String, GridCoverage2D>();
  
  protected GISAdder<T> adder;

  private Set<T> addedObjects = new HashSet<T>();

  private GeodeticCalculator calc;

  MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);

  /**
   * Creates a DefaultGeography with the specified name. The coordinate
   * reference system defaults to WGS84. That CRS uses (longitude, latitude)
   * coordinates with longitude values increasing East and latitude values
   * increasing North. Angular units are degrees and prime meridian is
   * Greenwich..
   * 
   * @param name
   *          the name of the geography
   */
  public DefaultGeography(String name) {
    super(name);
    setCRS(DefaultGeographicCRS.WGS84);
  }

  /**
   * Creates a DefaultGeography with a specified name and coordindate reference
   * system.
   * 
   * @param name
   *          the name of the geography
   * @param crsCode
   *          the coordinate reference system code (e.g. "EPSG:4326")
   */
  public DefaultGeography(String name, String crsCode) {
    super(name);
    try {
      setCRS(CRS.decode(crsCode));
    } catch (NoSuchAuthorityCodeException e) {
      msg.error("Error creating default geography", e);
    } catch (FactoryException e) {
      msg.error("Error creating default geography", e);
    }
  }

  /**
   * Gets the names of the layers in this geography. There will be one layer for
   * each type (by Java class) in a geography.
   * 
   * @return the names of the layers in this geography.
   */
  public Collection<String> getLayerNames() {
    return Collections.unmodifiableCollection(layerMap.keySet());
  }

  /**
   * Adds an object to this projection without giving it a location.
   * 
   * @param object
   *          the object to add
   */
  protected void add(T object) {
    addedObjects.add(object);
    if (!layerMap.containsKey(layerNameFor(object)))
      createLayer(object);
  }

  private String layerNameFor(T object) {
    return object.getClass().getName() + ".FeatureType";
  }

  private Layer<T> createLayer(T object) {
    String name = layerNameFor(object);
    Layer<T> layer = new Layer<T>(name);
    layer.setAgentType((Class<? extends T>) object.getClass());
    layerMap.put(name, layer);
    return layer;
  }

  /**
   * Moves the specified object to the specified location. If the location is
   * null then the object remains "in" this projection but without a location.
   * The type of geometry must match the type currently associated with the
   * layer. For example, an object cannot be located at a Point if the layer
   * geometery type is a Polygon. A layer gets its geometry type from the first
   * object that is moved in it.
   * 
   * @param object
   *          the object to move
   * @param geom
   *          the location to move the object to
   */
  public void move(T object, Geometry geom) {
    Layer<T> layer = layerMap.get(layerNameFor(object));
    if (layer == null)
      layer = createLayer(object);

    if (geom == null) {
      layer.getAgentSet().remove(object);
      GeomData data = geomMap.remove(object);
      
      if (data != null){
        index.remove(data.envelope, object);
      }
      return;
    }

    if (layer.getGeomType() == null) {
      layer.setGeomType(geom.getClass());
    } else if (!layer.getGeomType().isAssignableFrom(geom.getClass())) {
      msg.error("Error moving object in geography", new IllegalArgumentException(
          "Geometry type must match for layer"));
    }

    GeomData geomData = geomMap.get(object);
    if (geomData != null) {
      index.remove(geomData.envelope, object);
    } else {
      geomData = new GeomData();
      geomMap.put(object, geomData);
    }

    geom.geometryChanged();
    geomData.envelope = new Envelope(geom.getEnvelopeInternal());
    geomData.geom = geom;

    index.insert(geomData.envelope, object);

    addedObjects.remove(object);
    if (!layer.getAgentSet().contains(object))
      layer.getAgentSet().add(object);

    this.fireProjectionEvent(new ProjectionEvent(this, object, Type.OBJECT_MOVED));
  }

  /**
   * Gets the layer for the specified type.
   * 
   * @param clazz
   *          the type associated with the desired layer
   * @return the layer for the specified type.
   */
  public Layer getLayer(Class clazz) {
    String layerName = clazz.getName() + ".FeatureType";
    return layerMap.get(layerName);
  }

  /**
   * Gets the named layer.
   * 
   * @param name
   *          the layer name
   * @return the named Layer.
   */
  public Layer getLayer(String name) {
    return layerMap.get(name);
  }

  /**
   * Gets the geometric location of the specified object.
   * 
   * @param object
   *          the object
   * @return the geometric location of the specified object.
   */
  public Geometry getGeometry(Object object) {
    GeomData gd = geomMap.get(object);
    return gd != null ? gd.geom : null;
  }

  /**
   * Gets an iterable over all the objects within the specified envelope.
   * 
   * @param envelope
   *          the bounding envelope
   * @return an iterable over all the objects within the specified location.
   */
  public Iterable<T> getObjectsWithin(Envelope envelope) {
    WithinItemVisitor visitor = new WithinItemVisitor(envelope);
    index.query(envelope, visitor);
    return visitor;
  }

  /**
   * Queries this geography for objects that MAY intersect the the specified
   * envelope. This provides a first level filter for range rectangle queries. A
   * second level filter SHOULD be applied to test for exact intersection.
   * 
   * @param envelope
   *          the envelope to query for
   * @return an iterable over items whose extents MAY intersect the given search
   *         envelope.
   */
  public Iterable<T> queryInexact(Envelope envelope) {
    return index.query(envelope);
  }

  /**
   * Gets an iterable over all the objects within the specified envelope that
   * are of the specified type and only the specified type. Subclasses are
   * excluded.
   * 
   * @param envelope
   *          the bounding envelope
   * @param type
   *          the type of objects to return
   * @return an iterable over all the objects within the specified location.
   */
  public <X> Iterable<X> getObjectsWithin(Envelope envelope, Class<X> type) {
    WithinItemVisitor visitor = new WithinItemVisitor(envelope);
    index.query(envelope, visitor);
    return new FilteredIterator(visitor.iterator(), new TypePredicate(type));
  }

  /**
   * Queries this geography for objects that MAY intersect the the specified
   * envelope and are of the specified type and only the specified type.
   * Subclasses are excluded.This provides a first level filter for range
   * rectangle queries. A second level filter SHOULD be applied to test for
   * exact intersection.
   * 
   * @param envelope
   *          the envelope to query for
   * @param type
   *          the type of objects to return
   * @return an iterable over items whose extents MAY intersect the given search
   *         envelope.
   */
  public <X> Iterable<X> queryInexact(Envelope envelope, Class<X> type) {
    return new FilteredIterator(index.query(envelope).iterator(), new TypePredicate(type));
  }

  /**
   * Gets all the objects that are in this geography.
   * 
   * @return an iterable over all the objects in this geography.
   */
  public Iterable<T> getAllObjects() {
    return geomMap.keySet();
  }

  /**
   * Gets the number of objects in the geography.
   * 
   * @return the number of objects in the geography
   */
  public int size() {
    return geomMap.size();
  }

  /**
   * Sets the coordinate reference system for this Geometry. For example,
   * "EPSG:4326". All the locations of the objects in this Geometry will be
   * appropriately transformed.
   * 
   * @param crsCode
   *          the coordinate reference system code
   */
  public void setCRS(String crsCode) {
    try {
      setCRS(CRS.decode(crsCode));
    } catch (NoSuchAuthorityCodeException e) {
      msg.error("Error setting CRS", e);
    } catch (FactoryException e) {
      msg.error("Error setting CRS", e);
    }
  }

  /**
   * Sets the coordinate reference system for this Geometry. All the locations
   * of the objects in this Geometry will be appropriately transformed.
   * 
   * @param crs
   *          the coordinate reference system
   */
  public void setCRS(CoordinateReferenceSystem crs) {

  	// TODO GIS Transform coverage layers
  	
    calc = new GeodeticCalculator(crs);

    if (this.crs == null) {
      this.crs = crs;
      return;
    }
    try {
      MathTransform transform = CRS.findMathTransform(this.crs, crs);
      for (Entry<T, GeomData> entry : geomMap.entrySet()) {
        GeomData gd = entry.getValue();
        T key = entry.getKey();
        index.remove(gd.envelope, key);
        gd.geom = JTS.transform(gd.geom, transform);
        Envelope envelope = new Envelope(gd.geom.getEnvelopeInternal());
        index.insert(envelope, key);
        gd.envelope = envelope;
      }
      this.crs = crs;
    } catch (FactoryException e) {
      msg.error("Error setting CRS", e);
    } catch (MismatchedDimensionException e) {
      msg.error("Error setting CRS", e);
    } catch (TransformException e) {
      msg.error("Error setting CRS", e);
    }
  }

  /**
   * Moves the specified object the specified distance along the specified
   * angle.
   * 
   * @param object
   *          the object to move
   * @param distance
   *          the distance to move in meters
   * @param angleInRadians
   *          the angle along which to move. This USES standard mathematical
   *          axes where 90 degrees points "north".
   * @return the geometric location the object was moved to
   */
  public Geometry moveByVector(T object, double distance, double angleInRadians) {
  	Geometry geom = getGeometry(object);
    if (geom == null) {
    	 msg.error("Error moving object by vector", 
    			 new IllegalArgumentException(object.toString() 
    					 + " does not have an assigned geometry.  Initialize the geometry "
    					 + "using Geography.move(object, geom)"));
    	 
    	 return null;
    }

    if (angleInRadians > 2 * Math.PI || angleInRadians < 0) {
      throw new IllegalArgumentException("Direction cannot be > PI (360) or less than 0");
    }
    double angleInDegrees = Math.toDegrees(angleInRadians);
    angleInDegrees = angleInDegrees % 360;
    angleInDegrees = 360 - angleInDegrees;
    angleInDegrees = angleInDegrees + 90;
    angleInDegrees = angleInDegrees % 360;
    if (angleInDegrees > 180) {
      angleInDegrees = angleInDegrees - 360;
    }
    Coordinate coord = geom.getCoordinate();
    AffineTransform transform;

    try {
      if (!crs.equals(DefaultGeographicCRS.WGS84)) {
        MathTransform crsTrans = CRS.findMathTransform(this.crs, DefaultGeographicCRS.WGS84);
        Coordinate tmp = new Coordinate();
        JTS.transform(coord, tmp, crsTrans);
        calc.setStartingGeographicPoint(tmp.x, tmp.y);
      } else {
        calc.setStartingGeographicPoint(coord.x, coord.y);
      }
      calc.setDirection(angleInDegrees, distance);
      Point2D p = calc.getDestinationGeographicPoint();
      if (!crs.equals(DefaultGeographicCRS.WGS84)) {
        MathTransform crsTrans = CRS.findMathTransform(DefaultGeographicCRS.WGS84, this.crs);
        JTS.transform(new Coordinate(p.getX(), p.getY()), coord, crsTrans);
      }

      transform = AffineTransform.getTranslateInstance(p.getX() - coord.x, p.getY() - coord.y);

      MathTransform mt = mtFactory.createAffineTransform(new GeneralMatrix(transform));
      geom = JTS.transform(geom, mt);
    } catch (Exception ex) {
      msg.error("Error moving object by vector", ex);
    }

    move(object, geom);
    return geom;
  }

  /**
   * Moves the specified object the specified distance along the specified
   * angle.
   * 
   * @param object
   *          the object to move
   * @param distance
   *          the distance to move
   * @param unit
   *          the distance units. This must be convertable to meters
   * @param angleInRadians
   *          the angle along which to move
   * @return the geometric location the object was moved to
   */
  public Geometry moveByVector(T object, double distance, Unit unit, double angleInRadians) {
    if (!unit.isCompatible(SI.METER)) {
      msg.error("Error moving object by vector", new IllegalArgumentException("Unable to convert: "
          + unit + " into distance measure"));
    }
    return moveByVector(object, unit.getConverterTo(SI.METER).convert(distance), angleInRadians);
  }

  /**
   * Displaces the specified object by the specified lon and lat amount.
   * 
   * @param object
   *          the object to move
   * @param lonShift
   *          the amount to move longitudinaly
   * @param latShift
   *          the amount to move latitudinaly
   * @return the new geometry of the object
   */
  public Geometry moveByDisplacement(T object, double lonShift, double latShift) {
    Geometry geom = getGeometry(object);
    
    if (geom == null) {
    	msg.error("Error moving object by displacement", 
   			 new IllegalArgumentException(object.toString() 
  					 + " does not have an assigned geometry.  Initialize the geometry "
  					 + "using Geography.move(object, geom)"));
   	 
   	 return null;
    }
    
    AffineTransform transform = AffineTransform.getTranslateInstance(lonShift, latShift);
    try {
      MathTransform mt = mtFactory.createAffineTransform(new GeneralMatrix(transform));
      geom = JTS.transform(geom, mt);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to transform geometry", ex);
    }
    move(object, geom);
    return geom;
  }

  /**
   * Gets the current coordinate reference system for this geometry.
   * 
   * @return the current coordinate reference system for this geometry.
   */
  public CoordinateReferenceSystem getCRS() {
    return crs;
  }

  protected void remove(T object) {
    GeomData gd = geomMap.get(object);
    if (gd != null) {
      index.remove(gd.envelope, object);
      geomMap.remove(object);
      Layer<T> layer = layerMap.get(layerNameFor(object));
      layer.getAgentSet().remove(object);
    } else {
      addedObjects.remove(object);
    }
    fireProjectionEvent(new ProjectionEvent(this, object, ProjectionEvent.Type.OBJECT_REMOVED));
  }

  /**
   * Gets the current GISAdder that determines how objects are added to the
   * geometry when added to the containing context.
   * 
   * @return the current GISAdder
   */
  public GISAdder<T> getAdder() {
    return adder;
  }

  /**
   * Sets the current GISAdder that determines how objects are added to the
   * geometry when added to the containing context.
   * 
   * @param adder
   *          the new GISAdder
   */
  public void setAdder(GISAdder<T> adder) {
    this.adder = adder;
  }

  /**
   * Gets the coordinate reference system's axis units.
   * 
   * @param axis
   *          the axis index.
   * @return the coordinate reference system's axis units.
   */
  public Unit getUnits(int axis) {
    return crs.getCoordinateSystem().getAxis(axis).getUnit();
  }

  /**
   * Evaluate this Projection against the specified Predicate.  DefaultGeography
   *   provides doesn't call predicate.evaluate() but rather checks the predicate
   *   type and evaluates appropriately here.  This is because the Geography 
   *   interface is not available to repast.simphony.space.projection.ProjectionPredicate. 
   * 
   * @param predicate
   * @return true if the predicate evaluates to true, otherwise false. False can
   *         also mean that the predicate is not applicable to this Projection.
   *         For example, a linked type predicate evaluated against a grid
   *         projection.
   */
  @Override
  public boolean evaluate(ProjectionPredicate predicate) {
 	
  	if (predicate instanceof Within){
  		return evaluateWithin((Within)predicate);
  	}
  	
  	return false;
  }

  private static class TypePredicate implements Predicate<Object> {

    private Class<?> type;

    public TypePredicate(Class<?> type) {
      this.type = type;
    }

    public boolean evaluate(Object x) {
      return x.getClass().equals(type);
    }
  }

  // visitor that does a within check
  private class WithinItemVisitor<T> implements ItemVisitor, Iterable {

    List<T> items = new ArrayList<T>();
    Geometry env;

    public WithinItemVisitor(Envelope env) {
      this.env = fac.toGeometry(env);
    }

    public void visitItem(Object o) {
      Geometry geo = geomMap.get(o).geom;

      if (geo.within(env)) {
        items.add((T) o);
      }
    }

    public Iterator<T> iterator() {
      return items.iterator();
    }
  }
  
  /**
   * Evaluates the Geography against this predicate comparing
   * whether two objects are within a specified distance of each other.
   * The distance is orthodromic and in meters. Note that
   * for Polygons the distance is measured from the center
   * and not from the nearest point.
   *
   * @param geography the geography to evaluate against.
   * @return true if this predicate is true for the specified projection otherwise false.
   */
  public boolean evaluateWithin(Within within) {
  	Geometry geom1 = getGeometry(within.getObj1());
  	Geometry geom2 = getGeometry(within.getObj2());
  	try {
  		return JTS.orthodromicDistance(geom1.getCentroid().getCoordinate(),
  				geom2.getCentroid().getCoordinate(), getCRS()) <= within.getDistance();
  	} catch (TransformException e) {
  		msg.error("Error calculating orthodromic distance", e);
  	}
  	return false;
  }

	@Override
	public GridCoverage2D getCoverage(String name) {
		return coverageMap.get(name);
	}

	@Override
	public void addCoverage(String name, GridCoverage2D coverage) {
		
		// TODO GIS need to covert convert coverage CRS if different?
		
		coverageMap.put(name, coverage);
	}

	@Override
	public void removeCoverage(String name) {
		coverageMap.remove(name);
	}
}