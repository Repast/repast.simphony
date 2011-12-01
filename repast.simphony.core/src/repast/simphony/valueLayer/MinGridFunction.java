/**
 * 
 */
package repast.simphony.valueLayer;


/**
 * GridFunction that calculates the minimum value(s) from
 * those passed in.
 * 
 * @author Nick Collier
 */
public class MinGridFunction extends AbstractGridFunction {
  
  double minVal = Double.POSITIVE_INFINITY;
  
  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.GridFunction#apply(double, int[])
   */
  public void apply(double gridValue, int... location) {
    if (gridValue < minVal) {
      cells.clear();
      cells.add(new GridCell(gridValue, location));
      minVal = gridValue;
    } else if (gridValue == minVal) {
      cells.add(new GridCell(gridValue, location));
    }
  }

  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.AbstractGridFunction#doClearResults()
   */
  @Override
  protected void doReset() {
    minVal = Double.POSITIVE_INFINITY;
  }
  
  
}
