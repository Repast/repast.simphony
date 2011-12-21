package repast.simphony.render;

/**
 * Interface for classes that can render agents etc in a display.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public interface Renderer {

  /**
   * Render the display. This rendering should take place asynchronously on the
   * AWT thread.
   */
  void render();

  /**
   * Pause the rendering. The Renderer should update and render if pause is true, if possible.
   * The rendering should take place asynchronously on the AWT thread.
   * 
   * @param pause
   */
  void setPause(boolean pause);
}
