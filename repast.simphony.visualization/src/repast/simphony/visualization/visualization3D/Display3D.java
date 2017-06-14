package repast.simphony.visualization.visualization3D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Window;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.PickInfo;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.IDisplayLayer;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.editor.EditorFactory;
import repast.simphony.visualization.network.NetworkDisplayLayer3D;
import repast.simphony.visualization.visualization3D.style.DefaultEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.DefaultStyle3D;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * 3D Display class based on J3D. Uses a behavior to update the display.
 * 
 * @author Nick Collier
 */
public class Display3D extends AbstractDisplay3D {

  public static final int TRANSFORMS_APPLIED = 1;

  protected Style3D defaultStyle = new DefaultStyle3D();
  protected EdgeStyle3D defaultEdgeStyle = new DefaultEdgeStyle3D();
  protected boolean firstRender = true;
  protected Color backgroundColor = null;
  protected PickCanvas pick;

  static {
    SimpleUniverse.setJ3DThreadPriority(1);
  }

  protected JCanvas3D canvas;
  protected BranchGroup sceneRoot;
  protected SimpleUniverse universe;
  protected MainBehavior updater;

  protected TransformGroup topRotTransGroup;
  protected TransformGroup projectionTransGroup;
  protected TransformGroup valueLayerTransGroup;
  // tracks whether or not the last call to render
  // actually triggered an update of the scene or not.
  // an update won't be triggered if the canvas is
  // not visible
  protected boolean updatedLastRender = false;

  public Display3D(DisplayData<?> data, Layout layout) {
    super(data, layout);

    initScene();
  }

  private void initScene() {
    sceneRoot = new BranchGroup();
    sceneRoot.setCapability(BranchGroup.ALLOW_DETACH);

    topRotTransGroup = new TransformGroup();
    topRotTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    topRotTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    topRotTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
    sceneRoot.addChild(topRotTransGroup);

    projectionTransGroup = new TransformGroup();
    projectionTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    projectionTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    projectionTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
    projectionTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
    projectionTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
    topRotTransGroup.addChild(projectionTransGroup);

    valueLayerTransGroup = new TransformGroup();
    valueLayerTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    valueLayerTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    valueLayerTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
    valueLayerTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
    valueLayerTransGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
    topRotTransGroup.addChild(valueLayerTransGroup);

    // add a top level grid if there is a grid projection
    boolean tilt = false;

    if (!tilt) {
      // tilt because we have a real value layer and want
      tilt = !(valueLayer instanceof DummyValueDisplayLayer);
    }

    if (tilt) {
      // we have grid or a value layer and so we can tilt the display a bit.
      Transform3D trans = new Transform3D();
      trans.rotX(Math.toRadians(25));
      topRotTransGroup.setTransform(trans);
    }
  }

  /**
   * Called the first time the scene is rendered. We need to create the canvas
   * here because the GraphicsConfiguration must relate to the monitor on which
   * the runtime window is located, and not the default monitor, otherwise an
   * exception may be thrown.
   */
  private void initFirstRender() {
    Window window = SwingUtilities.getWindowAncestor(panel);
    GraphicsDevice device = window.getGraphicsConfiguration().getDevice();
    GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();

    canvas = new JCanvas3D(template, device);
    canvas.setResizeMode(JCanvas3D.RESIZE_IMMEDIATELY);
    canvas.setPreferredSize(new Dimension(100, 100));
    canvas.setSize(canvas.getPreferredSize());
    canvas.addMouseListener(new MouseAdapter() {

    });

    panel.add(canvas, BorderLayout.CENTER);

    universe = new SimpleUniverse(canvas.getOffscreenCanvas3D());
    universe.getViewingPlatform().setNominalViewingTransform();

    if (backgroundColor != null) {
      float r = (float) backgroundColor.getRed() / 255f;
      float g = (float) backgroundColor.getGreen() / 255f;
      float b = (float) backgroundColor.getBlue() / 255f;

      // TODO this can be used for background image
      // String textureFile = "../repast.simphony.demos/icons/stars3.jpg";
      // TextureLoader loader = new TextureLoader(textureFile, "RGB", new
      // Container());
      // Background background = new Background(loader.getImage());
      // background.setImageScaleMode(Background.SCALE_FIT_ALL);

      Background background = new Background(r, g, b);

      BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 1000000);
      background.setApplicationBounds(sphere);
      sceneRoot.addChild(background);
    }

    View view = canvas.getOffscreenCanvas3D().getView();
    // this will "smooth" out the animation but make it slower
    // view.setMinimumFrameCycleTime(20);
    // view.setSceneAntialiasingEnable(true);
    view.setBackClipDistance(3000.0d);
    view.setFrontClipDistance(0.01d);

    panel.add(canvas, BorderLayout.CENTER);

    initAdditional();

    sceneRoot.compile();
    universe.getLocale().addBranchGraph(sceneRoot);
    // by setting pause we disable
    // the behavior thread so we don't max the
    // cpu.
    setPause(true);
  }

  /**
   * Initialization additional components dependent on the canvas is necessary
   * during first render, because that's when the canvas is created. Was
   * previously in initScene().
   */
  private void initAdditional() {
    BoundingSphere bounds = new BoundingSphere(new Point3d(), Integer.MAX_VALUE);
    // rotate
    MouseRotate myMouseRotate = new MouseRotate(canvas);
    myMouseRotate.setTransformGroup(topRotTransGroup);
    myMouseRotate.setSchedulingBounds(bounds);
    myMouseRotate.setFactor(0.01); // sensitivity
    sceneRoot.addChild(myMouseRotate);

    // View Translation
    TransformGroup vpTrans = universe.getViewingPlatform().getViewPlatformTransform();
    MouseTranslate myMouseTranslate = new MouseTranslate(canvas, MouseBehavior.INVERT_INPUT);
    myMouseTranslate.setTransformGroup(vpTrans);
    myMouseTranslate.setSchedulingBounds(bounds);
    sceneRoot.addChild(myMouseTranslate);

    // View zoom
    // uses middle button
    MouseZoom myMouseZoom = new MouseZoom(canvas);
    myMouseZoom.setTransformGroup(vpTrans);
    myMouseZoom.setSchedulingBounds(bounds);
    sceneRoot.addChild(myMouseZoom);

    // View zoom alternative
    // uses mouse wheel
    MouseWheelZoom myMouseWheelZoom = new MouseWheelZoom(canvas);
    myMouseWheelZoom.setTransformGroup(vpTrans);
    myMouseWheelZoom.setSchedulingBounds(bounds);
    sceneRoot.addChild(myMouseWheelZoom);

    pick = new PickCanvas(canvas.getOffscreenCanvas3D(), sceneRoot);
    pick.setMode(PickInfo.PICK_GEOMETRY);
    pick.setFlags(PickInfo.NODE | PickInfo.CLOSEST_INTERSECTION_POINT);
    pick.setTolerance(4f);
    canvas.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent event) {
        int clicks = event.getClickCount();
        if (clicks == 2 && SwingUtilities.isLeftMouseButton(event)) {
          picked(event);
        }
      }
    });

    // directional lighting
    DirectionalLight light = new DirectionalLight();
    light.setDirection(-1f, -1f, -2f);
    light.setInfluencingBounds(bounds);
    sceneRoot.addChild(light);

    // ambient lighting */
    AmbientLight ambient = new AmbientLight();
    ambient.setInfluencingBounds(bounds);
    sceneRoot.addChild(ambient);

    // fog -- todo fog looks nice but
    // we need to set the back distance w/r to
    // the size of the scene.
    // LinearFog fog = new LinearFog(0f, 0f, 0f);
    // fog.setFrontDistance(2f);
    // fog.setBackDistance(4f);
    // fog.setInfluencingBounds(bounds);
    // sceneRoot.addChild(fog);

    // todo we need to change this from BoundingSphere so that it will
    // always update regardless of what the activation volume contains
    updater = new MainBehavior();
    updater.setSchedulingBounds(bounds);
    // add the trigger update to root
    sceneRoot.addChild(updater);
  }

  private void picked(MouseEvent evt) {
    pick.setShapeLocation(evt);

    PickInfo pickResult = pick.pickClosest();
    // PickResult[] pickResults = pickCanvas.pickAll();
    if (pickResult != null) {
      // for (PickResult pickResult : pickResults) {
      Shape3D shape = (Shape3D) pickResult.getNode();
      if (shape != null) {
        probe(shape, pickResult.getClosestIntersectionPoint());

        //shape.setAppearance(AppearanceFactory.setPolygonAppearance(shape.getAppearance(),
        //AppearanceFactory.PolygonDraw.LINE));
      }
    }
  }

  public void init() {
    super.init();
    if (valueLayer == null)
      valueLayer = new DummyValueDisplayLayer();
    else if (valueLayer instanceof DefaultValueDisplayLayer3D) {

      ((DefaultValueDisplayLayer3D) valueLayer).registerGrid(projectionTransGroup);
    }
    valueLayer.init(updater);

    for (Projection proj : initData.getProjections()) {
      ProjectionDecorator3D decorator = decoratorMap.get(proj.getName());
      if (decorator != null) {
        decorator.init(this, projectionTransGroup);
      }
    }

    update();
    valueLayer.applyUpdates();

    for (IDisplayLayer display : displayMap.values()) {
      display.applyUpdates();
    }

    for (IDisplayLayer display : networkMap.values()) {
      display.applyUpdates();
    }

    for (ProjectionDecorator3D deco : decoratorMap.values()) {
      deco.update();
    }

    // center the viz around 0,0,0
    BoundingSphere bounds = (BoundingSphere) projectionTransGroup.getBounds();
    Point3d center = new Point3d();
    bounds.getCenter(center);
    center.negate();
    Transform3D trans = new Transform3D();
    trans.set(new Vector3d(center));
    projectionTransGroup.setTransform(trans);

    /*
     * // pull the viewing platform back a bit. TransformGroup group =
     * universe.getViewingPlatform().getViewPlatformTransform(); trans = new
     * Transform3D(); trans.lookAt(new Point3d(0, 0, bounds.getRadius() * 2.5),
     * new Point3d(0, 0, 0), new Vector3d(0, 1, 0)); trans.invert();
     * group.setTransform(trans);
     */

  }

  public JCanvas3D getCanvas() {
    return canvas;
  }

  public BranchGroup getSceneRoot() {
    return sceneRoot;
  }

  /**
   * Given the shape3D, probe the object that it represents.
   * 
   * @param shape
   *          the shape representing the object we want to probe
   * @param intersectPoint
   *          the intersection point of the pick itself
   */
  public void probe(Shape3D shape, Point3d intersectPoint) {
    Object obj = null;
    for (IDisplayLayer3D layer : displayMap.values()) {
      IDisplayLayer3D layer3D = (IDisplayLayer3D) layer;
      obj = layer3D.findObjsForItem(shape);
      if (obj != null)
        break;
    }

    if (obj == null) {
      for (IDisplayLayer layer : networkMap.values()) {
        NetworkDisplayLayer3D layer3D = (NetworkDisplayLayer3D) layer;
        obj = layer3D.findObjsForItem(shape);
        if (obj != null)
          break;
      }
    }

    if (obj == null && valueLayer instanceof DefaultValueDisplayLayer3D) {
      obj = ((DefaultValueDisplayLayer3D) valueLayer).findObjForShape(shape, intersectPoint);
    }

    if (obj != null) {
      List<Object> list = new ArrayList<Object>();
      list.add(obj);
      probeSupport.fireProbeEvent(this, list);
    }
  }

  public VisualItem3D getVisualObject(Object obj) {
    IDisplayLayer<VisualItem3D> layer = findLayer(obj);
    return layer.getVisualItem(obj);
  }

  protected AbstractDisplayLayer3D createDisplayLayer(Style3D style) {
    if (style == null)
      return new DisplayLayer3D(defaultStyle, projectionTransGroup);
    return new DisplayLayer3D(style, projectionTransGroup);
  }

  public AbstractDisplayLayer3D createEdgeLayer(EdgeStyle3D style, Network network) {
    NetworkDisplayLayer3D layer;
    if (style == null)
      layer = new NetworkDisplayLayer3D(network, defaultEdgeStyle, projectionTransGroup, this);
    else
      layer = new NetworkDisplayLayer3D(network, style, projectionTransGroup, this);
    layer.setNetworkDirected(network.isDirected());
    return layer;
  }

  public ValueDisplayLayer3D createValueLayerDisplayLayer(ValueLayerStyle3D style) {
    return new DefaultValueDisplayLayer3D(style, valueLayerTransGroup, this);
  }

  public JPanel getPanel() {
    if (panel == null) {
      createPanel();
      panel.setLayout(new BorderLayout());
      // panel.add(canvas, BorderLayout.CENTER);
    }

    return panel;
  }

  // this applies the updates by setting the trigger condition to true
  // That forces Updater.doOnTrigger to be called and that tells all the
  // displays
  // to apply their updates.
  public synchronized void render() {
    if (firstRender) {
      initFirstRender();
      // updatedLastRender = true;
      updater.setTriggered(true);
      lastRenderTS = System.currentTimeMillis();
      firstRender = false;
    } else if (iconified || !canvas.isShowing()) {
      updatedLastRender = false;
    } else {
      long ts = System.currentTimeMillis();
      if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL) {
        updater.setTriggered(true);
        lastRenderTS = ts;
      }
    }
  }

  public void setPause(boolean pause) {
    if (!updatedLastRender && pause) {
      updater.setPaused(true);
      updater.setTriggered(true);
    } else {
      updater.setPaused(false);
      updater.setEnable(!pause);
    }
  }

  /*
   * public synchronized void frameFinished() { // we need to count frames like
   * this because some kinds of changes take // longer than others but
   * everything should be completed within two frames if
   * (behaviorTracker.isRenderFinished() && frameCount == 1) { // we only want
   * to call fireRenderFinished if we have actually started a render // and if
   * that render is finished. The BehaviorTracker takes care of handling that
   * behaviorTracker.reset(); support.fireRenderFinished(this); } }
   */

  /**
   * Notifies this IDisplay that its associated gui widget has been closed.
   */
  public void closed() {

  }

  /**
   * Notifies this IDisplay that its associated gui widget has been iconified.
   */
  public void iconified() {
    iconified = true;
    // fire this because the renderer may be waiting for this
    // when iconified is called.
  }

  /**
   * Notifies this IDisplay that its associated gui widget has been deIconified.
   */
  public void deIconified() {
    iconified = false;
  }

  public void destroy() {
    super.destroy();

    for (Projection proj : initData.getProjections()) {
      proj.removeProjectionListener(this);
    }
    initData = null;
    Window window = SwingUtilities.getWindowAncestor(panel);
    if (window != null)
      window.removeWindowListener(this);
    canvas.getOffscreenCanvas3D().stopRenderer();
    panel.remove(canvas);
    for (HierarchyListener listener : panel.getHierarchyListeners()) {
      panel.removeHierarchyListener(listener);
    }
    panel = null;
    universe.getLocale().removeBranchGraph(sceneRoot);
    universe.removeAllLocales();
    universe.cleanup();
    EditorFactory.getInstance().reset();
    probeSupport = null;
    dlSupport = null;
  }

  class MainBehavior extends Behavior {

    private boolean triggered = false;
    private boolean paused = false;
    // private BehaviorTracker behaviorTracker;
    private WakeupCondition wakeup;

    public MainBehavior() {
      // this.behaviorTracker = counter;
      wakeup = new WakeupOnElapsedFrames(0);
    }

    public boolean isTriggered() {
      return triggered;
    }

    public void setTriggered(boolean triggered) {
      this.triggered = triggered;
    }

    public void setPaused(boolean val) {
      paused = val;
    }

    public void initialize() {
      this.wakeupOn(wakeup);
    }

    public void processStimulus(Enumeration criteria) {
      if (triggered) {

        for (IDisplayLayer display : displayMap.values()) {
          display.applyUpdates();
        }

        for (IDisplayLayer display : networkMap.values()) {
          display.applyUpdates();
        }

        for (ProjectionDecorator3D deco : decoratorMap.values()) {
          deco.update();
        }

        valueLayer.applyUpdates();

        postId(Display3D.TRANSFORMS_APPLIED);

        triggered = false;
        if (paused) {
          paused = false;
          setEnable(false);
        }
      }

      this.wakeupOn(wakeup);
    }
  }

  public void windowDeiconified(WindowEvent e) {
    super.windowDeiconified(e);
    // workaround for ATI driver bug
    panel.remove(canvas);
    panel.add(canvas, BorderLayout.CENTER);
  }

  public Layout getLayout() {
    return layout;
  }

  /**
   * Creates an DisplayEditor appropriate for editing this display.
   * 
   * @param panel
   * @return an DisplayEditor appropriate for editing this display or null if
   *         this display cannot be edited.
   */
  public DisplayEditorLifecycle createEditor(JPanel panel) {
    return EditorFactory.getInstance().create3DEditor(this, panel);
  }

  /**
   * Resets the view to the original zoom and camera position
   */
  public void resetHomeView() {
    Transform3D trans = new Transform3D();
    trans.rotX(Math.toRadians(25));
    topRotTransGroup.setTransform(trans);

    universe.getViewingPlatform().setNominalViewingTransform();
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void toggleInfoProbe() {
    // TODO Auto-generated method stub

  }
}