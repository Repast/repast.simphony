package repast.simphony.visualization;

/**
 * Interface for display layers.
 * 
 * @author Nick Collier
 */
public interface IDisplayLayer<T> {

  /**
   * Updates the layout, styles etc. of the displayed objects.
   * 
   * @param updater used to update the layout and position information
   */
  void update(LayoutUpdater updater);

  /**
   * Applies the latest set of updates to the displayed objects. Depending
   * on the semantics of the display, this may or may not be necessary.
   */
  void applyUpdates();

  /**
   * Adds the specified object to the layer.
   * 
   * @param obj the object to add
   */
  void addObject(Object obj);

  /**
   * Removes the specified objecdt from the layer.
   * 
   * @param obj the object to remove
   */
  void removeObject(Object obj);

  /**
   * Gets the visual representation of this object.
   * 
   * @param obj the object whose visual representation we want to get
   * 
   * @return the visual representation of this object.
   */
  T getVisualItem(Object obj);
}
