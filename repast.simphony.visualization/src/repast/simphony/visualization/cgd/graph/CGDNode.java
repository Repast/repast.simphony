package repast.simphony.visualization.cgd.graph;

import java.awt.geom.Point2D;

import repast.simphony.visualization.cgd.util.CGDTreeSet;



public class CGDNode{
	public static int maxNIndex=0;

	private int index=0;
	private String identifier=null;
	protected double x;
	protected double y;
	protected double width, height;
	protected boolean isDummy=false;
	
	CGDTreeSet<Integer> children;
	
	public CGDNode(){
		this(0.0,0.0);
	}
	public CGDNode(int _index){
		this();
		setIndex(_index);
		setIdentifier(""+_index);
	}
	public CGDNode(int _index, String _identifier){
		this();
		setIndex(_index);
		setIdentifier(_identifier);
	}
	public CGDNode(double _x, double _y){
		x=_x;
		y=_y;
		setDefaultSize();
		children=new CGDTreeSet<Integer>();
	}
	
	public void addChild(int ch){
		children.add(ch);
	}
	
	private void setDefaultSize(){
		width=20.0;
		height=20.0;
	}
	
	public void setX(double _x){
		x=_x;
	}
	public double getX(){
		return x;
	}

	public void setY(double _y){
		y=_y;
	}
	public double getY(){
		return y;
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
	
	public CGDTreeSet<Integer> getChildren(){
		return children;
	}

	public Point2D.Double getBoundingBox() {
		Point2D.Double bbox = new Point2D.Double(width, height);
		return bbox;
	}

	public Point2D.Double getPosition() {
		Point2D.Double position = new Point2D.Double(x, y);
		return position;
	}

	public void setPosition(double _x, double _y) {
		x = _x;
		y = _y;
	}
	
	public void setPosition(Point2D.Double new_position) {
		x = new_position.x;
		y = new_position.y;
	}

	public void setBoundingBox(double _width, double _height) {
		width = _width;
		height = _height;
	}

}