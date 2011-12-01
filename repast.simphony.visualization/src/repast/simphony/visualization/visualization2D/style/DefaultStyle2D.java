package repast.simphony.visualization.visualization2D.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
/**
 *  @deprecated replaced by ogl 2D
 */
public class DefaultStyle2D implements Style2D<Object> {

	protected Rectangle2D rect = new Rectangle2D.Float(0, 0, 10, 10);

	protected Paint color = Color.RED;
	
	protected Shape s = new Ellipse2D.Float(0, 0, 10, 10);

	protected Stroke stroke = new BasicStroke(1);

	protected Paint strokeColor = Color.BLUE;
	
	protected double rotation = 0.0;

	public DefaultStyle2D() {
		super();
	}

	public DefaultStyle2D(Paint p) {
		super();
		this.color = p;
	}

	public PNode getPNode(Object object, PNode node) {
		PPath path = new PPath(s);
		path.setBounds(rect);
		return path;
	}

	public Paint getPaint(Object object) {
		return color;
	}

	public void setPaint(Paint p) {
		this.color = p;
	}

	public double getRotation(Object object) {
		return rotation;
	}

	public void setRotation(double rot) {
		this.rotation = rot;
	}

	public void setBounds(Rectangle2D bounds) {
		this.rect = bounds;
	}

	public Rectangle2D getBounds(Object object) {
		return rect;
	}

	public Stroke getStroke(Object object) {
		return stroke;
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	public boolean isScaled(Object object) {
		return true;
	}

	public Paint getStrokePaint(Object object) {
		return strokeColor;
	}

	public PText getLabel(Object object) {
		return null;
	}
}
