package repast.simphony.gis.tools;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;

import repast.simphony.gis.display.AbstractMarqueeZoomer;
import repast.simphony.gis.display.PGISCanvas;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PBounds;

public class PMarqueeZoomOut extends AbstractMarqueeZoomer {

	MapContent context;

	public PMarqueeZoomOut(MapContent context) {
		this.context = context;
	}

	@Override
	protected void execute(PInputEvent ev, PBounds rect) {
    if (ev.isPopupTrigger() || ev.isRightMouseButton()) return;
    PBounds viewBounds = ev.getCamera().getViewBounds();
		double scale = viewBounds.getWidth() / rect.getWidth();

		double width = viewBounds.getWidth() * scale / 2;
		double height = viewBounds.getHeight() * scale / 2;

		PGISCanvas canvas = ((PGISCanvas) ev.getComponent());
		ReferencedEnvelope aoe = new ReferencedEnvelope(canvas.getCRS());
		double x = rect.getCenterX();
		double y = rect.getCenterY();
		aoe.init(x - width, x + width,  viewBounds.getMinY() - height, y + height);
		canvas.setAreaOfInterest(aoe);

		/*
		 * Envelope maxEnv = CRS.getEnvelope(canvas.getCRS()); AffineTransform
		 * transform = new AffineTransform();
		 * transform.translate(rect.getCenterX(), rect.getCenterY());
		 * transform.scale(scaleX, scaleX);
		 * transform.translate(-rect.getCenterX(), -rect.getCenterY()); Shape
		 * shape = transform.createTransformedShape(rect); PathIterator iter =
		 * shape.getPathIterator(null); double[] points = new double[2];
		 * iter.currentSegment(points); double minX = points[0]; double maxY =
		 * points[1]; iter.next(); iter.next(); iter.currentSegment(points);
		 * double maxX = points[0]; double minY = points[1]; if (minX <
		 * maxEnv.getMinimum(0)) minX = maxEnv.getMinimum(0); if (minY <
		 * maxEnv.getMinimum(1)) minY = maxEnv.getMinimum(1); if (maxX >
		 * maxEnv.getMaximum(0)) maxX = maxEnv.getMaximum(0); if (maxY >
		 * maxEnv.getMaximum(1)) maxY = maxEnv.getMaximum(1); Rectangle2D
		 * newRect = new Rectangle2D.Double(minX, minY, maxX - minX, maxY -
		 * minY);
		 */

	}
}
