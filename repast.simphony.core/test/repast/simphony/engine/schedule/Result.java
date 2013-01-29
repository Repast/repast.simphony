package repast.simphony.engine.schedule;


public class Result {
  
  MethodName methodName;
  double tick;

  public Result(MethodName methodName, double tick) {
    this.methodName = methodName;
    this.tick = tick;
  }
}