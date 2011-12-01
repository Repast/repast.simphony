package repast.simphony.xml;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StickyBorders;
import repast.simphony.valueLayer.ContinuousValueLayer;
import repast.simphony.valueLayer.GridValueLayer;

import java.io.StringWriter;
import java.util.*;


/**
 * @author Nick Collier
 */
public class ContextToXmlTests extends TestCase {

  private Context<TestAgent> context;
  private XMLSerializer xmlSer;

  public void setUp() {
    context = new DefaultContext<TestAgent>("foo", "bar");
    for (int i = 0; i < 4; i++) {
      TestAgent agent = new TestAgent(i, "john the " + i);
      context.add(agent);
    }

    Network<TestAgent> network = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("Test Network",
            context, true);
    for (int i = 0; i < 40; i++) {
      TestAgent source = context.getRandomObject();
      TestAgent target = context.getRandomObject();
      RepastEdge edge = network.addEdge(source, target);
      edge.setWeight(Math.random());
    }

    GeographyFactoryFinder.createGeographyFactory(null).createGeography("geog", context,
            new GeographyParameters());

    GridFactoryFinder.createGridFactory(null).createGrid("grid", context,
            GridBuilderParameters.multiOccupancy2D(new RandomGridAdder(), new StickyBorders(), 10, 20));

    ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null).createContinuousSpace("space", context,
            new RandomCartesianAdder(), new BouncyBorders(), 10, 20, 30);

    xmlSer = new XMLSerializer();
  }

  public void testSubContext() {
    Context context = new DefaultContext("sub1", "sub1");
    context.add(new TestAgent(200, "Sam 1"));
    context.add(new TestAgent(201, "Sam 2"));
    this.context.addSubContext(context);

    context = new DefaultContext("sub2", "sub2");
    context.add(new TestAgent(300, "Bill 1"));
    context.add(new TestAgent(301, "Bill 2"));
    this.context.addSubContext(context);

    Context gContext = new DefaultContext("sub3", "sub3");
    TestAgent marty1 = new TestAgent(400, "Marty 1");
    gContext.add(marty1);
    TestAgent marty2 = new TestAgent(401, "Marty 2");
    gContext.add(marty2);
    context.addSubContext(gContext);

    Grid grid = GridFactoryFinder.createGridFactory(null).createGrid("grid", gContext,
            GridBuilderParameters.singleOccupancy1D(new RandomGridAdder(), new StickyBorders(), 10));

    // note that this only attempts to move
    // if the random adder has already filled spots
    // 3 or 4 then these moves will fail.
    grid.moveTo(marty1, 3);
    grid.moveTo(marty2, 4);

    int marty1Loc = grid.getLocation(marty1).getCoord(0);
    int marty2Loc = grid.getLocation(marty2).getCoord(0);


    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    for (TestAgent agent : this.context) {
      agentMap.put(agent.getIntVal(), agent);
    }

    StringWriter string = new StringWriter();
    xmlSer.toXML(this.context, string);
    //System.out.println("string =\n " + string);

    Context<TestAgent> newRoot = (Context<TestAgent>) xmlSer.fromXML(string.toString());
    assertTrue(newRoot instanceof DefaultContext);
    assertEquals("foo", this.context.getId());
    assertEquals("bar", this.context.getTypeID());

    Context sub = this.context.getSubContext("sub1");
    assertNotNull(sub);
    assertEquals("sub1", sub.getId());
    assertEquals("sub1", sub.getTypeID());
    Set<String> nameSet = new HashSet<String>();
    nameSet.add("Sam 1");
    nameSet.add("Sam 2");
    for (Object obj : sub) {
      assertTrue(nameSet.remove(((TestAgent) obj).getName()));
    }
    assertTrue(nameSet.isEmpty());

    sub = this.context.getSubContext("sub2");
    assertNotNull(sub);
    assertEquals("sub2", sub.getId());
    assertEquals("sub2", sub.getTypeID());
    nameSet = new HashSet<String>();
    nameSet.add("Bill 1");
    nameSet.add("Bill 2");
    nameSet.add("Marty 1");
    nameSet.add("Marty 2");
    for (Object obj : sub) {
      assertTrue(nameSet.remove(((TestAgent) obj).getName()));
    }
    assertTrue(nameSet.isEmpty());

    sub = sub.getSubContext("sub3");
    assertNotNull(sub);
    assertEquals("sub3", sub.getId());
    assertEquals("sub3", sub.getTypeID());
    nameSet = new HashSet<String>();
    nameSet.add("Marty 1");
    nameSet.add("Marty 2");
    for (Object obj : sub) {
      assertTrue(nameSet.remove(((TestAgent) obj).getName()));
    }
    assertTrue(nameSet.isEmpty());

    grid = (Grid) sub.getProjection("grid");
    assertNotNull(grid);
    assertEquals(2, grid.size());
    //System.out.println(string);
    for (Object obj : sub) {
      String name = ((TestAgent) obj).getName();
      if (name.equals("Marty 1")) assertEquals(marty1Loc, grid.getLocation(obj).getCoord(0));
      else assertEquals(marty2Loc, grid.getLocation(obj).getCoord(0));
    }

    for (TestAgent agent : newRoot) {
      TestAgent other = agentMap.remove(agent.getIntVal());
      assertEquals(other.getName(), agent.getName());
    }
    assertTrue(agentMap.isEmpty());


  }

  public void testConverter() {
    StringWriter string = new StringWriter();
    xmlSer.registerConverter(new TestConverter());
    xmlSer.toXML(context, string);

    // this one doesn't have the TestConverter registered
    // but it should get registered from the converters created
    // from the xml file itself.
    XMLSerializer xml = new XMLSerializer();
    context = (Context<TestAgent>) xml.fromXML(string.toString());
    for (TestAgent agent : context) {
      assertTrue(agent.getObj().getName().endsWith("__converted"));
    }


  }


  public void testContext() {

    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    for (TestAgent agent : context) {
      agentMap.put(agent.getIntVal(), agent);
    }

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);

    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());
    assertTrue(context instanceof DefaultContext);
    assertEquals("foo", context.getId());
    assertEquals("bar", context.getTypeID());

    for (TestAgent agent : context) {
      TestAgent other = agentMap.remove(agent.getIntVal());
      assertEquals(other.getName(), agent.getName());
    }
    assertTrue(agentMap.isEmpty());
  }

  public void testGrid() {
    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    Grid grid = (Grid) context.getProjection("grid");
    for (TestAgent agent : context) {
      agentMap.put(agent.getIntVal(), agent);
    }

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);
    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());

    Grid newGrid = (Grid) context.getProjection("grid");
    assertEquals(grid.size(), newGrid.size());
    assertEquals(grid.getDimensions(), newGrid.getDimensions());
    assertEquals(grid.getCellAccessor().getClass(), newGrid.getCellAccessor().getClass());
    assertEquals(grid.getGridPointTranslator().getClass(), newGrid.getGridPointTranslator().getClass());
    assertEquals(grid.getAdder().getClass(), newGrid.getAdder().getClass());

    for (Object obj : newGrid.getObjects()) {
      Object other = agentMap.get(((TestAgent) obj).getIntVal());
      assertEquals(grid.getLocation(other), newGrid.getLocation(obj));
    }
  }

  public void testSpace() {
    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    ContinuousSpace space = (ContinuousSpace) context.getProjection("space");
    for (TestAgent agent : context) {
      agentMap.put(agent.getIntVal(), agent);
    }

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);
    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());

    ContinuousSpace newSpace = (ContinuousSpace) context.getProjection("space");
    assertEquals(space.size(), newSpace.size());
    assertEquals(space.getDimensions(), newSpace.getDimensions());
    assertEquals(space.getPointTranslator().getClass(), newSpace.getPointTranslator().getClass());
    assertEquals(space.getAdder().getClass(), newSpace.getAdder().getClass());

    for (Object obj : newSpace.getObjects()) {
      Object other = agentMap.get(((TestAgent) obj).getIntVal());
      assertEquals(space.getLocation(other), newSpace.getLocation(obj));
    }
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

  public void testNetwork() {
    Map<Integer, TestAgent> agentMap = new HashMap<Integer, TestAgent>();
    for (TestAgent agent : context) {
      agentMap.put(agent.getIntVal(), agent);
    }

    Network<TestAgent> origNetwork = (Network<TestAgent>) context.getProjection("Test Network");

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);

    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());
    Network<TestAgent> newNetwork = (Network<TestAgent>) context.getProjection("Test Network");
    assertNotNull(newNetwork);

    Set<RepastEdge<TestAgent>> edges = new HashSet<RepastEdge<TestAgent>>();
    for (TestAgent agent : newNetwork.getNodes()) {
      for (RepastEdge<TestAgent> edge : newNetwork.getEdges(agent)) {
        edges.add(edge);
      }

      TestAgent orig = agentMap.get(agent.getIntVal());
      assertEquals(origNetwork.getDegree(orig), newNetwork.getDegree(agent));
      for (RepastEdge<TestAgent> edge : origNetwork.getEdges(orig)) {
        // remove the matching edge from edges
        removeMatch(edges, edge);
      }
      // edges should be empty because all the edges from the
      // orig and the new agents should be identical.
      assertTrue(edges.isEmpty());
    }
  }

  private void removeMatch(Set<RepastEdge<TestAgent>> edges, RepastEdge<TestAgent> edge) {
    TestAgent source = edge.getSource();
    TestAgent target = edge.getTarget();

    for (Iterator<RepastEdge<TestAgent>> iter = edges.iterator(); iter.hasNext();) {
      RepastEdge<TestAgent> setEdge = iter.next();
      TestAgent nSource = setEdge.getSource();
      TestAgent nTarget = setEdge.getTarget();

      // look for matching edge where source and target are the same and edge weight is the same
      // matches then remove it.
      if (equals(nSource, source) && equals(target, nTarget) && setEdge.getWeight() == edge.getWeight()) {
        iter.remove();
        break;
      }
    }
  }


  public void testGridValueLayer() {
    int[] dims = {10, 3, 4, 5};
    GridValueLayer grid = new GridValueLayer("vl", true, dims);
    for (int i = 0; i < dims[0]; i++) {
      for (int j = 0; j < dims[1]; j++) {
        for (int k = 0; k < dims[2]; k++) {
          for (int l = 0; l < dims[3]; l++) {
            grid.set(Math.random(), i, j, k, l);
          }
        }
      }
    }
    context.addValueLayer(grid);

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);

    System.out.println(string.toString());
    
    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());
    GridValueLayer newGrid = (GridValueLayer) context.getValueLayer("vl");
    assertNotNull(newGrid);
    assertEquals(grid.getDimensions(), newGrid.getDimensions());

    for (int i = 0; i < dims[0]; i++) {
      for (int j = 0; j < dims[1]; j++) {
        for (int k = 0; k < dims[2]; k++) {
          for (int l = 0; l < dims[3]; l++) {
            assertEquals(grid.get(i, j, k, l), newGrid.get(i, j, k, l));
          }
        }
      }
    }
  }

  public void testContValueLayer() {
    double[] dims = {10.3, 3.234, 4, 5.1};
    ContinuousValueLayer layer = new ContinuousValueLayer("cvl", 3.0, true, dims);
    for (int i = 0; i < dims[0]; i++) {
      for (int j = 0; j < dims[1]; j++) {
        for (int k = 0; k < dims[2]; k++) {
          for (int l = 0; l < dims[3]; l++) {
            layer.set(Math.random(), i, j, k, l);
          }
        }
      }
    }
    context.addValueLayer(layer);

    StringWriter string = new StringWriter();
    xmlSer.toXML(context, string);

    context = (Context<TestAgent>) xmlSer.fromXML(string.toString());
    ContinuousValueLayer newLayer = (ContinuousValueLayer) context.getValueLayer("cvl");
    assertNotNull(newLayer);
    assertEquals(layer.getDimensions(), newLayer.getDimensions());

    for (int i = 0; i < dims[0]; i++) {
      for (int j = 0; j < dims[1]; j++) {
        for (int k = 0; k < dims[2]; k++) {
          for (int l = 0; l < dims[3]; l++) {
            assertEquals(layer.get(i, j, k, l), newLayer.get(i, j, k, l));
          }
        }
      }
    }

    // def value is 3.0 so see if no int cells return
    // that
    assertEquals(3.0, newLayer.get(1.34, .2, 3.5, 5));
  }

  private boolean equals(TestAgent one, TestAgent two) {
    return one.getIntVal() == two.getIntVal() && one.getName().equals(two.getName());
  }
}
