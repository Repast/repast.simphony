package repast.simphony.visualizationOGL2D;

import java.awt.Color;
import java.awt.Font;

import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

/**
 * Default 2D OGL Style.
 */

@SuppressWarnings({ "rawtypes" })
public class DefaultStyleOGL2D implements StyleOGL2D {
  
  protected ShapeFactory2D shapeFactory;

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#init(saf.v3d.ShapeFactory2D)
   */
  public void init(ShapeFactory2D factory) {
    this.shapeFactory = factory;
  }

  /**
   * @return a circle of radius 4.
   */
  public VSpatial getVSpatial(Object agent, VSpatial spatial) {
    if (spatial == null) {
      spatial = shapeFactory.createCircle(4, 16);
    }
    return spatial;
  }

  /**
   * @return Color.BLUE.
   */
  public Color getColor(Object agent) {
    return Color.BLUE;
  }

  public float getRotation(Object agent) {
    return 0f;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderColor(java.lang.
   * Object)
   */
  public Color getBorderColor(Object object) {
    return Color.BLACK;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderSize(java.lang.Object
   * )
   */
  public int getBorderSize(Object object) {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.StyleOGL2D#getScale(java.lang.Object)
   */
  public float getScale(Object object) {
    return 1f;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabel(java.lang.Object)
   */
  public String getLabel(Object object) {
    return null;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelFont(java.lang.Object)
   */
  public Font getLabelFont(Object object) {
    return null;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelPosition(java.lang.Object)
   */
  public Position getLabelPosition(Object object) {
    return Position.NORTH;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelXOffset(java.lang.Object)
   */
  public float getLabelXOffset(Object object) {
    return 0;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelYOffset(java.lang.Object)
   */
  public float getLabelYOffset(Object object) {
    return 0;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelColor(java.lang.Object)
   */
  public Color getLabelColor(Object object) {
    return Color.BLACK;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.StyleOGL2D#getLabelAntialiased()
   */
  @Override
  public boolean getLabelAntialiased() {
    return true;
  }
}
