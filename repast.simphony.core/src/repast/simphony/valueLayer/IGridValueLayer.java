/**
 * 
 */
package repast.simphony.valueLayer;

/**
 * Interface for grid style value layers.
 * 
 * @author Nick Collier
 */
public interface IGridValueLayer extends ValueLayer {
  
  /**
   * Sets the specified cell to the specified value.
   * 
   * @param value
   *          the new value of the cell
   * @param coordinate
   *          the coordinate of the cell whose value we want to set
   */
  void set(double value, int... coordinate);

}
