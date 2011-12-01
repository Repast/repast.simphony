package repast.simphony.scenario;

/**
 * Encapsulates an action entry in a scenario.xml file. Object implementing this interface
 * can be used with the Scenario.vt file.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */

public interface ScenarioEntry {
	/**
	 * Gets the file name of the serialized action.
	 *
	 * @return the file name of the serialized action.
	 */
	String getFileName();

	/**
	 * Gets the context the action is associated with.
	 *
	 * @return the context the action is associated with.
	 */
	String getContextID();

	/**
	 * Gets the id associated with the action itself. For example, repast.action.display.
	 * @return the id associated with the action itself. For example, repast.action.display.
	 */
	String getRegistryID();
}
