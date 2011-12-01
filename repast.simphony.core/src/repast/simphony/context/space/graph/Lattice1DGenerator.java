package repast.simphony.context.space.graph;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.space.graph.Network;
import edu.uci.ics.jung.algorithms.util.Indexer;

/**
 * Creates a n x 1 lattice where each vertex is incident to
 * two neighbors (except at the edges if the lattice is non-toroidal.)
 * <p/>
 * Based on Jung Lattice1DGenerator
 *
 * @author Nick Collier
 * @author Jung Project
 */
public class Lattice1DGenerator<T> extends AbstractGenerator<T> {

  private boolean isToroidal, isSymmetrical;
  private int numNodes;

  /**
   * Creates a Lattice1DGenerator.
   *
   * @param toroidal    whether lattice wraps or not.
   * @param symmetrical whether the lattice is symmetrical (edges are bi-directional).
   *                    Note this only matters for a directed network.
   */
  public Lattice1DGenerator(boolean toroidal, boolean symmetrical) {
    isToroidal = toroidal;
    isSymmetrical = symmetrical;
  }

  /**
   * Given an existing network, add edges to create a
   * n x 1 lattice. The lattice dimension n is the number of nodes in
   * the specified network.
   *
   * @param network the network to rewire
   * @return the created network
   */
  public Network<T> createNetwork(Network<T> network) {
    numNodes = network.size();
    Set<T> set = new HashSet<T>();
    for (T node : network.getNodes()) {
      set.add(node);
    }

    int upI, downI;
    BidiMap<T, Integer> map = Indexer.create(set);
    boolean isDirected = network.isDirected();

    for (int i = 0; i < numNodes; i++) {
      T source = map.getKey(i);
      upI = upIndex(i);
      if (upI != -1) {
        T target = map.getKey(upI);
        if (isDirected)
          network.addEdge(source, target);
        else if (!network.isAdjacent(source, target))
          network.addEdge(source, target);
      }

      if (isDirected && isSymmetrical) {
        downI = downIndex(i);
        if (downI != -1) {
          T target = map.getKey(downI);
          if (isDirected)
            network.addEdge(source, target);
          else if (!network.isAdjacent(source, target))
            network.addEdge(source, target);
        }
      }
    }

    return network;
  }

  // returns -1 if no valid index
  protected int downIndex(int currentIndex) {
    if (currentIndex == 0) {
      if (isToroidal) return numNodes - 1;
      else return -1;

    }
    return currentIndex - 1;
  }


  // returns -1 if no valid index
  private int upIndex(int currentIndex) {
    if (currentIndex == numNodes - 1) {
      if (isToroidal) return 0;
      else return -1;
    }
    return currentIndex + 1;
  }


}
