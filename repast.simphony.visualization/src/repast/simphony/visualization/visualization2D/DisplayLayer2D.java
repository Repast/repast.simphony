package repast.simphony.visualization.visualization2D;

import java.util.HashMap;
import java.util.Map;

import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PInputEventListener;
/**
 *  @deprecated replaced by ogl 2D
 */
public abstract class DisplayLayer2D<T, E extends PNode> extends PLayer implements
        IDisplayLayer2D {
  protected PInputEventListener currentListener;

  protected final Map<T, E> visualItemMap;

  public DisplayLayer2D() {
    visualItemMap = new HashMap<T, E>();
    setPickable(false);
  }

  public void setListener(PInputEventListener listener) {
    if (currentListener != null) {
      removeInputEventListener(currentListener);
    }
    addInputEventListener(listener);
  }
}
