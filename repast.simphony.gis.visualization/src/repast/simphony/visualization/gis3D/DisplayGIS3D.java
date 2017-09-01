package repast.simphony.visualization.gis3D;

import static repast.simphony.ui.RSGUIConstants.CAMERA_ICON;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;

import com.jogamp.common.os.Platform;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.StereoSceneController;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
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
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.SurfaceImage;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;
import repast.simphony.gis.visualization.engine.GISDisplayData;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor.VIEW_TYPE;
import repast.simphony.space.gis.Geography;
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
import repast.simphony.visualization.gis3D.style.CoverageStyle;
import repast.simphony.visualization.gis3D.style.MarkStyle;
import repast.simphony.visualization.gis3D.style.NetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;
import simphony.util.ThreadUtilities;

public class DisplayGIS3D extends AbstractDisplay {

	protected static final double MIN_DEFAULT_ZOOM_ALTITUDE = 5000;  // meters

	static {
		// this seems to fix jogl canvas flicker issues on windows
		System.setProperty("sun.awt.noerasebackground", "true");
	}

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

	protected Map<String,Integer> layerOrder;
	protected Map<Class<?>, AbstractRenderableLayer<?,?>> classStyleMap;
	protected Map<Network<?>, GISNetworkLayer> networkLayerMap;
	protected Map<String, CoverageLayer> coverageLayerMap;

	protected Map<GridCoverage2D, SurfaceImage> coverageToRenderableMap;
	
	protected WorldWindow worldWindow;
	protected String displayMode = AVKey.STEREO_MODE_NONE;
	protected LayerPanel layerPanel;

	protected boolean doRender = true;
	protected boolean visible;
	
	protected RepastViewControlsSelectListener viewControlsSelectListener = null;
	protected RepastStatusBar statusBar = null;
	
	protected Globe roundGlobe;
	protected FlatGlobe flatGlobe;
	protected boolean trackAgents; // keeps all agents in view even if moving
	protected Sector boundingSector;

	public DisplayGIS3D(GISDisplayData<?> data, Layout layout) {
		layerOrder = new HashMap<String,Integer>();
		classStyleMap = new LinkedHashMap<Class<?>, AbstractRenderableLayer<?,?>>();
		networkLayerMap = new LinkedHashMap<Network<?>, GISNetworkLayer>();
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
		WWUtils.insertBeforeCompass(worldWindow, viewControlsLayer);
		viewControlsSelectListener = new RepastViewControlsSelectListener(worldWindow, viewControlsLayer);
		worldWindow.addSelectListener(viewControlsSelectListener);

		StereoSceneController asc = (StereoSceneController) worldWindow.getSceneController();
		asc.setStereoMode(this.displayMode);

		initGlobes(data.getViewType());
		initListener();
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
							probe((Renderable) event.getTopObject());
					}
				}
			}
		});
	}

	public void probe(Renderable pickedShape) {
		Object obj = findObjForItem(pickedShape);

		List objList = new ArrayList() {};
		objList.add(obj);

		if (obj != null)
			probeSupport.fireProbeEvent(this, objList);
	}

	/**
	 * Register the agent class and style information
	 * 
	 * @param clazz the agent class to style in the display
	 * @param style the agent style
	 * @param order the agent layer order in the display
	 */
	public void registerStyle(Class<?> clazz, StyleGIS<?> style, Integer order) {
		AbstractRenderableLayer layer = classStyleMap.get(clazz);

		String layerName = clazz.getSimpleName();
		
		// TODO WWJ - set the layer type based on the style
		if (layer == null) {
			
			if (style instanceof MarkStyle){
			  layer = new PlaceMarkLayer(layerName, (MarkStyle)style);
			}
			else if (style instanceof SurfaceShapeStyle){
			  layer = new SurfaceShapeLayer(layerName, (SurfaceShapeStyle)style);
			}
			
			if (layer != null) {
			  classStyleMap.put(clazz, layer);
			  layerOrder.put(layerName, order);
			}
		} 
		else {
			layer.setStyle(style);
			layerOrder.put(layerName, order);
		}
	}  
	
	//	TODO GIS - register network styles
	public void registerNetworkStyle(Network<?> network, NetworkStyleGIS style) {

		
		//		networkLayerMap.put(key, value)
	}
	
	/**
	 * Register the dynamic coverage and style information
	 * 
	 * @param coverageName the coverage name to style in the display
	 * @param style the coverage style
	 * @param order the coverage layer order in the display
	 */
	public void registerCoverageStyle(String coverageName, CoverageStyle style, Integer order) {
		
		CoverageLayer layer = coverageLayerMap.get(coverageName);

		if (layer == null) {
			layer = new CoverageLayer(coverageName, style);
			coverageLayerMap.put(coverageName, layer);
			layerOrder.put(coverageName, order);
		} 
		else {
			layer.setStyle(style);
			layerOrder.put(coverageName, order);
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
	 * Finds the object for which the specified PNode is the representation.
	 * 
	 * @param node
	 *          the representational PNode
	 * @return the object for which the specified PNode is the representation or
	 *         null if the object is not found.
	 */
	public Object findObjForItem(Renderable renderable) {
		Collection<AbstractRenderableLayer<?,?>> layers = classStyleMap.values();
		for (AbstractRenderableLayer<?,?> layer : layers) {
			Object obj = layer.findObjectForRenderable(renderable);
			if (obj != null)
				return obj;
		}

		// TODO WWJ also loop through network and raster styles TBD
		
		// WWJ Renderable for coverage will be the SurfaceImage

		return null;
	}

	@Override
	public void init() {

		for (Object obj : initData.objects()) {
			addObject(obj);
		}

		// TODO GIS This seems brittle since there technically could be multiple Geography
		for (Projection proj : initData.getProjections()) {
			if (proj instanceof Geography) {
				geog = (Geography) proj;
			}
		}

		// TODO GIS need to register network listener also?
		geog.addProjectionListener(this);

		// TODO GIS probable better to hand the geography to the layout ?
		for (AbstractRenderableLayer<?,?> layer : classStyleMap.values()) {
			layer.setGeography(geog);
		}
		for (CoverageLayer layer : coverageLayerMap.values()) {
			layer.setGeography(geog);
		}
		
		// First collect all layers, then set the layer order using the order number 
		// Ordered map of all renderable layers
	  TreeMap<Integer, RenderableLayer> orderedLayerMap = 
	  		new TreeMap<Integer, RenderableLayer>();
    
	  // Unsorted layers have no specified layer order
	  List<RenderableLayer> unsortedLayers = new ArrayList<RenderableLayer>();
    List<RenderableLayer> layersToSort = new ArrayList<RenderableLayer>();
   
    // TODO GIS add other layer types to be sorted
    layersToSort.addAll(classStyleMap.values());
    layersToSort.addAll(coverageLayerMap.values());
    
  	for (RenderableLayer layer : layersToSort) {
  		Integer order = layerOrder.get(layer.getName());
  		
  		// If the order is non null and doesnt already exist, add the layer
  		if (order != null && !orderedLayerMap.containsKey(order)) {	
  			orderedLayerMap.put(order, layer);
  		}
  		// Otherwise save in the ordered layer list
  		else {
  			unsortedLayers.add(layer);
  		}
  	}
  	
  	for (RenderableLayer layer : orderedLayerMap.values()) {
			model.getLayers().add(layer);
  	}
  	
  	// GIS TODO should all unsorted coverage layers go to the back?
  	for (RenderableLayer layer : unsortedLayers) {
			model.getLayers().add(layer);
  	}
   
  	
		// TODO GIS static raster layers should just load here once at init
		for (String fileName : initData.getStaticRasterLayers()) {
			
		}
		
		// TODO WWJ also loop through network and raster styles TBD
		for (GISNetworkLayer layer : networkLayerMap.values()) {
			
		}
		
		// TODO set background image color from display creator
		setBackgroundColor(null);
		
		// TODO Testing
		
		addStaticRasterLayer(new File("data/craterlake-imagery-30m.tif"), true);
		addStaticRasterLayer(new File("data/UTM2GTIF.TIF"), true);
//		addStaticRasterLayer(new File("data/SP27GTIF.TIF"), true);
		addStaticRasterLayer(new File("data/sample_gray.tif"),true);
		
		boundingSector = calculateBoundingSector();
		doUpdate();
		doRender();
		
		resetHomeView();
	}
	
	// TODO GIS Background create the ImageIcon programmatically instead of using file.
	public void setBackgroundColor (Color color){
	  SurfaceImage bgImage = new SurfaceImage(new ImageIcon(getClass().getClassLoader().getResource("white.png")), 
	  		new ArrayList<LatLon>(Arrays.asList(
        LatLon.fromDegrees(-90d, -180d),
        LatLon.fromDegrees(-90d, 180d),
        LatLon.fromDegrees(90d, 180d),
        LatLon.fromDegrees(90d, -180d)
    )));
	  
	  RenderableLayer layer = new RenderableLayer();
    layer.setName("Background");
    layer.setPickEnabled(false);
    layer.addRenderable(bgImage);
    
    // TODO GIS background dont show in layer legend.
    
    // Add background layer to first layer position (reshuffles automatically)
    model.getLayers().add(0, layer);
	}
	
		
	/**
	 * Adds a static raster layer from the provided file.  The raster BufferedImage
	 *   is created and added to the WWJ Globe so that it persists as long as the
	 *   display exists, but is not updated with display updates.
	 *   
	 * @param file the GIS raster file to display
	 * @param forceLongitudeFirstAxis true if lon should be forced first axis in coverage loader
	 */
	protected void addStaticRasterLayer(File file, boolean forceLongitudeFirstAxis){
		
		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, forceLongitudeFirstAxis);
		
		AbstractGridFormat format = GridFormatFinder.findFormat(file);
		GridCoverage2DReader reader = format.getReader(file, hints);
		GridCoverage2D coverage = null;

		try {
			coverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
			
			// TODO GIS messagecenter file format not supported
		}
		
		ReferencedEnvelope envelope = null;
		
		// TODO make sure not to resample to WGS84 if already set to WGS84
		// TODO need to automatically project coverages to WGS84
//		if (!CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), DefaultGeographicCRS.WGS84)) {
//			coverage = (GridCoverage2D)Operations.DEFAULT.resample(coverage,DefaultGeographicCRS.WGS84);
//		}
		
		envelope = new ReferencedEnvelope(coverage.getEnvelope());
		Sector sector = WWUtils.envelopeToSectorWGS84(envelope);
		
		// GridCoverage2D.getRenderedImage() returns a PlanarImage
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		
		SurfaceImage si = new RepastSurfaceImage(pi.getAsBufferedImage(), sector);

		// SurfaceImageLayer is only useful when adding image paths for large images
		//  since it does automatic tiling.  SurfaceImageLayer.addRenderable() simply
		//  passes the object to the RenderableLayer
//		SurfaceImageLayer layer = new SurfaceImageLayer();
		
		// TODO GIS use AbstractRenderableLayer ?
		RenderableLayer layer = new RenderableLayer();
		
		
		layer.setName(file.getName());
		layer.setPickEnabled(false);
		layer.addRenderable(si);
		
		// TODO GIS add layer attributes to descriptor
//		layer.setOpacity(0.5);
		
		// TODO GIS all rasters before a specific layer name ? compass??
		 WWUtils.insertBeforeCompass(worldWindow, layer);
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
		layerPanel.dispose();
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
				
		// TODO GIS it might be useful to provide the GPU cache size in the UI
//		long cacheBytes = worldWindow.getGpuResourceCache().getUsedCapacity();
//		
//		System.out.println("GPU Cache (MB): " + cacheBytes / 1E6);
		
		if (isVisible()){
			doUpdate();
			doRender = true;
		}
	}
	
	/**
	 * Do an update without checking for display visibility
	 */
	private void doUpdate(){
		layoutUpdater.update();
		try{
			updateLock.lock();

			for (AbstractRenderableLayer<?,?> layer : classStyleMap.values()) {
				layer.update(layoutUpdater);
			}

			for (CoverageLayer layer : coverageLayerMap.values()){
				layer.update();
			}
						
			// TODO GIS loop through network layers
			for (GISNetworkLayer layer : networkLayerMap.values()) {
				
			}
					
			// test
			
			
//	    WritableRenderedImage writableImage = (WritableRenderedImage)coverage.getRenderedImage();

//			RenderedImage image = coverage.getRenderedImage();
//			WritableRenderedImage writableImage = new TiledImage(image, true);
//			WritableRandomIter writeIter = RandomIterFactory.createWritable(writableImage, null);
//
//			for (int i=0; i<image.getWidth(); i++) {
//				for (int j=0; j<image.getHeight(); j++) {
//					writeIter.setSample(i, j, 0, RandomHelper.nextIntFromTo(1, 65000));
//				}
//			}
//			
//			PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
//			ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getEnvelope());
//			Sector sector = WWUtils.envelopeToSectorWGS84(envelope);
//			
//			// slow step
//			si.setImageSource(pi.getAsBufferedImage(), sector);

		}
		finally {
			updateLock.unlock();
		}
	}
	
	@Override
	public void render() {		
		long ts = System.currentTimeMillis();
		if (doRender && isVisible()) {
			if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL) {
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

	private Sector calculateBoundingSector(){
		ArrayList<LatLon> points = new ArrayList<LatLon>(); 
		for (Object o : geog.getAllObjects()){
			points.addAll(WWUtils.CoordToLatLon(geog.getGeometry(o).getCoordinates()));
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
		
		double scale = 1.2;  // make the view a little larger than the bounding sector
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
		
		// TODO replace quality button with JList
		
		// Add the quality button
		JToggleButton qualityButton = new JToggleButton(new AbstractAction(){
			public void actionPerformed(ActionEvent event){
				AbstractButton abstractButton = (AbstractButton) event.getSource();
				boolean selected = abstractButton.getModel().isSelected();
				
				RepastStereoOptionSceneController controller = 
						(RepastStereoOptionSceneController)worldWindow.getSceneController();
		
				if (selected){
					controller.setSplitScape(RepastStereoOptionSceneController.SPLIT_SCALE_VERY_HIGH_QUALITY);
					System.out.println("HQ");
				}
				else{
					controller.setSplitScape(RepastStereoOptionSceneController.SPLIT_SCALE_MEDIUM_QUALITY);
				}
								
				worldWindow.redraw();
			}
		});
		
			qualityButton.setText("HQ");
//			qualityButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(WMS_ICON)));
			qualityButton.setToolTipText("Map shape draw quality");
			bar.add(qualityButton);
		
	  // TODO Adjust Gazetter size and move to the right
			
		// Add a Gazetter
		try {
			bar.add(new GazetteerPanel(worldWindow, null));
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

	public void setLayerOrder(String layerName, int order) {
		layerOrder.put(layerName, order);
	}
	
	
}