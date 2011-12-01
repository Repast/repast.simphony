/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedEdgeStyle2D;
import repast.simphony.visualization.visualization2D.style.EdgeStyle2D;

/**
 * Network style registrar for 2D network styles.
 * @author Nick Collier
 */
public class NetworkStyleRegistrar2D extends NetworkStyleRegistrar<EdgeStyle2D> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.NetworkStyleRegistrar#createEditedEdgeStyle(java.lang.String)
   */
  @Override
  protected EdgeStyle2D createEditedEdgeStyle(String styleName) {
    return new EditedEdgeStyle2D(styleName);
  }
}
