package repast.simphony.visualization.gisnew;

import static repast.simphony.ui.RSGUIConstants.CAMERA_ICON;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.StereoOptionSceneController;
import gov.nasa.worldwind.StereoSceneController;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

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
import repast.simphony.visualization.gis3D.GazetteerPanel;
import repast.simphony.visualization.gis3D.LayerPanel;
import repast.simphony.visualization.gis3D.WMSLayerManagerFrame;

public class NewDisplayGIS extends AbstractDisplay implements WindowListener {

  private static final String ANAGLYPH_ICON = "3d_smiley.png";
  private static final String WMS_ICON = "wms2.png";

  protected JPanel panel;
  private boolean addWindowListener = true;
  private Layout layout;
  private LayoutUpdater layoutUpdater;
  protected DisplayData<?> initData;
//	private Styler styler = new Styler();
//	private java.util.List<FeatureSource> featureSources = new ArrayList<FeatureSource>();
  //	private Map<Integer, Object> layerOrder = new HashMap<Integer, Object>();
  private Geography geog;
  private Model model;

  private Map<Class, AbstractSurfaceLayer> classStyleMap;
  private Map<Object, AbstractSurfaceLayer> objectStyleMap;

  private WorldWindowGLCanvas wwglCanvas;
  private String displayMode = AVKey.STEREO_MODE_NONE;
  private LayerPanel layerPanel;

  private boolean doRender = true;
  
  public NewDisplayGIS(DisplayData<?> data, Layout layout) {
    classStyleMap = new LinkedHashMap<Class, AbstractSurfaceLayer>();
    objectStyleMap = new HashMap<Object, AbstractSurfaceLayer>();
    initData = data;
    this.layout = layout;
    this.layoutUpdater = new UpdateLayoutUpdater(layout);

    Configuration.setValue(AVKey.SCENE_CONTROLLER_CLASS_NAME,
    		StereoOptionSceneController.class.getName());

    // TODO explore "flat world"
//    Configuration.setValue(AVKey.GLOBE_CLASS_NAME, EarthFlat.class.getName());
//    Configuration.setValue(AVKey.VIEW_CLASS_NAME, FlatOrbitView.class.getName());
    
    model = new BasicModel();
//    model.getLayers().add(new WorldBordersMetacartaLayer());
    wwglCanvas = new WorldWindowGLCanvas();
    wwglCanvas.setModel(model);
    

    StereoSceneController asc = (StereoSceneController) wwglCanvas.getSceneController();
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
  
//	public void registerAgentStyle(String agentName, Style style, Integer order) {
//	styler.registerStyle(agentName, style);
//	layerOrder.put(order, agentName);

//	}

  /**
   * Create the select listener.
   */
  private void initListener() {
    wwglCanvas.addSelectListener(new SelectListener() {
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

  public void registerStyle(Class clazz, StyleGIS style) {
  	AbstractSurfaceLayer layer = classStyleMap.get(clazz);
    if (layer == null) {
      layer = new StyledSurfaceShapeLayer(clazz.getSimpleName(), style, wwglCanvas);
      classStyleMap.put(clazz, layer);
    } else {
      ((StyledSurfaceShapeLayer) layer).setStyle(style);
    }
  }

//  public void registerNetworkStyle(Network net, EdgeStyleGIS3D style) {
//  	AbstractSurfaceDisplayLayerGIS layer = objectStyleMap.get(net);
//    if (layer == null) {
//      layer = new NetworkDisplayLayerGIS3D(net, style, this, wwglCanvas, net.getName());
//      objectStyleMap.put(net, layer);
//    } else {
//      ((NetworkDisplayLayerGIS3D) layer).setStyle(style);
//    }
//  }

  public void createPanel() {
    panel = new JPanel();
    panel.addHierarchyListener(new HierarchyListener() {
      public void hierarchyChanged(HierarchyEvent e) {
        if (e.getChangeFlags() == HierarchyEvent.DISPLAYABILITY_CHANGED
                && addWindowListener) {
          Window window = SwingUtilities.getWindowAncestor(panel);
          window.addWindowListener(NewDisplayGIS.this);
          addWindowListener = false;
        }
      }
    });

    panel.setLayout(new BorderLayout());
    wwglCanvas.setPreferredSize(new Dimension(0, 0));

    JPanel wwPanel = new JPanel(new BorderLayout());
    wwPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
    wwPanel.add(wwglCanvas, BorderLayout.CENTER);

    panel.add(wwPanel, BorderLayout.CENTER);

    StatusBar statusBar = new StatusBar();
    statusBar.setEventSource(wwglCanvas);
    panel.add(statusBar, BorderLayout.PAGE_END);

    JPanel leftPanel = new JPanel(new BorderLayout());

    layerPanel = new LayerPanel(wwglCanvas);
    leftPanel.add(layerPanel, BorderLayout.WEST);

    panel.add(leftPanel, BorderLayout.WEST);
    
  }

  public JPanel getPanel() {
    if (panel == null) {
      createPanel();
    }
    return panel;
  }


  /**
   * Finds the object for which the specified PNode is the representation.
   *
   * @param node the representational PNode
   * @return the object for which the specified PNode is the representation or
   *         null if the object is not found.
   */
  public Object findObjForItem(Renderable renderable) {
    Collection<AbstractSurfaceLayer> layers = classStyleMap.values();
    for (AbstractSurfaceLayer layer : layers) {
      Object obj = layer.findObjectForRenderable(renderable);
      if (obj != null)
        return obj;
    }

    // more to find, so search for edges
    Collection<AbstractSurfaceLayer> netLayers = objectStyleMap.values();
    for (AbstractSurfaceLayer layer : netLayers) {
      Object obj = layer.findObjectForRenderable(renderable);
      if (obj != null)
        return obj;
    }
    return null;
  }


  public void init() {

    for (Object obj : initData.objects()) {
      addObject(obj);
    }

    for (Projection proj : initData.getProjections()){
    	if (proj instanceof Geography){
    		geog = (Geography)proj;
    	}
    }

    geog.addProjectionListener(this);

    for (AbstractSurfaceLayer layer : classStyleMap.values()) {
      layer.setGeography(geog);
      layer.setModel(model);
      model.getLayers().add(layer);
    }
    for (AbstractSurfaceLayer layer : objectStyleMap.values()) {
      layer.setGeography(geog);
      layer.setModel(model);
      model.getLayers().add(layer);
    }
    
    update();
    render();
  }

  public synchronized void render() {
    
  	if (doRender && wwglCanvas.isVisible()){
  	
  		for (AbstractSurfaceLayer layer : classStyleMap.values()) {
  			layer.applyUpdates();
  		}
  		for (AbstractSurfaceLayer layer : objectStyleMap.values()) {
  			layer.applyUpdates();
  		}

  		// Performance improvement. Null pick point skips the doPick() method on
  		// the RenderableLayer.  Set pick point to null for our rendering
  		// since worldwind will update it on mouse input anyway.
  		wwglCanvas.getSceneController().setPickPoint(null);
  		
  		wwglCanvas.redraw();
  		doRender = false;  
  	}
  }

  public void destroy() {
    super.destroy();
    for (Projection proj : initData.getProjections()) {
      proj.removeProjectionListener(this);
    }
    initData = null;
    Window window = SwingUtilities.getWindowAncestor(panel);
    if (window != null) window.removeWindowListener(this);
    EditorFactory.getInstance().reset();
    
  	WorldWind.shutDown();
  }

  @Override
  protected void addObject(Object o) {
  	AbstractSurfaceLayer layer = classStyleMap.get(o.getClass());
    if (layer != null){
    	layer.addObject(o);
    	layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.ADDED);
    }
  }

  @Override
  protected void moveObject(Object o) {
    layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.MOVED);
  }

  @Override
  protected void removeObject(Object o) {
    Class clazz = o.getClass();
    AbstractSurfaceLayer layer = classStyleMap.get(clazz);
    if (layer != null) {
      layer.removeObject(o);
      layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.REMOVED);
    }
  }

  public void setLayout(Layout layout) {
    this.layout = layout;
    layoutUpdater.setLayout(layout);
  }

  public void setLayoutFrequency(IDisplay.LayoutFrequency frequency,
                                 int interval) {
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

  public synchronized void update() {  	
  	layoutUpdater.update();

      for (AbstractSurfaceLayer layer : classStyleMap.values()) 
        layer.update(layoutUpdater);
      
      for (AbstractSurfaceLayer layer : objectStyleMap.values()) 
        layer.update(layoutUpdater);
      
      doRender = true;
  }

//  public Renderable getVisualItem(Object o) {
//    Class clazz = o.getClass();
//    AbstractSurfaceDisplayLayerGIS layer = classStyleMap.get(clazz);
//    Renderable pNode = null;
//    if (layer != null) {
//      pNode = layer.getVisualItem(o);
//    }
//
//    if (pNode == null) {
//      // try the net layer
//      for (AbstractSurfaceDisplayLayerGIS netLayer : objectStyleMap.values()) {
//        pNode = netLayer.getVisualItem(o);
//        if (pNode != null) break;
//      }
//    }
//    return pNode;
//  }

  public void windowActivated(WindowEvent arg0) {
  	System.out.println("Window Activated");
  }

  public void windowClosed(WindowEvent arg0) {
  	System.out.println("Window Closed");
  }

  public void windowClosing(WindowEvent arg0) {
  	System.out.println("Window Closing");
  }

  public void windowDeactivated(WindowEvent arg0) {
  	System.out.println("Window Deactivated");
  }

  public void windowDeiconified(WindowEvent e) {
  	System.out.println("Window Deiconified");
  }

  public void windowIconified(WindowEvent e) {
  	System.out.println("Window Iconified");
  }

  public void windowOpened(WindowEvent arg0) {
  	System.out.println("Window Opened");
  }

  public void setPause(boolean pause) {
  }

  public void iconified() {
  	System.out.println("Iconified");
  }

  public void closed() {
  	System.out.println("Closed");
  }

  public void deIconified() {
  	System.out.println("Deiconified");
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

//  	BasicOrbitView homeView = (BasicOrbitView) WorldWind.createConfigurationComponent(AVKey.VIEW_CLASS_NAME);
//
//
//  	wwglCanvas.getView().stopStateIterators();
//  	wwglCanvas.getView().setOrientation(homeView.getEyePosition(), homeView.getCenterPosition());
  	
//  	wwglCanvas.getView().setEyePosition(homeView.getEyePosition());
  	
  	 }

  public void toggleAnaglyphStereo() {
    if (displayMode == AVKey.STEREO_MODE_NONE)
      displayMode = AVKey.STEREO_MODE_RED_BLUE;
    else
      displayMode = AVKey.STEREO_MODE_NONE;

    StereoSceneController asc = (StereoSceneController) wwglCanvas.getSceneController();
    asc.setStereoMode(this.displayMode);

    wwglCanvas.redraw();
  }

  @Override
  public void registerToolBar(JToolBar bar) {
  	// Assume first component is the default camera button...
  	// replace its screenshot action with the WWJ Screenshot action
  	JButton cameraButton = (JButton)bar.getComponent(0);
  	
  	cameraButton.setAction(new ScreenShotAction(wwglCanvas));
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
        new WMSLayerManagerFrame(wwglCanvas, layerPanel);
      }
    });

    wmsButton.setText(null);
    wmsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource(WMS_ICON)));
    wmsButton.setToolTipText("WMS");
    bar.add(wmsButton);

    // Add a Gazetter
    try {
			bar.add(new GazetteerPanel(wwglCanvas, null));
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

	public Map<Class, AbstractSurfaceLayer> getClassStyleMap() {
		return this.classStyleMap;
	}

	public WorldWindowGLCanvas getWwglCanvas() {
		return this.wwglCanvas;
	}
}