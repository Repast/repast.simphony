package repast.simphony.valueLayer;

import repast.simphony.space.Dimensions;

/**
 * Interface for classes that produce some numeric value when given spatial
 * coordinates. For example, a grid value layer may have a value in each grid
 * cell and would return the value of the cell at the specified coordinates.
 * Alternatively, a function type ValueLayer would take the coordinates and
 * return some value based on a function.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ValueLayer {

  /**
   * Gets the name of this ValueLayer.
   * 
   * @return the name of this ValueLayer.
   */
  String getName();

  /**
   * Gets a value given the specified coordinates.
   * 
   * @param coordinates
   *          the coordinates used to return the value.
   * 
   * @return a value given the specified coordinates.
   */
  double get(double... coordinates);

  /**
   * Gets the dimensions of this ValueLayer.
   * 
   * @return the dimensions of this ValueLayer.
   */
  Dimensions getDimensions();
}
