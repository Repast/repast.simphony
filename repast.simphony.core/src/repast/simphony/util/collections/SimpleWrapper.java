/*CopyrightHere*/
package repast.simphony.util.collections;


/**
 * A simple wrapper class that will hold an object and for its {@link #toString()} will return a
 * given description. This stores the description as the super's first, and the object as the
 * super's second.
 * 
 * @author Jerry Vos
 */
public class SimpleWrapper<T> extends Pair<String, T> {

	/**
	 * Constructs this with the specified description and wrapped object. This calls on the super
	 * {@link Pair#Pair(X, Y)} with the first argument as the description and the second as the
	 * wrapped object.
	 * 
	 * @param description
	 *            the description of the wrapped object
	 * @param wrappedObject
	 *            the wrapped object
	 */
	public SimpleWrapper(String description, T wrappedObject) {
		super(description, wrappedObject);
	}

	/**
	 * Same as {@link Pair#getFirst()}.
	 */
	@Override
	public String toString() {
		return getFirst();
	}

	/**
	 * Same as {@link Pair#getSecond()}.
	 */
	public T getWrappedObject() {
		return getSecond();
	}
}
