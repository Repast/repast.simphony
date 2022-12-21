package repast.simphony.gis.xml;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.gis.engine.GeographyProjectionController;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.xml.TestAgent;
import repast.simphony.xml.XMLSerializer;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;


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
    
    for (int i = 0; i < 4; i++) {
      TestAgent agent = new TestAgent(i, "john the " + i);
      context.add(agent);
    }
    
    GeographyFactoryFinder.createGeographyFactory(null).createGeography("geog", context,
            new GeographyParameters());
    
    Network<TestAgent> network = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network",
    		context, true);
   
    xmlSer = new XMLSerializer();
  }
 
  public void testGeography() {
    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    Map<Integer, RepastEdge> edgeMap = new HashMap<Integer, RepastEdge>();
    Geography geog = (Geography) context.getProjection("geog");
    Network net = (Network) context.getProjection("network");
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
    
    // Check network with GIS
    for (int i = 0; i < 10; i++) {   	
    	TestAgent source = context.getRandomObject();
    	TestAgent target = context.getRandomObject();
    	RepastEdge edge = net.addEdge(source, target);
    	edge.setWeight(i);
    	edgeMap.put((int)edge.getWeight(), edge);
    	Coordinate[] coords = new Coordinate[5];
    	for (int j = 0; j < 5; j++) {
    		Coordinate coord = new Coordinate(Math.random(), Math.random());
    		coords[j] = coord;
    	}
    	geog.move(edge, factory.createLineString(coords));
    }

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);

    //System.out.println("string = " + string);
    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());

    Geography newGeog = (Geography) context.getProjection("geog");
    assertEquals(geog.size(), newGeog.size());
    assertEquals(geog.getCRS().toWKT(), newGeog.getCRS().toWKT());
    assertEquals(geog.getAdder().getClass(), newGeog.getAdder().getClass());
    
    for (Object obj : newGeog.getLayer(TestAgent.class).getAgentSet()) {
      TestAgent agent = agentMap.get(((TestAgent) obj).getIntVal());
      geomEquals(geog.getGeometry(agent), newGeog.getGeometry(obj));
    }
    
    for (Object obj : newGeog.getLayer(RepastEdge.class).getAgentSet()) {
    	RepastEdge edge = edgeMap.get((int)((RepastEdge) obj).getWeight());	
    	geomEquals(geog.getGeometry(edge), newGeog.getGeometry(obj));
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