/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Color;

import repast.simphony.space.graph.RepastEdge;

/**
 * Default implementation of OGL 2D edge style.
 * 
 * 
 * @author Nick Collier
 */
public class DefaultEdgeStyleOGL2D implements EdgeStyleOGL2D {

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getColor(repast.simphony.space.graph.RepastEdge)
   */
  public Color getColor(RepastEdge<?> edge) {
    return Color.GREEN;
  }

  /* (non-Javadoc)
   * @see repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getLineWidth(repast.simphony.space.graph.RepastEdge)
   */
  public int getLineWidth(RepastEdge<?> edge) {
    return 2;
  }
  
  

}
