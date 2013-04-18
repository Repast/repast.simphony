package repast.simphony.gis.util;

import java.io.IOException;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.collection.CollectionDataStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

import simphony.util.messages.MessageCenter;

/**
 * Utility methods for working with gis data.
 *
 * @author Nick Collier
 * @author Eric Tatara
 */
public class DataUtilities {

	private static MessageCenter msg = MessageCenter.getMessageCenter(DataUtilities.class);

	/**
	 * Creates a FeatureSource from a list of SimpleFeatures to be used in memory.
	 * 
	 * @param features the list of SimpleFeatures
	 * @return a FeatureSource
	 */
	public static FeatureSource createFeatureSource(List<SimpleFeature> features) {

		DefaultFeatureCollection collection = new DefaultFeatureCollection(null,null);
		
		collection.addAll(features);
		
		return createFeatureSource(collection);
	}
	
	/**
	 * Adapts the specified feature collection into a FeatureSource with
	 * the specified FeatureType.
	 *
	 * @param collection the collection to adapt.
	 * @param type the feature type
	 * @return the created FeatureSource
	 */
	public static FeatureSource createFeatureSource(FeatureCollection collection) {

		DataStore store = new CollectionDataStore(collection);

		try {
			return store.getFeatureSource(store.getTypeNames()[0]);
		} catch (IOException e) {
			msg.error("Error creating feature source from FeatureCollection", e);
		}
		return null;
	}
}
