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

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
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

  // used to synchronize add features to a feature collection map layer
  private Lock updateLock = new ReentrantLock();

  private Geography geog;

  // Map FeatureCollections to the agent or static layer name.  Each 
  //   FeatureCollection will be represented in a different map layer.
  private Map<String, FeatureCollection> featureCollectionMap = 
  		new HashMap<String, FeatureCollection>();
  
  // Map the object (agent) to the FeatureAgent instance that is displayed
  private Map<Object, FeatureAgent> agentMap = new HashMap<Object, FeatureAgent>();
  
  // The FeatureAgent objects to be added/removed to/from the FeatureCollection
  //  between updates and render calls
  private Map<Class, Set<FeatureAgent>> featuresToAddMap = new HashMap<Class, Set<FeatureAgent>>();
  private Map<Class, Set<FeatureAgent>> featuresToRemoveMap = new HashMap<Class, Set<FeatureAgent>>();
 
  // The actual agents to be added / removed between update and render calls
  private Set<Object> agentsToAdd = new HashSet<Object>();
  private Set<Object> agentsToRemove = new HashSet<Object>();
  
  private Map<Class, DefaultFeatureAgentFactory> factoryMap;
  private Map<Class, DefaultFeatureAgentFactory> renderMap;
  private List<RepastMapLayer> repastMapLayerList = new ArrayList<RepastMapLayer>();
  
  // Layer order maps a layer ID to an integer ordering
  private Map<Integer, String> layerOrder;

  // maps the original geometry of a object to the object. This is necessary 
  // because GT renderer seems to render with only the orig geometry.
//  private Map<Object, Geometry> origGeomMap = new HashMap<Object, Geometry>();
  private boolean addRender = true;
  private boolean reorder = false;
  private Styler styler;
//  private CoordinateUpdater coordUpdater = new CoordinateUpdater();

  public Updater(MapContent mapContext, Geography geog, Styler styler,
      Map<String,FeatureCollection> staticFeatures, List<Class> registeredClasses,
      Map<Integer, String> layerOrder) {
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
      addBackgrounds(staticFeatures);
      render(mapContext);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void addBackgrounds(Map<String,FeatureCollection> staticFeatures) throws IOException {      
  	featureCollectionMap.putAll(staticFeatures);
  	reorder = true;
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
        FeatureCollection agentCollection = featureCollectionMap.get(clazz.getName());
        Set<FeatureAgent> featureAgentsToAdd = featuresToAddMap.get(clazz);

        // If there is a new agent type, then create a new FeatureCollection for the type
        if (agentCollection == null) {  
        	agentCollection = new DefaultFeatureCollection(null,null);
        	((DefaultFeatureCollection)agentCollection).addAll(featureAgentsToAdd);
        	
        	featureCollectionMap.put(clazz.getName(), agentCollection);          
          reorder = true;
        } 
        else { // otherwise just add the agents to the layer
        	updateLock.lock();
        	((DefaultFeatureCollection)agentCollection).addAll(featureAgentsToAdd);
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
    		FeatureCollection agentCollection = featureCollectionMap.get(clazz.getName());

    		updateLock.lock();
    		((DefaultFeatureCollection)agentCollection).removeAll(featureAgentsToRemove);
    		updateLock.unlock();

    		featureAgentsToRemove.clear();
    	}
    }

    if (reorder)
      ThreadUtilities.runInEventThread(new LayersAdder(mapContext, updateLock));
    	
    for (RepastMapLayer layer : repastMapLayerList) {
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

      // First remove and dispose all of the existing layers
      for (Layer layer : mapContent.layers()){
        mapContent.removeLayer(layer);
        repastMapLayerList.remove(layer);
        layer.dispose();
      }
      
      // Then re-create layers in order and add them
      for (Integer order : indices) {
        String layerID = layerOrder.get(order);
        if (featureCollectionMap.get(layerID) != null){
        		RepastMapLayer layer = new RepastMapLayer(featureCollectionMap.get(layerID), 
        			styler.getStyle(layerID));

        	mapContent.addLayer(layer);
        	repastMapLayerList.add(layer);
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