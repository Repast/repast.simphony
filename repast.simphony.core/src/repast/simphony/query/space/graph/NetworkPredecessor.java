package repast.simphony.query.space.graph;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

import java.util.Iterator;

/**
 * Queries a network(s) for the predecessor nodes of a specified node.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NetworkPredecessor<T> extends AbstractNetworkQuery<T> {

	/**
	 * Creates a NetworkAdjacent query that will query the specified
	 * network for the predecessor nodes of the specified object.
	 *
	 * @param network
	 * @param obj
	 */
	public NetworkPredecessor(Network<T> network, T obj) {
		super(network, obj);
	}

	/**
	 * Creates a NetworkAdjacent query that will query any networks in the specified
	 * context for the predecessor nodes of the specified object.
	 *
	 * @param context
	 * @param obj
	 */
	public NetworkPredecessor(Context<T> context, T obj) {
		super(context, obj);
	}

	/**
	 * Returns an iterator over nodes in the network that are predecessor of the
	 * node specified in the constructor. A predecessor node is a node on the "from"
	 * side of a directed link.
	 *
	 * @param net
	 * @return an iterator over nodes in the network that are predecessors of the
	 * node specified in the constructor.
	 */
	protected Iterator<T> getNetNghIterable(Network<T> net) {
		return net.getPredecessors(target).iterator();
	}
}
