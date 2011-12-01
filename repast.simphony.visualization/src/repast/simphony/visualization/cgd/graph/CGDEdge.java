package repast.simphony.visualization.cgd.graph;

import java.awt.geom.Point2D;


public class CGDEdge {
	public static int maxEIndex=0;
	private int index=0;
	private String identifier=null;

	protected CGDNode source;
	protected CGDNode target;
	protected Point2D.Double[] points;
	
	public CGDEdge(){
		this(null,null);
	}
	public CGDEdge(CGDNode a, CGDNode b){
		source=a;
		target=b;
	}
	public CGDEdge(CGDNode a, CGDNode b, int _index, String _identifier){
		this(a,b);
		setIndex(_index);
		setIdentifier(_identifier);
	}

	public CGDNode getSource(){
		return source;
	}
	public CGDNode getTarget(){
		return target;
	}
	
	public int getIndex(){
		return index;
	}
	public void setIndex(int i){
		index=i;
	}
	
	public String getIdentifier(){
		return identifier;
	}
	public void setIdentifier(String s){
		identifier=s;		
	}
	
	public Point2D.Double[] getPoints(){
		return points;
	}
	public void setPoints(Point2D.Double[] _points){
		points=_points;
	}
}