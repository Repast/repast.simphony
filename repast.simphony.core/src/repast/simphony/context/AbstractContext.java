package repast.simphony.context;

import javolution.util.FastMap;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.PredicateUtils;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.apache.commons.collections15.iterators.IteratorChain;
import repast.simphony.context.ContextEvent.EventType;
import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.DelegatedIterator;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.util.collections.IterableAdaptor;
import repast.simphony.valueLayer.ValueLayer;
import simphony.util.messages.MessageCenter;

import java.util.*;

/**
 * Abstract base class implementation of the <code>Context</code> interface.
 */
public abstract class AbstractContext<T> extends AbstractCollection<T>
		implements Context<T>, ContextListener {

  private Object id;
	private Object typeID;

	private static int idNo = 0;

	private List<ContextListener<T>> listeners;

	protected Map<Object, Context<? extends T>> subContexts;

	private Map<String, Projection<?>> projectionMap;
	private Map<String, ValueLayer> valueLayerMap;

	private Set<Class> agentClasses;

	public AbstractContext() {
		this.listeners = new ArrayList<ContextListener<T>>();
		this.subContexts = new LinkedHashMap<Object, Context<? extends T>>();
		this.projectionMap = new FastMap<String, Projection<?>>();
		this.valueLayerMap = new FastMap<String, ValueLayer>();
		this.agentClasses = new HashSet<Class>();
		
		this.id = SYN_CONTEXT_PREFIX + idNo;
		this.typeID = SYN_CONTEXT_PREFIX + "Type__" + idNo;
    idNo++;
  }

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	/**
	 * Gets an id that indentifies the user-defined type of this context. A context type typically
	 * refers to the role the context plays in a particular model. For example, the type of the
	 * context may be "School" and the agents in that context would be "Pupils".
	 *
	 * @return an id that indentifies the user-defined type of this context.
	 */
	public Object getTypeID() {
		return typeID;
	}

	/**
	 * Sets an id that indentifies the user-defined type of this context. A context type typically
	 * refers to the role the context plays in a particular model. For example, the type of the
	 * context may be "School" and the agents in that context would be "Pupils".
	 *
	 * @param id the type id
	 */
	public void setTypeID(Object id) {
		this.typeID = id;
	}

	public void addContextListener(ContextListener<T> listener) {
		
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public Collection<ContextListener<T>> getContextListeners() {
		return listeners;
	}

	public Iterable<T> query(Predicate query) {
		return new IterableAdaptor<T>(new FilterIterator<T>(iterator(), query));
	}

	public void removeContextListener(ContextListener<T> listener) {
		listeners.remove(listener);
	}

	@Override
	public final boolean add(T o) {
		if (addInternal(o)) {
			agentClasses.add(o.getClass());
			fireAddContextEvent(o);
			return true;
		}
		return false;
	}

	/**
	 * @param o
	 */
	protected void fireAddContextEvent(T o) {
		fireContextEvent(new ContextEvent<T>(EventType.AGENT_ADDED,
				this, o));
	}

	protected abstract boolean addInternal(T o);

	protected abstract boolean containsInternal(Object o);

	public boolean contains(Object o) {
		if (containsInternal(o)) {
			return true;
		}
		for (Context<? extends T> sub : getSubContexts()) {
			if (sub.contains(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * By default the iterator returns in the contents in this context in the
	 * order entered then it returns the contents of the subcontexts in the
	 * order the subcontexts were added.
	 */
	@Override
	public Iterator<T> iterator() {
		IteratorChain<T> iter = new IteratorChain<T>();
		iter.addIterator(iteratorInternal());
		for (Context<? extends T> sub : getSubContexts()) {
			iter.addIterator(sub.iterator());
		}
		return iter;
	}

	protected abstract Iterator<T> iteratorInternal();

	/**
	 * Listener method for context events. This is primiarly used to receive ContextEvents
	 * from a subcontext and pass them on to this context's own listeners.
	 *
	 * @param ev
	 */
	public void eventOccured(ContextEvent ev) {
		for (ContextListener<T> listener : listeners) {
			listener.eventOccured(ev);
		}
	}

	public void addSubContext(Context<? extends T> context) {
		subContexts.put(context.getId(), context);
		for (Object obj : context) {
			fireAddContextEvent((T)obj);
		}
		context.addContextListener(this);
		fireSubContextAdded(context);
	}
	
	public boolean hasSubContext(){
		if(this.subContexts.values().size()>0)
			return true;
		else
			return false;
	}

	public Iterable<Context<? extends T>> getSubContexts() {
		return subContexts.values();
	}

	public void removeSubContext(Context<? extends T> context) {
		subContexts.remove(context.getId());
		for (Object obj : context) {
			fireRemoveEvent((T)obj);
		}
		context.removeContextListener(this);
		fireSubContextRemoved(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final boolean remove(Object o) {
		boolean result = false;
		result = handleRemove(o);
		if (removeInternal(o)) {
			result = true;
		}

		if (result) {
			fireRemoveEvent(o);
		}
		return result;
	}

  @Override
  public void clear() {
    Set<T> set = new HashSet<T>();
    for (Iterator<T> iter = iterator(); iter.hasNext(); ) {
      set.add(iter.next());
    }
    for (T obj : set) {
      remove(obj);
    }
  }

  protected boolean handleRemove(Object o) {
		boolean result = false;
		for (Context<? extends T> child : subContexts.values()) {
			if (child.remove(o)) {
				result = true;
			}
		}

		return result;
	}

	protected void fireRemoveEvent(Object o) {
		fireContextEvent(new ContextEvent<T>(EventType.AGENT_REMOVED,
				this, (T) o));
	}

	protected void fireSubContextAdded(Context<? extends T> context) {
		fireContextEvent(new ContextEvent<T>(EventType.SUBCONTEXT_ADDED, this, context));
	}

	protected void fireSubContextRemoved(Context<? extends T> context) {
		fireContextEvent(new ContextEvent<T>(EventType.SUBCONTEXT_REMOVED, this, context));
	}

	protected abstract boolean removeInternal(Object o);

	public int size() {
		int size = sizeInternal();
		for (Context<? extends T> context : getSubContexts()) {
			size = size + context.size();
		}
		return size;
	}

	protected abstract int sizeInternal();

	public Context<? extends T> getSubContext(Object id) {
		return subContexts.get(id);
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
		Context contextFound = null;
		List<Context<?>> list = new ArrayList<Context<?>>();
		list.add(this);
		for (Iterator<Context<?>> iter = list.iterator(); iter.hasNext();) {
			Context context = iter.next();
			if (context.contains(o)) {
				contextFound = context;
				if (context.getSubContexts() != null) {
					iter = context.getSubContexts().iterator();
				} else {
					break;
				}
			}
		}
		// TODO: if null, error handling here
		return contextFound;
	}

	/**
	 * This searches the specified context and its descendants for the context with the specified
	 * id. This searches through the contexts in a breadth-first manner, and will return the first
	 * context whose getId() method returns the specified id. This includes the start context.
	 * 
	 * @param id
	 *            the id of the context to search for
	 * 
	 * @return the first context found with the given id, or null if no context is found
	 */
	@SuppressWarnings("unchecked")
	public Context findContext(final Object id) {
		if (getId().equals(id)) {
			return this;
		} else if (getSubContext(id) != null) {
			return getSubContext(id);
		} else {
			for (Map.Entry<Object, Context<? extends T>> subContext : subContexts.entrySet()) {
				Context resSub = subContext.getValue().findContext(id);
				if (resSub != null) {
					return resSub;
				}
			}
		}
		return null;
	}
	
	public Iterable<Class> getAgentTypes() {
		HashSet<Class> set = new HashSet<Class>();
		set.addAll(agentClasses);
		for (Context<? extends T> sub : getSubContexts()) {
			for (Class c : sub.getAgentTypes()) {
				set.add(c);
			}
		}
		return set;
	}

	public Iterable<T> getAgentLayer(Class<T> agentType) {
		return query(PredicateUtils.instanceofPredicate(agentType));
	}

	public <X extends Projection<?>> X getProjection(Class<X> projection,
	                                                 String name) {
		return (X) projectionMap.get(name);
	}

	public <X extends Projection<?>> Iterable<X> getProjections(Class<X> clazz) {
		return new IterableAdaptor<X>(new FilterIterator(
						projectionMap.values().iterator(),PredicateUtils.instanceofPredicate(clazz)));
	}

	public Projection<?> getProjection(String name) {
		return projectionMap.get(name);
	}

	public void addProjection(Projection<? super T> projection) {
	  if (projectionMap.containsKey(projection.getName())) {
	    MessageCenter.getMessageCenter(getClass()).error("Projection '" + projection.getName() + "' has already been added\n" +
	        "Note that projection factories automatically add the projection to the context.",
	        new IllegalArgumentException("Duplicate projections added."));
	    return;
	  }
		if (projection instanceof ContextListener) {
			addContextListener((ContextListener) projection);
		}
		fireContextEvent(new ContextEvent<T>(EventType.PROJECTION_ADDED, this, projection));
		projectionMap.put(projection.getName(), projection);
	}

  /**
   * Removes the named projection from this Context.
   *
   * @param projectionName the name projection to remove
   * @return the removed projection.
   */
  public Projection<? super T> removeProjection(String projectionName) {
    Projection proj = projectionMap.remove(projectionName);
    if (proj == null) return null;
    fireContextEvent(new ContextEvent<T>(EventType.PROJECTION_REMOVED, this, proj));
    
    if (proj instanceof ContextListener) {
			removeContextListener((ContextListener) proj);
		}
    
    return proj;
  }

  private void fireContextEvent(ContextEvent<T> evt) {
		// OPTIMIZED: code this out for speed because this gets called a lot (potentially)
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			listeners.get(i).eventOccured(evt);
		}
	}

	/**
	 * Gets an iterable over all the projections contained by this Context.
	 *
	 * @return an iterable over all the projections contained by this Context.
	 */
	public Collection<Projection<?>> getProjections() {
		return projectionMap.values();
	}

	/**
	 * Adds the specified ValueLayer to this Context.
	 *
	 * @param valueLayer the ValueLayer to add
	 */
	public void addValueLayer(ValueLayer valueLayer) {
		valueLayerMap.put(valueLayer.getName(), valueLayer);
	}

	/**
	 * Removes the named ValueLayer from this Context.
	 *
	 * @param name the name of the ValueLayer to remove.
	 * @return the removed ValueLayer or null if the named ValueLayer was not found.
	 */
	public ValueLayer removeValueLayer(String name) {
		return valueLayerMap.remove(name);
	}

	/**
	 * Gets an iterable over the ValueLayer-s contained by this Context.
	 *
	 * @return an iterable over the ValueLayer-s contained by this Context.
	 */
	public Collection<ValueLayer> getValueLayers() {
		return valueLayerMap.values();
	}

	/**
	 * Gets the named ValueLayer. This does not search subcontexts.
	 *
	 * @param name the name of the ValueLayer to get
	 * @return the named ValueLayer.
	 */
	public ValueLayer getValueLayer(String name) {
		return valueLayerMap.get(name);
	}
}

// this isn't public because the constructor seems unsafe in that the list
// and the context need to contain the same objects. Creating it from
// within a context should help ensure that it is done correctly.
/**
 * An indexed iterable that decorates a List of objects contained in a context
 * and provides support for removing objects from the context via an iterator.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
class ContextIndexedIterable<T> implements IndexedIterable<T> {

	private class CIIterator<T> extends DelegatedIterator<T> {

		public CIIterator(Iterator<T> delegate) {
			super(delegate);
		}

		@Override
		public void remove() {
			super.remove();
			context.remove(lastObj);

		}
	}

	private List<T> list;
	private Context<T> context;


	/**
	 * Creates a ContextIndexedIterable for the specified context and that wraps
	 * the specified list. It is assumed that the List contains objects from the context.
	 * The iterator, get and size operations will work on the list while the remove via the
	 * iterator will apply to the context.
	 *
	 * @param context
	 * @param list
	 */
	public ContextIndexedIterable(Context<T> context, List<T> list) {
		this.context = context;
		this.list = list;
	}

	/**
	 * Gets the number of elements in this IndexedIterable.
	 *
	 * @return the number of elements in this IndexedIterable.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Gets the element at the specified position in this IndexedIterable.
	 *
	 * @param index the index of the element to return
	 * @return the element at the specified position in this IndexedIterable.
	 * @throws IndexOutOfBoundsException if the given index is out of range
	 *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>).
	 */
	public T get(int index) {
		return list.get(index);
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		return new CIIterator<T>(list.iterator());
	}
}
