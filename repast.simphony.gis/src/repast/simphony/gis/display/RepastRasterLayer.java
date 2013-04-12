package repast.simphony.gis.display;

import static org.geotools.map.event.MapLayerEvent.DATA_CHANGED;
import static org.geotools.map.event.MapLayerEvent.FILTER_CHANGED;
import static org.geotools.map.event.MapLayerEvent.STYLE_CHANGED;
import static org.geotools.map.event.MapLayerEvent.VISIBILITY_CHANGED;

import java.util.ArrayList;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.Style;

/**
 * Repast Raster Layer that wraps Geotools GridCoverageLayer
 * 
 * @author Eric Tatara
 *
 */
public class RepastRasterLayer extends GridCoverageLayer {

	public RepastRasterLayer(GridCoverage2D coverage, Style style, String title) {
		super(coverage, style, title);
	}

	public RepastRasterLayer(GridCoverage2D coverage, Style style) {
		super(coverage, style);
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