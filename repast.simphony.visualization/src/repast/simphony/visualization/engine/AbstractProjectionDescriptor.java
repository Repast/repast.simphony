package repast.simphony.visualization.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.decorator.ProjectionDecoratorFactory;

/**
 * Abstract base class for implementors of ProjectionDescriptor
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractProjectionDescriptor implements ProjectionDescriptor {

	private ProjectionData proj;
	protected Map<String, Map<String, Object>> props = new HashMap<String, Map<String, Object>>();

	protected AbstractProjectionDescriptor(ProjectionData proj) {
		this.proj = proj;
        if (proj == null) throw new IllegalArgumentException("Projection cannot be null.");
	}

	/**
	 * Gets the name of the projection.
	 *
	 * @return the name of the projection.
	 */
	public String getProjectionName() {
		return proj.getId();
	}

	/**
	 * Gets the named property in the specified property group.
	 * For example, each kind of decorator would have its own properties
	 * and these would be grouped into a property group.
	 *
	 * @param groupID
	 * @param key
	 * @return the named property in the specified property group or null
	 * if no such property is found.
	 */
	public Object getProperty(String groupID, String key) {
		Map<String, Object> map = props.get(groupID);
		if (map != null) {
			return map.get(key);
		}

		return null;
	}

	/**
	 * Sets the named property in the specified property group.
	 * For example, each kind of decorator would have its own properties
	 * and these would be grouped into a property group.
	 *
	 * @param groupID
	 * @param key
	 * @param value
	 */
	public void setProperty(String groupID, String key, Object value) {
		Map<String, Object> map = props.get(groupID);
		if (map == null) {
			map = new HashMap<String, Object>();
			props.put(groupID, map);
		}
		map.put(key, value);
	}

	/**
	 * Gets an Iterable over the 2d projection decorators for the projection.
	 *
	 * @return an Iterable over the 2d projection decorators for the projection.
	 */
	public Iterable<ProjectionDecorator2D> get2DDecorators() {
		List<ProjectionDecorator2D> list = new ArrayList<ProjectionDecorator2D>();
		ProjectionDecoratorFactory fac = new ProjectionDecoratorFactory();
		for (String key : props.keySet()) {
			ProjectionDecorator2D deco = fac.create2DDecorator(key, props.get(key));
			if (deco != null) list.add(deco);
		}
		return list;

	}

	/**
	 * Gets an Iterable over the 3d projection decorators for the projection.
	 *
	 * @return an Iterable over the 3d projection decorators for the projection.
	 */
	public Iterable<ProjectionDecorator3D> get3DDecorators() {
		List<ProjectionDecorator3D> list = new ArrayList<ProjectionDecorator3D>();
		ProjectionDecoratorFactory fac = new ProjectionDecoratorFactory();
		for (String key : props.keySet()) {
			ProjectionDecorator3D deco = fac.create3DDecorator(key, props.get(key));
			if (deco != null) list.add(deco);
		}
		return list;
	}
}
