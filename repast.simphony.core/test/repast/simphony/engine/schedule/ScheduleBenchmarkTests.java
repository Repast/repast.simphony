/**
 * 
 */
package repast.simphony.engine.schedule;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.random.RandomHelper;

/**
 * @author Nick Collier
 */
public class ScheduleBenchmarkTests {
  
  private static class AnAction implements IAction {
    public void execute() {}
  }
  
  private static class AgentAction implements IAction {
    AgentA agent;
    
    public AgentAction(ISchedule schedule, List<Result> results) {
      agent = new AgentA(results, schedule);
    }
    
    public void execute() {
      agent.method1();
    }
  }
  
  private ISchedule schedule;
  private List<Result> results;
  
  @Before
  public void setup() {
    schedule = new Schedule();
    results = new ArrayList<Result>();
  }
  
  @Test
  public void testQueue() {
    long start = System.currentTimeMillis();
    ActionQueue queue = new ActionQueue();
    for (int i = 0; i < 1000000; i++) {
      ScheduleParameters params = ScheduleParameters.createOneTime(RandomHelper.nextDoubleFromTo(1, 4));
      queue.toss(new DefaultAction(params, new AnAction(), i));
    }
    long end = System.currentTimeMillis();
    System.out.printf("Toss time: %f%n", (end - start) / 1000d);
    
    end = start;
    // force a sort
    ISchedulableAction action = queue.popMin();
    end = System.currentTimeMillis();
    System.out.printf("Sort Time: %f%n", (end - start) / 1000d);
    // use the action to avoid it being optimized away
    System.out.println(action.getNextTime());
    System.out.println(queue.size());
  }
  
  @Test
  public void addAtSameTimeTest() {
    ScheduleParameters params = ScheduleParameters.createOneTime(1);
    schedule.schedule(params, new AgentAction(schedule, results));
    schedule.schedule(params, new IAction() {
      public void execute() {
        ScheduleParameters params = ScheduleParameters.createOneTime(1);
        schedule.schedule(params, new AgentAction(schedule, results));
      }
    });
    
    schedule.execute();
  }
  
  @Test
  // ~86.839 seconds
  public void simpleScheduleSameTimeTest() {
    int count = 500000;
    long start = System.currentTimeMillis();
    for (int i = 0; i < count; i++) {
      schedule.schedule(ScheduleParameters.createRepeating(1, 1), new AgentAction(schedule, results));
    }
    
    long end = System.currentTimeMillis();
    System.out.printf("Setup time: %f seconds%n", (end - start) / 1000.0);
    
    start = end;
    schedule.execute();
    end = System.currentTimeMillis();
    System.out.printf("First Execute time: %f seconds%n", (end - start) / 1000.0);
    start = end;
    assertEquals(count, results.size());
    
    results.clear();
    schedule.execute();
    end = System.currentTimeMillis();
    System.out.printf("Second Execute time: %f seconds%n", (end - start) / 1000.0);
    start = end;
    assertEquals(count, results.size());
  }
}
