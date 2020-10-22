package repast.simphony.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections15.iterators.IteratorChain;
import org.apache.commons.collections15.iterators.SingletonIterator;

import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.CompositeIndexedIterable;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.util.collections.IterableAdaptor;

public class SmallDefaultContext<T> extends AbstractContext<T> {

  // to insure iteration order is repeatable.
  protected Map<Class, List<T>> objectMap = new LinkedHashMap<Class, List<T>>();
  private int size;
  protected IndexedIterable allObjs = null;

  public SmallDefaultContext() {
    super();
  }

  public SmallDefaultContext(Object id) {
    this(id, id);
  }

  public SmallDefaultContext(Object name, Object typeID) {
    setId(name);
    setTypeID(typeID);
  }

  @Override
  protected boolean containsInternal(Object o) {
    for (List<T> list : objectMap.values()) {
      if (list.contains(o)) return true;
    }
    return false;
  }

  @Override
  protected Iterator<T> iteratorInternal() {
    IteratorChain<T> chain = new IteratorChain<T>();
    for (List<T> list : objectMap.values()) {
      chain.addIterator(list.iterator());
    }
    return chain;
  }

  @Override
  protected int sizeInternal() {
    return size;
  }

  @Override
  // NOTE this makes no attempt to
  // avoid adding duplicate items. Such a check
  // must be done before calling this method in
  // order to preserve set semantics.
  protected boolean addInternal(T o) {
    allObjs = null;
    Class clazz = o.getClass();
    List<T> list = objectMap.get(clazz);
    if (list == null) {
      list = new ArrayList<T>();
      objectMap.put(clazz, list);
    }

    // doesn't make sense to check the retVal
    // here as List.add will always return true.
    list.add(o);
    size++;
    return true;
  }

  @Override
  protected boolean removeInternal(Object obj) {
    allObjs = null;
    Class<? extends Object> clazz = obj.getClass();
    List<T> list = objectMap.get(clazz);
    if (list == null) return false;
    boolean val = list.remove(obj);
    if (list.size() == 0) {
      objectMap.remove(clazz);
    }
    if (val) size--;
    return val;
  }

  /* (non-Javadoc)
   * @see java.util.AbstractCollection#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(Collection<?> c) {
    boolean ret = false;
    for (Object obj : c) {
      if (remove(obj)) ret = true;
    }
    return ret;
  }

  /* (non-Javadoc)
   * @see java.util.AbstractCollection#retainAll(java.util.Collection)
   */
  @Override
  public boolean retainAll(Collection<?> c) {
    Set<?> set = null;
    if (c instanceof Set) {
      set = (Set<?>)c;
    } else {
      set = new HashSet<Object>(c);
    }
    
    List<Object> toRemove = new ArrayList<Object>();
    for (Object obj : this) {
      if (!set.contains(obj)) toRemove.add(obj);
    }
    removeAll(toRemove);
    return toRemove.size() > 0;
  }

  /**
   * Gets a IndexedIterable over all the objects in this context (and thus in the sub contexts) that are
   * of the specified type.
   *
   * @param clazz the type of objects to return
   * @return a IndexedIterable over all the objects in this context (and thus in the sub contexts) that are
   *         of the specified type.
   */
  public IndexedIterable<T> getObjects(Class<?> clazz) {

    CompositeIndexedIterable<T> iter = new CompositeIndexedIterable<T>();
    for (Map.Entry<Class, List<T>> entry : objectMap.entrySet()) {
      if (clazz.isAssignableFrom(entry.getKey()))
        iter.addIndexedIterable(new ContextIndexedIterable<T>(this, entry.getValue()));
    }
    if (subContexts.size() == 0) return iter;

    for (Context context : subContexts.values()) {
      IndexedIterable child = context.getObjects(clazz);
      if (child.size() > 0) iter.addIndexedIterable(child);
    }
    return iter;
  }
  
  	/**
	 * Gets a sequential Stream over all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type.
	 *
	 * @param clazz the type of objects to return
	 * @return  a Stream over all the objects in this context (and thus in the sub contexts) that are
	 * of the specified type. The Stream is sequential.
	 */
	public Stream<T> getObjectsAsStream(Class<?> clazz) {
		IndexedIterable<T> iiter = getObjects(clazz);
		Spliterator<T> iter = Spliterators.spliterator(iiter.iterator(), iiter.size(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
		return StreamSupport.stream(iter, false);
	}
	
	/**
	 * Gets a sequential Stream over a collection of objects chosen at random. The number of objects
	 * is determined by the specified count and the type of objects by the specified class.
	 * If the context contains fewer objects than the specified count, all
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
		Iterable<T> riter = getRandomObjects(clazz, count);
		Spliterator<T> iter = Spliterators.spliteratorUnknownSize(riter.iterator(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
		return StreamSupport.stream(iter, false);
	}

  /**
   * Gets an iterable over a collection of objects chosen at random. The number of objects
   * is determined by the specified count and the type of objects by the specified class.
   * If the context contains fewer objects than the specified count, all
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
    IndexedIterable<T> iter = getObjects(clazz);
    if (iter.size() == 0) return Collections.emptyList();
    if (count == 1) {
      T obj = iter.get(RandomHelper.nextIntFromTo(0, iter.size() - 1));
      if (obj == null) {
        return Collections.emptyList();
      }
      return new IterableAdaptor<T>(new SingletonIterator<T>(obj, false));
    }
    return new ContextIterable<T>(iter, count, this);
  }

  /**
   * Gets an object in this context chosen at random from a uniform distribution.
   *
   * @return an object in this context chosen at random from a uniform distribution.
   */
  public T getRandomObject() {
    if (allObjs == null) allObjs = getObjects(Object.class);
    return (T) allObjs.get(RandomHelper.nextIntFromTo(0, allObjs.size() - 1));
  }

}
