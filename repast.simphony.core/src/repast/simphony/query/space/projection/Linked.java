package repast.simphony.query.space.projection;

import repast.simphony.space.graph.Network;

/**
 * Predicate that evalutes whether two objects are "linked" in a projection. This is unaffected
 * by the directionality of the link.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class Linked extends SpatialPredicate {

	private Object obj1, obj2;

	/**
	 * Creates a Linked predicate that will evaluate whether or not the first object is linked to the second.
	 *
	 * @param obj1
	 * @param obj2
	 */
	public Linked(Object obj1, Object obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	/**
	 *
	 * @param network the network to evaluate against.
	 * @return true if the first object is adjacent the second in the specified Network.
	 */
	public boolean evaluate(Network network) {
		return network.isAdjacent(obj1, obj2);
	}
}
