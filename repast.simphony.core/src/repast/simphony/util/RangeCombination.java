package repast.simphony.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Nick Collier
 *         Date: Nov 6, 2008 12:13:17 PM
 */
public class RangeCombination {


  public static class RangeComboBuilder {
    List<IntRange> ranges = new ArrayList<IntRange>();

    public void addRange(int min, int max) {
      addRange(min, max, 1);
    }

    public void addRange(int min, int max, int step) {
      ranges.add(new IntRange(min, max, step));
    }

    public RangeCombination build() {
      return new RangeCombination(this.ranges);
    }
  }

  private List<IntRange> ranges = new ArrayList<IntRange>();
  private boolean done;
  private int[] current;


  public RangeCombination(List<IntRange> ranges) {
    this.ranges.addAll(ranges);
    current = new int[ranges.size()];
    done = false;
    setCurrent();
  }

  public int numRanges() {
    return ranges.size();
  }

  public void next(int[] value) {
    if (done) throw new NoSuchElementException("All combinations have been returned");

    System.arraycopy(current, 0, value, 0, current.length);

    if (ranges.get(0).hasNext()) {

      ranges.get(0).next();
      setCurrent();

    } else {

      int index = 0;
      int limit = ranges.size() - 1;
      for (int i = 1; i <= limit; i++) {
        if (hasNext(i)) {
          break;
        } else {
          if (i == limit) {
            done = true;
            break;
          } else {
            index = i;
          }
        }
      }

      if (!done) {
        ranges.get(index + 1).next();
        for (int j = 0; j <= index; j++) {
          ranges.get(j).reset();
        }
      }

      setCurrent();
    }
  }

  public boolean hasNext() {
    return !done;
  }

  private void setCurrent() {
    for (int i = 0, n = ranges.size(); i < n; i++) {
      current[i] = ranges.get(i).getValue();
    }
  }

  private boolean hasNext(int rangeIndex) {
    return ranges.get(rangeIndex).hasNext();
  }
}
