package repast.simphony.terracotta.datagrid;

/**
 * Defines the interface to an object which holds work state.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public interface WorkCache {
	/**
	 * Set the source based on its location in a storable object.
	 * @param location the place an object is stored
	 * @param workSource the work object
	 */
	public void setWork(String location, RepastWork workSource);

	/**
	 * Return the source object for a given location definition of the object.
	 * 
	 * @param location the fully qualified location
	 * @return work object, null if it does not exist
	 */
	public RepastWork getWork(String location);

	/**
	 * Return true if the source is cached.
	 * 
	 * @param location the fully qualified location
	 * @return true if the work exists, otherwise false
	 */
	public boolean hasWork(String location);
}