package repast.simphony.integration;

import org.jdom.Content;

/**
 * A {@link org.jdom.Content} that holds a value in it, and when it's getValue method is called will
 * return that value. Used by the JDOM builders for storing values in {@link org.jdom.Element}s.
 * 
 * @author Jerry Vos
 */
public class DataContent extends Content {
	private static final long serialVersionUID = 8305400916413852228L;

	private Object data;

	/**
	 * Creates this without an object to use as data. Before this is used the
	 * {@link #setData(Object)} method should be called
	 * 
	 * @see #setData(Object)
	 */
	public DataContent() {
		this(null);
	}

	/**
	 * Creates this with the specified object as data.
	 * 
	 * @see #getValue()
	 * 
	 * @param data
	 *            the object to used in {@link #getValue()}
	 */
	public DataContent(Object data) {
		super();
		this.data = data;
	}

	/**
	 * Retrieves the string value of the data.
	 * 
	 * @return null if the data is null, otherwise <code>data.toString()</code>
	 */
	@Override
	public String getValue() {
		if (data == null) {
			return null;
		}
		return data.toString();
	}

	/**
	 * Retrieves the data to be used in the {@link #getValue()} method.
	 * 
	 * @return the data object
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets the data to be used in the {@link #getValue()} method.
	 * 
	 * @param data
	 *            the data object
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * This is the same as {@link #getValue()}.
	 * 
	 * @see #getValue()
	 * 
	 * @return the same as {@link #getValue()}
	 */
	@Override
	public String toString() {
		return getValue();
	}
}
