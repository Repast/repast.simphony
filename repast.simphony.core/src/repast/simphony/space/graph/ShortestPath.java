package repast.simphony.space.graph;

import java.util.List;

import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;

/**
 * Calculates the shortest path from a specified node to all other nodes in the
 * net using Dijkstra's algorithm.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 * @version $Revision$ $Date$
 */

public class ShortestPath<T> implements ProjectionListener<T> {

	private Network<T> net;
	private boolean calc = true;
	private T source;
  private JungEdgeTransformer transformer;
  private DijkstraShortestPath<T,RepastEdge<T>> dsp;
  
  /**
   * Constructor
   * 
   * @param net the Network
   */
  
    public ShortestPath(){
    	
    }
	public ShortestPath(Network<T> net){
		init(net);
	}
	
	/**
	 * Creates shortest path info from the specified source to all other nodes
	 * in the specified network.
	 * 
	 * @deprecated  As of release 1.2, replaced by {@link #ShortestPath(Network<T> net)}
	 * @param net the network
	 * @param source the source node
	 */
	@Deprecated
	public ShortestPath(Network<T> net, T source) {
		this.source = source;
	  init(net);	
	}
	
	
	private void init(Network<T> net){
		this.net = net;
		transformer = new JungEdgeTransformer<T>();
		net.addProjectionListener(this);
	}

	/**
	 * Returns a list of RepastEdges in the shortest path from source to target.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public List<RepastEdge<T>> getPath(T source, T target){
		
		if (calc){
			calcPaths();
			calc = false;
		}
		return dsp.getPath(source, target); 
	}
	
	/**
	 * Gets the path length from the source node to the target node.
	 * 
	 * @param source the node we want to get the path length from
	 * @param target the node we want to get the path length to
	 * @return the path length from the source node to the target node.
	 */
	public double getPathLength(T source, T target){
		if (calc){
			calcPaths();
			calc = false;
		}
		
		Number n = dsp.getDistance(source, target);

		if (n != null)
			return n.doubleValue();
		else
			return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Gets the path length from the source node specified in the constructor
	 *  to the target node.
	 * 
	 * @deprecated  As of release 1.2, replaced by {@link #getPathLength(T source, T target)}
	 * @param target the node we want to get the path length to
	 * @return the path length from the source node to the target node.
	 */
	@Deprecated
	public double getPathLength(T target){
		return getPathLength(this.source, target);
	}
	
	/**
	 * Creates shortest path info  nodes using the Jung Dijkstra algorithm
	 */
	private void calcPaths(){
		Graph<T, RepastEdge<T>> graph = null;
		
		if (net instanceof JungNetwork)
			graph = ((JungNetwork)net).getGraph();
		else if (net instanceof ContextJungNetwork)
			graph = ((ContextJungNetwork)net).getGraph();
		
		dsp = new DijkstraShortestPath<T,RepastEdge<T>>(graph, transformer);
	}
	
	/**
	 * Called when the network is modified so that this will recalculate the
	 * shortest path info.
	 * 
	 * @param evt
	 */
	public void projectionEventOccurred(ProjectionEvent<T> evt) {
		if (evt.getType() != ProjectionEvent.OBJECT_MOVED) {
			calc = true;
		}
	}
	
	/**
	 * Removes this as a projection listener when this ShortestPath is garbage
	 * collected.
	 */
	public void finalize() {
		if (net != null)
			net.removeProjectionListener(this);
	}
	
	/**
	 * Null the object so that the Garbage Collector recognizes to remove 
	 * the object from the jvm.
	 */
	public static ShortestPath finished(ShortestPath<?> sp){
		sp.finalize();
		sp=null;
		return sp;
	}
}