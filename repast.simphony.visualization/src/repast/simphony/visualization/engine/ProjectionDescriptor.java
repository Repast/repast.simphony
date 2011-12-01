package repast.simphony.visualization.engine;

import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;


/**
 * Interface for classes that describe how a projection should be
 * visualized.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ProjectionDescriptor {

	/**
	 * Gets the name of the projection.
	 *
	 * @return the name of the projection.
	 */
	String getProjectionName();

	/**
	 * Sets the named property in the specified property group.
	 * For example, each kind of decorator would have its own properties
	 * and these would be grouped into a property group.
	 *
	 * @param groupID
	 * @param key
	 * @param value
	 */
	void setProperty(String groupID, String key, Object value);

	/**
	 * Gets the named property in the specified property group.
	 * For example, each kind of decorator would have its own properties
	 * and these would be grouped into a property group.
	 *
	 * @param groupID
	 * @param key
	 * @return the named property in the specified property group.
	 */
	Object getProperty(String groupID, String key);

	/**
	 * Gets an Iterable over the 3d projection decorators for the projection.
	 *
	 * @return an Iterable over the 3d projection decorators for the projection.
	 */
	Iterable<ProjectionDecorator3D> get3DDecorators();

	/**
	 * Gets an Iterable over the 2d projection decorators for the projection.
	 *
	 * @return an Iterable over the 2d projection decorators for the projection.
	 */
	Iterable<ProjectionDecorator2D> get2DDecorators();

	/**
	 * Gets the implied 3d layout of the projection, if any. For
	 * example, a grid projection implies a grid layout.
	 *
	 *
	 * @return the implied layout of the projection, if any.
	 */
	String getImpliedLayout3D();

	/**
	 * Gets the implied 2d layout of the projection, if any. For
	 * example, a grid projection implies a grid layout.
	 *
	 * @return the implied layout of the projection, if any.
	 */
	String getImpliedLayout2D();
}
