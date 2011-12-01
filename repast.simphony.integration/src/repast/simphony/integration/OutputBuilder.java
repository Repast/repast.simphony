/*CopyrightHere*/
package repast.simphony.integration;

/**
 * Interface representing a type that builds the "output" data read in from a Reader.
 * Implementations of this interface are assumed to be dealing with some sort of hierarchical data
 * and will write to the location they are currently set at. The location is set with the
 * {@link #goRoot()}, {@link #goUp()}, and {@link #createAndGoInto(String)} methods.<p/>
 * 
 * Initialize must be called before using an {@link OutputBuilder}.
 * 
 * @see repast.simphony.integration.Reader
 * 
 * @author Jerry Vos
 */
public interface OutputBuilder<X, Y> extends Queryable {
	/**
	 * Initializes the writer for writing.
	 */
	void initialize();

	/**
	 * Enters into the sub-tree with the specified name, creating the tree (or any values in it) if
	 * needed.
	 * 
	 * @see #detach(Iterable)
	 * 
	 * @param name
	 *            the name of the tree
	 * @return a detachable object
	 */
	X createAndGoInto(String name);

	/**
	 * Goes up a level in the hierarchy.
	 */
	void goUp();

	/**
	 * Goes to the root in the hierarchy.
	 */
	void goRoot();

	/**
	 * Writes the specifed value using the specified name. What this means is implementation
	 * dependent.
	 * 
	 * @param name
	 *            the name to write the value under
	 * @param value
	 *            the value to write
	 * @return a detachable object
	 */
	X writeValue(String name, Object value);

	/**
	 * Retrieves the object (or tree) that has been built with this outputter. This corresponds to
	 * the root of the tree, not the current branch.
	 * 
	 * @return the object that is a result of the previous writes
	 */
	Y getWrittenObject();

	/**
	 * Detaches the current tree and goes up a level.
	 */
	void detach();

	/**
	 * Detaches the specified objects. These should be return values from the
	 * {@link #writeValue(String, Object)} or {@link createAndGoInto} methods.
	 * 
	 * @param objsToDetach
	 *            the objects to detach
	 */
	void detach(Iterable<X> objsToDetach);

}