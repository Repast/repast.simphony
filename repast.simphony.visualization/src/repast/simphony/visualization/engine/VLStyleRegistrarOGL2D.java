/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedValueLayerStyleOGL2D;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * Style registrar for 2D value layer styles.
 * 
 * @author Nick Collier
 */
public class VLStyleRegistrarOGL2D extends VLStyleRegistrar<ValueLayerStyleOGL> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.VLStyleRegistrar#createEditedValueLayerStyle(java.lang.String)
   */
  @Override
  public ValueLayerStyleOGL createEditedValueLayerStyle(String styleName) {
    return new EditedValueLayerStyleOGL2D(styleName);
  }
}
