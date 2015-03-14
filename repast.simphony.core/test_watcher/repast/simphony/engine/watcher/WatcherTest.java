package repast.simphony.engine.watcher;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class WatcherTest extends TestCase {

  private Schedule schedule;

  public WatcherTest() {
  }

  public WatcherTest(String s) {
    super(s);
  }

  private Class clazz;

  public void setUp() {
    RunState.init();
    WatcherTrigger.getInstance().clearNotifiers();
    schedule = new Schedule();
    clazz = MyWatcher.class;
  }

  public void testNotifyID() {
    MyWatcher watcher = new MyWatcher();
    try {
      WatchParameters blueParams = new WatchParameters(Generator.class.getName(), "counter", watcher,
          clazz.getMethod("watchTrigger", Generator.class, int.class));
      blueParams.setWatchID("blue");
      
      WatchParameters blueParams2 = new WatchParameters(Generator.class.getName(), "counter", watcher,
          clazz.getMethod("watchTrigger", Generator.class, int.class));
      blueParams2.setWatchID("blue");
      
      WatchParameters redParams = new WatchParameters(Generator.class.getName(), "counter", watcher,
          clazz.getMethod("watchTrigger", Generator.class, int.class));
      redParams.setWatchID("red");
      
      NotifierID blueId = new NotifierID(blueParams);
      NotifierID blue2Id = new NotifierID(blueParams2);
      NotifierID redId = new NotifierID(redParams);
      
      assertEquals(blueId, blue2Id);
      assertTrue(!blueId.equals(redId));
      Map<NotifierID, String> map = new HashMap<NotifierID, String>();
      map.put(blueId, "A");
      
      assertFalse(map.containsKey(redId));
      assertNotNull(map.get(blue2Id));
      assertEquals("A", map.get(blue2Id));
      
    } catch (NoSuchMethodException e) {

    }

  }

  public void testBaseCase() {
    MyWatcher watcher = new MyWatcher();
    Generator generator = new Generator();

    Class clazz = watcher.getClass();
    try {

      WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
          clazz.getMethod("watchTrigger", Generator.class, int.class));
      WatcherTrigger.getInstance().addFieldSetWatch(params, null);

      params = new WatchParameters(Generator.class.getName(), "dVal", watcher, clazz.getMethod(
          "watchTrigger", Generator.class, double.class));
      WatcherTrigger.getInstance().addFieldSetWatch(params, null);

      params = new WatchParameters(Generator.class.getName(), "lVal", watcher, clazz.getMethod(
          "watchTrigger", Generator.class, long.class));
      WatcherTrigger.getInstance().addFieldSetWatch(params, null);

      params = new WatchParameters(Generator.class.getName(), "bVal", watcher, clazz.getMethod(
          "watchTrigger", Generator.class, boolean.class));
      WatcherTrigger.getInstance().addFieldSetWatch(params, null);

      params = new WatchParameters(Generator.class.getName(), "fVal", watcher, clazz.getMethod(
          "watchTrigger", Generator.class, float.class));
      WatcherTrigger.getInstance().addFieldSetWatch(params, null);
    } catch (NoSuchMethodException e) {
      e.printStackTrace(); // To change body of catch statement use File |
      // Settings | File Templates.
    }

    // increments the counter
    generator.run();
    // THESE ARGS ARE BACKWARDS!!!
    assertEquals(watcher.generator, generator);
    assertEquals(watcher.val, generator.getCounter());
    assertEquals(watcher.dval, generator.getdVal());
    assertEquals(watcher.lval, generator.getlVal());
    assertEquals(watcher.fval, generator.getfVal());
    // assertEquals(watcher.byval, generator.getByVal());
    assertEquals(watcher.cval, generator.getcVal());
    // assertEquals(watcher.sval, generator.getsVal());
    assertEquals(watcher.bval, generator.isbVal());
  }

  public void testWatcheeCondition() {
    MyWatcher watcher = new MyWatcher();
    Generator generator = new Generator();
    Class clazz = watcher.getClass();
    try {
      WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
          clazz.getMethod("watchTrigger", Generator.class, int.class));
      params.setWatcheeCondition("$field > 10");
      WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    // increments the counter
    generator.run();
    assertEquals(watcher.generator, null);
    assertEquals(watcher.val, -1);

    generator.setCounter(12);
    assertEquals(watcher.generator, generator);
    assertEquals(watcher.val, generator.getCounter());
  }

  public void testWatcheeConditionII() {
    MyWatcher watcher = new MyWatcher();
    Generator generator = new Generator();
    WatchParameters params = null;
    try {
      params = new WatchParameters(Generator.class.getName(), "counter", watcher, clazz.getMethod(
          "watchTrigger", Generator.class, int.class));
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    params
        .setWatcheeCondition("$watchee.getCounter() > 10 && $watcher.getName().equals(\"Fred\") && $field > 10");
    WatcherTrigger.getInstance().addFieldSetWatch(params, null);
    // increments the counter
    generator.run();
    assertEquals(watcher.generator, null);
    assertEquals(watcher.val, -1);

    generator.setCounter(12);
    assertEquals(watcher.generator, generator);
    assertEquals(watcher.val, generator.getCounter());
  }

  public void testBaseCaseII() throws NoSuchMethodException {
    MyWatcher watcher = new MyWatcher();
    Generator generator = new Generator();
    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
        clazz.getMethod("watchTrigger", Generator.class, int.class));
    WatcherTrigger.getInstance().addFieldSetWatch(params, null);
    // increments the counter
    generator.setCounter(15);
    assertEquals(watcher.generator, generator);
    assertEquals(watcher.val, generator.getCounter());
  }

  public void testMultWatchers() throws NoSuchMethodException {
    MyWatcher watcher1 = new MyWatcher();
    MyWatcher watcher2 = new MyWatcher();
    Generator generator = new Generator();

    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher1,
        clazz.getMethod("watchTrigger", Generator.class, int.class));
    WatcherTrigger.getInstance().addFieldSetWatch(params, null);

    params = new WatchParameters(Generator.class.getName(), "counter", watcher2, clazz.getMethod(
        "watchTrigger", Generator.class, int.class));
    WatcherTrigger.getInstance().addFieldSetWatch(params, null);
    // increments the counter
    generator.setCounter(15);
    assertEquals(watcher1.generator, generator);
    assertEquals(watcher1.val, generator.getCounter());

    assertEquals(watcher2.generator, generator);
    assertEquals(watcher2.val, generator.getCounter());

  }

  public void testNotifierNoWatchee() throws NoSuchMethodException {
    MyWatcher watcher = new MyWatcher();
    Generator generator = new Generator();
    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
        clazz.getMethod("watchTrigger", int.class));
    WatcherTrigger.getInstance().addFieldSetWatch(params, null);

    // increments the counter
    generator.run();
    assertTrue(watcher.generator == null);
    assertEquals(watcher.val, generator.getCounter());
  }

  public void testNotifierNoValue() throws NoSuchMethodException {
    MyWatcher watcher = new MyWatcher();
    Generator generator = new Generator();
    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
        clazz.getMethod("watchTrigger", Generator.class));
    WatcherTrigger.getInstance().addFieldSetWatch(params, null);
    // increments the counter
    generator.run();
    assertEquals(watcher.generator, generator);
    assertEquals(watcher.val, -1);
  }

  public void testScheduleI() throws NoSuchMethodException {
    MyWatcher watcher = new MyWatcher();
    final Generator generator = new Generator();

    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
        clazz.getMethod("watchTrigger", Generator.class, int.class));
    params.setTriggerSchedule(WatcherTriggerSchedule.LATER, 0, 0);
    WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createOneTime(0), action);
    // fire the trigger by executing the action
    // this should schedule the trigger to occur at the next execute, but
    // the
    // tick clock should not be incremented
    schedule.execute();
    assertEquals(0.0, schedule.getTickCount());
    assertEquals(watcher.val, 1);
    assertEquals(generator.getCounter(), 1);
  }

  public void testScheduleII() throws NoSuchMethodException {
    MyWatcher watcher = new MyWatcher();
    final Generator generator = new Generator();

    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher,
        clazz.getMethod("watchTrigger", Generator.class, int.class));
    params.setTriggerSchedule(WatcherTriggerSchedule.LATER, 2, 0);
    WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createOneTime(1), action);
    // fire the trigger by executing the action
    // this should schedule the trigger to occur at the next execute, but
    // the
    // tick clock should not be incremented
    schedule.execute();
    assertEquals(1.0, schedule.getTickCount());
    assertEquals(watcher.val, -1);
    assertEquals(generator.getCounter(), 1);

    schedule.execute();
    assertEquals(3.0, schedule.getTickCount());
    assertEquals(watcher.val, 1);
    assertEquals(generator.getCounter(), 1);
  }

  // test priority
  public void testScheduleIII() throws NoSuchMethodException {

    final Generator generator = new Generator();

    MyWatcher watcher3 = new MyWatcher();
    WatchParameters params = new WatchParameters(Generator.class.getName(), "counter", watcher3,
        clazz.getMethod("watchTrigger", Generator.class, int.class));
    params.setTriggerSchedule(WatcherTriggerSchedule.LATER, 2, 3);
    WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);

    MyWatcher watcher1 = new MyWatcher();
    params = new WatchParameters(Generator.class.getName(), "counter", watcher1, clazz.getMethod(
        "watchTrigger", Generator.class, int.class));
    params.setTriggerSchedule(WatcherTriggerSchedule.LATER, 2, 1);
    WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);

    MyWatcher watcher2 = new MyWatcher();
    params = new WatchParameters(Generator.class.getName(), "counter", watcher2, clazz.getMethod(
        "watchTrigger", Generator.class, int.class));
    params.setTriggerSchedule(WatcherTriggerSchedule.LATER, 2, 2);
    WatcherTrigger.getInstance().addFieldSetWatch(params, schedule);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createOneTime(0), action);
    // fire the trigger by executing the action
    // this should schedule the trigger to occur at the next execute, but
    // the
    // tick clock should not be incremented
    schedule.execute();
    assertEquals(0.0, schedule.getTickCount());
    assertEquals(watcher3.val, -1);
    assertEquals(watcher1.val, -1);
    assertEquals(watcher2.val, -1);
    assertEquals(generator.getCounter(), 1);

    schedule.execute();
    assertEquals(2.0, schedule.getTickCount());
    assertEquals(watcher3.val, 1);
    assertEquals(watcher1.val, 1);
    assertEquals(watcher2.val, 1);
    assertEquals(generator.getCounter(), 1);
  }

  public void testDblFieldAnnotation() {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      // ModelSpecReader mReader = new
      // ModelSpecReader(WatcherTest.class.getResource("/repast/engine/watcher/ModelSpec.xml"));
      reader.processClass(MyWatcher.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Context<MyWatcher> context = new DefaultContext<MyWatcher>();
    context.addContextListener(reader.getContextListener(schedule));
    MyWatcher watcher = new MyWatcher();
    context.add(watcher);

    // we've set watcher to listen for fields writes on
    // bVal and counter, so they both should trigger a
    // put of "double_field"
    Generator generator = new Generator();
    generator.setBVal();
    assertTrue(watcher.queryResult("double_field"));
    watcher.queryResult.clear();

    generator.incrCounter();
    assertTrue(watcher.queryResult("double_field"));
  }

  public void testAnnotation() {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      // ModelSpecReader mReader = new
      // ModelSpecReader(WatcherTest.class.getResource("/repast/engine/watcher/ModelSpec.xml"));
      reader.processClass(MyWatcher.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Context<MyWatcher> context = new DefaultContext<MyWatcher>();
    context.addContextListener(reader.getContextListener(schedule));
    MyWatcher watcher = new MyWatcher();
    context.add(watcher);

    assertEquals(watcher.plainTriggerVal, -1);
    assertEquals(watcher.valTriggerVal, -1);
    assertEquals(watcher.genTriggerVal, -1);
    assertEquals(watcher.genValTriggerVal, -1);

    Generator generator = new Generator();
    generator.run();

    assertEquals(watcher.plainTriggerVal, 0);
    assertEquals(watcher.valTriggerVal, generator.getCounter());
    assertEquals(watcher.genTriggerVal, 0);
    assertEquals(watcher.genValTriggerVal, generator.getCounter());
    assertEquals(watcher.generator, generator);
  }

  public void testAnnotationCondition() {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      reader.processClass(MyWatcher.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Context<MyWatcher> context = new DefaultContext<MyWatcher>();
    context.addContextListener(reader.getContextListener(schedule));
    MyWatcher watcher = new MyWatcher();
    context.add(watcher);

    assertEquals(watcher.triggered, false);
    Generator generator = new Generator();
    generator.run();

    assertEquals(watcher.triggered, false);
    generator.run();
    assertEquals(watcher.triggered, true);
  }

  public void testAnnotationSchedule() {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      reader.processClass(MyWatcher.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Context<MyWatcher> context = new DefaultContext<MyWatcher>();
    context.addContextListener(reader.getContextListener(schedule));
    MyWatcher watcher = new MyWatcher();
    context.add(watcher);
    final Generator generator = new Generator();

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createOneTime(0), action);
    schedule.execute();
    assertEquals(0.0, schedule.getTickCount());
    assertEquals(watcher.scheduleTest, false);

    schedule.execute();
    // delta = 2
    assertEquals(2.0, schedule.getTickCount());
    assertEquals(watcher.scheduleTest, true);
  }

  public void testAnnotationBadMethod() {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      reader.processClass(MyWatcher.class);
      reader.processClass(Watcher2.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Context<Watcher2> context = new DefaultContext<Watcher2>();
    context.addContextListener(reader.getContextListener(schedule));
    Watcher2 watcher = new Watcher2();
    try {
      // this should throw an exception because the method is not correct
      // for a watcher.
      context.add(watcher);
      assertTrue(false);
    } catch (RuntimeException ex) {
      assertTrue(true);
    }
  }

  public void testAnnotationQuery() {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      reader.processClass(MyWatcher.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    Context<Object> context = new DefaultContext<Object>();
    context.addContextListener(reader.getContextListener(schedule));
    MyWatcher watcher = new MyWatcher();
    context.add(watcher);

    final Generator generator = new Generator();
    context.add(generator);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createRepeating(1, 1), action);
    schedule.execute();
    assertEquals(false, watcher.queryResult("linked"));
    Network net = NetworkFactoryFinder.createNetworkFactory(null).createNetwork("network", context,
        true);
    net.addEdge(watcher, generator);
    schedule.execute();
    assertEquals(true, watcher.queryResult("linked"));
  }

  public static junit.framework.Test suite() {
    TestSuite suite = new TestSuite(repast.simphony.engine.watcher.WatcherTest.class);
    // TestSuite suite = new TestSuite();
    // suite.addTest(new WatcherTest("testDblFieldAnnotation"));
    return suite;
  }
}
