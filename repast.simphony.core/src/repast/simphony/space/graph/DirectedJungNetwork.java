package repast.simphony.space.graph;

import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class DirectedJungNetwork<T> extends JungNetwork<T> {

  public DirectedJungNetwork(String name) {
    this(name, new DefaultEdgeCreator<T>());
  }

  public DirectedJungNetwork(String name, EdgeCreator<? extends RepastEdge<T>, T> creator) {
    super(name, creator);
    graph = new DirectedOrderedSparseMultigraph<T, RepastEdge<T>>();
  }

  @Override
  public RepastEdge<T> addEdge(RepastEdge<T> edge) {
    edge.setDirected(true);
    return super.addEdge(edge, EdgeType.DIRECTED);
  }

  public RepastEdge<T> addEdge(T source, T target, double weight) {
    RepastEdge<T> edge = creator.createEdge(source, target, true, weight);
    return addEdge(edge);
  }


  public boolean isDirected() {
    return true;
  }

}
