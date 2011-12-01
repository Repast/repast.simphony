/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedStyle3D;
import repast.simphony.visualization.visualization3D.style.Style3D;

/**
 * Style registrar for 3D displays.
 * 
 * @author Nick Collier
 */
public class StyleRegistrar3D extends StyleRegistrar<Style3D<?>> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.StyleRegistrar#getEditedStyle(java.lang.String)
   */
  @Override
  protected Style3D<?> createdEditedStyle(String editedStyleName) {
    return new EditedStyle3D(editedStyleName);
  }
}
