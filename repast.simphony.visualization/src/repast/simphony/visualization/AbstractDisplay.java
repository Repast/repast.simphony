package repast.simphony.visualization;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JToolBar;

import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

public abstract class AbstractDisplay implements IDisplay, ProjectionListener {
  
  // approx one sixitieth of a second in milliseconds
  // 60 frames a second
  public static long FRAME_UPDATE_INTERVAL = 17;
  
  private static final String INFO_POINTER_ICON = "info_viz_pointer.png";

  protected static final Cursor INFO_CURSOR = createInfoCursor();

  protected ProbeListenerSupport probeSupport = new ProbeListenerSupport();
  protected DisplayListenerSupport dlSupport = new DisplayListenerSupport();
  
  // timestamp of the last render
  protected long lastRenderTS = 0;

  public void projectionEventOccurred(ProjectionEvent evt) {
    if (evt.getType() == ProjectionEvent.OBJECT_ADDED) {
      Object obj = evt.getSubject();
      addObject(obj);
    } else if (evt.getType() == ProjectionEvent.OBJECT_REMOVED) {
      Object obj = evt.getSubject();
      removeObject(obj);
    } else if (evt.getType() == ProjectionEvent.OBJECT_MOVED) {
      Object obj = evt.getSubject();
      moveObject(obj);
    }/*
      * else if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
      * addEdge((RepastEdge)evt.getSubject(), (Network)evt.getProjection()); }
      * else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
      * removeEdge((RepastEdge)evt.getSubject(), (Network)evt.getProjection());
      * }
      */
  }

  protected abstract void addObject(Object o);

  protected abstract void removeObject(Object o);

  protected abstract void moveObject(Object o);

  /**
   * Adds a probe listener to listen for probe events produced by this IDisplay.
   * 
   * @param listener
   *          the listener to add
   */
  public void addProbeListener(ProbeListener listener) {
    probeSupport.addProbeListener(listener);
  }

  /**
   * Adds a display listener to this display.
   * 
   * @param listener
   *          the listener to add
   */
  public void addDisplayListener(DisplayListener listener) {
    dlSupport.addDisplayListener(listener);
  }

  /**
   * Registers the specified toolbar with this IDisplay. This IDisplay can then
   * put buttons etc. are on this toolbar. This is a default no-op
   * implementation.
   * 
   * @param bar
   *          the bar to register
   */
  public void registerToolBar(JToolBar bar) {
  }

  public ProbeListenerSupport getProbeSupport() {
    return probeSupport;
  }

  public void setProbeSupport(ProbeListenerSupport probeSupport) {
    this.probeSupport = probeSupport;
  }

  /**
   * Destroys the display, allowing it to free any resources it may have
   * acquired.
   */
  public void destroy() {
    probeSupport.removeAllProbeListeners();
  }

  public static Cursor createInfoCursor() {
    BufferedImage im = null;
    try {
      URL url = AbstractDisplay.class.getClassLoader().getResource(INFO_POINTER_ICON);
      if (url == null)
        return null;

      im = ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Toolkit.getDefaultToolkit().createCustomCursor(im, new Point(0, 0), "Info Cursor");
  }
}
