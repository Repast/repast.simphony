package repast.simphony.visualization.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.piccolo2d.PNode;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.VisualizedObjectContainer;
import repast.simphony.visualization.network.NetworkDisplayLayer2D;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization2D.StyledDisplayLayer2D;
import simphony.util.messages.MessageCenter;

/**
 * DisplayEditor for 2D displays.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class DisplayEditor2D implements DisplayEditor, PNodeSelectionListener, ProjectionListener {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(DisplayEditor2D.class);

  private Display2D display;
  private JPanel panel;
  private JPanel editorPanel;
  private List<PEditorEventListener> modelessListeners = new ArrayList<PEditorEventListener>();
  private Map<Mode, PEditorEventListener> modalListeners = new HashMap<Mode, PEditorEventListener>();
  private AddListener addListener;
  private PEditorEventListener currentListener;
  private VizEditorForm palette;
  private AgentEditor agentEditor;
  private List<Network> networks = new ArrayList<Network>();
  private Network selectedNetwork;
  private EditorNotifier notifier;
  private Object objToAdd;

  DisplayEditor2D(Display2D display, JPanel panel, EditorNotifier notifier) {
    this.display = display;
    this.panel = panel;
    for (Projection proj : (Iterable<Projection>) display.getInitData().getProjections()) {
      if (proj instanceof Network) {
        networks.add((Network) proj);
      }
    }

    if (networks.size() > 0) {
      selectedNetwork = networks.get(0);
    }

    this.notifier = notifier;
  }

  void addModelessListener(PEditorEventListener listener) {
    modelessListeners.add(listener);
  }

  void addModalListener(Mode mode, PEditorEventListener listener) {
    modalListeners.put(mode, listener);
  }

  void initAddListener(AddListener listener) {
    addListener = listener;
  }

  /**
   * Called when the palette mode is switched.
   *
   * @param mode the new mode
   */
  public void modeSwitched(Mode mode) {
    if (currentListener != null) currentListener.stop();
    currentListener = modalListeners.get(mode);
    if (currentListener != null) currentListener.start();
  }

  /**
   * Invoked when nodes are selected.
   *
   * @param nodes the selected nodes
   */
  public void pNodesSelected(List<PNode> nodes) {
    List<Object> agents = new ArrayList<Object>();
    List<Object> edges = new ArrayList<Object>();

    for (PNode node : nodes) {
      Object obj = node.getAttribute(StyledDisplayLayer2D.AGENT_KEY);
      // add the agents
      if (obj != null) {
        agents.add(obj);
        continue;
      }

      // add the edges
      obj = node.getAttribute(NetworkDisplayLayer2D.REL_KEY);
      if (obj != null) edges.add(obj);

    }
    agentEditor.edgesSelected(edges);
    agentEditor.agentsSelected(agents);
  }

  /**
   * Creates and returns a RepastEdge in the currently selected network.
   *
   * @param source the edges source
   * @param target the edges target
   * @return the created edge
   */
  public RepastEdge addEdge(Object source, Object target) {
    RepastEdge edge = selectedNetwork.addEdge(source, target);
    agentEditor.edgeAdded(edge, true);
    return edge;
  }

  /**
   * Invoked when agents are selected.
   *
   * @param objs the selected agents
   */
  public void agentsSelected(Object[] objs) {
    if (objToAdd == null) {
      palette.setMode(Mode.SELECT);
      ((SelectionHandler) currentListener).objectsSelected(objs);
    }
  }

  /**
   * Invoked when edges are selected.
   *
   * @param objs the selected edges
   */
  public void edgesSelected(Object[] objs) {
    palette.setMode(Mode.SELECT);
    ((SelectionHandler) currentListener).objectsSelected(objs);
  }

  /**
   * Adds a new agent of the specified class the current agent collection
   *
   * @param clazz the class of agent to add
   */
  public void addAgent(Class clazz) {
    try {
      Object agent = clazz.newInstance();
      doAddAgent(agent);
    } catch (IllegalAccessException e) {
      String info = "Error while creating a new agent: see the error log for details.";
      JOptionPane.showMessageDialog(editorPanel, info, "Editor Error", JOptionPane.ERROR_MESSAGE);
      msg.error(info, e);
    } catch (InstantiationException e) {
      String info = "Error while creating a new agent: see the error log for details.";
      JOptionPane.showMessageDialog(editorPanel, info, "Editor Error", JOptionPane.ERROR_MESSAGE);
      msg.error(info, e);
    }
  }

  /**
   * Removes the specified edges from the network.
   *
   * @param edges the edges to remove
   * @return true if the remove was was confirmed and successful otherwise false.
   */
  public boolean removeEdges(RepastEdge[] edges) {
    int res = JOptionPane.showConfirmDialog(editorPanel, "Are you sure you want to delete the selected edges?",
            "Delete Edges", JOptionPane.YES_NO_OPTION);
    if (res == JOptionPane.YES_OPTION) {
      for (RepastEdge edge : edges) {
        for (Network net : networks) {
          if (net.containsEdge(edge)) {
//            System.out.println("edge found");
            net.removeEdge(edge);
            break;
          }

        }
      }
      notifier.editorEventOccurred();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Removes the specified agents from the agent collection.
   *
   * @param agents the agents to remove
   * @return true if the remove was confirmed and successful otherwise false.
   */
  public boolean removeAgents(Object[] agents) {
    int res = JOptionPane.showConfirmDialog(editorPanel, "Are you sure you want to delete the selected agents?",
            "Delete Agents", JOptionPane.YES_NO_OPTION);
    if (res == JOptionPane.YES_OPTION) {
      VisualizedObjectContainer container = display.getInitData().getContainer();
      for (Object agent : agents) {
        container.remove(agent);
      }
      notifier.editorEventOccurred();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Clones the specified agent and adds the clone to the agent collection.
   *
   * @param agent the agent to clone
   */
  public void cloneAgent(Object agent) {
    AgentCloner cloner = new AgentCloner(agent);
    try {
      Object clone = cloner.createClone();
      doAddAgent(clone);
    } catch (Exception ex) {
      String info = "Error while cloning agent: see the error log for details.";
      JOptionPane.showMessageDialog(editorPanel, info, "Editor Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doAddAgent(Object obj) {
    if (addListener != null) {
      objToAdd = obj;
      currentListener.stop();
      addListener.getAddHandler().start();
    }
  }

  /**
   * Notifies this editor that an add was canceled.
   */
  public void addCanceled() {
    objToAdd = null;
    addListener.postAdd();
    addListener.getAddHandler().stop();
    currentListener.start();
  }

  /**
   * Adds the current object to add at the specified coordinates.
   *
   * @param x the x location to add the object
   * @param y the y location to add the object
   */
  public void addAgentAt(double x, double y) {
    if (addListener.preAdd(objToAdd, x, y)) {
      addListener.getAddHandler().stop();
      display.getInitData().getContainer().add(objToAdd);
      notifier.editorEventOccurred();
      addListener.postAdd();
      agentEditor.agentAdded(objToAdd, true);
      currentListener.start();
      Object tmp = objToAdd;
      objToAdd = null;
      agentsSelected(new Object[]{tmp});
    } else {
      JOptionPane.showMessageDialog(panel, "Invalid location. Please try again.", "Error Adding Agent",
              JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Runs the editor.
   */
  public void run() {
    display.update();
    display.render();

    for (Projection proj : (Iterable<Projection>) display.getInitData().getProjections()) {
      proj.addProjectionListener(this);
    }

    for (PEditorEventListener listener : modelessListeners) {
      listener.init();
      listener.start();
    }

    for (PEditorEventListener listener : modalListeners.values()) {
      listener.init();
    }

    editorPanel = new JPanel(new BorderLayout());
    JPanel parent = (JPanel) panel.getParent();
    parent.remove(panel);

    palette = new VizEditorForm();
    palette.init(this, networks);
    palette.setAddEdgeEnabled(networks.size() > 0);
    palette.setPreferredSize(new Dimension(-1, 28));
    //editorPanel.add(palette, BorderLayout.NORTH);

    agentEditor = new AgentEditor();
    List<RepastEdge> edges = new ArrayList<RepastEdge>();
    for (Network net : networks) {
      for (RepastEdge edge : (Iterable<RepastEdge>) net.getEdges()) {
        edges.add(edge);
      }
    }

    // null signifies no nets present.
    if (edges.isEmpty()) edges = null;

    int height = panel.getSize().height;
    if (height == 0) height = panel.getPreferredSize().height;
    int width = panel.getSize().width;
    if (width == 0) width = panel.getPreferredSize().width;
    agentEditor.init(this, display.getRegisteredClasses(), edges, height / 3);
    JPanel p = new JPanel(new BorderLayout());
    p.add(panel, BorderLayout.CENTER);
    p.add(palette, BorderLayout.NORTH);
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p, agentEditor);
    splitPane.setDividerLocation(width / 3 * 2);

    editorPanel.add(splitPane, BorderLayout.CENTER);
    parent.add(editorPanel);

    parent.revalidate();
    parent.repaint();
  }

  /**
   * Stops the Editor and performs any clean up.
   */

  public void stop() {
    if (addListener != null) addListener.getAddHandler().stop();

    for (PEditorEventListener listener : modelessListeners) {
      listener.stop();
      listener.destroy();
    }

    for (PEditorEventListener listener : modalListeners.values()) {
      listener.destroy();
    }

    for (Projection proj : (Iterable<Projection>) display.getInitData().getProjections()) {
      proj.removeProjectionListener(this);
    }

    if (currentListener != null) currentListener.stop();

    Container parent = editorPanel.getParent();
    parent.remove(editorPanel);
    panel.getParent().remove(panel);
    editorPanel.removeAll();
    parent.add(panel, BorderLayout.CENTER);

    panel.revalidate();
    panel.repaint();
    parent.invalidate();
    parent.repaint();
  }

  /**
   * Invoked when a projection event occurs.
   *
   * @param evt the object describing the event
   */
  public void projectionEventOccurred(ProjectionEvent evt) {
    Object subject = evt.getSubject();
    ProjectionEvent.Type type = evt.getType();
    if (type == ProjectionEvent.OBJECT_ADDED) {
      agentEditor.agentAdded(subject, false);
    } else if (type == ProjectionEvent.OBJECT_REMOVED) {
      agentEditor.agentRemoved(subject);
    } else if (type == ProjectionEvent.EDGE_ADDED) {
      agentEditor.edgeAdded((RepastEdge) subject, false);
    } else if (type == ProjectionEvent.EDGE_REMOVED) {
      agentEditor.edgeRemoved((RepastEdge) subject);
    } else if (type == ProjectionEvent.OBJECT_MOVED) {
      agentEditor.agentMoved(subject);
    }
  }

  /**
   * Gets the container of the visualized objects.
   *
   * @return the container of the visualized objects.
   */
  public VisualizedObjectContainer getContainer() {
    return display.getInitData().getContainer();
  }

  public void netSelected(Network network) {
    selectedNetwork = network;
  }
}
