package repast.simphony.essentials;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.jscience.physics.amount.Amount;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkFileFormat;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.DefaultScheduleFactory;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.DefaultContinuousSpace;
import repast.simphony.space.graph.DirectedJungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.grid.BouncyBorders;
import repast.simphony.space.grid.DefaultGrid;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.MultiOccupancyCellAccessor;
import repast.simphony.space.grid.SingleOccupancyCellAccessor;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.projection.Projection;
import repast.simphony.valueLayer.ContinuousValueLayer;
import repast.simphony.valueLayer.GridValueLayer;
import repast.simphony.valueLayer.ValueLayer;

/**
 * Tests of RepastEssentials
 *
 * @author Nick Collier
 */
public class RETest extends TestCase {


  private Context root;

  public void setUp() {
    // context tree by id is root: child1, child2, then child2: grandchild2, grandchild2
    root = new DefaultContext("root");
    root.addProjection(new DirectedJungNetwork("rootNet"));
    root.addSubContext(new DefaultContext("child1"));
    DefaultContext child2 = new DefaultContext("child2");
    child2.addProjection(new DirectedJungNetwork("net1"));
    root.addSubContext(child2);
    child2.addSubContext(new DefaultContext("grandchild2"));

    DefaultContext grandchild1 = new DefaultContext("grandchild1");
    grandchild1.addProjection(new DirectedJungNetwork("net2"));
    child2.addSubContext(grandchild1);
    
    root.addProjection(new DefaultGrid("rootGrid"));
    root.addProjection(new DefaultContinuousSpace("rootSpace"));
    
    RunState.init();
    RunState.getInstance().setMasterContext(root);
    DefaultScheduleFactory fac = new DefaultScheduleFactory();
    RunEnvironment.init(fac.createSchedule(), null, null, false);
  }


  public void testFindContext() {
    Context context = RepastEssentials.FindContext("root");
    assertEquals(root, context);

    context = RepastEssentials.FindContext("root/child2");
    assertNotNull(context);
    assertEquals(root.getSubContext("child2"), context);

    context = RepastEssentials.FindContext("root/child2/grandchild1");
    assertNotNull(context);
    assertEquals(root.getSubContext("child2").getSubContext("grandchild1"), context);

    // try without specifying root as that is what the original RepastEssentials code
    // seems to try to do
    context = RepastEssentials.FindContext("child2/grandchild2");
    assertNotNull(context);
    assertEquals(root.getSubContext("child2").getSubContext("grandchild2"), context);

    context = RepastEssentials.FindContext("root/foo/grandchild1");
    assertNull(context);
  }

  public void testGetObjs() {
    for (int i = 0; i < 5; i++) {
      root.add(i);
    }

    root.add("hello");
    root.add("goodbye");

    Set<String> set = new HashSet<String>();
    set.add("hello");
    set.add("goodbye");

    for (Object obj : RepastEssentials.GetObjects("root", "java.lang.String")) {
      assertTrue(set.remove(obj));
    }
    assertEquals(0, set.size());

    set.add("hello");
    set.add("goodbye");
    for (Object obj : RepastEssentials.GetRandomObjects("root", "java.lang.String", 1)) {
      assertTrue(set.remove(obj));
    }
    assertEquals(1, set.size());

    set.clear();
    set.add("hello");
    set.add("goodbye");
    for (Object obj : RepastEssentials.GetRandomObjects("root", "java.lang.String", 10)) {
      assertTrue(set.remove(obj));
    }
    assertEquals(0, set.size());


  }

  public void testFindParentContext() {
    Context context = RepastEssentials.FindParentContext("root/child2");
    assertNotNull(context);
    assertEquals(root, context);

    context = RepastEssentials.FindParentContext("root/child2/grandchild1");
    assertNotNull(context);
    assertEquals(root.getSubContext("child2"), context);

    // try without specifying root as that is what the original RepastEssentials code
    // seems to try to do
    context = RepastEssentials.FindParentContext("child2/grandchild2");
    assertNotNull(context);
    assertEquals(root.getSubContext("child2"), context);

    context = RepastEssentials.FindParentContext("root/foo/grandchild1");
    assertNull(context);

  }

  public void testFindProjection() {
    Projection proj = RepastEssentials.FindProjection("/root/rootNet");
    assertNotNull(proj);
    assertEquals(root.getProjection("rootNet"), proj);
    assertEquals(proj, RepastEssentials.FindProjection("rootNet"));

    proj = RepastEssentials.FindProjection("child2/grandchild1/net2");
    assertNotNull(proj);
    assertEquals(root.getSubContext("child2").getSubContext("grandchild1").getProjection("net2"), proj);

    proj = RepastEssentials.FindProjection("root/child2/grandchild1/net2");
    assertNotNull(proj);
    assertEquals(root.getSubContext("child2").getSubContext("grandchild1").getProjection("net2"), proj);

    proj = RepastEssentials.FindProjection("child2/net10");
    assertNull(proj);
  }
  
  public void testFindNetwork() {
    Network net = RepastEssentials.FindNetwork("/root/rootNet");
    assertNotNull(net);
    assertEquals(root.getProjection("rootNet"), net);
    assertEquals(net, RepastEssentials.FindNetwork("rootNet"));
  }
  
  public void testFindGrid() {
    Grid grid = RepastEssentials.FindGrid("/root/rootGrid");
    assertNotNull(grid);
    assertEquals(root.getProjection("rootGrid"), grid);
    assertEquals(grid, RepastEssentials.FindGrid("rootGrid"));
  }
  
  public void testFindContinuousSpace() {
    ContinuousSpace space = RepastEssentials.FindContinuousSpace("/root/rootSpace");
    assertNotNull(space);
    assertEquals(root.getProjection("rootSpace"), space);
    assertEquals(space, RepastEssentials.FindContinuousSpace("rootSpace"));
  }

  public void testCreateContext() {
    Context context = RepastEssentials.CreateContext("/root/child1", "baby wee");
    assertNotNull(context);
    assertEquals(root.getSubContext("child1").getSubContext("baby wee"), context);
  }

  public void testRemoveContext() {
    // add it
    Context context = RepastEssentials.CreateContext("/root/child1", "baby wee");
    assertNotNull(context);
    Context babyWee = root.getSubContext("child1").getSubContext("baby wee");
    assertEquals(babyWee, context);

    context = RepastEssentials.RemoveContext("child1/baby wee");
    assertNotNull(context);
    assertEquals(babyWee, context);
    assertNull(root.getSubContext("child1").getSubContext("baby wee"));
  }

  public void testCreateProjection() {
    Projection proj = RepastEssentials.CreateProjection("/root/child1", "grid", "Grid", true,
            "BouncyBorders", 10, 20, 30);
    assertNotNull(proj);
    assertEquals(root.getSubContext("child1").getProjection("grid"), proj);
    Grid grid = (Grid) proj;
    GridDimensions dims = grid.getDimensions();
    assertEquals(10, dims.getWidth());
    assertEquals(20, dims.getHeight());
    assertEquals(30, dims.getDepth());
    assertTrue(grid.getGridPointTranslator() instanceof BouncyBorders);
    assertTrue(grid.getCellAccessor() instanceof MultiOccupancyCellAccessor);

    proj = RepastEssentials.CreateProjection("/root/child1", "grid", "Grid", false,
            "StrictBorders", 10, 20, 30);
    assertNotNull(proj);
    assertEquals(root.getSubContext("child1").getProjection("grid"), proj);
    grid = (Grid) proj;
    dims = grid.getDimensions();
    assertEquals(10, dims.getWidth());
    assertEquals(20, dims.getHeight());
    assertEquals(30, dims.getDepth());
    assertTrue(grid.getGridPointTranslator() instanceof StrictBorders);
    assertTrue(grid.getCellAccessor() instanceof SingleOccupancyCellAccessor);

    proj = RepastEssentials.CreateProjection("/root/child1", "net", "Graph", true);
    assertNotNull(proj);
    assertEquals(root.getSubContext("child1").getProjection("net"), proj);
    Network net = (Network) proj;
    assertTrue(net.isDirected());

    proj = RepastEssentials.CreateProjection("/root/child1", "net2", "Graph", false,
            // these should be ignored
            "StrictBorders", 10, 20, 30);
    assertNotNull(proj);
    assertEquals(root.getSubContext("child1").getProjection("net2"), proj);
    net = (Network) proj;
    assertTrue(!net.isDirected());

    proj = RepastEssentials.CreateProjection("/root/child1", "cs", "Continuous", false,
            "StrictBorders", 10, 20, 30);
    assertNotNull(proj);
    assertEquals(root.getSubContext("child1").getProjection("cs"), proj);
    ContinuousSpace space = (ContinuousSpace) proj;
    Dimensions spaceDims = space.getDimensions();
    assertEquals(10.0, spaceDims.getWidth());
    assertEquals(20.0, spaceDims.getHeight());
    assertEquals(30.0, spaceDims.getDepth());
    assertTrue(space.getPointTranslator() instanceof repast.simphony.space.continuous.StrictBorders);
  }

  public void testRemoveProjection() {
    Projection proj = RepastEssentials.CreateProjection("/root/child1", "grid", "Grid", true,
            "BouncyBorders", 10, 20, 30);
    assertNotNull(proj);
    assertEquals(root.getSubContext("child1").getProjection("grid"), proj);

    Projection removed = RepastEssentials.RemoveProjection("/child1/grid");
    assertEquals(proj, removed);
    assertNull(root.getSubContext("child1").getProjection("grid"));
  }

  public void testCreateAgent() {
    Object obj = RepastEssentials.CreateAgent("/root/child2", "java.lang.Object");
    assertEquals(1, root.getSubContext("child2").size());
    assertEquals(obj, root.getSubContext("child2").getRandomObject());
  }

  public void testEdges() {
    Object source = new Object();
    Object target = new Object();
    root.add(source);
    root.add(target);

    Network net = (Network) RepastEssentials.CreateProjection("/root", "net", "Graph", true);
    RepastEdge edge = RepastEssentials.CreateEdge("/root/net", source, target);
    assertEquals(source, edge.getSource());
    assertEquals(target, edge.getTarget());
    assertEquals(1, net.numEdges());
    assertEquals(edge, net.getEdges().iterator().next());
    net.removeEdge(edge);

    edge = RepastEssentials.CreateEdge("/root/net", source, target, 2.0f);
    assertEquals(source, edge.getSource());
    assertEquals(target, edge.getTarget());
    assertEquals(2.0, edge.getWeight());
    assertEquals(1, net.numEdges());
    assertEquals(edge, net.getEdges().iterator().next());

    RepastEdge foundEdge = RepastEssentials.FindEdge("/root/net", source, target);
    assertEquals(edge, foundEdge);

    assertEquals(2.0, RepastEssentials.GetEdgeWeight("/root/net", source, target));
    foundEdge = RepastEssentials.SetEdgeWeight("/root/net", source, target, 1.5);
    assertEquals(edge, foundEdge);
    assertEquals(1.5, foundEdge.getWeight());

    foundEdge = RepastEssentials.RemoveEdge("/root/net", source, target);
    assertEquals(edge, foundEdge);
    assertEquals(0, net.numEdges());
    assertNull(RepastEssentials.FindEdge("/root/net", source, target));
  }

  public void testValueLayers() {
    GridValueLayer layer = RepastEssentials.CreateGridValueLayer("/root/child1", "gvl", 10, 20);
    assertNotNull(layer);
    assertEquals(10.0, layer.getDimensions().getDimension(0));
    assertEquals(20.0, layer.getDimensions().getDimension(1));

    ValueLayer oLayer = RepastEssentials.FindValueLayer("/root/child1/gvl");
    assertEquals(oLayer, layer);

    ContinuousValueLayer clayer = RepastEssentials.CreateContinuousValueLayer("root", "cvl", 10.5, 20.3);
    assertNotNull(clayer);
    assertEquals(10.5, clayer.getDimensions().getDimension(0));
    assertEquals(20.3, clayer.getDimensions().getDimension(1));

    oLayer = RepastEssentials.FindValueLayer("root/cvl");
    assertEquals(oLayer, clayer);

    layer = new GridValueLayer("gvl2", false, 1, 2);
    RepastEssentials.AddValueLayer("root/child1", layer);

    assertEquals(layer, RepastEssentials.FindValueLayer("root/child1/gvl2"));


  }

  public void testNetLoader() {
    //NetworkBuilder builder = new NetworkBuilder("Network", context, true);
    //builder.load("./repast.simphony.core/test/repast/simphony/graph/double_matrix.dl", NetworkFileFormat.DL,

    Network net = RepastEssentials.CreateNetwork("/root", "Loaded Net", true, "java.lang.Object",
            "./repast.simphony.core/test/repast/simphony/graph/double_matrix.dl", NetworkFileFormat.DL);
    assertNotNull(net);
    assertEquals(19, net.getDegree());

  }

  public void testAgents() {
    Object obj = new Object();
    Context context = RepastEssentials.AddAgentToContext("/root/child1", obj);
    assertEquals(root.getSubContext("child1"), context);
    assertEquals(1, context.size());
    assertEquals(obj, context.iterator().next());

    context = RepastEssentials.RemoveAgentFromContext("/root/child1", obj);
    assertEquals(root.getSubContext("child1"), context);
    assertEquals(0, context.size());

    root.getSubContext("child1").add(obj);
    root.getSubContext("child2").add(obj);
    root.add(new Object());

    context = RepastEssentials.RemoveAgentFromModel(obj);
    assertEquals(root, context);
    assertEquals(0, root.getSubContext("child1").size());
    assertEquals(0, root.getSubContext("child2").size());
    assertEquals(1, root.size());
  }

  public void testMove() {
    Projection proj = RepastEssentials.CreateProjection("/root/child1", "grid", "Grid", true,
            "BouncyBorders", 10, 20, 30);
    assertNotNull(proj);
    Context context = root.getSubContext("child1");
    assertEquals(context.getProjection("grid"), proj);
    Object obj = new Object();
    context.add(obj);
    boolean result = RepastEssentials.MoveAgent("/root/child1/grid", obj, 4, 3, 1);
    assertTrue(result);
    Grid grid = (Grid) proj;
    GridPoint pt = grid.getLocation(obj);
    assertEquals(4, pt.getX());
    assertEquals(3, pt.getY());
    assertEquals(1, pt.getZ());
  }

  public void testTickCount() {
    ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
    Amount<Duration> unitAmount = Amount.valueOf(2L, SI.SECOND);
    schedule.setTimeUnits(unitAmount);
    schedule.schedule(ScheduleParameters.createOneTime(2.5), new IAction() {
      public void execute() {
      }
    });

    schedule.execute();
    assertEquals(2.5, RepastEssentials.GetTickCount());
    assertEquals(unitAmount.times(2.5), RepastEssentials.GetTickCountInTimeUnits());
  }

  class TestObject {

    double tick;
    String name = "foo";

    public void run() {
      this.tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
    }

    public void run(int val) {
      this.tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount() + val;
    }

    public double getTick() {
      return tick;
    }

    public void setTick(double tick) {
      this.tick = tick;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public void testActions() {
    ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
    TestObject obj = new TestObject();
    RepastEssentials.ScheduleAction(obj, "run", 2);
    schedule.execute();
    assertEquals(1.0, schedule.getTickCount());
    // current tick + param = 3.0
    assertEquals(3.0, obj.tick);

    obj.tick = 0;
    RepastEssentials.ScheduleAction(obj, 2.0, "run");
    schedule.execute();
    assertEquals(2.0, obj.tick);

    ISchedulableAction action = RepastEssentials.ScheduleAction(obj, 3.0, 1.0, "run", 2);
    for (int i = 0; i < 5; i++) {
      schedule.execute();
      assertEquals(i + 3.0 + 2, obj.tick);
    }

    RepastEssentials.CancelAction(action);
    obj.tick = -1;
    for (int i = 0; i < 10; i++) {
      schedule.execute();
      assertEquals(-1.0, obj.tick);
    }
  }

  public void testNetMethods() {
    Object one = new Object();
    Object two = new Object();
    Object three = new Object();
    root.add(one);
    root.add(two);
    root.add(three);

    RepastEdge oneToTwo = RepastEssentials.CreateEdge("/root/rootNet", one, two);
    RepastEdge twoToThree = RepastEssentials.CreateEdge("/root/rootNet", two, three);
    RepastEdge oneToThree = RepastEssentials.CreateEdge("/root/rootNet", one, three);

    List list = RepastEssentials.GetPredecessors("/root/rootNet", one);
    assertEquals(0, list.size());

    list = RepastEssentials.GetSuccessors("/root/rootNet", one);
    assertEquals(2, list.size());
    assertTrue(list.contains(two));
    assertTrue(list.contains(three));

    list = RepastEssentials.GetPredecessors("/root/rootNet", three);
    assertEquals(2, list.size());
    assertTrue(list.contains(two));
    assertTrue(list.contains(one));


    list = RepastEssentials.GetAdjacent("/root/rootNet", two);
    assertEquals(2, list.size());
    assertTrue(list.contains(three));
    assertTrue(list.contains(one));


    list = RepastEssentials.GetInEdges("/root/rootNet", three);
    assertEquals(2, list.size());
    assertTrue(list.contains(twoToThree));
    assertTrue(list.contains(oneToThree));

    list = RepastEssentials.GetOutEdges("/root/rootNet", one);
    assertEquals(2, list.size());
    assertTrue(list.contains(oneToThree));
    assertTrue(list.contains(oneToTwo));

    list = RepastEssentials.GetEdges("/root/rootNet", two);
    assertEquals(2, list.size());
    assertTrue(list.contains(twoToThree));
    assertTrue(list.contains(oneToTwo));
  }

  public void testFileReadWrite() {
    SimpleDataObject obj = new SimpleDataObject();
    RepastEssentials.ReadExternalFile(obj,
            "./test/repast/simphony/essentials/SimpleDef.xml",
            "./test/repast/simphony/essentials/simple_data_read.txt");

    assertEquals("Nick Collier", obj.getName());
    assertEquals(23.22343, obj.getValue());

    obj.setName("Sam Jones");
    obj.setValue(3.2);
    RepastEssentials.WriteExternalFile(obj, "./test/repast/simphony/essentials/SimpleDef.xml",
            "./test/repast/simphony/essentials/simple_data_write.txt");

    obj.setName("");
    obj.setValue(0);
    RepastEssentials.ReadExternalFile(obj,
            "./test/repast/simphony/essentials/SimpleDef.xml",
            "./test/repast/simphony/essentials/simple_data_write.txt");

    assertEquals("Sam Jones", obj.getName());
    assertEquals(3.2, obj.getValue());
  }

  public class DataObject {

    private double sinInput, tanInput, sinVal, tanVal;

    public double getSinInput() {
      return sinInput;
    }

    public void setSinInput(double sinInput) {
      this.sinInput = sinInput;
    }

    public double getSinVal() {
      return sinVal;
    }

    public void setSinVal(double sinVal) {
      this.sinVal = sinVal;
    }

    public double getTanInput() {
      return tanInput;
    }

    public void setTanInput(double tanInput) {
      this.tanInput = tanInput;
    }

    public double getTanVal() {
      return tanVal;
    }

    public void setTanVal(double tanVal) {
      this.tanVal = tanVal;
    }
  }


  public void testExecute() {
    DataObject obj = new DataObject();
    obj.setSinInput(2.0);
    obj.setTanInput(.5);
    RepastEssentials.WriteExternalFile(obj, "./test/repast/simphony/essentials/TrigOutDef.xml",
            "./input.txt");
    RepastEssentials.ExecuteProgram("", "./trig.exe");

    RepastEssentials.ReadExternalFile(obj, "./test/repast/simphony/essentials/TrigInDef.xml",
            "./output.txt");
    assertEquals(Math.sin(obj.getSinInput()), obj.getSinVal(), .00001);
    assertEquals(Math.tan(obj.getTanInput()), obj.getTanVal(), .00001);
  }
}
