package repast.simphony.util;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 *         Date: Nov 6, 2008 12:46:35 PM
 */
public class RangeComboTest extends TestCase {

  public void testCombo() {
    List<int[]> expected = new ArrayList<int[]>();

    for (int i = 7; i <= 11; i++) {
      for (int j = 3; j <= 5; j++) {
        for (int k = 1; k <= 4; k++) {
          expected.add(new int[]{k, j, i});
        }
      }
    }


    RangeCombination.RangeComboBuilder builder = new RangeCombination.RangeComboBuilder();
    builder.addRange(1, 4);
    builder.addRange(3, 5);
    builder.addRange(7, 11);

    RangeCombination combo = builder.build();

    int[] vals = new int[combo.numRanges()];


    int i = 0;
    while (combo.hasNext()) {
      combo.next(vals);
      int[] ex = expected.get(i);
      assertEquals(ex[0], vals[0]);
      assertEquals(ex[1], vals[1]);
      assertEquals(ex[2], vals[2]);
      //System.out.printf("[%d, %d, %d]\n", vals[0], vals[1], vals[2]);
      i++;
    }
    assertEquals(expected.size(), i);
  }
}
