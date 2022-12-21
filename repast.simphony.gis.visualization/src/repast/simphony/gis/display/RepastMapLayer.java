package repast.simphony.gis.display;

import static org.geotools.map.MapLayerEvent.DATA_CHANGED;
import static org.geotools.map.MapLayerEvent.FILTER_CHANGED;
import static org.geotools.map.MapLayerEvent.STYLE_CHANGED;
import static org.geotools.map.MapLayerEvent.VISIBILITY_CHANGED;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListener;
import org.geotools.styling.Style;

/**
 * Simple wrapper for the Geotools FeatureLayer.
 * 
 * @author Eric Tatara
 *
 *@deprecated 2D piccolo based code is being removed
 */
public class RepastMapLayer extends FeatureLayer {

	public RepastMapLayer(FeatureCollection coll, Style style, String title) {
		super(coll, style, title);
	}

	public RepastMapLayer(FeatureCollection coll, Style style) {
		super(coll, style);
	}

	public RepastMapLayer(FeatureSource source, Style style, String title) {
		super(source, style, title);
	}

	public RepastMapLayer(FeatureSource source, Style style) {
		super(source, style);
	}

	List<MapLayerListener> listenerList = new ArrayList<MapLayerListener>();

	@Override
	public synchronized void addMapLayerListener(MapLayerListener listener) {
		listenerList.add(listener);
	}

	@Override
	public synchronized void removeMapLayerListener(MapLayerListener listener) {
		listenerList.remove(listener);
	}

	public void fireMapLayerChangedEvent(MapLayerEvent event) {
		for (MapLayerListener listener : listenerList) {
			if (event.getReason() == DATA_CHANGED
					|| event.getReason() == FILTER_CHANGED
					|| event.getReason() == STYLE_CHANGED) {
				
				listener.layerChanged(event);
			} else if (event.getReason() == VISIBILITY_CHANGED
					&& this.isVisible()) {
				listener.layerShown(event);
			} else if (event.getReason() == VISIBILITY_CHANGED
					&& !this.isVisible()) {
				listener.layerHidden(event);
			}
		}

	}

	@Override
	public void setVisible(boolean arg0) {
		super.setVisible(arg0);
		fireMapLayerChangedEvent(new MapLayerEvent(this,
				MapLayerEvent.VISIBILITY_CHANGED));
	}
}