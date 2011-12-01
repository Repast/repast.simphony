package repast.simphony.render;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class RenderEvent {
  
  private Object source;

  public RenderEvent(Object source) {
    this.source = source;
  }

  public Object getSource() {
    return source;
  }
}

