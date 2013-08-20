package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


/**
 * Probe of an objects location in a grid.
 *
 * @author Nick Collier
 */
public class GridLocationProbe {

  private Grid<?> grid;
  private Object obj;

  public GridLocationProbe(Object obj, Grid<?> grid) {
    this.obj = obj;
    this.grid = grid;
  }
  
  /**
   * Gets the property descriptor for the location property of this probe.
   * 
   * @return
   * @throws IntrospectionException
   */
  public PropertyDescriptor getLocationDescriptor() throws IntrospectionException {
    PropertyDescriptor pd = new PropertyDescriptor("location", this.getClass(), "getLocation", null);
    pd.setDisplayName(grid.getName() + " Location");
    return pd;
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
