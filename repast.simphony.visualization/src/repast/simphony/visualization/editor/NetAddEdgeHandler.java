package repast.simphony.visualization.editor;

import java.awt.geom.Point2D;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.visualization2D.Display2D;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolo.util.PStack;

/**
 * Event handler for adding edges.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class NetAddEdgeHandler extends PDragSequenceEventHandler implements PEditorEventListener {

  private PCanvas canvas;
  private Display2D display;
  private PNodeSelector selector = new PNodeSelector();
  private PPath edge;
  private DisplayEditor2D editor;
  private EditorNotifier notifier;

  public NetAddEdgeHandler(DisplayEditor2D editor, PCanvas canvas, Display2D display, EditorNotifier notifier) {
    this.canvas = canvas;
    this.display = display;
    this.editor = editor;
    this.notifier = notifier;
  }

  /**
   * Initializes this listener. This happens only once whereas start / stop may
   * occur multiple times.
   */
  public void init() {
  }

  /**
   * Cleans up anything created by this listener in init. This happens only once whereas
   * start / stop may occur multiple times.
   */
  public void destroy() {
  }

  @Override
  protected boolean shouldStartDragInteraction(PInputEvent event) {
    if (super.shouldStartDragInteraction(event)) {
      selector.unselectNodes();
      selectNodes(event.getPath());
      return selector.getSelectedNodes().size() == 1;
    }
    return false;
  }

  @Override
  protected void startDrag(PInputEvent event) {
    super.startDrag(event);
    PNode selectedNode = selector.getSelectedNodes().get(0);
    selectedNode.moveToFront();
    Point2D.Double bound1 = (Point2D.Double) selectedNode.getBounds().getCenter2D();

    edge = new PPath();
    edge.moveTo((float) bound1.getX(), (float) bound1.getY());
    //edgeLayer.addChild(edge);
    canvas.getLayer().addChild(edge);
  }

  protected void drag(PInputEvent event) {
    super.drag(event);

    PNode selectedNode = selector.getSelectedNodes().get(0);
    Point2D.Double bound1 = (Point2D.Double) selectedNode.getFullBounds().getCenter2D();
    Point2D current = event.getPosition();

    edge.reset();
    edge.moveTo((float) bound1.getX(), (float) bound1.getY());
    edge.lineTo((float) current.getX(), (float) current.getY());
  }

  @Override
  protected void endDrag(PInputEvent event) {
    super.endDrag(event);

    java.util.List<PNode> nodes = selector.getNodes(canvas.getCamera().pick(event.getCanvasPosition().getX(),
            event.getCanvasPosition().getY(), 2).getNodeStackReference(), false);
    RepastEdge newEdge = null;
    if (nodes.size() > 0) {
      PNode node = nodes.get(0);
      Object source = display.findObjForItem(selector.getSelectedNodes().get(0));
      Object target = display.findObjForItem(node);
      newEdge = editor.addEdge(source, target);
      notifier.editorEventOccurred();
    }
    canvas.getLayer().removeChild(edge);
    selector.unselectNodes();

    // visually select the just created edge
    if (newEdge != null) {
      PNode node = display.getVisualItem(newEdge);
      PStack stack = new PStack();
      stack.add(node);
      selector.selectNodes(stack, true);
    }
  }

  /**
   * Starts the listener. At the very least this should
   * add the listener to whatever PNode it is listening on.
   */
  public void start() {
    canvas.addInputEventListener(this);
  }

  /**
   * Stops the listener. At the very least this should
   * remove the listener to whatever PNode it is listening on.
   */
  public void stop() {
    selector.unselectNodes();
    canvas.removeInputEventListener(this);
  }

  private void selectNodes(PPickPath path) {
    PStack stack = path.getNodeStackReference();
    selector.selectNodes(stack, false);
  }
}