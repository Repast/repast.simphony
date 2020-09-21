package repast.simphony.context;

import java.util.Collection;
import java.util.stream.Stream;

import org.apache.commons.collections15.Predicate;

import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.valueLayer.ValueLayer;

/**
 * Represents a group of agents that participate in a simulation. A <code>Context</code> ecapsulates
 * a population of agents. <p>
 * 
 * A <code>Context</code> may have one or more <code>Projection</code>s associated with it to create a 
 * relational structure on agents in the <code>Context</code>.<p>
 * 
 * <code>Context</code>s may also contain multiple sub-Contexts as well as agents. Any number of 
 * sub-Contexts may exist in a <code>Context</code>. A <code>Context</code> keeps a reference to its 
 * parent, or containing <code>Context</code>, if it exists.<p>
 * 
 * The <code>Context</code> interface contains methods that define Observer design pattern capability. This is 
 * the capability for a <code>Context</code> to allow listeners to register for future notifications
 * when certain events occur to the <code>Context</code>. When one of these  
 * 
 * @author Howe
 */
public interface Context<T> extends Collection<T>, RepastElement {

  static final String SYN_CONTEXT_PREFIX = "__Synthetic_Context__";

  Iterable<T> query(Predicate query);

	/**
	 * Gets an object in this context chosen at random from a uniform distribution.
	 *
	 * @return an object in this context chosen at random from a uniform distribution.
	 */
	T getRandomObject();

	/**
	 * Gets an iterable over a collection of objects chosen at random. The number of objects
	 * is determined by the specified count and the type of objects by the specified class.
	 * If the context contains less objects than the specified count, all
	 * the appropriate objects in the context will be returned. <p>
	 *
	 * If this is repeatedly called with a count equal to the number of objects
	 * in the context, the iteration order will be shuffled each time.
	 *
	 * @param clazz the class of the objects to return
	 * @param count the number of random objects to return
	 * @return an iterable over a collection of random objects
	 */
	Iterable<T> getRandomObjects(Class<? extends T> clazz, long count);
	
	/**
	 * Gets a sequential Stream over a collection of objects chosen at random. The number of objects
	 * is determined by the specified count and the type of objects by the specified class.
	 * If the context contains less objects than the specified count, all
	 * the appropriate objects in the context will be returned. <p>
	 *
	 * If this is repeatedly called with a count equal to the number of objects
	 * in the context, the iteration order will be shuffled each time.
	 *
	 * @param clazz the class of the objects to return
	 * @param count the number of random objects to return
	 * @return a sequential Stream over a collection of random objects
	 */
	Stream<T> getRandomObjectsAsStream(Class<? extends T> clazz, long count);
	

	/**
	 * Gets a IndexedIterable over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 *
	 * @param clazz the type of objects to return
	 * @return  a IndexedIterable over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 */
	IndexedIterable<T> getObjects(Class<?> clazz);
	
	
	/**
	 * Gets a sequential Stream over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 *
	 * @param clazz the type of objects to return
	 * @return  a Stream over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 */
	Stream<T> getObjectsAsStream(Class<?> clazz);

	/**
	 * Gets an id that indentifies the user-defined type of this context. A context type typically
	 * refers to the role the context plays in a particular model. For example, the type of the
	 * context may be "School" and the agents in that context would be "Pupils".
	 *
	 * @return an id that indentifies the user-defined type of this context.
	 */
	Object getTypeID();

	/**
	 * Sets an id that indentifies the user-defined type of this context. A context type typically
	 * refers to the role the context plays in a particular model. For example, the type of the
	 * context may be "School" and the agents in that context would be "Pupils".
	 *
	 * @param id the type id
	 */
	void setTypeID(Object id);

	Iterable<Context<? extends T>> getSubContexts();

	void addSubContext(Context<? extends T> context);

	void removeSubContext(Context<? extends T> context);

	Context<? extends T> getSubContext(Object id);

	Context findParent(Object o);

	Context findContext(Object id);

	Collection<ContextListener<T>> getContextListeners();

	void addContextListener(ContextListener<T> listener);

	void removeContextListener(ContextListener<T> listener);

	Iterable<T> getAgentLayer(Class<T> agentType);

//	@Deprecated
//	Network<T> getNetwork(Object name);
//
//	@Deprecated
//	Iterable<Network<T>> getNetworks();

	Iterable<Class> getAgentTypes();
	
	/**
	 * Method to check if subcontext(s) are present in the context
	 * @return true if subcontext(s) are present and false if not
	 */
	public boolean hasSubContext();
	
	/**
	 * Gets the named projection. This does not query subcontexts.
	 *
	 * @param projection the type of the projection
	 * @param name the name of the projection to get
	 * @return the named projection.
	 */
	<X extends Projection<?>> X getProjection(Class<X> projection,
	                                          String name);

	/**
	 * Gets all the projections in this Context of the specified type. This does
	 *  not query subcontexts.
	 *
	 * @param clazz the type of projections to get
	 *
	 * @return all the projections in this Context of the specified type.
	 */
	<X extends Projection<?>> Iterable<X> getProjections(Class<X> clazz);

	/**
	 * Adds the specified Projection to this Context.
	 *
	 * @param projection the projection to add
	 */
	void addProjection(Projection<? super T> projection);

  /**
   * Removes the named projection from this Context.
   *
   * @param projectionName the name projection to remove
   *
   * @return the removed projection.
   */
  Projection<? super T> removeProjection(String projectionName);

	/**
	 * Gets the named projection. This does not search subcontexts.
	 *
	 * @param name the name of the projection to get
	 * @return the named projection.
	 */
	Projection<?> getProjection(String name);

	/**
	 * Gets an iterable over all the projections contained by this Context.
	 * @return an iterable over all the projections contained by this Context.
	 */
	Collection<Projection<?>> getProjections();

	/**
	 * Adds the specified ValueLayer to this Context.
	 *
	 * @param valueLayer the ValueLayer to add
	 */
	void addValueLayer(ValueLayer valueLayer);

	/**
	 * Gets the named ValueLayer. This does not search subcontexts.
	 * @param name the name of the ValueLayer to get
	 *
	 * @return the named ValueLayer.
	 */
	ValueLayer getValueLayer(String name);

	/**
	 * Gets an iterable over the ValueLayer-s contained by this Context.
	 *
	 * @return an iterable over the ValueLayer-s contained by this Context.
	 */
	Collection<ValueLayer> getValueLayers();

	/**
	 * Removes the named ValueLayer from this Context.
	 *
	 * @param name the name of the ValueLayer to remove.
	 * @return the removed ValueLayer or null if the named ValueLayer was not found.
	 */
	ValueLayer removeValueLayer(String name);


}
