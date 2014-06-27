package repast.simphony.gis.tools;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PBounds;

import repast.simphony.gis.display.PGISCanvas;

public class PMarqueeZoomOut extends AbstractMarqueeHandler {

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
	}
}