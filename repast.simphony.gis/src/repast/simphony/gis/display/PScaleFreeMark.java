package repast.simphony.gis.display;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class PScaleFreeMark extends PPath {
	MessageCenter msg = MessageCenter.getMessageCenter(getClass());

	private static final long serialVersionUID = -715923511456057001L;

	double oldScale = Double.NaN, scale = 1;

	GeneralPath tmp = null;

	public PScaleFreeMark() {
		super();
	}

	public PScaleFreeMark(Shape arg0, Stroke arg1) {
		super(arg0, arg1);
	}

	public PScaleFreeMark(Shape arg0) {
		super(arg0);
	}

	public boolean intersects(Rectangle2D bounds) {
		if (scale != oldScale || tmp == null) {
			PAffineTransform transform = getTransform();
			transform.scale(1 / scale * getScale(), 1 / scale * getScale());
			tmp = (GeneralPath) getPathReference().clone();
			tmp.transform(transform);
		}
		boolean intersects = tmp.intersects(localToGlobal(bounds));
		return intersects;
	}

	public void paint(PPaintContext context) {
		scale = context.getScale();
		getTransformReference(true).setScale(1 / scale);
		Graphics2D g2 = context.getGraphics();
		g2.scale(1 / scale * getScale(), 1 / scale * getScale());
		super.paint(context);
		g2.setPaint(getStrokePaint());
	}
}
