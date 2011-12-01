/**
 * 
 */
package repast.simphony.data2;

/**
 * Enum specifying textual DataSink format type.
 * 
 * @author Nick Collier
 */
public enum FormatType {
  
  TABULAR() {
    public String toString() {
      return "Tabular";
    }
  },
  
  LINE() {
    public String toString() {
      return "Line";
    }
  }

}
