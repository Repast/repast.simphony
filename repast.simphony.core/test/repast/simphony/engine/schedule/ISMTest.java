package repast.simphony.engine.schedule;

/**
 * Interface with ScheduledMethods to test.
 * 
 * @author Nick Collier
 */
public interface ISMTest {
  
  @ScheduledMethod(start=1, interval = 1)
  void method1();
  
  @ScheduledMethod(start=1.5, interval = 1)
  void method2();

}
