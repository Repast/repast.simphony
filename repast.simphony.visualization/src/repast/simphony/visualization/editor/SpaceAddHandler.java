package repast.simphony.visualization.editor;

import java.awt.Cursor;
import java.awt.geom.Point2D;

import org.piccolo2d.PCamera;
import org.piccolo2d.PCanvas;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.visualization2D.Display2D;

/**
 * Handler for adding via mouse click.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class SpaceAddHandler extends PBasicInputEventHandler implements PEditorEventListener {

  private ContinuousSpace space;
  private boolean shapeFound;
  private PCamera camera;
  private PNode shape;
  private DisplayEditor2D editor;
  private PCanvas canvas;
  private float unitSize;

  public SpaceAddHandler(PCanvas canvas, Display2D display, DisplayEditor2D editor, ContinuousSpace space) {
    this.space = space;
    this.camera = canvas.getCamera();
    this.editor = editor;
    this.canvas = canvas;
    this.unitSize = (Float) display.getLayout().getLayoutProperties().getProperty(UnitSizeLayoutProperties.UNIT_SIZE);
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


  public void mouseClicked(PInputEvent evt) {
    if (evt.isLeftMouseButton()) {
      Point2D xy = evt.getPosition();
      if (xy != null) {
        double x = xy.getX() / unitSize;
        double y = xy.getY() / unitSize;
        double[] origin = space.getDimensions().originToDoubleArray(null);
        double xOffset = origin[0];
        double yOffset = origin[1];
        if (x < space.getDimensions().getWidth() && x >= 0 && y < space.getDimensions().getHeight() &&
                y >= 0) {
          editor.addAgentAt(x-xOffset, y-yOffset);
        }
      }
    } else if (evt.isRightMouseButton()) {
      editor.addCanceled();
    }
  }
}