/*CopyrightHere*/
package repast.simphony.engine.graph;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.Traverser;

import java.util.Iterator;

/**
 * A traverser for Repast Topologies.
 * 
 * @author Jerry Vos
 */
public class NetworkTraverser<T> implements Traverser<T> {
	private Network<T> network;

	/**
	 * Constructs this traverser with the specified network topology to retrieve
	 * information from.
	 * 
	 * @param topology
	 *            the topology that will be the source of information on passed
	 *            in nodes
	 */
	public NetworkTraverser(Network<T> topology) {
		super();
		this.network = topology;
	}

	/**
	 * Retrieves what the current node's nearest neighbors excluding the
	 * previous node.
	 *
	 * @return the current node's nearest neighbors not including the previous
	 *         node
	 */
	@SuppressWarnings({"serial"})
	public Iterator<T> getSuccessors(final T previousNode, final T currentNode) {
		return new FilterIterator<T>(network.getSuccessors(
				currentNode).iterator(), new Predicate<T>() {
			public boolean evaluate(T toCheck) {
				if (toCheck != previousNode) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	/**
	 * Retrieves the distance between the nodes according to the topology.
	 *
	 * @return the distance between the nodes
	 */
	public double getDistance(final T previousNode, final T currentNode) {
		RepastEdge edge = network.getEdge(previousNode, currentNode);
		if (edge == null) {
			return Double.POSITIVE_INFINITY;
		}
		return edge.getWeight();
	}
}
