package repast.simphony.visualizationOGL2D;

import java.awt.Color;

import repast.simphony.valueLayer.ValueLayer;

/**
 * Interface for classes that implement visualization styles for 
 * 2D ogl ValueLayers.
 * 
 * @see repast.simphony.valueLayer.ValueLayer
 * @author Nick Collier
 * 
 */
public interface ValueLayerStyleOGL {
  
  enum Shading {FLAT, SMOOTH}

  /**
   * Gets the color at the specified coordinates.
   * 
   * @param coordinates
   *          the coordinate to get the color for
   * @return the paint for the specified coordinates
   */
   Color getColor(double... coordinates);

  /**
   * Gets the size of one size of the cell value layer cell. The units are those
   * appropriate to the display -- 2D or 3D.
   * 
   * @return the width of the cell.
   */
  float getCellSize();

  /**
   * Initializes this style with the specified ValueLayer.
   * 
   * @param layer
   *          the layer to initialize with
   */
  void init(ValueLayer layer);
}