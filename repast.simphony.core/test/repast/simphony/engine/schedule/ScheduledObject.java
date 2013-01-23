package repast.simphony.engine.schedule;

import java.util.ArrayList;
import java.util.List;


/**
 * An object with annotated methods used to test scheduling via annotation.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class ScheduledObject  {
  
  List<Result> results = new ArrayList<Result>();
  ISchedule schedule;

  public ScheduledObject(ISchedule schedule) {
    this.schedule = schedule;
  }

  @ScheduledMethod(start = 3.0)
  public void startOnly() {
    results.add(new Result(MethodName.START, schedule.getTickCount()));
  }

  @ScheduledMethod(start = 3.5)
  public void startOnlyWithParams(MethodName name) {
    results.add(new Result(name, schedule.getTickCount()));
  }

  @ScheduledMethod
  public void scheduledUserParams(MethodName name, String count) {
    results.add(new Result(name, Double.parseDouble(count)));
  }


  @ScheduledMethod(start = 4.0, interval = 2.0)
  public void interval() {
    results.add(new Result(MethodName.INTERVAL, schedule.getTickCount()));
  }

  @ScheduledMethod(start = 8.0, interval = 2.0, priority = Double.NEGATIVE_INFINITY)
  public void priority() {
    results.add(new Result(MethodName.PRIORITY, schedule.getTickCount()));
  }

  @ScheduledMethod(start = ScheduledMethod.END)
  public void end() {
    results.add(new Result(MethodName.END, Double.POSITIVE_INFINITY));
  }
}
