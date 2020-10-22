/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.util.concurrent.locks.Lock;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.LayoutUpdater;
import saf.v3d.AppearanceFactory;
import saf.v3d.scene.VEdge2D;
import saf.v3d.scene.VEdgeLayer;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VSpatial;

/**
 * @author Nick Collier
 */
@SuppressWarnings("rawtypes")
public class NetworkLayerOGL2D extends AbstractDisplayLayerOGL2D<EdgeStyleOGL2D> implements
    ProjectionListener {

  private boolean isDirected;
  private DisplayOGL2D display;

  public NetworkLayerOGL2D(Network<?> network, EdgeStyleOGL2D style, VLayer layer,
      DisplayOGL2D display) {
    super(style, new VEdgeLayer());
    layer.addChild(this.layer);

    isDirected = network.isDirected();
    network.addProjectionListener(this);
    this.display = display;

    for (RepastEdge edge : network.getEdges()) {
      addObject(edge);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.space.projection.ProjectionListener#projectionEventOccurred
   * (repast.simphony.space.projection.ProjectionEvent)
   */
  public void projectionEventOccurred(ProjectionEvent evt) {
    if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
      Lock lock = display.getRenderLock();
      try {
        lock.lock();
        addObject((RepastEdge) evt.getSubject());
      } finally {
        lock.unlock();
      }
    } else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
      Lock lock = display.getRenderLock();
      try {
        lock.lock();
        removeObject((RepastEdge) evt.getSubject());
      } finally {
        lock.unlock();
      }
    }
  }

  private void applyStyle(RepastEdge edge, VEdge2D item) {
    item.setAppearance(AppearanceFactory.createColorAppearance(style.getColor(edge)));
    item.setEdgeWidth(style.getLineWidth(edge));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.AbstractDisplayLayerOGL2D#update(repast
   * .simphony.visualization.LayoutUpdater)
   */
  // Note the updater is not actually used here
  @Override
  public void update(LayoutUpdater updater) {
    for (Object obj : toBeRemoved) {
      VEdge2D item = (VEdge2D) objMap.remove(obj);
      layer.removeChild(item);
    }
    toBeRemoved.clear();

    // style, update the location of the existing nodes
    for (VSpatial item : layer.children()) {
      RepastEdge rEdge = (RepastEdge) item.getProperty(MODEL_OBJECT_KEY);
      VEdge2D edge = (VEdge2D) item;
      applyStyle(rEdge, edge);
      edge.update();
    }

    // create VSpatials for the objs to add,
    // style, update the location of them.
    for (Object obj : toBeAdded) {
      RepastEdge rEdge = (RepastEdge) obj;
      VSpatial source = display.getSpatialForObject(rEdge.getSource());
      VSpatial target = display.getSpatialForObject(rEdge.getTarget());
      
      if (source == null) {
    	  System.out.println("NULL SOURCE: " + rEdge.getSource().hashCode());
    	  System.exit(0);
      }
      
      if (target == null) {
    	  System.out.println("NULL TARGET: " + rEdge.getTarget().hashCode());
    	  System.exit(0);
      }
      
      VEdge2D edge = new VEdge2D(source, target, isDirected);
      edge.putProperty(MODEL_OBJECT_KEY, obj);
      objMap.put(rEdge, edge);
      applyStyle(rEdge, edge);
      layer.addChild(edge);
    }
    toBeAdded.clear();

  }

}
