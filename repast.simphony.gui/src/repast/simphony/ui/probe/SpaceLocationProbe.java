package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

/**
 * Probe of an objects location in a continuous space.
 * 
 * @author Nick Collier
 */
public class SpaceLocationProbe {

  private ContinuousSpace<?> space;
  private Object obj;

  public SpaceLocationProbe(Object obj, ContinuousSpace<?> space) {
    this.obj = obj;
    this.space = space;
  }
  
  /**
   * Gets the property descriptor for the location property of this probe.
   * 
   * @return
   * @throws IntrospectionException
   */
  public PropertyDescriptor getLocationDescriptor() throws IntrospectionException {
    PropertyDescriptor pd = new PropertyDescriptor("location", this.getClass(), "getLocation", null);
    pd.setDisplayName(space.getName() + " Location");
    return pd;
  }

  /**
   * Gets the objects location as a String.
   * 
   * @return the objects location as a String.
   */
  public String getLocation() {
    NdPoint pt = space.getLocation(obj);
    if (pt == null) {
      return "N/A";
    } else {
      String val = "";
      int dims = pt.dimensionCount();
      int dimsMinusOne = dims - 1;
      for (int i = 0; i < dims; i++) {
        val += Utils.getNumberFormatInstance().format(pt.getCoord(i));
        if (i < dimsMinusOne) {
          val += ", ";
        }
      }
      return val;
    }
  }
}