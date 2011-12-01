package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolo.util.PStack;
import repast.simphony.space.grid.Grid;
import repast.simphony.visualization.grid.GridShape;

import java.awt.*;
import java.util.Iterator;

/**
 * Handler for adding via mouse click.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GridAddHandler extends PBasicInputEventHandler implements PEditorEventListener {

	private Grid grid;
	private GridShape shape;
	private PCamera camera;
	private DisplayEditor2D editor;
	private PCanvas canvas;

  public GridAddHandler(PCanvas canvas, DisplayEditor2D editor, Grid grid) {
    this.camera = canvas.getCamera();
    this.editor = editor;
    this.canvas = canvas;
    this.grid = grid;
  }

  public void init() {
  }

  public void destroy() {
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

  @Override
  public void mouseEntered(PInputEvent evt) {
    findShape(evt);
  }

  
  /**
      double[] origin = space.getDimensions().originToDoubleArray(null);
        double xOffset = origin[0];
        double yOffset = origin[1];
        if (x < space.getDimensions().getWidth() && x >= 0 && y < space.getDimensions().getHeight() &&
                y >= 0) {
          editor.addAgentAt(x-xOffset, y-yOffset);

   */
  public void mouseClicked(PInputEvent evt) {
    if (shape != null) {
      if (evt.isLeftMouseButton()) {
        Point xy = shape.getXY(evt.getPosition());
        if (xy != null) {
        	int[] origin = grid.getDimensions().originToIntArray(null);
        	int xOffset = origin[0];
        	int yOffset = origin[1];
        	editor.addAgentAt(xy.getX() - xOffset, xy.getY() - yOffset);
        }
      } else if (evt.isRightMouseButton()) {
        editor.addCanceled();
      }
    }
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