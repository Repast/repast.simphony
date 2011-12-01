/**
 * 
 */
package repast.simphony.chart2;

/**
 * Interface for classes that convert data for use in charts.
 * 
 * @author Nick Collier
 */
public interface DataConverter {

  /**
   * Converts the obj into a double.
   * 
   * @param obj
   * 
   * @return
   */
  double convert(Object obj);;
}
