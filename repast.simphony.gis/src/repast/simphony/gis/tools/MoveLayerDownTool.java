package repast.simphony.gis.tools;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

import repast.simphony.gis.legend.MapLegend;

public class MoveLayerDownTool extends AbstractAction {

	private MapLegend legend;

	private MapContext context;

	public MoveLayerDownTool(MapLegend legend, MapContext context) {
		super();
		this.legend = legend;
		this.context = context;
	}

	public void actionPerformed(ActionEvent e) {
		MapLayer layer = legend.getSelectedLayer();
		if (layer == null)
			return;
		try {
			context.moveLayer(context.indexOf(layer), context
					.indexOf(layer) - 1);
		} catch (Exception ex) {
			// If this happens it's no biggie, we just don't move the layer.
		}
	}

}
