/*CopyrightHere*/
package repast.simphony.engine.schedule;

/**
 * Interface representing a descriptor used to build a simulation's settings.
 * This is meant to be extended by specialized descriptors and the actual
 * simulation built by controller actions that parse the descriptors and build
 * the necessary simulation pieces.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface Descriptor {
	/**
	 * Retrieves the name of the descriptor. This will generally be used for
	 * displaying the descriptor in GUIs and in other situations.
	 * 
	 * @return the name of this descriptor
	 */
	public String getName();

	/**
	 * Sets the name of the descriptor. This will generally be used for
	 * displaying the descriptor in GUIs and in other situations.
	 * 
	 * @param name
	 *            the name of this descriptor
	 */
	public void setName(String name);
}
