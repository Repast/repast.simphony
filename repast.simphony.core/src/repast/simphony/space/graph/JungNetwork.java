package repast.simphony.space.graph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.projection.DefaultProjection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionPredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public abstract class JungNetwork<T> extends DefaultProjection<T> implements
		Network<T> {

	protected Graph<T, RepastEdge<T>> graph;
	private ArrayList<T> tmpRandomList = new ArrayList<T>();
  protected EdgeCreator<? extends RepastEdge<T>, T> creator;

  public JungNetwork(String name) {
		this(name, new DefaultEdgeCreator<T>());
	}

  public JungNetwork(String name, EdgeCreator<? extends RepastEdge<T>, T> creator) {
    super(name);
    this.creator = creator;
  }

  /**
   * Gets the EdgeCreator used to create edges for
   * this Network. {@link #addEdge(Object, Object) addEdge} and
   * {@link #addEdge(Object, Object, double) addEdge} will use
   * this creator to create edges. Any edge added with
   * {@link #addEdge(repast.simphony.space.graph.RepastEdge) addEdge} must be of the same
   * type as that created with this EdgeCreator. By default,
   * an edge creator that creates RepastEdges is used.
   * <p/>
   * The default EdgeCreator will create
   * RepastEdge
   *
   * @return the edge class of this network
   */
  public EdgeCreator<? extends RepastEdge<T>, T> getEdgeCreator() {
    return creator;
  }

  public void setGraph(Graph<T, RepastEdge<T>> graph) {
		this.graph = graph;
	}

	public Graph<T, RepastEdge<T>> getGraph() {
		return graph;
	}

	public boolean evaluate(ProjectionPredicate predicate) {
		return predicate.evaluate(this);
	}

	public Iterable<T> getAdjacent(T agent) {
		Collection<T> out = new LinkedHashSet<T>();
		out.addAll(graph.getPredecessors(agent));
		out.addAll(graph.getSuccessors(agent));
		return out;
	}

	public int getDegree() {
		return graph.getEdges().size();
	}

	public int getDegree(T agent) {
		return graph.degree(agent);
	}

	public Iterable<RepastEdge<T>> getEdges() {
		return graph.getEdges();
	}

	public Iterable<RepastEdge<T>> getEdges(T agent) {
		return graph.getIncidentEdges(agent);
	}

	public int getInDegree(T agent) {
		return graph.inDegree(agent);
	}

	public Iterable<RepastEdge<T>> getInEdges(T agent) {
		return graph.getInEdges(agent);
	}

	public Iterable<T> getNodes() {
		return graph.getVertices();
	}

	public int getOutDegree(T agent) {
		return graph.outDegree(agent);
	}

	public Iterable<RepastEdge<T>> getOutEdges(T agent) {
		return graph.getOutEdges(agent);
	}

	public Iterable<T> getPredecessors(T agent) {
		return graph.getPredecessors(agent);
	}

	public T getRandomAdjacent(T agent) {
		int size = graph.getNeighbors(agent).size();
		if (size == 0) {
			return null;
		}
		int index = RandomHelper.getUniform().nextIntFromTo(0, size - 1);
		tmpRandomList.clear();
		tmpRandomList.addAll(graph.getNeighbors(agent));
		return tmpRandomList.get(index);
	}

	public T getRandomPredecessor(T agent) {
		int size = graph.getPredecessors(agent).size();
		if (size == 0) {
			return null;
		}
		int index = RandomHelper.getUniform().nextIntFromTo(0, size - 1);
		tmpRandomList.clear();
		tmpRandomList.addAll(graph.getPredecessors(agent));
		return tmpRandomList.get(index);
	}

	public T getRandomSuccessor(T agent) {
		int size = graph.getSuccessors(agent).size();
		if (size == 0) {
			return null;
		}
		int index = RandomHelper.getUniform().nextIntFromTo(0, size - 1);
		tmpRandomList.clear();
		tmpRandomList.addAll(graph.getSuccessors(agent));
		return tmpRandomList.get(index);
	}

	public Iterable<T> getSuccessors(T agent) {
		return graph.getSuccessors(agent);
	}

	public boolean isAdjacent(T first, T second) {
		return graph.isNeighbor(first, second)
				|| graph.isNeighbor(second, first);
	}

	public boolean isPredecessor(T first, T second) {
		return graph.isPredecessor(second, first);
	}

	public boolean isSuccessor(T first, T second) {
		return graph.isSuccessor(second, first);
	}

	public int numEdges() {
		return graph.getEdges().size();
	}

	public void removeEdge(RepastEdge<T> edge) {
	        if (graph.removeEdge(edge)) {
	          fireProjectionEvent(new ProjectionEvent<T>(this, edge,
    				ProjectionEvent.EDGE_REMOVED));
	        }
	}

  /**
   * Returns whether or not this network contains the specified edge.
   *
   * @param edge the edge to check
   * @return true if the network contains the specified edge, otherwise false.
   */
  public boolean containsEdge(RepastEdge<T> edge) {
    return graph.containsEdge(edge);
  }

  public RepastEdge<T> addEdge(RepastEdge<T> edge) {
    RepastEdge<T> oldEdge = graph.findEdge(edge.getSource(), edge.getTarget());
    if (oldEdge != null) removeEdge(oldEdge);
    graph.addEdge(edge, edge.getSource(), edge.getTarget());
		fireProjectionEvent(new ProjectionEvent<T>(this, edge,
				ProjectionEvent.EDGE_ADDED));
		return edge;
	}
	
  public RepastEdge<T> addEdge(RepastEdge<T> edge, EdgeType type) {
	RepastEdge<T> oldEdge = graph.findEdge(edge.getSource(), edge.getTarget());
    if (oldEdge != null) removeEdge(oldEdge);
    graph.addEdge(edge, edge.getSource(), edge.getTarget(), type);
		fireProjectionEvent(new ProjectionEvent<T>(this, edge,
				ProjectionEvent.EDGE_ADDED));
		return edge;
	}

	public RepastEdge<T> addEdge(T source, T target) {
		return addEdge(source, target, 1);
	}

	public RepastEdge<T> getEdge(T source, T target) {
		return graph.findEdge(source, target);
	}

	public int size() {
		return graph.getVertices().size();
	}

	public void addVertex(T vertex) {
		graph.addVertex(vertex);
		fireProjectionEvent(new ProjectionEvent<T>(this, vertex,
				ProjectionEvent.OBJECT_ADDED));
	}

	public void removeVertex(T vertex) {
    Collection<RepastEdge<T>> edges = graph.getIncidentEdges(vertex);
    
    ArrayList<RepastEdge<T>> tempEdges = new ArrayList<RepastEdge<T>>();
    for (RepastEdge<T> edge : edges)
    	tempEdges.add(edge);
      
		graph.removeVertex(vertex);
		
		fireProjectionEvent(new ProjectionEvent<T>(this, vertex,ProjectionEvent.OBJECT_REMOVED));
		
		for (RepastEdge<T> edge : tempEdges) 	
			fireProjectionEvent(new ProjectionEvent<T>(this, edge,ProjectionEvent.EDGE_REMOVED));
  }
	
	public void removeEdges() {
		Iterator<RepastEdge<T>> ie = this.getEdges().iterator();
		 while(ie.hasNext()){
			 this.removeEdge(ie.next());
			 ie=this.getEdges().iterator();
		 }
	}
}