package repast.simphony.visualization.gis;

import java.io.IOException;
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
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapLayerEvent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import repast.simphony.gis.display.RepastMapLayer;
import repast.simphony.space.gis.DefaultFeatureAgentFactory;
import repast.simphony.space.gis.FeatureAgent;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.space.gis.Geography;
import simphony.util.ThreadUtilities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;

/**
 * Handles the updating of a DisplayGIS w/r to agent adding, moving and removal.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class Updater {

  // used to synchronize add features to a feature collection
  // that exists in a maplayer and drawing the map layers
  private Lock updateLock = new ReentrantLock();

  private Geography geog;
    
  // Note that DefaultFeatureCollection should be used to store an in memory
  //  FeatureCollection in the Geotools 9.0 API
  private Map<Class, DefaultFeatureCollection> featureMap = 
  		new HashMap<Class, DefaultFeatureCollection>();
  
  // Map the object (agent) to the FeatureAgent instance that is displayed
  private Map<Object, FeatureAgent> agentMap = new HashMap<Object, FeatureAgent>();
  
  // The FeatureAgent objects to be added/removed to/from the FeatureCollection
  private Map<Class, Set<FeatureAgent>> featuresToAddMap = new HashMap<Class, Set<FeatureAgent>>();
  private Map<Class, Set<FeatureAgent>> featuresToRemoveMap = new HashMap<Class, Set<FeatureAgent>>();
 
  // The actual agents to be added / removed
  private Set<Object> agentsToAdd = new HashSet<Object>();
  private Set<Object> agentsToRemove = new HashSet<Object>();
  
  private Map<Class, DefaultFeatureAgentFactory> factoryMap;
  private Map<Class, DefaultFeatureAgentFactory> renderMap;
  private Map<String, RepastMapLayer> layerMap = new HashMap<String, RepastMapLayer>();
  private Map<FeatureSource, Layer> featureLayerMap = new HashMap<FeatureSource, Layer>();

  private Map<Integer, Object> layerOrder;

  // maps the original geometry of a object to the object. This is necessary 
  // because GT renderer seems to render with only the orig geometry.
//  private Map<Object, Geometry> origGeomMap = new HashMap<Object, Geometry>();
  private boolean addRender = true;
  private boolean reorder = false;
  private Styler styler;
//  private CoordinateUpdater coordUpdater = new CoordinateUpdater();

  public Updater(MapContent mapContext, Geography geog, Styler styler,
      List<FeatureSource> featureSources, List<Class> registeredClasses,
      Map<Integer, Object> layerOrder) {
    this.geog = geog;
    this.styler = styler;
    factoryMap = new HashMap<Class, DefaultFeatureAgentFactory>();
    renderMap = new HashMap<Class, DefaultFeatureAgentFactory>();
    this.layerOrder = layerOrder;

//    initMapLayers(mapContext, registeredClasses, styler);
    
    for (Object obj : geog.getAllObjects()) {
      agentsToAdd.add(obj);
    }

//    addAgents();
//    render(mapContext);
    try {
      addAgents();
      addBackgrounds(featureSources);
      render(mapContext);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initMapLayers(MapContent mapContext, List<Class> registeredClasses, 
  		Styler styler){
  	for (Class clazz : registeredClasses){

  		DefaultFeatureCollection agentCollection = new DefaultFeatureCollection();
  		featureMap.put(clazz, agentCollection);

  		RepastMapLayer layer = new RepastMapLayer(agentCollection, 
  				styler.getStyle(clazz.getName()));

  		layerMap.put(clazz.getName(), layer);
  		
  		mapContext.addLayer(layer);
  	}
//  	reorder = true;

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
      
      Set<FeatureAgent> featureAgentsToAdd = featuresToAddMap.get(clazz);
      
      if (featureAgentsToAdd == null){
      	featureAgentsToAdd = new HashSet<FeatureAgent>();
      	featuresToAddMap.put(clazz, featureAgentsToAdd);
      }
      
      featureAgentsToAdd.add(fa);
    }
    agentsToAdd.clear();
  }

  public void render(MapContent mapContext) {
    if (addRender) {  // if new agents have been added
      for (Class clazz : renderMap.keySet()) {
        DefaultFeatureAgentFactory fac = renderMap.get(clazz);
                
        // FeatureCollection of FeatureAgents for this agent class
        DefaultFeatureCollection agentCollection = featureMap.get(clazz);
        Set<FeatureAgent> featureAgentsToAdd = featuresToAddMap.get(clazz);

        // If there is a new agent type, then create a new layer for the type
        if (agentCollection == null) {  
        	agentCollection = new DefaultFeatureCollection(null,null);
        	agentCollection.addAll(featureAgentsToAdd);
        	
          featureMap.put(clazz, agentCollection);

          Class geomClass = fac.getFeatureType().getGeometryDescriptor().getType().getBinding();

          RepastMapLayer layer = new RepastMapLayer(agentCollection, 
          		styler.getStyle(clazz.getName(), geomClass));

          layerMap.put(clazz.getName(), layer);          
          reorder = true;

        } 
        else { // otherwise just add the agents to the layer
        	updateLock.lock();
          agentCollection.addAll(featureAgentsToAdd);
          updateLock.unlock();
        }  

        if (featureAgentsToAdd != null)
        	featureAgentsToAdd.clear();
      }
      renderMap.clear();
      addRender = false;
    }
    
    // Remove FeatureAgents for agents that have been removed
    for (Class clazz : featuresToRemoveMap.keySet()){
    	Set<FeatureAgent> featureAgentsToRemove = featuresToRemoveMap.get(clazz);

    	if (featureAgentsToRemove != null){
    		DefaultFeatureCollection agentCollection = featureMap.get(clazz);

    		updateLock.lock();
    		agentCollection.removeAll(featureAgentsToRemove);
    		updateLock.unlock();

    		featureAgentsToRemove.clear();
    	}
    }

    if (reorder)
      ThreadUtilities.runInEventThread(new LayersAdder(mapContext, updateLock));
    	
    // we need to update every layer because although we can track
    // agents being added, removed and moved and update accordingly
    // we cannot track changes to an agent that would update the agent's
    // styled display.
    for (RepastMapLayer layer : layerMap.values()) {
      ThreadUtilities.runInEventThread(new LayerUpdater(layer, updateLock));    	
    }
  }

  // TODO Geotools [minor] - this doesn't seem to be needed to actually update the position
  //      of the FeatureAgents since the Geometry would be directly manipulated
  //      by the user model or through the Geograby.moveBy...().
  //      This might only be needed if the geometry type changes...which it 
  //      shouldn't.  Save for later?
  public void agentMoved(Object obj) {
//    Geometry geom = geog.getGeometry(obj);
//    Geometry origGeom = origGeomMap.get(obj);
//    // may be null if object is moved before
//    // the added update occurs.
//    if (origGeom != null && !geom.equals(origGeom)) {
//      if (geom.getNumPoints() == origGeom.getNumPoints()) {
//        coordUpdater.reset(geom.getCoordinates());
//        origGeom.apply(coordUpdater);
//        origGeom.geometryChanged();
//      } else {
//        // TODO (legacy) some scheme to clear the cached geometry
//        // so that this will repaint with the correct geometry
//      }
//    }
  }
 
  private void removeAgents() {
    try {
      updateLock.lock();
      for (Object obj : agentsToRemove) {
//        origGeomMap.remove(obj);
        FeatureAgent fa = agentMap.remove(obj);
       
        Set<FeatureAgent> featureAgentsToRemove = featuresToRemoveMap.get(obj.getClass());
        
        if (featureAgentsToRemove == null){
        	featureAgentsToRemove = new HashSet<FeatureAgent>();
        	featuresToRemoveMap.put(obj.getClass(), featureAgentsToRemove);
        }
        featureAgentsToRemove.add(fa);
      }
      agentsToRemove.clear();
    } finally {
    	updateLock.unlock();
    }
  }

  public void update() {
    try {
      updateLock.lock();
      if (agentsToAdd.size() > 0) {
        addAgents();
        addRender = true;
      }
      if (agentsToRemove.size() > 0) {
        removeAgents();
      }
    } finally {
      updateLock.unlock();
    }    
  }

  /**
   * Called when an agent has been added and the display needs to be updated.
   * 
   * @param agent the added agent
   */
  public void agentAdded(Object agent) {
    try {
      updateLock.lock();
      agentsToAdd.add(agent);

      // in case the agent is removed and added in the same step
      agentsToRemove.remove(agent);  
    } finally {
      updateLock.unlock();
    }
  }

  /**
   * Called when an agent has been removed and the display needs to be updated.
   * 
   * @param agent the removed agent
   */
  public void agentRemoved(Object agent) {
    try {
    	updateLock.lock();
      if (!agentsToAdd.remove(agent)) {
        agentsToRemove.add(agent);
      }
    } finally {
    	updateLock.unlock();
    }
  }

  class LayersAdder implements Runnable {

    private MapContent mapContent;
    private Lock lock;

    LayersAdder(MapContent mapContent, Lock lock) {
      this.mapContent = mapContent;
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
      List<Layer> layers = mapContent.layers();
      for (Layer layer : layers) {
      	mapContent.removeLayer(layer);
      }

//      for (Layer layer : layerMap.values()){
//      	if (!mapContent.layers().contains(layer)){
//      		mapContent.addLayer(layer);
//      	}
//      }
            
      for (Integer val : indices) {
        Object obj = layerOrder.get(val);
        Layer layer = featureLayerMap.get(obj);
        if (layer != null)
        	mapContent.addLayer(layer);
        else {
          RepastMapLayer mapLayer = layerMap.get(obj);
          // layers may be null because agents of that type
          // might not have been added to the geography yet
          if (mapLayer != null){
          	mapContent.addLayer(mapLayer);           
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