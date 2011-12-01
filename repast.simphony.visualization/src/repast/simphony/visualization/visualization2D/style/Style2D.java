package repast.simphony.visualization.visualization2D.style;

import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;

/**
 *  @deprecated replaced by ogl 2D
 */
public interface Style2D<T> {

	public PNode getPNode(T object, PNode node);

	public Paint getPaint(T object);

	public Stroke getStroke(T object);
	
	public Paint getStrokePaint(T object);

	public double getRotation(T object);

	public Rectangle2D getBounds(T object);

	public boolean isScaled(T object);
	
	public PText getLabel(T object);
}
