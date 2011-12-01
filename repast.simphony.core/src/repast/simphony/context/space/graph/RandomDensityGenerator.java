package repast.simphony.context.space.graph;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import edu.uci.ics.jung.algorithms.util.Indexer;

/**
 * Generates a random network with a specified approximate density. The network is
 * created by looping over all i, j node pairs and deciding on the existence
 * of a link between the nodes by comparing the value of a probability to a uniform
 * random number.  If the boolean allowLoops is false, no self loops
 * (links from i to itself) will be permitted.  If the boolean isSymmetric is
 * true, all ties will be bidirectional (i -> j = j -> i). This is what is
 * generally referred to in the network literature as "random" network -
 * a class of networks which have been well studied analytically, but which
 * are structurally quite unlike most empirically observed "social" networks.
 *
 * @author Nick Collier
 */
public class RandomDensityGenerator<T> extends AbstractGenerator<T> {

  private double density;
  private boolean loops, isSymmetric;
  private BidiMap<T, Integer> map;

  /**
   * Creates a random network.
   *
   * @param density        the approximate desnity of the network
   * @param allowSelfLoops whether or not self loops are allowed in
   *                       the created network
   * @param symmetric      whether or not ties will be bidirectional in the
   *                       created network
   */
  public RandomDensityGenerator(double density, boolean allowSelfLoops, boolean symmetric) {
    this.loops = allowSelfLoops;
    this.isSymmetric = symmetric;
    this.density = density;
    if (density > 1.0 || density < 0.0) {
      msg.error("Error creating RandomDensityNetworkGenerator",
              new IllegalArgumentException("Density must be between 0 and 1."));
    }
  }

  /**
   * Add edges to the existing network to create a random density network.
   *
   * @param network the network to add edges to
   * @return the random network
   */
  public Network<T> createNetwork(Network<T> network) {
    boolean isDirected = network.isDirected();
    init(network);
    if (isDirected) {
      if (loops && isSymmetric) return symmetricLoops(network);
      else if (!loops && !isSymmetric) return nonSymmetricNoLoops(network);
      else if (loops) return nonSymmetricLoops(network);
      else return symmetricNoLoops(network);
    } else {
      if (loops) return nonSymmetricLoops(network);
      else return nonSymmetricNoLoops(network);
    }
  }

  private Network<T> symmetricLoops(Network<T> network) {
    for (int i = 0, n = network.size(); i < n; i++) {
      T source = map.getKey(i);
      for (int j = i; j < n; j++) {
        if (RandomHelper.nextDouble() < density) {
          T target = map.getKey(j);
          network.addEdge(source, target);
          network.addEdge(target, source);
        }
      }
    }

    return network;
  }

  private void init(Network<T> network) {
    Set<T> set = new HashSet<T>();
    for (T node : network.getNodes()) {
      set.add(node);
    }

    map = Indexer.create(set);
  }

  private Network<T> nonSymmetricNoLoops(Network<T> network) {
    for (int i = 0, n = network.size(); i < n; i++) {
      T source = map.getKey(i);
      for (int j = i + 1; j < n; j++) {
        if (RandomHelper.nextDouble() < density) {
          T target = map.getKey(j);
          network.addEdge(source, target);
        }
      }
    }

    return network;
  }

  private Network<T> nonSymmetricLoops(Network<T> network) {
    for (int i = 0, n = network.size(); i < n; i++) {
      T source = map.getKey(i);
      for (int j = i; j < n; j++) {
        if (RandomHelper.nextDouble() < density) {
          T target = map.getKey(j);
          network.addEdge(source, target);
        }
      }
    }

    return network;
  }

  private Network<T> symmetricNoLoops(Network<T> network) {
    for (int i = 0, n = network.size(); i < n; i++) {
      T source = map.getKey(i);
      for (int j = i + 1; j < n; j++) {
        if (RandomHelper.nextDouble() < density) {
          T target = map.getKey(j);
          network.addEdge(source, target);
          network.addEdge(target, source);
        }
      }
    }

    return network;
  }
}
