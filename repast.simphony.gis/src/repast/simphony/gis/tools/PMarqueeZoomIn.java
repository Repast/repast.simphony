package repast.simphony.gis.tools;

import java.awt.event.InputEvent;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;

import repast.simphony.gis.display.AbstractMarqueeZoomer;
import repast.simphony.gis.display.PGISCanvas;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PBounds;

public class PMarqueeZoomIn extends AbstractMarqueeZoomer {

	MapContent context;

	public PMarqueeZoomIn(MapContent context) {
		setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
		this.context = context;
	}

	@Override
	protected void execute(PInputEvent ev, PBounds rect) {
		PGISCanvas canvas = ((PGISCanvas) ev.getComponent());

		PBounds cRect = ev.getCamera().getViewBounds();
		ReferencedEnvelope envelope = new ReferencedEnvelope(canvas.getCRS());

		double dx = rect.getWidth() / cRect.getWidth();
		double dy = rect.getHeight() / cRect.getHeight();

		double scale;
		double wid, hi;
		if (dx > dy) {
			scale = dx;
		} else { //dy > dx
			scale = dy;
		}
		wid = scale * cRect.getWidth();
		hi = scale * cRect.getHeight();

		double w2 = wid/2.0;
		double h2 = hi/2.0;

		envelope.init(rect.getCenterX()-w2, rect.getCenterX()+w2, rect.getCenterY()-h2,rect.getCenterY() + h2);
		canvas.setAreaOfInterest(envelope);
	}
}
