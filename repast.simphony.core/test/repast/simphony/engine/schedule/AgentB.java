/**
 * 
 */
package repast.simphony.engine.schedule;

import java.util.List;

/**
 * @author Nick Collier
 */
public class AgentB extends AgentA {

  public AgentB(List<Result> results, ISchedule schedule) {
    super(results, schedule);
  }

  /* (non-Javadoc)
   * @see repast.simphony.engine.schedule.AgentA#method1()
   */
  @Override
  @ScheduledMethod(start = 1.25, interval = 2.0)
  public void method1() {
    results.add(new Result(MethodName.M1B, schedule.getTickCount()));
   
  }
}
