/*CopyrightHere*/
package repast.simphony.integration;

/**
 * A simple class used as the root object for Bean querying. This allows bean querying to use the
 * same XPath expressions as with DOM querying (meaning they both can start with /FileDef).
 * 
 * @author Jerry Vos
 */
public class FileDef {
	private Object fileDef;

	/**
	 * Constructs this with the specified object to return from {@link #getFileDef()}.
	 * 
	 * @param fileDef
	 *            the object to return in the getter
	 */
	public FileDef(Object fileDef) {
		this.fileDef = fileDef;
	}

	/**
	 * Returns the given FileDef object.
	 * 
	 * @return the given FileDef object
	 */
	public Object getFileDef() {
		return fileDef;
	}
}