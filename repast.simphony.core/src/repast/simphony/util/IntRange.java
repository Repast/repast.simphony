package repast.simphony.util;

import java.util.NoSuchElementException;

/**
 * @author Nick Collier
 *         Date: Nov 6, 2008 12:06:01 PM
 */
public class IntRange {

  private int min, max, current;
  private int step;


  public IntRange(int min, int max) {
    this(min, max, 1);
  }

  public IntRange(int min, int max, int step) {
    this.min = min;
    this.max = max;
    this.step = step;
    current = this.min;
  }

  /**
   * Gets the next int in the range
   *
   * @return the next int in the range
   * @throws NoSuchElementException if the next int is out of range.
   */
  public void next() {
    if (current > max) {
      throw new NoSuchElementException("Next number is out of range");
    }

    current += step;
  }

  public int getValue() {
    return current;
  }

  public void reset() {
    current = min;
  }

  public boolean hasNext() {
    return current + step <= max;
  }

}
