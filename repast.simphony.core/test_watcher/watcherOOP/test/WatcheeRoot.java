/**
 * 
 */
package watcherOOP.test;

/**
 * A example watchee root class.
 * 
 * @author Nick Collier
 */
public abstract class WatcheeRoot {
  
  protected int val = 1;
  protected double dVal = 1;
  
  public int getVal() {
    return val;
  }
  
  public void setVal(int val) {
    this.val = val;
  }
  
  public void incrVal() {
    val++;
    dVal++;
  }
  
  public double getdVal() {
    return dVal;
  }
  
  public void setdVal(double dVal) {
    this.dVal = dVal;
  }
}
