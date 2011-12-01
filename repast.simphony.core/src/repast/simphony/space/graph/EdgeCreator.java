package repast.simphony.space.graph;

/**
 * Factory class for creating edges.
 *
 * @author Nick Collier
 */
public interface EdgeCreator<E extends RepastEdge, T> {

  /**
   * Gets the edge type produced by this EdgeCreator.
   *
   * @return the edge type produced by this EdgeCreator.
   */
  Class getEdgeType();


  /**
   * Creates an Edge with the specified source, target, direction and weight.
   *
   * @param source the edge source
   * @param target the edge target
   * @param isDirected whether or not the edge is directed
   * @param weight the weight of the edge
   * @return the created edge.
   */
  E createEdge(T source, T target, boolean isDirected, double weight);

}
