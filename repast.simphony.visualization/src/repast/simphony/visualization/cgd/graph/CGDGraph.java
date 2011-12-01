package repast.simphony.visualization.cgd.graph;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import repast.simphony.visualization.cgd.algorithm.ClanGraphDecompAlgorithm;
import repast.simphony.visualization.cgd.util.CGDTreeSet;
import repast.simphony.visualization.cgd.util.ParseClanTree;


public class CGDGraph  implements Cloneable {
	ArrayList nodes;
	HashMap edges;
	GraphUtil gu;
	
	public CGDGraph(){
		this(null,null);
	}
	public CGDGraph(ArrayList _nodes, HashMap _edges){
		nodes=_nodes;
		edges=_edges;
		gu=new GraphUtil();
	}
	
	public void addNode(int index){
		CGDNode node=new CGDNode(index);
		node.isDummy=true;
		nodes.add(node);
	}
	public void addDummyNode(int index){
		CGDNode node=new CGDNode(index);
		node.isDummy=true;
		nodes.add(node);
	}
	public void setNodes(ArrayList _nodes){
		nodes=_nodes;
	}
	public ArrayList getNodes(){
		return nodes;
	}
	
	public void setEdges(HashMap _edges){
		edges=_edges;
	}
	public HashMap getEdges(){
		return edges;
	}

	public CGDEdge getEdge(int x,int y){
		CGDEdge edge=null;
		
		edge=(CGDEdge)this.edges.get(new Point(x,y));
		
		return edge;
	}
	
	public CGDNode getNode(int ix){
		CGDNode n=null;
		
		for(int i=0;i<nodes.size();i++){
			n=(CGDNode)nodes.get(i);
			if(n.getIndex()==ix)
				return n;
		}
		return null;
	}

	public Object clone() {
		CGDGraph newGraph;

		try{
			newGraph=(CGDGraph)super.clone();
			newGraph.nodes=new ArrayList();
			newGraph.edges=new HashMap();
			
			for(int i=0;i<this.nodes.size();i++){
				CGDNode n=(CGDNode)this.nodes.get(i);
				CGDNode newN=new CGDNode(n.getIndex(),n.getIdentifier());
				newGraph.nodes.add(newN);
			}
			
			Iterator it=this.edges.keySet().iterator();
			while(it.hasNext()){
				Point p=(Point)it.next();
				CGDEdge e=(CGDEdge)edges.get(p);
				CGDEdge newE=new CGDEdge(e.getSource(),e.getTarget(),e.getIndex(),e.getIdentifier());
				newGraph.edges.put(p,newE);
			}
			
			return newGraph;
		} catch (CloneNotSupportedException e) {}
		
		return null;
	}
	
	public void copy(CGDGraph g){
		// used only for getting back data from initial setup in the case of error.
		this.edges=g.edges;
		this.nodes=g.nodes;
	}
	
	public void compute(){
		ClanGraphDecompAlgorithm alg=new ClanGraphDecompAlgorithm();
		alg.compute(this);
	}
	
	/**
	 * This method assigns a list of children to each node.
	 * Next it stores in the array of TreeSet's (each for each node)
	 * and returns to a caller.
	 * 
	 * @return TreeSet[]
	 */
	public TreeSet[] processChildRelation(){
		TreeSet[] result=new TreeSet[nodes.size()];
		Point edge=null;
		int x,y=0;
		CGDNode node=null;
		
		// Establish a list of children for each node.
		Object[] eArray=edges.keySet().toArray();
		for(int i=0;i<eArray.length;i++){
			edge=(Point)eArray[i];
			node=((CGDEdge)edges.get(edge)).getSource();
			x=(int)edge.getX();
			y=(int)edge.getY();
			if(node!=null){
				node.children.add(y);
			}
		}
		
		System.out.println("maxNIndex = "+CGDNode.maxNIndex+" ; nodes list size = "+nodes.size());
		// Assuming that node indexes follow the order 0-n. 
		for(int j=0;j<nodes.size();j++){
			node=(CGDNode)nodes.get(j);
			result[j]=node.children;
		}
		
		return result;
	}

	public void addEdge(int x, int y){
		CGDNode source=(CGDNode)this.getNode(x);
		CGDNode target=(CGDNode)this.getNode(y);

		if(source==null || target==null){
			System.out.println("CGDGraph.addEdge: Node does not exist: source="+x+", target="+y+".");
			return;
		}
		CGDEdge edge=new CGDEdge(source,target);
		
		CGDEdge.maxEIndex++;
		edge.setIdentifier(""+CGDEdge.maxEIndex);
		edge.setIndex(CGDEdge.maxEIndex);
		edges.put(new Point(x,y),edge);
	}

	public void addEdge(int x, int y, Point2D.Double[] _edgePoints){
		addEdge(x, y);
		CGDEdge edge=this.getEdge(x,y);
		edge.setPoints(_edgePoints);
	}
	public boolean removeEdge(int x, int y){
		boolean result=false;
		Point newPoint=new Point(x,y);
		
		Object o=edges.remove(newPoint);
		if(o!=null) result=true;
		
		return result;
	}
	
	public GraphUtil getGraphUtil(){
		return gu;
	}
	
	/**
	 * Initializing bounding box placement
	 *
	 */
	public void buildNodeLayout(ParseClanTree _root, int numNodes, int initialNumNodes, TreeSet[] parentRel){
		gu.buildNodeLayout(this,_root, numNodes, initialNumNodes, parentRel);
	}
	
	/**
	 * Parents of all the nodes leading to n
	 */
	public CGDTreeSet parents(int n) {
		CGDTreeSet<Integer> parents = new CGDTreeSet<Integer>();

		for (int i=0;i<nodes.size();i++){
			CGDNode node=(CGDNode)nodes.get(i);
			if (node.children.contains(n))
				parents.add(node.getIndex());
		}
		return parents;
	}

	/**
	 * Children of all the nodes n leads to
	 */
	public CGDTreeSet children(int n) {
		return ((CGDNode)nodes.get(n)).getChildren();
	}

	public void dummysToEdgePaths() {
		gu.dummysToEdgePaths(this);
	}
	
	public int getMaxNIndex(){
		return CGDNode.maxNIndex;
	}
	public void setMaxNIndex(int i){
		CGDNode.maxNIndex=i;
	}

	public int getMaxEIndex(){
		return CGDEdge.maxEIndex;
	}
	public void setMaxEIndex(int i){
		CGDEdge.maxEIndex=i;
	}
	public int getNumOfNodes(){
		return nodes.size();
	}
}