package repast.simphony.visualization.gis3D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Renderable;
import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.IDisplayLayer;
import repast.simphony.visualization.LayoutUpdater;

/**
 * Abstract base class for WorldWind GIS display Renderable layers.
 * 
 * @author Eric Tatara
 * 
 */
public abstract class AbstractRenderableLayer<S,T extends Renderable> 
			extends RenderableLayer implements IDisplayLayer<Object>{

  protected Map<Object, T> visualItemMap;
  protected Map<Renderable, Object> renderableToObjectMap;

  protected S style;
  protected Geography<?> geography;
  protected Set<Object> addedObjects;
  protected Set<Object> removeObjects;

  /**
   * Defines how the implementing subclasses apply updates to Renderables, e.g.
   *   setting line colors or updating coordinates.
   *   
   * @param o the object (agent) to be used for styling the renderable
   */
  protected abstract void applyUpdatesToShape(Object o);

  /**
   * Defines how the implementing subclasses create Renderables for objects in
   *   the display
   *   
   * @param o the object for which to create the renderable
   * @return the Renderable to be displayed
   */
  protected abstract T createVisualItem(Object o);
  
  public AbstractRenderableLayer(String name, S style){
  	setName(name);
  	this.style = style;
  	
  	addedObjects = new HashSet<Object>();
  	removeObjects = new HashSet<Object>();
  	visualItemMap = new HashMap<Object, T>();
  	renderableToObjectMap = new HashMap<Renderable, Object>();

  	this.setPickEnabled(true);
  }

  /**
   * Gets the style used by this display layer.
   * 
   * @return the style used by this display layer.
   */
  public S getStyle() {
    return style;
  }

  /**
   * Sets the style used by this display layer.
   * 
   * @param style the new style
   */
  public void setStyle(S style) {
    this.style = style;
  }
  
  /**
   * Set the geography for this display.
   * 
   * @param geography
   */
  public void setGeography(Geography geography) {
    this.geography = geography;
  }
  
  @Override
  public void addObject(Object o) {
    // remove it from the removed objects because
    // there might be a remove and then add in the same
    // update -- for example when a object switches contexts
    // the display is part of their parent context
  	removeObjects.remove(o);
  	addedObjects.add(o);
  }
  
  /**
   * No-op for WWJ based code as the displaying the Renderable will reflect
   * the updates. Nothing needs to be done here.
   */
  @Override 
  public void applyUpdates() {}
  
  @Override
  /**
   * Gets the visual item that represents the specified object in the display.
   * 
   * @return the visual item that represents the specified object in the display.
   */
  public T getVisualItem(Object o) {
    return visualItemMap.get(o);
  }
  
  /**
   * Removes this specified object from this layer.
   * 
   * @param obj
   *          the object to remove
   */
  @Override
  public void removeObject(Object o) {
    // if the object to remove is addedObjects
    // we don't need to actually remove it because it
    // hasn't been added yet -- so just remove it from there
    // otherwise add it to the objects to remove.
    if (!addedObjects.remove(o))
      removeObjects.add(o);
  }
  
  /**
   * Updates the displayed nodes by applying styles etc. The display is not
   * updated to reflect these changes.
   */
  @Override
  public void update(LayoutUpdater updater) {
    processRemovedObjects();
    processAddedObjects();
    updateObjects(updater);
    
    firePropertyChange(AVKey.LAYER, null, this);
  }
  
  @Override   // TODO WWJ - find out if this can be handled better
  /**
   * 
   * Override dispose() to prevent losing renderables on frame resize/dock.
   */
  public void dispose() {}

  /**
   * Returns the object that is associated with the renderable argument.
   *   
   * @param renderable the Renderable
   * @return the object associated with the renderable in this display.
   */
  public Object findObjectForRenderable(Renderable renderable) {
    return renderableToObjectMap.get(renderable);
  }
  
  /**
   * Apply style to all objects that exist in the layer
   * 
   * @param updater
   */
  protected void updateObjects(LayoutUpdater updater){
  	for (Object o : visualItemMap.keySet()){
  		applyUpdatesToShape(o);
  	}
  }
  
  /**
   * Create visual items for each object added since the last update.
   */
  protected void processAddedObjects() {
    for (Object o : addedObjects) {
    	T renderable = createVisualItem(o);
    	
    	if (renderable == null) return;
    	
    	renderableToObjectMap.put(renderable, o);
    	addRenderable(renderable);
    }
    addedObjects.clear();
  }
  
  /**
   * Remove any items and their renderables since the last update.
   */
  protected void processRemovedObjects() {
    for (Object o : removeObjects) {
    	T renderable  = visualItemMap.remove(o);
      if (renderable != null) {
        removeRenderable(renderable);
        renderableToObjectMap.remove(renderable);
      }
    }
    removeObjects.clear();
  }
}