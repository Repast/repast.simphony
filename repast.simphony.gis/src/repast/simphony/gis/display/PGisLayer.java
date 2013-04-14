package repast.simphony.gis.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;

import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Envelope;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PImage;

/**
 * This represents a FeatureLayer as an image. Each layer is inserted as a node into
 * the PCanvas.
 *
 * @author Tom Howe
 * @author Eric Tatara
 */
public class PGisLayer extends PLayer implements MapLayerListener {
  private static final long serialVersionUID = 1732090043130770803L;

  protected MessageCenter center = MessageCenter.getMessageCenter(getClass());
  protected BufferedImage image;
  protected MapContent context;
  protected GTRenderer rend;
  protected Rectangle rect = new Rectangle(800, 800);
  protected boolean interacting = false;
  protected boolean drawn = false;
  protected AffineTransform transform;
  protected Layer layer;
  protected MapContent localContext;
  protected PImage pImage;
  protected Envelope layerArea;
  protected double scaleDenominator;

  private static class BoundsListener implements MapBoundsListener {

    private MapContent mainContext, localContext;

    public BoundsListener(MapContent mainContext, MapContent localContext) {
      this.mainContext = mainContext;
      this.localContext = localContext;
    }

    public void mapBoundsChanged(MapBoundsEvent event) {
      localContext.getViewport().setBounds(mainContext.getViewport().getBounds());
    }
  }

  private BoundsListener boundsListener;

  /**
   * Create a new Layer based on the FeatureLayer, the MapContext (eventually for
   * projection) and the AffineTransform as supplied by piccolo.
   *
   * @param layer
   * @param context
   * @param transform
   */
  public PGisLayer(Layer layer, MapContent context, AffineTransform transform) {
    this.transform = transform;
    this.layer = layer;
    this.layer.addMapLayerListener(this);
    try {
      this.layerArea = layer.getFeatureSource().getBounds();
    } catch (IOException e) {
      center.error("Unable to get Layer bounds for: "
              + layer.getFeatureSource().getSchema().getName().getLocalPart(), e);
    }
    this.context = context;
    
    localContext = new MapContent();
    localContext.getViewport().setBounds(context.getViewport().getBounds());
    
    boundsListener = new BoundsListener(context, localContext);
    context.addMapBoundsListener(boundsListener);
    localContext.addLayer(layer);
    
    rend = new StreamingRenderer();
    rend.setMapContent(localContext);
    
    Map rendererParams = new HashMap();
    rendererParams.put("optimizedDataLoadingEnabled", Boolean.TRUE);
    rend.setRendererHints(rendererParams);
   
    image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
    rend.paint((Graphics2D) image.getGraphics(), rect, transform);
    pImage = new PImage(image);

    try {
      pImage.setTransform(transform.createInverse());
    } catch (Exception ex) {
      center.error("Error while drawing layer: " + 
         layer.getStyle().getDescription().getTitle().toString(), ex);
    }
    addChild(pImage);
  }

  /**
   * Calls this to remove listeners etc. Functions like a destructor
   * in C++.
   */
  public void cleanUp() {
    context.removeMapBoundsListener(boundsListener);
    localContext.removeLayer(layer);
  }

  /**
   * Gets the FeatureLayer that this PGisLayer displays.
   *
   * @return the FeatureLayer that this PGisLayer displays.
   */
  public Layer getLayer() {
    return layer;
  }

  public static enum RenderEnum {
    START, FINISH
  }

  /**
   * Redraw the image of the layer.
   */
  public void update() {
    Graphics2D g2d;
    Rectangle rect;
    clear();
    
    try {
      g2d = image.createGraphics();
      //if (context.getAreaOfInterest().getHeight() == 0 || context.getAreaOfInterest().get)
      localContext.getViewport().setBounds(context.getViewport().getBounds());
      rect = new Rectangle(0, 0, image.getWidth(), image.getHeight());
      rend.paint(g2d, rect, context.getViewport().getBounds(), transform);
      g2d.dispose();
      pImage.setTransform(transform.createInverse());
      pImage.setImage(image);
    } catch (Exception ex) {
      center.error("Error while drawing layer: " + 
         layer.getStyle().getDescription().getTitle().toString(), ex);
    }
  }

  public void clear() {
    Graphics2D g2d = image.createGraphics();
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
    g2d.setColor(Color.WHITE);
    Rectangle rect = new Rectangle(0, 0, image.getWidth(), image
            .getHeight());
    g2d.fill(rect);
  }

  public void setSize(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    rect.setFrame(0, 0, image.getWidth(), image.getHeight());
    update();
  }

  @Override
  public void layerChanged(MapLayerEvent arg0) {
  }

  @Override
  public void layerHidden(MapLayerEvent arg0) {
    setVisible(false);
  }

  @Override
  public void layerShown(MapLayerEvent arg0) {
    setVisible(true);
  }

	@Override
	public void layerDeselected(MapLayerEvent arg0) {
	}

	@Override
	public void layerPreDispose(MapLayerEvent arg0) {
	}

	@Override
	public void layerSelected(MapLayerEvent arg0) {
	}
}