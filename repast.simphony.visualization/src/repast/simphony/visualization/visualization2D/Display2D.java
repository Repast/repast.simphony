package repast.simphony.visualization.visualization2D;

import static repast.simphony.ui.RSGUIConstants.VIZ_INFO_ICON;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.piccolo2d.PCamera;
import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventListener;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.util.PPickPath;

import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.ui.probe.ProbeManager;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.AbstractDisplay;
import repast.simphony.visualization.AddedRemovedLayoutUpdater;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.DisplayEditorLifecycle;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.IDisplayLayer;
import repast.simphony.visualization.IntervalLayoutUpdater;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.MovedLayoutUpdater;
import repast.simphony.visualization.UpdateLayoutUpdater;
import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import repast.simphony.visualization.editor.EditorFactory;
import repast.simphony.visualization.engine.BoundingBox;
import repast.simphony.visualization.network.NetworkDisplayLayer2D;
import repast.simphony.visualization.visualization2D.style.EdgeStyle2D;
import repast.simphony.visualization.visualization2D.style.Style2D;
import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;
import simphony.util.ThreadUtilities;

/**
 * @deprecated replaced by ogl 2D
 */
public class Display2D extends AbstractDisplay implements WindowListener {

	public static int DISPLAY_HEIGHT = 500;
	public static int DISPLAY_WIDTH = 500;
	
  // used to synchronize between the adding and removing of objs which
  // happens in the simulation thread and applying the latest set of transforms
  // which happens in the rendering thread.
  protected final Object lock = new Object();

  /**
   * Runnable that does the actual render of the 2D display.
   */
  class Renderer implements Runnable {
		public void run() {
      for (IDisplayLayer layer : classStyleMap.values()) {
        layer.applyUpdates();
      }
      for (IDisplayLayer layer : objectStyleMap.values()) {
        layer.applyUpdates();
      }

      for (ProjectionDecorator2D deco : decoratorMap.values()) {
        deco.update();
      }
      
      if (valueDisplayLayer != null)
        valueDisplayLayer.applyUpdates();
      
      doRender = false;                  // reset the render flag
    }
  }

  /**
   * The Updater class takes care of adding/deleting objects in the layers,
   *  but does not actually render. 
   *
   */
  class Updater implements Runnable {
    public void run() {
      for (IDisplayLayer layer : classStyleMap.values()) {
        layer.update(layoutUpdater);
      }

      for (IDisplayLayer layer : objectStyleMap.values()) {
        layer.update(layoutUpdater);
      }
    }
  }

  protected Map<Class, IDisplayLayer2D> classStyleMap;

  protected Map<Object, IDisplayLayer2D> objectStyleMap;

  protected ValueDisplayLayer2D valueDisplayLayer;
  
  protected Layout layout;

  protected LayoutUpdater layoutUpdater;

  protected DisplayData<?> initData;

  protected boolean iconified = false;

  protected JPanel panel;

  private boolean addWindowListener = true;

  protected Runnable renderer;
  protected Updater updater;

  protected RepastCanvas2D canvas;

  protected Map<String, ProjectionDecorator2D> decoratorMap;
  
  protected Iterable<ValueLayer> valueLayers;
  
  protected boolean doRender = true;
  
  protected boolean showHoverProbes = false;
  
  protected PPath hoverProbe = new PPath.Double();
 
  protected PPath hoverProbeBox; 
  
  protected Object lastProbedObject = null;
  
  protected boolean pause = true;
  
  public Display2D(DisplayData<?> data, Layout layout) {
    classStyleMap = new LinkedHashMap<Class, IDisplayLayer2D>();
    objectStyleMap = new HashMap<Object, IDisplayLayer2D>();
    // networkLayerMap = new HashMap<Network, DisplayLayer2D>();
    this.initData = data;
    this.layout = layout;
    this.valueLayers = data.getValueLayers();
    this.layoutUpdater = new UpdateLayoutUpdater(layout);
    canvas = new RepastCanvas2D();
    decoratorMap = new HashMap<String, ProjectionDecorator2D>();
    
    hoverProbe.setPickable(false);
    canvas.getCamera().addChild(hoverProbe);
    initHoverProbeBox();
    initScene();
    
    // uncomment to print piccolo debug info to console
//		PDebug.debugPaintCalls = true;
//		PDebug.debugThreads = true;

  }

  /**
   * Gets the data used to initialize this display.
   *
   * @return the data used to initialize this display.
   */
  public DisplayData getInitData() {
    return initData;
  }

  protected void initScene() {

    PInputEventListener listener = new PBasicInputEventHandler() {
    	private List<Object> probedObjects;
    	
    	/**
    	 * Double click events open a probe panel.
    	 */
    	public void mouseClicked(PInputEvent event) {	
        if (event.isLeftMouseButton() && event.getClickCount() == 2) {
        	PPickPath path = event.getPath();
          Point2D probeLocation = event.getPosition();
          probedObjects = getprobedObjects(path, probeLocation);
          if (probedObjects.size() > 0)
            probeSupport.fireProbeEvent(this, probedObjects);
        }
      }
      
    	/**
    	 * Hover events optionally shows a overlay probe.
    	 */
    	public void mouseMoved(PInputEvent event){    		
    		if (showHoverProbes && pause){
    			PPickPath path  = event.getInputManager().getMouseOver();
    			Point2D probeLocation = event.getPosition();
    			probedObjects = getprobedObjects(path, probeLocation);

    			Object obj = null;

    			if (probedObjects.size() > 0)
    				obj = probedObjects.get(0);
    		      			
    			showHoverProbe(event,obj);
    		}
    	}
    };
    canvas.addInputEventListener(listener);
  }

  /**
   * Create the background box for the hovering probe.
   */
  private void initHoverProbeBox(){
  	Color color = new Color(155,177,228);
	  int width = 250;
	  int height = 200;
	  int titleHeight = 17;
	  
	  hoverProbeBox = PPath.createRectangle(0,0,width,height);
	  hoverProbeBox.setPaint(color);
	  hoverProbeBox.setTransparency(0.75f);
	  hoverProbeBox.setStroke(null);
	  
		PPath titleRect = PPath.createRectangle(0,0,width,titleHeight); 
		titleRect.setPaint(color);
		titleRect.setStroke(null);
//		titleRect.setTransparency(0.25f);
//		titleRect.setStroke(new BasicStroke(1.0f));
//		titleRect.setStrokePaint(color);
		
		hoverProbeBox.addChild(titleRect);
  }
  
  /**
   * Creates a simple probe that hovers at the mouse position when moving over agents.
   * 
   * @param event the mouse event.
   * @param obj the probed object.
   */
  private void showHoverProbe(PInputEvent event, Object obj){
		Point2D p = event.getCanvasPosition();

		event.getPath().canvasToLocal(p, canvas.getCamera());
		
		int horzProbeOffset = 20;
		int vertProbeOffset = 0;        // changes based on cursor location
		
		// find all properties for a newly probed object and create the probe.
		if (obj != null && obj != lastProbedObject){
		  /*
		   * commented out because we dont' use this anymore and this is a deprecated class
			Map<String,Object> valuesMap = new ProbePanelCreator(obj).getProbedProperties();
			
			// the left text box contains the probe title and property names.
			PText names = new PText();
			names.setText(getNamesForHoverProbe(obj,valuesMap));
			names.setFont(new Font("Arial", Font.PLAIN, 14));
			names.setOffset(5, 0);
			
			// the right text box contains the property values.
			PText values = new PText();
			values.setText(getValuesForHoverProbe(valuesMap));
			values.setFont(new Font("Arial", Font.PLAIN, 14));
			values.setOffset(hoverProbeBox.getWidth()/2, 0);
			
			// 12 lines of text (incl title) fit in 200 px height at 14pt font. For
			// every additional value over 12, add 17px to the height of the box.
			int minSize = 200;
			float pixelMultiplier = 16.67f;
			
			int variableHeight = (int)(pixelMultiplier*(valuesMap.keySet().size() + 1));
			
			int probeHeight = Math.max(minSize,variableHeight);
			
			hoverProbeBox.setBounds(0,0,250,probeHeight);
			
			hoverProbe.removeAllChildren();
			hoverProbe.addChild(hoverProbeBox);
			hoverProbe.addChild(names);
			hoverProbe.addChild(values);
			
			if (p.getY() > DISPLAY_HEIGHT / 2)
				vertProbeOffset = -(int)hoverProbeBox.getHeight();
			
		  // offset the tooltip node next to the mouse
			hoverProbe.setOffset(p.getX() + horzProbeOffset, p.getY() + vertProbeOffset);
			lastProbedObject = obj;
			*/
		}
		// if the mouse just moved over the same object, then just repositions the probe.
		else if (obj == lastProbedObject){
			if (p.getY() > DISPLAY_HEIGHT / 2)
				vertProbeOffset = -(int)hoverProbeBox.getHeight();
			
			hoverProbe.setOffset(p.getX() + horzProbeOffset, p.getY() + vertProbeOffset);
		}
		else{
			// node always exists, but contains nothing when not actively probing.
			hoverProbe.removeAllChildren();
			lastProbedObject = null;
		}
  }
  
  /**
   * Returns a substring if the provided string is too long.
   * 
   * @param string the original string.
   * @return a shortened version of the original.
   */
  private String trimString(String string, int length){
  	if (string.length()> length)
  	  return string.substring(0, length);
  	else
  		return string;
  }
  
  /**
   * Creates a String with the title and property names for the hover probe.
   * 
   * @param obj the probed object
   * @param valuesMap a map of properties and values for the probed object.
   * @return a String with the title and property names for the hover probe.
   */
  private String getNamesForHoverProbe(Object obj, Map<String,Object> valuesMap){
  	StringBuffer buffer = new StringBuffer();
  	  	
  	buffer.append(trimString(ProbeManager.createTitle(obj),30) + "\n");
  
  	for (String name : valuesMap.keySet())
  		buffer.append(trimString(name,15) + "\n");
  	
  	return buffer.toString();
  }
  
  /**
   * Creates a string with the values for the hover probe.
   * 
   * @param valuesMap a map of properties and values for the probed object.
   * @return a string with the values for the hover probe.
   */
  private String getValuesForHoverProbe(Map<String,Object> valuesMap){
  	StringBuffer buffer = new StringBuffer();

  	buffer.append("\n");

  	for (String name : valuesMap.keySet()){
  		Object val = valuesMap.get(name);
  		buffer.append(trimString(val.toString(),15) + "\n");
  	}
  	return buffer.toString();
  }
  
  /**
   * Finds the object for which the specified PNode is the representation.
   *
   * @param node the representational PNode
   * @return the object for which the specified PNode is the representation or
   *         null if the object is not found.
   */
  public Object findObjForItem(PNode node) {
    List<PNode> nodes = new ArrayList<PNode>();
    nodes.add(node);
    Collection<IDisplayLayer2D> layers = classStyleMap.values();
    for (IDisplayLayer2D layer : layers) {
      Collection<Object> objs = layer.findObjsForItems(nodes);
      if (objs.size() > 0) return objs.iterator().next();
    }

    // more to find, so search for edges
    Collection<IDisplayLayer2D> netLayers = objectStyleMap.values();
    for (IDisplayLayer2D layer : netLayers) {
      Collection<Object> objs = layer.findObjsForItems(nodes);
      if (objs.size() > 0) return objs.iterator().next();
    }
    return null;
  }
 
  /**
   * Returns a list of agents at the probe location
   * 
   * @param path the path at the probe location
   * @param probeLocation the probe location
   * 
   * @return the list of agents at the probe location.
   */
  protected  List<Object> getprobedObjects(PPickPath path, Point2D probeLocation) {
    List<PNode> pNodes = path.getNodeStackReference();
    
    // remove all the non-representational nodes from the list    
    for (Iterator iter = pNodes.iterator(); iter.hasNext();) {
      PNode node = (PNode) iter.next();
            
      if (node instanceof PCamera || node instanceof DisplayLayer2D) {
        iter.remove();
      }
    }

    int numToFind = pNodes.size();

    List<Object> probedObjects = new ArrayList<Object>();
 
    if (pNodes.contains(valueDisplayLayer)){
    	int[] loc = valueDisplayLayer.convertPixelToLoc(probeLocation);
    	
    	ValueLayer vl = valueLayers.iterator().next();
    	
    	double dLoc[] = new double[loc.length];
    	for (int i=0; i<loc.length; i++)
    		dLoc[i] = loc[i];
    	double val = vl.get(dLoc);
   
    	//probedObjects.add(new ValueLayerProbeObject2D(loc, val, vl.getName()));
    }
    Collection<IDisplayLayer2D> layers = classStyleMap.values();
    for (IDisplayLayer2D layer : layers) {
      Collection<Object> objs = layer.findObjsForItems(pNodes);
      probedObjects.addAll(objs);
      if (probedObjects.size() == numToFind) {
        // found all so no need to search additional layers
        break;
      }
      for (Object obj : objs) {
        // remove the ones we've found from the list to search
        pNodes.remove(layer.getVisualItem(obj));
      }
    }

    if (pNodes.size() != 0) {
      // more to find, so search for edges
      Collection<IDisplayLayer2D> netLayers = objectStyleMap.values();
      for (IDisplayLayer2D layer : netLayers) {
        Collection<Object> objs = layer.findObjsForItems(pNodes);
        probedObjects.addAll(objs);
        if (probedObjects.size() == numToFind) {
          // found all so no need to search additional layers
          break;
        }
        for (Object obj : objs) {
          // remove the ones we've found from the list to search
          pNodes.remove(layer.getVisualItem(obj));
        }
      }
    }
    return probedObjects;
  }

  public void registerDecorator(ProjectionDecorator2D decorator) {
    decoratorMap.put(decorator.getProjection().getName(), decorator);
  }

  public void init() {
    for (Object obj : initData.objects()) {
      addObject(obj);
    }

    for (ValueLayer layer : initData.getValueLayers()) {
//    if (valueLayer == null) 
//      valueLayer = new ValueDisplayLayer2D(null,canvas);
    
      valueDisplayLayer.init(layer);
    }
    
    // add the decorators to the value layer or otherwise canvas base layer
    PLayer baseLayer = valueDisplayLayer != null ? valueDisplayLayer : canvas.getLayer();
    
    for (Projection proj : initData.getProjections()) {
      //ProjectionDecorator2D deco = decoratorMap.get(proj.getName());
      //if (deco != null) 
        //deco.init(this, baseLayer);
      
      proj.addProjectionListener(this);
    }
    
    renderer = new Renderer();
    updater = new Updater();

    update();
    forceRender();
  }

  public void setBoundingBox(BoundingBox box) {
    Rectangle2D rect = box.getRectangle();
    canvas.getCamera().animateViewToCenterBounds(rect, true, 0);
  }

  public void registerStyle(Class clazz, Style2D style) {
    IDisplayLayer2D layer = classStyleMap.get(clazz);
    if (layer == null) {
      layer = new StyledDisplayLayer2D(style, canvas);
      classStyleMap.put(clazz, layer);
    } else {
      ((StyledDisplayLayer2D) layer).setStyle(style);
    }
  }

  public void registerNetworkStyle(Network topology, EdgeStyle2D style) {
    IDisplayLayer2D layer = objectStyleMap.get(topology);
    if (layer == null) {
      layer = new NetworkDisplayLayer2D(topology, style, this, canvas);
      objectStyleMap.put(topology, layer);
    } else {
      ((NetworkDisplayLayer2D) layer).setStyle(style);
    }
  }

  public void registerValueLayerStyle(ValueLayerStyle style) {
  	if (valueDisplayLayer == null)
  		valueDisplayLayer = new ValueDisplayLayer2D(style, canvas);
  	else
  	  valueDisplayLayer.setStyle(style);
  }
  
  public void createPanel() {
    panel = new JPanel();
    panel.addHierarchyListener(new HierarchyListener() {
      public void hierarchyChanged(HierarchyEvent e) {
        if (e.getChangeFlags() == HierarchyEvent.DISPLAYABILITY_CHANGED
                && addWindowListener) {
          Window window = SwingUtilities.getWindowAncestor(panel);
          window.addWindowListener(Display2D.this);
          addWindowListener = false;
        }
      }
    });
  }

  /**
   * The render method is alled each time the simulation runner thread steps 
   *   the schedule.
   */
  public void render() {
  	// Only render if it's time to render (set by the display updater) and
  	// if the display is showing.
  	
  	if (doRender && canvas.isShowing()) synchronized (lock) {
        ThreadUtilities.runInEventThread(renderer);
      }
  }
  
  /**
   * Need to render displays when the sim is paused and initialized, because the 
   *   display won't render with Display2D.render() if it is hidden.
   */
  public void forceRender(){
  	synchronized (lock) {
      ThreadUtilities.runInEventThread(renderer);
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
  }

  public JPanel getPanel() {
    if (panel == null) {
      createPanel();
      JScrollPane scroller = new JScrollPane(canvas);
      panel.setLayout(new BorderLayout());
      panel.add(scroller, BorderLayout.CENTER);
    }
    return panel;
  }

  @Override
  protected void addObject(Object o) {
    IDisplayLayer layer = findLayer(o);
    if (layer != null){
      layer.addObject(o);
      layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.ADDED);
    }
  }

  // @Override
  // protected void addEdge(RepastEdge edge, Network network) {
  // NetworkDisplayLayer2D layer = networkLayerMap.get(network);
  // if (layer == null) {
  // registerNetworkStyle(network, new DefaultEdgeStyle2D());
  // layer = networkLayerMap.get(network);
  // }
  // layer.addEdge(edge);
  // }

  @Override
  protected void moveObject(Object o) {
    layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.MOVED);
  }
  
  protected IDisplayLayer2D findLayer(Object obj) {
    Class<? extends Object> objClass = obj.getClass();
    IDisplayLayer2D layer = classStyleMap.get(objClass);
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

  @Override
  protected void removeObject(Object o) {
    IDisplayLayer2D layer = findLayer(o);
    if (layer != null) {
      layer.removeObject(o);
      layoutUpdater.addTriggerCondition(LayoutUpdater.Condition.REMOVED);
    }
  }

  // @Override
  // protected void removeEdge(RepastEdge edge, Network network) {
  // NetworkDisplayLayer2D layer = networkLayerMap.get(network);
  // if (layer == null) {
  // throw new IllegalArgumentException("NetworkLayer2D for " +
  // network.getName() + " not found");
  // }
  // layer.removeEdge(edge);
  // }

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

  /**
   * The update method is called according to the frequency in the display
   *  descriptor.  First the layoutUpdater.update() is called to calculate the
   *  new positions for objects, then the doRender flag is set so that the next
   *  render() call renders the updates, then the updater runnable is executed
   *  in the event dispatch thread.
   */
  public void update() {
    synchronized (lock) {
    	layoutUpdater.update();
    	doRender = true;
      ThreadUtilities.runInEventThread(updater);
    }
  }

  public PNode getVisualItem(Object o) {
    IDisplayLayer2D layer = findLayer(o);
    PNode pNode = null;
    if (layer != null) {
      pNode = layer.getVisualItem(o);
    }

    if (pNode == null) {
      // try the net layer
      for (IDisplayLayer2D netLayer : objectStyleMap.values()) {
        pNode = netLayer.getVisualItem(o);
        if (pNode != null) break;
      }
    }
    return pNode;
  }

  public void windowActivated(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void windowClosed(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void windowClosing(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void windowDeactivated(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void windowDeiconified(WindowEvent e) {
    iconified = false;
  }

  public void windowIconified(WindowEvent e) {
    iconified = true;
  }

  public void windowOpened(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  public void setPause(boolean pause) {
  	this.pause = pause;
  	
  	if (pause)
  	  forceRender();
  }

  /**
   * Notifies this IDisplay that its associated gui widget has been iconified.
   */
  public void iconified() {
    // todo implement
  }

  /**
   * Notifies this IDisplay that its associated gui widget has been closed.
   */
  public void closed() {
  }

  /**
   * Notifies this IDisplay that its associated gui widget has been
   * deIconified.
   */
  public void deIconified() {
    // todo implement method
  }

  /**
   * Gets the layout.
   *
   * @return the layout.
   */
  public Layout getLayout() {
    return layout;
  }


  /**
   * Gets the classes for which styles have been registered.
   *
   * @return the classes for which styles have been registered.
   */
  public List<Class> getRegisteredClasses() {
    return new ArrayList<Class>(classStyleMap.keySet());
  }

  /**
   * Creates an DisplayEditor appropriate for editing this display.
   *
   * @return an DisplayEditor appropriate for editing this display or null if
   *         this display cannot be edited.
   */
  public DisplayEditorLifecycle createEditor(JPanel panel) {
    return EditorFactory.getInstance().create2DEditor(this, canvas, panel);
  }

  public void resetHomeView() {
    canvas.resetHomeView();
  }
  
  public void setBackgroundColor(Color color){
  	if (color != null)
  	  canvas.setBackground(color);
  }

  @Override
  public void registerToolBar(JToolBar bar) {
  	// The info probe is only implemented for Display2D at the moment, so we 
  	// add the button to the toolbar here instead of in RSGui.
  	JToggleButton infoButton = new JToggleButton();
    infoButton.setAction(new AbstractAction(){
    	 public void actionPerformed(ActionEvent e) {
         toggleInfoProbe();
     }
    });
    infoButton.setText(null);
    infoButton.setIcon(VIZ_INFO_ICON);
    infoButton.setToolTipText("Toggle Info Probe");
    bar.add(infoButton);
  }
  
	public void toggleInfoProbe() {
		if (showHoverProbes == false){
			showHoverProbes = true;
		  canvas.setCursor(INFO_CURSOR);
		}
		else{
		  showHoverProbes = false;
		  canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
