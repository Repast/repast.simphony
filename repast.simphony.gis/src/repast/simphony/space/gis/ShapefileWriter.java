package repast.simphony.space.gis;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import simphony.util.messages.MessageCenter;

/**
 * Writes the contents of a geography to a shapefile each layer of the
 * geography is written to a separate shapefile.
 *
 * @author Nick Collier
 * @author Eric Tatara
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
    List<SimpleFeature> features = new ArrayList<SimpleFeature>();
    Layer layer = geography.getLayer(layerName);
    
    ShapefileFeatureAgentFactory fac = FeatureAgentFactoryFinder.getInstance().
            getShapefileFeatureAgentFactory(layer.getAgentType(), 
            		layer.getGeomType(), geography.getCRS());

    for (Object obj : layer.getAgentSet()) {
      features.add(fac.getFeature(obj, geography));
    }
    
    SimpleFeatureType type = fac.getFeatureType();
    
    try {
      write(url, features, type);
    } catch (IOException ex) {
      msg.error("Error writing geography to shapefile", ex);
    }
  }

  private void write(URL url, List<SimpleFeature> features, SimpleFeatureType type) throws IOException {
  	ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
      	
  	Map<String, Serializable> params = new HashMap<String, Serializable>();
    params.put("url", url);
    params.put("create spatial index", Boolean.TRUE);
  	
    ShapefileDataStore store = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
    store.forceSchemaCRS(geography.getCRS());
    store.createSchema(type);

    String featureName = store.getTypeNames()[0]; // there is only one in a shapefile
    Transaction transaction = new DefaultTransaction("create");
    SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource(featureName);
    fs.setTransaction(transaction);
    
    SimpleFeatureCollection collection = new ListFeatureCollection(type, features);
    
    try {
    	fs.addFeatures(collection);
    	transaction.commit();
    } catch (Exception problem) {
    	problem.printStackTrace();
    	transaction.rollback();
    } finally {
    	transaction.close();
    }
  }
}