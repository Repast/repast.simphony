/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.editedStyle.EditedStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;

/**
 * Style registrar for 2D displays.
 * 
 * @author Nick Collier
 */
public class StyleRegistrarOGL2D extends StyleRegistrar<StyleOGL2D<?>> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.StyleRegistrar#getEditedStyle(java.lang.String)
   */
  @Override
  protected StyleOGL2D<?> createdEditedStyle(String editedStyleName) {
    return new EditedStyleOGL2D(editedStyleName);
  }
}
