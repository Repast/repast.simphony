/**
 * 
 */
package repast.simphony.systemdynamics.sheet;

import static org.junit.Assert.*;

import org.junit.Test;

import repast.simphony.systemdynamics.sheets.FunctionManager;

/**
 * @author Nick Collier
 */
public class FunctionTests {
  
  @Test
  public void lookupTest() {
    FunctionManager manager = FunctionManager.getInstance();
    assertEquals("(A)", manager.getFunctionPattern("ABS"));
    assertEquals("(X, T)", manager.getFunctionPattern("DELAY1"));
  }
}
