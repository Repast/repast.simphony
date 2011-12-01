package repast.simphony.gis.id;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;

public interface LayerIDCallback {

	public void handleLayerID(FeatureType type, FeatureCollection collection);

}
