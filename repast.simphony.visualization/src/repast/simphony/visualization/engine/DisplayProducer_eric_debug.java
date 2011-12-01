package repast.simphony.visualization.engine;
//package repast.simphony.visualization.engine;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.StringReader;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.geotools.data.FeatureSource;
//import org.geotools.data.shapefile.ShapefileDataStore;
//import org.geotools.styling.SLDParser;
//import org.geotools.styling.Style;
//import org.geotools.styling.StyleFactoryImpl;
//
//import repast.simphony.context.Context;
//import repast.simphony.engine.environment.RunState;
//import repast.simphony.score.ProjectionData;
//import repast.simphony.score.ProjectionType;
//import repast.simphony.space.graph.Network;
//import repast.simphony.space.projection.Projection;
//import repast.simphony.valueLayer.ValueLayer;
//import repast.simphony.visualization.DefaultDisplayData;
//import repast.simphony.visualization.IDisplay;
//import repast.simphony.visualization.Layout;
//import repast.simphony.visualization.NullLayout;
//import repast.simphony.visualization.decorator.ProjectionDecorator2D;
//import repast.simphony.visualization.decorator.ProjectionDecorator3D;
//import repast.simphony.visualization.editedStyle.EditedEdgeStyle2D;
//import repast.simphony.visualization.editedStyle.EditedEdgeStyle3D;
//import repast.simphony.visualization.editedStyle.EditedStyle2D;
//import repast.simphony.visualization.editedStyle.EditedStyle3D;
//import repast.simphony.visualization.editedStyle.EditedValueLayerStyle2D;
//import repast.simphony.visualization.editedStyle.EditedValueLayerStyle3D;
//import repast.simphony.visualization.gis.DisplayGIS;
//import repast.simphony.visualization.gisnew.NewDisplayGIS;
//import repast.simphony.visualization.gisnew.StyleGIS;
//import repast.simphony.visualization.visualization2D.Display2D;
//import repast.simphony.visualization.visualization2D.style.EdgeStyle2D;
//import repast.simphony.visualization.visualization2D.style.Style2D;
//import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;
//import repast.simphony.visualization.visualization3D.Display3D;
//import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
//import repast.simphony.visualization.visualization3D.style.Style3D;
//import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;
//import simphony.util.messages.MessageCenter;
//
///**
// * Produces an IDisplay from a DisplayDescriptor.
// *
// * @author Nick Collier
// * @author Eric Tatara
// */
//public class DisplayProducer {
//
//	private static final MessageCenter msg = MessageCenter.getMessageCenter(DisplayProducer.class);
//	
//  private DisplayDescriptor displayDescriptor;
//
//  private Context<?> context;
//
//  public DisplayProducer(Object contextID, RunState runState, DisplayDescriptor displayDescriptor) {
//    this.displayDescriptor = displayDescriptor;
//    Context masterContext = runState.getMasterContext();
//    setContext(masterContext, contextID);
//  }
//
//  private void setContext(Context<?> context, Object contextID) {
//    if (context.getId().equals(contextID)) {
//      this.context = context;
//    } else {
//      for (Context child : context.getSubContexts()) {
//        setContext(child, contextID);
//      }
//    }
//  }
//
//  public IDisplay createDisplay() throws IllegalAccessException, InvocationTargetException,
//      InstantiationException, ClassNotFoundException, IOException {
//    if (displayDescriptor.getDisplayType() == DisplayDescriptor.DisplayType.TWO_D) 
//      return createDisplay2D();
//     else if (displayDescriptor.getDisplayType() == DisplayDescriptor.DisplayType.THREE_D) 
//      return createDisplay3D();
//     else if (displayDescriptor.getDisplayType() == DisplayDescriptor.DisplayType.GIS)
//      return createDisplayGIS();
//     else
//    	 return createDisplayGIS3d();
//  }
//
//  private NewDisplayGIS createDisplayGIS3d() throws ClassNotFoundException,
//  IllegalAccessException, InstantiationException {
//  	DefaultDisplayData data = createDisplayData();
//  	Layout layout = null;
//  	
//  	// TODO
////  	if (data.getProjectionCount() > 0) {
////  		String layoutProj = displayDescriptor.getLayoutProjection();
//    // ProjectionDescriptor projDesc =
//    // displayDescriptor.getProjectionDescriptor(layoutProj);
////  		String layoutClassName = projDesc.getImpliedLayout3D();
////  		layout = createLayout(layoutClassName);
////  	} else
//  		layout = new NullLayout();
//
//  		NewDisplayGIS display = new NewDisplayGIS(data, layout);
//  	
//  	registerStyles(display);
//  	registerNetworkStyles(display);
////  	registerProjectionDecorators(display);
////  	registerValueLayerStyles(display);
//    display.setLayoutFrequency(displayDescriptor.getLayoutFrqeuency(), displayDescriptor
//	.getLayoutInterval());
//  	return display;
//  }
//  
//  private DisplayGIS createDisplayGIS() throws IllegalAccessException, InstantiationException,
//      ClassNotFoundException, IOException {
//
//    DefaultDisplayData data = createDisplayData();
//  
//    // TODO assume that network projections are defined in descriptor, so
//    //      don't automatically add all.  Remove below code?
//    
//    // gis displays can act in concert with networks
//    // at least in the editor so add the networks here as
//    // well
////    for (Projection proj : context.getProjections()) {
////      if (proj instanceof Network) {
////        data.addProjection(proj.getName());
////      }
////    }
//     
//    DisplayGIS display = new DisplayGIS(data);
//
//    for (String agentName : displayDescriptor.agentClassStyleNames()) {
//      String styleXML = displayDescriptor.getStyleClassName(agentName);
//      SLDParser parser = new SLDParser(new StyleFactoryImpl(), new StringReader(styleXML));
//      display.registerAgentStyle(agentName, parser.readXML()[0], displayDescriptor
//	  .getLayerOrder(agentName));
//    }
//
//    Map<String, String> shpMap = (Map<String, String>) displayDescriptor
//	.getProperty(DisplayGIS.SHP_FILE_STYLE_PROP);
//    if (shpMap != null) {
//      for (Map.Entry<String, String> entry : shpMap.entrySet()) {
//        File file = new File(entry.getKey());
//        FeatureSource source = createFeatureSource(file);
//        Style style = null;
//        if (entry.getValue().length() > 0) {
//	  SLDParser parser = new SLDParser(new StyleFactoryImpl(), new StringReader(entry
//	      .getValue()));
//          style = parser.readXML()[0];
//        }
//	display.registerFeatureSource(source, style, displayDescriptor
//	    .getLayerOrder(entry.getKey()));
//      }
//    }
//    
////    for (SProjection proj : displayDescriptor.getProjections()) {
////      if (proj instanceof SNetwork) {
////      	Network network = context.getProjection(Network.class, proj.getLabel());
////      	
//    // String styleXML =
//    // displayDescriptor.getNetworkStyleClassName(proj.getLabel());
//    // SLDParser parser = new SLDParser(new StyleFactoryImpl(), new
//    // StringReader(styleXML));
////        display.registerNetworkStyle(network, parser.readXML()[0]);
////      }
////    }
//    
//    return display;
//  }
//
//  private FeatureSource createFeatureSource(File shpFile) throws IOException {
//    ShapefileDataStore dataStore = new ShapefileDataStore(shpFile.toURL());
//    return dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
//  }
//
//  private Display2D createDisplay2D() throws IllegalAccessException, InstantiationException,
//      ClassNotFoundException {
//    DefaultDisplayData data = createDisplayData();
//    Layout layout = null;
//    if (data.getProjectionCount() > 0) {
//      String layoutProj = displayDescriptor.getLayoutProjection();
//      ProjectionDescriptor projDesc = displayDescriptor.getProjectionDescriptor(layoutProj);
//      String layoutClassName = projDesc.getImpliedLayout2D();
//      layout = createLayout(layoutClassName);
//    } else
//      layout = new NullLayout();
//    Display2D display = new Display2D(data, layout);
//    registerValueLayerStyles(display); // do first so vl at back
//    registerStyles(display);
//    registerNetworkStyles(display);
//    registerProjectionDecorators(display);
//    display.setLayoutFrequency(displayDescriptor.getLayoutFrqeuency(), displayDescriptor
//	.getLayoutInterval());
//    display.setBackgroundColor(displayDescriptor.getBackgroundColor());
//    return display;
//  }
//
//  private Display3D createDisplay3D() throws ClassNotFoundException, IllegalAccessException,
//      InstantiationException {
//    DefaultDisplayData data = createDisplayData();
//    Layout layout = null;
//    if (data.getProjectionCount() > 0) {
//      String layoutProj = displayDescriptor.getLayoutProjection();
//      ProjectionDescriptor projDesc = displayDescriptor.getProjectionDescriptor(layoutProj);
//      String layoutClassName = projDesc.getImpliedLayout3D();
//      layout = createLayout(layoutClassName);
//    } else
//      layout = new NullLayout();
//
//    Display3D display = new Display3D(data, layout);
//    registerStyles(display);
//    registerNetworkStyles(display);
//    registerProjectionDecorators(display);
//    registerValueLayerStyles(display);
//    display.setLayoutFrequency(displayDescriptor.getLayoutFrqeuency(), displayDescriptor
//	.getLayoutInterval());
//    display.setBackgroundColor(displayDescriptor.getBackgroundColor());
//    return display;
//  }
//
//  private DefaultDisplayData createDisplayData() {
//    DefaultDisplayData data = new DefaultDisplayData(context);
//    for (ProjectionData pData : displayDescriptor.getProjections()) {
//      data.addProjection(pData.getId());
//    }
//
//    for (String vlName : displayDescriptor.getValueLayerNames()) {
//      data.addValueLayer(vlName);
//    }
//    return data;
//  }
//
//  /*
//   * private void registerProjectionStyles(Display2D display) throws
//   * ClassNotFoundException, InstantiationException, IllegalAccessException {
//   * for (ProjectionData data : displayDescriptor.getProjections()) { String
//   * decoName = displayDescriptor.getDecoratorClassName(data.getName()); if
//   * (decoName != null) { Class styleClass = Class.forName(decoName, true,
//   * this.getClass() .getClassLoader()); //ProjectionStyle2D style =
//   * (ProjectionStyle2D) styleClass.newInstance(); //Projection projection =
//   * context.getProjection(data.getName());
//   * //display.registerProjectionStyle(style, projection.getName()); } } }
//    */
//
//  private void registerProjectionDecorators(Display3D display) {
//    for (ProjectionDescriptor projDesc : displayDescriptor.getProjectionDescriptors()) {
//      String name = projDesc.getProjectionName();
//      for (ProjectionDecorator3D deco : projDesc.get3DDecorators()) {
//        Projection projection = context.getProjection(name);
//        deco.setProjection(projection);
//        display.registerDecorator(deco);
//      }
//    }
//  }
//
//  private void registerProjectionDecorators(Display2D display) {
//    for (ProjectionDescriptor projDesc : displayDescriptor.getProjectionDescriptors()) {
//      String name = projDesc.getProjectionName();
//      for (ProjectionDecorator2D deco : projDesc.get2DDecorators()) {
//        Projection projection = context.getProjection(name);
//        deco.setProjection(projection);
//        display.registerDecorator(deco);
//      }
//    }
//  }
//
//  private void registerNetworkStyles(Display2D display) throws ClassNotFoundException,
//      IllegalAccessException, InstantiationException {
//    for (ProjectionData proj : displayDescriptor.getProjections()) {
//      if (proj.getType() == ProjectionType.NETWORK) {
//	String netStyleName = displayDescriptor.getNetworkStyleClassName(proj.getId());
//	String netEditedStyleName = displayDescriptor.getNetworkEditedStyleName(proj.getId());
//
//	Network network = context.getProjection(Network.class, proj.getId());
//
//        // Style editor references get priority over explicit style classes if
//        // both are specified in descriptor
//        if (netEditedStyleName != null) {
//          EdgeStyle2D style = new EditedEdgeStyle2D(netEditedStyleName);
//          display.registerNetworkStyle(network, style);
//        } else if (netStyleName != null) {
//	  Class styleClass = Class.forName(netStyleName, true, this.getClass().getClassLoader());
//
//          EdgeStyle2D style = (EdgeStyle2D) styleClass.newInstance();
//
//          display.registerNetworkStyle(network, style);
//        }
//      }
//    }
//  }
//
//  private void registerValueLayerStyles(Display2D display) throws ClassNotFoundException,
//      IllegalAccessException, InstantiationException {
//
//    String vlStyleName = displayDescriptor.getValueLayerStyleName();
//    String vlEditedStyleName = displayDescriptor.getValueLayerEditedStyleName();
//    
//    ValueLayerStyle style = null;
//    
//    if (vlEditedStyleName != null) 
//    	style = new EditedValueLayerStyle2D(vlEditedStyleName);
//    	
//    else if (vlStyleName != null) {
//      Class styleClass = Class.forName(vlStyleName, true, this.getClass().getClassLoader());
//      style = (ValueLayerStyle) styleClass.newInstance();
//    }
//
//    if (style == null)
//    	return;
//    
//    // only supports one value layer in display
//    String vlLayer = displayDescriptor.getValueLayerNames().iterator().next();
//    ValueLayer layer = context.getValueLayer(vlLayer);
//    style.addValueLayer(layer);
//    display.registerValueLayerStyle(style);
//  }
//
//  private void registerValueLayerStyles(Display3D display) throws ClassNotFoundException,
//      IllegalAccessException, InstantiationException {
//
//    String vlStyleName = displayDescriptor.getValueLayerStyleName();
//    String vlEditedStyleName = displayDescriptor.getValueLayerEditedStyleName();
//    
//    ValueLayerStyle3D style = null;
//    
//    if (vlEditedStyleName != null) 
//    	style = new EditedValueLayerStyle3D(vlEditedStyleName);
//    
//    else if (vlStyleName != null) {
//      Class styleClass = Class.forName(vlStyleName, true, this.getClass().getClassLoader());
//    	style = (ValueLayerStyle3D) styleClass.newInstance();
//    }
//    
//    if (style == null)
//    	return;
//    
//    // only supports one value layer in display
//    String vlLayer = displayDescriptor.getValueLayerNames().iterator().next();
//    ValueLayer layer = context.getValueLayer(vlLayer);
//    style.addValueLayer(layer);
//    display.registerValueLayerStyle(style);
//  }
//
//  private void registerNetworkStyles(Display3D display) throws ClassNotFoundException,
//      IllegalAccessException, InstantiationException {
//    //for (Network network : context.getProjections(Network.class)) {
//    for (ProjectionData proj : displayDescriptor.getProjections()) {
//      if (proj.getType() == ProjectionType.NETWORK) {
//	String netStyleName = displayDescriptor.getNetworkStyleClassName(proj.getId());
//	String netEditedStyleName = displayDescriptor.getNetworkEditedStyleName(proj.getId());
//
//	Network network = context.getProjection(Network.class, proj.getId());
//
//        // Style editor references get priority over explicit style classes if
//        // both are specified in descriptor
//        if (netEditedStyleName != null) {
//          EdgeStyle3D style = new EditedEdgeStyle3D(netEditedStyleName);
//          display.registerNetworkStyle(network, style);
//        } else if (netStyleName != null) {
//	  Class styleClass = Class.forName(netStyleName, true, this.getClass().getClassLoader());
//          EdgeStyle3D style = (EdgeStyle3D) styleClass.newInstance();
//
//          display.registerNetworkStyle(network, style);
//        }
//      }
//    }
//  }
//  
//  private void registerNetworkStyles(NewDisplayGIS display) throws ClassNotFoundException, 
//  IllegalAccessException, InstantiationException {
////  	for (SProjection proj : displayDescriptor.getProjections()) {
////  		if (proj instanceof SNetwork) {
////  			String netStyleName = displayDescriptor.getNetworkStyleClassName(proj.getLabel());
////  			String netEditedStyleName = displayDescriptor.getNetworkEditedStyleName(proj.getLabel());
////
////  			Network network = context.getProjection(Network.class, proj.getLabel());
////
////        // Style editor references get priority over explicit style classes if
////        // both are specified in descriptor
////  			if (netEditedStyleName != null) {
////  				EdgeStyleGIS3D style = new EditedEdgeStyleGIS3D(netEditedStyleName);
////  				display.registerNetworkStyle(network, style);
////  			} else if (netStyleName != null) {
////  				Class styleClass = Class.forName(netStyleName, true, this
////  						.getClass().getClassLoader());
////  				EdgeStyleGIS3D style = (EdgeStyleGIS3D) styleClass.newInstance();
////
////  				display.registerNetworkStyle(network, style);
////  			}
////  		}
////  	}
//  }
//
//  private void registerStyles(Display2D display) throws IllegalAccessException,
//      InstantiationException, ClassNotFoundException {
//
//    TreeMap<Integer, String> map = new TreeMap<Integer, String>();
//
//    List<String> unsorted = new ArrayList<String>();
//
//    // Styles need to be registered in the order in which they
//    // are to be displayed, with 0 being the lowest (background).
//
//    // First check available layer order info and add to map
//    for (String agentName : displayDescriptor.agentClassStyleNames()) {
//      Integer layerOrder = displayDescriptor.getLayerOrder(agentName);
//      if (layerOrder != null)
//        map.put(layerOrder, agentName);
//      else
//        unsorted.add(agentName);  // if no order info for layer,
//    }
//
//    // Add the layers with no order info to the end of the map
//    int index;
//    if (map.size() > 0)
//      index = map.lastKey() + 1;
//    else
//      index = 0;
//    for (String s : unsorted) {
//      map.put(index, s);
//      index++;
//    }
//
//    // Iterate through layers in order and register
//    for (String agentName : map.values()) {
//      String styleName = displayDescriptor.getStyleClassName(agentName);
//      String editedStyleName = displayDescriptor.getEditedStyleName(agentName);
//
//      Class agentClass;
//      
//      try{
//	agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());
//      
//      // Style editor references get priority over explicit style classes if
//      // both are specified in descriptor
//      if (editedStyleName != null) {
//        Style2D style = new EditedStyle2D(editedStyleName);
//        display.registerStyle(agentClass, style);
//      } else if (styleName != null) {
//	  Class styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
//
//        Style2D style = (Style2D) styleClass.newInstance();
//        display.registerStyle(agentClass, style);
//      }
//      
//      } catch (ClassNotFoundException e) {
//      	msg.error("Error while creating displays", e);
//      }
//    }
//  }
//
//  private void registerStyles(Display3D display) throws IllegalAccessException,
//      InstantiationException, ClassNotFoundException {
//    for (String agentName : displayDescriptor.agentClassStyleNames()) {
//      String styleName = displayDescriptor.getStyleClassName(agentName);
//      String editedStyleName = displayDescriptor.getEditedStyleName(agentName);
//
//      Class agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());
//
//      // Style editor references get priority over explicit style classes if
//      // both are specified in descriptor
//      if (editedStyleName != null) {
//        Style3D style = new EditedStyle3D(editedStyleName);
//        display.registerStyle(agentClass, style);
//      } else if (styleName != null) {
//	Class styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
//
//        Style3D style = (Style3D) styleClass.newInstance();
//        display.registerStyle(agentClass, style);
//      }
//    }
//  }
//  
//  private void registerStyles(NewDisplayGIS display)
//  throws IllegalAccessException, InstantiationException,
//  ClassNotFoundException {
//  	for (String agentName : displayDescriptor.agentClassStyleNames()) {
//  		String styleName = displayDescriptor.getStyleClassName(agentName);
//  		String editedStyleName = displayDescriptor.getEditedStyleName(agentName);
//
//      Class agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());
//
////		Style editor references get priority over explicit style classes if
////		both are specified in descriptor
//  		if (editedStyleName != null) {
////  			StyleGIS style = new EditedStyleGIS3D(editedStyleName);
//// 			display.registerStyle(agentClass, style);
//  		} else if (styleName != null) {
//	Class styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
//
//  			StyleGIS style = (StyleGIS) styleClass.newInstance();
//  			display.registerStyle(agentClass, style);
//  		}
//  	}
//  }
//
//  private Layout createLayout(String layoutClassName) throws ClassNotFoundException,
//      IllegalAccessException, InstantiationException {
//    // get the layout class, checking if the projection to base
//    // the layout on has an implied layout (i.e. like grid) that
//    // overrides the layout in the display descriptor.
//    if (layoutClassName == null || layoutClassName.length() == 0) {
//      layoutClassName = displayDescriptor.getLayoutClassName();
//    }
//
//    Class clazz = Class.forName(layoutClassName, true, this.getClass().getClassLoader());
//    if (!Layout.class.isAssignableFrom(clazz)) {
//      // todo better errors.
//      throw new IllegalArgumentException("Layout class must implements Layout interface.");
//    }
//    Layout layout = (Layout) clazz.newInstance();
//    String layoutProj = displayDescriptor.getLayoutProjection();
//    Projection<?> projection = context.getProjection(layoutProj);
//    if (projection == null) {
//      throw new RuntimeException("Projection '" + layoutProj + "' not found.");
//    }
//    layout.setProjection(projection);
//    layout.setLayoutProperties(displayDescriptor.getLayoutProperties());
//    return layout;
//  }
//}

