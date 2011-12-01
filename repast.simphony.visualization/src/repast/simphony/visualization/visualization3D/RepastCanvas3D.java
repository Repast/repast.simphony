package repast.simphony.visualization.visualization3D;

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

import javax.media.j3d.Canvas3D;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class RepastCanvas3D extends Canvas3D {
  
  private java.util.List<Canvas3DListener> listeners = new ArrayList<Canvas3DListener>();

  public RepastCanvas3D(GraphicsConfiguration graphicsConfiguration) {
    super(graphicsConfiguration);
  }

  public RepastCanvas3D(GraphicsConfiguration graphicsConfiguration, boolean offScreen) {
    super(graphicsConfiguration, offScreen);
  }
  
  public void postSwap() {
    fireFrameFinished();
  }
  
  public void addListener(Canvas3DListener listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }
  
  public boolean removeListeners(Canvas3DListener listener) {
    synchronized (listeners) {
      return listeners.remove(listener);
    }
  }
  
  public void fireFrameFinished() { 
    //java.util.List<Canvas3DListener> workingList;
    //synchronized (listeners) {
    //  workingList = (java.util.List<Canvas3DListener>)((ArrayList)listeners).clone();
    //}
      
    for (Canvas3DListener listener : listeners) {
      listener.frameFinished();
    }
  }
}
