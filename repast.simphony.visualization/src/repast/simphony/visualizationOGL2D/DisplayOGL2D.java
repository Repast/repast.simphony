package repast.simphony.visualizationOGL2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.jogamp.opengl.GLAutoDrawable;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jogamp.vecmath.Point3f;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.ui.Imageable;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.AbstractDisplay;
import repast.simphony.visualization.AddedRemovedLayoutUpdater;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.IntervalLayoutUpdater;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.MovedLayoutUpdater;
import repast.simphony.visualization.UpdateLayoutUpdater;
import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import saf.v3d.Canvas2D;
import saf.v3d.CanvasListener;
import saf.v3d.picking.PickEvent;
import saf.v3d.picking.PickListener;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VRoot;
import saf.v3d.scene.VSpatial;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

/**
 * 2D display that uses jogl for rendering.
 * 
 * @author Nick Collier
 */
public class DisplayOGL2D extends AbstractDisplay implements CanvasListener, PickListener,
    ChangeListener {

  static {
    // this seems to fix jogl canvas flicker issues on windows
    System.setProperty("sun.awt.noerasebackground", "true");
  }

  protected Runnable updater = new Runnable() {
    public void run() {
      canvas.update();
    }
  };

  protected JPanel panel;
  protected Layout<?, ?> layout;
  protected Canvas2D canvas;
  protected DisplayData<?> displayData;

  protected Map<Class<?>, StyledDisplayLayerOGL2D> classStyleMap = new HashMap<Class<?>, StyledDisplayLayerOGL2D>();
  protected Map<Network<?>, NetworkLayerOGL2D> networkStyleMap = new HashMap<Network<?>, NetworkLayerOGL2D>();
  protected Map<ValueLayer, ValueLayerDisplayLayer> valueLayerStyleMap = new HashMap<ValueLayer, ValueLayerDisplayLayer>();
  protected Map<String, ProjectionDecorator2D<?>> decoratorMap = new HashMap<String, ProjectionDecorator2D<?>>();
  protected LayoutUpdater layoutUpdater;
  protected boolean doRender = false, glInitialized = false, iconified = false;
  protected JTabbedPane tabParent = null;
  protected Component tabChild = null;

  protected VLayer decoratorLayer = new VLayer();

  public DisplayOGL2D(DisplayData<?> data, Layout<?, ?> layout) {
    this.layout = layout;

    try {
      canvas = new Canvas2D();
    } catch (Exception ex) {
      // could get here if jogl is not installed
      MessageCenter.getMessageCenter(getClass()).error(
          "Error initializing OGL 2D display. "
              + "JOGL must be installed and computer must support open gl rendering", ex);
    }

    Box box = layout.getBoundingBox();
    if (box.getWidth() > 0) {
      canvas.setDefaultExtent(box.getWidth(), box.getHeight());
    }

    canvas.addCanvasListener(this);
    canvas.addPickListener(this);
    canvas.getRoot().addChild(decoratorLayer);
    displayData = data;
    layoutUpdater = new UpdateLayoutUpdater(layout);
  }

  /**
   * Gets the lock used to synchronize between update thread and display thread.
   * 
   * @return the lock used to synchronize between update thread and display
   *         thread.
   */
  Lock getRenderLock() {
    return canvas.getRenderLock();
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#init()
   */
  public void init() {
    boolean decoAdded = false;
    for (Projection<?> proj : displayData.getProjections()) {
      proj.addProjectionListener(this);
      ProjectionDecorator2D<?> deco = decoratorMap.get(proj.getName());
      if (deco != null) {
        deco.init(this, decoratorLayer);
        decoAdded = true;
      }
    }
    if (!decoAdded)
      canvas.getRoot().removeChild(decoratorLayer);

    for (Object obj : displayData.objects()) {
      addObject(obj);
    }
  }

  /**
   * Sets the background color of this display.
   * 
   * @param color
   *          the new background color
   */
  public void setBackgroundColor(Color color) {
    canvas.setBackgroundColor(color);
  }

  /**
   * Gets the background color of this display.
   * 
   * @return the background color.
   */
  public Color getBackgroundColor() {
    return canvas.getBackgroundColor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.AbstractDisplay#moveObject(java.lang.Object)
   */
  @Override
  protected void moveObject(Object o) {
    layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.MOVED);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.AbstractDisplay#addObject(java.lang.Object)
   */
  @Override
  protected void addObject(Object o) {
    StyledDisplayLayerOGL2D layer = findLayer(o);
    if (layer != null) {
      Lock lock = canvas.getRenderLock();
      try {
        lock.lock();
        layer.addObject(o);
        layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.ADDED);
      } finally {
        lock.unlock();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.AbstractDisplay#removeObject(java.lang.Object
   * )
   */
  @Override
  protected void removeObject(Object o) {
    StyledDisplayLayerOGL2D layer = findLayer(o);
    if (layer != null) {
      Lock lock = canvas.getRenderLock();
      try {
        lock.lock();
        layer.removeObject(o);
        layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.REMOVED);
      } finally {
        lock.unlock();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#closed()
   */
  public void closed() {

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.IDisplay#createEditor(javax.swing.JPanel)
   */
  public DisplayEditorLifecycle createEditor(JPanel panel) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#deIconified()
   */
  public void deIconified() {
    iconified = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#iconified()
   */
  public void iconified() {
    iconified = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#resetHomeView()
   */
  public void resetHomeView() {
    canvas.resetCamera();
    canvas.centerScene();
    ThreadUtilities.runInEventThread(updater);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#getLayout()
   */
  public Layout<?, ?> getLayout() {
    return layout;
  }

  public JPanel getPanel() {
    if (panel == null) {
      createPanel();
    }

    return panel;
  }

  public void createPanel() {
    JPanel cpanel = canvas.getPanel();
    panel = new ImageablePanel(new BorderLayout());
    panel.add(cpanel, BorderLayout.CENTER);

    cpanel.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        if (glInitialized) {
          if (isVisible()) {
            // canvas.centerSceneKeepScale();
            canvas.update();
          }
        }
      }
    });

    cpanel.addPropertyChangeListener("ancestor", new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        Component child = canvas.getPanel();
        Component parent = child.getParent();
        while (parent != null) {
          if (parent instanceof JTabbedPane) {
            // if (tabParent != null)
            // tabParent.removeChangeListener(DisplayOGL2D.this);
            tabParent = (JTabbedPane) parent;
            // tabParent.addChangeListener(DisplayOGL2D.this);
            tabChild = child;
            return;
          }
          child = parent;
          parent = parent.getParent();
        }

        // if (tabParent != null)
        // tabParent.removeChangeListener(DisplayOGL2D.this);
        tabParent = null;
        tabChild = null;
      }
    });
  }

  public void stateChanged(ChangeEvent evt) {
    // System.out.println("tab state changed");
    // this moved to top tab
    if (tabParent.getSelectedComponent().equals(tabChild)) {
      // System.out.println("updating scene");
      canvas.centerScene();
      canvas.update();
    }
  }

  private boolean isVisible() {
    if (iconified)
      return false;
    if (tabParent != null)
      return tabParent.getSelectedComponent().equals(tabChild);

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.IDisplay#setLayout(repast.simphony.visualization
   * .Layout)
   */
  public void setLayout(Layout<?, ?> layout) {
    this.layout = layout;
    layoutUpdater.setLayout(layout);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.IDisplay#setLayoutFrequency(repast.simphony
   * .visualization.IDisplay.LayoutFrequency, int)
   */
  public void setLayoutFrequency(LayoutFrequency frequency, int interval) {
    if (frequency == LayoutFrequency.AT_UPDATE) {
      layoutUpdater = new UpdateLayoutUpdater(layout);

    } else if (frequency == LayoutFrequency.AT_INTERVAL) {
      layoutUpdater = new IntervalLayoutUpdater(layout, interval);

    } else if (frequency == LayoutFrequency.ON_NEW) {
      layoutUpdater = new AddedRemovedLayoutUpdater(layout);
    } else if (frequency == LayoutFrequency.ON_MOVE) {
      layoutUpdater = new MovedLayoutUpdater(layout);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.render.Renderer#setPause(boolean)
   */
  public void setPause(boolean pause) {
    if (pause) {
      update();
      render();
    }
    ThreadUtilities.runInEventThread(updater);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.IDisplay#update()
   */
  public void update() {
    if (glInitialized && !iconified) {
      layoutUpdater.update();
      try {
        canvas.getRenderLock().lock();

        for (ValueLayerDisplayLayer layer : valueLayerStyleMap.values()) {
          layer.update();
        }

        for (StyledDisplayLayerOGL2D layer : classStyleMap.values()) {
          layer.update(layoutUpdater);
        }

        for (NetworkLayerOGL2D layer : networkStyleMap.values()) {
          layer.update(layoutUpdater);
        }
        doRender = true;
      } finally {
        canvas.getRenderLock().unlock();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.render.Renderer#render()
   */
  public void render() {
    long ts = System.currentTimeMillis();
    if (doRender && !iconified) {
      if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL) {
        ThreadUtilities.runInEventThread(updater);
        lastRenderTS = ts;
      }
      doRender = false;
    }
  }

  public void registerDecorator(ProjectionDecorator2D<?> decorator) {
    decoratorMap.put(decorator.getProjection().getName(), decorator);
  }

  /**
   * Finds the layer associated with this object. This will return the layer
   * explicitly associated with the object. If that is not found then this will
   * return the first layer associated with the objects parent classes or
   * interface.
   * 
   * @param obj
   *          the object to find the layer for
   * 
   * @return the layer that best matches the object
   */
  protected StyledDisplayLayerOGL2D findLayer(Object obj) {
    Class<?> objClass = obj.getClass();
    StyledDisplayLayerOGL2D layer = classStyleMap.get(objClass);
    if (layer == null) {
      // find a parent class or interface
      for (Class<?> clazz : classStyleMap.keySet()) {
        if (clazz.isAssignableFrom(objClass)) {
          layer = classStyleMap.get(clazz);
          break;
        }
      }
    }
    return layer;
  }

  /**
   * Register the specified style for the specified class. All objects of that
   * class will be styled using that style.
   * 
   * @param clazz
   *          the class to register the style for
   * @param style
   *          the style to register
   */
  public void registerStyle(Class<?> clazz, StyleOGL2D<?> style) {
    style.init(canvas.getShapeFactory());
    StyledDisplayLayerOGL2D layer = classStyleMap.get(clazz);
    if (layer == null) {
      VLayer vLayer = new VLayer();
      canvas.getRoot().addChild(vLayer);
      layer = new StyledDisplayLayerOGL2D(style, vLayer);
      classStyleMap.put(clazz, layer);
    } else {
      layer.setStyle(style);
    }
  }

  /**
   * Registers the specified style for the specified network.
   * 
   * @param network
   *          the network
   * @param style
   */
  public void registerNetworkStyle(Network<?> network, EdgeStyleOGL2D style) {
    NetworkLayerOGL2D layer = networkStyleMap.get(network);
    if (layer == null) {
      VLayer vLayer = new VLayer();
      canvas.getRoot().addChild(vLayer);
      layer = new NetworkLayerOGL2D(network, style, vLayer, this);
      networkStyleMap.put(network, layer);
    } else {
      layer.setStyle(style);
    }
  }

  /**
   * Registers the specified style and layer for display.
   * 
   * @param style
   *          the styling info for displaying the layer
   * @param layer
   *          the layer to display
   */
  public void registerValueLayerStyle(ValueLayer layer, ValueLayerStyleOGL style) {
    ValueLayerDisplayLayer vdLayer = valueLayerStyleMap.get(layer);
    if (vdLayer == null) {
      VLayer vLayer = new VLayer();
      canvas.getRoot().addChild(vLayer);
      vdLayer = new ValueLayerDisplayLayer(layer, style, vLayer);
      valueLayerStyleMap.put(layer, vdLayer);
    } else {
      vdLayer.resetLayer(layer, style);
    }
  }

  /**
   * Gets the visual item that represents the specified object in the display.
   * This does NOT look for spatials representing RepastEdge objects.
   * 
   * @return the visual item that represents the specified object in the
   *         display, or null the VSpatial cannot be found.
   */
  VSpatial getSpatialForObject(Object obj) {
    StyledDisplayLayerOGL2D layer = findLayer(obj);
    return layer.getVisualItem(obj);
  }

  public void destroy() {
    super.destroy();
    canvas.dispose();
    for (Projection<?> proj : displayData.getProjections()) {
      proj.removeProjectionListener(this);
    }
    displayData = null;
  }

  // PickListener implementation
  /*
   * (non-Javadoc)
   * 
   * @see saf.v3d.picking.PickListener#pickPerformed(saf.v3d.picking.PickEvent)
   */
  public void pickPerformed(PickEvent evt) {
    List<Object> probedObjects = new ArrayList<Object>();
    for (VSpatial spatial : evt.getPicked()) {
      Object obj = spatial.getProperty(AbstractDisplayLayerOGL2D.MODEL_OBJECT_KEY);
      if (obj != null) {
        // check if is a value layer probe which needs additional processing
        if (spatial.getProperty(ValueLayerDisplayLayer.VALUE_LAYER_KEY) != null) {
          obj = ((ValueLayerDisplayLayer) spatial
              .getProperty(ValueLayerDisplayLayer.VALUE_LAYER_KEY)).getProbedObject();
        }
        probedObjects.add(obj);
      }
    }

    probeSupport.fireProbeEvent(this, probedObjects);
  }

  // CanvasListener implementation

  /*
   * (non-Javadoc)
   * 
   * @see saf.ui.v3d.CanvasListener#init(com.jogamp.opengl.GLAutoDrawable,
   * saf.ui.v3d.VNode)
   */
  public void init(GLAutoDrawable drawable, VRoot root) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see saf.ui.v3d.CanvasListener#reshape(com.jogamp.opengl.GLAutoDrawable,
   * float, float, saf.ui.v3d.VNode)
   */
  public void reshape(GLAutoDrawable drawable, float width, float height, VRoot root) {
    glInitialized = true;
    update();
    // System.out.printf("width: %f, height: %f%n", width, height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see saf.ui.v3d.CanvasListener#vSpatialMoved(saf.ui.v3d.VSpatial,
   * org.jogamp.vecmath.Point3f)
   */
  public void vSpatialMoved(VSpatial spatial, Point3f localTrans) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see saf.ui.v3d.CanvasListener#dispose(com.jogamp.opengl.GLAutoDrawable)
   */
  public void dispose(GLAutoDrawable drawable) {}

  @SuppressWarnings("serial")
  class ImageablePanel extends JPanel implements Imageable {

    public ImageablePanel(LayoutManager layout) {
      super(layout);
    }

    public BufferedImage getImage() {
      try {
        getRenderLock().lock();
        if (SystemUtils.IS_OS_WINDOWS) {
          return canvas.createImage();
        } else {
          BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
          Graphics2D g = bi.createGraphics();
          paint(g);
          g.dispose();
          return bi;
        }
      } finally {
        getRenderLock().unlock();
      }
    }
  }

  public Canvas2D getCanvas(){
  	return canvas;
  }
}
