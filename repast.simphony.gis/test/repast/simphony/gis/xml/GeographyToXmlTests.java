package repast.simphony.gis.xml;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.gis.engine.GeographyProjectionController;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.xml.TestAgent;
import repast.simphony.xml.XMLSerializer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * Test Geography XML serialization.  The test class has some depedencies on the
 *   XML tests in r.s.core.
 * 
 * @author Nick Collier
 */
public class GeographyToXmlTests extends TestCase {

  private Context<TestAgent> context;
  private XMLSerializer xmlSer;

  public void setUp() {
    // The projection controller registers the projection.
    new GeographyProjectionController();
  	
    context = new DefaultContext<TestAgent>("foo", "bar");
       
    GeographyFactoryFinder.createGeographyFactory(null).createGeography("geog", context,
            new GeographyParameters());

    xmlSer = new XMLSerializer();
  }
 
  public void testGeography() {
    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    Geography geog = (Geography) context.getProjection("geog");
    GeometryFactory factory = new GeometryFactory();
    for (TestAgent agent : context) {
      agentMap.put(agent.getIntVal(), agent);

      Coordinate[] coords = new Coordinate[5];
      for (int i = 0; i < 5; i++) {
        Coordinate coord = new Coordinate(Math.random(), Math.random());
        coords[i] = coord;
      }

      geog.move(agent, factory.createLineString(coords));
    }

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);

    //System.out.println("string = " + string);
    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());

    Geography newGeog = (Geography) context.getProjection("geog");
    assertEquals(geog.size(), newGeog.size());
    assertEquals(geog.getCRS().toWKT(), newGeog.getCRS().toWKT());
    assertEquals(geog.getAdder().getClass(), newGeog.getAdder().getClass());
    for (Object obj : newGeog.getAllObjects()) {
      TestAgent agent = agentMap.get(((TestAgent) obj).getIntVal());
      geomEquals(geog.getGeometry(agent), newGeog.getGeometry(obj));
    }
  }

  private void geomEquals(Geometry one, Geometry two) {
    Coordinate[] c1 = one.getCoordinates();
    Coordinate[] c2 = two.getCoordinates();
    assertEquals(c1.length, c2.length);
    for (int i = 0; i < c1.length; i++) {
      // for some reason some miniscule precision is lost
      // when going back from wkt
      assertEquals(c1[i].x, c2[i].x, .00000001);
      assertEquals(c1[i].y, c2[i].y, .00000001);
    }
  }
}