package repast.simphony.engine.watcher;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class WatcherPickTests extends TestCase {

  private Schedule schedule;
  private Generator generator;

  Context<Object> context;

  public WatcherPickTests(String string) {
    super(string);
  }

  public WatcherPickTests() {
  }

  public void setUp() {
    RunState.init();
    WatcherTrigger.getInstance().clearNotifiers();
    schedule = new Schedule();
    WatchAnnotationReader reader = new WatchAnnotationReader();
    try {
      reader.processClass(PickTestAgent.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    context = new DefaultContext<Object>();
    context.addContextListener(reader.getContextListener(schedule));
    generator = new Generator();
    context.add(generator);

    IAction action = new IAction() {
      public void execute() {
        generator.run();
      }
    };

    schedule.schedule(ScheduleParameters.createRepeating(1, 1), action);
  }

  public void testLessThanPick() {
    Counter counter = new Counter();
    for (int i = 0; i < 8; i++) {
      context.add(new PickTestAgent(counter));
    }

    schedule.execute();
    assertEquals(8, counter.getCountA());
    assertEquals(0, counter.getCountB());

    schedule.execute();
    assertEquals(16, counter.getCountA());
    // should now be 8 because the first schedule
    // execute should trigger the watch which
    // then schedule the counter update method for
    // the next tick.
    assertEquals(8, counter.getCountB());
  }

  public void testGreaterThanPick() {
    Counter counter = new Counter();
    for (int i = 0; i < 40; i++) {
      context.add(new PickTestAgent(counter));
    }

    schedule.execute();
    assertEquals(10, counter.getCountA());
    assertEquals(0, counter.getCountB());

    schedule.execute();
    assertEquals(20, counter.getCountA());
    // should now be 10 because the first schedule
    // execute should trigger the watch which
    // then schedule the counter update method for
    // the next tick.
    assertEquals(10, counter.getCountB());
  }

  public void testEqualsPick() {
    Counter counter = new Counter();
    for (int i = 0; i < 10; i++) {
      context.add(new PickTestAgent(counter));
    }

    schedule.execute();
    assertEquals(10, counter.getCountA());
    assertEquals(0, counter.getCountB());

    schedule.execute();
    assertEquals(20, counter.getCountA());
    // should now be 10 because the first schedule
    // execute should trigger the watch which
    // then schedule the counter update method for
    // the next tick.
    assertEquals(10, counter.getCountB());
  }

  public void testRemoveWithPick() {
    Counter counter = new Counter();
    List<PickTestAgent> agents = new ArrayList<PickTestAgent>();
    for (int i = 0; i < 12; i++) {
      PickTestAgent agent = new PickTestAgent(counter);
      context.add(agent);
      agents.add(agent);
    }

    schedule.execute();
    assertEquals(10, counter.getCountA());
    assertEquals(0, counter.getCountB());

    schedule.execute();
    assertEquals(20, counter.getCountA());
    // should now be 10 because the first schedule
    // execute should trigger the watch which
    // then schedule the counter update method for
    // the next tick.
    assertEquals(10, counter.getCountB());

    context.remove(agents.get(0));
    context.remove(agents.get(1));
    context.remove(agents.get(2));

    schedule.execute();
    schedule.execute();
    assertEquals(38, counter.getCountA());
    // 29 because the first execute increments by 10 (not 9,
    // because 10 were scheduled before 3 removed) and the
    // next then increments by 9
    assertEquals(29, counter.getCountB());
  }


  public static junit.framework.Test suite() {
    TestSuite suite = new TestSuite(WatcherPickTests.class);
    // TestSuite suite = new TestSuite();
    // suite.addTest(new WatcherQueryTests("testWithinNghQuery"));
    return suite;
  }
}