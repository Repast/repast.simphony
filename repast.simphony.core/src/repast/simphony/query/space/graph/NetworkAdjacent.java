package repast.simphony.query.space.graph;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

import java.util.Iterator;

/**
 * Queries a network(s) for the adjacent nodes to a specified node.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NetworkAdjacent<T> extends AbstractNetworkQuery<T> {

	/**
	 * Creates a NetworkAdjacent query that will query the specified
	 * network for the nodes adjacent to the specified object.
	 *
	 * @param network
	 * @param obj
	 */
	public NetworkAdjacent(Network<T> network, T obj) {
		super(network, obj);
	}

	/**
	 * Creates a NetworkAdjacent query that will query any networks in the specified
	 * context for the nodes adjacent to the specified object.
	 *
	 * @param context
	 * @param obj
	 */
	public NetworkAdjacent(Context<T> context, T obj) {
		super(context, obj);
	}

	/**
	 * Returns an iterator over nodes in the network that are adjacent to the
	 * node specified in the constructor.
	 *
	 * @param net
	 * @return an iterator over nodes in the network that are adjacent to the
	 * node specified in the constructor.
	 */
	protected Iterator<T> getNetNghIterable(Network<T> net) {
		return net.getAdjacent(target).iterator();
	}
}
