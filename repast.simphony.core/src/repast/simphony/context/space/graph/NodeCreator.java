package repast.simphony.context.space.graph;

/**
 * Creates agents to be used as nodes in a network. This is
 * intended to be used for automated network creation that
 * also creates the nodes in the network.
 *
 * @author Nick Collier
 */
public interface NodeCreator<T> {

  /**
   * Creates and returns a node to be added to a network
   * via a context.
   *
   * @param label the node label. If the node label does not exist
   *              this will be an empty string
   * @return the created Node.
   */
  T createNode(String label);
}
