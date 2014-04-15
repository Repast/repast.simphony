package repast.simphony.visualization.editor;

import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Iterator;

import org.piccolo2d.PCamera;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.nodes.PText;
import org.piccolo2d.util.PPickPath;
import org.piccolo2d.util.PStack;

import repast.simphony.visualization.grid.GridShape;

/**
 * Displays a tooltip specifying the current grid location.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GridLocationToolTip extends PBasicInputEventHandler implements PEditorEventListener {

  private GridShape shape;
  private PText ttNode = new PText();
  private String location = "";
  private PCamera camera;

  public GridLocationToolTip(PCamera camera) {
    this.camera = camera;
    ttNode.setPickable(false);
    ttNode.setFont(ttNode.getFont().deriveFont(Font.BOLD));
  }

  public void init() {}

  public void destroy() {}

  /**
   * Starts the listener. At the very least this should
   * add the listener to whatever PNode it is listening on.
   */
  public void start() {
    camera.addChild(ttNode);
    camera.addInputEventListener(this);
  }

  /**
   * Stops the listener. At the very least this should
   * remove the listener to whatever PNode it is listening on.
   */
  public void stop() {
    camera.removeChild(ttNode);
    camera.removeInputEventListener(this);
  }

  @Override
  public void mouseEntered(PInputEvent evt) {
    findShape(evt);
    updateToolTip(evt);
  }

  public void mouseExited(PInputEvent evt) {
    updateToolTip(evt);
  }

  @Override
  public void mouseMoved(PInputEvent evt) {
    updateToolTip(evt);
  }

  @Override
  public void mouseDragged(PInputEvent evt) {
    updateToolTip(evt);
  }

  public void updateToolTip(PInputEvent event) {
    if (shape == null) location = "";
    else {
      Point xy = shape.getXY(event.getPosition());
      String newLoc = xy == null ? null : xy.getX() + ", " + xy.getY();
      if (newLoc == null) newLoc = "";
      if (!location.equals(newLoc)) {
        location = newLoc;
      }
    }

    Point2D p = event.getCanvasPosition();
    event.getPath().canvasToLocal(p, camera);
    ttNode.setText(location);
    ttNode.setOffset(p.getX() + 8, p.getY() - 8);
  }

  private void findShape(PInputEvent evt) {
    PPickPath path = evt.getInputManager().getMouseOver();
    PStack pNodes = path.getNodeStackReference();

    for (Iterator iter = pNodes.iterator(); iter.hasNext();) {
      PNode node = (PNode) iter.next();
      if (node instanceof GridShape) {
        shape = (GridShape) node;
      }
    }
  }
}
