package repast.simphony.visualization.decorator;

import javax.media.j3d.Group;

import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.visualization3D.Display3D;

/**
 * Interface for classes that will decorate projection visualizations. Decorating
 * a projection refers to such things as adding grid lines for a grid projection,
 * bounding boxes, etc.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ProjectionDecorator3D<T extends Projection> {

	/**
	 * Initializes the decorator. Implementors should add
	 * the decorating shapes to the parent branch group.
	 *
	 * @param display
	 * @param parent the parent to which the decoration should be added
	 */
	void init(Display3D display, Group parent);

	/**
	 * Updates the decorator. The intention is that this would only do something if
	 * the decoration has changed from that created in init.
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
