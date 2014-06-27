package repast.simphony.visualization.editor;

import java.util.List;

import org.piccolo2d.PNode;

import repast.simphony.visualization.network.PEdge;

/**
 * Interface for classes that given a PNode, this will find any edges associated with it, if any.
 * 
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface EdgeFinder {

  /**
   * Finds the PEdges associated with the specified PNode.
   *
   * @param node the node whose edges should be found
   *
   * @return the PEdges associated with the specified PNode or an empty list if none
   * are found.
   */
  List<PEdge> findEdges(PNode node);
}