package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PNode;

import java.util.List;

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
