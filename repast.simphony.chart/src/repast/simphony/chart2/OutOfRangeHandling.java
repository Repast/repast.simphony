/*CopyrightHere*/
package repast.simphony.chart2;

/**
 * An enumeration of how values that are out of range should be displayed on a
 * histogram.
 * 
 * @author Jerry Vos
 */
public enum OutOfRangeHandling {
  
  IGNORE() {
    public String toString() {
      return "Ignore";
    }
  },
  
  ADD() {
    public String toString() {
      return "Add to Min / Max Bins";
    }
  },

  DISPLAY() {
    public String toString() {
      return "Display Values in Chart Subtitle";
    }
  }
}