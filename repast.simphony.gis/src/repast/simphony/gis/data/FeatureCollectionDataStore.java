package repast.simphony.gis.data;

import java.io.IOException;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.feature.CollectionEvent;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Adapts a FeatureCollection to a DataStore. This fixes a bug in
 * geotools' CollectionDataStore.
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public class FeatureCollectionDataStore extends AbstractDataStore {

	protected FeatureType featureType;
	protected FeatureCollection collection;

	/**
	 * Builds a data store wrapper on top of a feature collection
	 *
	 * @param collection
	 */
	public FeatureCollectionDataStore(FeatureCollection collection) {
		this.collection = collection;

		if (collection.size() == 0) {
			this.featureType = DefaultFeatureType.EMPTY;
		} else {
			this.featureType = ((Feature) collection.iterator().next()).getFeatureType();
		}

		collection.addListener(new FeatureCollectionListener());
	}

	/**
	 * Builds a data store wrapper on top of a feature collection
	 *
	 * @param collection
	 * @param type
	 */
	public FeatureCollectionDataStore(FeatureCollection collection, FeatureType type) {
		this.collection = collection;
		this.featureType = type;
		collection.addListener(new FeatureCollectionListener());
	}



	/**
	 * @see org.geotools.data.DataStore#getTypeNames()
	 */
	public String[] getTypeNames() {
		return new String[]{featureType.getTypeName()};
	}

	/**
	 * @see org.geotools.data.DataStore#getSchema(java.lang.String)
	 */
	public FeatureType getSchema(String typeName) throws IOException {
		if ((typeName != null) && typeName.equals(featureType.getTypeName())) {
			return featureType;
		}

		throw new IOException(typeName + " not available");
	}

	/**
	 * Provides FeatureReader over the entire contents of <code>typeName</code>.
	 * <p/>
	 * <p/>
	 * Implements getFeatureReader contract for AbstractDataStore.
	 * </p>
	 *
	 * @param typeName
	 * @throws IOException If typeName could not be found
	 * @throws org.geotools.data.DataSourceException
	 *                     See IOException
	 * @see org.geotools.data.AbstractDataStore#getFeatureSource(java.lang.String)
	 */
	public FeatureReader getFeatureReader(final String typeName)
					throws IOException {
		return new DelegateFeatureReader(getSchema(typeName), collection.features());
	}

	/**
	 * Returns the feature collection held by this data store
	 */
	public FeatureCollection getCollection() {
		return collection;
	}

	/**
	 * @throws org.geotools.data.SchemaNotFoundException
	 *
	 */
	protected Envelope getBounds(Query query) throws SchemaNotFoundException {
		String featureTypeName = query.getTypeName();
		if (!featureType.getTypeName().equals(featureTypeName)) {
			throw new SchemaNotFoundException(featureTypeName);
		}

		return getBoundsInternal(query);
	}

	/**
	 * @param query
	 */
	protected Envelope getBoundsInternal(Query query) {
		FeatureIterator iterator = collection.features();
		Envelope envelope = null;

		if (iterator.hasNext()) {
			int count = 1;
			Filter filter = query.getFilter();
			envelope = new Envelope(iterator.next().getDefaultGeometry().getEnvelopeInternal());

			while (iterator.hasNext() && (count < query.getMaxFeatures())) {
				Feature feature = iterator.next();

				if (filter.contains(feature)) {
					count++;
					envelope.expandToInclude(feature.getDefaultGeometry().getEnvelopeInternal());
				}
			}
		}

		return envelope;

	}

	/**
	 *
	 */
	protected int getCount(Query query)
					throws IOException {
		String featureTypeName = query.getTypeName();
		if (!featureType.getTypeName().equals(featureTypeName)) {
			throw new SchemaNotFoundException(featureTypeName);
		}
		int count = 0;
		FeatureIterator iterator = collection.features();

		Filter filter = query.getFilter();

		while (iterator.hasNext() && (count < query.getMaxFeatures())) {
			if (filter.contains(iterator.next())) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Simple listener that forwards collection events into data store events
	 *
	 * @author aaime
	 */
	private class FeatureCollectionListener implements CollectionListener {
		public void collectionChanged(CollectionEvent tce) {
			String typeName = featureType.getTypeName();
			Envelope bounds = null;

			bounds = getBoundsInternal(Query.ALL);

			switch (tce.getEventType()) {
				case CollectionEvent.FEATURES_ADDED:
					listenerManager.fireFeaturesAdded(typeName, Transaction.AUTO_COMMIT, bounds, false);

					break;

				case CollectionEvent.FEATURES_CHANGED:
					listenerManager.fireFeaturesChanged(typeName, Transaction.AUTO_COMMIT, bounds, false);

					break;

				case CollectionEvent.FEATURES_REMOVED:
					listenerManager.fireFeaturesRemoved(typeName, Transaction.AUTO_COMMIT, bounds, false);

					break;
			}
		}
	}
}
