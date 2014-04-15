package repast.simphony.gis.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;

import org.piccolo2d.PCanvas;
import org.piccolo2d.PLayer;
import org.piccolo2d.event.PDragSequenceEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.util.PBounds;

public abstract class AbstractMarqueeHandler extends PDragSequenceEventHandler {

	PPath.Double rect;

	Point2D start;

	PBounds bounds;

	PLayer layer = new PLayer();

	public void mousePressed(PInputEvent ev) {
		rect = new PPath.Double();
		rect.setStroke(new BasicStroke(0));
		rect.setStrokePaint(Color.BLACK);
		rect.setPaint(new Color(0,0,0,0));
		start = ev.getPosition();
		PCanvas canvas = ((PCanvas) ev.getComponent());
		layer.addChild(rect);
		canvas.getCamera().addLayer(layer);
	}

	public void mouseDragged(PInputEvent ev) {
		bounds = new PBounds();
		bounds.add(start);
		bounds.add(ev.getPosition());
		rect.reset();
		rect.append(bounds, false);

		// Canvas repaint helps to clear any rectangle leftover pixels
		PCanvas canvas = ((PCanvas) ev.getComponent());
		canvas.repaint();
	}

	public void mouseReleased(PInputEvent ev) {
		layer.removeChild(rect);
		ev.getCamera().removeLayer(layer);
		if (rect.getWidth() > 0 && rect.getHeight() > 0) execute(ev, bounds);
	}

	protected abstract void execute(PInputEvent ev, PBounds rect);
}
