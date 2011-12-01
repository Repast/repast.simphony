package repast.simphony.visualization;

/**
 * Interface for classes that contain objects that can be visualized. This
 * interface essentially decouples the visualization mechanism from
 * Contexts, so that the vizualization code can be used with any class
 * that implements this interface.
 *
 * @author Nick Collier
 */
public interface VisualizedObjectContainer<T> extends Iterable<T> {

  /**
   * Adds the specified object to this container.
   *
   * @param obj the object to add. This is the object itself
   * not its visualized representation.
   */
  void add(T obj);

  /**
   * Removes the specified object from this container.
   *
   * @param obj the object to add. This is the object itself
   * not its visualized representation.
   */
  void remove(T obj);

  /**
   * Whether or not the this container contains the specified object.
   *
   * @param obj the object to check
   * @return true if this contains the object otherwise false.
   */
  boolean contains(T obj);

}
