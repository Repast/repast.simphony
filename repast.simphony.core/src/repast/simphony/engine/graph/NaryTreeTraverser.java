/*CopyrightHere*/
package repast.simphony.engine.graph;

import repast.simphony.space.graph.Traverser;
import repast.simphony.util.collections.NaryTree;

import java.util.Iterator;

/**
 * A simple traverser for {@link repast.simphony.util.collections.NaryTree}s.
 * 
 * @author Jerry Vos
 */
public class NaryTreeTraverser<T> implements Traverser<T> {

	protected NaryTree<T> tree;

	protected boolean simpleDistance;

	public NaryTreeTraverser(){
		
	}
	/**
	 * Constructs this {@link Traverser}, traversing the given tree.
	 * 
	 * @param tree
	 *            the tree to traverse
	 * @param simpleDistance
	 *            when enabled this cancels the distance calculation and always makes it return 1
	 */
	public NaryTreeTraverser(NaryTree<T> tree, boolean simpleDistance) {
		this.tree = tree;
		this.simpleDistance = simpleDistance;
	}

	/**
	 * Constructs this {@link Traverser}, traversing the given tree. This sets simpleDistance to
	 * tree, so it is the same as <code>NaryTreeTravserer(tree, true)</code>.
	 * 
	 * @param tree
	 *            the tree to traverse
	 */
	public NaryTreeTraverser(NaryTree<T> tree) {
		this(tree, true);
		this.tree = tree;
	}

	/**
	 * Returns the currentNode's children.
	 * 
	 * @param previousNode
	 *            ignored
	 * @param currentNode
	 *            the node whose children to return
	 * @return the children
	 */
	public Iterator<T> getSuccessors(T previousNode, T currentNode) {
		return tree.getChildren(currentNode).iterator();
	}

	/**
	 * Returns 1 if the toNode is a child of the fromNode, otherwise Double.POSITIVE_INFINITY. If
	 * simpleDistance is turned on, this will always return 1.
	 * 
	 * @param fromNode
	 *            assumed to be the parent
	 * @param toNode
	 *            assumed to be the child
	 * @return 1 or Double.POSITIVE_INFINITY
	 */
	public double getDistance(T fromNode, T toNode) {
		if (simpleDistance || tree.getChildren(fromNode).contains(toNode)) {
			return 1;
		}
		return Double.POSITIVE_INFINITY;
	}

}
