package repast.simphony.visualization.visualization3D;

import javolution.util.FastSet;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.visualization3D.style.Style3D;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Nick Collier
 */
public abstract class AbstractDisplayLayer3D implements IDisplayLayer3D {

  protected Style3D style;

  protected BranchGroup parentGroup;

  protected Set<Object> objsToAdd = new FastSet<Object>();

  protected Set<Object> objsToRemove = new FastSet<Object>();

  protected VisualItem3DAdderRemover adder = new VisualItem3DAdderRemover();

  protected final Map<Object, VisualItem3D> visualItemMap = new HashMap<Object, VisualItem3D>();

  public AbstractDisplayLayer3D(Style3D style, Group topGroup) {
    this.style = style;
    parentGroup = new BranchGroup();
    parentGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
    parentGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
    topGroup.addChild(parentGroup);
  }

  public void addObject(Object obj) {
    objsToAdd.add(obj);
    // remove  it from the removed objects because
    // there might be a remove and then add in the same
    // update -- for example when an object switches contexts
    // and the display is displaying their parent context
    objsToRemove.remove(obj);
  }

  public void removeObject(Object obj) {
    if (!objsToAdd.remove(obj)) objsToRemove.add(obj);
  }

  // abstract because non-edge layers will do the layout, but edge layers will
  // not
  // but both will add
  protected abstract void createItemsForAddedObjects(Layout layout,
                                                     boolean doLayout);

  protected void removeItemsForRemovedObjects() {
    for (Object obj : objsToRemove) {
      VisualItem3D item = visualItemMap.remove(obj);
      adder.addItemForRemoval(item);
    }

    objsToRemove.clear();
  }

  public VisualItem3D getVisualItem(Object obj) {
    return visualItemMap.get(obj);
  }

  /**
   * Given a Shape3D, return the object that the shape is the visualization of.
   *
   * @param shape the shape whose associated object we want to find
   * @return the object that the specified shape represents
   */
  public Object findObjsForItem(Shape3D shape) {
    synchronized (visualItemMap) {
      for (VisualItem3D<?> item : visualItemMap.values()) {
        for (Iterator<Shape3D> iter = item.shapes(); iter.hasNext();) {
          Shape3D itemShape = iter.next();
          if (itemShape.equals(shape)) return item.getVisualizedObject();
        }
      }
    }
    return null;
  }


  protected abstract void doUpdate(Layout layout, boolean layoutPerformed);

  protected abstract Label createLabel();

  public void update(LayoutUpdater updater) {
    synchronized (visualItemMap) {
      createItemsForAddedObjects(updater.getLayout(), updater.getDoSetLocationForAdded());
      removeItemsForRemovedObjects();
      doUpdate(updater.getLayout(), updater.getUpdateItemsLocation());
    }
  }

  public void setStyle(Style3D style) {
    this.style = style;
  }

  /**
   * Apply the updates to the scene graph. This should be
   * called by the 3D behavior in display3D.
   */
  public void applyUpdates() {
    synchronized (visualItemMap) {
      adder.execute(parentGroup);
      for (VisualItem3D item : visualItemMap.values()) {
        item.applyTransform();
      }
    }
  }
}
