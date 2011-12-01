package repast.simphony.relogo;

import repast.simphony.space.graph.EdgeCreator;

public class TrackingEdgeCreator<T> implements EdgeCreator<TrackingEdge<T>, T> {
	/**
	   * Creates an Edge with the specified source, target, direction and weight.
	   *
	   * @param source     the edge source
	   * @param target     the edge target
	   * @param isDirected whether or not the edge is directed
	   * @param weight     the weight of the edge
	   * @return the created edge.
	   */
	  public TrackingEdge<T> createEdge(T source, T target, boolean isDirected, double weight) {
	    return new TrackingEdge<T>(source, target, isDirected, weight);
	  }

	  /**
	   * Gets the edge type produced by this EdgeCreator.
	   *
	   * @return the edge type produced by this EdgeCreator.
	   */
	  public Class<TrackingEdge> getEdgeType() {
	    return TrackingEdge.class;
	  }
	

}
