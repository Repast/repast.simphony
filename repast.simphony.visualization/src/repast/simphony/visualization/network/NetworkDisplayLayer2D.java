package repast.simphony.visualization.network;

import edu.umd.cs.piccolo.PNode;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization2D.DisplayLayer2D;
import repast.simphony.visualization.visualization2D.RepastCanvas2D;
import repast.simphony.visualization.visualization2D.style.EdgeStyle2D;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkDisplayLayer2D extends DisplayLayer2D<RepastEdge, PEdge> implements
        ProjectionListener {

  private static final long serialVersionUID = -1976427158589343866L;

  EdgeStyle2D style;

  Display2D display;

  Set<RepastEdge> edgesToAdd;

  List<PEdge> nodesToAdd, nodesToRemove;

  Set<RepastEdge> edgesToRemove;

  private Lock lock = new ReentrantLock();

  public static final String REL_KEY = "Relationship";

  public NetworkDisplayLayer2D(Network<?> net, EdgeStyle2D style,
                               Display2D display, RepastCanvas2D canvas) {
    super();
    this.style = style;
    this.display = display;

    edgesToAdd = new HashSet<RepastEdge>();
    nodesToAdd = new ArrayList<PEdge>();
    nodesToRemove = new ArrayList<PEdge>();
    edgesToRemove = new HashSet<RepastEdge>();
    canvas.addLayer(this);

    net.addProjectionListener(this);

    for (RepastEdge edge : net.getEdges()) {
      edgesToAdd.add(edge);
    }
  }

  /**
   * Given list PNode, return the objects that the PNodes represent.
   *
   * @param pnodes
   * @return the object that the specified pnodes represents
   */
  public List<Object> findObjsForItems(Collection<PNode> pnodes) {
    List<Object> objs = new ArrayList<Object>();
    Set<PNode> nodes = new HashSet<PNode>(pnodes);
    synchronized (visualItemMap) {
      Set<Map.Entry<RepastEdge, PEdge>> entries = visualItemMap
              .entrySet();
      for (Map.Entry<RepastEdge, PEdge> entry : entries) {
        PNode mapNode = entry.getValue();
        if (nodes.contains(mapNode)) {
          nodes.remove(mapNode);
          objs.add(entry.getKey());
        }
      }
    }
    return objs;
  }

  public void setStyle(EdgeStyle2D style) {
    this.style = style;
  }

  public void addEdge(RepastEdge edge) {
    try {
      lock.lock();
      // remove  it from the removed objects because
      // there might be a remove and then add in the same
      // update -- for example when a object switches contexts
      // the display is part of their parent context
      edgesToRemove.remove(edge);
      edgesToAdd.add(edge);
    } finally {
      lock.unlock();
    }
  }

  public void removeEdge(RepastEdge edge) {
    try {
      lock.lock();
      if (!edgesToAdd.remove(edge)) edgesToRemove.add(edge);
    } finally {
      lock.unlock();
    }
  }

  public void update(LayoutUpdater updater) {
    try {
      lock.lock();

      addAddedEdges();
      removeRemovedEdges();

    } finally {
      lock.unlock();
    }
  }

  protected void addAddedEdges() {
    for (RepastEdge rEdge : edgesToAdd) {
      PNode source = display.getVisualItem(rEdge.getSource());
      PNode target = display.getVisualItem(rEdge.getTarget());

      if (source != null && target != null) {

        PEdge edge = new PEdge(source, target);

        edge.addAttribute(REL_KEY, rEdge);
        visualItemMap.put(rEdge, edge);
        nodesToAdd.add(edge);
      }
    }
    edgesToAdd.clear();
  }

  protected void removeRemovedEdges() {
    for (RepastEdge rEdge : edgesToRemove) {
      PEdge edge = visualItemMap.remove(rEdge);
      if (edge != null) {
        nodesToRemove.add(edge);
      }
    }
    edgesToRemove.clear();
  }

  public void applyUpdates() {

    for (PEdge edge : nodesToAdd) {
      addChild(edge);
    }

    for (PEdge edge : nodesToRemove) {
      if (edge.getParent() != null) removeChild(edge);
    }

    for (int i = 0; i < this.getChildrenCount(); i++) {
      PEdge edge = (PEdge) getChild(i);
      RepastEdge redge = (RepastEdge) edge.getAttribute(REL_KEY);
      edge.setStroke(style.getStroke(redge));
      edge.setStrokePaint(style.getPaint(redge));
      edge.setTargetEndPaint(style.getTargetEndPaint(redge));
      edge.setTargetEndType(style.getTargetEndStyle(redge));
      edge.setSourceEndPaint(style.getSourceEndPaint(redge));
      edge.setSourceEndType(style.getSourceEndStyle(redge));

      edge.update();
    }

    nodesToRemove.clear();
    nodesToAdd.clear();
    repaint();
  }

  public void projectionEventOccurred(ProjectionEvent evt) {
    if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
      addEdge((RepastEdge) evt.getSubject());
    } else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
      removeEdge((RepastEdge) evt.getSubject());
    }
  }

  public PNode getVisualItem(Object obj) {
    return visualItemMap.get(obj);
  }

  public void addObject(Object o) {
    // TODO Auto-generated method stub
  }

  public void removeObject(Object obj) {
    // TODO Auto-generated method stub
  }
}