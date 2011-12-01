package repast.simphony.visualization.decorator;

import repast.simphony.space.projection.Projection;
import repast.simphony.visualizationOGL2D.DisplayOGL2D;
import saf.v3d.scene.VComposite;

/**
 * Interface for classes that will decorate projection visualizations.
 * Decorating a projection refers to such things as adding grid lines for a grid
 * projection, bounding boxes, etc.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ProjectionDecorator2D<T extends Projection<?>> {

  /**
   * Initializes the decorator. Implementors should add the decorating shapes to
   * the parent node.
   * 
   * @param display
   * @param parent
   *          the parent to which the decoration should be added
   */
  void init(DisplayOGL2D display, VComposite parent);

  /**
   * Updates the decorator. The intention is that this would only do something
   * if the decoration has changed from that created in init.
   */
  void update();

  /**
   * Sets the that this decorator decorates.
   * 
   * @param projection
   */
  void setProjection(T projection);

  /**
   * Gets the projection that this decorator decorates.
   * 
   * @return the projection that this decorator decorates.
   */
  T getProjection();
}
