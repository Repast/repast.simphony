package repast.simphony.batch.distributed;

import java.io.Serializable;

/**
 * Interface for constructing messages to send to remote nodes.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public interface Message extends Serializable {

	/**
	 * Check to see if there is a message to send.
	 * @return a boolean if there is a message to send or not.
	 */
	public boolean isThereIsMessage();
}
