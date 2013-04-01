package repast.simphony.gis.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class AbstractMarqueeHandler extends PDragSequenceEventHandler {

	PPath rect;

	Point2D start;

	PBounds bounds;

	PLayer layer = new PLayer();

	public void mousePressed(PInputEvent ev) {
		rect = new PPath();
		rect.setStroke(new BasicStroke(0));
		rect.setStrokePaint(Color.BLACK);
		start = ev.getPosition();
		PCanvas canvas = ((PCanvas) ev.getComponent());
		layer.addChild(rect);
		canvas.getCamera().addLayer(layer);
	}

	public void mouseDragged(PInputEvent ev) {
		bounds = new PBounds();
		bounds.add(start);
		bounds.add(ev.getPosition());
		rect.setPathTo(bounds);
		rect.repaint();
	}

	public void mouseReleased(PInputEvent ev) {
		layer.removeChild(rect);
		ev.getCamera().removeLayer(layer);
		execute(ev, bounds);
	}

	protected abstract void execute(PInputEvent ev, PBounds rect);
}
