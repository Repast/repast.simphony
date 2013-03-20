package repast.simphony.visualization.gis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import repast.simphony.gis.data.DataUtilities;
import repast.simphony.gis.display.RepastMapLayer;
import repast.simphony.space.gis.DefaultFeatureAgentFactory;
import repast.simphony.space.gis.FeatureAgent;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.space.gis.Geography;
import simphony.util.ThreadUtilities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Handles the updating of a DisplayGIS w/r to agent adding, moving and removal.
 * 
 * @author Nick Collier
 */
public class Updater {

  // used to synchronize add features to a feature collection
  // that exists in a maplayer and drawing the map layers
  private Lock addFeaturesLock = new ReentrantLock();
  private Lock updateLock = new ReentrantLock();

  private Geography geog;
  private Map<Class, FeatureCollection> featureMap = 
  		new HashMap<Class, FeatureCollection>();
  private Map<Object, FeatureAgent> agentMap = new HashMap<Object, FeatureAgent>();

  private Set<Object> agentsToAdd = new HashSet<Object>();
  private Set<Object> agentsToRemove = new HashSet<Object>();
  private Map<Class, DefaultFeatureAgentFactory> factoryMap;
  private Map<Class, DefaultFeatureAgentFactory> renderMap;
  private Map<String, RepastMapLayer> layerMap = new HashMap<String, RepastMapLayer>();
  private Map<FeatureSource, Layer> featureLayerMap = new HashMap<FeatureSource, Layer>();
  private Set<Class> updateClasses = new HashSet<Class>();
  private Map<Integer, Object> layerOrder;
  // maps the original geometry of a object to
  // the object. This is necessary because GT renderer
  // seems to render with only the orig geometry.
  private Map<Object, Geometry> origGeomMap = new HashMap<Object, Geometry>();
  private boolean addRender = true;
  private boolean updateRender = true, reorder = false;
  private Styler styler;
  private CoordinateUpdater coordUpdater = new CoordinateUpdater();

  public Updater(MapContent mapContext, Geography geog, Styler styler,
      List<FeatureSource> featureSources, Map<Integer, Object> layerOrder) {
    this.geog = geog;
    this.styler = styler;
    factoryMap = new HashMap<Class, DefaultFeatureAgentFactory>();
    renderMap = new HashMap<Class, DefaultFeatureAgentFactory>();
    this.layerOrder = layerOrder;

    for (Object obj : geog.getAllObjects()) {
      agentsToAdd.add(obj);
    }

    try {
      addAgents();
      addBackgrounds(featureSources);
      render(mapContext);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void addBackgrounds(List<FeatureSource> sources) throws IOException {
    for (FeatureSource source : sources) {
      Layer layer = new FeatureLayer(source, styler.getStyle(source));
      featureLayerMap.put(source, layer);
      reorder = true;
    }
  }

  private void addAgents() {
    CoordinateReferenceSystem crs = geog.getCRS();
    FeatureAgentFactoryFinder finder = FeatureAgentFactoryFinder.getInstance();
    for (Object obj : agentsToAdd) {
      Class<? extends Object> clazz = obj.getClass();
      DefaultFeatureAgentFactory fac = factoryMap.get(clazz);
      if (fac == null) {

        fac = finder.getFeatureAgentFactory(clazz, geog.getGeometry(obj).getClass(), geog.getCRS());
        factoryMap.put(clazz, fac);
        reorder = true;
      }

      if (!fac.getCrs().equals(crs)) {
        fac = finder.getFeatureAgentFactory(clazz, geog.getGeometry(obj).getClass(), geog.getCRS());
        factoryMap.put(clazz, fac);
      }
      renderMap.put(clazz, fac);
      FeatureAgent fa = fac.getFeature(obj, geog);
      agentMap.put(obj, fa);
      origGeomMap.put(obj, fa.getDefaultGeometry());
    }

    agentsToAdd.clear();
  }

  private void resetFactories() {
    for (DefaultFeatureAgentFactory fac : factoryMap.values()) {
      fac.reset();
    }
  }
  
  private List testFeatures(){
  	List list = new ArrayList();
  	
  	try {
			String dataFileName = "sampleData/streams.shp"; 
//			String styleFileName = "sampleData/streams.xml";
			
			String styleFileName = "archsites.xml";
			
			URL shapefile = new File(dataFileName).toURL();
			
			ShapefileDataStore store = new ShapefileDataStore(shapefile);
			FeatureSource source = store.getFeatureSource();
			
			list.add(0,source);
			
			File styleFile = new File(styleFileName);
			StyleFactory fac = CommonFactoryFinder.getStyleFactory(null);
			SLDParser parser = new SLDParser(fac, styleFile);
			Style style = parser.readXML()[0];
			
			list.add(1,style);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
  	
  	
  	return list;
  }

  public void render(MapContent mapContext) {
    if (addRender) {
      for (Class clazz : renderMap.keySet()) {
        DefaultFeatureAgentFactory fac = renderMap.get(clazz);
        FeatureCollection newAgents = fac.getFeatures();
        FeatureCollection currentAgents = featureMap.get(clazz);
        if (currentAgents == null) {
          featureMap.put(clazz, newAgents);
          FeatureSource source = DataUtilities.createFeatureSource(newAgents);
          FeatureAgent agent = (FeatureAgent) newAgents.iterator().next();
          
          RepastMapLayer layer = new RepastMapLayer(source, 
          		styler.getStyle(clazz.getName(), 
          				agent.getDefaultGeometry().getClass()));
          
//          List list = testFeatures();
//          FeatureSource source2 = (FeatureSource)list.get(0);
//          Style style = (Style)list.get(1);
//          RepastMapLayer layer = new RepastMapLayer(source2, style, "Streams");
            
          
//          layer.setDynamic(true);
          layerMap.put(clazz.getName(), layer);
          
          reorder = true;
        } else {
          addFeaturesLock.lock();
          currentAgents.addAll(newAgents);
          addFeaturesLock.unlock();
        }
      }

      renderMap.clear();
      addRender = false;
    }

    if (reorder)
      ThreadUtilities.runInEventThread(new LayersAdder(mapContext, addFeaturesLock));
    	
    // we need to update every layer because although we can track
    // agents being added, removed and moved and update accordingly
    // we cannot track changes to an agent that would update the agent's
    // styled display.
    for (RepastMapLayer layer : layerMap.values()) {
      // RepastMapLayer mapLayer = layerMap.get(clazz.getName());
      ThreadUtilities.runInEventThread(new LayerUpdater(layer, addFeaturesLock));
    }
  }

  public void agentMoved(Object obj) {
    updateClasses.add(obj.getClass());
    Geometry geom = geog.getGeometry(obj);
    Geometry origGeom = origGeomMap.get(obj);
    // may be null if object is moved before
    // the added update occurs.
    if (origGeom != null && !geom.equals(origGeom)) {
      if (geom.getNumPoints() == origGeom.getNumPoints()) {
        coordUpdater.reset(geom.getCoordinates());
        origGeom.apply(coordUpdater);
        origGeom.geometryChanged();
      } else {
        // todo some scheme to clear the cached geometry
        // so that this will repaint with the correct geometry
      }
    }
    updateRender = true;
  }

  public void removeAgents() {
    try {
      addFeaturesLock.lock();
      for (Object obj : agentsToRemove) {
        origGeomMap.remove(obj);
        FeatureAgent fa = agentMap.remove(obj);
        updateClasses.add(obj.getClass());
      }
      
      agentsToRemove.clear();
    } finally {
      addFeaturesLock.unlock();
    }
  }

  public void update() {
    try {
      updateLock.lock();
      if (agentsToAdd.size() > 0) {
        resetFactories();
        addAgents();
        addRender = true;
      }
    } finally {
      updateLock.unlock();
    }

    if (agentsToRemove.size() > 0) {
      removeAgents();
      updateRender = true;
    }

  }

  /**
   * Called when an agent has been added and the display needs to be updated.
   * 
   * @param agent
   *          the added agent
   */
  public void agentAdded(Object agent) {
    try {
      updateLock.lock();
      agentsToAdd.add(agent);
    } finally {
      updateLock.unlock();
    }
  }

  /**
   * Called when an agent has been removed and the display needs to be updated.
   * 
   * @param agent
   *          the removed agent
   */
  public void agentRemoved(Object agent) {
    try {
      addFeaturesLock.lock();
      if (!agentsToAdd.remove(agent)) {
        agentsToRemove.add(agent);
      }
    } finally {
      addFeaturesLock.unlock();
    }
  }

  class LayersAdder implements Runnable {

    private MapContent context;
    private Lock lock;

    LayersAdder(MapContent context, Lock lock) {
      this.context = context;
      this.lock = lock;
    }

    public void run() {
      lock.lock();
      reorderLayers();
      reorder = false;
      lock.unlock();
    }

    private void reorderLayers() {
      List<Integer> indices = new ArrayList<Integer>(layerOrder.keySet());
      Collections.sort(indices);
      Collections.reverse(indices);
      List<Layer> layers = context.layers();
      for (Layer layer : layers) {
        context.removeLayer(layer);
      }

      for (Integer val : indices) {
        Object obj = layerOrder.get(val);
        Layer layer = featureLayerMap.get(obj);
        if (layer != null)
          context.addLayer(layer);
        else {
          RepastMapLayer mapLayer = layerMap.get(obj);
          // layers may be null because agents of that type
          // might not have been added to the geography yet
          if (mapLayer != null){
            context.addLayer(mapLayer);           
          }
        }
      }
    }
  }

  static class LayerUpdater implements Runnable {

    private RepastMapLayer layer;
    private Lock lock;

    LayerUpdater(RepastMapLayer layer, Lock lock) {
      this.layer = layer;
      this.lock = lock;
    }

    public void run() {
      lock.lock();
      layer.fireMapLayerChangedEvent(new MapLayerEvent(layer, MapLayerEvent.DATA_CHANGED));
      lock.unlock();
    }
  }

  static class CoordinateUpdater implements CoordinateFilter {

    int index = 0;
    Coordinate[] newCoords;

    public void filter(Coordinate coordinate) {
      Coordinate coord = newCoords[index++];
      coordinate.x = coord.x;
      coordinate.y = coord.y;
    }

    void reset(Coordinate[] newCoords) {
      this.newCoords = newCoords;
      index = 0;
    }
  }

}
