/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedEdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;

/**
 * Network style registrar for 2D network styles.
 * @author Nick Collier
 */
public class NetworkStyleRegistrarOGL2D extends NetworkStyleRegistrar<EdgeStyleOGL2D> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.NetworkStyleRegistrar#createEditedEdgeStyle(java.lang.String)
   */
  @Override
  protected EdgeStyleOGL2D createEditedEdgeStyle(String styleName) {
    return new EditedEdgeStyleOGL2D(styleName);
  }
}
