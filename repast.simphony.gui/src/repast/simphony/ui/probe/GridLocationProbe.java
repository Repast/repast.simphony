package repast.simphony.ui.probe;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


/**
 * ProbeableBean that for an objects location in a grid.
 *
 * @author Nick Collier
 */
public class GridLocationProbe extends ProbeableBean {

  private Object obj;
  private Grid grid;

  public GridLocationProbe(Object obj, Grid grid) {
    this.obj = obj;
    this.grid = grid;
  }

  /**
   * Gets the objects location as a String.
   *
   * @return the objects location as a String.
   */
  public String getLocation() {
    GridPoint pt = grid.getLocation(obj);
    if (pt != null) {
      String val = pt.toString();
      return val.substring(1, val.toString().length() - 1);
    }

    return "N/A";
  }
}
