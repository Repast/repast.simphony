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
  
  @ProbeProperty(usageName="intVal", displayName="Integer Value", 
      converter="repast.simphony.parameter.StringConverterFactory$IntConverter")
  public int getIntVal() {
    return intVal;
  }
  
  @ProbeProperty(usageName="intVal")
  public void setIntVal(int val) {
    this.intVal = val;
  }
  
  @ProbeProperty(usageName="code", displayName="Code")
  public String getCode() {
    return "code";
  }
  
  @ProbeProperty(usageName="code")
  public void setCode(String val) {
    
  }
}
