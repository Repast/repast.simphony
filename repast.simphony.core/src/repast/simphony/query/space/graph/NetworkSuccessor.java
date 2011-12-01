package repast.simphony.query.space.graph;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

import java.util.Iterator;

/**
 * Queries a network(s) for the successor nodes of a specified node.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NetworkSuccessor<T> extends AbstractNetworkQuery<T> {

	/**
	 * Creates a NetworkAdjacent query that will query the specified
	 * network for the successor nodes of the specified object.
	 *
	 * @param network
	 * @param obj
	 */
	public NetworkSuccessor(Network<T> network, T obj) {
		super(network, obj);
	}

	/**
	 * Creates a NetworkAdjacent query that will query any networks in the specified
	 * context for the successor nodes of the specified object.
	 *
	 * @param context
	 * @param obj
	 */
	public NetworkSuccessor(Context<T> context, T obj) {
		super(context, obj);
	}

	/**
	 * Returns an iterator over nodes in the network that are successors of the
	 * node specified in the constructor. A successor node is a node on the "to"
	 * side of a directed link.
	 *
	 * @param net
	 * @return an iterator over nodes in the network that are successors of the
	 * node specified in the constructor.
	 */
	protected Iterator<T> getNetNghIterable(Network<T> net) {
		return net.getSuccessors(target).iterator();
	}
}
