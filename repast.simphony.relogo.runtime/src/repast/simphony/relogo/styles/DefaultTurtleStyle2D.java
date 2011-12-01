package repast.simphony.relogo.styles;

import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import repast.simphony.relogo.Turtle;
import repast.simphony.relogo.ide.runtime.ReLogoSupport;
import repast.simphony.visualization.visualization2D.style.Style2D;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * public PNode getPNode(T object, PNode node);

	public Paint getPaint(T object);

	public Stroke getStroke(T object);
	
	public Paint getStrokePaint(T object);

	public double getRotation(T object);

	public Rectangle2D getBounds(T object);

	public boolean isScaled(T object);
	
	public PText getLabel(T object);
 * @author jozik
 *
 */

public class DefaultTurtleStyle2D implements Style2D<Turtle> {
	
	public DefaultTurtleStyle2D(){
		PPathBuilder.init();
	}

	Rectangle2D rectangle = new Rectangle2D.Float(0, 0, 15, 15);

//	Shape shape = new Rectangle2D.Float(0, 0, 15, 15);
	int[] xPoints = {0, 15, 15, 0, 4, 4};
	int[] yPoints = {0, 7, 8, 15, 8, 7};
	Shape shape = new Polygon(xPoints, yPoints, 6);
	
	int[] xP2 = {10, 12, 12, 10};
	int[] yP2 = {1, 1, 14, 14};
	Shape s2 = new Polygon(xP2, yP2, 4);
	
	Stroke stroke = null;

	public Rectangle2D getBounds(Turtle turtle) {
//		return rectangle;
		return new Rectangle2D.Float(0, 0, 8, 15);
	}

	public PNode getPNode(Turtle turtle, PNode node) {
//		PNode path = PPath.createRectangle(0, 0, 8, 15);
		
//		PPath path = PPathBuilder.getPPathFromString("first image");
//		Iterator iter = path.getChildrenIterator();
//		while (iter.hasNext()){
//    		PPath child = (PPath) iter.next();
//    		child.setPaint(this.getPaint(turtle));
//    		child.setStroke(stroke);
//    	}
		
		PPath path = new PPath(getShapeFromString(""));
		
//		path.setStroke(this.getStroke(turtle));
//		path.setTransparency(0.9f);
		
//		path.setBounds(rectangle);
		return path;
	}

	public Paint getPaint(Turtle turtle) {
		return ReLogoSupport.lookupColor(turtle.getColor());
	}

	// Note that Theta_R = Pi/2 - Theta_N
	public double getRotation(Turtle turtle) {
//		return Math.PI / 4;
		return Math.PI / 2 - turtle.getHeadingInRads();
	}

	public Stroke getStroke(Turtle turtle) {
		return null;
	}

	public boolean isScaled(Turtle turtle) {
		return true;
	}

	public PText getLabel(Turtle turtle) {
		// TODO Auto-generated method stub
		return null;
	}

	public Paint getStrokePaint(Turtle turtle) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Shape getShapeFromString(String shapeString){
//		int[] xPoints = {0, 15, 15, 0, 4, 4};
//		int[] yPoints = {0, 7, 8, 15, 8, 7};
//		Shape shape = new Polygon(xPoints, yPoints, 6);
		int[] xPoints = {0, 15, 15, 0};
		int[] yPoints = {0, 7, 8, 15};
		Shape shape = new Polygon(xPoints, yPoints, 4);
		return shape;
	}
}
