package repast.simphony.visualization.visualization2D.style;

import java.awt.Paint;

import repast.simphony.valueLayer.ValueLayer;

/**
 * Interface for classes that implement visualization styles for ValueLayers.
 * 
 * @see repast.simphony.valueLayer.ValueLayer
 * @author Nick Collier
 * @author Eric Tatara
 * 
 *  @deprecated replaced by ogl 2D
 * 
 */
public interface ValueLayerStyle {

  /**
   * Gets the red component of the color for the specified coordinate.
   * 
   * @param coordinates
   *          the coordinate to get the color for
   * @return the red component of the color for the specified coordinate.
   */
  @Deprecated
  int getRed(double... coordinates);

  /**
   * Gets the green component of the color for the specified coordinate.
   * 
   * @param coordinates
   *          the coordinate to get the color for
   * @return the green component of the color for the specified coordinate.
   */
  @Deprecated
  int getGreen(double... coordinates);

  /**
   * Gets the blue component of the color for the specified coordinate.
   * 
   * @param coordinates
   *          the coordinate to get the color for
   * @return the blue component of the color for the specified coordinate.
   */
  @Deprecated
  int getBlue(double... coordinates);

  /**
   * 
   * @param coordinates
   *          the coordinate to get the color for
   * @return the paint for the specified coordinates
   */
  public Paint getPaint(double... coordinates);

  /**
   * Gets the size of one size of the cell value layer cell. The units are those
   * appropriate to the display -- 2D or 3D.
   * 
   * @return the width of the cell.
   */
  float getCellSize();

  /**
   * Adds a value layer to this ValueLayerStyle.
   * 
   * @param layer
   *          the layer to add
   */
  void addValueLayer(ValueLayer layer);
}