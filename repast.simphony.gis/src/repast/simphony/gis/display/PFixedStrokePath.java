package repast.simphony.gis.display;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;
import org.apache.commons.lang.SystemUtils;

import java.awt.*;

public class PFixedStrokePath extends PPath {

	@Override
	protected void paint(PPaintContext paintContext) {
		Paint p = getPaint();
		Graphics2D g2 = paintContext.getGraphics();

		if (p != null) {
			g2.setPaint(p);
			g2.fill(getPathReference());
		}
		Stroke stroke = getStroke();
		Paint strokePaint = getStrokePaint();
		if (stroke != null && strokePaint != null) {
			g2.setPaint(strokePaint);
			g2.setStroke(prepareStroke(stroke, paintContext));
			g2.draw(getPathReference());
		}
	}

	public PFixedStrokePath() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PFixedStrokePath(Shape arg0, Stroke arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public PFixedStrokePath(Shape arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private Stroke prepareStroke(final Stroke stroke,
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
