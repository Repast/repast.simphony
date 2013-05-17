package repast.simphony.visualization.gis3D;

import static repast.simphony.ui.RSGUIConstants.CAMERA_ICON;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.SceneController;
import gov.nasa.worldwind.StereoOptionSceneController;
import gov.nasa.worldwind.StereoSceneController;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.cache.BasicMemoryCache;
import gov.nasa.worldwind.cache.MemoryCache;
import gov.nasa.worldwind.event.NoOpInputHandler;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.Logging;
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

	public static final String BUFFERED_IMAGE_CACHE_SIZE = "gov.nasa.worldwind.avkey.BufferedImageCacheSize";
	public static final String BUFFERED_IMAGE_CACHE_NAME = java.awt.image.BufferedImage.class.getName();
	protected static final long DEFAULT_BUFFERED_IMAGE_CACHE_SIZE = 1000000;

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
		                  
		// TODO WWJ need to set the max cache size to prevent huge memory allocations with buffered images
		long cacheSizeBytes = 100000000;
		worldWindow.getGpuResourceCache().setCapacity(cacheSizeBytes);
		worldWindow.getGpuResourceCache().setLowWater(cacheSizeBytes/2);

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
			  layer = new StyledSurfaceShapeLayer(clazz.getSimpleName(), (SurfaceShapeStyle)style);
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

		update();
		render();
	}

	public void destroy() {
		super.destroy();
		for (Projection proj : initData.getProjections()) {
			proj.removeProjectionListener(this);
		}
		initData = null;

		EditorFactory.getInstance().reset();

		// TODO WWJ shutdown properly - currently WorldWind and WorldWindow.shutdown() 
		//      doesn't work with jogl_981, so we can try to set = null.
		//
		//      There still seems to be a memory leak with multiple resets !!
//		WorldWind.shutDown();
//		worldWindow.shutdown();

		shutdownWWJ();
		worldWindow = null;

	}

	// TODO WWJ - This shutdown is copied from WorldWindowImpl and reused here because
	//   the current daily build (5/8/2013) shutdown does not work with the latest JOGL
	private void shutdownWWJ(){

		if (worldWindow.getInputHandler() != null){
			worldWindow.getInputHandler().dispose();
			worldWindow.setInputHandler(new NoOpInputHandler());
		}

		// Clear the texture cache
		if (worldWindow.getGpuResourceCache() != null)
			worldWindow.getGpuResourceCache().clear();

		// Dispose all the layers //  TODO: Need per-window dispose for layers
		if (worldWindow.getModel() != null && worldWindow.getModel().getLayers() != null)	{
			for (Layer layer : worldWindow.getModel().getLayers()){
				try	{
					layer.dispose();
				}
				catch (Exception e){
					Logging.logger().log(java.util.logging.Level.SEVERE, Logging.getMessage(
							"WorldWindowGLCanvas.ExceptionWhileShuttingDownWorldWindow"), e);
				}
			}
		}

		SceneController sc = worldWindow.getSceneController();
		if (sc != null)
			if (sc.getGpuResourceCache() != null)
				sc.getGpuResourceCache().clear();
//			sc.dispose();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.visualization.IDisplay#update()
	 */
	public void update() {
		if (isVisible()){
			layoutUpdater.update();
			try{
				updateLock.lock();

				for (AbstractRenderableLayer layer : classStyleMap.values())
					layer.update(layoutUpdater);

				// TODO WWJ also loop through network and raster styles TBD

				doRender = true;
			}
			finally {
				updateLock.unlock();
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
		if (doRender && isVisible()) {
			if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL) {
				ThreadUtilities.runInEventThread(updater);
				lastRenderTS = ts;
			}
			doRender = false;
		}
	}

	public void setPause(boolean pause) {
		if (pause) {
			update();
			render();
		}
		ThreadUtilities.runInEventThread(updater);
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

	public void resetHomeView() {

		// BasicOrbitView homeView = (BasicOrbitView)
		// WorldWind.createConfigurationComponent(AVKey.VIEW_CLASS_NAME);
		//
		//
		// wwglCanvas.getView().stopStateIterators();
		// wwglCanvas.getView().setOrientation(homeView.getEyePosition(),
		// homeView.getCenterPosition());

		// wwglCanvas.getView().setEyePosition(homeView.getEyePosition());

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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void toggleInfoProbe() {
		// TODO Auto-generated method stub

	}

	public Map<Class, AbstractRenderableLayer> getClassStyleMap() {
		return this.classStyleMap;
	}

	public WorldWindow getWwglCanvas() {
		return this.worldWindow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.visualization.IDisplay#deIconified()
	 */
	@Override
	public void deIconified() {
		iconified = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.visualization.IDisplay#iconified()
	 */
	@Override
	public void iconified() {
		iconified = true;
	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub

	}
}