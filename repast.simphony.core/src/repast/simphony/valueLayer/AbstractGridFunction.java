package repast.simphony.valueLayer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGridFunction implements GridFunction {

  protected List<GridCell> cells = new ArrayList<GridCell>();

  /**
   * Resets this GridFunction so it can be used again.
   */
  public void reset() {
    cells.clear();
    doReset();
  }
  
  /**
   * Performs any subclass specific resetting.
   */
  protected abstract void doReset();
  
  /**
   * Gets the grid cell(s) with the maximum value
   * of those passed to the apply method.
   * 
   * @return the grid cell(s) with the maximum value
   * of those passed to the apply method.
   */
  public List<GridCell> getResults() {
    return cells;
  }

}