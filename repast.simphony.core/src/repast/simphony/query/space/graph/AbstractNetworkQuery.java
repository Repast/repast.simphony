package repast.simphony.query.space.graph;

import org.apache.commons.collections15.iterators.IteratorChain;
import repast.simphony.context.Context;
import repast.simphony.query.Query;
import repast.simphony.query.QueryUtils;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.FilteredIterator;
import repast.simphony.util.collections.IterableAdaptor;

import java.util.Iterator;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractNetworkQuery<T> implements Query<T> {

	private Network<T> network;
	protected T target;
	private Context<T> context;

	/**
	 * Creates a network query that will query any networks in the specified
	 * context using the specified object.
	 *
	 * @param context
	 * @param obj
	 */
	protected AbstractNetworkQuery(Context<T> context, T obj) {
		this.context = context;
		this.target = obj;
	}

	/**
	 * Creates a network query that will query the specified
	 * network using the specified object.
	 *
	 * @param network
	 * @param obj
	 */
	protected AbstractNetworkQuery(Network<T> network, T obj) {
		this.network = network;
		this.target = obj;
	}

	/**
	 * Gets an iterable over a collection of nodes. The actual members of
	 * this set will be determined by sub-classes. For example,
	 * the NetworkAdjacent query will return an iterable over
	 * nodes that are adjacent to the node specified in the constructor.
	 *
	 * @return an iterable over a collection of nodes.
	 */
	public Iterable<T> query() {
		if (context != null) {
			IteratorChain<T> chain = new IteratorChain<T>();
			for (Network<T> net : context.getProjections(Network.class)) {
				chain.addIterator(getNetNghIterable(net));
			}
			return new IterableAdaptor<T>(chain);
		} else {
			return new IterableAdaptor<T>(getNetNghIterable(network));
		}
	}

	/**
	 * Implementor should return the iterator appropriate to their query. For
	 * example, an network adjacent query would return an iterator over
	 * the nodes adjacent to the target node specified in the constructor.
	 *
	 * @param net
	 * @return the iterator appropriate to the implemented query.
	 */
	protected abstract Iterator<T> getNetNghIterable(Network<T> net);

	/**
	 * Gets an iterable over the collection of nodes returned by this query.
	 * where the members of that collection are also part of the
	 * specified iterable parameter. The actual members of
	 * the iterable returned by the query will be determined by sub-classes. For example,
	 * the NetworkAdjacent query will return an iterable over
	 * nodes that are adjacent to the node specified in the constructor.
	 *
	 * @return an iterable over a collection of nodes.
	 */
	public Iterable<T> query(Iterable<T> iter) {
		return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(iter));
	}
}
