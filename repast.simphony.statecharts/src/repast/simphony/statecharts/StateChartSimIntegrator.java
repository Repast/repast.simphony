/**
 * 
 */
package repast.simphony.statecharts;

/**
 * Interface for classes that can integrate a statechart into
 * the larger simulation.
 *  
 * @author Nick Collier
 */
public interface StateChartSimIntegrator {
  
  /**
   * Integrates the specified StateChart with the larger
   * simulation.  
   * 
   * @param chart
   * 
   * @return true if the integration worked, otherwise false.
   */
  boolean integrate(StateChart<?> chart);
  
  /**
   * Resets this StateChartSimIntegrator.
   */
  void reset();

}
