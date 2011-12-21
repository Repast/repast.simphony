package repast.simphony.render;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of Renderers, allowing client code to call update and
 * pause them collectively.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class RendererManager  {

  private List<Renderer> renderers = new ArrayList<Renderer>();
  
  /**
   * Adds a Renderer to this RendererManager.
   * 
   * @param renderer the renderer to add
   */
  public void addRenderer(Renderer renderer) {
    synchronized (renderers) {
      renderers.add(renderer);
    }
  }

  /**
   * Calls render on each Renderer contained in this RendererManager.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void render() {
    List<Renderer> workingList;
    synchronized (renderers) {
      workingList = (List<Renderer>) ((ArrayList) renderers).clone();
    }

    for (Renderer renderer : workingList) {
      renderer.render();
    }
  }

  /**
   * Calls setPause on each Renderer contained in this RendererManager.
   */
  public void setPause(boolean pause) {
    synchronized (renderers) {
      for (Renderer renderer : renderers) {
        renderer.setPause(pause);
      }
    }
  }

  /**
   * Clears the list of Renderers contained by this RendererManager.
   */
  public void clear() {
    renderers.clear();
  }
}
