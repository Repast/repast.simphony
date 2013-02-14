/**
 * 
 */
package repast.simphony.ui.probe;


/**
 * Test object to bind as Probe.
 * 
 * @author Nick Collier
 */
public class SampleObject2 {
  
  @ProbedProperty(usageName="foo", displayName="My Foo")
  private long foo = 3;
  
  public String id() {
    return "My Object";
  }
  
  private int intVal = 3;
  
 
  public int getIntVal() {
    return intVal;
  }
  
  public void setIntVal(int val) {
    this.intVal = val;
  }
  
  public String getCode() {
    return "code";
  }
  
  public void setCode(String val) {
    
  }
}
