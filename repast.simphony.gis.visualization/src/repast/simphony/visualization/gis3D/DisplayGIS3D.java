package repast.simphony.visualization.gis3D;

import static repast.simphony.ui.RSGUIConstants.CAMERA_ICON;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.media.jai.PlanarImage;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.ReferencedEnvelope;

import com.jogamp.common.os.Platform;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.StereoSceneController;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.globes.FlatGlobe;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.SurfaceImage;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import repast.simphony.gis.visualization.engine.GISDisplayData;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor.VIEW_TYPE;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.RepastCoverageFactory;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.AbstractDisplay;
import repast.simphony.visualization.AddedRemovedLayoutUpdater;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.IntervalLayoutUpdater;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.MovedLayoutUpdater;
import repast.simphony.visualization.UpdateLayoutUpdater;
import repast.simphony.visualization.editor.EditorFactory;
import repast.simphony.visualization.gis3D.RepastStereoOptionSceneController.RenderQuality;
import repast.simphony.visualization.gis3D.style.CoverageStyle;
import repast.simphony.visualization.gis3D.style.MarkStyle;
import repast.simphony.visualization.gis3D.style.NetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

/**
 * Display for GIS 3D
 * 
 * @author Eric Tatara
 *
 *	TODO GIS add separator layers that are always hidden with predefined 
 *       String key IDs that can be used to separate layer types such as 
 *       agent layers, network layers, coverage layers, wms layers, etc.
 */
public class DisplayGIS3D extends AbstractDisplay {
	private static MessageCenter msg = MessageCenter.getMessageCenter(DisplayGIS3D.class);
	protected static final double MIN_DEFAULT_ZOOM_ALTITUDE = 5000;  // meters

	static {
		// this seems to fix jogl canvas flicker issues on windows
		System.setProperty("sun.awt.noerasebackground", "true");
	}

	public static final String BACKGROUND_LAYER_NAME = "Background";
	public static final String LAYER_ID_KEY = "layer id";
	
	private static final String ANAGLYPH_ICON = "3d_smiley.png";
	private static final String GLOBE_ICON = "browser.png";
	private static final String WMS_ICON = "wms2.png";

	protected Lock updateLock = new ReentrantLock();
	protected JPanel panel;
	
	// TODO GIS need a GeoLayout that holds the geography and updateds locations
	protected Layout layout;
	protected LayoutUpdater layoutUpdater;
	protected GISDisplayData<?> initData;

	protected Geography geog;
	protected Model model;

	protected Map<Class<?>, AbstractRenderableLayer<?,?>> classStyleMap;
	protected Map<Network<?>, NetworkLayerGIS> networkLayerMap;
	protected Map<String, CoverageLayer> coverageLayerMap;
	protected List<Layer> globeLayers;
	protected Map<GridCoverage2D, SurfaceImage> coverageToRenderableMap;
	
	protected WorldWindow worldWindow;
	protected String displayMode = AVKey.STEREO_MODE_NONE;
	protected LayerPanel layerPanel;

	protected boolean doRender = true;
	protected boolean visible;
	protected Color backgroundColor;
	
	protected RepastViewControlsSelectListener viewControlsSelectListener = null;
	protected RepastStatusBar statusBar = null;
	
	protected Globe roundGlobe;
	protected FlatGlobe flatGlobe;
	protected boolean trackAgents; // keeps all agents in view even if moving
	protected Sector boundingSector;
	
	// Use a position listener that keeps track of the last lat/lon coordinates the
	//   mouse is pointed to.  This is easier than trying to determine the lat/lon
	//   from the display directly for the purposes of probing.
	protected GlobePositionListener positionListener; 
	
	public class GlobePositionListener implements PositionListener{
		private Position pos = null;
		
		@Override
		public void moved(PositionEvent event) {
			pos = event.getPosition();
		}
		
		public Position getPosition() {
			return pos;
		}
	}
	
	public DisplayGIS3D(GISDisplayData<?> data, Layout layout) {
		classStyleMap = new LinkedHashMap<Class<?>, AbstractRenderableLayer<?,?>>();
		networkLayerMap = new LinkedHashMap<Network<?>, NetworkLayerGIS>();
		coverageLayerMap = new LinkedHashMap<String, CoverageLayer>();
		
		coverageToRenderableMap = new LinkedHashMap<GridCoverage2D, SurfaceImage>();

		initData = data;
		this.layout = layout;
		layoutUpdater = new UpdateLayoutUpdater(layout);

		Configuration.setValue(AVKey.SCENE_CONTROLLER_CLASS_NAME,
				RepastStereoOptionSceneController.class.getName());
		
		Configuration.setValue(AVKey.GLOBE_CLASS_NAME, EarthFlat.class.getName());
		Configuration.setValue(AVKey.VIEW_CLASS_NAME, FlatOrbitView.class.getName());

		model = new BasicModel();
		
		// Only include WWJ globe layers specified in the descriptor if any
		globeLayers = new ArrayList<Layer>();
		LayerList modelLayers = model.getLayers();
		
		Map<String,Boolean> globeLayersToInclude = data.getGlobeLayers();
		for (String layerName : globeLayersToInclude.keySet()) {
			Layer layer = modelLayers.getLayerByName(layerName);
		
			if (layer != null) {
				layer.setValue(LAYER_ID_KEY, layerName);
				globeLayers.add(layer);
				Boolean enabled = globeLayersToInclude.get(layerName);
				if (enabled != null) 
					layer.setEnabled(enabled);
			}
			else {
				msg.warn("Globe layer not available: " + layerName);
			}
		}
			
		model.getLayers().removeAll();  // clear all default layers
		
		if (Platform.getOSType() == Platform.OSType.MACOS) {
			// use the slower swing version to avoid problems on
			// OSX with jogl 2.0 under Java7

			worldWindow = new RepastWorldWindowGLJPanel();
		} else {
			worldWindow = new RepastWorldWindowGLCanvas();
		}
		worldWindow.setModel(model);

		// Create and install the view controls layer and register a controller for
		// it with the World Window.
		ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
		model.getLayers().add(0, viewControlsLayer);
//		WWUtils.insertBeforeCompass(worldWindow, viewControlsLayer);
		initListener();
		viewControlsSelectListener = new RepastViewControlsSelectListener(worldWindow, viewControlsLayer);
		worldWindow.addSelectListener(viewControlsSelectListener);

		StereoSceneController asc = (StereoSceneController) worldWindow.getSceneController();
		asc.setStereoMode(this.displayMode);

		initGlobes(data.getViewType());
	}


	/**
	 * Register the agent class and style information
	 * 
	 * @param clazz the agent class to style in the display
	 * @param style the agent style
	 * @param order the agent layer order in the display
	 */
	public void registerStyle(Class<?> clazz, StyleGIS<?> style) {
		AbstractRenderableLayer layer = classStyleMap.get(clazz);

		String layerName = clazz.getSimpleName();
		
		// TODO WWJ - set the layer type based on the style
		if (layer == null) {
			
			if (style instanceof MarkStyle){
			  layer = new PlaceMarkLayer(layerName, (MarkStyle<?>)style);
			}
			else if (style instanceof SurfaceShapeStyle){
			  layer = new SurfaceShapeLayer(layerName, (SurfaceShapeStyle<?>)style);
			}
			
			if (layer != null) {
			  layer.setValue(LAYER_ID_KEY, clazz.getName());
			  classStyleMap.put(clazz, layer);
			}
		} 
		else {
			layer.setStyle(style);
		}
	}  
	
	/**
	 * Register the network and style information
	 * @param network the network
	 * @param style the network style
	 */
	public void registerNetworkStyle(Network<?> network, NetworkStyleGIS style) {
		NetworkLayerGIS layer = networkLayerMap.get(network);
		
		if (layer == null) {
			layer = new NetworkLayerGIS(network, style);
			layer.setValue(LAYER_ID_KEY, network.getName());
			networkLayerMap.put(network, layer);
		} 
		else {
			layer.setStyle(style);
		}
	}
	
	/**
	 * Register the dynamic coverage and style information
	 * 
	 * @param coverageName the coverage name to style in the display
	 * @param style the coverage style
	 * @param order the coverage layer order in the display
	 */
	public void registerCoverageStyle(String coverageName, CoverageStyle<?> style) {
		
		CoverageLayer layer = coverageLayerMap.get(coverageName);

		if (layer == null) {
			layer = new CoverageLayer(coverageName, style);
			layer.setValue(LAYER_ID_KEY, coverageName);
			coverageLayerMap.put(coverageName, layer);
		} 
		else {
			layer.setStyle(style);
		}
	}
	
	public void createPanel() {
		panel = new JPanel();

		panel.setLayout(new BorderLayout());
		((Component) worldWindow).setPreferredSize(new Dimension(0, 0));

		JPanel wwPanel = new JPanel(new BorderLayout());
		wwPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
		wwPanel.add(((Component) worldWindow), BorderLayout.CENTER);

		panel.add(wwPanel, BorderLayout.CENTER);

		statusBar = new RepastStatusBar();
		statusBar.setEventSource(worldWindow);
		panel.add(statusBar, BorderLayout.PAGE_END);

		JPanel leftPanel = new JPanel(new BorderLayout());

		layerPanel = new LayerPanel(worldWindow);
		leftPanel.add(layerPanel, BorderLayout.WEST);

		panel.add(leftPanel, BorderLayout.WEST);
		
		// This HierarchyListener forces an update/render when the panel changes 
		//   "visibility" from events that show the panel when it was previously
		//   not shown, e.g. changing tabs or other docking operations.  This 
		//   listener is necessary because the WorldWindow may not render when
		//   the panel is not shown.
		panel.addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED ||
						e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED + HierarchyEvent.DISPLAYABILITY_CHANGED) {
					if (e.getComponent().isShowing()) {
						visible = true;
						update();
						render();
					} 
					else{
						visible = false;
					}
				}
			}
		});
	}

	public JPanel getPanel() {
		if (panel == null) {
			createPanel();
		}
		return panel;
	}

	protected boolean isVisible() {
		return visible;
	}

	/**
	 * Create the select listener.
	 */
	private void initListener() {		
		worldWindow.addSelectListener(new SelectListener() {
			public void selected(SelectEvent event) {
				if (event.getEventAction().equals(SelectEvent.LEFT_DOUBLE_CLICK)) {
					if (event.hasObjects()) {
						if (event.getTopObject() instanceof Renderable)
							probe(event);
					}
				}
			}
		});
		
		// The position listener maintains the current globe position of the cursor.
		positionListener = new GlobePositionListener();
		worldWindow.addPositionListener(positionListener);
	}

	public void probe(SelectEvent event) {
		Object obj = null;
		Renderable pickedShape = (Renderable) event.getTopObject();
		
		// First check if probed object is a coverage
		if (pickedShape instanceof SurfaceImage) {
			for (CoverageLayer layer : coverageLayerMap.values()) {
				if ( pickedShape == layer.getSurfaceImage() ) {					
					obj = layer.getProbedObject(positionListener.getPosition());
				}
			}
		}
		
		// Next check if probed object is an agent
		else {
			for (AbstractRenderableLayer<?,?> layer : classStyleMap.values()) {
				Object foundObj = layer.findObjectForRenderable(pickedShape);
				if (foundObj != null)
					obj = foundObj;
			}
			
			for (AbstractRenderableLayer<?,?> layer : networkLayerMap.values()) {
				Object foundObj = layer.findObjectForRenderable(pickedShape);
				if (foundObj != null)
					obj = foundObj;
			}
		}
		
		List<Object> objList = new ArrayList<Object>() {};
		objList.add(obj);

		if (obj != null)
			probeSupport.fireProbeEvent(this, objList);
	}
	
	@Override
	public void init() {

		for (Object obj : initData.objects()) {
			addObject(obj);
		}

		// TODO GIS This seems brittle since there technically could be multiple Geography
		for (Projection<?> proj : initData.getProjections()) {
			if (proj instanceof Geography) {
				geog = (Geography<?>) proj;
			}
		}

		geog.addProjectionListener(this);  // Listen for agent add/remove from geog

		// TODO GIS probable better to hand the geography to the layout ?
		for (AbstractRenderableLayer<?,?> layer : classStyleMap.values()) {
			layer.setGeography(geog);
		}
		for (AbstractRenderableLayer<?,?> layer : networkLayerMap.values()) {
			layer.setGeography(geog);
		}
		for (CoverageLayer layer : coverageLayerMap.values()) {
			layer.setGeography(geog);
		}
		
		// TODO GIS should the axis order be part of the style?
		List<RenderableLayer> staticLayers = new ArrayList<RenderableLayer>();
		boolean forceLonLatOrder = true;
		for (String fileName : initData.getStaticCoverageMap().keySet()) {
			RenderableLayer layer = createStaticRasterLayer(fileName, forceLonLatOrder);

			if (layer != null)
				staticLayers.add(layer);
		}
		
		// First collect all layers, then set the layer order using the order number 
		// Ordered map of all renderable layers
	  TreeMap<Integer, Layer> orderedLayerMap =	new TreeMap<Integer, Layer>();
    
	  // Unsorted layers have no specified layer order
	  List<Layer> unsortedLayers = new ArrayList<Layer>();
    List<Layer> layersToSort = new ArrayList<Layer>();
   
    layersToSort.addAll(classStyleMap.values());
    layersToSort.addAll(coverageLayerMap.values());
    layersToSort.addAll(networkLayerMap.values());
    layersToSort.addAll(globeLayers);
    layersToSort.addAll(staticLayers);
    
  	for (Layer layer : layersToSort) {
  		Integer order = initData.getLayerOrders().get(layer.getValue(LAYER_ID_KEY));
  		
  		// If the order is non null and doesnt already exist, add the layer
  		if (order != null && !orderedLayerMap.containsKey(order)) {	
  			orderedLayerMap.put(order, layer);
  		}
  		// Otherwise save in the ordered layer list
  		else {
  			unsortedLayers.add(layer);
  		}
  	}
  	
  	for (Layer layer : orderedLayerMap.values()) {
  		
  		System.out.println(layer.getName());
  		
			model.getLayers().add(layer);
  	}
  	
  	// Put all unsorted layers at the back (index 0 - auto index shift)
  	for (Layer layer : unsortedLayers) {
			model.getLayers().add(0,layer);
  	}
   		
		// Create a background layer with color from descriptor
		createBackgroundLayer();
		
		boundingSector = calculateBoundingSector();
		doUpdate();
		doRender();
		
		resetHomeView();
	}
	
	/**
	 * Create a simple background layer that is a single colored rectangle that
	 *   covers the entire globe.
	 */
	protected void createBackgroundLayer(){
		if (backgroundColor == null)
			backgroundColor = Color.WHITE;  
		
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		
		g2d.setPaint(backgroundColor);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight() );
		
	  SurfaceImage bgImage = new SurfaceImage(image, 
	  		new ArrayList<LatLon>(Arrays.asList(
        LatLon.fromDegrees(-90d, -180d),
        LatLon.fromDegrees(-90d, 180d),
        LatLon.fromDegrees(90d, 180d),
        LatLon.fromDegrees(90d, -180d)
    )));
	  
	  RenderableLayer layer = new RenderableLayer();
    layer.setName(BACKGROUND_LAYER_NAME);
    layer.setPickEnabled(false);
    layer.addRenderable(bgImage);
     
    // Add background layer to first layer position (reshuffles automatically)
    model.getLayers().add(0, layer);
	}
	
		
	/**
	 * Adds a static raster layer from the provided file.  The raster BufferedImage
	 *   is created and added to the WWJ Globe so that it persists as long as the
	 *   display exists, but is not updated with display updates.
	 *   
	 * @param filename the GIS raster filename to display
	 * @param forceLongitudeFirstAxis true if lon should be forced first axis in coverage loader
	 */
	protected RenderableLayer createStaticRasterLayer(String filename, 
			boolean forceLongitudeFirstAxis){
		
		File file = new File(filename);
			
		GridCoverage2D coverage = RepastCoverageFactory.createCoverageFromFile(file, 
				forceLongitudeFirstAxis);
		
		if (coverage == null) { 
			String info = "Error loading coverage for display: " + file.getPath();
			Exception ex = new Exception(info);
			msg.error(info, ex);
			ex.printStackTrace();
			return null;
		}
		
		ReferencedEnvelope envelope = null;
		
		envelope = new ReferencedEnvelope(coverage.getEnvelope());
		Sector sector = WWUtils.envelopeToSectorWGS84(envelope);
		
		// GridCoverage2D.getRenderedImage() returns a PlanarImage
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		
		SurfaceImage si = new RepastSurfaceImage(pi.getAsBufferedImage(), sector);

		// Use a standard Renderable layer for static images
		RenderableLayer layer = new RenderableLayer();
		
		layer.setName(file.getName());
		layer.setValue(LAYER_ID_KEY, filename);
		layer.setPickEnabled(false);
		layer.addRenderable(si);
		
		// TODO GIS static layer styling - get the style for opacity, smoothing and RasterSymbolizerHelper
		
//		layer.setOpacity(0.5);
		
		return layer;
	}
	

	/**
	 * !!! Destroy needs to properly dispose and shutdown of WorldWind objects 
	 *     to prevent memory leaks !!!!
	 */
	public void destroy() {
		super.destroy();
		for (Projection proj : initData.getProjections()) {
			proj.removeProjectionListener(this);
		}
		initData = null;

		EditorFactory.getInstance().reset();

		// The following lines call modified customized dispose() methods since 
		//   the default WWJ classes don't dispose properly.
		worldWindow.removeSelectListener(viewControlsSelectListener);
		viewControlsSelectListener.dispose();
		statusBar.dispose();
//		layerPanel.dispose();
		worldWindow.shutdown();
		WorldWind.shutDown();
		worldWindow = null;
	}

	@Override
	protected void addObject(Object o) {
		AbstractRenderableLayer<?,?> layer = classStyleMap.get(o.getClass());
		if (layer != null) {
			try{
				updateLock.lock();
				layer.addObject(o);
				layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.ADDED);
			}
			finally {
				updateLock.unlock();
			}
		}
	}

	@Override
	protected void moveObject(Object o) {
		
		// TODO GIS - Move object in GIS3D.  It would be useful to call a 
		//      layer.moveObject(...) in cases where the geometry is null and set 
		//      later.
		
		layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.MOVED);
	}

	@Override
	protected void removeObject(Object o) {
		Class<?> clazz = o.getClass();
		AbstractRenderableLayer<?,?> layer = classStyleMap.get(clazz);
		if (layer != null) {
			try{
				updateLock.lock();
				layer.removeObject(o);
				layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.REMOVED);
			}
			finally{
				updateLock.unlock();
			}
		}
	}

	@Override
	public void setLayout(Layout layout) {
		this.layout = layout;
		layoutUpdater.setLayout(layout);
	}

	public void setLayoutFrequency(IDisplay.LayoutFrequency frequency, int interval) {
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

	@Override
	public void update() {

		// TODO GIS The update/render cycle needs to be cleaned up.  Originally, the
		//      update() was just supposed to set values, while render() draw the 
		//      screen.  However, the WWJ globe is updated when any surface object
		//      is updated, so we need to treat the update() call like a render,
		//      which should be less frequent.  Perhaps update() here doesn't actually
		//      need to do anything, and we can delegate all to render.

//		System.out.println("DisplayGIS3D.update()");
		
		if (isVisible()){
//			doUpdate();
			doRender = true;
			
			render();
		}
	}
	
	/**
	 * Do an update without checking for display visibility
	 */
	private void doUpdate(){
		try{
			updateLock.lock();

			for (AbstractRenderableLayer<?,?> layer : classStyleMap.values()) {
				layer.update(layoutUpdater);
			}		
			for (NetworkLayerGIS layer : networkLayerMap.values()) {
				layer.update(layoutUpdater);
			}
			for (CoverageLayer layer : coverageLayerMap.values()){
				layer.update();
			}					
		}
		finally {
			updateLock.unlock();
		}
	}
	
	@Override
	public void render() {	
//		System.out.println("DisplayGIS3D.render()");
		
		long ts = System.currentTimeMillis();
		if (doRender && isVisible()) {
			if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL) {
				
				// Putting doUpdate() in the update interval is key to avoiding flicker
				
				doUpdate();
				doRender();
				lastRenderTS = ts;
			}
			doRender = false;
		}
	}

	/**
	 * Do a render without checking update interval or display visibility
	 */
	private void doRender(){
		ThreadUtilities.runInEventThread(updater);
	}
	
	@Override
	public void setPause(boolean pause) {
		if (pause) {
			doUpdate();
			doRender();
		}
	}

	public Layout getLayout() {
		return layout;
	}

	public List<Class> getRegisteredClasses() {
		return new ArrayList<Class>(classStyleMap.keySet());
	}

	public DisplayEditorLifecycle createEditor(JPanel panel) {
		return null;
	}

	/**
	 * Calculate a bounding sector around all objects and coverages in the display.
	 * 
	 * @return the bounding sector.
	 */
	private Sector calculateBoundingSector(){
		ArrayList<LatLon> points = new ArrayList<LatLon>(); 
		for (Object o : geog.getAllObjects()){
			points.addAll(WWUtils.CoordToLatLon(geog.getGeometry(o).getCoordinates()));
		}
		
		for (CoverageLayer layer : coverageLayerMap.values()){
			Sector sector = layer.getBoundingSector();
					
			// Don't include empty sectors in view limits
			if (!Sector.EMPTY_SECTOR.equals(sector))
				points.addAll(sector.asList());
		}
 
		return Sector.boundingSector(points);
	}

	@Override
	public void resetHomeView() {
		boundingSector = calculateBoundingSector();
		zoomToSector(boundingSector);

		doUpdate();
		doRender();
	}

	private void zoomToSector(Sector sector) {
		double dx = sector.getDeltaLonRadians();
		double dy = sector.getDeltaLatRadians();

		double d = Math.max(dx, dy) / 2;
		
		double distance = d * Earth.WGS84_EQUATORIAL_RADIUS;
		
		double scale = 1.5;  // make the view a little larger than the bounding sector
		double altitude = scale * distance / Math.tan(worldWindow.getView().getFieldOfView().radians / 2);
		
		// don't allow the camera to zoom in super close in the case of features
		//   that a very close together.
		altitude = Math.max(altitude, MIN_DEFAULT_ZOOM_ALTITUDE);
		
		LatLon latlon = sector.getCentroid();
		Position pos = new Position(latlon, altitude);

		worldWindow.getView().setEyePosition(pos);
	}
	
	public void toggleAnaglyphStereo() {
		if (displayMode == AVKey.STEREO_MODE_NONE)
			displayMode = AVKey.STEREO_MODE_RED_BLUE;
		else
			displayMode = AVKey.STEREO_MODE_NONE;

		StereoSceneController asc = (StereoSceneController) worldWindow.getSceneController();
		asc.setStereoMode(this.displayMode);

		worldWindow.redraw();
	}

	@Override
	public void registerToolBar(JToolBar bar) {
		// Assume first component is the default camera button...
		// replace its screenshot action with the WWJ Screenshot action
		JButton cameraButton = (JButton) bar.getComponent(0);

		cameraButton.setAction(new ScreenShotAction(worldWindow));
		cameraButton.setIcon(CAMERA_ICON);
		cameraButton.setText("");

		bar.addSeparator();

		// Add the glob/flat toggle button
		JToggleButton projectionButton = new JToggleButton(new AbstractAction(){
			public void actionPerformed(ActionEvent event){
				AbstractButton abstractButton = (AbstractButton) event.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				enableRoundGlobe(selected);
			}
		});
		projectionButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(GLOBE_ICON)));
		projectionButton.setToolTipText("Toggle Globe / Flat Earth");
		if (!isFlatGlobe())
			projectionButton.setSelected(true);
    bar.add(projectionButton);
		
		// Add the Anaglyph stereo button
    JToggleButton anaglyphButton = new JToggleButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				toggleAnaglyphStereo();
			}
		});

		anaglyphButton.setText(null);
		anaglyphButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(ANAGLYPH_ICON)));
		anaglyphButton.setToolTipText("Anaglyph Stereo");
		bar.add(anaglyphButton);

		// Add the wms button
		JButton wmsButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				new WMSLayerManagerFrame(worldWindow, layerPanel);
			}
		});

		wmsButton.setText(null);
		wmsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(WMS_ICON)));
		wmsButton.setToolTipText("WMS");
		bar.add(wmsButton);
		
		// Render quality list
		DefaultComboBoxModel<RenderQuality> rendermodel = 
				new DefaultComboBoxModel<RenderQuality>();
	
		for (RenderQuality type : RenderQuality.values()){
			rendermodel.addElement(type);		
		}
		
		JComboBox<RenderQuality> renderQualityBox = new JComboBox<>();
		renderQualityBox.setModel(rendermodel);
		renderQualityBox.setToolTipText("Sets the display render quality for agents and networks.");
		
		RepastStereoOptionSceneController controller = 
				(RepastStereoOptionSceneController)worldWindow.getSceneController();
		renderQualityBox.setSelectedItem(controller.getRenderQuality());
		
		renderQualityBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 JComboBox<RenderQuality> combo = (JComboBox)e.getSource();
				 RenderQuality quality = (RenderQuality)combo.getSelectedItem();
				
				 setRenderQuality(quality);
			}
		});
		
		bar.add(renderQualityBox);
				
		// Add a Search bar
		try {
			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(new EmptyBorder(0,10,0,0));
			panel.add(new GazetteerPanel(worldWindow, null));
			bar.add(panel);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Initialize the WWJ globe (flat/globe)
	 * 
	 * @param viewType the view type (flat/globe)
	 */
	protected void initGlobes(VIEW_TYPE viewType){
		flatGlobe = new EarthFlat();
		roundGlobe = new Earth();
		
		if (viewType.equals(VIEW_TYPE.GLOBE)){
			enableRoundGlobe(true);
		}
		else {
			model.setGlobe(flatGlobe);
			flatGlobe.setProjection(FlatGlobe.PROJECTION_MERCATOR);
			setFlatGlobeViewControls();
		}
	}

	public boolean isFlatGlobe(){
		return model.getGlobe() instanceof FlatGlobe;
	}

	public void setTrackAgents(boolean trackAgents) {
		this.trackAgents = trackAgents;
	}
	
	public void setRenderQuality(RenderQuality quality) {
		RepastStereoOptionSceneController controller = 
				(RepastStereoOptionSceneController)worldWindow.getSceneController();

			controller.setRenderQuality(quality);		
			doRender();
	}
	
	/**
	 * Set View controls for flat world
	 */
	private void setFlatGlobeViewControls(){
		BasicOrbitView orbitView = (BasicOrbitView)worldWindow.getView();
		FlatOrbitView flatOrbitView = new FlatOrbitView();
		flatOrbitView.setCenterPosition(orbitView.getCenterPosition());
		flatOrbitView.setZoom(orbitView.getZoom( ));

		// lock the pitch and heading controls (allows zoom only)
		flatOrbitView.setHeading(Angle.ZERO);
		flatOrbitView.setPitch(Angle.ZERO);
	
		flatOrbitView.getViewPropertyLimits().setPitchLimits(Angle.ZERO, Angle.ZERO);
		flatOrbitView.getViewPropertyLimits().setHeadingLimits(Angle.ZERO, Angle.ZERO);
		worldWindow.setView(flatOrbitView);
	}
	
	public void enableRoundGlobe(boolean round){
		if(isFlatGlobe() != round)
			return;

		if(round){
			// Switch to round globe
			model.setGlobe(roundGlobe) ;
			// Switch to orbit view and update with current position
			FlatOrbitView flatOrbitView = (FlatOrbitView)worldWindow.getView();
			BasicOrbitView orbitView = new BasicOrbitView();
			orbitView.setCenterPosition(flatOrbitView.getCenterPosition());
			orbitView.setZoom(flatOrbitView.getZoom( ));
			orbitView.setHeading(flatOrbitView.getHeading());
			orbitView.setPitch(flatOrbitView.getPitch());
			worldWindow.setView(orbitView);
		}
		else{
			// Switch to flat globe
			model.setGlobe(flatGlobe);
			// Switch to flat view and update with current position
			setFlatGlobeViewControls();
		}
		doRender();
	}
	
	public void toggleInfoProbe() {
	}

	public Map<Class<?>, AbstractRenderableLayer<?,?>> getClassStyleMap() {
		return this.classStyleMap;
	}

	public WorldWindow getWwglCanvas() {
		return this.worldWindow;
	}

	public LayerPanel getLayerPanel() {
		return layerPanel;
	}

	@Override
	public void deIconified() {		
	}

	@Override
	public void iconified() {
	}

	@Override
	public void closed() {
	}
	
	protected Runnable updater = new Runnable() {
		public void run() {
			
			// Need to check if the WorldWindow is null, which can happen here if the
			//   HierarchyListener calls for an update after the display is disposed.
			if (worldWindow == null) return;
			
			// Performance improvement. Null pick point skips the doPick() method on
			// the RenderableLayer. Set pick point to null for our rendering
			// since worldwind will update it on mouse input anyway.
			worldWindow.getSceneController().setPickPoint(null);

			// TODO GIS Agent tracking is very inconsistent
//			if (trackAgents && worldWindow.getView().getEyePosition() != null){
//				boundingSector = calculateBoundingSector();
//				
//				Sector visibleSector = worldWindow.getSceneController().getDrawContext().getVisibleSector();
//				
//				if (!visibleSector.contains(boundingSector)){
//					zoomToSector(boundingSector);
//				}				
//			}
			
			try {
				updateLock.lock();
				worldWindow.redraw();
			} 
			finally {
				updateLock.unlock();
			}
		}
	};

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}	
}