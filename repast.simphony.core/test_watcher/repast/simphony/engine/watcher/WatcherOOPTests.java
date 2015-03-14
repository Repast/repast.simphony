/**
 * 
 */
package repast.simphony.engine.watcher;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.CallBackAction;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;
import watcherOOP.test.OOPWatcherOnA;
import watcherOOP.test.OOPWatcherOnB;
import watcherOOP.test.OOPWatcherOnC;
import watcherOOP.test.SimpleWatchee;
import watcherOOP.test.SimpleWatcher;
import watcherOOP.test.WatcheeChildA;
import watcherOOP.test.WatcheeChildB;
import watcherOOP.test.WatcheeChildC;
import watcherOOP.test.WatcherWithQuery;

/**
 * Tests of OOP style watchers where child classes changing parent class
 * watching works.
 * 
 * @author Nick Collier
 */
public class WatcherOOPTests {

  public void setUp() {
    RunState.init();
    WatcherTrigger.getInstance().clearNotifiers();
  }

  @SuppressWarnings("unchecked")
  private Context<Object> setupContext(Schedule schedule, Object... watchers) {
    WatchAnnotationReader reader = new WatchAnnotationReader();
    Set<Class<?>> classes = new HashSet<Class<?>>();

    try {
      for (Object obj : watchers) {
        classes.add(obj.getClass());
      }
      reader.processClasses(new ArrayList<Class<?>>(classes));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Context<Object> context = new DefaultContext<Object>();
    context.addContextListener(reader.getContextListener(schedule));

    for (Object obj : watchers) {
      context.add(obj);
    }

    return context;

  }

  @Test
  public void baseTest() {
    System.out.println("Base Test");
    Schedule schedule = new Schedule();
    SimpleWatcher watcher = new SimpleWatcher();
    setupContext(schedule, watcher);

    int newIntVal = 2;

    SimpleWatchee watchee = new SimpleWatchee();
    CallBackAction action1 = new CallBackAction(watchee, "updateInt", newIntVal);
    schedule.schedule(ScheduleParameters.createOneTime(0), action1);

    String newStringVal = "fulham";
    CallBackAction action2 = new CallBackAction(watchee, "updateString", newStringVal);
    schedule.schedule(ScheduleParameters.createOneTime(1), action2);

    schedule.execute();
    // watchee updateInt called, which should trigger the watcher
    // setting the int val on the watcher
    assertEquals(newIntVal, watcher.getInt());

    schedule.execute();
    // watchee updateString called, triggering the watcher
    // setting the watchers string val
    assertEquals(newStringVal, watcher.getStr());
  }

  @Test
  // Watching C, and value changed in C,
  // parent class (A), and grand parent class (R),
  // should trigger anything watching C.
  public void testOOPChange() {
    System.out.println("Test OOP Change");
    Schedule schedule = new Schedule();
    OOPWatcherOnC watcherOnC = new OOPWatcherOnC();
    setupContext(schedule, watcherOnC);

    SimpleWatchee simple = new SimpleWatchee();

    // sets watched val directly in C
    String newStringVal = "fulham";
    CallBackAction action = new CallBackAction(simple, "updateString", newStringVal);
    schedule.schedule(ScheduleParameters.createOneTime(0), action);

    WatcheeChildC watchee = new WatcheeChildC();

    // sets watched val directly in C
    action = new CallBackAction(watchee, "cIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(1), action);

    // calls method in C that forwards to A
    action = new CallBackAction(watchee, "cIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(2), action);

    // calls on C inherited from A
    action = new CallBackAction(watchee, "aIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(3), action);

    // calls on C inherited from A, A calls R method
    action = new CallBackAction(watchee, "aIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(4), action);

    // calls on C inherited from R
    action = new CallBackAction(watchee, "incrVal");
    schedule.schedule(ScheduleParameters.createOneTime(5), action);

    schedule.execute();
    assertEquals(newStringVal, watcherOnC.getString());
    watcherOnC.reset();

    schedule.execute();
    assertEquals(2, watcherOnC.getVal());
    watcherOnC.reset();

    schedule.execute();
    assertEquals(3, watcherOnC.getVal());
    watcherOnC.reset();

    schedule.execute();
    assertEquals(4, watcherOnC.getVal());
    watcherOnC.reset();

    schedule.execute();
    assertEquals(5, watcherOnC.getVal());
    watcherOnC.reset();

    schedule.execute();
    assertEquals(6, watcherOnC.getVal());
    watcherOnC.reset();

  }

  @Test
  // Watching A and C. Changes to C,
  // should trigger the watcher on both,
  // because C is of type C and A.
  public void testChildInclusion() {
    System.out.println("Test CI");
    Schedule schedule = new Schedule();
    OOPWatcherOnC watcherOnC = new OOPWatcherOnC();
    OOPWatcherOnA watcherOnA = new OOPWatcherOnA();
    setupContext(schedule, watcherOnC, watcherOnA);

    // WatcheeChildA watcheeA = new WatcheeChildA();
    WatcheeChildC watchee = new WatcheeChildC();

    // calls method in C that forwards to A
    CallBackAction action = new CallBackAction(watchee, "cIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(2), action);

    // calls on C inherited from A
    action = new CallBackAction(watchee, "aIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(3), action);

    // calls on C inherited from A, A calls R method
    action = new CallBackAction(watchee, "aIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(4), action);

    // calls on C inherited from R
    action = new CallBackAction(watchee, "incrVal");
    schedule.schedule(ScheduleParameters.createOneTime(5), action);

    schedule.execute();
    assertEquals(2, watcherOnC.getVal());
    assertEquals(2, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(3, watcherOnC.getVal());
    assertEquals(3, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(4, watcherOnC.getVal());
    assertEquals(4, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(5, watcherOnC.getVal());
    assertEquals(5, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();
  }

  @Test
  // Watching C and A. Change A, watcher on A
  // should trigger, but not watcher on C, because
  // A is not of type C.
  public void testChildExclusion() {
    System.out.println("Test CE");
    Schedule schedule = new Schedule();
    OOPWatcherOnC watcherOnC = new OOPWatcherOnC();
    OOPWatcherOnA watcherOnA = new OOPWatcherOnA();
    setupContext(schedule, watcherOnC, watcherOnA);

    WatcheeChildA watchee = new WatcheeChildA();
    WatcheeChildC watcheeC = new WatcheeChildC();

    CallBackAction action = new CallBackAction(watchee, "aIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(1), action);

    // calls on A, forwards to R
    action = new CallBackAction(watchee, "aIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(2), action);

    // calls on A inherited from R
    action = new CallBackAction(watchee, "incrVal");
    schedule.schedule(ScheduleParameters.createOneTime(3), action);

    // calls on C inherited from R
    action = new CallBackAction(watcheeC, "incrVal");
    schedule.schedule(ScheduleParameters.createOneTime(4), action);

    schedule.execute();
    assertEquals(-1, watcherOnC.getVal());
    assertEquals(2, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(-1, watcherOnC.getVal());
    assertEquals(3, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(-1, watcherOnC.getVal());
    assertEquals(4, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();

    // Instance of C triggered, so watcher on C changes
    // as do watcher on A because C is of type A.
    schedule.execute();
    assertEquals(2, watcherOnC.getVal());
    // 2 because the value is being passed form WatcheeC, which
    // incremented to 2
    assertEquals(2, watcherOnA.getVal());
    watcherOnC.reset();
    watcherOnA.reset();
  }

  @Test
  // Watching B, and value changed in B
  // and parent (R), should trigger those watching
  // B and not those watching sibling A.
  public void testSiblingExclusion() {
    System.out.println("Test SE");
    Schedule schedule = new Schedule();
    OOPWatcherOnB watcherOnB = new OOPWatcherOnB();
    OOPWatcherOnA watcherOnA = new OOPWatcherOnA();
    setupContext(schedule, watcherOnB, watcherOnA);

    WatcheeChildA watcheeA = new WatcheeChildA();
    WatcheeChildB watcheeB = new WatcheeChildB();

    watcherOnB.reset();
    watcherOnA.reset();

    CallBackAction action = new CallBackAction(watcheeA, "aIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(1), action);

    action = new CallBackAction(watcheeB, "bIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(2), action);

    action = new CallBackAction(watcheeA, "aIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(3), action);

    action = new CallBackAction(watcheeB, "bIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(4), action);

    schedule.execute();
    assertEquals(2, watcherOnA.getVal());
    assertEquals(-1, watcherOnB.getVal(), 0);
    watcherOnB.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(-1, watcherOnA.getVal());
    assertEquals(2, watcherOnB.getVal(), 0);
    watcherOnB.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(3, watcherOnA.getVal());
    assertEquals(-1, watcherOnB.getVal(), 0);
    watcherOnB.reset();
    watcherOnA.reset();

    schedule.execute();
    assertEquals(-1, watcherOnA.getVal());
    assertEquals(3, watcherOnB.getVal(), 0);
    watcherOnB.reset();
    watcherOnA.reset();
  }

  @Test
  // test that the caching mechanism works --
  // multiple watchers triggering correctly.
  public void testCache() {
    System.out.println("Test Cache");
    Schedule schedule = new Schedule();
    OOPWatcherOnA[] watchers = { new OOPWatcherOnA(), new OOPWatcherOnA(), new OOPWatcherOnA(),
        new OOPWatcherOnA() };
    setupContext(schedule, watchers[0], watchers[1], watchers[2], watchers[3]);

    WatcheeChildA watchee = new WatcheeChildA();
    CallBackAction action = new CallBackAction(watchee, "aIncrVal");
    schedule.schedule(ScheduleParameters.createOneTime(1), action);

    // calls on A, forwards to R
    action = new CallBackAction(watchee, "aIncrValSuper");
    schedule.schedule(ScheduleParameters.createOneTime(2), action);

    // calls on A inherited from R
    action = new CallBackAction(watchee, "incrVal");
    schedule.schedule(ScheduleParameters.createOneTime(3), action);

    schedule.execute();
    for (OOPWatcherOnA watcher : watchers) {
      assertEquals(2, watcher.getVal());
      watcher.reset();
    }

    schedule.execute();
    for (OOPWatcherOnA watcher : watchers) {
      assertEquals(3, watcher.getVal());
      watcher.reset();
    }

    schedule.execute();
    for (OOPWatcherOnA watcher : watchers) {
      assertEquals(4, watcher.getVal());
      watcher.reset();
    }
  }

  @Test
  // @Watch with query and two watchees -- one
  // that satisfies the query, and one that doesn't
  public void testCacheWithQuery() {
    System.out.println("Test Cache With Query");

    WatcherWithQuery watcher1 = new WatcherWithQuery();
    WatcherWithQuery watcher2 = new WatcherWithQuery();
    Schedule schedule = new Schedule();
    Context<Object> context = setupContext(schedule, watcher1, watcher2);

    WatcheeChildA watchee = new WatcheeChildA();
    CallBackAction action = new CallBackAction(watchee, "aIncrVal");
    schedule.schedule(ScheduleParameters.createRepeating(1, 1), action);

    watcher1.reset();
    watcher2.reset();
    schedule.execute();
    assertEquals(-1, watcher1.getVal());
    assertEquals(-1, watcher2.getVal());

    // query is linked 'network' so will trigger if
    // there is a link
    NetworkBuilder<Object> builder = new NetworkBuilder<Object>("network", context, true);
    Network<Object> net = builder.buildNetwork();
    net.addEdge(watcher1, watchee);

    watcher1.reset();
    watcher2.reset();
    schedule.execute();
    assertEquals(3, watcher1.getVal());
    assertEquals(-1, watcher2.getVal());

    net.addEdge(watcher2, watchee);

    watcher1.reset();
    watcher2.reset();
    schedule.execute();
    assertEquals(4, watcher1.getVal());
    assertEquals(4, watcher2.getVal());

    net.removeEdge(net.getEdge(watcher1, watchee));

    watcher1.reset();
    watcher2.reset();
    schedule.execute();
    assertEquals(-1, watcher1.getVal());
    assertEquals(5, watcher2.getVal());

    net.removeEdge(net.getEdge(watcher2, watchee));

    watcher1.reset();
    watcher2.reset();
    schedule.execute();
    assertEquals(-1, watcher1.getVal());
    assertEquals(-1, watcher2.getVal());
  }
}
