/*CopyrightHere*/
package repast.simphony.space.continuous;

/**
 * This is a simple implementation of an adder that doesn't perform any action.
 */
public class SimpleCartesianAdder<T> implements ContinuousAdder<T> {

	/**
	 * Adds the specified object to the space. This will add the object to a space without assigning
	 * it a location. When this adder is used, the user must then move the object to some location
	 * on the space. 
	 * 
	 * @param destination
	 *            the space to add the object to.
	 * @param object
	 *            the object to add.
	 */
	public void add(ContinuousSpace<T> destination, T object) {
		// do nothing
	}

}
