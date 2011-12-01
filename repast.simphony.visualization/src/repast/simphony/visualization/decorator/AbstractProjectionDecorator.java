package repast.simphony.visualization.decorator;

import java.awt.Color;

import repast.simphony.space.projection.Projection;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractProjectionDecorator<T extends Projection<?>> {

  public static final String TYPE_KEY = "DECORATOR_TYPE_KEY";
  public static final String SHOW_DECORATOR = "SHOW";
  public static final String UNIT_SIZE = "UNIT_SIZE";
  public static final String COLOR = "COLOR";

  protected T projection;
  protected float unitSize = 1f;
  protected Color color = Color.WHITE;

  /**
   * Gets the projection that this decorator decorates.
   * 
   * @return the projection that this decorator decorates.
   */
  public T getProjection() {
    return projection;
  }

  /**
   * Sets the Grid that this decorator decorates.
   * 
   * @param grid
   */
  public void setProjection(T projection) {
    this.projection = projection;
  }

  /**
   * Gets the color of the decoration.
   * 
   * @return the color of the decoration.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the color of the decoration.
   * 
   * @param color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Gets the unit size of the decoration.
   * 
   * @return the cell size of the decoration.
   */
  public float getUnitSize() {
    return unitSize;
  }

  /**
   * Sets the unit size of the decoration.
   * 
   * @param unitSize
   */
  public void setUnitSize(float unitSize) {
    this.unitSize = unitSize;
  }

}
