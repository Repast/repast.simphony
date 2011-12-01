package repast.simphony.visualization.editor;

/**
 * Listens for agent add events.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface AddListener {

  /**
   * Called immediately prior to an agent being added.
   *
   * @param obj      the object that will be added
   * @param location the location at which the agent should
   *                 be added. @return true if an object can be added to the specified location,
   *                 otherwise false.
   */
  boolean preAdd(Object obj, double... location);

  /**
   * Called immediately after the agent has been added.
   */
  void postAdd();

  /**
   * Gets the PInputEvent handler that will handle
   * the gui part of the adding.
   *
   * @return the PInputEvent handler that will handle
   *         the gui part of the adding.
   */
  PEditorEventListener getAddHandler();
}
