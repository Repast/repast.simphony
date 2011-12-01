package repast.simphony.visualization.editor;

import edu.umd.cs.piccolo.PNode;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.network.PEdge;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization2D.StyledDisplayLayer2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a PNode, this will find any edges associated with it, if any.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class DefaultEdgeFinder implements EdgeFinder {

  private List<Network> nets;
  private Display2D display;

  public DefaultEdgeFinder(List<Network> nets, Display2D display) {
    this.nets = nets;
    this.display = display;
  }

  /**
   * Finds the PEdges associated with the specified PNode.
   *
   * @param node the node whose edges should be found
   * @return the PEdges associated with the specified PNode or an empty list if none
   *         are found.
   */
  public List<PEdge> findEdges(PNode node) {
    Object obj = node.getAttribute(StyledDisplayLayer2D.AGENT_KEY);
    List<PEdge> edges = new ArrayList<PEdge>();
    for (Network net : nets) {
      for (RepastEdge edge : (Iterable<RepastEdge>) net.getEdges(obj)) {
        PEdge pEdge = (PEdge) display.getVisualItem(edge);
        edges.add(pEdge);
      }
    }
    return edges;
  }
}
