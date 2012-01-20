package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastSet;
import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.LayoutUpdater;

/**
 * 
 * @author Eric Tatara
 *
 */
public abstract class AbstractSurfaceLayer extends RenderableLayer {

	protected Map<Object, GeoShape> visualItemMap;
	protected Map<Renderable, Object> renderableToObjectMap;

//	protected WorldWindowGLCanvas wwglCanvas;
	protected Model model;
	protected Geography geography;
	protected Set<Object> addedObjects;
	protected Set<Object> removeObjects;
	protected List<GeoShape> addedShapes;
	
	protected abstract void applyUpdatesToShape(Object o);
	protected abstract GeoShape createVisualItem(Object o);
	
	protected void init(String name, WorldWindowGLCanvas wwglCanvas){
//		this.wwglCanvas = wwglCanvas;

		setName(name);

		addedObjects  = new FastSet<Object>();
		removeObjects = new FastSet<Object>();
		addedShapes = new ArrayList<GeoShape>();
		visualItemMap = new HashMap<Object, GeoShape>();
		renderableToObjectMap = new HashMap<Renderable,Object>();

		this.setPickEnabled(true);
	}

	@Override
	/**
	 * Override dispose() to prevent losing renderables on frame resize/dock.
	 */
	public void dispose(){
		
	}
	
	public synchronized void applyUpdates() {
//	try{
//		lock.lock();
		for (GeoShape shape : addedShapes) {
			this.addRenderable(shape.getRenderable());
		}

		for (Object o : visualItemMap.keySet()){
			applyUpdatesToShape(o);
		}

		addedShapes.clear();

		// TODO sufficient to fire layer update?
		firePropertyChange(AVKey.LAYER, null, this);
//	}
//  finally{
//  	lock.unlock();
//  }
	}
	
	public synchronized void update(LayoutUpdater updater) {
		addAddedObjects();
		removeRemovedObjects();
	}
	
	public void addObject(Object o) {
		addedObjects.add(o);
	}
	
	public void removeObject(Object o) {
		// if the object to remove is addedObjects
		// we don't need to actually remove it because it
		// hasn't been added yet -- so just remove it from there
		// otherwise add it to the objects to remove.
		if (!addedObjects.remove(o)) removeObjects.add(o);
	}
	
	protected void addAddedObjects() {
		for (Object o : addedObjects) {
			GeoShape shape = createVisualItem(o);
			
			if (shape !=  null){
			  renderableToObjectMap.put(shape.getRenderable(), o);
			  addedShapes.add(shape);
			}
		}
		addedObjects.clear();
	}
	
	protected void removeRemovedObjects() {
		for (Object o : removeObjects) {
			GeoShape shape = visualItemMap.remove(o);
			if (shape != null) {
				this.removeRenderable(shape.getRenderable());
				renderableToObjectMap.remove(shape.getRenderable());
			}
		}
		removeObjects.clear();
	}
	
//	public Map<GeoShape,Object> getShapeToObjectMap(){
//		return this.shapeToObjectMap;
//	}
	
	public Object findObjectForRenderable(Renderable renderable) {
		return renderableToObjectMap.get(renderable);
	}
	
	public GeoShape getVisualItem(Object o) {
		return visualItemMap.get(o);
	}
	
	public void setGeography(Geography geography) {
		this.geography = geography;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
