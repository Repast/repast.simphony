package repast.simphony.gis.display;

import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListListener;
import org.geotools.map.event.MapLayerListener;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Mediator for MapLayer events.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 *
 */
public class GisDisplayMediator implements MapLayerListener,
        ComponentListener, MapLayerListListener {


  private Map<Layer, PGisLayer> layerMap = new HashMap<Layer, PGisLayer>();

  public void layerAdded(Layer layer, PGisLayer gisLayer) {
    layerMap.put(layer, gisLayer);
    gisLayer.getLayer().addMapLayerListener(this);
  }

  @Override
  public void layerAdded(MapLayerListEvent event) {
  }

  @Override
  public void layerChanged(MapLayerListEvent event) {
    PGisLayer layer = layerMap.get(event.getSource());
    layer.update();
  }

  @Override
  public void layerMoved(MapLayerListEvent event) {
  }

  @Override
  public void layerRemoved(MapLayerListEvent event) {
  }

  public void layerRemoved(Layer layer) {
    layerMap.remove(layer);
    layer.removeMapLayerListener(this);
  }

  @Override
  public void componentResized(ComponentEvent e) {
    for (PGisLayer layer : layerMap.values()) {
      Dimension size = e.getComponent().getSize();
      layer.setSize(size.width, size.height);
    }
  }

  public void update() {
    for (PGisLayer layer : layerMap.values()) {
      layer.update();
    }
  }
  
  @Override
  public void layerChanged(MapLayerEvent event) {
   	if (event.getSource() instanceof FeatureLayer) {
  		Layer layer = (FeatureLayer) event.getSource();
      switch (event.getReason()) {
        case MapLayerEvent.DATA_CHANGED:
          layerMap.get(layer).update();
          break;
        case MapLayerEvent.FILTER_CHANGED:
          layerMap.get(layer).update();
          break;
        case MapLayerEvent.STYLE_CHANGED:
          layerMap.get(layer).update();
          break;
        default:

      }
    }
  }

  @Override
  public void layerHidden(MapLayerEvent event) {
  }

  @Override
  public void layerShown(MapLayerEvent event) {
  }

  @Override
  public void componentHidden(ComponentEvent e) {
  }

  @Override
  public void componentMoved(ComponentEvent e) {
  }

  @Override
  public void componentShown(ComponentEvent e) {
  }

	@Override
	public void layerPreDispose(MapLayerListEvent arg0) {
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