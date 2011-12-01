package repast.simphony.space.graph;

import repast.simphony.util.HashCodeUtil;

/**
 * Event fired when an edge is added or removed from a network.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NetworkEvent<T> {

	public enum EventType {
		EDGE_ADDED, EDGE_REMOVED
	};

	private RepastEdge<T> edge;
	private Network<T> network;
	private EventType type;

	/**
	 * Creates a NetworkEvent from the specified type, with the specified network as the source,
	 * and the specified edge.
	 * @param type
	 * @param network
	 * @param edge
	 */
	public NetworkEvent(EventType type, Network<T> network, RepastEdge<T> edge) {
		this.type = type;
		this.network = network;
		this.edge = edge;
	}


	/**
	 * Gets the edge that this event applies to.
	 * @return the edge that this event applies to.
	 */
	public RepastEdge<T> getEdge() {
		return edge;
	}

	/**
	 * Gets the network that was the source of this event.
	 * @return the network that was the source of this event.
	 */
	public Network<T> getNetwork() {
		return network;
	}


	/**
	 * Gets the type of this event.
	 *
	 * @return the type of this event.
	 */
	public EventType getType() {
		return type;
	}

	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, network);
		result = HashCodeUtil.hash(result, edge);
		result = HashCodeUtil.hash(result, type);
		return result;
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof NetworkEvent)) {
			return false;
		}
		NetworkEvent<?> other = (NetworkEvent<?>) o;
		return network.equals(other.getNetwork()) && type.equals(other.getType()) &&
						edge.equals(other.getEdge());
	}
}
