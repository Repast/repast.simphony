package repast.simphony.visualization;

import repast.simphony.space.projection.Projection;

/**
 * Interface for display layouts. A layout provides display coordinates for the
 * displayed objects.
 * 
 * @author Nick Collier
 */
public interface Layout<T, U extends Projection<?>> {

  /**
   * Updates this layout.
   */
  void update();

  /**
   * Sets the projection associated with this Layout.
   * 
   * @param projection
   *          the associated projection.
   */
  void setProjection(U projection);

  /**
   * Gets the location of the specified object as determined by this layout.
   * 
   * @param obj
   *          the object whose location we want.
   * 
   * @return the location of the object.
   */
  float[] getLocation(T obj);

  /**
   * Sets the layout properties for this layout.
   * 
   * @param props
   *          the layout properties
   */
  void setLayoutProperties(VisualizationProperties props);

  /**
   * Gets the layout properties for this layout.
   * 
   * @return the layout properties for this layout.
   */
  VisualizationProperties getLayoutProperties();

  /**
   * Gets the name of the layout
   * 
   * @return name of the layout type
   */
  String getName();
  
  /**
   * Gets a bounding box that describes the extent of this layout 
   * in location coordinates.
   * 
   * @return a bounding box that describes the extent of this layout 
   * in location coordinates, or a 0 size box if the extent is unknown.
   */
  Box getBoundingBox();
}
