package repast.simphony.query.space.projection;

import repast.simphony.space.graph.Network;

/**
 * Predicate that evalutes whether one object has a link from another in a projection. The
 * result is affected by the directionality of the link.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class LinkedFrom extends SpatialPredicate {

	private Object obj1, obj2;

	/**
	 * Creates a LinkedTo predicate that will evaluate whether or not the first object has a link from
	 * the second.
	 *
	 * @param obj1
	 * @param obj2
	 */
	public LinkedFrom(Object obj1, Object obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	/**
	 *
	 * @param network the network to evaluate against.
	 * @return true if the first object is a successor of the second in the specified Network.
	 */
	public boolean evaluate(Network network) {
		return network.isSuccessor(obj1, obj2);
	}
}
