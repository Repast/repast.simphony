package repast.simphony.gis.tools;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;

import repast.simphony.gis.display.PGISCanvas;

import com.vividsolutions.jts.geom.Envelope;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.util.PBounds;

public class PGISPanTool extends PPanEventHandler {
	MapContent context;

	PGISCanvas canvas;

	public PGISPanTool(MapContent context, PGISCanvas canvas) {
		this.context = context;
		this.canvas = canvas;
	}

	@Override
	protected void endDrag(PInputEvent e) {
		super.endDrag(e);
		PBounds bounds = canvas.getCamera().getViewBounds();
		Envelope env = new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds
				.getMinY(), bounds.getMaxY());
		context.getViewport().setBounds(new ReferencedEnvelope(env, canvas.getCRS()));
	}
}
