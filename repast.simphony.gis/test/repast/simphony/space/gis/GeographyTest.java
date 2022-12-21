package repast.simphony.space.gis;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import junit.framework.TestCase;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.query.space.gis.*;
import repast.simphony.query.space.projection.Within;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GeographyTest extends TestCase {
  private Geography<Object> geography;
  private Context<Object> town;

  private House[] houses = new House[10];

  private Car[] cars = new Car[10];

  public void setUp() {
    town = new DefaultContext<Object>();
    for (int i = 0; i < 10; i++) {
      House house = new House("Owner" + i, 1 + "Main St", "Anytown",
              "Anystate");
      town.add(house);
      houses[i] = house;
    }
    for (int i = 0; i < 10; i++) {
      Car car = new Car("Saturn", "SW1", i, "Brown");
      town.add(car);
      cars[i] = car;
    }
    GeographyParameters<Object> params = new GeographyParameters<Object>();
    geography = GeographyFactoryFinder.createGeographyFactory(null).createGeography("AnyTown", town, params);
  }

  public void testUTMZone() {
    // lat, lon
    short val = UTMFinder.determineZone(48.3, -114.26667);
    assertEquals(11, val);

    val = UTMFinder.determineZone(45.8, -108.5333);
    assertEquals(12, val);

    val = UTMFinder.determineZone(45.8,174);
    assertEquals(60, val);

    val = UTMFinder.determineZone(45.8, 173.999);
    assertEquals(59, val);

    val = UTMFinder.determineZone(45.8, -180);
    assertEquals(1, val);

    val = UTMFinder.determineZone(45.8, 180);
    assertEquals(60, val);
  }

  /*
    * Test method for 'repast.simphony.gis.Geography.getLayerNames()'
    */
  public void testGetLayerNames() {
    Collection<String> layerNames = geography.getLayerNames();
    Set<String> expected = new HashSet<String>();
    expected.add(House.class.getName() + ".FeatureType");
    expected.add(Car.class.getName() + ".FeatureType");
    for (String layer : layerNames) {
      assertTrue(expected.remove(layer));
    }
    assertEquals(0, expected.size());
  }

  /*
    * Test method for 'repast.simphony.gis.Geography.move(T, Geometry)'
    */
  public void testMove() {
    GeometryFactory fac = new GeometryFactory();
    Coordinate coord = new Coordinate(-87.2, 42.1);
    Geometry geom = fac.createPoint(coord);
    assertNull(geography.getGeometry(houses[0]));
    geography.move(houses[0], geom);
    assertEquals(geom, geography.getGeometry(houses[0]));

    geom = geography.getGeometry(houses[0]);
    geom.getCoordinate().x += 10;
    double x = geom.getCoordinate().x;
    geography.move(this, geom);
    assertEquals(x, geography.getGeometry(houses[0]).getCoordinate().x);
  }

  public void testDisplacementMove() {
    GeometryFactory fac = new GeometryFactory();
    Coordinate coord = new Coordinate(12, 32);
    Geometry geom = fac.createPoint(coord);
    geography.move(cars[0], geom);
    assertEquals(geom, geography.getGeometry(cars[0]));

    geom = geography.moveByDisplacement(cars[0], -5.5, .32);
    assertTrue(fac.createPoint(new Coordinate(12 + -5.5, 32 + .32)).equalsExact(geom));
  }

  public void testVectorMove() {
    GeometryFactory fac = new GeometryFactory();
    Coordinate coord = new Coordinate(12, 32);
    Geometry geom = fac.createPoint(coord);
    geography.move(cars[0], geom);
    assertEquals(geom, geography.getGeometry(cars[0]));

    // move 10,000m north
    Geometry newGeom = geography.moveByVector(cars[0], 10000, Math.toRadians(90));
    GeodeticCalculator calc = new GeodeticCalculator(geography.getCRS());
    calc.setStartingGeographicPoint(coord.x, coord.y);
    // north
    calc.setDirection(0.0, 10000);
    Point2D pt = calc.getDestinationGeographicPoint();
    assertEquals(newGeom.getCoordinate().x, pt.getX());
    assertEquals(newGeom.getCoordinate().y, pt.getY());

    calc.setStartingGeographicPoint(geom.getCoordinate().x, geom.getCoordinate().y);
    calc.setDestinationGeographicPoint(newGeom.getCoordinate().x, newGeom.getCoordinate().y);
    System.out.println("calc.getOrthodromicDistance() = " + calc.getOrthodromicDistance());
  }

  /*
	 * Test method for 'repast.simphony.gis.Geography.getLayer(Class)'
	 */
  public void testGetLayer() {
    Layer layer = geography.getLayer(Car.class);
    assertEquals(Car.class, layer.getAgentType());
  }

  /*
    * Test method for 'repast.simphony.gis.Geography.getGeometry(T)'
    */
  public void testGetGeometry() {
    GeometryFactory fac = new GeometryFactory();
    Coordinate coord = new Coordinate(-87.2, 42.1);
    Geometry geom = fac.createPoint(coord);
    geography.move(houses[0], geom);
    assertEquals(geom, geography.getGeometry(houses[0]));

  }

  /*
    * Test method for 'repast.simphony.gis.Geography.getObjectsWithin(Envelope)'
    */
  public void testGetObjectsAt() {
    GeometryFactory fac = new GeometryFactory();
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 2; x++) {
        Car car = cars[i++];
        Coordinate coord = new Coordinate(x, y);
        Geometry geom = fac.createPoint(coord);
        geography.move(car, geom);
      }
    }

    // test for same geometry
    geography.move(cars[0], geography.getGeometry(cars[2]));
    geography.move(houses[0], geography.getGeometry(cars[2]));

    Envelope env = new Envelope(1.5, -1, 0.5, 2.5);
    Set<Object> resultSet = new HashSet<Object>();
    resultSet.add(cars[0]);
    resultSet.add(cars[2]);
    resultSet.add(cars[3]);
    resultSet.add(cars[4]);
    resultSet.add(cars[5]);
    resultSet.add(houses[0]);
    for (Object obj : geography.getObjectsWithin(env)) {
      assertTrue(resultSet.remove(obj));
    }
    assertEquals(0, resultSet.size());

    resultSet.add(cars[0]);
    resultSet.add(cars[2]);
    resultSet.add(cars[3]);
    resultSet.add(cars[4]);
    resultSet.add(cars[5]);
    for (Object obj : geography.getObjectsWithin(env, Car.class)) {
      assertTrue(resultSet.remove(obj));
    }
    assertEquals(0, resultSet.size());

    resultSet.add(houses[0]);
    for (Object obj : geography.getObjectsWithin(env, House.class)) {
      assertTrue(resultSet.remove(obj));
    }
    assertEquals(0, resultSet.size());
  }

  public void testContainsQuery() {
    GeometryFactory fac = new GeometryFactory();
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 2; x++) {
        Car car = cars[i++];
        Coordinate coord = new Coordinate(x, y);
        Geometry geom = fac.createPoint(coord);
        geography.move(car, geom);
      }
    }

    Shed shed = new Shed();
    town.add(shed);
    Coordinate[] coords = new Coordinate[]{new Coordinate(-1, .5), new Coordinate(1.5, .5),
            new Coordinate(1.5, 1.5), new Coordinate(-1, 1.5), new Coordinate(-1, .5)};
    Geometry geom = fac.createPolygon(fac.createLinearRing(coords), null);
    geography.move(shed, geom);

    Set<Car> expected = new HashSet<Car>();
    expected.add(cars[2]);
    expected.add(cars[3]);

    ContainsQuery query = new ContainsQuery(geography, shed);
    for (Object obj : query.query()) {
      assertTrue(expected.remove(obj));
      //System.out.println("obj = " + ((Car)obj).getYear());
    }
    assertEquals(0, expected.size());

    Set<Car> filterSet = new HashSet<Car>();
    filterSet.add(cars[0]);
    for (Object obj : query.query(filterSet)) {
      fail();
    }

    filterSet.clear();
    filterSet.add(cars[2]);
    expected.add(cars[2]);
    for (Object obj : query.query(filterSet)) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());
  }

  public void testWithinQuery() {
    GeometryFactory fac = new GeometryFactory();
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 2; x++) {
        Car car = cars[i++];
        Coordinate coord = new Coordinate(x, y);
        Geometry geom = fac.createPoint(coord);
        geography.move(car, geom);
      }
    }

    Shed shed = new Shed();
    town.add(shed);
    Coordinate[] coords = new Coordinate[]{new Coordinate(-1, .5), new Coordinate(1.5, .5),
            new Coordinate(1.5, 1.5), new Coordinate(-1, 1.5), new Coordinate(-1, .5)};
    Geometry geom = fac.createPolygon(fac.createLinearRing(coords), null);
    geography.move(shed, geom);

    Set<Shed> expected = new HashSet<Shed>();
    expected.add(shed);

    WithinQuery query = new WithinQuery(geography, cars[2]);
    for (Object obj : query.query()) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());

    Set<Shed> filterSet = new HashSet<Shed>();
    for (Object obj : query.query(filterSet)) {
      fail();
    }

    filterSet.clear();
    filterSet.add(shed);
    expected.add(shed);
    for (Object obj : query.query(filterSet)) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());
  }

  public void testWithinPredicate() {
    GeometryFactory fac = new GeometryFactory();
    // these shed's have lat lon of montanta cities
    Shed billings = new Shed("Billings");
    Coordinate coord = new Coordinate(-108.5333, 45.8);
    Geometry geom = fac.createPoint(coord);
    geography.move(billings, geom);

    Shed bozeman = new Shed("bozeman");
    coord = new Coordinate(-111.15, 45.78333);
    geom = fac.createPoint(coord);
    geography.move(bozeman, geom);

    // ~203,000 meters from billings to bozeman
    
    Within within = new Within(billings, bozeman, 204000);
    assertTrue(geography.evaluate(within));
    
    within = new Within(billings, bozeman, 201000);
    assertFalse(geography.evaluate(within));
    
  }

  
  public void testWithinDistance() throws TransformException {
    GeometryFactory fac = new GeometryFactory();
    // these shed's have lat lon of montanta cities
    Shed billings = new Shed("Billings");
    Coordinate coord = new Coordinate(-108.5333, 45.8);
    Geometry geom = fac.createPoint(coord);
    geography.move(billings, geom);

    Shed kalispell = new Shed("Kalispell");
    coord = new Coordinate(-114.26667, 48.3);
    geom = fac.createPoint(coord);
    geography.move(kalispell, geom);

    Shed bozeman = new Shed("bozeman");
    coord = new Coordinate(-111.15, 45.78333);
    geom = fac.createPoint(coord);
    geography.move(bozeman, geom);

    // ~516K from billings to kalispell
    // ~203K from billings to bozeman

    GeographyWithin within = new GeographyWithin(geography, 220 * 1000, billings);
    Set<Shed> expected = new HashSet<Shed>();
    expected.add(bozeman);
    for (Object obj : within.query()) {
      assertTrue(expected.remove(obj));
    }

    assertEquals(0, expected.size());

    within = new GeographyWithin(geography, 1000, geography.getGeometry(billings));
    expected = new HashSet<Shed>();
    expected.add(billings);
    for (Object obj : within.query()) {
      assertTrue(expected.remove(obj));
    }

    within = new GeographyWithin(geography, 1000, billings);
    assertTrue(!within.query().iterator().hasNext());

    within = new GeographyWithin(geography, 520 * 1000, kalispell);
    expected = new HashSet<Shed>();
    expected.add(billings);
    expected.add(bozeman);
    for (Object obj : within.query()) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());
  }

  public void testIntersectsQuery() {
    GeometryFactory fac = new GeometryFactory();
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 2; x++) {
        Car car = cars[i++];
        Coordinate coord = new Coordinate(x, y);
        Geometry geom = fac.createPoint(coord);
        geography.move(car, geom);
      }
    }

    Shed shed = new Shed();
    town.add(shed);
    Coordinate[] coords = new Coordinate[]{new Coordinate(0, .5), new Coordinate(1.5, .5),
            new Coordinate(1.5, 1.5), new Coordinate(0, 1.5), new Coordinate(0, .5)};
    Geometry geom = fac.createPolygon(fac.createLinearRing(coords), null);
    geography.move(shed, geom);

    Set<Shed> expected = new HashSet<Shed>();
    expected.add(shed);

    IntersectsQuery query = new IntersectsQuery(geography, cars[2]);
    for (Object obj : query.query()) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());

    Set<Shed> filterSet = new HashSet<Shed>();
    for (Object obj : query.query(filterSet)) {
      fail();
    }

    filterSet.clear();
    filterSet.add(shed);
    expected.add(shed);
    for (Object obj : query.query(filterSet)) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());
  }

  public void testTouches() {
    GeometryFactory fac = new GeometryFactory();
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 2; x++) {
        Car car = cars[i++];
        Coordinate coord = new Coordinate(x, y);
        Geometry geom = fac.createPoint(coord);
        geography.move(car, geom);
      }
    }

    Shed shed = new Shed();
    town.add(shed);
    Coordinate[] coords = new Coordinate[]{new Coordinate(0, .5), new Coordinate(1.5, .5),
            new Coordinate(1.5, 1.5), new Coordinate(0, 1.5), new Coordinate(0, .5)};
    Geometry geom = fac.createPolygon(fac.createLinearRing(coords), null);
    geography.move(shed, geom);

    Road road = new Road();
    Geometry rg = fac.createLineString(new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, .5)});
    geography.move(road, rg);

    Set expected = new HashSet();
    expected.add(shed);
    expected.add(cars[0]);

    TouchesQuery query = new TouchesQuery(geography, road);
    for (Object obj : query.query()) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());

    Set<Shed> filterSet = new HashSet<Shed>();
    for (Object obj : query.query(filterSet)) {
      fail();
    }

    filterSet.clear();
    filterSet.add(shed);
    expected.add(shed);
    for (Object obj : query.query(filterSet)) {
      assertTrue(expected.remove(obj));
    }
    assertEquals(0, expected.size());
  }

  public void testRemove() {
    GeometryFactory fac = new GeometryFactory();
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 2; x++) {
        Car car = cars[i++];
        Coordinate coord = new Coordinate(x, y);
        Geometry geom = fac.createPoint(coord);
        geography.move(car, geom);
      }
    }
    geography.move(cars[0], geography.getGeometry(cars[2]));

    town.remove(cars[2]);
    Envelope env = new Envelope(1.5, -1, 0.5, 2.5);
    Set<Car> carSet = new HashSet<Car>();

    carSet.add(cars[0]);
    carSet.add(cars[3]);
    carSet.add(cars[4]);
    carSet.add(cars[5]);
    for (Object obj : geography.getObjectsWithin(env)) {
      assertTrue(carSet.remove(obj));
    }
    assertEquals(0, carSet.size());
  }


  /*
    * Test method for 'repast.simphony.gis.Geography.setCRS(String)'
    */
  public void testSetCRSString() throws FactoryException, TransformException {
    GeometryFactory fac = new GeometryFactory();
    Coordinate coord = new Coordinate(-87.2, 42.1);
    Geometry geom = fac.createPoint(coord);
    geography.move(houses[0], geom);
    CoordinateReferenceSystem crs = geography.getCRS();
    geography.setCRS("epsg:26971");
    assertEquals("EPSG:NAD83 / Illinois East", geography.getCRS().getName().toString());

    Geometry newGeom = geography.getGeometry(houses[0]);

    MathTransform transform = CRS.findMathTransform(crs, CRS.decode("epsg:26971"));
    Geometry tGeom = JTS.transform(geom, transform);
    assertEquals(newGeom.getCoordinate().x, tGeom.getCoordinate().x);
    assertEquals(newGeom.getCoordinate().y, tGeom.getCoordinate().y);
  }

  /*
    * Test method for 'repast.simphony.gis.Geography.getCRS()'
    */
  public void testGetCRS() {
  	// Note WCS 1.0 standard changed "WGS84" to "WGS84(DD)"
    assertEquals("WGS84(DD)", geography.getCRS().getName().toString());
  }

  /*
    * Test method for 'repast.simphony.gis.Geography.getAdder()'
    */
  public void testGetAdder() {
    assertEquals("SimpleAdder", geography.getAdder().getClass().getSimpleName());
  }

  /*
    * Test method for 'repast.simphony.gis.Geography.setAdder(Adder<T>)'
    */
  public void testSetAdder() {
    geography.setAdder(new RandomGISAdder(new GeometryFactory().toGeometry(new Envelope(0, 100, 0, 4))));
    assertEquals("RandomGISAdder", geography.getAdder().getClass().getSimpleName());
    town.remove(cars[0]);
    town.add(cars[0]);
    Geometry geometry = geography.getGeometry(cars[0]);
    assertTrue(geometry != null);
    assertTrue(geometry.getCoordinate().x >= 0 && geometry.getCoordinate().x <= 100);
    assertTrue(geometry.getCoordinate().y >= 0 && geometry.getCoordinate().y <= 4);
  }

  class PListener implements ProjectionListener {

    Object movedObj, addedObj, removedObj;
    Projection proj;

    public void projectionEventOccurred(ProjectionEvent evt) {

      proj = evt.getProjection();

      ProjectionEvent.Type type = evt.getType();
      if (type == ProjectionEvent.Type.OBJECT_MOVED) {
        movedObj = evt.getSubject();
      } else if (type == ProjectionEvent.OBJECT_REMOVED) {
        removedObj = evt.getSubject();
      } else if (type == ProjectionEvent.OBJECT_ADDED) {
        addedObj = evt.getSubject();
      }
    }
  }

  public void testProjectionListener() {
    PListener listener = new PListener();
    geography.addProjectionListener(listener);
    town.remove(cars[0]);
    assertEquals(cars[0], listener.removedObj);
    Car car = new Car("foo", "bar", 199, "blue");
    town.add(car);
    assertEquals(car, listener.addedObj);
    Coordinate coord = new Coordinate(-87.2, 42.1);
    Geometry geom = new GeometryFactory().createPoint(coord);
    geography.move(car, geom);
    assertEquals(car, listener.movedObj);
    assertEquals(geography, listener.proj);
  }
}
