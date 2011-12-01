package repast.simphony.gis.io;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.ShapefileLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Shapefile loader tests
 * @author Nick Collier
 */
public class LoaderTest {


  ShapefileLoader<TestAgent> loader;
  Geography geography;

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
    Context context = new DefaultContext();
    GeographyParameters<Object> params = new GeographyParameters<Object>();
    geography = GeographyFactoryFinder.createGeographyFactory(null).createGeography("Geography", context, params);
    File file = new File("/Users/nick/src/pave2/plugins/bootstrap/data/states.shp");
    loader = new ShapefileLoader<TestAgent>(TestAgent.class, file.toURL(), geography, context);
  }

  @Test
  public void testNext() throws IOException, FactoryException, TransformException {
    File file = new File("/Users/nick/src/pave2/plugins/bootstrap/data/states.shp");
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    FeatureCollection collection = store.getFeatureSource().getFeatures();
    MathTransform trans = CRS.findMathTransform(store.getFeatureSource().getSchema().
            getDefaultGeometry().getCoordinateSystem(),
            geography.getCRS(), true);

    FeatureIterator fIterator = collection.features();
    // this relies on the features coming out of the
    // collection in the same order that the agents were
    // created.
    while (loader.hasNext()) {
      TestAgent agent = loader.next();
      Feature feature = fIterator.next();
      assertEquals(((Double)feature.getAttribute("AREA")).doubleValue(), agent.getArea());
      assertEquals(feature.getAttribute("STATE_NAME"), agent.getStateName());
      assertEquals(feature.getAttribute("POP1999"), agent.getPop1999());
      // for some reason the text is the same but the polygons aren't evaluating
      // as equal ...
      assertEquals(JTS.transform(feature.getDefaultGeometry(), trans).toText(),
              geography.getGeometry(agent).toText());
    }
    fIterator.close();
  }

  @Test
  public void testNextWithCArgs() throws IOException, FactoryException, TransformException {
    File file = new File("/Users/nick/src/pave2/plugins/bootstrap/data/states.shp");
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    FeatureCollection collection = store.getFeatureSource().getFeatures();
    MathTransform trans = CRS.findMathTransform(store.getFeatureSource().getSchema().
            getDefaultGeometry().getCoordinateSystem(),
            geography.getCRS(), true);

    FeatureIterator fIterator = collection.features();
    // this relies on the features coming out of the
    // collection in the same order that the agents were
    // created.
    int i = 0;
    while (loader.hasNext()) {
      TestAgent agent = loader.nextWithArgs(new Integer(i), String.valueOf(i));
      Feature feature = fIterator.next();
      assertEquals(((Double)feature.getAttribute("AREA")).doubleValue(), agent.getArea());
      assertEquals(feature.getAttribute("STATE_NAME"), agent.getStateName());
      assertEquals(feature.getAttribute("POP1999"), agent.getPop1999());
      // for some reason the text is the same but the polygons aren't evaluating
      // as equal ...
      assertEquals(JTS.transform(feature.getDefaultGeometry(), trans).toText(),
              geography.getGeometry(agent).toText());
      assertEquals(i, agent.id);
      assertEquals(String.valueOf(i), agent.strID);
      i++;
    }
    fIterator.close();
  }

  @Test
  public void testNextWithObj() throws IOException, FactoryException, TransformException {
    File file = new File("/Users/nick/src/pave2/plugins/bootstrap/data/states.shp");
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    FeatureCollection collection = store.getFeatureSource().getFeatures();
    MathTransform trans = CRS.findMathTransform(store.getFeatureSource().getSchema().
            getDefaultGeometry().getCoordinateSystem(),
            geography.getCRS(), true);

    FeatureIterator fIterator = collection.features();
    // this relies on the features coming out of the
    // collection in the same order that the agents were
    // created.
    int i = 0;
    while (loader.hasNext()) {
      TestAgent agent = new TestAgent(new Integer(i), String.valueOf(i));
      loader.next(agent);
      Feature feature = fIterator.next();
      assertEquals(((Double)feature.getAttribute("AREA")).doubleValue(), agent.getArea());
      assertEquals(feature.getAttribute("STATE_NAME"), agent.getStateName());
      assertEquals(feature.getAttribute("POP1999"), agent.getPop1999());
      // for some reason the text is the same but the polygons aren't evaluating
      // as equal ...
      assertEquals(JTS.transform(feature.getDefaultGeometry(), trans).toText(),
              geography.getGeometry(agent).toText());
      assertEquals(i, agent.id);
      assertEquals(String.valueOf(i), agent.strID);
      i++;
    }
    fIterator.close();
  }
}
