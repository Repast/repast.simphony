package repast.simphony.visualization.visualization3D.style;

import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;

/**
 * Interface for classes that implement 3D visualization styles for ValueLayers.
 * 
 * @see repast.simphony.valueLayer.ValueLayer
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ValueLayerStyle3D extends ValueLayerStyle {

  /**
   * Gets the Y (height) value for the specified coordinates.
   * 
   * @param coordinates
   *          the coordinates to get the Y value for.
   * @return the Y (height) value for the specified coordinates.
   */
  public float getY(double... coordinates);

}
