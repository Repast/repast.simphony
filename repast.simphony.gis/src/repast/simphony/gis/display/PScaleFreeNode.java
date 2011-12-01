package repast.simphony.gis.display;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;
import org.apache.commons.lang.SystemUtils;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PScaleFreeNode extends PNode {

	private static final long serialVersionUID = -8178099195359821960L;

	PBounds intersectBounds = new PBounds();

	public PScaleFreeNode() {
		setChildrenPickable(false);
	}

	public boolean intersects(Rectangle2D queryBounds) {
		for (Object o : getChildrenReference()) {
			intersectBounds.setFrame(queryBounds);
			PNode node = (PNode) o;
			if (node.intersects(node.parentToLocal(intersectBounds))) {
				return true;
			}
		}
		return false;
		// if (!repainting) {
		// repainting = true;
		// currentViewScale = context.getScale();
		// getTransformReference(true).setScale(1 / currentViewScale);
		// }
		// repainting = false;
		// return super.intersects(queryBounds);
	}

	double currentViewScale;

	boolean repainting = false;

	public void fullPaint(PPaintContext context) {
		MessageCenter.getMessageCenter(PScaleFreeNode.class).debug(
				context.getScale());
		if (!repainting) {
			repainting = true;
			currentViewScale = context.getScale();
			getTransformReference(true).setScale(1 / currentViewScale);
		}
		repainting = false;
//		Iterator iter = getChildrenIterator();
//		while (iter.hasNext()) {
//			PNode node = (PNode) iter.next();
//			if (node instanceof PPath) {
//				PPath path = (PPath) node;
//				path.setStroke(prepareStroke(path.getStroke(), context));
//			}
//		}
		super.fullPaint(context);
	}

	public static Stroke prepareStroke(final Stroke stroke,
			final PPaintContext paintContext) {
		if (stroke == null) {
			throw new IllegalArgumentException("stroke must not be null");
		}
		if (paintContext == null) {
			throw new IllegalArgumentException("paintContext must not be null");
		}

		// use the existing stroke on platforms other than MacOSX
		if (!SystemUtils.IS_OS_MAC_OSX) {
			return stroke;
		}

		double scale = paintContext.getScale();

		// create a scaled BasicStroke for PFixedWidthStrokes on MacOSX
		if (stroke instanceof PFixedWidthStroke) {
			PFixedWidthStroke fixedWidthStroke = (PFixedWidthStroke) stroke;
			float lineWidth = (float) (fixedWidthStroke.getLineWidth() / scale);
			int endCap = fixedWidthStroke.getEndCap();
			int lineJoin = fixedWidthStroke.getLineJoin();
			float miterLimit = fixedWidthStroke.getMiterLimit();
			float[] dashArray = fixedWidthStroke.getDashArray();
			float dashPhase = fixedWidthStroke.getDashPhase();
			Stroke scaledStroke = new BasicStroke(lineWidth, endCap, lineJoin,
					miterLimit, dashArray, dashPhase);
			return scaledStroke;
		}

		if (stroke instanceof BasicStroke) {
			// use the existing stroke on MacOSX at scales <= 1.0d
			if (scale <= 1.0) {
				return stroke;
			}

			// return a new instance of the specified stroke after scaling line
			// width
			BasicStroke basicStroke = (BasicStroke) stroke;
			float lineWidth = (float) (basicStroke.getLineWidth() / scale);
			int endCap = basicStroke.getEndCap();
			int lineJoin = basicStroke.getLineJoin();
			float miterLimit = basicStroke.getMiterLimit();
			float[] dashArray = basicStroke.getDashArray();
			float dashPhase = basicStroke.getDashPhase();
			Stroke scaledStroke = new BasicStroke(lineWidth, endCap, lineJoin,
					miterLimit, dashArray, dashPhase);
			return scaledStroke;
		}

		// give up, custom strokes aren't supported on Mac OSX
		return stroke;
	}
}
