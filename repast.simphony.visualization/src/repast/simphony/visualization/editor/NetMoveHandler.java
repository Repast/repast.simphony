package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolox.handles.PHandle;
import repast.simphony.space.graph.Network;
import repast.simphony.visualization.network.PEdge;
import repast.simphony.visualization.visualization2D.Display2D;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Event handler for moving nodes in a grid.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class NetMoveHandler extends PDragSequenceEventHandler implements PEditorEventListener {

  private PNodeSelector selector = new PNodeSelector();
  private PCanvas canvas;
  private Point2D nodesStartPosition;
  private EdgeFinder edgeFinder;
  private List<PEdge> curEdges = new ArrayList<PEdge>();

  static {
    PHandle.DEFAULT_HANDLE_SHAPE = new Ellipse2D.Float(0f, 0f, 2, 2);
  }

  public NetMoveHandler(PCanvas canvas, Display2D display, List<Network> nets) {
    this.canvas = canvas;
    edgeFinder = new DefaultEdgeFinder(nets, display);
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
      selector.selectNodes(event.getPath().getNodeStackReference(), false);
      return selector.getSelectedNodes().size() > 0;
    }
    return false;
  }

  @Override
  protected void startDrag(PInputEvent event) {
    super.startDrag(event);
    PNode selectedNode = selector.getSelectedNodes().get(0);
    selectedNode.moveToFront();
    nodesStartPosition = selectedNode.getOffset();
    curEdges = edgeFinder.findEdges(selectedNode);
  }

  protected void drag(PInputEvent event) {
    super.drag(event);

    Point2D start = canvas.getCamera().localToView((Point2D) getMousePressedCanvasPoint().clone());
    Point2D current = event.getPosition();
    Point2D dest = new Point2D.Double();

    dest.setLocation(nodesStartPosition.getX() + (current.getX() - start.getX()),
            nodesStartPosition.getY() + (current.getY() - start.getY()));

    for (PNode node : selector.getSelectedNodes()) {
      node.setOffset(dest.getX(), dest.getY());
    }

    for (PEdge edge : curEdges) {
      edge.update();
    }
  }

  @Override
  protected void endDrag(PInputEvent event) {
    super.endDrag(event);
    selector.unselectNodes();
    curEdges.clear();
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
}