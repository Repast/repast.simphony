package repast.simphony.valueLayer;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the continuous value layer.
 *
 * @author Eric Tatara
 */
public class ContinuousValueLayerTest extends TestCase {

  public void testDefaultVal() {
    ContinuousValueLayer vl = new ContinuousValueLayer("value", true, 3, 3);
    for (int x = 0; x < vl.getDimensions().getWidth(); x++) {
      for (int y = 0; y < vl.getDimensions().getHeight(); y++) {
        assertEquals(0.0, vl.get(x, y));
      }
    }

    double defVal = 3.143;
    vl = new ContinuousValueLayer("value", defVal, true, 3, 3);
    for (int x = 0; x < vl.getDimensions().getWidth(); x++) {
      for (int y = 0; y < vl.getDimensions().getHeight(); y++) {
        assertEquals(defVal, vl.get(x, y));
      }
    }
  }

  public void testVals2() {
    ContinuousValueLayer vl = new ContinuousValueLayer("value", true, 3.5, 3);

    vl.set(1.0, 0.001, 0.235);
    vl.set(2.3, 2.250, 0.1123);
    vl.set(3.0, 2.9385, 2.113);
    vl.set(4.9, 1.3340, 0);

    assertEquals(1.0, vl.get(0.001, 0.235));
    assertEquals(2.3, vl.get(2.250, 0.1123));
    assertEquals(3.0, vl.get(2.9385, 2.113));
    assertEquals(4.9, vl.get(1.3340, 0));
  }

  public void testVals() {
    ContinuousValueLayer vl = new ContinuousValueLayer("value", true, 20, 10, 3, 4);
    double i = 0;
    for (int x = 0; x < vl.getDimensions().getWidth(); x++) {
      for (int y = 0; y < vl.getDimensions().getHeight(); y++) {
        for (int z = 0; z < vl.getDimensions().getDepth(); z++) {
          for (int n = 0; n < vl.getDimensions().getDimension(3); n++) {
            vl.set(i++, x, y, z, n);
          }
        }
      }
    }

    i = 0;
    for (int x = 0; x < vl.getDimensions().getWidth(); x++) {
      for (int y = 0; y < vl.getDimensions().getHeight(); y++) {
        for (int z = 0; z < vl.getDimensions().getDepth(); z++) {
          for (int n = 0; n < vl.getDimensions().getDimension(3); n++) {
            assertEquals(i++, vl.get(x, y, z, n));
          }
        }
      }
    }
  }


  public static junit.framework.Test suite() {
    return new TestSuite(ContinuousValueLayerTest.class);
  }
}
