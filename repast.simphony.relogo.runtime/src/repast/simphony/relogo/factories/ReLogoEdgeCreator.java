package repast.simphony.relogo.factories;

import repast.simphony.relogo.BaseLink;
import repast.simphony.relogo.Link;
import repast.simphony.space.graph.EdgeCreator;

public class ReLogoEdgeCreator implements EdgeCreator {
	
	LinkFactory lF;
	Class<? extends BaseLink> linkClass;
	
	public ReLogoEdgeCreator(LinkFactory lF, Class<? extends BaseLink> linkClass){
		this.lF = lF;
		this.linkClass = linkClass;
	}
	/**
	   * Creates an Edge with the specified source, target, direction and weight.
	   *
	   * @param source     the edge source
	   * @param target     the edge target
	   * @param isDirected whether or not the edge is directed
	   * @param weight     the weight of the edge
	   * @return the created edge.
	   */
	  public Link createEdge(Object source, Object target, boolean isDirected, double weight) {
	    return lF.createLink(linkClass, source, target, isDirected, weight);
	  }

	  /**
	   * Gets the edge type produced by this EdgeCreator.
	   *
	   * @return the edge type produced by this EdgeCreator.
	   */
	  public Class getEdgeType() {
	    return linkClass;
	  }

}