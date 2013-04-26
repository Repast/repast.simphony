/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Color;
import java.awt.Font;

import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

/**
 * Style interface for ogl 2D styling.
 * 
 * @author Nick Collier
 */
public interface StyleOGL2D<T> {
  
  /**
   * Initializes this style with the ShapeFactory2D that should be used
   * to create any VSpatials produced by this style.
   * 
   * @param factory the factory to intialize this style with
   */
  void init(ShapeFactory2D factory);
  
  /**
   * Gets the VSpatial visual representation of the object. 
   * If there are no changes to the representation then the 
   * VSpatial spatial parameter should be returned.
   * 
   * @param object the object whose VSpatial representation we want
   * @param spatial the current VSpatial representation
   * @return a new VSpatial visual representation of the object or
   * the current one if there has been no change.
   */
  VSpatial getVSpatial(T object, VSpatial spatial);
  
  /**
   * Gets the color for the specified object.
   * 
   * @param object
   * @return the color for the specified object.
   */
  Color getColor(T object);
  
  /**
   * Gets the size of the border for the specified object.
   * A size of 0 means no border is drawn.
   * 
   * @param object
   * @return the size of the border for the specified object.
   */
  int getBorderSize(T object);
  
  /**
   * Gets the border color for the specified object. This
   * will be ignored if the border size is 0.
   * 
   * @param object
   * @return the border color for the specified object.
   */
  Color getBorderColor(T object);
  
  /**
   * Gets the rotation, in degrees, of the VSpatial for the specified 
   * object.
   * 
   * @param object
   * @return the rotation, in degrees, of the VSpatial for the specified 
   * object.
   */
  float getRotation(T object);
  
  /**
   * Gets the scale for the specified object.
   * 
   * @param object
   * @return the for the specified object.
   */
  float getScale(T object);
  
  
  /**
   * Gets a label for the specified object. Null and an empty String are
   * permitted values.
   * 
   * @param object the object to get the label for
   *  
   * @return a label for the specified object. Null and an empty String are
   * permitted values.
   */
  String getLabel(T object);
  
  /**
   * Gets the font for this objects label. This
   * can be null if there is no label.
   * 
   * @param object
   * 
   * @return the font for this objects label. This
   * can be null if there is no label.
   */
  Font getLabelFont(T object);
  
  /**
   * Gets an optional X offset for the label location.
   * This can be used to make small adjustments the label location. 
   * 
   * @return an optional X offset for the label placement.
   */
  float getLabelXOffset(T object);
  
  /**
   * Gets an optional Y offset for the label location.
   * This can be used to make small adjustments the label location. 
   * 
   * @return an optional Y offset for the label placement.
   */
  float getLabelYOffset(T object);
  
  /**
   * Gets the position of the label relative the VSpatial. Valid values are
   * <ul>
   * <li>Position.CENTER
   * <li>Position.NORTH
   * <li>Position.SOUTH
   * <li>Position.EAST
   * <li>Position.WEST
   * <li>Position.NORTH_EAST
   * <li>Position.NORTH_WEST
   * <li>Position.SOUTH_EAST
   * <li>Position.SOUTH_WEST
   * </ul>
   * 
   * @param object
   * @return the position of the label relative the VSpatial.
   */
  Position getLabelPosition(T object);
  
  /**
   * Gets the label color. Can return null if there is no label.
   * @param object
   * 
   * @return the label color. Can return null if there is no label.
   */
  Color getLabelColor(T object);
  
  /**
   * Gets whether or not the labels (if any) produced by this style will be drawn
   * with antialiasing (i.e. smoothing).
   * 
   * @return true if the labels should be drawn with antialiasing on, otherwise false.
   */
  boolean getLabelAntialiased();
}
