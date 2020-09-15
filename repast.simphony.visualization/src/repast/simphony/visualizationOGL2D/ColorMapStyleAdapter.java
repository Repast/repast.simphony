/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Color;

import org.jogamp.vecmath.Color3f;

import saf.v3d.grid.GridColorMap;
import saf.v3d.util.Utils3D;

/**
 * Adapts a ValueLayerStyle to a saf uil grid color map.
 * 
 * @author Nick Collier
 */
public class ColorMapStyleAdapter implements GridColorMap {
  
  private ValueLayerStyleOGL style;
  private int[] origin;

  /**
   * @param style the style value layer style
   * @param origin the origin coordinate of the value layer
   */
  public ColorMapStyleAdapter(ValueLayerStyleOGL style, int[] origin) {
    super();
    this.style = style;
    this.origin = origin;
  }


  /* (non-Javadoc)
   * @see saf.v3d.GridColorMap#getColor(int, int)
   */
  public void getColor(int x, int y, Color3f color) {
    int tx = x - origin[0];
    int ty = y - origin[1];
    Color c = style.getColor(tx, ty);
    Utils3D.updateColor(c);
  }
}
