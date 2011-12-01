/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Color;

import repast.simphony.space.graph.RepastEdge;

/**
 * Inteface for 2D OGL edge styles.
 * 
 * @author Nick Collier
 */
public interface EdgeStyleOGL2D {

  /**
   * Gets the width of the line used to represent the edge.
   * 
   * @param edge
   * 
   * @return the width of the line used to represent the edge.
   */
   int getLineWidth(RepastEdge<?> edge);

   /**
    * Gets the color of the line used to represent the edge.
    * 
    * @param edge
    * @return the color of the line used to represent the edge.
    */
   Color getColor(RepastEdge<?> edge);

}
