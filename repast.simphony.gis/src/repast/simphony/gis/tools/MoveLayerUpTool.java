package repast.simphony.gis.tools;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

import repast.simphony.gis.legend.MapLegend;

public class MoveLayerUpTool extends AbstractAction {

	private MapLegend legend;

	private MapContext context;

	public MoveLayerUpTool(MapLegend legend, MapContext context) {
		super();
		this.legend = legend;
		this.context = context;
	}

	public void actionPerformed(ActionEvent e) {
		MapLayer layer = legend.getSelectedLayer();
		if (layer == null)
			return;
		int currentIndex = context.indexOf(layer);
		try {
			context.moveLayer(currentIndex, currentIndex + 1);
		} catch (Exception ex) {
			// If this happens, no biggie, just don't move the layer
		}
	}
}
