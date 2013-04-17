package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickSupport;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.media.opengl.GL2;

import javolution.util.FastSet;
import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.LayoutUpdater;

/**
 * 
 * @author Eric Tatara
 * 
 */
public abstract class AbstractDisplayLayerGIS3D extends RenderableLayer {

  protected Map<Object, Renderable> visualItemMap;
  protected Map<Renderable, Object> shapeToObjectMap;

  protected WorldWindow canvas;
  protected Model model;
  protected Geography geography;
  protected Set<Object> addedObjects;
  protected Set<Object> removeObjects;
  protected List<Renderable> addedNodes;

  protected final PickSupport pickSupport = new PickSupport();

  ReentrantLock lock = new ReentrantLock();

  protected abstract void applyUpdatesToNode(RenderableShape node);

  protected abstract RenderableShape createVisualItem(Object o);

  protected void init(String name, WorldWindow wwglCanvas) {
    this.canvas = wwglCanvas;

    setName(name);

    addedObjects = new FastSet<Object>();
    removeObjects = new FastSet<Object>();
    addedNodes = new ArrayList<Renderable>();
    visualItemMap = new HashMap<Object, Renderable>();
    shapeToObjectMap = new HashMap<Renderable, Object>();

    this.setPickEnabled(true);
  }

  @Override
  protected synchronized void doPick(DrawContext dc, java.awt.Point pickPoint) {
    this.pickSupport.clearPickList();
    this.pickSupport.beginPicking(dc);

    for (Renderable renderable : this.getRenderables()) {

      // save the current color
      float[] inColor = new float[4];
      dc.getGL().getGL2().glGetFloatv(GL2.GL_CURRENT_COLOR, inColor, 0);

      // Generate a random color
      Color color = dc.getUniquePickColor();

      // set the renderable color
      dc.getGL().getGL2()
          .glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());

      // render it
      ((Pickable) renderable).pickRender(dc);

      // reset the current color
      dc.getGL().getGL2().glColor4fv(inColor, 0);

      this.pickSupport.addPickableObject(color.getRGB(), renderable);
    }

    this.pickSupport.resolvePick(dc, pickPoint, this);
    this.pickSupport.endPicking(dc);
  }

  @Override
  protected synchronized void doRender(DrawContext dc) {
    // try {
    // lock.lock();

    // TODO hack
    // RenderUtils.begin(dc);

    for (Renderable renderable : this.getRenderables()) {
      renderable.render(dc);
    }

    // TODO hack
    // RenderUtils.end(dc);

    // } finally {
    // lock.unlock();
    // }
  }

  public synchronized void applyUpdates() {
    // try{
    // lock.lock();
    for (Renderable node : addedNodes) {
      this.addRenderable(node);
    }

    for (Renderable node : this.getRenderables()) {
      applyUpdatesToNode((RenderableShape) node);
    }

    addedNodes.clear();

    firePropertyChange(AVKey.LAYER, null, this);
    // }
    // finally{
    // lock.unlock();
    // }
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
    if (!addedObjects.remove(o))
      removeObjects.add(o);
  }

  protected void addAddedObjects() {
    for (Object object : addedObjects) {
      Renderable node = createVisualItem(object);

      if (node != null) {
        shapeToObjectMap.put(node, object);
        addedNodes.add(node);
      }
    }
    addedObjects.clear();
  }

  protected void removeRemovedObjects() {
    for (Object object : removeObjects) {
      Renderable node = visualItemMap.remove(object);
      if (node != null) {
        this.removeRenderable(node);
        shapeToObjectMap.remove(node);
      }
    }
    removeObjects.clear();
  }

  public Map<Renderable, Object> getShapeToObjectMap() {
    return this.shapeToObjectMap;
  }

  public Object findObjectForShape(Renderable shape) {
    return shapeToObjectMap.get(shape);
  }

  public Renderable getVisualItem(Object o) {
    return visualItemMap.get(o);
  }

  public void setGeography(Geography geography) {
    this.geography = geography;
  }

  public void setModel(Model model) {
    this.model = model;
  }
}
