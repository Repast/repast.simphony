package repast.simphony.query;

import org.apache.commons.collections15.Predicate;
import repast.simphony.context.Context;
import repast.simphony.util.collections.FilteredIterator;
import simphony.util.messages.MessageCenter;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstact implementation of a query that queries based on an object's property. A property
 * is defined in the standard java bean way as a getter or setter, such that the presence of a getFoo
 * or setFoo method defines a "foo" property.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractPropertyQuery<T> implements Query<T> {

	protected static final MessageCenter msgCenter = MessageCenter.getMessageCenter(AbstractPropertyQuery.class);
	protected static final Object[] EMPTY = {};

	protected static Set<Class> primNums = new HashSet<Class>();
	static {
		primNums.add(int.class);
		primNums.add(double.class);
		primNums.add(float.class);
		primNums.add(long.class);
		primNums.add(char.class);
		primNums.add(byte.class);
	}

	private Context<T> context;
	protected Object propertyValue;
	private Predicate<T> predicate;
  private String propertyName;

  /**
	 * Creates an AbstractProperty query to query the specified contexts using
	 * the specified property name and value.
	 *
	 * @param context
	 * @param propertyName
	 * @param propertyValue
	 */
	public AbstractPropertyQuery(Context<T> context, String propertyName, Object propertyValue) {
		this.context = context;
		this.propertyValue = propertyValue;
    this.propertyName = propertyName;
  }

	/**
	 * Gets the Predicate that will be used in this Predicate's
	 * next call to query().
	 *
	 * @return the Predicate that will be used in this Predicate's
	 *         next call to query().
	 */
	public Predicate<T> getQueryPredicate() {
		return predicate;
	}

	/**
	 * Implemented by subclasses to return the appropriate Predicate. For example,
	 * a property equals query predicate will return true if an object has that
	 * property and its value is equal to the value specified in the constructor.
	 *
	 * @param context
	 * @param propertyName
	 *
	 * @return the Predicate appropriate to the implementing sub class.
	 */
	protected abstract Predicate<T> createPredicate(Context<T> context, String propertyName);

	/**
	 * Returns the result of the query.
	 *
	 * @return an iterable over the objects that are the result of the query.
	 */
	public Iterable<T> query() {
    // lazily create the predicate
    if (predicate == null) predicate = createPredicate(context, propertyName);
    return new FilteredIterator<T>(context.iterator(), predicate);
	}

	protected void propertyNotFound(String propertyName) {
		IllegalArgumentException ex = new IllegalArgumentException("Property '" +
						propertyName + "' not found.");
		AbstractPropertyQuery.msgCenter.warn("Possible error while creating property query", ex);
	}

	/**
	 * Returns an iterable over the objects that are the result of the query
	 * and are in the passed in iterable. This allows queries to be chained
	 * together where the result of one query is passed into another.
	 *
	 * @param iter
	 * @return an iterable over the objects that are the result of the query
	 *         and are in the passed in iterable.
	 */
	public Iterable<T> query(Iterable<T> iter) {
		return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(iter));
	}
}
