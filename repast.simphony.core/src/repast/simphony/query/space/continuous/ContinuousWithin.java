package repast.simphony.query.space.continuous;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.IteratorChain;
import repast.simphony.context.Context;
import repast.simphony.query.WithinDistance;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.collections.FilteredIterator;
import repast.simphony.util.collections.IterableAdaptor;

/**
 * A within type query over a continous space. This query will return all the objects
 * that are within a specified distance of a specified object in either
 * all the continuous spaces in a context or in a specific continuous space.
 * "Within" includes the distance, so within 10 means all the objects from a
 * distance of 0 to 10 including 10.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ContinuousWithin<T> extends WithinDistance<T> {

	private ContinuousSpace<T> space;

	private static class WithinPredicate implements Predicate {

		private ContinuousSpace space;
		private NdPoint origin;
		private double distSq;
		private Object target;

		public WithinPredicate(double distSq, NdPoint origin, ContinuousSpace space, Object target) {
			this.distSq = distSq;
			this.origin = origin;
			this.space = space;
			this.target = target;
		}

		public boolean evaluate(Object o) {
			return !o.equals(target) && space.getDistanceSq(space.getLocation(o),origin) <= distSq;
		}
	}

	/**
	 * Creates a ContinuousWithin query that will find all the objects
	 * within the specified distance of the specified object in
	 * all the continuous spaces in the specified context.
	 *
	 * @param context
	 * @param obj
	 * @param distance
	 */
	public ContinuousWithin(Context<T> context, T obj, double distance) {
		super(context, distance, obj);
	}

	/**
	 * Creates a ContinuousWithin query that will find all the objects
	 * within the specified distance of the specified object in the
	 * specified space.
	 *
	 * @param space
	 * @param obj
	 * @param distance
	 */
	public ContinuousWithin(ContinuousSpace<T> space, T obj, double distance) {
		super(null, distance, obj);
		this.space = space;
	}

	/**
	 * Creates an iterable over all the objects within
	 * the specified distance in the space.
	 *
	 * @return an iterable over all the objects within
	 * the specified distance in the space.
	 */
	protected Iterable<T> createIterable() {
		NdPoint origin = space.getLocation(obj);
		if (origin == null) return EMPTY;
		return new FilteredIterator<T>(space.getObjects().iterator(),
						new WithinPredicate(distanceSq, origin, space, obj));
	}

	/**
	 * Creates an iterable over all the objects within
	 * the specified distance in all the continuoous spaces
	 * in the context.
	 *
	 * @param context
	 * @return an iterable over all the objects within
	 * the specified distance in all the continuoous spaces
	 * in the context.
	 */
	protected Iterable<T> createIterable(Context<T> context) {
		IteratorChain<T> chain = new IteratorChain<T>();
		for (ContinuousSpace<T> space : context.getProjections(ContinuousSpace.class)) {
			NdPoint origin = space.getLocation(obj);
			if (origin != null) {
				chain.addIterator(new FilteredIterator<T>(space.getObjects().iterator(),
								new WithinPredicate(distanceSq, origin, space, obj)));
			}
		}
		return new IterableAdaptor<T>(chain);
	}
}
