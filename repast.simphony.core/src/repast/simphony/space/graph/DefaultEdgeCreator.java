package repast.simphony.space.graph;

/**
 * Default implementation of EdgeCreator that return RepastEdge.
 *
 * @author Nick Collier
 */
public class DefaultEdgeCreator<T> implements EdgeCreator<RepastEdge<T>, T> {

  /**
   * Creates an Edge with the specified source, target, direction and weight.
   *
   * @param source     the edge source
   * @param target     the edge target
   * @param isDirected whether or not the edge is directed
   * @param weight     the weight of the edge
   * @return the created edge.
   */
  public RepastEdge<T> createEdge(T source, T target, boolean isDirected, double weight) {
    return new RepastEdge<T>(source, target, isDirected, weight);
  }

  /**
   * Gets the edge type produced by this EdgeCreator.
   *
   * @return the edge type produced by this EdgeCreator.
   */
  public Class<RepastEdge> getEdgeType() {
    return RepastEdge.class;
  }
}
