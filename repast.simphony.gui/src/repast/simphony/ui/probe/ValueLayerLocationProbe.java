package repast.simphony.ui.probe;


/**
 * ProbeableBean for an objects location in a value layer.
 *
 * @author Eric Tatara
 */
public class ValueLayerLocationProbe extends ProbeableBean {

  private Object obj;
  

  public ValueLayerLocationProbe(Object obj) {
    this.obj = obj;
  }

  /**
   * Gets the objects location as a String.
   *
   * @return the objects location as a String.
   */
  public String getLocation() {
    String loc = "" + ((ValueLayerProbeObject2D)obj).getLoc()[0] +
                 ", " + ((ValueLayerProbeObject2D)obj).getLoc()[1];
  	
  	return loc;
  }
}
