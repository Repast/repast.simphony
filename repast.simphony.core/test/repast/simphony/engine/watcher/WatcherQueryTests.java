package repast.simphony.engine.watcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class WatcherQueryTests extends TestCase {

  private Schedule schedule;

  private MyWatcher watcher;
  private MyMultWatcher multWatcher;

  private Generator generator;

  Context<Object> context;

  public WatcherQueryTests(String string) {
    super(string);
  }

  public WatcherQueryTests() {
  }

  public void setUp() {
    RunState.init();
    WatcherTrigger.getInstance().clearNotifiers();
    schedule = new Schedule();
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      reader.processClass(MyWatcher.class);
      reader.processClass(MyMultWatcher.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    context = new DefaultContext<Object>();
    context.addContextListener(reader.getContextListener(schedule));
    watcher = new MyWatcher();
    context.add(watcher);
    multWatcher = new MyMultWatcher();
    context.add(multWatcher);

    generator = new Generator();
    context.add(generator);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createRepeating(1, 1), action);
  }

  public void testLinkedQuery() {
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network", context,
        true);
    RepastEdge edge = net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(true, watcher.queryResult("linked"));
    // clear the results
    watcher.queryResult.clear();
    net.removeEdge(edge);
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked"));
  }

  public void testLinkedMultQuery() {
    // multWatcher has a @WatchItems with two elements
    // one triggers on linked in "network_1", the other
    // on linked in "network_2". We test for both these
    // conditions below
    schedule.execute();
    assertTrue(!multWatcher.isTriggered());
    assertEquals("", multWatcher.getWatchID());
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network_1",
        context, true);
    RepastEdge edge = net.addEdge(multWatcher, generator);
    schedule.execute();
    assertTrue(multWatcher.isTriggered());
    assertEquals("watch1", multWatcher.getWatchID());
    // reset the multWatcher
    multWatcher.reset();
    net.removeEdge(edge);
    schedule.execute();
    assertTrue(!multWatcher.isTriggered());
    assertEquals("", multWatcher.getWatchID());

    net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network_2", context, true);
    edge = net.addEdge(multWatcher, generator);
    schedule.execute();
    assertTrue(multWatcher.isTriggered());
    assertEquals("watch2", multWatcher.getWatchID());
    // reset the multWatcher
    multWatcher.reset();
    net.removeEdge(edge);
    schedule.execute();
    assertTrue(!multWatcher.isTriggered());
    assertEquals("", multWatcher.getWatchID());
  }

  public void testMultNoAnnotation() throws NoSuchMethodException {
    Class clazz = multWatcher.getClass();
    Method method = clazz.getMethod("nonAnnotatedTrigger", WatchData.class, Generator.class,
        int.class);
    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", multWatcher,
        method);
    String watchID = "Non Annotated Watch";
    params.setWatchID(watchID);
    WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);

    assertTrue(!multWatcher.isTriggered());
    assertEquals("", multWatcher.getWatchID());
    schedule.execute();
    assertTrue(multWatcher.isTriggered());
    assertEquals(watchID, multWatcher.getWatchID());
    assertEquals(generator, multWatcher.generator);
    assertEquals(generator.getCounter(), multWatcher.val);
  }

  public void testLinkedToQuery() {
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_to"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network", context,
        true);
    RepastEdge edge = net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(true, watcher.queryResult("linked_to"));
    // clear the results
    watcher.queryResult.clear();
    net.removeEdge(edge);
    net.addEdge(generator, watcher);
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_to"));
  }

  public void testLinkedToNamedQuery() {
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_to_named"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network", context,
        true);
    net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_to_named"));
    // clear the results
    net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("family", context, true);
    watcher.queryResult.clear();
    net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(true, watcher.queryResult("linked_to_named"));
  }

  public void testLinkedFromQuery() {
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_from"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network", context,
        true);
    RepastEdge edge = net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_from"));
    // clear the results
    watcher.queryResult.clear();
    net.addEdge(generator, watcher);
    schedule.execute();
    assertEquals(true, watcher.queryResult("linked_from"));
  }

  public void testLinkedNonDirectedQuery() {
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_from"));
    assertEquals(false, watcher.queryResult("linked_to"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network", context,
        false);
    net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(true, watcher.queryResult("linked_from"));
    assertEquals(true, watcher.queryResult("linked_to"));
  }

  public void testWithinQuery() {
    List<Integer> ints = new ArrayList<Integer>();
    for (int i = 0; i < 7; i++) {
      ints.add(new Integer(i));
      context.add(ints.get(i));
    }

    Network net1 = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network",
        context, false);
    net1.addEdge(watcher, ints.get(1), 2);
    net1.addEdge(watcher, ints.get(3), 1);

    net1.addEdge(ints.get(1), ints.get(3), 3);
    net1.addEdge(ints.get(1), generator, 10);

    net1.addEdge(ints.get(2), watcher, 4);
    net1.addEdge(ints.get(2), ints.get(5), 5);

    net1.addEdge(ints.get(3), ints.get(2), 2);
    RepastEdge edge = net1.addEdge(ints.get(3), generator, 2);
    net1.addEdge(ints.get(3), ints.get(5), 8);
    net1.addEdge(ints.get(3), ints.get(6), 4);

    net1.addEdge(generator, ints.get(6), 6);
    net1.addEdge(ints.get(6), ints.get(5), 1);

    schedule.execute();
    // we are testing whether generator is within 3 (<=) of watcher.
    // given the above setup it is.
    assertEquals(true, watcher.queryResult("within"));

    // by removing the edge between 3 and generator
    // the path between watchee and generator is much longer
    net1.removeEdge(edge);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("within"));
  }

  public void testWithinNghQuery() {
    GridBuilderParameters<Object> params = GridBuilderParameters.singleOccupancy2DTorus(
        new SimpleGridAdder<Object>(), 10, 30);
    Grid<Object> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context,
        params);
    grid.moveTo(watcher, 3, 3);
    grid.moveTo(generator, 3, 4);
    schedule.execute();
    assertEquals(true, watcher.queryResult("within_vn"));
    assertEquals(true, watcher.queryResult("within_moore"));

    grid.moveTo(watcher, 9, 29);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("within_vn"));
    assertEquals(false, watcher.queryResult("within_moore"));

    grid.moveTo(watcher, 3, 3);
    grid.moveTo(generator, 1, 1);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("within_vn"));
    assertEquals(true, watcher.queryResult("within_moore"));
  }

  public void testWithinNamedNghQuery() {
    GridBuilderParameters<Object> params = GridBuilderParameters.singleOccupancy2DTorus(
        new SimpleGridAdder<Object>(), 10, 30);
    Grid<Object> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context,
        params);
    grid.moveTo(watcher, 3, 3);
    grid.moveTo(generator, 3, 4);
    schedule.execute();
    assertEquals(false, watcher.queryResult("within_vn_named"));
    assertEquals(false, watcher.queryResult("within_moore_named"));

    grid = GridFactoryFinder.createGridFactory(null).createGrid("family", context, params);
    grid.moveTo(watcher, 3, 3);
    grid.moveTo(generator, 3, 4);
    schedule.execute();
    assertEquals(true, watcher.queryResult("within_vn_named"));
    assertEquals(true, watcher.queryResult("within_moore_named"));

    grid.moveTo(watcher, 9, 29);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("within_vn_named"));
    assertEquals(false, watcher.queryResult("within_moore_named"));

    grid.moveTo(watcher, 3, 3);
    grid.moveTo(generator, 1, 1);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("within_vn_named"));
    assertEquals(true, watcher.queryResult("within_moore_named"));
  }

  public void testWithinNamedQuery() {
    List<Integer> ints = new ArrayList<Integer>();
    for (int i = 0; i < 7; i++) {
      ints.add(new Integer(i));
      context.add(ints.get(i));
    }

    Network net1 = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network",
        context, false);
    net1.addEdge(watcher, ints.get(1), 2);
    net1.addEdge(watcher, ints.get(3), 1);

    net1.addEdge(ints.get(1), ints.get(3), 3);
    net1.addEdge(ints.get(1), generator, 10);

    net1.addEdge(ints.get(2), watcher, 4);
    net1.addEdge(ints.get(2), ints.get(5), 5);

    net1.addEdge(ints.get(3), ints.get(2), 2);
    RepastEdge edge = net1.addEdge(ints.get(3), generator, 2);
    net1.addEdge(ints.get(3), ints.get(5), 8);
    net1.addEdge(ints.get(3), ints.get(6), 4);

    net1.addEdge(generator, ints.get(6), 6);
    net1.addEdge(ints.get(6), ints.get(5), 1);

    schedule.execute();
    // we are testing whether generator is within 3 (<=) of watcher.
    // in "family" network given the above setup it is is in NETWORK
    // but NOT in family.
    assertEquals(false, watcher.queryResult("within_named"));

    // do again but make family network
    net1 = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("family", context, true);
    net1.addEdge(watcher, ints.get(1), 2);
    net1.addEdge(watcher, ints.get(3), 1);

    net1.addEdge(ints.get(1), ints.get(3), 3);
    net1.addEdge(ints.get(1), generator, 10);

    net1.addEdge(ints.get(2), watcher, 4);
    net1.addEdge(ints.get(2), ints.get(5), 5);

    net1.addEdge(ints.get(3), ints.get(2), 2);
    edge = net1.addEdge(ints.get(3), generator, 2);
    net1.addEdge(ints.get(3), ints.get(5), 8);
    net1.addEdge(ints.get(3), ints.get(6), 4);

    net1.addEdge(generator, ints.get(6), 6);
    net1.addEdge(ints.get(6), ints.get(5), 1);

    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(true, watcher.queryResult("within_named"));

    // by removing the edge between 3 and generator
    // the path between watchee and generator is much longer
    net1.removeEdge(edge);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("within_named"));
  }

  public void testColocatedQuery() {
    schedule.execute();
    assertEquals(true, watcher.queryResult("colocated"));
    context.remove(generator);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(false, watcher.queryResult("colocated"));
  }

  public void testNotColocatedQuery() {
    schedule.execute();
    assertEquals(false, watcher.queryResult("not_colocated"));
    context.remove(generator);
    watcher.queryResult.clear();
    schedule.execute();
    assertEquals(true, watcher.queryResult("not_colocated"));
  }

  public void testSubLinkedToNamedQuery() {
    context.remove(watcher);
    context.remove(generator);

    Context<Object> sub = new DefaultContext<Object>();
    MyWatcher watcher = new MyWatcher();
    final Generator generator = new Generator();
    sub.add(watcher);
    sub.add(generator);
    context.addSubContext(sub);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };
    schedule = new Schedule();
    schedule.schedule(ScheduleParameters.createRepeating(1, 1), action);

    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_to_named"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("family", context,
        true);
    net.addEdge(watcher, generator);
    schedule.execute();
    // not found in the "family" projection in the parent context.
    // because these objects exist in the sub context and so use the
    // projections in the subcontext in their queries
    assertEquals(false, watcher.queryResult("linked_to_named"));
    // clear the results
    net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("family", sub, true);
    watcher.queryResult.clear();
    net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked_to_named"));
  }

  public static junit.framework.Test suite() {
    TestSuite suite = new TestSuite(repast.simphony.engine.watcher.WatcherQueryTests.class);
    // TestSuite suite = new TestSuite();
    // suite.addTest(new WatcherQueryTests("testWithinNghQuery"));
    return suite;
  }
}
