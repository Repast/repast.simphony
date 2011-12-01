package repast.simphony.context.space.graph;

import edu.uci.ics.jung.graph.Graph;
import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import static repast.simphony.context.ContextEvent.EventType.AGENT_ADDED;
import static repast.simphony.context.ContextEvent.EventType.AGENT_REMOVED;
import repast.simphony.context.ContextListener;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.space.projection.ProjectionPredicate;
import simphony.util.messages.MessageCenter;

import java.util.Collection;

import org.apache.log4j.Level;


public class ContextJungNetwork<T> implements Network<T>, ContextListener<T> {

  JungNetwork<T> network;
  Context<T> context;
  private int hashCode;

  public ContextJungNetwork(JungNetwork<T> net, Context<T> context) {
    this.network = net;

    hashCode = 17;
    hashCode = 37 * hashCode + network.hashCode();
    hashCode = 37 * hashCode + context.hashCode();
    this.context = context;
  }

  public void eventOccured(ContextEvent<T> ev) {
    ContextEvent.EventType type = ev.getType();
    if (type == AGENT_ADDED) {
      addVertex(ev.getTarget());
    } else if (type == AGENT_REMOVED) {
      removeVertex(ev.getTarget());

    } else if (type == ContextEvent.EventType.PROJECTION_ADDED
            && ev.getProjection().equals(this)) {
      this.context = ev.getContext();
      addAll();
    } else if (type == ContextEvent.EventType.PROJECTION_REMOVED
            && ev.getProjection().equals(this)) {
      removeAll();
      context = null;
    }
  }

  protected void addAll() {
    for (T item : context) {
      addVertex(item);
    }
  }

  protected void removeAll() {
    for (T item : context) {
      removeVertex(item);
    }
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
    return network.getEdgeCreator();
  }

  public RepastEdge<T> addEdge(RepastEdge<T> edge) {
	  if(!context.contains(edge.getSource()) || 
			  !context.contains(edge.getTarget())){
		  String message="Source and/or target of the network" +
			" have not been added to the context.";
		  MessageCenter.getMessageCenter(ContextJungNetwork.class).
		  	fireMessageEvent(Level.ERROR,message,new NullPointerException(message));
	  }
	  return network.addEdge(edge);
  }

  public RepastEdge<T> addEdge(T source, T target, double weight) {
    return network.addEdge(source, target, weight);
  }

  public RepastEdge<T> addEdge(T source, T target) {
    return network.addEdge(source, target);
  }

  public void addProjectionListener(ProjectionListener listener) {
    network.addProjectionListener(listener);
  }

  public void addVertex(T vertex) {
    network.addVertex(vertex);
  }

  public boolean equals(Object obj) {
    if (obj instanceof ContextJungNetwork) {
      ContextJungNetwork other = (ContextJungNetwork) obj;

      return network.equals(other.network) && (context == null ? other.context == null : (other.context != null &&
              context.equals(other.context)));
    }

    return false;
  }

  public boolean evaluate(ProjectionPredicate predicate) {
    return network.evaluate(predicate);
  }

  public Iterable<T> getAdjacent(T agent) {
    return network.getAdjacent(agent);
  }

  public int getDegree() {
    return network.getDegree();
  }

  public int getDegree(T agent) {
    return network.getDegree(agent);
  }

  public RepastEdge<T> getEdge(T source, T target) {
    return network.getEdge(source, target);
  }

  public Iterable<RepastEdge<T>> getEdges() {
    return network.getEdges();
  }

  public Iterable<RepastEdge<T>> getEdges(T agent) {
    return network.getEdges(agent);
  }

  public Graph<T, RepastEdge<T>> getGraph() {
    return network.getGraph();
  }

  public int getInDegree(T agent) {
    return network.getInDegree(agent);
  }

  public Iterable<RepastEdge<T>> getInEdges(T agent) {
    return network.getInEdges(agent);
  }

  public String getName() {
    return network.getName();
  }

  public Iterable<T> getNodes() {
    return network.getNodes();
  }

  public int getOutDegree(T agent) {
    return network.getOutDegree(agent);
  }

  public Iterable<RepastEdge<T>> getOutEdges(T agent) {
    return network.getOutEdges(agent);
  }

  public Iterable<T> getPredecessors(T agent) {
    return network.getPredecessors(agent);
  }

  public Collection<ProjectionListener> getProjectionListeners() {
    return network.getProjectionListeners();
  }

  public T getRandomAdjacent(T agent) {
    return network.getRandomAdjacent(agent);
  }

  public T getRandomPredecessor(T agent) {
    return network.getRandomPredecessor(agent);
  }

  public T getRandomSuccessor(T agent) {
    return network.getRandomSuccessor(agent);
  }

  public Iterable<T> getSuccessors(T agent) {
    return network.getSuccessors(agent);
  }

  public int hashCode() {
    return hashCode;
  }

  public boolean isAdjacent(T first, T second) {
    return network.isAdjacent(first, second);
  }

  public boolean isDirected() {
    return network.isDirected();
  }

  public boolean isPredecessor(T first, T second) {
    return network.isPredecessor(first, second);
  }

  public boolean isSuccessor(T first, T second) {
    return network.isSuccessor(first, second);
  }

  public int numEdges() {
    return network.numEdges();
  }

  public void removeEdge(RepastEdge<T> edge) {
    network.removeEdge(edge);
  }

  /**
   * Returns whether or not this network contains the specified edge.
   *
   * @param edge the edge to check
   * @return true if the network contains the specified edge, otherwise false.
   */
  public boolean containsEdge(RepastEdge<T> edge) {
    return network.containsEdge(edge);
  }

  public boolean removeProjectionListener(ProjectionListener listener) {
    return network.removeProjectionListener(listener);
  }

  public void removeVertex(T vertex) {
    network.removeVertex(vertex);
  }

  public void setGraph(Graph<T, RepastEdge<T>> graph) {
    network.setGraph(graph);
  }

  public int size() {
    return network.size();
  }

  public String toString() {
    return network.toString();
  }

  public void removeEdges() {
	  network.removeEdges();
  }
}
