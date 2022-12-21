package repast.simphony.gis.display;

import java.awt.Cursor;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapBoundsEvent;
import org.geotools.map.MapBoundsListener;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.MapLayerListListener;
import org.geotools.map.MapLayerListener;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.piccolo2d.PCamera;
import org.piccolo2d.PCanvas;
import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventListener;
import org.piccolo2d.nodes.PText;
import org.piccolo2d.util.PBounds;
import org.piccolo2d.util.PPickPath;

import repast.simphony.gis.tools.DistanceTool;
import repast.simphony.gis.tools.MapTool;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

/**
 * This will show a MapContext and adds support for various tools.
 *
 * @author Howe
 * @author Eric Tatara
 * 
 * @deprecated 2D piccolo based code is being removed
 */
public class PGISCanvas extends PCanvas implements MapLayerListListener,
        PropertyChangeListener, MapBoundsListener {
  private static final long serialVersionUID = 2739102421248235987L;

  protected static final int CANVAS_WIDTH = 800;
  protected static final int CANVAS_HEIGHT = 800;
  
  protected MessageCenter msg = MessageCenter.getMessageCenter(getClass());

  protected PInputEventListener currentListener;
  protected PLayer toolLayer = new PLayer();
  protected MapContent context;
  protected Rectangle2D rect;
  protected Map<String, PGisLayer> layerNames;
  protected Map<Layer, PGisLayer> layers;
  protected double scaleDenominator;
  protected DefaultEllipsoid ellipse = DefaultEllipsoid.WGS84;
  protected  PLayer layerListening;
  protected  PLayer mapLayer = new PLayer();
  protected  GisDisplayMediator mediator = new GisDisplayMediator();

  /**
   * Create and new Canvas for the given context.
   *
   * @param context The context to be displayed
   */
  public PGISCanvas(MapContent context) {
    if (context.getViewport().getBounds() == null) {
      context.getViewport().setBounds(new ReferencedEnvelope(-90, 90, -90, 90,
              DefaultGeographicCRS.WGS84));
    }
    this.context = context;
    calcScaleDenominator(context);
    getCamera().addPropertyChangeListener(this);
    
    context.addMapLayerListListener(this);
    context.addMapBoundsListener(this);
    init();

    final PCamera camera = getCamera();
    final PText tooltipNode = new PText();
    camera.addLayer(mapLayer);
    getRoot().addChild(mapLayer);
    tooltipNode.setPickable(false);
    camera.addChild(tooltipNode);
    camera.addInputEventListener(new PBasicInputEventHandler() {
    	@Override
    	public void mouseMoved(PInputEvent event) {
        updateToolTip(event);
      }

      @Override
      public void mouseDragged(PInputEvent event) {
        updateToolTip(event);
      }

      public void updateToolTip(PInputEvent event) {
        PPickPath path = event.getInputManager().getMouseOver();
        PNode n = path.getPickedNode();
        String tooltipString;
        do {
          tooltipString = (String) n.getAttribute("tooltip");
          if (tooltipString != null) {
            break;
          }
          n = path.nextPickedNode();
        } while (n != null);
        Point2D p = event.getCanvasPosition();

        event.getPath().canvasToLocal(p, camera);

        tooltipNode.setText(tooltipString);
        tooltipNode.setOffset(p.getX() + 8, p.getY() - 8);
      }
    });
    setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    
    // Event handler to set cursor based on tool
    addInputEventListener(new PBasicInputEventHandler() {

    	// TODO Geotools [major] - set the cursor for each tool.

    	@Override
    	public void mouseDragged(PInputEvent event) {
    		 if (currentListener instanceof MapTool) {
           PGISCanvas.this.setCursor(((MapTool) currentListener).getCursor());
         } 
    		 else {
           PGISCanvas.this.setCursor(Cursor.getDefaultCursor());
         }
    	}

      @Override
      public void mouseEntered(PInputEvent event) {
        // The Distance Tool cursor should always be visible when enabled
      	if (currentListener instanceof DistanceTool) {
          PGISCanvas.this.setCursor(((MapTool) currentListener).getCursor());
        } 
      	else {
          PGISCanvas.this.setCursor(Cursor.getDefaultCursor());
        }
      }

      @Override
      public void mouseExited(PInputEvent event) {
        PGISCanvas.this.setCursor(Cursor.getDefaultCursor());
      }

    });
    //context.addMapBoundsListener(mediator);
    this.addComponentListener(mediator);
  }

  public CoordinateReferenceSystem getCRS() {
    return context.getCoordinateReferenceSystem();
  }

  private void calcScaleDenominator(MapContent context) {
    Envelope mapArea = context.getViewport().getBounds();
    Rectangle2D paintArea = getBounds();
    Point2D point1 = new Point2D.Double(mapArea.getMinX(), mapArea
            .getMinY());
    Point2D point2 = new Point2D.Double(mapArea.getMaxX(), mapArea
            .getMaxY());
    double distance_ground = ellipse.orthodromicDistance(point1, point2);
    double pixel_distance = Math.sqrt(paintArea.getWidth()
            * paintArea.getWidth() + paintArea.getHeight()
            * paintArea.getHeight());
    double pixel_distance_m = pixel_distance / 90 * 2.54 * 100.0;
    scaleDenominator = distance_ground / pixel_distance_m;
  }

  public void zoomToPreviousExtent() {

  }

  private void init() {
    layers = new HashMap<Layer, PGisLayer>();
    layerNames = new HashMap<String, PGisLayer>();
    removeInputEventListener(getZoomEventHandler());
    removeInputEventListener(this.getPanEventHandler());
    setEventHandler(getPanEventHandler());
    setSize(500, 500);
    AffineTransform transform = getCamera().getViewTransformReference();
    transform.scale(1, -1);
    getCamera().addLayer(toolLayer);
    
    for (Layer mapLayer : context.layers()) {
    	addMapLayer(mapLayer);
    }
    getCamera().addLayer(toolLayer);
    getRoot().addChild(toolLayer);

    if (context.getViewport().getBounds() != null) {
      zoomToAreaOfInterest();
    }
  }

  public double getScaleDenominator() {
    return scaleDenominator;
  }

  private void addMapLayer(Layer mapLayer) {
    addMapLayer(this.mapLayer.getChildrenCount(), mapLayer);
  }

  private void addMapLayer(int index, Layer mapLayer) {
   	PGisLayer gisLayer = new PGisLayer(mapLayer, context, getCamera().getViewTransformReference());
  	mediator.layerAdded(mapLayer, gisLayer);
    mapLayer.addMapLayerListener(gisLayer);
    this.mapLayer.addChild(index, gisLayer);
    layerNames.put(mapLayer.getFeatureSource().getSchema().getName().getLocalPart(), gisLayer);
    layers.put(mapLayer, gisLayer);
  }

  private void removeMapLayer(Layer mapLayer) {
    mediator.layerRemoved(mapLayer);
    layerNames.remove(mapLayer.getFeatureSource().getSchema().getName().getLocalPart());
    PLayer gisLayer = layers.get(mapLayer);
    if (gisLayer instanceof MapLayerListener) {
      MapLayerListener listener = (MapLayerListener) gisLayer;
      mapLayer.removeMapLayerListener(listener);
    }

    this.mapLayer.removeChild(gisLayer);
    if (gisLayer instanceof PropertyChangeListener) {
      PropertyChangeListener listener = (PropertyChangeListener) gisLayer;
      getCamera().removePropertyChangeListener(listener);
      removePropertyChangeListener(listener);
    }

    if (gisLayer instanceof ComponentListener) {
      ComponentListener listener = (ComponentListener) gisLayer;
      removeComponentListener(listener);
    }

    if (gisLayer instanceof PGisLayer) {
      ((PGisLayer) gisLayer).cleanUp();
    }

    layers.remove(mapLayer);
  }

  /**
   * Zoom the map to the area of interest specified in the MapContext.
   */
  public void zoomToAreaOfInterest() {
    Envelope aoe = context.getViewport().getBounds();
    if (aoe.getWidth() == 0) {
      aoe.expandBy(.001, 0);
    }

    if (aoe.getHeight() == 0) {
      aoe.expandBy(0, .001);
    }
    Rectangle2D rect = new Rectangle2D.Double(aoe.getMinX(), aoe.getMinY(),
            aoe.getWidth(), aoe.getHeight());
    getCamera().animateViewToCenterBounds(rect, true, 0);
  }

  public void setAreaOfInterest(ReferencedEnvelope aoe) {
    //First checkthe scale X and Y of the new area of interest match the canvas x-y ratio
    PBounds cRect = getCamera().getViewBounds();
    ReferencedEnvelope envelope = new ReferencedEnvelope(getCRS());

    double dx = aoe.getWidth() / cRect.getWidth();
    double dy = aoe.getHeight() / cRect.getHeight();

    double scale;
    double wid, hi;
    if (dx > dy) {
      scale = dx;
    } else { //dy > dx
      scale = dy;
    }
    wid = scale * cRect.getWidth();
    hi = scale * cRect.getHeight();

    double w2 = wid / 2.0;
    double h2 = hi / 2.0;

    envelope.init(aoe.getMedian(0) - w2, aoe.getMedian(0) + w2, aoe.getMedian(1) - h2, aoe.getMedian(1) + h2);
    setAreaOfInterest_preScaled(envelope);
  }

  private void setAreaOfInterest_preScaled(ReferencedEnvelope aoe) {
    context.getViewport().setBounds(aoe);
    zoomToAreaOfInterest();
  }

  /**
   * Set the tool which the map is listening to.
   *
   * @param listener the new tool to listen to.
   */
  public void setEventHandler(PInputEventListener listener) {
    if (layerListening != null) {
      layerListening.removeInputEventListener(currentListener);
      layerListening = null;
    } else {
      removeInputEventListener(currentListener);
    }
    currentListener = listener;
    addInputEventListener(currentListener);
  }

  public void addLayerEventHandler(String layerName,
                                   PInputEventListener listener) {
    PLayer layer = layerNames.get(layerName);
    layer.addInputEventListener(listener);
  }

  public PInputEventListener getCurrentEventHandler() {
    return currentListener;
  }

  /**
   * Gets the layer on which tools should draw.
   *
   * @return the layer on which tools should draw.
   */
  public PLayer getToolLayer() {
    return toolLayer;
  }

  /**
   * Gets the layer that contains the individual
   * map layers.
   *
   * @return the layer that contains the individual
   *         map layers.
   */
  public PLayer getMapLayer() {
    return mapLayer;
  }

  @Override
  public void layerAdded(MapLayerListEvent event) {
    addMapLayer(event.getToIndex(), event.getLayer());
  }

  @Override
  public void layerChanged(MapLayerListEvent event) {
  }

  @Override
  public void layerMoved(MapLayerListEvent event) {
    Layer layer = event.getLayer();
    int toIndex = event.getToIndex();
    removeMapLayer(layer);
    addMapLayer(toIndex, event.getLayer());
  }

  @Override
  public void layerRemoved(MapLayerListEvent event) {
    Layer mapLayer = event.getLayer();
    removeMapLayer(mapLayer);
  }

  @Override
  public void repaint() {
    ThreadUtilities.runInEventThread(new Runnable() {
      public void run() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        PGISCanvas.super.repaint();
        setCursor(Cursor.getDefaultCursor());
      }
    });
  }

  @Override
  public void propertyChange(PropertyChangeEvent arg0) {
    if (!arg0.getPropertyName().equals(PCamera.PROPERTY_VIEW_TRANSFORM)) {
      return;
    }
    calcScaleDenominator(context);
  }

  @Override
  public void mapBoundsChanged(MapBoundsEvent event) {
    zoomToAreaOfInterest();
    mediator.update();
  }

	@Override
	public void layerPreDispose(MapLayerListEvent event) {
	}
}