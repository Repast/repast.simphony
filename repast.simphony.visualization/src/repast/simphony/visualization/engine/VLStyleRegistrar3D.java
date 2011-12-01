/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedValueLayerStyle3D;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

/**
 * Style registrar for 3D value layer styles.
 * 
 * @author Nick Collier
 */
public class VLStyleRegistrar3D extends VLStyleRegistrar<ValueLayerStyle3D> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.VLStyleRegistrar#createEditedValueLayerStyle(java.lang.String)
   */
  @Override
  public ValueLayerStyle3D createEditedValueLayerStyle(String styleName) {
    return new EditedValueLayerStyle3D(styleName);
  }
}
