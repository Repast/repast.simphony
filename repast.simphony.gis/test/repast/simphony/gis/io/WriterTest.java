package repast.simphony.gis.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.measure.Quantity;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.ShapefileWriter;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

/**
 * Tests of shapefile writing.
 * 
 * @author Nick Collier
 */
public class WriterTest {

  static class GisAgent {

    private double wealth = 10;
    private long longVal = 3L;
    private float floatVal = 3.3f;
    private String strVal = "hello";
    private int intVal = 3;

    private Quantity quantity = Quantities.getQuantity(10, Units.METRE);

    public GisAgent() {
    }

    public Quantity getAmount() {
      return quantity;
    }

    public void setAmount(Quantity quantity) {
      this.quantity = quantity;
    }

    public double getWealth() {
      return wealth;
    }

    public void setWealth(double wealth) {
      this.wealth = wealth;
    }

    public long getLongVal() {
      return longVal;
    }

    public float getFloatVal() {
      return floatVal;
    }

    public String getStrVal() {
      return strVal;
    }

    public int getIntVal() {
      return intVal;
    }
  }


  private Geography geog;

  @Before
  public void setUp() {
    DefaultContext context = new DefaultContext();
     GeographyParameters geoParams = new GeographyParameters();
     geog = GeographyFactoryFinder.createGeographyFactory(null)
            .createGeography("Geography", context, geoParams);

    GeometryFactory fac = new GeometryFactory();

    for (int i = 0; i < 10; i++) {
      GisAgent agent = new GisAgent();
      context.add(agent);
      agent.setWealth(i);
      Coordinate coord = new Coordinate(-103.823 + i / 100.0, 44.373 + i / 100.0);
      Point geom = fac.createPoint(coord);
      geog.move(agent, geom);
    }
  }

  @Test
  public void writeTest() throws IOException {
    File file = new File("test/testWrite.shp");
    ShapefileWriter writer = new ShapefileWriter(geog);
    writer.write(geog.getLayer(GisAgent.class).getName(), file.toURI().toURL());

    Set<String> set = new HashSet<String>();
    for (Object obj : geog.getLayer(GisAgent.class).getAgentSet()) {
      set.add(geog.getGeometry(obj).toText());
    }
    
    ShapefileDataStore store = new ShapefileDataStore(file.toURL());
    SimpleFeatureCollection collection = store.getFeatureSource().getFeatures();
    assertEquals(10, collection.size());

    for (SimpleFeatureIterator iter = collection.features(); iter.hasNext();) {
     
    	SimpleFeature feature = iter.next();
    	
    	assertTrue(set.remove(feature.getDefaultGeometry().toString()));
    }

    assertEquals(0, set.size());
  }
}
