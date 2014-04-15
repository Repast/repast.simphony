package repast.simphony.visualization.visualization2D;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javolution.util.FastSet;

import org.piccolo2d.PNode;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;
import org.piccolo2d.util.PBounds;

import repast.simphony.visualization.Layout;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.visualization2D.style.Style2D;

/**
 *  @deprecated replaced by ogl 2D
 */
public class StyledDisplayLayer2D extends DisplayLayer2D<Object, PNode> {
  private static final long serialVersionUID = 7275726272264198637L;

  Style2D style;

  RepastCanvas2D canvas;

  private Set<Object> addedObjects;
  private List<PNode> addedNodes, removedNodes;
  private Set<Object> removedObjects;

  private Lock lock = new ReentrantLock();

  private PBounds tmpBounds = new PBounds();

  public final static String LABEL_KEY = "LABEL";
  public final static String AGENT_KEY = "AGENT";
  public final static String BOUNDS_KEY = "BOUNDS";
  public final static String PAINT_KEY = "PAINT";
  public final static String STROKE_KEY = "STROKE";
  public final static String STROKE_PAINT_KEY = "STROKE PAINT";
  public final static String COORD_KEY = "COORDS";
  public final static String NODE_STYLE_ID = "NODE_STYLE";
  public final static String ROTATION_MODE = "ROTATION";


  public StyledDisplayLayer2D(Style2D s, RepastCanvas2D canvas) {
    super();
    this.style = s;
    this.canvas = canvas;
    canvas.addLayer(this);
    addedObjects = new FastSet<Object>();
    removedObjects = new FastSet<Object>();
    addedNodes = new ArrayList<PNode>();
    removedNodes = new ArrayList<PNode>();
  }

  public void addObject(Object o) {
    try {
      lock.lock();
      // remove  it from the removed objects because
      // there might be a remove and then add in the same
      // update -- for example when a object switches contexts
      // the display is part of their parent context
      removedObjects.remove(o);
      addedObjects.add(o);
    } finally {
      lock.unlock();
    }
  }

  /**
   * For now, this method is just storing the new location of the node.
   *
   * @param node
   * @param layout
   * @param doLayout
   */
  protected void updateNode(PNode node, Layout layout, boolean doLayout) {
    Object o = node.getAttribute(AGENT_KEY);
    applyUpdatesToNode(node);
    

    // TODO originally the node attributes were obtained from the style
    //      class, saved here and then
    //      set in applyUpdatesToNode, however it is not clear why
    //      this was done. - et
//		node.addAttribute(BOUNDS_KEY, style.getBounds(o));
//		node.addAttribute(PAINT_KEY, style.getPaint(o));
//		node.addAttribute(STROKE_KEY, style.getStroke(o));
//		node.addAttribute(STROKE_PAINT_KEY, style.getStrokePaint(o));
//		node.addAttribute(LABEL_KEY, style.getLabel(o));

    if (doLayout) {
      float[] loc = layout.getLocation(o);
      node.setOffset(putCoordLocation(node, loc));
      applyUpdatesToNode(node);
      //node.addAttribute(COORD_KEY,new Point2D.Double(loc[0],loc[1]));
    }
  }

  /**
   * Actually set the node attributes here.  Piccolo will check the set
   * values internally, and only change them if the old value is
   * different from the new value.
   *
   * @param node
   */
  protected void applyUpdatesToNode(PNode node) {
    Object o = node.getAttribute(AGENT_KEY);


    // TODO originally the node attributes were first stored and
    //      then set here, but it seems that the attributes
    //      can be read directly from the style
//		node.setPaint((Paint) node.getAttribute(PAINT_KEY));
//		((PPath)node).setStroke((Stroke) node.getAttribute(STROKE_KEY));
//		((PPath)node).setStrokePaint((Paint) node.getAttribute(STROKE_PAINT_KEY));
//		((PPath)node).setBounds(((Rectangle2D) node.getAttribute(BOUNDS_KEY)));

    Object nodeStyle = node.getAttribute(StyledDisplayLayer2D.NODE_STYLE_ID);
    if (nodeStyle != null) {
      style.getPNode(o, node);
    }
    Paint paint = style.getPaint(o);
    if (!paint.equals(node.getPaint())) node.setPaint(paint);

    if (node.getBooleanAttribute(StyledDisplayLayer2D.ROTATION_MODE, false)) {
        double rot = style.getRotation(o);
        if (rot != node.getRotation()) {
          node.rotateAboutPoint(rot - node.getRotation(), node
            .getBounds().getCenter2D());
        }
      }

    Rectangle2D bounds = style.getBounds(o);
    tmpBounds.setRect(bounds);
    if (!node.getBounds().equals(tmpBounds)) node.setBounds(bounds);

    if (node instanceof PPath) {
      ((PPath) node).setStroke(style.getStroke(o));
      ((PPath) node).setStrokePaint(style.getStrokePaint(o));
    }

    PText label = style.getLabel(o);
    if (label != null) {
      PText oldLabel = (PText) node.getAttribute(LABEL_KEY);

      if (oldLabel != null)
        node.removeChild(oldLabel);

      node.addAttribute(LABEL_KEY, label);

      AffineTransform trans = new AffineTransform();
      trans.setToScale(1, -1);
      label.transformBy(trans);
      node.addChild(label);
    }

    double scale = 0;
    if (!style.isScaled(o))
      scale = 1 / canvas.getCamera().getViewScale();

    else if (scale != 0)
      node.setScale(scale);

    // update the location if changed from previous and repaint node
    Point2D oldPoint = node.getOffset();
    Point2D newPoint = (Point2D) node.getAttribute(COORD_KEY);

    if (newPoint != null && oldPoint.distanceSq(newPoint) != 0) {
      node.setOffset(newPoint);
    }
  }

  /**
   * Called from Display2D.update() according to the update interval
   */
  public void update(LayoutUpdater updater) {
    try {
      lock.lock();
      addAddedObjects(updater.getLayout());
      removeRemovedObjects();

      for (int i = 0; i < this.getChildrenCount(); i++) {
        PNode node = this.getChild(i);
        updateNode(node, updater.getLayout(), updater.getUpdateItemsLocation());
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Called from Display2D.render() every time the schedule advances
   */
  public void applyUpdates() {
    for (PNode node : addedNodes) {
      addChild(node);
    }

    for (PNode node : removedNodes) {
      if (node.getParent() != null) removeChild(node);
    }

    for (int i = 0; i < this.getChildrenCount(); i++) {
      PNode node = this.getChild(i);
      applyUpdatesToNode(node);
    }

    removedNodes.clear();
    addedNodes.clear();
    repaint();
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
      Set<Map.Entry<Object, PNode>> entries = visualItemMap.entrySet();
      for (Map.Entry<Object, PNode> entry : entries) {
        PNode mapNode = entry.getValue();
        if (nodes.contains(mapNode)) {
          nodes.remove(mapNode);
          objs.add(entry.getKey());
        }
      }
    }
    return objs;
  }

  /**
   * This does a slow O(n) check on the visual items. This method should not
   * be used in general.
   *
   * @param node
   * @return the found object
   */
  public Object findObjForItem(PNode node) {
    List<Object> objs = new ArrayList<Object>();
    synchronized (visualItemMap) {
      Set<Map.Entry<Object, PNode>> entries = visualItemMap.entrySet();
      for (Map.Entry<Object, PNode> entry : entries) {
        PNode mapNode = entry.getValue();
        if (node.equals(mapNode)) {
          return entry.getKey();
        }
      }
    }
    return objs;
  }

  public void removeObject(Object o) {
    try {
      lock.lock();
      // if the object to remove is in addedObjects
      // we don't need to actually remove it because it
      // hasn't been added yet -- so just remove it from there
      // otherwise add it to the objects to remove.
      if (!addedObjects.remove(o)) removedObjects.add(o);
    } finally {
      lock.unlock();
    }
  }

  protected void addAddedObjects(Layout layout) {
    //if (doLayout) {
    for (Object object : addedObjects) {
      PNode node = createVisualItem(object);
      node.addAttribute(StyledDisplayLayer2D.AGENT_KEY, object);
      if (!style.isScaled(object)) {
        node.setScale(1 / canvas.getCamera().getViewScale());
      }
//      node.setPaint(style.getPaint(object));
      float[] location = layout.getLocation(object);
      // node.setX(location[0]);
      // node.setY(location[1]);
      Point2D pt = putCoordLocation(node, location);
      node.setOffset(pt);
      
      addedNodes.add(node);
    }

    /*
       * //} else { for (Object object : addedObjects) { PNode node =
       * createVisualItem(object); node.setPaint(style.getPaint(object));
       * node.addClientProperty(DisplayLayer2D.AGENT_KEY, object);
       * addedNodes.add(node); } }
       */
    addedObjects.clear();
  }

  /**
   * Calculates and returns the offset of the PNode, based on its location and rotation, 
   * and sets the PNode's COORD_KEY attribute.
   * @param node
   * @param location
   * @return
   */
  private Point2D putCoordLocation(PNode node, float[] location) {
		PBounds bounds = node.getBounds();
		Point2D.Double pt = (Point2D.Double) node.getAttribute(COORD_KEY);
		double r = Math.sqrt(bounds.width * bounds.width + bounds.height
				* bounds.height) / 2;
		double phi = Math.atan(bounds.height / bounds.width);
		double xOffset = r * Math.cos(node.getRotation() + phi);
		double yOffset = r * Math.sin(node.getRotation() + phi);
		pt.x = location[0] - xOffset;
		pt.y = location[1] - yOffset;
		return pt;
	}

  protected PNode createVisualItem(Object o) {
    PNode node = style.getPNode(o, null);
    node.setPaint(style.getPaint(o));
    node.setBounds(style.getBounds(o));
//    node.rotateAboutPoint(style.getRotation(o), node.getBounds().getCenter2D());
    node.rotateAboutPoint(style.getRotation(o) - node.getRotation(), node.getBounds().getCenter2D());
    node.addAttribute(COORD_KEY, new Point2D.Double(0, 0));
    visualItemMap.put(o, node);
    // style.getStroke()
    return node;
  }

  protected void removeRemovedObjects() {
    int count = 0;
    for (Object object : removedObjects) {
      PNode node = visualItemMap.remove(object);
      if (node != null) {
        removedNodes.add(node);
        count++;
      }
    }

    removedObjects.clear();
  }

  public void setStyle(Style2D style) {
    this.style = style;
  }

  public PNode getVisualItem(Object o) {
    return visualItemMap.get(o);
  }
}
