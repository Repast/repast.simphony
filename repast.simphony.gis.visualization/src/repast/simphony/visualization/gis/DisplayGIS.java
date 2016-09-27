package repast.simphony.visualization.gis;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.piccolo2d.PCamera;
import org.piccolo2d.util.PBounds;

import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.gis.display.PiccoloMapPanel;
import repast.simphony.gis.tools.DistanceSetter;
import repast.simphony.gis.tools.DistanceTool;
import repast.simphony.gis.tools.LocationSetter;
import repast.simphony.gis.tools.PGISPanTool;
import repast.simphony.gis.tools.PMarqueeZoomIn;
import repast.simphony.gis.tools.PMarqueeZoomOut;
import repast.simphony.gis.tools.PositionTool;
import repast.simphony.gis.tools.ToolManager;
import repast.simphony.gis.ui.probe.GISProbeHandler;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.ui.RSGUIConstants;
import repast.simphony.visualization.AbstractDisplay;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.DisplayEvent;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.ProbeEvent;
import repast.simphony.visualization.editor.gis.SelectionDecorator;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Standard GIS Display
 * 
 * @author Nick Collier
 * @author Eric Tatara
 * 
 */
public class DisplayGIS extends AbstractDisplay implements WindowListener {

	// The 2D GIS needs a slower frame rate since it can't update as fast as the
	//   JOGL-based displays.
	// ~10 frames a second
	public static long GIS_FRAME_UPDATE_INTERVAL = 100;
	
	// Used to help differentiate background shapefile layers from agent layers
	public static final String BACKGROUND_LAYER_PREFIX = "backgroud.layer.";
	
	private final static String ICON_FORMAT = ".png";
  public static final String SHP_FILE_STYLE_PROP = DisplayGIS.class + ".SHP_FILE_STYLE_PROP";
  private static final MessageCenter msg = MessageCenter.getMessageCenter(DisplayGIS.class);

  protected PiccoloMapPanel panel;
  private boolean addWindowListener = true;
  protected DisplayData<?> initData;
  private boolean iconified = false;
  protected Object lock = new Object();
  private MapContent mapContext;
  private Updater updater;
  private Styler styler = new Styler();
  
  // Static map feature collections such as background map layers
  private Map<String,FeatureCollection> staticMapFeatures = 
  		new HashMap<String,FeatureCollection>();
  
  // The ordering of map layers
  private Map<Integer, String> layerOrder = new HashMap<Integer, String>();
  
  private java.util.List<String> classNames = new ArrayList<String>();
  private Geography geog;
  private boolean doRender = true;
  private Runnable myRenderer;
  private Runnable myUpdater;

  private SelectionDecorator decorator;

  public DisplayGIS(DisplayData<?> data) {
    this.initData = data;
    
    // uncomment to print piccolo debug info to console
//	PDebug.debugPaintCalls = true;
//	PDebug.debugThreads = true;
  }

  /**
   * Gets the geography this display displays.
   * 
   * @return the geography this display displays.
   */
  public Geography getGeography() {
    if (geog != null)
      return geog;
    // should only be a single geog
    for (Projection proj : initData.getProjections()) {
      if (proj instanceof Geography)
        return (Geography) proj;
    }
    return null;
  }

  @Override
  /**
   * Here we override the event method to handle network events, since the GIS
   * displays don't have network layers.  Added/Removed edges should have a
   * corresponding geometry in the geography (probably added in the same 
   * procedure where the edges are added to the network).
   */
  public void projectionEventOccurred(ProjectionEvent evt) {
    if (evt.getType() == ProjectionEvent.OBJECT_ADDED
        || evt.getType() == ProjectionEvent.EDGE_ADDED) {
      Object obj = evt.getSubject();
      addObject(obj);
    } else if (evt.getType() == ProjectionEvent.OBJECT_REMOVED
        || evt.getType() == ProjectionEvent.EDGE_REMOVED) {
      Object obj = evt.getSubject();
      removeObject(obj);
    } else if (evt.getType() == ProjectionEvent.OBJECT_MOVED) {
      Object obj = evt.getSubject();
      moveObject(obj);
    }
  }

  /**
   * Registers a style for the specified agent.
   * 
   * @param agentName
   *          the name of the agent
   * @param style
   *          the style
   * @param order
   *          the layer order
   */
  public void registerAgentStyle(String agentName, Style style, Integer order) {
    
  	if (style != null && order != null){
  		classNames.add(agentName);
  		styler.registerStyle(agentName, style);
  		layerOrder.put(order, agentName);
  	}
  }

  /**
   * Gets a list of the agent classes registered with this display.
   * 
   * @return a list of the agent classes registered with this display.
   */
  public java.util.List<Class> getRegisteredClasses() {
    java.util.List<Class> list = new ArrayList<Class>();
    try {
      for (String name : classNames) {
        list.add(Class.forName(name));
      }
    } catch (ClassNotFoundException e) {
      msg.warn("Error while creating classes from agent names", e);
    }
    return list;
  }

  /**
   * Registers a style for the specified feature source.  Used for adding 
   *   static background layers from a shapefile.
   * 
   * @param source the feature source
   * @param style the style
   * @param order the layer order
   */
  public void registerFeatureSource(FeatureSource source, Style style, Integer order) {
  	FeatureCollection collection = null;
		try {
			collection = source.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
		}
  	
		String layerName = BACKGROUND_LAYER_PREFIX + source.getName().toString();
		
		if (collection != null){
			staticMapFeatures.put(layerName,collection);
			styler.registerStyle(layerName, style);
			layerOrder.put(order, layerName);	
		}
  }

  @Override
  protected void addObject(Object o) {
    updater.agentAdded(o);
  }

  @Override
  protected void moveObject(Object o) {
    updater.agentMoved(o);
  }

  @Override
  protected void removeObject(Object o) {
    updater.agentRemoved(o);
  }

  @Override
  public void destroy() {
    super.destroy();
    for (Projection proj : initData.getProjections()) {
      proj.removeProjectionListener(this);
    }
    initData = null;
    Window window = SwingUtilities.getWindowAncestor(panel);
    if (window != null)
      window.removeWindowListener(this);
  }

  public Layout getLayout() {
    return null;
  }

  /**
   * Initializes the display. Called once before the display is made visible.
   */
  @Override
  public void init() {
    for (Projection proj : initData.getProjections()) {
      if (proj instanceof Geography)
        geog = (Geography) proj;

      // add network listeners to the display, unlike with other displays which
      // have network layers that do the listening.
      else if (proj instanceof Network)
        proj.addProjectionListener(this);
    }
    mapContext = new MapContent();
    mapContext.getViewport().setBounds(new ReferencedEnvelope(
    		new Envelope(-90, -90, -90, 90), geog.getCRS()));
    
    // TODO Geotools: Look at how mapContext.getViewport().setCoordinateReferenceSystem(arg0);
    //        might be used to set the display based on the layer CRS.  The viewport
    //        bounds are also set in several other classes eg PGISCanvas, etc.
    
    geog.addProjectionListener(this);
    decorator = new SelectionDecorator(mapContext);
    
    for (Class clazz : getRegisteredClasses()) {
      decorator.initClass(clazz);
    }
    
    updater = new Updater(mapContext, geog, styler, staticMapFeatures, 
    		getRegisteredClasses(), layerOrder);
    
    myRenderer = new MyRenderer();
    myUpdater = new MyUpdater();

    update();
    forceRender();
  }

  public void probe(Envelope env) {
    Geometry gEnv = new GeometryFactory().toGeometry(env);
    java.util.List<Object> objs = new ArrayList<Object>();
    for (Object obj : geog.queryInexact(env)) {
      Geometry geom = geog.getGeometry(obj);
      if (geom.intersects(gEnv))
        objs.add(obj);
    }
    if (objs.size() > 0)
      probeSupport.fireProbeEvent(this, objs, ProbeEvent.Type.REGION);
  }

  public void setLayout(Layout layout) {
  }

  public void setLayoutFrequency(LayoutFrequency frequency, int interval) {
  }

  public void update() {
    synchronized (lock) {
      doRender = true;
      ThreadUtilities.runInEventThread(myUpdater);
    }
  }

  public void render() {
    long ts = System.currentTimeMillis();
    if (doRender && panel.getCanvas().isShowing()) {
      synchronized (lock) {
        if (ts - lastRenderTS > GIS_FRAME_UPDATE_INTERVAL) {
        ThreadUtilities.runInEventThread(myRenderer);
        lastRenderTS = ts;
        }
      }
    }
  }

  /**
   * Need to render displays when the sim is paused and initialized, because the
   * display won't render with DisplayGIS.render() if it is hidden.
   */
  public void forceRender() {
    synchronized (lock) {
      ThreadUtilities.runInEventThread(myRenderer);
    }
  }

  private class MyUpdater implements Runnable {
    public void run() {
      updater.update();
    }
  }

  private class MyRenderer implements Runnable {
    public void run() {
      updater.render(mapContext);
      doRender = false; // reset the render flag
    }
  }

  /**
   * Executes when simulation is paused.
   */
  public void setPause(boolean pause) {
  	if (pause) {
			update();
			forceRender();
		}
  }

  // we calculate our own layer bounds
  // because mapContext.getLayerBounds throws an NPE
  // if a layer is empty.
  private ReferencedEnvelope getLayerBounds() {
    ReferencedEnvelope result = null;
    CoordinateReferenceSystem crs = mapContext.getCoordinateReferenceSystem();
    try {
      for (Layer layer : mapContext.layers()) {

        FeatureSource fs = layer.getFeatureSource();
        if (!fs.getFeatures().isEmpty()) {
          CoordinateReferenceSystem sourceCrs = fs.getSchema().getCoordinateReferenceSystem();
          ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(), sourceCrs);

          if ((sourceCrs != null) && (crs != null)
              && !CRS.equalsIgnoreMetadata(sourceCrs, crs)) {
            env = env.transform(crs, true);
          }

          if (result == null) {
            result = env;
          } else {
            result.expandToInclude(env);
          }
        }
      }
    } catch (Exception ex) {
      msg.warn("Unable to fully calculate map layer bounds in display", ex);
    }

    return result;
  }

  public void resetHomeView() {

    ReferencedEnvelope aoe = getLayerBounds();

    if (aoe != null) {
      if (aoe.getWidth() == 0) {
        aoe.expandBy(.001, 0);
      }

      if (aoe.getHeight() == 0) {
        aoe.expandBy(0, .001);
      }

      if (aoe.getCoordinateReferenceSystem() == null)
        return;

      mapContext.getViewport().setBounds(aoe);
      PGISCanvas canvas = panel.getCanvas();
      PBounds bounds = canvas.getCamera().getViewBounds();
      Envelope env = new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(),
          bounds.getMaxY());
      mapContext.getViewport().setBounds(new ReferencedEnvelope(env, canvas.getCRS()));
    }
  }

  public void windowActivated(WindowEvent e) {
  }

  public void windowClosed(WindowEvent e) {
  }

  public void windowClosing(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }

  public void windowDeiconified(WindowEvent e) {
    iconified = false;
  }

  public void windowIconified(WindowEvent e) {
    iconified = true;
  }

  public void windowOpened(WindowEvent e) {
  }

  public void closed() {
  }

  public void deIconified() {
  }

  private double mousePositionX;
  private double mousePositionY;

  /**
   * Zoom control with mouse wheel
   * 
   * @author tatara
   */
  public class RepastPiccoloMouseWheelListener implements MouseWheelListener {
    private double minScale = 0;
    private double maxScale = Double.MAX_VALUE;

    public void mouseWheelMoved(MouseWheelEvent e) {
      PGISCanvas canvas = panel.getCanvas();
      PCamera camera = canvas.getCamera();

      double dx = -e.getWheelRotation();

      double scaleDelta = (1.0 + (0.1 * dx));

      double currentScale = camera.getViewScale();
      double newScale = currentScale * scaleDelta;

      if (newScale < minScale)
        scaleDelta = minScale / currentScale;

      if ((maxScale > 0) && (newScale > maxScale))
        scaleDelta = maxScale / currentScale;

      Point2D point = camera.localToView(new Point2D.Double(mousePositionX, mousePositionY));

      camera.scaleViewAboutPoint(scaleDelta, point.getX(), point.getY());
      PBounds viewBounds = camera.getViewBounds();
      canvas.setAreaOfInterest(new ReferencedEnvelope(viewBounds.getMinX(), viewBounds.getMaxX(),
          viewBounds.getMinY(), viewBounds.getMaxY(), canvas.getCRS()));
    }
  }

  /**
   * Assists mouse wheel zoom control by updating the center coord
   * 
   * @author tatara
   */
  public class RepastPiccoloMouseMotionListener implements MouseMotionListener {

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
      mousePositionX = e.getX();
      mousePositionY = e.getY();
    }
  }

  /**
   * Gets the data used to initialize this display.
   * 
   * @return the data used to initialize this display.
   */
  public DisplayData getInitData() {
    return initData;
  }

  /**
   * Gets the style for the specified agent type.
   * 
   * @param agentClassName
   *          the type whose style we want
   * @return the style for the specified agent type.
   */
  public Style getStyleFor(String agentClassName) {
    return styler.getStyle(agentClassName, null);
  }

  /**
   * Creates an DisplayEditor appropriate for editing this display.
   * 
   * @param panel
   * @return an DisplayEditor appropriate for editing this display or null if
   *         this display cannot be edited.
   */
  public DisplayEditorLifecycle createEditor(JPanel panel) {
    // return EditorFactory.getInstance().createGISEditor(this,
    // this.panel.getCanvas(), panel);
    return null;
  }

  /**
   * Gets a panel that contains the actual gui for visualization.
   * 
   * @return a panel that contains the actual gui for visualization.
   */
  public JPanel getPanel() {
    if (panel == null) {
      createPanel();
      resetHomeView();
    }
    return panel;
  }

  /**
   * Registers the specified toolbar with this IDisplay. This IDisplay can then
   * put buttons etc. are on this toolbar.
   * 
   * @param bar
   *          the bar to register
   */
  @Override
  public void registerToolBar(JToolBar bar) {
    bar.addSeparator();
    getPanel();
    // add the toolbar components from the gis toolbar
    // to the simphony toolbar. We can't add them directly
    // because adding them removes them from the pBar and
    // that screws up the component count.
    JToolBar pBar = panel.getToolBar();
    Component[] comps = new Component[pBar.getComponentCount()];
    for (int i = 0, n = pBar.getComponentCount(); i < n; i++) {
      comps[i] = pBar.getComponentAtIndex(i);
    }
    pBar.removeAll();

    Dimension dim = comps[0].getPreferredSize();
    for (Component comp : comps) {
      if (!comp.getPreferredSize().equals(dim)) {
        comp.setPreferredSize(dim);
        comp.setMaximumSize(dim);
      }
      bar.add(comp);
    }
  }

  public void iconified() {
  }

  public ToolManager getToolManager() {
    return panel.getToolManager();
  }

  /**
   * Gets the decorator used to decorate selected gis features.
   * 
   * @return the decorator used to decorate selected gis features.
   */
  public SelectionDecorator getDecorator() {
    return decorator;
  }

  public static Icon loadIcon(String name) {
    try {
      return new ImageIcon(RSGUIConstants.class.getClassLoader().getResource(name + ICON_FORMAT));
    } catch (Exception e) {
      msg.warn("Error loading: " + name + ", it will not be used.");
      return new ImageIcon(new byte[0]);
    }
  }
  
  public void createPanel() {
    panel = new PiccoloMapPanel(mapContext);

    // zoomIn
    Map<String, Object> toolParams = new HashMap<String, Object>();
    toolParams.put(ToolManager.TOGGLE, true);
    toolParams.put(Action.SMALL_ICON, loadIcon("mActionZoomIn"));
    toolParams.put(Action.SHORT_DESCRIPTION, "Zoom In");
    panel.addTool(new PMarqueeZoomIn(mapContext), toolParams);

    // zoomOut
    toolParams = new HashMap<String, Object>();
    toolParams.put(ToolManager.TOGGLE, true);
    toolParams.put(Action.SMALL_ICON, loadIcon("mActionZoomOut"));
    toolParams.put(Action.SHORT_DESCRIPTION, "Zoom Out");
    panel.addTool(new PMarqueeZoomOut(mapContext), toolParams);

    // pan
    PGISCanvas canvas = panel.getCanvas();
    toolParams = new HashMap<String, Object>();
    toolParams.put(Action.SHORT_DESCRIPTION, "Pan the map");
    toolParams.put(ToolManager.TOGGLE, true);
    toolParams.put(Action.SMALL_ICON, loadIcon("mActionPan"));
    toolParams.put("DEFAULT", Boolean.TRUE);
    toolParams.put(ToolManager.SELECTED, Boolean.TRUE);
    panel.addTool(new PGISPanTool(mapContext, canvas), toolParams);

    // ruler
    toolParams = new HashMap<String, Object>();
    toolParams.put(ToolManager.TOGGLE, true);
    toolParams.put(Action.SMALL_ICON, loadIcon("ruler"));
    toolParams.put(Action.SHORT_DESCRIPTION, "Calculate Distance between 2 points");

    DistanceSetter setter = new DistanceSetter() {
      DecimalFormat format = new DecimalFormat("###.####");

      public void setDistance(double distance, Unit units) {
        DisplayEvent evt = new DisplayEvent(DisplayGIS.this, format.format(distance) + " " + units);
        evt.addProperty(DisplayEvent.TYPE, "distance");
        dlSupport.fireInfoMessage(evt);
      }

      public void clearDistance() {
        DisplayEvent evt = new DisplayEvent(DisplayGIS.this, "");
        evt.addProperty(DisplayEvent.TYPE, "distance");
        dlSupport.fireInfoMessage(evt);
      }
    };
    panel.addTool(new DistanceTool(mapContext, SI.METER, setter), toolParams);

    toolParams = new HashMap<String, Object>();
    toolParams.put(ToolManager.TOGGLE, true);
    toolParams.put(Action.SMALL_ICON, loadIcon("mActionIdentify"));
    toolParams.put(Action.SHORT_DESCRIPTION, "Probe");
    panel.addTool(new GISProbeHandler(this), toolParams);

    LocationSetter locationSetter = new LocationSetter() {
      DecimalFormat format = new DecimalFormat("###.####");

      public void setLocation(double lon, double lat) {
        Coordinate coordinate = new Coordinate(lon, lat);
        dlSupport.fireInfoMessage(DisplayGIS.this,
            format.format(coordinate.x) + ", " + format.format(coordinate.y));
      }

      public void unsetLocation() {
        dlSupport.fireInfoMessage(DisplayGIS.this, "");
      }
    };

    panel.getCanvas().addInputEventListener(
        new PositionTool(mapContext.getCoordinateReferenceSystem(), locationSetter));

    panel.addHierarchyListener(new HierarchyListener() {
      public void hierarchyChanged(HierarchyEvent e) {
        if (e.getChangeFlags() == HierarchyEvent.DISPLAYABILITY_CHANGED && addWindowListener) {
          Window window = SwingUtilities.getWindowAncestor(panel);
          window.addWindowListener(DisplayGIS.this);
          addWindowListener = false;
        }
      }
    });

    // add mouse wheel zoom control
    panel.getCanvas().addMouseWheelListener(new RepastPiccoloMouseWheelListener());
    panel.getCanvas().addMouseMotionListener(new RepastPiccoloMouseMotionListener());
  }

  public void toggleInfoProbe() {
    // TODO Auto-generated method stub

  }
}