package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolo.util.PStack;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.grid.Grid2DLayout;
import repast.simphony.visualization.grid.GridShape;
import repast.simphony.visualization.network.PEdge;
import repast.simphony.visualization.visualization2D.Display2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Event handler for moving nodes in a grid.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GridMoveHandler extends PDragSequenceEventHandler implements PEditorEventListener {

  private Grid grid;
  private PCanvas canvas;
  private Point2D nodesStartPosition;
  private GridShape shape;
  private Display2D display;
  private boolean shapeAdded = false;
  private PNodeSelector selector = new PNodeSelector();
  private EdgeFinder edgeFinder = new EmptyEdgeFinder();
  private List<PEdge> curEdges = new ArrayList<PEdge>();
  private EditorNotifier notifier;

  public GridMoveHandler(PCanvas canvas, Display2D display, Grid grid, List<Network> nets, EditorNotifier notifier) {
    this.canvas = canvas;
    this.grid = grid;
    this.display = display;
    if (nets.size() > 0) edgeFinder = new DefaultEdgeFinder(nets, display);
    this.notifier = notifier;
  }

  /**
   * Initializes this listener. This happens only once whereas start / stop may
   * occur multiple times.
   */
  public void init() {
    findGridShape();
  }

  /**
   * Cleans up anything created by this listener in init. This happens only once whereas
   * start / stop may occur multiple times.
   */
  public void destroy() {
    if (shapeAdded) canvas.getLayer().removeChild(shape);
  }

  @Override
  protected boolean shouldStartDragInteraction(PInputEvent event) {
    if (super.shouldStartDragInteraction(event) && shape != null) {
      selectNodes(event.getPath());
      return selector.getSelectedNodes().size() > 0;
    }
    return false;
  }

  @Override
  protected void startDrag(PInputEvent event) {
    super.startDrag(event);
    List<PNode> selectedNodes = selector.getSelectedNodes();
    selectedNodes.get(0).moveToFront();
    nodesStartPosition = selectedNodes.get(0).getOffset();
    curEdges = edgeFinder.findEdges(selectedNodes.get(0));
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
    Point xy = shape.getXY(event.getPosition());
    List<PNode> selectedNodes = selector.getSelectedNodes();
    if (xy == null) {
      for (PNode node : selectedNodes) {
        node.setOffset(nodesStartPosition.getX(), nodesStartPosition.getY());
      }
    } else {
      for (PNode node : selectedNodes) {
        Object obj = display.findObjForItem(node);
        GridPoint loc = grid.getLocation(obj);
        if (loc.getX() == xy.getX() && loc.getY() == xy.getY()) {
          node.setOffset(nodesStartPosition.getX(), nodesStartPosition.getY());
        } else {
        	int[] origin = grid.getDimensions().originToIntArray(null);
        	int xOffset = origin[0];
        	int yOffset = origin[1];

        	grid.moveTo(obj, (int) (xy.getX()- xOffset), (int) (xy.getY() - yOffset));
        }
      }
      notifier.editorEventOccurred();
    }

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

  private void selectNodes(PPickPath path) {
    PStack stack = path.getNodeStackReference();
    selector.selectNodes(stack, false);
  }

  private void findGridShape() {
    for (Object obj : canvas.getRoot().getAllNodes()) {
      PNode node = (PNode) obj;
      if (node instanceof GridShape) {
        shape = (GridShape) node;
      }
    }

    if (shape == null) {
      // make our own
      GridDimensions dims = grid.getDimensions();
      // if there is a grid we can assume a grid layout
      Grid2DLayout layout = (Grid2DLayout) display.getLayout();
      float unitSize = (Float) layout.getLayoutProperties().getProperty(UnitSizeLayoutProperties.UNIT_SIZE);
      shape = new GridShape(unitSize, Color.BLACK, dims.getDimension(0), dims.getDimension(1));
      shape.translate(-unitSize / 2, -unitSize / 2);
      canvas.getLayer().addChild(shape);
      shapeAdded = true;
    }
  }
}
