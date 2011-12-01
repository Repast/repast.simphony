package repast.simphony.visualization.cgd;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.cgd.graph.CGDEdge;
import repast.simphony.visualization.cgd.graph.CGDGraph;
import repast.simphony.visualization.cgd.graph.CGDNode;

// It needs to be in this packege because of class visibility.



public class CGDProcessor{
	CGDGraph graph=new CGDGraph();
	Network rootGraph;
	HashMap<Network,CGDGraph> translation = new HashMap<Network,CGDGraph>();
	HashMap<Object,Object> objectData = new HashMap<Object,Object>();
	HashMap<Object,Integer> indexNumber = new HashMap<Object,Integer>();
	int rootNumbNodes=0;
	
	public HashMap processGraph(Network rGraph){
		HashMap result=new HashMap();
		
		createCGDGraph(rGraph);
		graph.compute();
		transferFromCGDGraphToOrig();
		
		return result;
	}
	
	private void createCGDGraph(Network rGraph){
		ArrayList nodes=new ArrayList();
		HashMap edges=new HashMap();
		Iterator itN=rGraph.getNodes().iterator(); // this returns ordered list of nodes.
		Iterator itE=rGraph.getEdges().iterator();
		int eIndex=0;
		int maxEIndex=0;
		int nIndex=0;
		int maxNIndex=0;

		int n=0;
		while(itN.hasNext()){
			Object fNode=itN.next();
			CGDNode node=new CGDNode();
			node.setIdentifier(rGraph.getName());
	//		nIndex=n;
			node.setIndex(n);
	//		if(maxNIndex < nIndex)
	//			maxNIndex=nIndex;
			nodes.add(node);
			indexNumber.put(fNode,n);
			objectData.put(fNode,node);
			n++;
		}
		rootNumbNodes=nodes.size();
		graph.setNodes(nodes);
		graph.setMaxNIndex(maxNIndex);
		
		while(itE.hasNext()){
			RepastEdge fEdge=(RepastEdge)itE.next();
			Object fSource=fEdge.getSource();;
			int s=indexNumber.get(fSource);
			CGDNode source=(CGDNode)graph.getNode(s);
			Object fTarget=fEdge.getTarget();
			int t=indexNumber.get(fTarget);
			CGDNode target=(CGDNode)graph.getNode(t);

			CGDEdge edge=new CGDEdge(source,target);
			edge.setIdentifier(rGraph.getName());
			eIndex=s;
//			edge.setIdentifier(fEdge.getIdentifier());
			edge.setIndex(eIndex);
			if(maxEIndex < eIndex)
				maxEIndex=eIndex;
			
			edges.put(new Point(s,t),edge);
			n++;
		}
		graph.setEdges(edges);
		translation.put(rGraph,graph);
	}
	
	public HashMap getFinalEdges(){
		HashMap results=new HashMap();
		
		return results;
	}
	
	public HashMap getFinalNodes(){
		HashMap results=new HashMap();
		
		return results;
	}

	/*
	private boolean transferFromCGDGraphToOrig(){
		boolean result=false;
		ArrayList nodes=graph.getNodes();
		HashMap edges=graph.getEdges();
		
		if(nodes==null) return result;
		if(nodes.size()!=rootNumbNodes){
			System.out.println("nodes.size="+nodes.size()+" and origNumbNodes="+rootNumbNodes);
			return result;
		}
		
		for(int i=0;i<nodes.size();i++){
			CGDNode cgdNode=(CGDNode)nodes.get(i);
			int nIndex=cgdNode.getIndex();

			double x=cgdNode.getX();
			double y=cgdNode.getY();
			node.setPosition(x,y);
		}
		
		Iterator it=edges.keySet().iterator();
		while(it.hasNext()){
			Point p=(Point)it.next();
			int t=p.x;
			int h=p.y;
			Node head=rootGraph.getNodeFromIndex(h);
			Node tail=rootGraph.getNodeFromIndex(t);
			if(head==null || tail==null){
				System.out.println("CGDAlgorithmTransfer.transferFromCGDGraphToOrig(): Node not found tail="+t+", head="+h+".");
				continue;
			}
			Edge edge=rootGraph.getEdge(t,h);
			String label=null;
			if(edge!=null){
				label=edge.getLabel();
			}
			CGDEdge cgdEdge=(CGDEdge)edges.get(p);
			int pSize=0;
			Point2D.Double[] points=cgdEdge.getPoints();
			if(points!=null)
				pSize=points.length;
			else
				pSize=0;
			DPoint3[] _points=new DPoint3[pSize];
			for(int j=0;j<pSize;j++){
				Point2D.Double dd=points[j];
				DPoint3 dp=new DPoint3(dd.x,dd.y,0);
				_points[j]=dp;
			}
			rootGraph.insertEdge(t,h,_points,label);
		}
		result=true;
		
		return result;
	}
	*/
	private boolean transferFromCGDGraphToOrig(){
		boolean result=false;
		ArrayList nodes=graph.getNodes();
		HashMap edges=graph.getEdges();
		
		if(nodes==null) return result;
		if(nodes.size()!=rootNumbNodes){
			System.out.println("nodes.size="+nodes.size()+" and origNumbNodes="+rootNumbNodes);
			return result;
		}
		
		for(int i=0;i<nodes.size();i++){
			CGDNode cgdNode=(CGDNode)nodes.get(i);
			int nIndex=cgdNode.getIndex();
			Object node=null;
			Iterator ic = objectData.entrySet().iterator();
			while(ic.hasNext()) {
				Map.Entry e = (Map.Entry)ic.next();
				CGDNode o = (CGDNode) e.getValue();
				if(o.equals(cgdNode)) {
					node=e.getKey();
				}
			}
		/*	
			if(node==null){
				System.out.println("CGDAlgorithmTransfer.transferFromCGDGraphToOrig(): The node index="+nIndex+" not found in the original graph.");
			}
			double x=cgdNode.getX();
			double y=cgdNode.getY();
			node.setPosition(x,y);
		}
		
		Iterator it=edges.keySet().iterator();
		while(it.hasNext()){
			Map.Entry e = (Map.Entry)it.next();
			Point p=(Point) e.getKey();
			RepastEdge edge = (RepastEdge) e.getValue();
			int t=p.x;
			int h=p.y;
			Object head=edge.getSource();
			Object tail=edge.getTarget();

			if(head==null || tail==null){
				System.out.println("CGDAlgorithmTransfer.transferFromCGDGraphToOrig(): Node not found tail="+t+", head="+h+".");
				continue;
			}
	//		Edge edge=origGraph.getEdge(t,h);
			String label=null;
			if(edge!=null){
				rootGraph.getName();
			}
			CGDEdge cgdEdge=(CGDEdge)edges.get(p);
			int pSize=0;
			Point2D.Double[] points=cgdEdge.getPoints();
			if(points!=null)
				pSize=points.length;
			else
				pSize=0;
			double[] _points=new double[pSize*2];
			for(int j=0;j<pSize;j++){
				Point2D.Double dd=points[j];
				_points[j]=dd.x;
				_points[j+1]=dd.y;
				j++;
			}
//			rootGraph.insertEdge(t,h,_points,label);
		*/
		}
		result=true;
		
		return result;
		
	}
	
	public HashMap getObjectData() {
		return objectData;
	}
}