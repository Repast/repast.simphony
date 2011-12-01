/*CopyrightHere*/
package repast.simphony.util.collections;

import org.apache.commons.collections15.Predicate;
import repast.simphony.engine.graph.Executor;
import repast.simphony.space.graph.Traverser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utilities for working with collections of many types. This includes search, map(collection,
 * function) functionality and so forth.
 * 
 * @author Jerry Vos
 */
public class CollectionUtils {
	/**
	 * This is a simple predicate that will always return false (making us "search" the entire 
	 * graph) and on each object being checked execute a given function.<p/>
	 * 
	 * This is what performs the execution of actions on each object in the graph.
	 */
	private static class MapPredicate<T> implements Predicate<T> {
		private static final long serialVersionUID = -8855984562901493039L;

		private Executor<T> executor;

		public MapPredicate(Executor<T> executor) {
			this.executor = executor;
		}

		public boolean evaluate(T toCheck) {
			executor.execute(toCheck);
			return false;
		}
	}

	/**
	 * Performs a breadth-first traversal applying the given executor to the objects returned by the
	 * traverser. It is up to the traverser to handle how the objects are returned and to prevent
	 * repeated execution on the same nodes (if so desired). For example, it is up to the traverser
	 * to not return the same node in a graph multiple times, this method performs no checks for
	 * this type of situation. Note: This passes null to the {@link Traverser#getSuccessors(E, E)}
	 * method for the value of the previous node.<p/>
	 * 
	 * This is implemented as a call to {@link #breadthFirstSearch(Executor, Traverser, T)} with the
	 * calling the Executor and always returning {@link RuleResult#INVALID_CONTINUE}.
	 * 
	 * @param visitor
	 *            the executor which receives the objects from the traverser
	 * @param traverser
	 *            the object handles gathering and returning the objects that will be passed to the
	 *            Executor
	 * @param root
	 *            the object to begin the traversal with
	 */
	public static <T> void breadthFirstMap(Executor<T> visitor, Traverser<T> traverser, T root) {
		breadthFirstSearch(new MapPredicate<T>(visitor), traverser, root);
	}

	/**
	 * Performs a breadth-first traversal, applying the given rule to the objects returned by the
	 * traverser. It is up to the traverser to handle how the objects are returned and to prevent
	 * repeated execution on the same nodes (if so desired). For example, it is up to the traverser
	 * to not return the same node in a graph multiple times, this method performs no checks for
	 * this type of situation. Note: This passes null to the {@link Traverser#getSuccessors(E, E)}
	 * method for the value of the previous node.
	 * 
	 * @param checker
	 * @param traverser
	 *            the object handles gathering and returning the objects that will be passed to the
	 *            Executor
	 * @param root
	 *            the object to begin the traversal with
	 */
	public static <T> T breadthFirstSearch(Predicate<T> checker, Traverser<T> traverser, T root) {
		LinkedList<T> queue = new LinkedList<T>();

		queue.add(root);
		while (!queue.isEmpty()) {
			T node = queue.remove();

			if (checker.evaluate(node)) {
				return node;
			}

			for (Iterator<T> iter = traverser.getSuccessors(null, node); iter.hasNext();) {

				queue.add(iter.next());
			}
		}

		return null;
	}
    
//    public static List asList(Iterator iter) {
//    	//there has got to be autil method for this!
//    	List res = new ArrayList();
//    	for (; iter.hasNext();) {
//			res.add(iter.next());
//		}
//    	return res;
//    }
    
    public static <T> List<T> asList(Iterator iter) {
    	//there has got to be autil method for this!
    	List<T> res = new ArrayList<T>();
    	for (; iter.hasNext();) {
			res.add((T) iter.next());
		}
    	return res;
    }
}
