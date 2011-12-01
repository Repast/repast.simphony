/**
 * 
 */
package repast.simphony.valueLayer;

import repast.simphony.space.grid.GridPoint;

/**
 * Encapsulates a grid location and value.
 * 
 * @author Nick Collier
 */
public class GridCell {
  
  private double value;
  private GridPoint location;
  
  public GridCell(double value, int... location) {
    this.value = value;
    this.location = new GridPoint(location);
  }

  /**
   * @return the value
   */
  public double getValue() {
    return value;
  }

  /**
   * @return the location
   */
  public GridPoint getLocation() {
    return location;
  }
}
