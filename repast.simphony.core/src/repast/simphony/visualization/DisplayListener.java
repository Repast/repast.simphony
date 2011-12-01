package repast.simphony.visualization;

/**
 * Interface for classes that listen for display events.
 *
 * @author Nick Collier
 */
public interface DisplayListener {

  /**
   * Called when a display sends an informational message. The message
   * will be the subject of the display event.
   *
   * @param evt the evt containing the messge sent by the display
   */
  void receiveInfoMessage(DisplayEvent evt);
}
