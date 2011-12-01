package repast.simphony.gis.display;

import org.geotools.map.MapLayer;
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


  private Map<MapLayer, PGisLayer> layerMap = new HashMap<MapLayer, PGisLayer>();

  public void layerAdded(MapLayer layer, PGisLayer gisLayer) {
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

  public void layerRemoved(MapLayer layer) {
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
    if (event.getSource() instanceof MapLayer) {
      MapLayer layer = (MapLayer) event.getSource();
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
}