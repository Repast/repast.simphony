/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedValueLayerStyle2D;
import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;

/**
 * Style registrar for 2D value layer styles.
 * 
 * @author Nick Collier
 */
public class VLStyleRegistrar2D extends VLStyleRegistrar<ValueLayerStyle> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.VLStyleRegistrar#createEditedValueLayerStyle(java.lang.String)
   */
  @Override
  public ValueLayerStyle createEditedValueLayerStyle(String styleName) {
    return new EditedValueLayerStyle2D(styleName);
  }
}
