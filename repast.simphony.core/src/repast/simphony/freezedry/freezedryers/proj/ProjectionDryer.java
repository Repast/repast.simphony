/*CopyrightHere*/
package repast.simphony.freezedry.freezedryers.proj;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.ProjectionRegistryData;
import repast.simphony.space.projection.Projection;

/**
 * Implementations of this class will handle storing settings for some projections. Implementations
 * should override the {@link #handles(Class)}, {@link #addProperties(Context, T, Map)},
 * {@link #instantiate(Context, Map)}, and {@link #loadProperties(Context, T, Map)} methods.<p/>
 * 
 * By default this adds (through it's static {}) a
 * {@link repast.simphony.freezedry.freezedryers.proj.NetworkProjectionDryer}, a
 * {@link repast.simphony.freezedry.freezedryers.proj.ContinuousProjectionDryer}, and a
 * {@link repast.simphony.freezedry.freezedryers.proj.GridProjectionDryer}.
 * 
 * @see #handles(Class)
 * @see #addProperties(Context, T, Map)
 * @see #instantiate(Context, Map)
 * @see #loadProperties(Context, T, Map)
 * 
 * @author Jerry Vos
 */
public abstract class ProjectionDryer<T extends Projection> {
	public static final String NAME_KEY = "name";

	public static final String PROJ_LISTENING_KEY = "projectionListening";

	public static final String CONTEXT_LISTENING_KEY = "contextListening";

	private static LinkedList<ProjectionDryer> projectionDryers;

	/**
	 * Called when attempting to save the given projection. Implementations should store the
	 * settings they require to build the projection later during this method, and also call their
	 * super's method so that it can store its properties.
	 * 
	 * @param context
	 *            the context this projection is a member of
	 * @param t
	 *            the projection to store properties for
	 * @param properties
	 *            a place to store the properties for a projection
	 */
	protected abstract void addProperties(Context<?> context, T t, Map<String, Object> properties);

	/**
	 * Should createa a Projection for the given context. Implementing classes do not have to add
	 * the created projection to the context, that will be done after calling this method.
	 * 
	 * @param context
	 *            the context the projection will be a member of
	 * @param properties
	 *            the properties specified when saving the projection
	 * @return a created projection
	 */
	protected abstract T instantiate(Context<?> context, Map<String, Object> properties);

	/**
	 * This should return true if the implementation handles projections of the given type.
	 * 
	 * @param type
	 *            the type of the projection
	 * @return if the implementation handles the given type
	 */
	public abstract boolean handles(Class<?> type);

	/**
	 * This handles loading of properties into the instantiated projection. Implementations should
	 * override this method and load their properties here. If the properties are loaded in their
	 * {@link #instantiate(Context, Map)} method, then if a subclass overrides the instantiate
	 * method it will have to duplicate the property loading (or that loading will not be done).<p/>
	 * Implementations should call their super's {@link #loadProperties(Context, T, Map)} method so
	 * that all properties can be loaded.<p/>
	 * 
	 * This implementation loads the projection as a context listener (if it originally one) and the
	 * context as a projection listener if it was.  This then adds the projection to the context.
	 * 
	 * @param context
	 *            the context the projection is a member of
	 * @param proj
	 *            the projection that was instantiated
	 * @param properties
	 *            the properties that should be used to loaded into the projection
	 */
	protected void loadProperties(Context<?> context, T proj, Map<String, Object> properties) {
		// need to do this here so that any projection listening for projection added events
		// receieves that it is added (before it loads its properties)
		context.addProjection(proj);
	}

	/**
	 * Retrieves the serializable properties for a given projection that is a member of the given
	 * context. These properties should be reloaded and passed to the
	 * {@link #buildAndAddProjection(Context, Map)} method when deserializing the projection.
	 * 
	 * @see #getDryer(Class) for retrieving the dryer of the projection type
	 * 
	 * @param context
	 *            the context the projection is a member of
	 * @param t
	 *            the projection to serialize
	 * @return a Map of properties used to recreate the given projection
	 */
	public Map<String, Object> getProperties(Context<?> context, T t) {
		HashMap<String, Object> map = new HashMap<String, Object>(3);

		map.put(NAME_KEY, t.getName());
		map.put(PROJ_LISTENING_KEY, context.getContextListeners().contains(t));
		map.put(CONTEXT_LISTENING_KEY, t.getProjectionListeners().contains(context));

		addProperties(context, t, map);

		return map;
	}

	/**
	 * Creates a projection based on the given properties. It will also add the created projection
	 * to the given context.<p/>
	 * 
	 * @param context
	 *            the context to build a projection for
	 * @param properties
	 *            the properties for building the projection
	 * @return the created projection
	 */
	public T buildAndAddProjection(Context<?> context, Map<String, Object> properties) {
		T t = instantiate(context, properties);

		loadProperties(context, t, properties);

		return t;
	}

	/**
	 * Adds a dryer to be used by the {@link repast.simphony.freezedry.freezedryers.ContextFreezeDryer}.
	 * This will be added to the front of the dryer list, so it will override any equivalent dryer
	 * added before it that dries the same class.
	 * 
	 * @param dryer
	 *            a projection dryer
	 */
	public static void addProjectionDryer(ProjectionDryer dryer) {
		if (projectionDryers == null) {
			projectionDryers = new LinkedList<ProjectionDryer>();
		}
		projectionDryers.addFirst(dryer);
	}

	/**
	 * Retrieves the last added dryer that handles the given type. If none is found this returns
	 * null.
	 * 
	 * @param type
	 *            the projection type to dry
	 * @return a dryer for the given type
	 */
	public static <T extends Projection> ProjectionDryer<T> getDryer(Class<T> type) {
		if (projectionDryers == null) {
			return null;
		}

		for (ProjectionDryer<?> dryer : projectionDryers) {
			if (dryer.handles(type)) {
				return (ProjectionDryer<T>) dryer;
			}
		}
		
		// Last, look for any projection dryers in the projection registry.
		for (ProjectionRegistryData registryData : ProjectionRegistry.getRegistryData()){
			ProjectionDryer<?> dryer = registryData.getProjectionDryer();
			if (dryer.handles(type)) {
				return (ProjectionDryer<T>) dryer;
			}
		}
		
		return null;
	}

	static {
		
		// TODO Projections: update to use the projection registry
		addProjectionDryer(new NetworkProjectionDryer());
		addProjectionDryer(new ContinuousProjectionDryer());
		addProjectionDryer(new GridProjectionDryer2());
	}
}