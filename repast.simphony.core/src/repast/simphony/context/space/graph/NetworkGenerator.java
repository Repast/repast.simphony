package repast.simphony.context.space.graph;

import repast.simphony.space.graph.Network;

/**
 * Interface for classes that take nodes and create links between
 * them to create typical network configurations.
 * 
 * @author Nick Collier
 */
public interface NetworkGenerator<T> {

  /**
   * Creates edges using the nodes in the specified network. The
   * semantics of edge creation is determined by implementing classes.
   * For example, a Small World generator would create edges in a
   * small world configuration.
   *
   * @param network a network containing nodes
   * @return the generated network
   */
  Network<T> createNetwork(Network<T> network);
}
