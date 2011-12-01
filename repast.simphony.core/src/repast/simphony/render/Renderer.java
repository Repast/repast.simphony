package repast.simphony.render;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public interface Renderer {
  
  public void render();
  public void addRenderListener(RenderListener listener);

	void setPause(boolean pause);
}
