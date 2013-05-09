package repast.simphony.gis.tools;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;

import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.gis.display.PiccoloMapPanel;

import com.vividsolutions.jts.geom.Envelope;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.util.PBounds;

/**
 * 
 * 
 *
 */
public class PGISPanTool extends PPanEventHandler implements MapTool {
	
	private static final String CURSOR_IMAGE = "mActionPan.png";
	private MapContent context;
	private PGISCanvas canvas;

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

	@Override
	public void activate(PiccoloMapPanel panel) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
	}

	@Override
	public Cursor getCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	  Image image = toolkit.getImage(MapTool.class.getClassLoader().getResource(CURSOR_IMAGE));
	  
	  return toolkit.createCustomCursor(image, new Point(10, 10), "Pan");
	}
}