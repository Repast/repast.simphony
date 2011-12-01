package repast.simphony.space.graph;

import repast.simphony.space.projection.Projection;

/**
 * Interface for Network projections.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface Network<T> extends Projection<T> {
  /**
   * @return true if this Network is directed, otherwise false.
   */
  boolean isDirected();

  /**
   * Gets the EdgeCreator used to create edges for
   * this Network. {@link #addEdge(Object, Object) addEdge} and
   * {@link #addEdge(Object, Object, double) addEdge} will use
   * this creator to create edges. Any edge added with
   * {@link #addEdge(RepastEdge) addEdge} must be of the same
   * type as that created with this EdgeCreator. By default,
   * an edge creator that creates RepastEdges is used.
   * <p/>
   * The default EdgeCreator will create
   * RepastEdge
   *
   * @return the edge class of this network
   */
  EdgeCreator<? extends RepastEdge<T>, T> getEdgeCreator();

  /**
   * Adds an edge between the specified objects.
   *
   * @param source the source object
   * @param target the target object
   * @return the created edge.
   */
  RepastEdge<T> addEdge(T source, T target);

  /**
   * Adds an edge between the specified objects.
   *
   * @param source the source object
   * @param target the target object
   * @param weight weight of the new edge
   * @return the created edge.
   */
  RepastEdge<T> addEdge(T source, T target, double weight);

  /**
   * Adds the specified edge to this Network. This will change the
   * directionality of the edge to met that of the network.
   *
   * @param edge the edge to add.
   * @return the added edge.
   */
  RepastEdge<T> addEdge(RepastEdge<T> edge);

  /**
   * Retrieves the edge between the specified source and target. If multiple
   * edges exist between these two objects, the first found will be returned.
   *
   * @param source the source of the edge
   * @param target the target of the edge
   * @return an edge or null if no such edge exists
   */
  RepastEdge<T> getEdge(T source, T target);

  /**
   * Removes the specified edge from this Network.
   *
   * @param edge the edge to remove
   */
  void removeEdge(RepastEdge<T> edge);

  /**
   * Returns whether or not this network contains the specified edge.
   *
   * @param edge the edge to check
   * @return true if the network contains the specified edge, otherwise false.
   */
  boolean containsEdge(RepastEdge<T> edge);

  /**
   * Gets the number of nodes in this network.
   *
   * @return the number of nodes in this network.
   */
  int size();

  /**
   * Gets the number of edges in this Network.
   *
   * @return the number of edges in this Network.
   */
  int numEdges();

  /**
   * Gets the predecessors of the specified object. If the network is directed
   * then the predecessors are any objects connected to the specified object
   * by an edge where the specified object is the target of that edge. If the
   * network is undirected, then this returns any object connected to the
   * specified object.
   *
   * @param agent the object whose predecessors should be returned
   * @return an iterator over the predecessors of the specified object.
   */
  Iterable<T> getPredecessors(T agent);

  /**
   * Gets the successors of the specified object. If the network is directed
   * then the successors are any objects connected to the specified object by
   * an edge where the specified object is the source of that edge. If the
   * network is undirected, then this returns any object connected to the
   * specified object.
   *
   * @param agent the object whose successors should be returned
   * @return an iterator over the successors of the specified object.
   */
  Iterable<T> getSuccessors(T agent);

  /**
   * Gets any objects that are adjacent to this object. An object is adjacent
   * if it has an edge either from or to the specified object.
   *
   * @param agent the object whose adjacent objects should be returned
   * @return an iterator over all the objects adjacent to the specified
   *         object.
   */
  Iterable<T> getAdjacent(T agent);

  /**
   * Get a random predecessor of the specified object. If the network is
   * directed then the predecessors are any objects connected to the specified
   * object by an edge where the specified object is the target of that edge.
   * If the network is undirected, then a predecessor can be any object
   * connected to the specified object.
   *
   * @param agent
   * @return a random predecessor of the specified object or null if there are
   *         no predecessors.
   */
  T getRandomPredecessor(T agent);

  /**
   * Gets a random successor of the specified object. If the network is
   * directed then the successors are any objects connected to the specified
   * object by an edge where the specified object is the source of that edge.
   * If the network is undirected, then a successor can be any object
   * connected to the specified object.
   *
   * @param agent
   * @return a random predecessor of the specified object or null if there are
   *         no successors.
   */
  T getRandomSuccessor(T agent);

  /**
   * Gets a random object that is adjacent to the specified object.
   *
   * @param agent
   * @return a random object that is adjacent to the specified object or null
   *         if no objects are adjacent.
   */
  T getRandomAdjacent(T agent);

  /**
   * Returns true if the first object is a predecessor of the second. If the
   * network is directed then the predecessors are any objects connected to
   * the specified object by an edge where the specified object is the target
   * of that edge. If the network is undirected, then a predecessor can be any
   * object connected to the specified object.
   *
   * @param first
   * @param second
   * @return true if the first object is a predecessor of the second.
   */
  boolean isPredecessor(T first, T second);

  /**
   * Returns true if the first object is a successor of the second. If the
   * network is directed then the successors are any objects connected to the
   * specified object by an edge where the specified object is the source of
   * that edge. If the network is undirected, then a successor can be any
   * object connected to the specified object.
   *
   * @param first
   * @param second
   * @return true if the first object is a successor of the second.
   */
  boolean isSuccessor(T first, T second);

  /**
   * Returns true if the first object is adjacent to the second. An object is
   * adjacent to another if there is an edge between the first object and the
   * second, regardeless of the edges directionality.
   *
   * @param first
   * @param second
   * @return true if the first object is a adjacent to the second.
   */
  boolean isAdjacent(T first, T second);

  /**
   * Gets the degree of the node associated with the specified object.
   *
   * @param agent the object whose node's degree we want.
   * @return the degree of the node associated with the specified object.
   */
  int getDegree(T agent);

  /**
   * Gets all the in-edges for the specified object. In a directed network an
   * in edge is any edge where the specified object is the target. In an
   * undirected network, edges are both in- and out-, and so all edges are
   * returned.
   *
   * @param agent the object whose in-edges should be returned
   * @return an iterator over all the in-edges for the specified object.
   */
  Iterable<RepastEdge<T>> getInEdges(T agent);

  /**
   * Gets all the out-edges for the specified object. In a directed network an
   * out edge is any edge where the specified object is the source. In an
   * undirected network, edges are both in- and out-, and so all edges are
   * returned.
   *
   * @param agent the object whose out-edges should be returned
   * @return an iterator over all the out-edges for the specified object.
   */
  Iterable<RepastEdge<T>> getOutEdges(T agent);

  /**
   * Gets all the edges where the specified object is a source or a target.
   *
   * @param agent the object whose edges should be returned
   * @return an iterator over all the edges where the specified object is a
   *         source or a target.
   */
  Iterable<RepastEdge<T>> getEdges(T agent);

  /**
   * Gets all the edges in this Network.
   *
   * @return an iterator over all the edges in this Network.
   */
  Iterable<RepastEdge<T>> getEdges();

  /**
   * Gets an iterator over all the agent nodes in this network.
   *
   * @return an iterator over all the agent nodes in this network.
   */
  Iterable<T> getNodes();

  /**
   * Gets the in degree of the node associated with the specified object.
   *
   * @param agent the object whose node's in degree we want.
   * @return the in degree of the node associated with the specified object.
   */
  int getInDegree(T agent);

  /**
   * Gets the out degree of the node associated with the specified object.
   *
   * @param agent the object whose node's out degree we want.
   * @return the out degree of the node associated with the specified object.
   */
  int getOutDegree(T agent);

   /**
   * Get the total degree (number of edges) for the graph.
   *
   * @return the total degree (number of edges) for the graph.
   */
	public int getDegree();
	
	/**
	 * Method removes all edges in the given network.
	 */
	public void removeEdges();
}
