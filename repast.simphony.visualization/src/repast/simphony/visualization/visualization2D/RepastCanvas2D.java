package repast.simphony.visualization.visualization2D;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.piccolo2d.PCamera;
import org.piccolo2d.PCanvas;
import org.piccolo2d.PLayer;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEventFilter;
import org.piccolo2d.event.PInputEventListener;
import org.piccolo2d.util.PPaintContext;

/**
 * 
 *  @deprecated replaced by ogl 2D
 *
 * @author howe
 *
 */
public class RepastCanvas2D extends PCanvas{

	private static final long serialVersionUID = 291436309770995502L;
	private double mousePositionX;
	private double mousePositionY;

	PInputEventListener currentListener;

	Map<Class, DisplayLayer2D> layerMap = new HashMap<Class, DisplayLayer2D>();

	public RepastCanvas2D() {
		setSize(Display2D.DISPLAY_WIDTH, Display2D.DISPLAY_HEIGHT);
		AffineTransform viewTransform = getCamera().getViewTransform();
		viewTransform.scale(1, -1);
		getCamera().setViewTransform(viewTransform);
		getPanEventHandler().setEventFilter(
				new PInputEventFilter(InputEvent.BUTTON3_MASK));
		getZoomEventHandler().setEventFilter(
				new PInputEventFilter(InputEvent.BUTTON2_MASK)); // middle button zoom
		resetHomeView();

		// add mouse wheel zoom control
		this.addMouseWheelListener(new repastPiccoloMouseWheelListener());
		this.addMouseMotionListener(new repastPiccoloMouseMotionListener());
		
		// need to set to low quality otherwise the value layers get dithered.
		// TODO use custom PCanvas to switch between render qualities.
		this.setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
	}

	public void addLayer(PLayer layer) {
		this.getRoot().addChild(layer);
		this.getCamera().addLayer(layer);
	}

	public void setTool(PBasicInputEventHandler ev) {
		if (currentListener != null) {
			removeInputEventListener(currentListener);
		}
		currentListener = ev;
		super.addInputEventListener(ev);
	}

	public void setLayerTool(PBasicInputEventHandler ev, Class clazz) {
		DisplayLayer2D layer = layerMap.get(clazz);
		layer.setListener(ev);
		if (currentListener != null) {
			removeInputEventListener(currentListener);
		}
	}

	public void setViewBounds(Rectangle2D bounds) {
		getCamera().animateViewToCenterBounds(bounds, true, 0);
	}

	/**
	 * Zoom control with mouse wheel
	 * @author tatara
	 * 
	 */
	public class repastPiccoloMouseWheelListener implements MouseWheelListener{ 
		private double minScale = 0;
		private double maxScale = Double.MAX_VALUE;

		public void mouseWheelMoved(MouseWheelEvent e) {
			PCamera camera = getCamera();

			double dx = -e.getWheelRotation();

			double scaleDelta = (1.0 + (0.1 * dx));

			double currentScale = camera.getViewScale();
			double newScale = currentScale * scaleDelta;

			if (newScale < minScale) 
				scaleDelta = minScale / currentScale;
			
			if ((maxScale > 0) && (newScale > maxScale)) 
				scaleDelta = maxScale / currentScale;
			
			Point2D point = camera.localToView(new Point2D.Double(mousePositionX,mousePositionY));		

			camera.scaleViewAboutPoint(scaleDelta, point.getX(), point.getY());
		}
	}

	/**
	 * Assists mouse wheel zoom control by updating the center coord
	 * @author tatara
	 *
	 */
	public class repastPiccoloMouseMotionListener implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub			
		}

		public void mouseMoved(MouseEvent e) {
			mousePositionX = e.getX();
			mousePositionY = e.getY();
		} 
	}

	/**
	 * Resets the display view to the original zoom and camera bounds
	 */
	public void resetHomeView(){
		getCamera().animateViewToCenterBounds(
				new Rectangle2D.Float(0, 0, 500, 500), true, 0);
	}
}
