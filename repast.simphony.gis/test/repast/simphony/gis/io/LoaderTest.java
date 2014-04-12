package repast.simphony.gis.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.ShapefileLoader;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Shapefile loader tests
 * @author Nick Collier
 */
public class LoaderTest {
  Geography geography;
  Context context;
  GeographyParameters<Object> params;
  
  public static class TestAgent {

    private double area;
    private Long pop1999;
    private String stateName;
    private int id;
    private String strID;

    public TestAgent() {}

    public TestAgent(int id, String strID) {
      this.id = id;
      this.strID = strID;
    }

    public double getArea() {
      return area;
    }

    public void setArea(double area) {
      this.area = area;
    }

    public Long getPop1999() {
      return pop1999;
    }

    public void setPop1999(Long pop1999) {
      this.pop1999 = pop1999;
    }

    public String getStateName() {
      return stateName;
    }

    public void setStateName(String stateName) {
      this.stateName = stateName;
    }
  }

  @Before
  public void setUp() throws MalformedURLException {
    context = new DefaultContext();
    params = new GeographyParameters<Object>();
    geography = GeographyFactoryFinder.createGeographyFactory(null).createGeography("Geography", context, params);
  }

  @Test
  public void testNext() throws IOException, FactoryException, TransformException {
    File file = new File("./sampleData/states.shp");
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    SimpleFeatureCollection collection = store.getFeatureSource().getFeatures();
    MathTransform trans = CRS.findMathTransform(
    		store.getFeatureSource().getSchema().getCoordinateReferenceSystem(),
            geography.getCRS(), true);

    SimpleFeatureIterator fIterator = collection.features();
    // this relies on the features coming out of the
    // collection in the same order that the agents were
    // created.
    ShapefileLoader<TestAgent> loader = new ShapefileLoader<TestAgent>(TestAgent.class, file.toURL(), geography, context);
    
    while (loader.hasNext()) {
      TestAgent agent = loader.next();
      SimpleFeature feature = fIterator.next();
      assertEquals(((Double)feature.getAttribute("AREA")).doubleValue(), agent.getArea(), 0);
      assertEquals(feature.getAttribute("STATE_NAME"), agent.getStateName());
      assertEquals(feature.getAttribute("POP1999"), agent.getPop1999());
      // for some reason the text is the same but the polygons aren't evaluating
      // as equal ...
      assertEquals(JTS.transform(((Geometry)feature.getDefaultGeometry()), trans).toText(),
              geography.getGeometry(agent).toText());
    }
    fIterator.close();
  }

  @Test
  public void testNextWithCArgs() throws IOException, FactoryException, TransformException {
    File file = new File("./sampleData/states.shp");
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    SimpleFeatureCollection collection = store.getFeatureSource().getFeatures();
    MathTransform trans = CRS.findMathTransform(
    		store.getFeatureSource().getSchema().getCoordinateReferenceSystem(),
            geography.getCRS(), true);

    SimpleFeatureIterator fIterator = collection.features();
    // this relies on the features coming out of the
    // collection in the same order that the agents were
    // created.
    
    ShapefileLoader<TestAgent> loader = new ShapefileLoader<TestAgent>(TestAgent.class, file.toURL(), geography, context);
    int i = 0;
    while (loader.hasNext()) {
      TestAgent agent = loader.nextWithArgs(new Integer(i), String.valueOf(i));
      SimpleFeature feature = fIterator.next();
      assertEquals(((Double)feature.getAttribute("AREA")).doubleValue(), agent.getArea(), 0);
      assertEquals(feature.getAttribute("STATE_NAME"), agent.getStateName());
      assertEquals(feature.getAttribute("POP1999"), agent.getPop1999());
      // for some reason the text is the same but the polygons aren't evaluating
      // as equal ...
      assertEquals(JTS.transform(((Geometry)feature.getDefaultGeometry()), trans).toText(),
              geography.getGeometry(agent).toText());
      assertEquals(i, agent.id);
      assertEquals(String.valueOf(i), agent.strID);
      i++;
    }
    fIterator.close();
  }

  @Test
  public void testNextWithObj() throws IOException, FactoryException, TransformException {
    File file = new File("./sampleData/states.shp");
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    SimpleFeatureCollection collection = store.getFeatureSource().getFeatures();
    MathTransform trans = CRS.findMathTransform(
    		store.getFeatureSource().getSchema().getCoordinateReferenceSystem(),
            geography.getCRS(), true);

    SimpleFeatureIterator fIterator = collection.features();
    // this relies on the features coming out of the
    // collection in the same order that the agents were
    // created.
    
    ShapefileLoader<TestAgent> loader = new ShapefileLoader<TestAgent>(TestAgent.class, file.toURL(), geography, context);
    int i = 0;
    while (loader.hasNext()) {
      TestAgent agent = new TestAgent(new Integer(i), String.valueOf(i));
      loader.next(agent);
      SimpleFeature feature = fIterator.next();
      assertEquals(((Double)feature.getAttribute("AREA")).doubleValue(), agent.getArea(), 0);
      assertEquals(feature.getAttribute("STATE_NAME"), agent.getStateName());
      assertEquals(feature.getAttribute("POP1999"), agent.getPop1999());
      // for some reason the text is the same but the polygons aren't evaluating
      // as equal ...
      assertEquals(JTS.transform(((Geometry)feature.getDefaultGeometry()), trans).toText(),
              geography.getGeometry(agent).toText());
      assertEquals(i, agent.id);
      assertEquals(String.valueOf(i), agent.strID);
      i++;
    }
    fIterator.close();
  }
}
