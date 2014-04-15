package repast.simphony.visualization.editor;

import java.util.ArrayList;
import java.util.List;

import org.piccolo2d.PNode;

import repast.simphony.visualization.network.PEdge;

/**
 * Implementation of EdgeFinder that always returns an empty list. This
 * can be used when an EdgeFinder is necesary but there is no network.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class EmptyEdgeFinder implements EdgeFinder {

  private static final List<PEdge> LIST = new ArrayList<PEdge>();

  /**
   * Finds the PEdges associated with the specified PNode. This always
   * returns an empty list.
   *
   * @param node the node whose edges should be found
   * @return an empty list
   */
  public List<PEdge> findEdges(PNode node) {
    return LIST;
  }
}
