/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;

/**
 * @author Nick Collier
 */
public class NetworkStyleRegistrar3D extends NetworkStyleRegistrar<EdgeStyle3D<?>> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.NetworkStyleRegistrar#createEditedEdgeStyle(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected EdgeStyle3D<?> createEditedEdgeStyle(String styleName) {
    return new EditedEdgeStyle3D(styleName);
  }
}
