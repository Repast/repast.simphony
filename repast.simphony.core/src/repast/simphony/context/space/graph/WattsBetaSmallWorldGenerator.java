package repast.simphony.context.space.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import edu.uci.ics.jung.algorithms.util.Indexer;

/**
 * WattsBetaSmallWorldGenerator is a graph generator that produces a small
 * world network using the beta-model as proposed by Duncan Watts. The basic ideas is
 * to start with a one-dimensional ring lattice in which each vertex has k-neighbors and then randomly
 * rewire the edges, with probability beta, in such a way that a small world networks can be created for
 * certain values of beta and k that exhibit low charachteristic path lengths and high clustering coefficient.
 *
 * @author Jung Project
 * @author Nick Collier
 * @see "Small Worlds:The Dynamics of Networks between Order and Randomness by D.J. Watts"
 *      <p/>
 *      This is based on the Jung Project code.
 */
public class WattsBetaSmallWorldGenerator<T> extends AbstractGenerator<T> {

  private double beta;
  private int degree, numNodes;
  private boolean isSymmetrical;

  /**
   * Constructs the small world graph generator.
   *
   * @param beta        the probability of an edge being rewired randomly; the proportion of randomly
   *                    rewired edges in a graph. Must be between 0 and 1.
   * @param degree      the number of edges connected to each vertex around the local neighborhood. This is the
   *                    local ngh size. Must be an even number.
   * @param symmetrical whether or not the generated edges will be symmetrical. This has no effect on a
   *                    non-directed network.
   */
  public WattsBetaSmallWorldGenerator(double beta, int degree, boolean symmetrical) {
    this.isSymmetrical = symmetrical;
    if (degree % 2 != 0) {
      msg.error("Error creating WattsBetaSmallWorldGenerator",
              new IllegalArgumentException("All nodes must have an even degree."));
    }
    if (beta > 1.0 || beta < 0.0) {
      msg.error("Error creating WattsBetaSmallWorldGenerator",
              new IllegalArgumentException("Beta must be between 0 and 1."));
    }

    this.beta = beta;
    this.degree = degree;
  }

  /**
   * Generates a beta-network from a 1-lattice according to the parameters given.
   *
   * @return a beta-network model that is potentially a small-world
   */
  public Network<T> createNetwork(Network<T> network) {
    numNodes = network.size();
    if (numNodes < 10) msg.error("Error creating Watts beta small world network",
            new IllegalArgumentException("Number of nodes must be greater than 10"));


    Set<T> set = new HashSet<T>();
    for (T node : network.getNodes()) {
      set.add(node);
    }
    BidiMap<T, Integer> map = Indexer.create(set);

    boolean isDirected = network.isDirected();
    int numKNeighbors = degree / 2;

    // create the lattice
    for (int i = 0; i < numNodes; i++) {
      for (int s = 1; s <= numKNeighbors; s++) {
        T source = map.getKey(i);
        int upI = upIndex(i, s);
        T target = map.getKey(upI);
        network.addEdge(source, target);
        if (isDirected && isSymmetrical) {
          network.addEdge(target, source);
        }
      }
    }

    List<RepastEdge<T>> edges = new ArrayList<RepastEdge<T>>();
    for (RepastEdge<T> edge : network.getEdges()) {
      edges.add(edge);
    }

    Set<RepastEdge<T>> removedEdges = new HashSet<RepastEdge<T>>();

    for (RepastEdge<T> edge : edges) {
      if (!removedEdges.contains(edge)) {
        if (beta > RandomHelper.nextDouble()) {
          int rndIndex = RandomHelper.nextIntFromTo(0, numNodes - 1);
          T randomNode = map.getKey(rndIndex);
          T source = edge.getSource();
          if (!source.equals(randomNode) && !network.isPredecessor(source, randomNode)) {
            network.removeEdge(edge);
            network.addEdge(source, randomNode);

            if (isDirected && isSymmetrical) {
              // remove the t -> s edge
              T target = edge.getTarget();
              RepastEdge<T> otherEdge = network.getEdge(target, source);
              network.removeEdge(otherEdge);
              removedEdges.add(otherEdge);

              // add the randomNode -> edge
              network.addEdge(randomNode, source);
            }
          }
        }
      }
    }

    return network;
  }

  /**
   * Determines the index of the neighbor ksteps above
   *
   * @param numSteps     is the number of steps away from the current index that is being considered.
   * @param currentIndex the index of the selected vertex.
   */
  private int upIndex(int currentIndex, int numSteps) {

    int value = currentIndex + numSteps;
    if (value > numNodes - 1) return value % numNodes;
    return value;
  }

}
