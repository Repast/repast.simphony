/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedEdgeStyleGIS3D;
import repast.simphony.visualization.gis3D.style.EdgeStyleGIS3D;

/**
 * Network style registrar for 3D GIS network styles.
 * 
 * @author Nick Collier
 */
public class NetworkStyleRegistrarGIS3D extends NetworkStyleRegistrar<EdgeStyleGIS3D<?>> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.NetworkStyleRegistrar#createEditedEdgeStyle(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected EdgeStyleGIS3D<?> createEditedEdgeStyle(String styleName) {
    return new EditedEdgeStyleGIS3D(styleName);
  }
}
