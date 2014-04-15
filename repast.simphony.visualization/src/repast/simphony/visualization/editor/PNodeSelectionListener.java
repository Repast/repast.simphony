package repast.simphony.visualization.editor;

import java.util.List;

import org.piccolo2d.PNode;

/**
 * Interface for classes that want to be notified of PNode selection.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface PNodeSelectionListener {

  /**
   * Invoked when nodes have been selected.
   *
   * @param nodes the selected nodes.
   */
  void pNodesSelected(List<PNode> nodes);
}
