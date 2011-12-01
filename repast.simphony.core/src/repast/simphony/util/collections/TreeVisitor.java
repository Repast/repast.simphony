package repast.simphony.util.collections;

/**
 * Interface for a functor type object that is executed against nodes in a Tree.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface TreeVisitor<T> {

	/**
	 * Visit the specified node, presumably performing some operation on that node.
	 *
	 * @param node
	 */
	void visit(T node);
}
