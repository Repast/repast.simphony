package repast.simphony.visualization.editor;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.VisualizedObjectContainer;

/**
 * Interface for classes that allow the editing of display. This includes
 * adding new agents, edges, etc.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface DisplayEditor extends DisplayEditorLifecycle {

  /**
   * Adds a new agent of the specified class the current agent collection
   *
   * @param clazz the class of agent to add
   */
  void addAgent(Class clazz);

  /**
   * Removes the specified edges from the network.
   *
   * @param edges the edges to remove
   * @return true if the remove was was confirmed and successful otherwise false.
   */
  boolean removeEdges(RepastEdge[] edges);

  /**
   * Removes the specified agents from the agent collection.
   *
   * @param agents the agents to remove
   * @return true if the remove was confirmed and successful otherwise false.
   */
  boolean removeAgents(Object[] agents);

  /**
   * Clones the specified agent and adds the clone to the agent collection.
   *
   * @param agent the agent to clone
   */
  void cloneAgent(Object agent);

  /**
   * Invoked when agents are selected.
   *
   * @param objs the selected agents
   */
  void agentsSelected(Object[] objs);

  /**
   * Invoked when edges are selected.
   *
   * @param objs the selected edges
   */
  void edgesSelected(Object[] objs);

  /**
   * Gets the container of the visualized objects.
   *
   * @return the container of the visualized objects.
   */
  VisualizedObjectContainer getContainer();

  public enum Mode {
    MOVE, SELECT, ADD_EDGE, NONE
  }

  /**
   * Called when the palette mode is switched.
   *
   * @param mode the new mode
   */
  void modeSwitched(Mode mode);

  /**
   * Called when a network is selected in the palette.
   *
   * @param net the selected network
   */
  void netSelected(Network net);

}
