/**
 * 
 */
package repast.simphony.ui.probe;


/**
 * Test object to bind as Probe.
 * 
 * @author Nick Collier
 */
public class SampleObject {
  
  
  @ProbeID
  public String id() {
    return "My Object";
  }
  
  private int intVal = 3;
  private String code = "code";
  private int foo = 10;
  
  @ProbedProperty(usageName="intVal", displayName="Integer Value", 
      converter="repast.simphony.parameter.StringConverterFactory$IntConverter")
  public int getIntVal() {
    return intVal;
  }
  
  @ProbedProperty(usageName="intVal")
  public void setIntVal(int val) {
    this.intVal = val;
  }
  
  @ProbedProperty(usageName="code", displayName="Code")
  public String getCode() {
    return code;
  }
  
  @ProbedProperty(usageName="code")
  public void setCode(String val) {
    this.code = val;
  }
  
  public int val() {
    return intVal;
  }
  
  public int foo() {
    return foo;
  }
  
  public void bar(int i) {
    foo = i;
  }
  
  
  
  
}
