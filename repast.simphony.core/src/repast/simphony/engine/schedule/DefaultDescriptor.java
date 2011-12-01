/*CopyrightHere*/
package repast.simphony.engine.schedule;



/**
 * Default implementation of a
 * {@link repast.simphony.engine.schedule.Descriptor}. This just handles
 * the name of the descriptor.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultDescriptor implements Descriptor {
	private String name;

	/**
	 * Constructs the descriptor with the specified name.
	 * 
	 * @param name
	 *            the name of the descriptor
	 */
	public DefaultDescriptor(String name) {
		super();

		this.name = name;
	}

	/**
	 * Same as DefaultDescriptor(null).
	 * 
	 * @see #DefaultDescriptor(String)
	 */
	public DefaultDescriptor() {
		this(null);
	}

	/**
	 * Retrieves the name of this descriptor.
	 * 
	 * @return the name of this descriptor
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this descriptor.
	 * 
	 * @param name
	 *            the name of this descriptor
	 */
	public void setName(String name) {
		this.name = name;
	}

}
