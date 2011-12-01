package repast.simphony.util.collections;

import java.util.Collection;
import java.util.Comparator;

/**
 * Interface for class that want to implement Tree collection behavior.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface Tree<T> {
	/**
	 * Gets the root of the tree.
	 *
	 * @return the root of the tree.
	 */
	T getRoot();

	/**
	 * Gets the direct children of the specified node. This will not return
	 * grand children etc.
	 *
	 * @return the direct children of the specified node.
	 * @throws IllegalArgumentException if the object is not currently in the tree.
	 */
	Collection<T> getChildren(T obj);

	/**
	 * Adds the specified child to the tree as a child of the specified parent.
	 *
	 * @param parent the parent node
	 * @param child  the child node
	 * @throws IllegalArgumentException if the parent is not currently in the tree.
	 */
	void addNode(T parent, T child);

	/**
	 * Removes the specified object from the tree.
	 * @param obj the object to remove
	 * @return true if the object was successfully removed, otherwise false.
	 */
	boolean removeNode(T obj);

	/**
	 * Replaces the old object in the tree with the new one.
	 *
	 * @param oldObj the old object to replace
	 * @param newObj the new object
	 * @throws IllegalArgumentException if the old object is not currently in the tree.
	 */
	void replaceNode(T oldObj, T newObj);

	/**
	 * Checks if the tree contains the specified node with the specified parent. This will stop
	 * when it finds the first node corresponding to the parent, meaning, if multiple nodes 
	 * correspond to the parent, only the first one will be checked.
	 * 
	 * @param parent
	 *            the parent node
	 * @param node
	 *            the child of the parent
	 * @return true if the tree contains the specified child node pair
	 */
	boolean contains(T parent, T node);
	
	/**
	 * Gets the number of nodes currently in the tree.
	 *
	 * @return the number of nodes currently in the tree.
	 */
	int size();

	/**
	 * Sorts the children of each node w/r to each other according the specified
	 * comparator
	 *
	 * @param comparator the comparator used to sort the children
	 */
	void sortChildren(Comparator<T> comparator);

	/**
	 * Traverse the tree in preOrder - depth first, processing parents before children -
	 * applying the visitor to the nodes.
	 *
	 * @param visitor the visitor to apply to the nodes
	 */
	void preOrderTraversal(TreeVisitor<T> visitor);
}
