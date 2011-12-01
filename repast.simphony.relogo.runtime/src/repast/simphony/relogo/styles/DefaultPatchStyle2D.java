package repast.simphony.relogo.styles;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import repast.simphony.relogo.Patch;
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

public class DefaultPatchStyle2D implements Style2D<Patch> {

	Rectangle2D rectangle = new Rectangle2D.Float(0, 0, 15, 15);

	Shape shape = new Rectangle2D.Float(0, 0, 15, 15);

	Stroke stroke = null;

	public Rectangle2D getBounds(Patch patch) {
		return rectangle;
	}

	public PNode getPNode(Patch patch, PNode node) {
		PPath path = new PPath(shape);

		path.setBounds(rectangle);
		path.setPaint(this.getPaint(patch));
		path.setStroke(this.getStroke(patch));
//		path.setTransparency(0.9f);

		return path;
	}

	public Paint getPaint(Patch patch) {
		return ReLogoSupport.lookupColor(patch.getPcolor());
	}

	public double getRotation(Patch patch) {
		return 0;
	}

	public Stroke getStroke(Patch patch) {
		return stroke;
	}

	public boolean isScaled(Patch patch) {
		return true;
	}

	public PText getLabel(Patch object) {
		// TODO Auto-generated method stub
		return null;
	}

	public Paint getStrokePaint(Patch object) {
		// TODO Auto-generated method stub
		return null;
	}
}
