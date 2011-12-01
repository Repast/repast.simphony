package repast.simphony.visualization.editor;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.visualization.UnitSizeLayoutProperties;
import repast.simphony.visualization.continuous.Continuous2DProjectionDecorator;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.network.PEdge;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization2D.ShapeFactory2D;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolo.util.PStack;

/**
 * Event handler for moving nodes in a continous space.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class ContSpaceMoveHandler extends PDragSequenceEventHandler implements PEditorEventListener {

  private ContinuousSpace space;
  private float unitSize;
  private PCanvas canvas;
  private Point2D nodesStartPosition;
  private List<PEdge> curEdges = new ArrayList<PEdge>();
  private PNode shape;
  private Display2D display;
  private PNodeSelector selector = new PNodeSelector();
  private EdgeFinder edgeFinder = new EmptyEdgeFinder();
  private EditorNotifier notifier;

  public ContSpaceMoveHandler(PCanvas canvas, Display2D display, ContinuousSpace space, List<Network> nets,
                              EditorNotifier notifier) {
    this.canvas = canvas;
    this.space = space;
    this.display = display;
    this.unitSize = (Float) display.getLayout().getLayoutProperties().getProperty(UnitSizeLayoutProperties.UNIT_SIZE);
    if (nets.size() > 0) edgeFinder = new DefaultEdgeFinder(nets, display);
    this.notifier = notifier;
  }


  /**
   * Initializes this listener. This happens only once whereas start / stop may
   * occur multiple times.
   */
  public void init() {
    findBoundingShape();
  }

  /**
   * Cleans up anything created by this listener in init. This happens only once whereas
   * start / stop may occur multiple times.
   */
  public void destroy() {
    if (shape != null) canvas.getLayer().removeChild(shape);
  }

  @Override
  protected boolean shouldStartDragInteraction(PInputEvent event) {
    if (super.shouldStartDragInteraction(event)) {
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
    Point2D xy = event.getPosition();
    List<PNode> selectedNodes = selector.getSelectedNodes();
    if (xy == null) {
      for (PNode node : selectedNodes) {
        node.setOffset(nodesStartPosition.getX(), nodesStartPosition.getY());
      }
    } else {
      for (PNode node : selectedNodes) {
        Object obj = display.findObjForItem(node);
        double x = xy.getX() / unitSize;
        double y = xy.getY() / unitSize;
        if (x > space.getDimensions().getWidth() || x < 0 || y > space.getDimensions().getHeight() ||
                y < 0) {
          node.setOffset(nodesStartPosition.getX(), nodesStartPosition.getY());
        } else {
        	double[] origin = space.getDimensions().originToDoubleArray(null);
        	double xOffset = origin[0];
        	double yOffset = origin[1];
          space.moveTo(obj, (xy.getX() / unitSize) - xOffset , (xy.getY() / unitSize) - yOffset );
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

  private void findBoundingShape() {
    boolean shapeFound = false;
    for (Object obj : canvas.getRoot().getAllNodes()) {
      PNode node = (PNode) obj;
      Object type = node.getAttribute(AbstractProjectionDecorator.TYPE_KEY);
      if (type != null && type.equals(Continuous2DProjectionDecorator.TYPE)) {
        shapeFound = true;
        break;
      }
    }

    if (!shapeFound) {
      // make our own
      Dimensions dims = space.getDimensions();
      float width = (float) dims.getWidth();
      float height = (float) dims.getHeight();
      if (width != Float.NEGATIVE_INFINITY && height != Float.NEGATIVE_INFINITY) {
        shape = ShapeFactory2D.createBoundingBox(width * unitSize, height * unitSize, Color.BLACK);
        canvas.getLayer().addChild(shape);
      }
    }
  }
}