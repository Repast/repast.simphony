package repast.simphony.visualization.visualization2D.layout;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.AbstractNetworkLayout;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.visualization2D.Display2D;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;

/**
 * A Tree layout.
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class TreeLayout2D<T> extends AbstractNetworkLayout<T>{

	protected TreeLayout<T, RepastEdge<T>> layout;
	protected Network<T> network;
	
	public TreeLayout2D(){
		super();	
	}
	
	@Override
	public void setProjection(Network<T> projection) {
		this.baseGraph = projection;
		
		network = this.baseGraph;
			  
	  setLayout();
	}
	
	@Override
	public void update(){
		if (network!=null)
			setProjection(network);	
	}
	
	/**
	 * A Tree layout expects the graph to directed and acyclic, with a root node 
	 * and children nodes.  Isolated nodes without edges are pruned.  
	 */
	protected void setLayout(){
		// TODO warn for undirected graphs?
		
		if (!network.isDirected() || network.numEdges() < 1)
			return;
				
		DelegateTree<T, RepastEdge<T>> tree = new DelegateTree();
		
		// For each node in the network, determine if the node is a root, child,
		// or isolated node.  Isolated nodes (without edges) are pruned for display.
		for (T t : network.getNodes()){
			if (network.getDegree(t) != 0){
				if (!network.getPredecessors(t).iterator().hasNext())
					tree.setRoot(t);
				
				for (T child : network.getSuccessors(t))
					tree.addChild(network.getEdge(t, child), t, child);
				
			}
		}
		
		layout = new TreeLayout<T, RepastEdge<T>>(tree);
	}
	
	public float[] getLocation(T obj) {
		float[] location = new float[]{0,0};
	
		if (layout == null){
		  location[0] = (float)	Display2D.DISPLAY_WIDTH / 2;
		  location[1] = (float) Display2D.DISPLAY_HEIGHT / 2;
		}
		else{
		  location[0] = (float)	layout.transform(obj).getX();
		  location[1] = (float) -layout.transform(obj).getY();
		}
		return location;
	}

  /* (non-Javadoc)
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    return new Box();
  }
	
	
}