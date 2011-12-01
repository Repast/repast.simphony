package repast.simphony.util.collections;

import javolution.util.FastMap;

import java.util.*;

/**
 * A rooted tree where each node can have n number of children.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NaryTree<T> implements Tree<T> {

	// node comparator that compares using a comparator on the
	// node's contents
	protected class NodeComparator<T> implements Comparator<Node<T>> {

		private Comparator<T> comp;

		public NodeComparator(Comparator<T> comp) {
			this.comp = comp;
		}

		public int compare(Node<T> o1, Node<T> o2) {
			return comp.compare(o1.obj, o2.obj);
		}
	}

	// iterator used to iterate through direct children.
	protected  class ChildIterator<T> implements Iterator<T>, Iterable<T>{

		private Iterator<Node<T>> EMPTY_ITER = new ArrayList<Node<T>>().iterator();

		private Iterator<Node<T>> children;

		public ChildIterator(Node<T> parentNode) {
			if (parentNode.children == null) children = EMPTY_ITER;
			else children = parentNode.children.iterator();
		}

		/**
		 * Returns <tt>true</tt> if the iteration has more elements. (In other
		 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
		 * rather than throwing an exception.)
		 *
		 * @return <tt>true</tt> if the iterator has more elements.
		 */
		public boolean hasNext() {
			return children.hasNext();
		}

		/**
		 * Returns the next element in the iteration.  Calling this method
		 * repeatedly until the {@link #hasNext()} method returns false will
		 * return each element in the underlying collection exactly once.
		 *
		 * @return the next element in the iteration.
		 * @throws java.util.NoSuchElementException
		 *          iteration has no more elements.
		 */
		public T next() {
			Node<T> node = children.next();
			return node.obj;
		}

		/**
		 * Removes from the underlying collection the last element returned by the
		 * iterator (optional operation).  This method can be called only once per
		 * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
		 * the underlying collection is modified while the iteration is in
		 * progress in any way other than by calling this method.
		 *
		 * @throws UnsupportedOperationException if the <tt>remove</tt>
		 *                                       operation is not supported by this Iterator.
		 * @throws IllegalStateException         if the <tt>next</tt> method has not
		 *                                       yet been called, or the <tt>remove</tt> method has already
		 *                                       been called after the last call to the <tt>next</tt>
		 *                                       method.
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Returns an iterator over a set of elements of type T.
		 *
		 * @return an Iterator.
		 */
		public Iterator<T> iterator() {
			return this;
		}
	}

	 class Node<T> {
		T obj;
		Node<T> parent;
		List<Node<T>> children;

		public Node(T obj, Node<T> parent) {
			this.obj = obj;
			this.parent = parent;
		}

		void addChild(Node<T> node) {
			if (children == null) {
				children = new ArrayList<Node<T>>();
			}
			children.add(node);
		}

		void removeChild(Node<T> child) {
			if (children != null) {
				children.remove(child);
				if (children.size() == 0) children = null;
			}
		}
	}

	private Node<T> root;
	private FastMap<T, Node<T>> objNodeMap = new FastMap<T, Node<T>>();

	/**
	 * Creates a NaryTree with the specified object as the root
	 *
	 * @param rootObj the root object
	 */
	public NaryTree(T rootObj) {
		this.root = new Node<T>(rootObj, null);
		objNodeMap.put(rootObj, this.root);
	}

	/**
	 * Gets the root of the tree.
	 *
	 * @return the root of the tree.
	 */
	public T getRoot() {
		return root.obj;
	}

	/**
	 * Gets the direct children of the specified node. This will not return
	 * grand children etc.
	 *
	 * @return the direct children of the specified node.
	 * @throws IllegalArgumentException if the object is not currently in the tree.
	 */
	public Collection<T> getChildren(T obj) {
		Node<T> parentNode = objNodeMap.get(obj);
		if (parentNode == null) {
			throw new IllegalArgumentException("Parent node '" + obj + "' is not in the tree");
		}
		
		ChildIterator<T> children = new ChildIterator<T>(parentNode);
		
		ArrayList<T> childList = new ArrayList<T>();
		
		for (T child : children) {
			childList.add(child);
		}
		
		// TODO: optimize this implementation.
		
		return childList;
	}

	/**
	 * Adds the specified child to the tree as a child of the specified parent.
	 *
	 * @param parent the parent node
	 * @param child  the child node
	 * @throws IllegalArgumentException if the parent is not currently in the tree.
	 */
	public void addNode(T parent, T child) {
		Node<T> parentNode = objNodeMap.get(parent);
		if (parentNode == null) {
			throw new IllegalArgumentException("Parent node '" + parent + "' is not in the tree");
		}
		Node<T> node = new Node<T>(child, parentNode);
		parentNode.addChild(node);
		objNodeMap.put(child, node);
	}

	/**
	 * Retrieves the siblings of the specified object in the tree.
	 * 
	 * @param obj the object whose siblings to get
	 * @return null if the passed in object is null, otherwise the object's siblings
	 */
	public Collection<T> getSiblings(T obj) {
		if (obj == null) {
			return null;
		}
		
		Node<T> node = objNodeMap.get(obj);
		return getChildren(node.parent.obj);
	}
	
	/**
	 * Removes the specified object from the tree.  You cannot replace the root node, only
	 * {@link #replaceNode(T, T)} it.
	 *
	 * @param obj the object to remove
	 * @return true if the object was successfully removed, otherwise false.
	 */
	public boolean removeNode(T obj) {
		Node<T> node = objNodeMap.remove(obj);
		if (node != null) {
			// for now just do a search for the node starting at the root
			Node<T> parent = node.parent;
			if (parent != null) {
				parent.removeChild(node);
				// remove all the children of child from the objNodeMap.
				removeChildrenFromMap(node);
				return true;
			}
		}
		return false;
	}

	private void removeChildrenFromMap(Node<T> node) {
		if (node.children != null) {
			for (Node<T> child : node.children) {
				objNodeMap.remove(child.obj);
				removeChildrenFromMap(child);
			}
		}
	}

	/**
	 * Replaces the old object in the tree with the new one.
	 *
	 * @param oldObj the old object to replace
	 * @param newObj the new object
	 * @throws IllegalArgumentException if the old object is not currently in the tree.
	 */
	public void replaceNode(T oldObj, T newObj) {
		Node<T> node = objNodeMap.remove(oldObj);
		if (node == null) {
			throw new IllegalArgumentException("Node to replace '" + oldObj + "' is not in the tree");
		}
		node.obj = newObj;
		objNodeMap.put(newObj, node);
	}

	/**
	 * Sorts the children of each node w/r to each other according the specified
	 * comparator
	 *
	 * @param comparator the comparator used to sort the children
	 */
	public void sortChildren(Comparator<T> comparator) {
		sortChildren(root, new NodeComparator<T>(comparator));
	}

	private void sortChildren(Node<T> parent, NodeComparator<T> comparator) {
		if (parent.children != null) {
			for (Node<T> child : parent.children) {
				sortChildren(child, comparator);
			}
			Collections.sort(parent.children, comparator);
		}
	}

	/**
	 * Gets the number of nodes currently in the tree.
	 *
	 * @return the number of nodes currently in the tree.
	 */
	public int size() {
		return objNodeMap.size();
	}

	/**
	 * Traverse the tree in preOrder - depth first, processing parents before children -
	 * applying the visitor to the nodes.
	 *
	 * @param visitor the visitor to apply to the nodes
	 */
	public void preOrderTraversal(TreeVisitor<T> visitor) {
		// TODO: convert this to an iterative solution
		preOrderTraverals(root, visitor);
	}

	private void preOrderTraverals(Node<T> node, TreeVisitor<T> visitor) {
		visitor.visit(node.obj);
		if (node.children != null) {
			for (Node<T> child : node.children) {
				preOrderTraverals(child, visitor);
			}
		}
	}

	protected void preOrderTraveralsOfNodes(Node<T> node, TreeVisitor<Node<T>> visitor) {
		visitor.visit(node);
		if (node.children != null) {
			for (Node<T> child : node.children) {
				preOrderTraveralsOfNodes(child, visitor);
			}
		}
	}

	protected boolean containsChecker(Node<T> node, T parent, T child) {
		if (node.obj.equals(parent)) {
			if (node.children.contains(child)) {
				return true;
			}
			return false;
		}
		for (Node<T> childNode : node.children) {
			if (containsChecker(childNode, parent, child)) {
				return true;
			}
		}
		return false;
	}
	
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
	public boolean contains(T parent, T child) {
		return containsChecker(root, parent, child);
	}
}
