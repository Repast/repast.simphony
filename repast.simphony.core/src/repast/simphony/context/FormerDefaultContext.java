package repast.simphony.context;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.commons.collections15.Predicate;

import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.valueLayer.ValueLayer;

public class FormerDefaultContext<T> implements Context<T> {

	public enum ContextHint {
		FASTER, SMALLER
	}

	protected transient ContextHint hint = ContextHint.FASTER;
	protected Context<T> delegate;

	public FormerDefaultContext() {
		init();
	}

	public FormerDefaultContext(Object name) {
		this(name, name);
	}

	public FormerDefaultContext(Object name, Object typeID) {
		init();
		setId(name);
		setTypeID(typeID);
	}

	public FormerDefaultContext(Object name, ContextHint hint) {
		this(name, name, hint);
	}

	public FormerDefaultContext(Object name, Object typeID, ContextHint hint) {
		this.hint = hint;
		init();
		setId(name);
		setTypeID(typeID);
	}

	/**
	 *
	 */
	private void init() {
		if (hint == ContextHint.FASTER) {
			delegate = new DefaultContext<T>();
		} else {
			delegate = new SmallDefaultContext<T>();
		}

	}

	public void addContextListener(ContextListener<T> listener) {
		delegate.addContextListener(listener);
	}

	public void addProjection(Projection<? super T> projection) {
		delegate.addProjection(projection);
	}

  /**
   * Removes the named projection from this Context.
   *
   * @param projectionName the name projection to remove
   * @return the removed projection.
   */
  public Projection<? super T> removeProjection(String projectionName) {
    return delegate.removeProjection(projectionName);
  }

  public void addSubContext(Context<? extends T> context) {
		delegate.addSubContext(context);
	}

	public Iterable<T> getAgentLayer(Class<T> agentType) {
		return delegate.getAgentLayer(agentType);
	}

	public Iterable<Class> getAgentTypes() {
		return delegate.getAgentTypes();
	}

	public Collection<ContextListener<T>> getContextListeners() {
		return delegate.getContextListeners();
	}

	public Projection<?> getProjection(String name) {
		return delegate.getProjection(name);
	}

	public <X extends Projection<?>>X getProjection(Class<X> projection, String name) {
		return delegate.getProjection(projection, name);
	}

	public Collection<Projection<?>> getProjections() {
		return delegate.getProjections();
	}

	public <X extends Projection<?>>Iterable<X> getProjections(Class<X> clazz) {
		return delegate.getProjections(clazz);
	}

	public Context<? extends T> getSubContext(Object id) {
		return delegate.getSubContext(id);
	}

	/**
	 * Retrieves the context of the specified object starting with the specified Context. This will
	 * traverse into the specified context's sub contexts until it finds the lowest level context
	 * the object is in.
   *
	 * @param o
	 *            an object to find in the contexts
	 * @return the context that contains the object
	 */
	@SuppressWarnings("unchecked")
	public Context findParent(Object o) {
		return delegate.findParent(o);
	}

	/**
	 * This searches the specified context and its descendants for the context with the specified
	 * id. This searches through the contexts in a breadth-first manner, and will return the first
	 * context whose getId() method returns the specified id. This includes the start context.
	 * 
	 *
	 * @param id
	 *            the id of the context to search for
	 * 
	 * @return the first context found with the given id, or null if no context is found
	 */
	@SuppressWarnings("unchecked")
	public Context findContext(final Object id) {
		if (!getId().equals(id)) {
			return delegate.findContext(id);
		} else {
			return this;
		}
	}
	
	public Iterable<Context<? extends T>> getSubContexts() {
		return delegate.getSubContexts();
	}

	public Iterable<T> query(Predicate query) {
		return delegate.query(query);
	}

	public void removeContextListener(ContextListener<T> listener) {
		delegate.removeContextListener(listener);
	}

	public void removeSubContext(Context<? extends T> context) {
		delegate.removeSubContext(context);
	}

	/**
	 * Gets a IndexedIterable over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 *
	 * @param clazz the type of objects to return
	 * @return a IndexedIterable over the all the objects in this context (and thus in the sub contexts) that are
	 *         of the specified type.
	 */
	public IndexedIterable<T> getObjects(Class<?> clazz) {
		return delegate.getObjects(clazz);
	}

	/**
	 * Gets an iterable over a collection of objects chosen at random. The number of objects
	 * is determined by the specified count and the type of objects by the specified class.
	 * If the context contains less objects than the specified count, all
	 * the appropriate objects in the context will be returned. <b>
	 * <p/>
	 * If this is repeatedly called with a count equal to the number of objects
	 * in the context, the iteration order will be shuffled each time.
	 *
	 * @param clazz the class of the objects to return
	 * @param count the number of random objects to return
	 * @return an iterable over a collection of random objects
	 */
	public Iterable<T> getRandomObjects(Class<? extends T> clazz, long count) {
		return delegate.getRandomObjects(clazz, count);
	}

	/**
	 * Gets an object in this context chosen at random from a uniform distribution.
	 *
	 * @return an object in this context chosen at random from a uniform distribution.
	 */
	public T getRandomObject() {
		return delegate.getRandomObject();
	}

	public Object getId() {
		return delegate.getId();
	}

	public void setId(Object id) {
		delegate.setId(id);
	}


	/**
	 * Gets an id that indentifies the user-defined type of this context. A context type typically
	 * refers to the role the context plays in a particular model. For example, the type of the
	 * context may be "School" and the agents in that context would be "Pupils".
	 *
	 * @return an id that indentifies the user-defined type of this context.
	 */
	public Object getTypeID() {
		return delegate.getTypeID();
	}

	/**
	 * Sets an id that indentifies the user-defined type of this context. A context type typically
	 * refers to the role the context plays in a particular model. For example, the type of the
	 * context may be "School" and the agents in that context would be "Pupils".
	 *
	 * @param id the type id
	 */
	public void setTypeID(Object id) {
		delegate.setTypeID(id);
	}

	public void clear() {
		delegate.clear();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator<T> iterator() {
		return delegate.iterator();
	}

	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	public int size() {
		return delegate.size();
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <U> U[] toArray(U[] a) {
		return delegate.toArray(a);
	}

	public boolean add(T o) {
		return delegate.add(o);
	}

	public boolean addAll(Collection<? extends T> collection) {
		return delegate.addAll(collection);
	}

	/**
	 * Adds the specified ValueLayer to this Context.
	 *
	 * @param valueLayer the ValueLayer to add
	 */
	public void addValueLayer(ValueLayer valueLayer) {
		delegate.addValueLayer(valueLayer);
	}

	/**
	 * Gets the named ValueLayer. This does not search subcontexts.
	 *
	 * @param name the name of the ValueLayer to get
	 * @return the named ValueLayer.
	 */
	public ValueLayer getValueLayer(String name) {
		return delegate.getValueLayer(name);
	}

	/**
	 * Gets an iterable over the ValueLayer-s contained by this Context.
	 *
	 * @return an iterable over the ValueLayer-s contained by this Context.
	 */
	public Collection<ValueLayer> getValueLayers() {
		return delegate.getValueLayers();
	}

	/**
	 * Removes the named ValueLayer from this Context.
	 *
	 * @param name the name of the ValueLayer to remove.
	 * @return the removed ValueLayer or null if the named ValueLayer was not found.
	 */
	public ValueLayer removeValueLayer(String name) {
		return delegate.removeValueLayer(name);
	}
	
	public boolean hasSubContext(){
		if(this.delegate.size()>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets a sequential Stream over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 *
	 * @param clazz the type of objects to return
	 * @return  a Stream over the all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 */
	public Stream<T> getObjectsAsStream(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}
	
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
	public Stream<T> getRandomObjectsAsStream(Class<? extends T> clazz, long count) {
		throw new UnsupportedOperationException();
	}
}
