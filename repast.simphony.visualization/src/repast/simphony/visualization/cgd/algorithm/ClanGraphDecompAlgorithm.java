package repast.simphony.visualization.cgd.algorithm;

/*
 * This Clan Graph Decomposition Algorithm code was developed based 
 * on the information provided in two publicly accessible papers 
 * “Regularity Extraction Via Clan-Based Structural Decomposition” 
 * by Soha Hassoun, Carolyn McCreary and “Using Graph Parsing for 
 * Automatic Graph Drawing” by Carolyn McCreary, Richard Chapman, and F.-S. Shieh.
 */



import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import repast.simphony.visualization.cgd.graph.CGDGraph;
import repast.simphony.visualization.cgd.graph.CGDNode;
import repast.simphony.visualization.cgd.util.CGDTreeSet;
import repast.simphony.visualization.cgd.util.Clan;
import repast.simphony.visualization.cgd.util.ParseClanTree;


public class ClanGraphDecompAlgorithm{
	CGDGraph graph;
	int numNodes,initialNumNodes;
	
	private TreeSet[] childRelation;
	private TreeSet[] descendentRelation;
	private TreeSet[] parentRelation;
	private TreeSet[] ancestorRelation;

	private HashMap<Integer,Integer> topObjOrder; // node index, level.
	
	private ParseClanTree root;

	public String compute(CGDGraph _graph) {
		graph=_graph;
		
		if(graph.getNodes().size()<2)
			return "Graph needs to have more than one node.";
		
		// Store initial setup.
		CGDGraph oldG=(CGDGraph)graph.clone();
		numNodes=initialNumNodes=graph.getNodes().size();
		
		// Assign child relation.
		childRelation=graph.processChildRelation();
		
		// Eliminate long edges and dummy nodes when needed.
		eliminateLongEdges();
		
		// Check the graph for cycles.
		if(lookForCycles()){
			graph.copy(oldG);
			return "Graph contains cycles.";
		}
		
		// Establish a parent relation and an ancestor relation.
		parentAncestorStructure();
		
		assignLevels();
		
		TreeSet allNodes=fillAllNodes(numNodes);
		Parser parser=new Parser();
		parser.setTopObjOrder(topObjOrder);
		parser.setNumNodes(numNodes);
		root=parser.parseSet(childRelation, parentRelation, descendentRelation, 
				        ancestorRelation, allNodes);
		
		// Reorder independent children according to x position.
		setNodesPosition(root);
		root.reorder();

		graph.buildNodeLayout(root,numNodes,initialNumNodes,parentRelation); // build bounding box.
		
		graph.dummysToEdgePaths(); // this may be connected with removing edges.
		
		return null;
	}
	
	public CGDGraph compute(CGDGraph _graph, boolean originalCall){
		this.compute(_graph);
		return _graph;
	}
	
	private boolean eliminateLongEdges(){
		boolean result=false;
		
		// Store initial child relation setup.
		TreeSet[] initChRelation = new TreeSet[numNodes];
		for(int i = 0; i < numNodes; i++)
			initChRelation[i] = (CGDTreeSet)childRelation[i].clone();
		
		// Make sure a parent node does not have the same children what the child node.
		relationReduction();
		
		// Add dummy nodes for long edges if needed.
		createDummyNodes(initChRelation);
		result=true;
		
		return result;
	}

	/**
	 * If there is an edge from j to i, so let's make
	 * all children of i to be children of j.
	 *
	 */
	private void transitiveClosure() {
		for (int i = 0; i < numNodes; i++){
			int index=((CGDNode)graph.getNodes().get(i)).getIndex();
			for (int j = 0; j < numNodes; j++)
				if (childRelation[j].contains(index))
					((CGDTreeSet)childRelation[j]).union(childRelation[i]); // union children.
		}
	} 
	
	/**
	 * All children of the child node should not be 
	 * the children of the parent of that node.
	 */
	private void relationReduction() {
		transitiveClosure();
		for (int i = 0; i < numNodes; i++){
			int index=((CGDNode)graph.getNodes().get(i)).getIndex();
			for (int j=0; j < numNodes; j++)
				if (childRelation[j].contains(index))
					((CGDTreeSet)childRelation[j]).difference((TreeSet)childRelation[i]);
		}
	}
		
	private void createDummyNodes(TreeSet[] _initChRelation){
		TreeSet ts=null;
		// Get the number of new dummy nodes needed.
		int added = 0;
		for (int i = 0; i < numNodes; i++){
			ts=_initChRelation[i];
			Iterator it1=ts.iterator();
			while(it1.hasNext()){
				if (!childRelation[i].contains(it1.next()))
					added++;				
			}
		}
		
		// Set new child relation
		numNodes = numNodes + added;
		TreeSet[] newChRelation = new TreeSet[numNodes];
		for (int i = 0; i < numNodes; i++)
			newChRelation[i] = new CGDTreeSet<Integer>();
		
		// Add new edges.
		for (int i = 0; i < initialNumNodes; i++){
			ts=childRelation[i];
			Iterator it2=ts.iterator();
			while(it2.hasNext()){
				newChRelation[i].add(it2.next());
			}
		}

		// Update graph.
		int dummyIndex;
		for (int i = 0; i < initialNumNodes; i++){
			ts=_initChRelation[i];
			Iterator it3=ts.iterator();
			while(it3.hasNext()){
				Integer nodeIndex=(Integer)it3.next();
				if(!childRelation[i].contains(nodeIndex)){
					dummyIndex = ++CGDNode.maxNIndex;
					
					// create a new node.
					graph.addDummyNode(dummyIndex);

					// remove the old edge between i node and nodeIndex node.
					graph.removeEdge(i,nodeIndex);
					
					// insert the edge between the parent and the dummy node.
					graph.addEdge(i,dummyIndex);
					graph.getNode(i).addChild(dummyIndex);
					
					// insert the edge between the dummy node and the child one.
					graph.addEdge(dummyIndex,nodeIndex);
					graph.getNode(dummyIndex).addChild(nodeIndex);
					
					// update child relation set.
					newChRelation[i].add(dummyIndex);
					newChRelation[dummyIndex].add(nodeIndex);
				}
			}
		}
		childRelation=newChRelation;
	}
	
	private boolean lookForCycles(){
		boolean result=false;
		TreeSet[] tmpChRelation= new TreeSet[numNodes];
		for(int i=0;i<numNodes;i++){
			tmpChRelation[i] = (CGDTreeSet)childRelation[i].clone();
		}
		
		transitiveClosure();
		
		descendentRelation=new TreeSet[numNodes];
		for(int j=0;j<numNodes;j++){
			descendentRelation[j] = (CGDTreeSet)childRelation[j].clone();
		}
		childRelation=tmpChRelation;
		
		for(int k=0;k<numNodes;k++){
			int index=((CGDNode)graph.getNodes().get(k)).getIndex();
			if(descendentRelation[k].contains(index)){
				result=true;
			}
		}

		return result;
	}
	
	private void parentAncestorStructure(){
		Iterator it1,it2;
		parentRelation = new TreeSet[numNodes];
		ancestorRelation = new TreeSet[numNodes];
		int i=0;
		for (i=0;i<numNodes;i++){
			parentRelation[i] = new CGDTreeSet<Integer>();
			ancestorRelation[i] = new CGDTreeSet<Integer>();
		}
		for(i=0;i<numNodes;i++){			
			it1=childRelation[i].iterator();
			it2=descendentRelation[i].iterator();
			
			while(it1.hasNext()){
				int p=(Integer)it1.next();
				parentRelation[p].add(i);
			}

			while(it2.hasNext()){
				int a=(Integer)it2.next();
				ancestorRelation[a].add(i);
			}
		}
	}
	
	private void assignLevels(){
		topObjOrder=new HashMap<Integer,Integer>();

		for(int i=0;i<numNodes;i++){
			int index=((CGDNode)graph.getNodes().get(i)).getIndex();
			if(parentRelation[i].isEmpty()){
				assignLevels(index,0);
			}
		}
	}
	
	private void assignLevels(int node, int level){
		Object o=topObjOrder.get(node);
		if(o==null){
			topObjOrder.put(node,level);
			TreeSet children=childRelation[node];
			Iterator it=children.iterator();
			while(it.hasNext()){
				assignLevels((Integer)it.next(),level+1);
			}
		}
	}
	
	private TreeSet<Integer> fillAllNodes(int number){
		TreeSet<Integer> ts=new CGDTreeSet<Integer>();
		for(int i=0; i<number; i++){
			if(((CGDNode)graph.getNode(i))!=null){
				int index=((CGDNode)graph.getNode(i)).getIndex();
				ts.add(index);
			}
		}
		
		return ts;
	}
	
	// Order according to x position
	private void setNodesPosition(ParseClanTree node) {
		node.dummy = false;

		if (node.clan.clanType == Clan.SINGLETON) {
			int graphnode = (Integer)node.clan.nodes.first();
			if (graphnode < initialNumNodes)
				node.minx = node.maxx = node.centerx = graph.getNode(graphnode).getX();
			else
				node.dummy = true;
		}

		else {
			ParseClanTree tmp;
			boolean firsttime = true;
			for (tmp = node.firstChild; tmp != null; tmp = tmp.nextSibling) {
				setNodesPosition(tmp);
				if (tmp.dummy == false) {
					if (firsttime) {
						node.minx = tmp.minx;
						node.maxx = tmp.maxx;
					} else {
						if (tmp.minx < node.minx)
							node.minx = tmp.minx;
						if (tmp.maxx > node.maxx)
							node.maxx = tmp.maxx;
					}
					firsttime = false;
				}
			}
			if (firsttime)
				node.dummy = true;
			else
				node.centerx = (node.minx + node.maxx) / 2.0;
		}
	}
}