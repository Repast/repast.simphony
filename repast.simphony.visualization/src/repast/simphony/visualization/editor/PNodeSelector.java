package repast.simphony.visualization.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.piccolo2d.PNode;
import org.piccolo2d.extras.handles.PHandle;
import org.piccolo2d.extras.util.PBoundsLocator;
import org.piccolo2d.extras.util.PNodeLocator;
import org.piccolo2d.util.PStack;

import repast.simphony.visualization.network.NetworkDisplayLayer2D;
import repast.simphony.visualization.network.PEdge;
import repast.simphony.visualization.visualization2D.StyledDisplayLayer2D;

/**
 * Selects PNodes representing agent etc., given a PStack.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class PNodeSelector {

  private List<PNode> selectedNodes = new ArrayList<PNode>();

  // Not compatible with Piccolo 3.0
//  static {
//    PHandle.DEFAULT_HANDLE_SHAPE = new Ellipse2D.Float(0f, 0f, 2, 2);
//  }

  /**
   * Gets a list of the currently selected nodes.
   *
   * @return a list of the currently selected nodes.
   */
  public List<PNode> getSelectedNodes() {
    return selectedNodes;
  }

  /**
   * Selects the agent and optionally the edge nodes in the PStack.
   *
   * @param includeEdges whether or not to include edges in the returned list
   * @param stack the stack containing the nodes to select
   */
  public void selectNodes(PStack stack, boolean includeEdges) {
    for (PNode node : getNodes(stack, includeEdges)) {
      selectedNodes.add(node);
      addDecoration(node);
    }
  }

  /**
   * Gets the non-camera, layer, etc. nodes in the stack.
   *
   * @param stack the stack of nodes to query
   * @param includeEdges whether or not to include edge nodes in the returned list
   * @return the non-camera, layer, etc. nodes in the stack.
   */
  public List<PNode> getNodes(PStack stack, boolean includeEdges) {
    List<PNode> nodes = new ArrayList<PNode>();
    for (Iterator iter = stack.iterator(); iter.hasNext();) {
      PNode node = (PNode) iter.next();
      if (node.getAttribute(StyledDisplayLayer2D.AGENT_KEY) != null ||
              (includeEdges && node.getAttribute(NetworkDisplayLayer2D.REL_KEY) != null)) {
        nodes.add(node);
      } else if (includeEdges && node.getParent() instanceof PEdge) {
        // stack node is the head of an arrow, so select its parent
        // which actually references the edge
        nodes.add(node.getParent());
      }
    }
    return nodes;
  }

  private void addDecoration(PNode node) {
    if (node instanceof PEdge) {
      node.addChild(new PHandle(new PNodeLocator(node)));
      if (node.getChildrenCount() > 0) {
        node.getChild(0).addChild(new PHandle(new PNodeLocator(node.getChild(0))));
      }
    } else {
      node.addChild(new PHandle(PBoundsLocator.createNorthLocator(node)));
      node.addChild(new PHandle(PBoundsLocator.createSouthLocator(node)));
      node.addChild(new PHandle(PBoundsLocator.createEastLocator(node)));
      node.addChild(new PHandle(PBoundsLocator.createWestLocator(node)));
    }
  }

  /**
   * Unselects any selected nodes.
   */
  public void unselectNodes() {
    for (PNode node : selectedNodes) {
      removeHandles(node);
      if (node instanceof PEdge && node.getChildrenCount() > 0) {
        for (Iterator iter = node.getChildrenIterator(); iter.hasNext();) {
          removeHandles((PNode) iter.next());
        }
      }
    }
    selectedNodes.clear();
  }

  private void removeHandles(PNode node) {
    List<PHandle> handles = new ArrayList<PHandle>();
    for (Iterator iter = node.getChildrenIterator(); iter.hasNext();) {
      Object child = iter.next();
      if (child instanceof PHandle) {
        handles.add((PHandle) child);
      }
    }
    node.removeChildren(handles);
  }
}
