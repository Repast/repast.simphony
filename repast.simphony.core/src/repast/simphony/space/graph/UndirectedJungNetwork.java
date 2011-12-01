package repast.simphony.space.graph;

import edu.uci.ics.jung.graph.UndirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class UndirectedJungNetwork<T> extends JungNetwork<T> {

  public UndirectedJungNetwork(String name) {
    this(name, new DefaultEdgeCreator<T>());
  }

  public UndirectedJungNetwork(String name, EdgeCreator<? extends RepastEdge<T>, T> creator) {
    super(name, creator);
    graph = new UndirectedOrderedSparseMultigraph<T, RepastEdge<T>>();
  }

  @Override
  public RepastEdge<T> addEdge(RepastEdge<T> edge) {
    edge.setDirected(false);

    return super.addEdge(edge, EdgeType.UNDIRECTED);

  }

  public RepastEdge<T> addEdge(T source, T target, double weight) {
    RepastEdge<T> edge = creator.createEdge(source, target, false, weight);
    addEdge(edge);
    return edge;
  }

  @Override
  public int getDegree(T agent) {
    return graph.getIncidentEdges(agent).size();
  }

  @Override
  public int getInDegree(T agent) {
    return getDegree(agent);
  }

  @Override
  public int getOutDegree(T agent) {
    return getDegree(agent);
  }


  public boolean isDirected() {
    return false;
  }

}
