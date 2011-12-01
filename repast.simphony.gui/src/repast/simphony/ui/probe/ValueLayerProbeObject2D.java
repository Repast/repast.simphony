package repast.simphony.ui.probe;

import java.util.Arrays;

import repast.simphony.valueLayer.ValueLayer;

/**
 * Probe object for value layers
 * 
 * @author Eric Tatara
 * 
 */
public class ValueLayerProbeObject2D {
  
  private double[] loc;
  private String layerName;
  private ValueLayer layer;
  
  private int hashCode = 17;

  public ValueLayerProbeObject2D(double[] loc, ValueLayer layer) {
    this.loc = loc;
    this.layerName = layer.getName();
    this.layer = layer;
    
    hashCode = 31 * hashCode + layer.hashCode();
    hashCode = 31 * hashCode + Arrays.hashCode(loc);
  }
  
  public String getLayerName() {
    return layerName;
  }

  public double[] getLoc() {
    double[] tmp = new double[loc.length];
    System.arraycopy(loc, 0, tmp, 0, loc.length);
    return tmp;
  }

  public double getVal() {
    return layer.get(loc);
  }
  
  public int hashCode() {
    return hashCode;
  }
  
  // overrides equals and hashCode so that multiple probes on the
  // same location are considered equal
  public boolean equals(Object obj) {
    if (obj instanceof ValueLayerProbeObject2D) {
      ValueLayerProbeObject2D other = (ValueLayerProbeObject2D)obj;
      return other.layer.equals(layer) && Arrays.equals(other.loc, loc);
    }
    return false;
  }
}
