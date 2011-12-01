package repast.simphony.engine.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;

import repast.simphony.engine.environment.RunState;
import repast.simphony.random.RandomHelper;

/**
 * Unit tests for the schedule package.
 * 
 * @author Nick Collier
 */
public class ScheduleTest extends TestCase {
  
  static {
    BasicConfigurator.configure();
  }

  private ISchedule schedule;
  // when actions are executed the insert themselves in this list
  // we can then determine the order of their execution by comparing
  // their place in this list.
  private List sameTickOrderList = new ArrayList();

  @SuppressWarnings({ "unchecked", "serial" })
  class CallBackObj {

    List list = new ArrayList();
    List vals = new ArrayList();

    public CallBackObj() {
    }

    public CallBackObj(List list) {
      this.list = list;
    }

    public void run() {
      list.add(this);
    }

    public void insert(String obj) {
      vals.add(obj);
    }

    public void insert(String obj, String obj2) {
      vals.add(obj);
      vals.add(obj2);
    }

    public void insert(Object obj1, Object obj2, Object obj3) {
      vals.add(obj1);
      vals.add(obj2);
      vals.add(obj3);
    }
  }

  static boolean sameTickRun = false;
  boolean result = true;

  @SuppressWarnings("serial")
  class SameTickTestAction implements IAction {

    ISchedule schedule;
    double executedAt;
    private SameTickTestAction nextAction;

    public SameTickTestAction(ISchedule schedule) {
      this.schedule = schedule;
    }

    public void execute() {
      executedAt = schedule.getTickCount();
      ScheduleParameters params = ScheduleParameters.createOneTime(executedAt);
      nextAction = new SameTickTestAction(schedule);
      if (!sameTickRun)
        schedule.schedule(params, nextAction);
      sameTickRun = true;
    }

    public double getExecutedAt() {
      return executedAt;
    }
  }

  @SuppressWarnings({"unchecked" })
  class TestAction implements IAction, Serializable {

    private static final long serialVersionUID = 2468314384341110786L;
    
    ISchedule schedule;
    double executedAt;

    public TestAction(ISchedule schedule) {
      this.schedule = schedule;
    }

    public void execute() {
      executedAt = schedule.getTickCount();
      sameTickOrderList.add(this);
    }

    public double getExecutedAt() {
      return executedAt;
    }
  }

  @SuppressWarnings({ "serial", "unchecked" })
  class DynamicTestAction implements IAction {

    ISchedule schedule;
    double executedAt;
    boolean rescheduled = false;
    int interval;

    public DynamicTestAction(ISchedule schedule, int interval) {
      this.schedule = schedule;
      this.interval = interval;
    }

    public void execute() {
      executedAt = schedule.getTickCount();
      ScheduleParameters params = ScheduleParameters.createOneTime(schedule.getTickCount()
          + interval);
      schedule.schedule(params, this);
      sameTickOrderList.add(this);
    }

    public double getExecutedAt() {
      return executedAt;
    }
  }
  
  public static class ReproduceCheckAction implements IAction {
    
    int id;
    List<Integer> ids;
    
    public ReproduceCheckAction(int id, List<Integer> ids) {
      this.id = id;
      this.ids = ids;
    }

    public void execute() {
      ids.add(id);
    }
  }

  protected void setUp() {
    RunState.init();
    schedule = new Schedule();
    sameTickOrderList.clear();
  }

  public static junit.framework.Test suite() {
    return new TestSuite(repast.simphony.engine.schedule.ScheduleTest.class);
  }
  
  public void testRandomActionReproducible() {
    RandomHelper.setSeed(1);
    
    List<Integer> expected = new ArrayList<Integer>();
    
    for(int i = 0; i < 30; i++) {
      schedule.schedule(ScheduleParameters.createRepeating(1, 1), new ReproduceCheckAction(i, expected));
    }
    schedule.execute();
    schedule.execute();
    
    RandomHelper.setSeed(1);
    List<Integer> actual = new ArrayList<Integer>();
    schedule = new Schedule();
    
    for(int i = 0; i < 30; i++) {
      schedule.schedule(ScheduleParameters.createRepeating(1, 1), new ReproduceCheckAction(i, actual));
    }
    schedule.execute();
    schedule.execute();
    
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
    
  }

  public void testRemove() {
    TestAction action1 = new TestAction(schedule);
    ScheduleParameters params = ScheduleParameters.createRepeating(3, 1);
    ISchedulableAction action = schedule.schedule(params, action1);
    schedule.execute();
    assertEquals(3.0, action1.getExecutedAt());
    schedule.removeAction(action);
    schedule.execute();
    assertEquals(3.0, action1.getExecutedAt());
    assertEquals(3.0, schedule.getTickCount());
  }

  public void testRemove2() {
    TestAction action1 = new TestAction(schedule);
    result = true;
    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    final ISchedulableAction action = schedule.schedule(params, action1);
    schedule.schedule(ScheduleParameters.createOneTime(9), new IAction() {
      public void execute() {
        // the remove should return false
        // because shouldn't schedule a remove while executing.
        result = schedule.removeAction(action);
      }
    });

    for (int i = 0; i < 10; i++) {
      schedule.execute();
    }
    assertTrue(!result);
  }

  public void testQueue() {
    ScheduleParameters params = ScheduleParameters.createOneTime(10);
    DefaultAction action1 = new DefaultAction(params, new TestAction(schedule), 1);

    ActionQueue queue = new ActionQueue();
    queue.insert(action1);

    params = ScheduleParameters.createOneTime(2);
    DefaultAction action2 = new DefaultAction(params, new TestAction(schedule), 2);
    queue.toss(action2);

    params = ScheduleParameters.createOneTime(.01);
    DefaultAction action3 = new DefaultAction(params, new TestAction(schedule), 3);
    queue.insert(action3);

    assertTrue(queue.peekMin().equals(action3));
    assertTrue(queue.popMin().equals(action3));
    assertTrue(queue.peekMin().equals(action2));
    assertEquals(2, queue.size());

    queue.clear();
    try {
      // empty so should throw an exception
      queue.peekMin();
      assertTrue("Queue is empty so peekMin should throw exeception", false);
    } catch (NoSuchElementException ex) {
      assertTrue(true);
    }

    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());

    for (int i = 0; i < 10; i++) {
      params = ScheduleParameters.createOneTime(i);
      DefaultAction action = new DefaultAction(params, new TestAction(schedule), 4);
      queue.insert(action);
    }

    assertEquals(10, queue.size());
    for (int i = 0; i < 5; i++) {
      queue.popMin();
    }
  }

  public void testBasicAt() {
    TestAction action1 = new TestAction(schedule);
    TestAction action2 = new TestAction(schedule);
    TestAction action3 = new TestAction(schedule);
    TestAction action4 = new TestAction(schedule);

    ScheduleParameters params = ScheduleParameters.createOneTime(3);
    schedule.schedule(params, action1);
    schedule.schedule(ScheduleParameters.createRepeating(3, 1), action2);
    schedule.schedule(ScheduleParameters.createRepeating(3, 1), action3);
    schedule.schedule(ScheduleParameters.createRepeating(5, 1), action4);

    schedule.execute();

    // did they all execute?
    assertEquals(3.0, action1.getExecutedAt());
    assertEquals(3.0, action2.getExecutedAt());
    assertEquals(3.0, action3.getExecutedAt());

    // 4 didnt' execute yet
    assertEquals(0d, action4.getExecutedAt());

    // we need to execute twice beacuase actions 2 and 3 are repeat type
    // and so will execute at 4 with the first execute
    schedule.execute();
    schedule.execute();
    assertEquals(5.0, action4.getExecutedAt());
    // action1 was one timer so shouldn't have executed
    assertEquals(3.0, action1.getExecutedAt());
    // action2 was repeat by default so should have executed
    assertEquals(5.0, action2.getExecutedAt());

  }

  public void testDynamicAt() {
    int interval = 1;
    DynamicTestAction action = new DynamicTestAction(schedule, interval);

    schedule.schedule(ScheduleParameters.createOneTime(4.5), action);
    for (int i = 0; i < 100; i++) {
      schedule.execute();
      assertEquals(4.5 + i * interval, action.getExecutedAt());
      assertEquals(4.5 + i * interval, schedule.getTickCount());
    }
  }

  public void testBasicInterval() {
    TestAction action1 = new TestAction(schedule);
    TestAction action2 = new TestAction(schedule);
    TestAction action3 = new TestAction(schedule);
    TestAction action4 = new TestAction(schedule);

    ScheduleParameters params = ScheduleParameters.createRepeating(3, 1);
    schedule.schedule(params, action1);
    schedule.schedule(params, action2);
    schedule.schedule(params, action3);
    schedule.schedule(ScheduleParameters.createRepeating(5, 1), action4);

    for (int i = 0; i < 20; i++) {
      schedule.execute();
      assertEquals("i equals " + i, 3.0 + i, action1.getExecutedAt());
      assertEquals("i equals " + i, 3.0 + i, action2.getExecutedAt());
      assertEquals("i equals " + i, 3.0 + i, action3.getExecutedAt());

      if (i < 2)
        assertEquals(0d, action4.getExecutedAt());
      else
        assertEquals("i equals " + i, 5.0 + i - 2, action4.getExecutedAt());
    }
  }

  public void testBasicUserInterval() {
    TestAction action1 = new TestAction(schedule);
    TestAction action2 = new TestAction(schedule);
    TestAction action3 = new TestAction(schedule);
    TestAction action4 = new TestAction(schedule);

    ScheduleParameters params = ScheduleParameters.createRepeating(3, 2);
    schedule.schedule(params, action1);
    schedule.schedule(params, action2);

    params = ScheduleParameters.createRepeating(3, 1);
    schedule.schedule(params, action3);

    params = ScheduleParameters.createRepeating(5, .5);
    schedule.schedule(params, action4);

    schedule.execute();
    assertEquals(3.0, action1.getExecutedAt());
    assertEquals(3.0, action2.getExecutedAt());
    assertEquals(3.0, action3.getExecutedAt());
    assertEquals(0d, action4.getExecutedAt());

    schedule.execute();
    assertEquals(3.0, action1.getExecutedAt());
    assertEquals(3.0, action2.getExecutedAt());
    assertEquals(4.0, action3.getExecutedAt());
    assertEquals(0d, action4.getExecutedAt());

    schedule.execute();
    assertEquals(5.0, action1.getExecutedAt());
    assertEquals(5.0, action2.getExecutedAt());
    assertEquals(5.0, action3.getExecutedAt());
    assertEquals(5.0, action4.getExecutedAt());

    schedule.execute();
    assertEquals(5.0, action1.getExecutedAt());
    assertEquals(5.0, action2.getExecutedAt());
    assertEquals(5.0, action3.getExecutedAt());
    assertEquals(5.5, action4.getExecutedAt());

    schedule.execute();
    assertEquals(5.0, action1.getExecutedAt());
    assertEquals(5.0, action2.getExecutedAt());
    assertEquals(6.0, action3.getExecutedAt());
    assertEquals(6.0, action4.getExecutedAt());
  }

  public void testPriorityOrder() {
    TestAction action1 = new TestAction(schedule);
    TestAction action2 = new TestAction(schedule);
    TestAction action3 = new TestAction(schedule);
    TestAction action4 = new TestAction(schedule);
    TestAction action5 = new TestAction(schedule);
    TestAction action6 = new TestAction(schedule);
    TestAction action7 = new TestAction(schedule);

    // should execute last
    ScheduleParameters params = ScheduleParameters.createOneTime(3, Double.NEGATIVE_INFINITY);
    schedule.schedule(params, action1);

    // should execute first
    params = ScheduleParameters.createOneTime(3, Double.POSITIVE_INFINITY);
    schedule.schedule(params, action2);

    params = ScheduleParameters.createOneTime(3);
    schedule.schedule(params, action3);
    schedule.schedule(params, action4);

    params = ScheduleParameters.createOneTime(3, -2);
    schedule.schedule(params, action5);

    params = ScheduleParameters.createOneTime(3, 4);
    schedule.schedule(params, action6);

    params = ScheduleParameters.createOneTime(3, 100.5);
    schedule.schedule(params, action7);

    schedule.execute();

    // order should be action2, action5, action6, action7, action1
    // with action3 and action4 randomly between action1 and action2.
    assertTrue(sameTickOrderList.get(0).equals(action2));
    assertTrue(sameTickOrderList.get(sameTickOrderList.size() - 1).equals(action1));
    int index5 = sameTickOrderList.indexOf(action5);
    int index6 = sameTickOrderList.indexOf(action6);
    int index7 = sameTickOrderList.indexOf(action7);

    assertTrue(index6 < index5 && index7 < index6);

    System.out
        .println("sameTickOrderList.indexOf(action3) = " + sameTickOrderList.indexOf(action3));
    System.out
        .println("sameTickOrderList.indexOf(action4) = " + sameTickOrderList.indexOf(action4));
  }

  public void testNothingToSchedule() {
    schedule.execute();
    // if we get here without some sort of exception the we are OK
    assertTrue(true);
  }

  public void testBadCallBack() {
    Object target = new ArrayList();
    boolean passed = false;

    try {
      ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
      // non-existent method
      schedule.schedule(params, target, "foo", "bar", "bong");
    } catch (IllegalArgumentException ex) {
      passed = true;
    }

    assertTrue("Should throw IllegalArgumentException", passed);

    passed = false;
    try {
      ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
      // existing method but bad arg
      schedule.schedule(params, target, "addAll", "bar");
    } catch (IllegalArgumentException ex) {
      passed = true;
    }

  }

  public void testSimpleCallBack() {
    String param1 = "CallBack Test";
    List target = new ArrayList();

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.schedule(params, target, "add", param1);

    // this is to test that the method look up looks in parent classes
    // if there is no exception then we pass
    schedule.schedule(params, target, "hashCode");

    schedule.execute();
    assertEquals(1.0, schedule.getTickCount());
    assertEquals(param1, target.get(0));
  }

  public void testIterableCallBackIII() {
    // test shuffle
    List order = new ArrayList();
    List<CallBackObj> list = new ArrayList<CallBackObj>();

    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.scheduleIterable(params, list, "run", true);

    schedule.execute();
    CallBackObj first = (CallBackObj) order.get(0);
    CallBackObj second = (CallBackObj) order.get(1);
    CallBackObj third = (CallBackObj) order.get(2);
    CallBackObj fourth = (CallBackObj) order.get(3);
    CallBackObj fifth = (CallBackObj) order.get(4);

    schedule.execute();
    boolean passed = !first.equals(order.get(5)) || !second.equals(order.get(6))
        || !third.equals(order.get(7)) || !fourth.equals(order.get(8))
        || !fifth.equals(order.get(9));
    assertTrue(passed);
  }

  public void testIterableCallBackIV() {
    // test no shuffle
    List order = new ArrayList();
    List<CallBackObj> list = new ArrayList<CallBackObj>();

    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.scheduleIterable(params, list, "run", false);

    schedule.execute();
    CallBackObj first = (CallBackObj) order.get(0);
    CallBackObj second = (CallBackObj) order.get(1);
    CallBackObj third = (CallBackObj) order.get(2);

    schedule.execute();
    boolean passed = first.equals(order.get(3)) && second.equals(order.get(4))
        && third.equals(order.get(5));
    assertTrue(passed);
  }

  public void testIterableCallBackV() {
    // test shuffle with Set
    List order = new ArrayList();
    Set<CallBackObj> list = new HashSet<CallBackObj>();

    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.scheduleIterable(params, list, "run", true);

    schedule.execute();
    CallBackObj first = (CallBackObj) order.get(0);
    CallBackObj second = (CallBackObj) order.get(1);
    CallBackObj third = (CallBackObj) order.get(2);
    CallBackObj fourth = (CallBackObj) order.get(3);
    CallBackObj fifth = (CallBackObj) order.get(4);

    schedule.execute();
    boolean passed = !first.equals(order.get(5)) || !second.equals(order.get(6))
        || !third.equals(order.get(7)) || !fourth.equals(order.get(8))
        || !fifth.equals(order.get(9));
    assertTrue(passed);
  }

  public void testIterableCallBackVI() {
    // test no shuffle with Set
    List order = new ArrayList();
    Set<CallBackObj> list = new HashSet<CallBackObj>();

    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));
    list.add(new CallBackObj(order));

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.scheduleIterable(params, list, "run", false);

    schedule.execute();
    CallBackObj first = (CallBackObj) order.get(0);
    CallBackObj second = (CallBackObj) order.get(1);
    CallBackObj third = (CallBackObj) order.get(2);

    schedule.execute();
    boolean passed = first.equals(order.get(3)) && second.equals(order.get(4))
        && third.equals(order.get(5));
    assertTrue(passed);
  }

  public void testIterableCallBackI() {
    Set<CallBackObj> set = new HashSet<CallBackObj>();
    set.add(new CallBackObj());
    set.add(new CallBackObj());
    set.add(new CallBackObj());

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.scheduleIterable(params, set, "insert", true, "foo");
    schedule.execute();

    for (CallBackObj obj : set) {
      assertEquals("foo", obj.vals.get(0));
    }

    set.clear();
    schedule.execute();

    set.add(new CallBackObj());
    set.add(new CallBackObj());
    set.add(new CallBackObj());

    schedule.execute();

    for (CallBackObj obj : set) {
      assertEquals("foo", obj.vals.get(0));
    }
  }

  public void testIterableCallBackII() {
    List<CallBackObj> list = new ArrayList<CallBackObj>();

    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.scheduleIterable(params, list, "insert", false, "foo");

    schedule.execute();

    list.add(new CallBackObj());
    list.add(new CallBackObj());
    list.add(new CallBackObj());

    schedule.execute();

    for (CallBackObj obj : list) {
      assertEquals("foo", obj.vals.get(0));
    }
  }

  public void testCallBack() {
    CallBackObj target = new CallBackObj();
    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.schedule(params, target, "insert", "foo");

    CallBackObj target2 = new CallBackObj();
    schedule.schedule(params, target2, "insert", "foo", "bar");

    CallBackObj target3 = new CallBackObj();
    schedule.schedule(params, target3, "insert", "foo", "bar", "bong");

    schedule.execute();

    assertEquals("foo", target.vals.get(0));

    assertEquals("foo", target2.vals.get(0));
    assertEquals("bar", target2.vals.get(1));

    assertEquals("foo", target3.vals.get(0));
    assertEquals("bar", target3.vals.get(1));
    assertEquals("bong", target3.vals.get(2));
  }

  public void testScheduledMethodII() {
    ScheduledObject obj = new ScheduledObject(schedule);
    ScheduleParameters sp = ScheduleParameters.createOneTime(2);
    schedule.schedule(sp, obj, ScheduledObject.MethodName.START_PARAMS, "3.14");
    schedule.execute();
    assertEquals(3.14, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START_PARAMS, obj.results.get(0).methodName);
  }

  public void testScheduledMethod() {
    ScheduledObject obj = new ScheduledObject(schedule);
    schedule.schedule(obj);
    schedule.schedule(obj, ScheduledObject.MethodName.START_PARAMS);
    // obj has a startOnly method scheduled to start at 3.0
    schedule.execute();
    assertEquals(3.0, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START, obj.results.get(0).methodName);

    // method executes at 3.5 and an inserts the parameter above into
    // results
    schedule.execute();
    assertEquals(3.0, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START, obj.results.get(0).methodName);
    assertEquals(3.5, obj.results.get(1).tick);
    assertEquals(ScheduledObject.MethodName.START_PARAMS, obj.results.get(1).methodName);

    schedule.execute();
    assertEquals(3.0, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START, obj.results.get(0).methodName);
    assertEquals(3.5, obj.results.get(1).tick);
    assertEquals(ScheduledObject.MethodName.START_PARAMS, obj.results.get(1).methodName);

    assertEquals(4.0, obj.results.get(2).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(2).methodName);

    schedule.execute();
    assertEquals(3.0, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START, obj.results.get(0).methodName);
    assertEquals(3.5, obj.results.get(1).tick);
    assertEquals(ScheduledObject.MethodName.START_PARAMS, obj.results.get(1).methodName);

    assertEquals(4.0, obj.results.get(2).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(2).methodName);
    assertEquals(6.0, obj.results.get(3).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(3).methodName);
    assertEquals(4, obj.results.size());

    schedule.execute();
    // obj has a priority method scheduled to start at 8.0 with priority of -INF
    // and interval of 2.0
    assertEquals(3.0, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START, obj.results.get(0).methodName);
    assertEquals(3.5, obj.results.get(1).tick);
    assertEquals(ScheduledObject.MethodName.START_PARAMS, obj.results.get(1).methodName);

    assertEquals(4.0, obj.results.get(2).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(2).methodName);
    assertEquals(6.0, obj.results.get(3).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(3).methodName);
    assertEquals(8.0, obj.results.get(4).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(4).methodName);
    assertEquals(8.0, obj.results.get(5).tick);
    // comes last because priority was -INF
    assertEquals(ScheduledObject.MethodName.PRIORITY, obj.results.get(5).methodName);

    assertEquals(6, obj.results.size());

    schedule.executeEndActions();
    assertEquals(3.0, obj.results.get(0).tick);
    assertEquals(ScheduledObject.MethodName.START, obj.results.get(0).methodName);
    assertEquals(3.5, obj.results.get(1).tick);
    assertEquals(ScheduledObject.MethodName.START_PARAMS, obj.results.get(1).methodName);

    assertEquals(4.0, obj.results.get(2).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(2).methodName);
    assertEquals(6.0, obj.results.get(3).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(3).methodName);
    assertEquals(8.0, obj.results.get(4).tick);
    assertEquals(ScheduledObject.MethodName.INTERVAL, obj.results.get(4).methodName);
    assertEquals(8.0, obj.results.get(5).tick);
    // comes last because priority was -INF
    assertEquals(ScheduledObject.MethodName.PRIORITY, obj.results.get(5).methodName);

    assertEquals(ScheduledObject.MethodName.END, obj.results.get(6).methodName);
    assertEquals(Double.POSITIVE_INFINITY, obj.results.get(6).tick);

    assertEquals(7, obj.results.size());

  }

  @SuppressWarnings({ "serial", "unchecked" })
  class DurationTestAction implements IAction {

    int count = 0;

    public int getCount() {
      return count;
    }

    // I need to increase the count here so that we can test that something is
    // happening
    // in the background. But I also need the count to force a wait until the
    // duration has ended.
    //
    public void execute() {
      while (count < 1000000) {
        count++;
      }
    }

    public void reset() {
      count = 0;
    }

    @ScheduledMethod(start = 1, duration = 3)
    public void run() {
      execute();
    }
  }

  public void testDurationIAction() {
    TestAction tAction = new TestAction(schedule);
    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.schedule(params, tAction);

    // create with a duration of 3
    // the action increments a counter, each time we check after the first
    // execute, the counter should be > than the previous count.
    DurationTestAction durAction = new DurationTestAction();
    params = ScheduleParameters.createOneTime(1, ScheduleParameters.RANDOM_PRIORITY, 3);
    schedule.schedule(params, durAction);

    int count = durAction.getCount();
    schedule.execute();
    // need to pause a bit to let the background thread run
    try {
      Thread.sleep(100);
      // tests that the execution is running in the backgroud
      assertTrue(count < durAction.getCount());
      count = durAction.getCount();

      for (double i = 1; i < 20; i++) {
        assertEquals(i, tAction.executedAt);
        // need to pause a bit to let the background thread run and the count
        // increase
        Thread.sleep(100);
        if (i < 4) {
          // count should increment in the background until
          // 4 at which point it should stop
          assertTrue(count <= durAction.getCount());
        } else {
          assertTrue(count == durAction.getCount());
        }
        count = durAction.getCount();
        schedule.execute();

      }
    } catch (InterruptedException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
  }

  public void testRepeatDurationIAction() {
    TestAction tAction = new TestAction(schedule);
    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.schedule(params, tAction);

    // create with a duration of 3
    // the action increments a counter, each time we check after the first
    // execute, the counter should be > than the previous count.
    DurationTestAction durAction = new DurationTestAction();
    params = ScheduleParameters.createRepeating(1, 1, ScheduleParameters.RANDOM_PRIORITY, 3);
    schedule.schedule(params, durAction);

    int count = durAction.getCount();
    schedule.execute();
    // need to pause a bit to let the background thread run
    try {
      Thread.sleep(100);
      // tests that the execution is running in the backgroud
      assertTrue(count < durAction.getCount());
      count = durAction.getCount();

      for (double i = 1; i < 5; i++) {
        assertEquals(i, tAction.executedAt);
        // need to pause a bit to let the background thread run and the count
        // increase
        Thread.sleep(100);
        if (i < 4) {
          // count should increment in the background until
          // 4 at which point it should stop
          assertTrue(count <= durAction.getCount());
        } else {
          assertTrue(count == durAction.getCount());
        }
        count = durAction.getCount();
        schedule.execute();
      }

      durAction.reset();
      count = durAction.getCount();
      schedule.execute();
      Thread.sleep(100);
      // tests that the execution is running in the backgroud
      assertTrue(count < durAction.getCount());

      count = durAction.getCount();

      for (double i = 6; i < 8; i++) {
        assertEquals(i, tAction.executedAt);
        // need to pause a bit to let the background thread run and the count
        // increase
        Thread.sleep(100);
        if (i < 8) {
          // count should increment in the background until
          // 4 at which point it should stop
          assertTrue(count <= durAction.getCount());
        } else {
          assertTrue(count == durAction.getCount());
        }
        count = durAction.getCount();
        schedule.execute();
      }

    } catch (InterruptedException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
  }

  public void testCallbackDuration() {
    TestAction tAction = new TestAction(schedule);
    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.schedule(params, tAction);

    // create with a duration of 3
    // the action increments a counter, each time we check after the first
    // execute, the counter should be > than the previous count.
    DurationTestAction durAction = new DurationTestAction();
    params = ScheduleParameters.createOneTime(1, ScheduleParameters.RANDOM_PRIORITY, 3);
    schedule.schedule(params, durAction, "execute");

    int count = durAction.getCount();
    schedule.execute();
    // need to pause a bit to let the background thread run
    try {
      Thread.sleep(100);
      // tests that the execution is running in the backgroud
      assertTrue(count < durAction.getCount());
      count = durAction.getCount();

      for (double i = 1; i < 20; i++) {
        assertEquals(i, tAction.executedAt);
        // need to pause a bit to let the background thread run and the count
        // increase
        Thread.sleep(100);
        if (i < 4) {
          // count should increment in the background until
          // 4 at which point it should stop
          assertTrue(count <= durAction.getCount());
        } else {
          assertTrue(count == durAction.getCount());
        }
        count = durAction.getCount();
        schedule.execute();

      }
    } catch (InterruptedException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
  }

  public void testAnnotatedDuration() {
    TestAction tAction = new TestAction(schedule);
    ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
    schedule.schedule(params, tAction);

    // create with a duration of 3
    // the action increments a counter, each time we check after the first
    // execute, the counter should be > than the previous count.
    DurationTestAction durAction = new DurationTestAction();
    schedule.schedule(durAction);

    int count = durAction.getCount();
    schedule.execute();
    // need to pause a bit to let the background thread run
    try {
      Thread.sleep(100);
      // tests that the execution is running in the backgroud
      assertTrue(count < durAction.getCount());
      count = durAction.getCount();

      for (double i = 1; i < 20; i++) {
        assertEquals(i, tAction.executedAt);
        // need to pause a bit to let the background thread run and the count
        // increase
        Thread.sleep(100);
        if (i < 4) {
          // count should increment in the background until
          // 4 at which point it should stop
          assertTrue(count <= durAction.getCount());
        } else {
          assertTrue(count == durAction.getCount());
        }
        count = durAction.getCount();
        schedule.execute();

      }
    } catch (InterruptedException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
  }

  /**
   * Tests that an action that schedules another action (or itself) for the same
   * tick does not increment the tick count when executing again.
   */
  public void testScheduleSameTick() {
    IAction action = new IAction() {
      public void execute() {
        ScheduleParameters params = ScheduleParameters.createOneTime(schedule.getTickCount());
        System.out.println("executing");
        schedule.schedule(params, this);
      }
    };

    ScheduleParameters params = ScheduleParameters.createOneTime(schedule.getTickCount());
    schedule.schedule(params, action);
    assertEquals(-1.0, schedule.getTickCount());
    schedule.execute();
    assertEquals(-1.0, schedule.getTickCount());
    schedule.execute();
    assertEquals(-1.0, schedule.getTickCount());
  }

  public void testSameTickSchedule() {
    SameTickTestAction action1 = new SameTickTestAction(schedule);
    schedule.schedule(ScheduleParameters.createOneTime(1), action1);
    schedule.execute();
    assertEquals(1.0, action1.getExecutedAt());
    assertEquals(1.0, schedule.getTickCount());

    SameTickTestAction action2 = action1.nextAction;
    assertEquals(1.0, action2.getExecutedAt());
    assertEquals(1.0, schedule.getTickCount());
  }

  public void testSameTickPriority() {
    final TestAction firstAction = new TestAction(schedule);
    final TestAction lastAction = new TestAction(schedule);

    IAction action = new IAction() {
      @SuppressWarnings("unchecked")
      public void execute() {
        sameTickOrderList.add(this);
        double tick = schedule.getTickCount();
        schedule.schedule(ScheduleParameters.createOneTime(tick, Double.POSITIVE_INFINITY),
            firstAction);
        schedule.schedule(ScheduleParameters.createOneTime(tick, Double.NEGATIVE_INFINITY),
            lastAction);
      }
    };

    TestAction preAction = new TestAction(schedule);
    TestAction actionP2 = new TestAction(schedule);

    schedule.schedule(ScheduleParameters.createOneTime(2, Double.POSITIVE_INFINITY), preAction);
    schedule.schedule(ScheduleParameters.createOneTime(2, 2), action);
    schedule.schedule(ScheduleParameters.createOneTime(2, 0), actionP2);
    schedule.execute();

    // order in sameTickOrderList should be preAction, action, firstAction,
    // actionP2, lastAction
    assertEquals(preAction, sameTickOrderList.get(0));
    assertEquals(action, sameTickOrderList.get(1));
    assertEquals(firstAction, sameTickOrderList.get(2));
    assertEquals(actionP2, sameTickOrderList.get(3));
    assertEquals(lastAction, sameTickOrderList.get(4));

    assertEquals(preAction.executedAt, 2.0);
    assertEquals(firstAction.executedAt, 2.0);
    assertEquals(actionP2.executedAt, 2.0);
    assertEquals(lastAction.executedAt, 2.0);
  }

  @SuppressWarnings("serial")
  static class EndAction implements IAction {

    private int id;
    private List<Integer> list;

    public EndAction(List<Integer> list, int id) {
      this.list = list;
      this.id = id;
    }

    public void execute() {
      list.add(id);
    }

  }

  public void testEndActions() {
    List<Integer> list = new ArrayList<Integer>();
    EndAction a1 = new EndAction(list, 1);
    EndAction a2 = new EndAction(list, 2);
    EndAction a3 = new EndAction(list, 3);
    EndAction a4 = new EndAction(list, 4);

    schedule.schedule(ScheduleParameters.createAtEnd(1), a1);
    ISchedulableAction action = schedule.schedule(ScheduleParameters.createAtEnd(2), a2);
    schedule.schedule(ScheduleParameters.createAtEnd(3), a3);
    schedule.schedule(ScheduleParameters.createAtEnd(4), a4);

    schedule.executeEndActions();

    assertEquals(4, list.size());
    assertEquals(new Integer(4), list.get(0));
    assertEquals(new Integer(3), list.get(1));
    assertEquals(new Integer(2), list.get(2));
    assertEquals(new Integer(1), list.get(3));

    list.clear();
    schedule.schedule(ScheduleParameters.createAtEnd(1), a1);
    action = schedule.schedule(ScheduleParameters.createAtEnd(2), a2);
    schedule.schedule(ScheduleParameters.createAtEnd(3), a3);
    schedule.schedule(ScheduleParameters.createAtEnd(4), a4);

    schedule.removeAction(action);

    schedule.executeEndActions();
    assertEquals(3, list.size());
    assertEquals(new Integer(4), list.get(0));
    assertEquals(new Integer(3), list.get(1));
    assertEquals(new Integer(1), list.get(2));
  }

  public void testFinishing() {
    IAction action = new NonModel();
    schedule.schedule(ScheduleParameters.createRepeating(0, 1.0), action);

    // go 5 normal ticks, then switch to finishing mode, then make sure the tick
    // count doesn't
    // increment after that
    double tick = 0.0;
    int i = 0;
    while (i < 10) {
      i++;
      schedule.execute();

      assertEquals(tick, schedule.getTickCount());

      if (tick <= 5.0) {
        assertEquals(1, schedule.getActionCount());
        tick++;
      } else {
        schedule.setFinishing(true);
      }
    }
  }

  @NonModelAction
  static class NonModel implements IAction {
    private static final long serialVersionUID = 1L;

    public void execute() {
      int i = 2 + 2 + 3;
      Math.pow(i, 4);
    }
  }

  static class Model implements IAction {
    private static final long serialVersionUID = 1L;

    public void execute() {
      int i = 2 + 2 + 3;
      Math.pow(i, 4);
    }
  }

  public void testModelActionCount1() {
    schedule.schedule(ScheduleParameters.createOneTime(1.0), new NonModel());
    assertEquals(0, schedule.getModelActionCount());
    assertEquals(1, schedule.getActionCount());
  }

  public void testModelActionCount2() {
    schedule.schedule(ScheduleParameters.createOneTime(1.0), new Model());
    assertEquals(1, schedule.getModelActionCount());
    assertEquals(1, schedule.getActionCount());
  }

  public void testRemove3() {

    RemoveTestObject obj = new RemoveTestObject(schedule);
    schedule.schedule(ScheduleParameters.createOneTime(0, 1), obj, "test");
    schedule.schedule(ScheduleParameters.createOneTime(100, 0), obj, "remove");
    
    while (!obj.stop && schedule.getModelActionCount() > 0) {
      schedule.execute();
    }
    assertEquals(1000.0, schedule.getTickCount());

  }

  static class RemoveTestObject {

    ISchedule schedule;
    ISchedulableAction nexttest;
    boolean stop = false;

    public RemoveTestObject(ISchedule schedule2) {
      this.schedule = schedule2;
    }

    public void test() {
      nexttest = schedule.schedule(
          ScheduleParameters.createOneTime(schedule.getTickCount() + Math.random(), 1
              /*Math.random()*/), this, "test");
      // System.out.println("TEST");
    }

    public void remove() {
      //System.out.println("REMOVE");
      //System.out.println(schedule.getTickCount());
      //System.out.println(nexttest.getNextTime());
      //System.out.println("pre-remove: " + schedule.getModelActionCount());
      schedule.removeAction(nexttest);
     
      if (schedule.getTickCount() < 1000) {
        schedule.schedule(ScheduleParameters.createOneTime(schedule.getTickCount() + 100, 0), this,
            "remove");
        test();
      } else {
        stop = true;
      }
      
      //System.out.println("post-remove: " + schedule.getModelActionCount()); // should print 2 if
                                                          // TickCount < 1000, 0
                                                          // otherwise
                                                          // (incorrect)
    }

  }

  // public static void main(String[] args) {
  // long n = 10000000;
  // System.out.println("speed test, n=" + n);
  // Schedule sched = new Schedule();
  // @NonModelAction
  // IAction action = new NonModel();
  // sched.schedule(ScheduleParameters.createRepeating(0, 1.0), action);
  //
  // long start = System.currentTimeMillis();
  // System.out.println("start: " + start);
  // System.out.println(action.getClass().isAnnotationPresent(NonModelAction.class));
  // while (sched.getTickCount() < n && sched.getActionCount() > 0) {
  // System.out.println("action count " + sched.getActionCount());
  // System.out.println("model action count: " + sched.getModelActionCount());
  // System.out.println("tick count: " + sched.getTickCount());
  // sched.execute();
  // if (sched.getTickCount() == 5) {
  // sched.setFinishing(true);
  // }
  // }
  // long end = System.currentTimeMillis();
  // System.out.println("end: " + end);
  // System.out.println("time: " + (end - start));
  // System.out.println("avg time: " + (double) (end - start) / n);
  //
  // }
}
