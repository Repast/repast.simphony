package repast.simphony.visualization.editor;

import java.util.ArrayList;
import java.util.List;

import org.piccolo2d.PCanvas;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PNodeFilter;
import org.piccolo2d.util.PStack;

import repast.simphony.visualization.network.NetworkDisplayLayer2D;
import repast.simphony.visualization.visualization2D.StyledDisplayLayer2D;

/**
 * Event handler for selecting nodes.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class SelectionHandler extends PBasicInputEventHandler implements PEditorEventListener {

  private PCanvas canvas;
  private List<PNodeSelectionListener> listeners = new ArrayList<PNodeSelectionListener>();
  private boolean fireSelected = true;
  private PNodeSelector selector = new PNodeSelector();

  public SelectionHandler(PCanvas canvas) {
    this.canvas = canvas;
  }

  @Override
  public void mouseClicked(PInputEvent event) {
    if (!(event.isControlDown() || event.isMetaDown())) {
      selector.unselectNodes();
    }
    selectNodes(event.getPath().getNodeStackReference());
  }

  public void addNodeSelectionListener(PNodeSelectionListener listener) {
    listeners.add(listener);
  }

  class NodeFilter implements PNodeFilter {

    private Object[] objs;

    NodeFilter(Object[] objs) {
      this.objs = objs;
    }

    public boolean accept(PNode pNode) {
      Object obj = pNode.getAttribute(StyledDisplayLayer2D.AGENT_KEY);
      if (obj == null) {
        obj = pNode.getAttribute(NetworkDisplayLayer2D.REL_KEY);
      }
      if (obj == null) return false;
      for (Object o : objs) {
        if (o.equals(obj)) return true;
      }
      return false;
    }

    public boolean acceptChildrenOf(PNode pNode) {
      return true;
    }
  }

  /**
   * Invoked when the specified objects have been selected in
   * code external to this
   * @param objs the selected objects
   */
  public void objectsSelected(Object[] objs) {
    selector.unselectNodes();
    PStack stack = new PStack();
    canvas.getRoot().getAllNodes(new NodeFilter(objs), stack);
    fireSelected = false;
    selectNodes(stack);
    fireSelected = true;
  }

  /**
   * Initializes this listener. This happens only once whereas start / stop may
   * occur multiple times.
   */
  public void init() {}

  /**
   * Cleans up anything created by this listener in init. This happens only once whereas
   * start / stop may occur multiple times.
   */
  public void destroy() {}

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

  protected void selectNodes(PStack stack) {
    selector.selectNodes(stack, true);
    firePNodesSelected();
  }

  private void firePNodesSelected() {
    if (fireSelected) {
      List<PNode> nodes = new ArrayList<PNode>(selector.getSelectedNodes());
      for (PNodeSelectionListener listener : listeners) {
        listener.pNodesSelected(nodes);
      }
    }
  }
}