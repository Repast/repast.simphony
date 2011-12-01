/**
 * 
 */
package repast.simphony.valueLayer;

/**
 * Callback type function interface for classes that can be
 * passed to a GridValueLayer for each method.
 * 
 * @author Nick Collier
 */
public interface GridFunction {
  
  /**
   * Applies this GridFunction to the value and location.
   * 
   * @param gridValue the grid value at the location
   * @param location the value's location
   */
  void apply(double gridValue, int... location);

}
