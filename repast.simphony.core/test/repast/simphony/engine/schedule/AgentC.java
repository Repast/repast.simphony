/**
 * 
 */
package repast.simphony.engine.schedule;

import java.util.List;

/**
 * @author Nick Collier
 */
public class AgentC implements ISMTest {
  
  protected List<Result> results;
  protected ISchedule schedule;
  
  public AgentC(List<Result> results, ISchedule schedule) {
    this.results = results;
    this.schedule = schedule;
  }

  // changes the start time of method 1
  @ScheduledMethod(start = 1.25, interval = 1)
  public void method1() {
    results.add(new Result(MethodName.M1C, schedule.getTickCount()));
  }

  
  // implements method2 
  public void method2() {
    results.add(new Result(MethodName.M2C, schedule.getTickCount()));
  }
}
