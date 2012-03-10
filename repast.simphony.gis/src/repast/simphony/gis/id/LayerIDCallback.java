package repast.simphony.gis.id;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;

public interface LayerIDCallback {

	public void handleLayerID(SimpleFeatureType type, SimpleFeatureCollection collection);

}
