/**
 * 
 */
package repast.simphony.engine.schedule;

import java.util.List;

/**
 * @author Nick Collier
 */
public class AgentA {
  
  protected List<Result> results;
  protected ISchedule schedule;
  
  public AgentA(List<Result> results, ISchedule schedule) {
    this.results = results;
    this.schedule = schedule;
  }
  
  @ScheduledMethod(start = 1.0, interval = 1.0)
  public void method1() {
    results.add(new Result(MethodName.M1, schedule.getTickCount()));
  }
  
  @ScheduledMethod(start=1.5, interval = 2.0, pick = 3)
  public void method2() {
    results.add(new Result(MethodName.M2, schedule.getTickCount()));
  }
  
  @ScheduledMethod(start=2.5)
  public void method3() {
    results.add(new Result(MethodName.M3, schedule.getTickCount()));
  }
}
