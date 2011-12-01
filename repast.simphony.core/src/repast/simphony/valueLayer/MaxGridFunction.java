/**
 * 
 */
package repast.simphony.valueLayer;


/**
 * GridFunction that calculates the maxium value(s) from
 * those passed in.
 * 
 * @author Nick Collier
 */
public class MaxGridFunction extends AbstractGridFunction {
  
  double maxVal = Double.NEGATIVE_INFINITY;
  
  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.GridFunction#apply(double, int[])
   */
  public void apply(double gridValue, int... location) {
    if (gridValue > maxVal) {
      cells.clear();
      cells.add(new GridCell(gridValue, location));
      maxVal = gridValue;
    } else if (gridValue == maxVal) {
      cells.add(new GridCell(gridValue, location));
    }
  }

  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.AbstractGridFunction#doClearResults()
   */
  @Override
  protected void doReset() {
    maxVal = Double.NEGATIVE_INFINITY;
  }
  
  
}
