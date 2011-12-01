package repast.simphony.visualization.editor.gis;

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

import org.geotools.styling.Style;

import repast.simphony.gis.GeometryUtil;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.VisualizedObjectContainer;
import repast.simphony.visualization.editor.AgentCloner;
import repast.simphony.visualization.editor.AgentEditor;
import repast.simphony.visualization.editor.DisplayEditor;
import repast.simphony.visualization.editor.EditorNotifier;
import repast.simphony.visualization.editor.PEditorEventListener;
import repast.simphony.visualization.editor.VizEditorForm;
import repast.simphony.visualization.gis.DisplayGIS;
import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * DisplayEditor for gis displays.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class DisplayEditorGIS implements DisplayEditor, ObjectSelectionListener, ProjectionListener {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(DisplayEditorGIS.class);

  private DisplayGIS display;
  private JPanel panel;
  private JPanel editorPanel;
  private List<PEditorEventListener> modelessListeners = new ArrayList<PEditorEventListener>();
  private Map<Mode, PEditorEventListener> modalListeners = new HashMap<Mode, PEditorEventListener>();
  private GISAddListener addListener;
  private PEditorEventListener currentListener;
  private VizEditorForm palette;
  private AgentEditor agentEditor;
  private List<Network> networks = new ArrayList<Network>();
  private Network selectedNetwork;
  private EditorNotifier notifier;
  private Object objToAdd;
  private Geography geog;

  public DisplayEditorGIS(DisplayGIS display, JPanel panel, EditorNotifier notifier) {
    this.display = display;
    this.panel = panel;
    this.geog = display.getGeography();


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

  public void addModelessListener(PEditorEventListener listener) {
    modelessListeners.add(listener);
  }

  public void addModalListener(Mode mode, PEditorEventListener listener) {
    modalListeners.put(mode, listener);
  }

  public void initAddListener(GISAddListener listener) {
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
   * Called when a network is selected in the palette.
   *
   * @param net the selected network
   */
  public void netSelected(Network net) {
    selectedNetwork = net;
  }

  /**
   * Invoked when objects have been selected.
   *
   * @param objs the selected nodes.
   */
  public void objectsSelected(List<Object> objs) {
    List<Object> agents = new ArrayList<Object>();
    List<Object> edges = new ArrayList<Object>();

    for (Object obj : objs) {
      if (obj instanceof RepastEdge) edges.add(obj);
      else agents.add(obj);
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
  public RepastEdge addEdge(Object source, Object target, List<Coordinate> coords) {
    RepastEdge edge = selectedNetwork.addEdge(source, target);

    // TODO - added a projection listener to the geography to handle net events,
    //        so below should be unnecessary?
  
    // we add the edge to the geography but NOT to the context
    // as we don't want it to be a node in the network
    // in order for this to work we need to notify the gis display that
    // the object has been added so we have to mimic the event that
    // adding an object to a context would a cause
//    display.projectionEventOccurred(new ProjectionEvent(geog, edge, ProjectionEvent.Type.OBJECT_ADDED));

    // TODO use LineString or MultiLineString?
//  geog.move(edge, createLineString(coords));
    List<LineString> lines = new ArrayList<LineString>();
    lines.add(createLineString(coords));
    geog.move(edge, createMultiLineString(lines));
  
    agentEditor.edgeAdded(edge, true);
    notifier.editorEventOccurred();
    return edge;
  }

  public static LineString createLineString(List<Coordinate> coords) {
    Coordinate[] cArray = new Coordinate[coords.size()];
    return new GeometryFactory().createLineString(coords.toArray(cArray));
  }

  public static MultiLineString createMultiLineString(List<LineString> lines) {
    LineString[] lineStrings = new LineString[lines.size()];
    return new GeometryFactory().createMultiLineString(lines.toArray(lineStrings));
  }
  

  /**
   * Invoked when agents are selected.
   *
   * @param objs the selected agents
   */
  public void agentsSelected(Object[] objs) {
    if (objToAdd == null) {
      palette.setMode(Mode.SELECT);
      ((GISSelectionHandler) currentListener).objectsSelected(objs);
    }
  }

  /**
   * Invoked when edges are selected.
   *
   * @param objs the selected edges
   */
  public void edgesSelected(Object[] objs) {
    palette.setMode(Mode.SELECT);
    ((GISSelectionHandler) currentListener).objectsSelected(objs);
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
            net.removeEdge(edge);
            break;
          }
        }
        geog.move(edge, null); // remove the geography representation of the edge.
        
        // TODO - added a projection listener to the geography to handle net events,
        //        so below should be unnecessary?
        
        // mimic the event that removing an agent from a context
        // would fire. we need to do this because edges act like agents
        // when they are part of the geography
//        display.projectionEventOccurred(new ProjectionEvent(geog, edge, ProjectionEvent.Type.OBJECT_REMOVED));
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
      Style style = display.getStyleFor(obj.getClass().getName());
      GeometryUtil.GeometryType geomType = GeometryUtil.findGeometryType(style);
      addListener.getAddHandler().start(geomType);
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
   * @param coords the coordinates to add the agent at.
   */
  public void addAgentAt(List<Coordinate> coords, GeometryUtil.GeometryType type) {
    if (addListener.preAdd(coords, type)) {
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
    geog.addProjectionListener(this);

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
    display.getToolManager().addToGroup(palette.getButtonGroup());
    palette.setAddEdgeEnabled(networks.size() > 0);
    //palette.setMoveEnabled(false);
    palette.setPreferredSize(new Dimension(-1, 28));
    //editorPanel.add(palette, BorderLayout.NORTH);

    agentEditor = new AgentEditor();
    List<RepastEdge> edges = new ArrayList<RepastEdge>();
    for (Network net : networks) {
      net.addProjectionListener(this);
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

    // remove any edges that are registered as agents
    // edges as agents allows edges to have a geometry
    // in a geography
    List<Class> agentClasses = new ArrayList<Class>();
    for (Class clazz : display.getRegisteredClasses()) {
      if (!RepastEdge.class.isAssignableFrom(clazz)) agentClasses.add(clazz);
    }

    agentEditor.init(this, agentClasses, edges, height / 3);
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
    geog.removeProjectionListener(this);
    for (Network net : networks) {
      net.removeProjectionListener(this);
    }

    display.getToolManager().removeFromGroup(palette.getButtonGroup());
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
      RepastEdge edge = (RepastEdge) subject;
      agentEditor.edgeRemoved(edge);
      
      // TODO - added a projection listener to the geography to handle net events,
      //        so below should be unnecessary?
      
//      if (geog.getGeometry(edge) != null) {
//        geog.move(edge, null);
//        // moving null mimics normal removal and this
//        // completes the process
//        display.projectionEventOccurred(new ProjectionEvent(geog, edge, ProjectionEvent.Type.OBJECT_REMOVED));
//      }
    } else if (type == ProjectionEvent.OBJECT_MOVED) {
      agentEditor.agentMoved(subject);
      if (!(subject instanceof RepastEdge)) moveEdges(subject);
    }
  }

  private void moveEdges(Object obj) {
    com.vividsolutions.jts.geom.Point center = geog.getGeometry(obj).getCentroid();
    for (Network net : networks) {
      for (RepastEdge edge : ((Iterable<RepastEdge>) net.getEdges(obj))) {
        Geometry geom = geog.getGeometry(edge);
        if (geom != null) {
          int index = 0;
          if (edge.getTarget().equals(obj)) {
            index = geom.getCoordinates().length - 1;
          }
          geom.getCoordinates()[index].x = center.getX();
          geom.getCoordinates()[index].y = center.getY();
          geom.geometryChanged();
          display.projectionEventOccurred(new ProjectionEvent(geog, edge,
                  ProjectionEvent.Type.OBJECT_MOVED));
        }
      }
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
}