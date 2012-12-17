/**
 * 
 */
package my.statechart;

/**
 * Agent class to get test generated code to compile.
 * 
 * @author Nick Collier
 */
public class Agent {
  
  private int x = 0;
  
  public void right() {}
  public void left() {}
  public void run() {}
  
  public void up(int val) {}
  public void up() {}
  
  public void end() {}
  
  public int getX() {
    return x;
  }
  
  public void setX(int val) {
    x = val;
  }
  
  public double getProbability() {
    return 3.14;
  }
  
  public String getMessage() {
    return "my message";
  }

}
