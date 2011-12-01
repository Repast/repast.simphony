package repast.simphony.query.space.projection;

import repast.simphony.space.graph.Network;

/**
 * Predicate that evalutes whether one object has a link to another in a projection. The
 * result is affected by the directionality of the link.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class LinkedTo extends SpatialPredicate {

	private Object obj1, obj2;

	/**
	 * Creates a LinkedTo predicate that will evaluate whether or not the first object has a link t
	 * to the second.
	 *
	 * @param obj1
	 * @param obj2
	 */
	public LinkedTo(Object obj1, Object obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	/**
	 *
	 * @param network the network to evaluate against.
	 * @return true if the first object is a predeccessor of the second in the specified Network.
	 */
	public boolean evaluate(Network network) {
		return network.isPredecessor(obj1, obj2);
	}
}
