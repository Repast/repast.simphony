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

public class GisDisplayMediator2 implements MapLayerListener,
        ComponentListener, MapLayerListListener {


  private Map<Layer, PGisLayer> layerMap = new HashMap<Layer, PGisLayer>();

  public void layerAdded(Layer layer, PGisLayer gisLayer) {
    layerMap.put(layer, gisLayer);
    gisLayer.getLayer().addMapLayerListener(this);
  }

  public void layerAdded(MapLayerListEvent event) {
  }

  public void layerChanged(MapLayerListEvent event) {
    PGisLayer layer = layerMap.get(event.getSource());
    layer.update();
  }


  public void layerMoved(MapLayerListEvent event) {
  }

  public void layerRemoved(MapLayerListEvent event) {
  }

  public void layerRemoved(Layer layer) {
    layerMap.remove(layer);
    layer.removeMapLayerListener(this);
  }


  public void addLayer(PGisLayer display, Dimension size) {
    layerMap.put(display.getLayer(), display);
    display.getLayer().addMapLayerListener(this);
  }

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

  public void layerHidden(MapLayerEvent event) {
  }

  public void layerShown(MapLayerEvent event) {
  }

  public void componentHidden(ComponentEvent e) {
  }

  public void componentMoved(ComponentEvent e) {
  }

  public void componentShown(ComponentEvent e) {
  }

	@Override
	public void layerPreDispose(MapLayerListEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void layerDeselected(MapLayerEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void layerPreDispose(MapLayerEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void layerSelected(MapLayerEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}