package repast.simphony.graph;

import repast.simphony.space.graph.RepastEdge;

/**
 * @author Nick Collier
*         Date: Jan 15, 2008 2:15:14 PM
*/
public class TestEdge extends RepastEdge {

  public TestEdge(Object source, Object target, boolean directed) {
    super(source, target, directed);
  }

  public TestEdge(Object source, Object target, boolean directed, double weight) {
    super(source, target, directed, weight);
  }
}
