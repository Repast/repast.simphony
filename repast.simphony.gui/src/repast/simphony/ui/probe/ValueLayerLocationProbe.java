package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * ProbeableBean for an objects location in a value layer.
 * 
 * @author Eric Tatara
 */
public class ValueLayerLocationProbe implements LocationProbe {

  private Object obj;

  public ValueLayerLocationProbe(Object obj) {
    this.obj = obj;
  }

  /**
   * Gets the property descriptor for the location property of this probe.
   * 
   * @return
   * @throws IntrospectionException
   */
  public PropertyDescriptor getLocationDescriptor() throws IntrospectionException {
    PropertyDescriptor pd = new PropertyDescriptor("location", this.getClass(), "getLocation", null);
    pd.setDisplayName("Location");
    return pd;
  }

  /**
   * Gets the objects location as a String.
   * 
   * @return the objects location as a String.
   */
  public String getLocation() {
    String loc = "" + ((ValueLayerProbeObject2D) obj).getLoc()[0] + ", "
        + ((ValueLayerProbeObject2D) obj).getLoc()[1];

    return loc;
  }
}
