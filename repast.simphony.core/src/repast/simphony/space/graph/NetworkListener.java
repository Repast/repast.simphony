package repast.simphony.space.graph;

/**
 * Interface to be implemented by classes that want to listen for NetworkEvent-s.
 *
 * @author Nick Collier
 * @version $Date$ $Revision$
 */
public interface NetworkListener<T> {

	/**
	 * Notifies the implementor that a network event has occured.
	 *
	 * @param ev the details of the occuring event
	 */
	public void networkEventOccured(NetworkEvent<T> ev);
}

