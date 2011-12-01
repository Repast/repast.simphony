package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.NullLayout;
import repast.simphony.visualization.visualization2D.Display2D;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Listener for adding to a network. This doesn't do
 * much as the network isn't spatial to begin with.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class NetAddListener implements AddListener {

  class NetAddHandler extends PBasicInputEventHandler implements PEditorEventListener {

    private PCanvas canvas;
    private PCamera camera;
    private DisplayEditor2D editor;

    NetAddHandler(PCanvas canvas, PCamera camera, DisplayEditor2D editor) {
      this.canvas = canvas;
      this.camera = camera;
      this.editor = editor;
    }

    /**
     * Initializes this listener. This happens only once whereas start / stop may
     * occur multiple times.
     */
    public void init() {
    }

    /**
     * Starts the listener. At the very least this should
     * add the listener to whatever PNode it is listening on.
     */
    public void start() {
      camera.addInputEventListener(this);
      canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    /**
     * Stops the listener. At the very least this should
     * remove the listener to whatever PNode it is listening on.
     */
    public void stop() {
      camera.removeInputEventListener(this);
      canvas.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Cleans up anything created by this listener in init. This happens only once whereas
     * start / stop may occur multiple times.
     */
    public void destroy() {
    }

    @Override
    public void mouseClicked(PInputEvent event) {
      Point2D pt = event.getPosition();
      editor.addAgentAt(pt.getX(), pt.getY());
    }
  }


  private NetAddHandler handler;
  private Display2D display;
  private Layout layout;

  public NetAddListener(PCanvas canvas, DisplayEditor2D editor, Display2D display) {
    handler = new NetAddHandler(canvas, canvas.getCamera(), editor);
    this.display = display;
    layout = display.getLayout();
  }

  /**
   * Called immediately prior to an agent being added.
   *
   * @param obj
   *@param location the location at which the agent should
   *                 be added. @return true if an object can be added to the specified location,
   *         otherwise false.
   */
  public boolean preAdd(Object obj, double... location) {
    // set a layout frequency of at interval so
    // the layout won't change when the item is added
    display.setLayout(new TmpLayout(display, obj, (float)location[0], (float)location[1]));
    return true;
  }

  /**
   * Called immediately after the agent has been added.
   */
  public void postAdd() {
    // set back to the original layout
    display.setLayout(layout);
  }

  /**
   * Gets the PInputEvent handler that will handle
   * the gui part of the adding.
   *
   * @return the PInputEvent handler that will handle
   *         the gui part of the adding.
   */
  public PEditorEventListener getAddHandler() {
    return handler;
  }

  static class TmpLayout extends NullLayout {

    float[] newLocation = new float[2];
    Display2D display;
    Object obj;

    TmpLayout(Display2D display, Object obj, float x, float y) {
      this.display = display;
      newLocation[0] = x;
      newLocation[1] = y;
      this.obj = obj;
    }

    @Override
    public float[] getLocation(Object obj) {
      PNode node = display.getVisualItem(obj);
      if (this.obj.equals(obj)) return newLocation;
      return new float[]{(float)node.getOffset().getX(), (float)node.getOffset().getY()};
    }
  }
}
