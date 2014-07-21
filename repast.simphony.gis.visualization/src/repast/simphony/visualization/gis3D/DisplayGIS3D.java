package repast.simphony.visualization.gis3D;

import static repast.simphony.ui.RSGUIConstants.CAMERA_ICON;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.StereoOptionSceneController;
import gov.nasa.worldwind.StereoSceneController;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import repast.simphony.space.gis.Geography;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.AbstractDisplay;
import repast.simphony.visualization.AddedRemovedLayoutUpdater;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.IntervalLayoutUpdater;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.MovedLayoutUpdater;
import repast.simphony.visualization.UpdateLayoutUpdater;
import repast.simphony.visualization.editor.EditorFactory;
import repast.simphony.visualization.gis3D.style.MarkStyle;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;
import simphony.util.ThreadUtilities;

import com.jogamp.common.os.Platform;

public class DisplayGIS3D extends AbstractDisplay {

	protected static final double MIN_DEFAULT_ZOOM_ALTITUDE = 500;  // meters

	static {
		// this seems to fix jogl canvas flicker issues on windows
		System.setProperty("sun.awt.noerasebackground", "true");
	}

	private Runnable updater = new Runnable() {
		public void run() {
			// Performance improvement. Null pick point skips the doPick() method on
			// the RenderableLayer. Set pick point to null for our rendering
			// since worldwind will update it on mouse input anyway.
			worldWindow.getSceneController().setPickPoint(null);

			try {
				updateLock.lock();
				worldWindow.redraw();
			} 
			finally {
				updateLock.unlock();
			}
		}
	};

	private static final String ANAGLYPH_ICON = "3d_smiley.png";
	private static final String WMS_ICON = "wms2.png";

	private Lock updateLock = new ReentrantLock();
	protected JPanel panel;
	private Layout layout;
	private LayoutUpdater layoutUpdater;
	protected DisplayData<?> initData;

	private Geography geog;
	private Model model;

	private Map<Class, AbstractRenderableLayer> classStyleMap;

	private WorldWindow worldWindow;
	private String displayMode = AVKey.STEREO_MODE_NONE;
	private LayerPanel layerPanel;

	private boolean doRender = true;
	private boolean iconified = false;
	private JTabbedPane tabParent = null;
	private Component tabChild = null;

	public DisplayGIS3D(DisplayData<?> data, Layout layout) {
		classStyleMap = new LinkedHashMap<Class, AbstractRenderableLayer>();
		initData = data;
		this.layout = layout;
		this.layoutUpdater = new UpdateLayoutUpdater(layout);

		Configuration.setValue(AVKey.SCENE_CONTROLLER_CLASS_NAME,
				StereoOptionSceneController.class.getName());

		// TODO explore "flat world"
		// Configuration.setValue(AVKey.GLOBE_CLASS_NAME,
		// EarthFlat.class.getName());
		// Configuration.setValue(AVKey.VIEW_CLASS_NAME,
		// FlatOrbitView.class.getName());

		model = new BasicModel();
		// model.getLayers().add(new WorldBordersMetacartaLayer());
		if (Platform.getOSType() == Platform.OSType.MACOS) {
			// use the slower swing version to avoid problems on
			// OSX with jogl 2.0 under Java7

			worldWindow = new WorldWindowGLJPanel();
		} else {
			worldWindow = new WorldWindowGLCanvas();
		}
		worldWindow.setModel(model);

		// Create and install the view controls layer and register a controller for
		// it with the World Window.
		ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
		WWUtils.insertBeforeCompass(worldWindow, viewControlsLayer);
		worldWindow.addSelectListener(new ViewControlsSelectListener(worldWindow, viewControlsLayer));

		StereoSceneController asc = (StereoSceneController) worldWindow.getSceneController();
		asc.setStereoMode(this.displayMode);

		initListener();
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

		List objList = new ArrayList() {
		};
		objList.add(obj);

		if (obj != null)
			probeSupport.fireProbeEvent(this, objList);
	}

	public void registerStyle(Class clazz, StyleGIS style) {
		AbstractRenderableLayer layer = classStyleMap.get(clazz);

		// TODO WWJ - set the layer type based on the style
		if (layer == null) {
			
			if (style instanceof MarkStyle){
			  layer = new PlaceMarkLayer(clazz.getSimpleName(), (MarkStyle)style);
			}
			else if (style instanceof SurfaceShapeStyle){
			  layer = new SurfaceShapeLayer(clazz.getSimpleName(), (SurfaceShapeStyle)style);
			}
			
			if (layer != null)
			  classStyleMap.put(clazz, layer);
		} 
		else {
			layer.setStyle(style);
		}
	}

	// TODO WWJ - register network and raster styles  

	public void createPanel() {
		panel = new JPanel();

		panel.setLayout(new BorderLayout());
		((Component) worldWindow).setPreferredSize(new Dimension(0, 0));

		JPanel wwPanel = new JPanel(new BorderLayout());
		wwPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
		wwPanel.add(((Component) worldWindow), BorderLayout.CENTER);

		panel.add(wwPanel, BorderLayout.CENTER);

		StatusBar statusBar = new StatusBar();
		statusBar.setEventSource(worldWindow);
		panel.add(statusBar, BorderLayout.PAGE_END);

		JPanel leftPanel = new JPanel(new BorderLayout());

		layerPanel = new LayerPanel(worldWindow);
		leftPanel.add(layerPanel, BorderLayout.WEST);

		panel.add(leftPanel, BorderLayout.WEST);


		panel.addPropertyChangeListener("ancestor", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Component child = panel;
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

	public JPanel getPanel() {
		if (panel == null) {
			createPanel();
		}
		return panel;
	}

	private boolean isVisible() {
		if (iconified || !((Component) worldWindow).isVisible())
			return false;

		if (tabParent != null)
			return tabParent.getSelectedComponent().equals(tabChild);

		return true;
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
		Collection<AbstractRenderableLayer> layers = classStyleMap.values();
		for (AbstractRenderableLayer layer : layers) {
			Object obj = layer.findObjectForRenderable(renderable);
			if (obj != null)
				return obj;
		}

		// TODO WWJ also loop through network and raster styles TBD

		return null;
	}

	@Override
	public void init() {

		for (Object obj : initData.objects()) {
			addObject(obj);
		}

		for (Projection proj : initData.getProjections()) {
			if (proj instanceof Geography) {
				geog = (Geography) proj;
			}
		}

		geog.addProjectionListener(this);

		for (AbstractRenderableLayer layer : classStyleMap.values()) {
			layer.setGeography(geog);
			layer.setModel(model);
			model.getLayers().add(layer);
		}

		// TODO WWJ also loop through network and raster styles TBD

		doUpdate();
		doRender();
		
		resetHomeView();
	}

	public void destroy() {
		super.destroy();
		for (Projection proj : initData.getProjections()) {
			proj.removeProjectionListener(this);
		}
		initData = null;

		EditorFactory.getInstance().reset();

		WorldWind.shutDown();
		worldWindow = null;
	}

	@Override
	protected void addObject(Object o) {
		AbstractRenderableLayer layer = classStyleMap.get(o.getClass());
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
		layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.MOVED);
	}

	@Override
	protected void removeObject(Object o) {
		Class clazz = o.getClass();
		AbstractRenderableLayer layer = classStyleMap.get(clazz);
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

			for (AbstractRenderableLayer layer : classStyleMap.values())
				layer.update(layoutUpdater);

			// TODO WWJ also loop through network and raster styles TBD

			
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
		
		zoomToSector(calculateBoundingSector());

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

		// Add the Anaglyph stereo button
		JButton anaglyphButton = new JButton(new AbstractAction() {
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

	public void toggleInfoProbe() {
	}

	public Map<Class, AbstractRenderableLayer> getClassStyleMap() {
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
		iconified = false;
		
		doUpdate();
		doRender();
	}


	@Override
	public void iconified() {
		iconified = true;
	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub

	}
}