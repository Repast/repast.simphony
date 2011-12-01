package repast.simphony.gis.display;

import com.vividsolutions.jts.geom.Envelope;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PImage;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This represents a MapLayer as an image. Each layer is inserted as a node into
 * the PCanvas.
 *
 * @author Howe
 */
public class PGisLayer extends PLayer implements MapLayerListener {
  private static final long serialVersionUID = 1732090043130770803L;

  private static long layerKey = 0;

  MessageCenter center = MessageCenter.getMessageCenter(getClass());

  BufferedImage image;

  MapContext context;

  GTRenderer rend;

  Rectangle rect = new Rectangle(800, 800);

  boolean interacting = false;

  boolean drawn = false;

  AffineTransform transform;

  MapLayer layer;

  MapContext localContext;

  PImage pImage;

  Envelope layerArea;

  double scaleDenominator;

  private static class BoundsListener implements MapBoundsListener {

    private MapContext mainContext, localContext;

    public BoundsListener(MapContext mainContext, MapContext localContext) {
      this.mainContext = mainContext;
      this.localContext = localContext;
    }

    public void mapBoundsChanged(MapBoundsEvent event) {
      localContext.setAreaOfInterest(mainContext.getAreaOfInterest());
    }
  }

  private BoundsListener boundsListener;

  /**
   * Create a new Layer based on the MapLayer, the MapContext (eventually for
   * projection) and the AffineTransform as supplied by piccolo.
   *
   * @param layer
   * @param context
   * @param transform
   */
  public PGisLayer(MapLayer layer, MapContext context, AffineTransform transform) {
    this.transform = transform;
    this.layer = layer;
    this.layer.addMapLayerListener(this);
    try {
      this.layerArea = layer.getFeatureSource().getBounds();
    } catch (IOException e) {
      center.error("Unable to get Layer bounds for: "
              + layer.getFeatureSource().getSchema().getTypeName(), e);
    }

    this.context = context;
    localContext = new DefaultMapContext(context
            .getCoordinateReferenceSystem());
    localContext.setAreaOfInterest(context.getAreaOfInterest());
    boundsListener = new BoundsListener(context, localContext);
    context.addMapBoundsListener(boundsListener);
    localContext.addLayer(layer);

    rend = new StreamingRenderer();
    rend.setContext(localContext);

    Map<String, Serializable> rendererParams = new HashMap<String, Serializable>();
    rendererParams.put("optimizedDataLoadingEnabled", Boolean.TRUE);
    rend.setRendererHints(rendererParams);

    image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
    rend.paint((Graphics2D) image.getGraphics(), rect, transform);
    pImage = new PImage(image);

    try {
      pImage.setTransform(transform.createInverse());
    } catch (Exception ex) {
      center.error("Error while drawing layer: " + layer.getStyle().getTitle(), ex);
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
   * Gets the MapLayer that this PGisLayer displays.
   *
   * @return the MapLayer that this PGisLayer displays.
   */
  public MapLayer getLayer() {
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
      localContext.setAreaOfInterest(context.getAreaOfInterest());
      rect = new Rectangle(0, 0, image.getWidth(), image.getHeight());
      rend.paint(g2d, rect, context.getAreaOfInterest(), transform);
      g2d.dispose();
      pImage.setTransform(transform.createInverse());
      pImage.setImage(image);
    } catch (Exception ex) {
      center.error("Error while drawing layer: " + layer.getStyle().getTitle(), ex);
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

  public void layerChanged(MapLayerEvent arg0) {

  }

  public void layerHidden(MapLayerEvent arg0) {
    setVisible(false);
  }

  public void layerShown(MapLayerEvent arg0) {
    setVisible(true);
  }

}
