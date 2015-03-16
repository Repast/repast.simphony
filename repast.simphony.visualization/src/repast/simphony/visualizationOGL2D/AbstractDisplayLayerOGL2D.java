/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import repast.simphony.visualization.IDisplayLayer;
import repast.simphony.visualization.LayoutUpdater;
import saf.v3d.scene.VLabelLayer;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VSpatial;

/**
 * Abstract base class for OGL2D display layers.
 * 
 * @author Nick Collier
 * 
 * @param S the Style type
 */
public abstract class AbstractDisplayLayerOGL2D<S> implements IDisplayLayer<VSpatial> {
  
  public static final String MODEL_OBJECT_KEY = "DisplayLayerOGL2D.MODEL_OBJECT_KEY";
  public static final String LABEL_KEY = "DisplayLayerOGL2D.LABEL_KEY";

  protected S style;
  protected VLayer layer;
  protected Set<Object> toBeAdded = new HashSet<Object>();
  protected Set<Object> toBeRemoved = new HashSet<Object>();
  protected Map<Object, VSpatial> objMap = new HashMap<Object, VSpatial>();
  
  protected Map<Font, VLabelLayer> labelLayers = new HashMap<Font, VLabelLayer>();
  

  public AbstractDisplayLayerOGL2D(S style, VLayer layer) {
    this.style = style;
    this.layer = layer;
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

  public void addObject(Object obj) {
    // remove it from the removed objects because
    // there might be a remove and then add in the same
    // update -- for example when a object switches contexts
    // the display is part of their parent context
    toBeRemoved.remove(obj);
    toBeAdded.add(obj);
  }

  /**
   * No-op for ogl based code as the displaying the GLAutoDrawable will reflect
   * the updates. Nothing needs to be done here.
   */
  public void applyUpdates() {}

  /**
   * Gets the visual item that represents the specified object in the display.
   * 
   * @return the visual item that represents the specified object in the display.
   */
  public VSpatial getVisualItem(Object obj) {
    return objMap.get(obj);
  }

  /**
   * Removes this specified object from this layer.
   * 
   * @param obj
   *          the object to remove
   */
  public void removeObject(Object obj) {
    // if the object to remove is in addedObjects
    // we don't need to actually remove it because it
    // hasn't been added yet -- so just remove it from there
    // otherwise add it to the objects to remove.
    if (!toBeAdded.remove(obj)) toBeRemoved.add(obj);
  }

  /**
   * Updates the displayed nodes by applying styles etc. The display is not
   * updated to reflect these changes.
   */
  public abstract void update(LayoutUpdater updater); 
}
