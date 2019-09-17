package repast.simphony.ws;

import repast.simphony.visualization.engine.DisplayCreationException;

/**
 * Interface for classes that can create a display.
 * 
 * @author Nick Collier
 */
public interface ServerDisplayCreator {

	  /**
	   * Creates an DisplayServer.
	   * 
	   * @return the created display
	   * @throws DisplayCreationException if there is an error while creating the display.
	   */
	  DisplayServer createDisplay() throws DisplayCreationException;

}
