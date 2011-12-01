/*CopyrightHere*/
package repast.simphony.space.projection;

/**
 * Interface for classes that wish to add objects to a space. An adder is used by setting it on a
 * space. The space then will use whatever strategy is defined by the Adder to add objects to
 * itself. For example, a random grid adder may add objects at random locations on a grid.
 * 
 * @author Nick Collier
 */
public interface Adder<T, U> {
	/**
	 * Adds the specified object to the specified destination. Implementors are responsible for how
	 * this will actually occur, the location of the added object etc.
	 * 
	 * @param destination
	 *            the destination to add the object to.
	 * @param object
	 *            the object to add.
	 */
	void add(T destination, U object);

}
