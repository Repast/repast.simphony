/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.IDisplay;

/**
 * Interface for classes that can create a display.
 * 
 * @author Nick Collier
 */
public interface DisplayCreator {
  
  /**
   * Creates an IDisplay.
   * 
   * @return the created display
   * @throws DisplayCreationException if there is an error while creating the display.
   */
  IDisplay createDisplay() throws DisplayCreationException;

}
