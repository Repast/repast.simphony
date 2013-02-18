package repast.simphony.gis;

import static org.geotools.map.event.MapLayerEvent.DATA_CHANGED;
import static org.geotools.map.event.MapLayerEvent.FILTER_CHANGED;
import static org.geotools.map.event.MapLayerEvent.STYLE_CHANGED;
import static org.geotools.map.event.MapLayerEvent.VISIBILITY_CHANGED;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.Style;

public class RepastMapLayer extends FeatureLayer {

	private boolean dynamic;

	public RepastMapLayer(FeatureCollection arg0, Style arg1, String arg2) {
		super(arg0, arg1, arg2);
	}

	public RepastMapLayer(FeatureCollection arg0, Style arg1) {
		super(arg0, arg1);
	}

	public RepastMapLayer(FeatureSource arg0, Style arg1, String arg2) {
		super(arg0, arg1, arg2);
	}

	public RepastMapLayer(FeatureSource arg0, Style arg1) {
		super(arg0, arg1);
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
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
