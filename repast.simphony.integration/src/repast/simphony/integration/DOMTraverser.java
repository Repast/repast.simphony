package repast.simphony.integration;

import java.util.Iterator;

import org.jdom.Element;
import org.jdom.filter.ElementFilter;

import repast.simphony.space.graph.Traverser;

/**
 * A traverser that will traverse a jdom DOM.
 * 
 * @author Jerry Vos
 * @version $Revision$ $Date$
 */
public class DOMTraverser implements Traverser<Element> {
	/**
	 * Returns the current node's children.
	 * 
	 * @see Element#getDescendants(org.jdom.filter.Filter)
	 * @see ElementFilter
	 * 
	 * @param previousNode
	 *            ignored
	 * @param currentNode
	 *            the node whose children are retrieved
	 * @return what would be returned by currentNode.getChildren()
	 */
	@SuppressWarnings("unchecked")
	public Iterator<Element> getSuccessors(Element previousNode, Element currentNode) {
		return currentNode.getChildren().iterator();
	}

	/**
	 * Returns either 1 if the elements are connected, otherwise Double.POSITIVE_INFINITY.
	 * 
	 * @param fromNode
	 *            one node
	 * @param toNode
	 *            another node
	 * @return 1 or POSITIVE_INFINITY
	 */
	public double getDistance(Element fromNode, Element toNode) {
		if (toNode.getParentElement() == fromNode || fromNode.getParentElement() == toNode)
			return 1;
		else
			return Double.POSITIVE_INFINITY;
	}

}
