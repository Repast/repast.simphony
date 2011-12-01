package repast.simphony.gis.data;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;

import simphony.util.messages.MessageCenter;

/**
 * Utility methods for working with gis data.
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public class DataUtilities {

	private static MessageCenter msg = MessageCenter.getMessageCenter(DataUtilities.class);

	/**
	 * Adapts the specified feature collection into a FeatureSource with
	 * the specified FeatureType.
	 *
	 * @param collection the collection to adapt.
	 * @param type the feature type
	 * @return the created FeatureSource
	 */
	public static FeatureSource createFeatureSource(FeatureCollection collection, FeatureType type) {

		DataStore store = new FeatureCollectionDataStore(collection, type);

		try {
			return store.getFeatureSource(store.getTypeNames()[0]);
		} catch (IOException e) {
			msg.error("Error creating feature source from FeatureCollection", e);
		}
		return null;
	}

	/**
	 * Adapts the specified feature collection into a FeatureSource. The
	 * FeatureType is inferred from the first element in the collection.
	 *
	 * @param collection the collection to adapt.
	 * @return the created FeatureSource
	 */
	public static FeatureSource createFeatureSource(FeatureCollection collection) {

		DataStore store = new FeatureCollectionDataStore(collection);

		try {
			return store.getFeatureSource(store.getTypeNames()[0]);
		} catch (IOException e) {
			msg.error("Error creating feature source from FeatureCollection", e);
		}
		return null;
	}
}
