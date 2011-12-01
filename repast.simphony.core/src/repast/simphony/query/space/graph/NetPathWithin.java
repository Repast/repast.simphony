package repast.simphony.query.space.graph;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.IteratorChain;
import repast.simphony.context.Context;
import repast.simphony.query.Query;
import repast.simphony.query.QueryUtils;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.ShortestPath;
import repast.simphony.util.collections.FilteredIterator;
import repast.simphony.util.collections.IterableAdaptor;

/**
 * A Query that returns all nodes within some given path length of a specified node.
 * "Within" includes the upper limit.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NetPathWithin<T> implements Query<T> {

	private Network<T> network;
	private T target;
	private Context<T> context;
	private double distance;

	private static class DistancePredicate implements Predicate {

		private ShortestPath shortest;
		private double dist;
		private Object self;

		public DistancePredicate(ShortestPath shortest, double dist, Object self) {
			this.shortest = shortest;
			this.dist = dist;
			this.self = self;
		}

		public boolean evaluate(Object t) {
			return !self.equals(t) && shortest.getPathLength(self,t) <= dist;
		}
	}



	/**
	 * Creates a NetPathWithin query that returns all nodes within
	 * the specified path length of the specified node in all
	 * the networks in the specified context.
	 *
	 * @param context
	 * @param obj
	 * @param distance
	 */
	public NetPathWithin(Context<T> context, T obj, double distance) {
		this.context = context;
		this.target = obj;
		this.distance = distance;
	}

	/**
	 * Creates a NetPathWithin query that returns all nodes within
	 * the specified path length of the specified node in the specified
	 * network.
	 *
	 * @param network
	 * @param obj
	 * @param distance
	 */
	public NetPathWithin(Network<T> network, T obj, double distance) {
		this.network = network;
		this.target = obj;
		this.distance = distance;
	}

	/**
	 * Gets an iterable over all the nodes within the path
	 * length of the target node as specified in the constructor.
	 *
	 * @return an iterable over all the nodes with the path
	 * length of the target node as specified in the constructor.
	 */
	public Iterable<T> query() {
		if (context != null) {
			IteratorChain<T> chain = new IteratorChain<T>();
			for (Network<T> net : context.getProjections(Network.class)) {
				ShortestPath path = new ShortestPath(net);
				chain.addIterator(new FilteredIterator<T>(net.getNodes().iterator(), new DistancePredicate(path,
								distance, target)));
			}
			return new IterableAdaptor<T>(chain);
		} else {
			ShortestPath path = new ShortestPath(network);
			return new FilteredIterator<T>(network.getNodes().iterator(), new DistancePredicate(path,
							distance, target));
		}
	}

	/**
	 * Gets an iterable over all the nodes within the path
	 * length of the target node as specified in the constructor AND
	 * where those nodes are members of the iterable parameter.
	 *
	 * @param set
	 * @return an iterable over all the nodes within the path
	 * length of the target node as specified in the constructor AND
	 * where those nodes are members of the iterable parameter.
	 */
	public Iterable<T> query(Iterable<T> set) {
		return new FilteredIterator<T>(query().iterator(), QueryUtils.createContains(set));
	}
}
