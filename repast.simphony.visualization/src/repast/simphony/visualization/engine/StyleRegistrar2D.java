/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedStyle2D;
import repast.simphony.visualization.visualization2D.style.Style2D;

/**
 * Style registrar for 2D displays.
 * 
 * @author Nick Collier
 */
public class StyleRegistrar2D extends StyleRegistrar<Style2D<?>> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.StyleRegistrar#getEditedStyle(java.lang.String)
   */
  @Override
  protected Style2D<?> createdEditedStyle(String editedStyleName) {
    return new EditedStyle2D(editedStyleName);
  }
}
