package repast.simphony.space.gis;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import simphony.util.messages.MessageCenter;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

/**
 * Writes the contents of a geography to a shapefile each layer of the
 * geography is written to a separate shapefile.
 *
 * @author Nick Collier
 */
public class ShapefileWriter {

  private static MessageCenter msg = MessageCenter.getMessageCenter(ShapefileWriter.class);

  private Geography geography;

  /**
   * Creates a ShapefileWriter that will write layers
   * in the specified geography to a shapefile.
   *
   * @param geography the geography to write
   */
  public ShapefileWriter(Geography geography) {
    this.geography = geography;
  }


  /**
   * Writes named geography layer to the specified URL.
   *
   * @param layerName the name of the layer to write
   * @param url       url to write the layer to
   */
  public void write(String layerName, URL url) {
    ShapefileFeatureAgentFactory fac = createFactory(layerName);
    FeatureCollection collection = fac.getFeatures();
    FeatureType type = fac.getFeatureType();
    try {
      write(url, collection, type);
    } catch (IOException ex) {
      msg.error("Error writing geography to shapefile", ex);
    }
  }

  private void write(URL url, FeatureCollection collection, FeatureType type) throws IOException {
    FileDataStoreFactorySpi factory = new IndexedShapefileDataStoreFactory();
    Map<String, URL> map = Collections.singletonMap("shapefile url", url);
    ShapefileDataStore store = (ShapefileDataStore) factory.createNewDataStore(map);
    store.forceSchemaCRS(geography.getCRS());
    store.createSchema(type);

    String featureName = store.getTypeNames()[0]; // there is only one in a shapefile
    Transaction transaction = new DefaultTransaction();
    FeatureStore fs = (FeatureStore) store.getFeatureSource(featureName);
    fs.setTransaction(transaction);
    try {
      fs.addFeatures(collection);
      transaction.commit();
    } finally {
      transaction.close();
    }
  }


  private ShapefileFeatureAgentFactory createFactory(String layerName) {
    Layer layer = geography.getLayer(layerName);
    ShapefileFeatureAgentFactory fac = FeatureAgentFactoryFinder.getInstance().
            getShapefileFeatureAgentFactory(layer.getAgentType(), layer.getGeomType(), geography.getCRS());

    for (Object obj : layer.getAgentSet()) {
      fac.getFeature(obj, geography);
    }

    return fac;
  }
}


