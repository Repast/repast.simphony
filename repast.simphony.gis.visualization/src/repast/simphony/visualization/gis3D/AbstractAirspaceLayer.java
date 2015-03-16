package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.layers.AirspaceLayer;
import gov.nasa.worldwind.render.airspaces.Airspace;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.IDisplayLayer;
import repast.simphony.visualization.LayoutUpdater;

/**
 * Abstract base class for WorldWind GIS display Airspace layers.
 * 
 * @author Eric Tatara
 * 
 * TODO WWJ - consolidate share code with AbstractRenderableLayer ?
 *
 */
public abstract class AbstractAirspaceLayer<S,T> extends AirspaceLayer implements IDisplayLayer<T> {

	protected Map<Object, T> visualItemMap;
	protected Map<Airspace, Object> airspaceToObjectMap;

	protected S style;
	protected Model model;
	protected Geography geography;
	protected Set<Object> addedObjects;
	protected Set<Object> removeObjects;
	
	public AbstractAirspaceLayer(String name, S style){
		setName(name);
		this.style = style;
		addedObjects  = new HashSet<Object>();
		removeObjects = new HashSet<Object>();
		visualItemMap = new HashMap<Object, T>();
		airspaceToObjectMap = new HashMap<Airspace,Object>();

		setPickEnabled(true);
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

  /**
   * Set the gov.nasa.worldwind.Model for this display.
   * 
   * @param model
   */
  public void setModel(Model model) {
    this.model = model;
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
  public abstract void update(LayoutUpdater updater);
  
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
  public Object findObjectForRenderable(Airspace airspace) {
    return airspaceToObjectMap.get(airspace);
  }
}
