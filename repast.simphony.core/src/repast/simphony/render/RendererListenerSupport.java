package repast.simphony.render;

import java.util.ArrayList;
import java.util.List;

/**
 * Support class making it easier to add and remove renderListeners and fire render events.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class RendererListenerSupport {
  private List<RenderListener> listeners = new ArrayList<RenderListener>();
  
  public void addListener(RenderListener listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }
  
  public boolean removeListeners(RenderListener listener) {
    synchronized (listeners) {
      return listeners.remove(listener);
    }
  }
  
  public void fireRenderFinished(Object source) { 
    RenderEvent evt = new RenderEvent(source);
    List<RenderListener> workingList;
    synchronized (listeners) {
      workingList = (List<RenderListener>)((ArrayList)listeners).clone();
    }
      
    for (RenderListener listener : workingList) {
      listener.renderFinished(evt);
    }
  }
}
