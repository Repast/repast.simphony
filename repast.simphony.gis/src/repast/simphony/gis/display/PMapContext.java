package repast.simphony.gis.display;

import edu.umd.cs.piccolo.PNode;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;

import java.util.List;
import java.util.Map;

public class PMapContext extends PNode {

	private Map<MapLayer, PGisLayer> layerMap;

	private List<PGisLayer> layerList;

	public void addLayer(MapLayer layer) {

	}

	public static void main(String[] args) {
		MapLayer[] layers = new MapLayer[10];
		for (int i = 0; i < 10; i++) {
			MapLayer layer = new DefaultMapLayer((FeatureCollection) null,
					(Style) null);
			layer.setTitle("Layer " + i);
		}

	}
}
