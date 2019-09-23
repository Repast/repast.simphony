package repast.simphony.ws.gis;

import repast.simphony.context.Context;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.graph.Network;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.gis3D.style.EditedMarkStyle;
import repast.simphony.visualization.gis3D.style.EditedNetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.EditedSurfaceShapeStyle;
import repast.simphony.visualization.gis3D.style.NetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.StyleGIS;


/**
 * Creates GIS3D styles from a style classname.  The GIS registrar implementation
 *   is separate from the r.s.visualization.engine.StyleRegistrar because GIS
 *   displays can handle more complex ordering of the various layer types
 * 
 *  TODO this is exactly the same as the RS GISStyleRegistrar except that the
 *       display class is different, so this should be refactored to avoid duplication.
 * 
 * @author Eric Tatara
 */
public class DisplayServerGISStyleRegistrar {

	protected DisplayServerGIS display;
	
	public DisplayServerGISStyleRegistrar(DisplayServerGIS display) {
		this.display = display;
	}

	public void registerAllStyles(GISDisplayDescriptor descriptor, Context<?> context) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		registerAgentStyles(descriptor);
		registerNetworkStyles(descriptor, context);
//		registerCoverageStyles(descriptor);
	}
	
  public void registerAgentStyles(GISDisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
  	
    // Iterate through layers in order and register
    for (String agentName : descriptor.agentClassStyleNames()) {
    	
    	// The Style class name (e.g. org.MyStyle3D) for the agent class.
    	String styleName = descriptor.getStyleClassName(agentName);  
     
    	// The agent class for this style, e.g. org.MyAgent
      Class<?> agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());

      // TODO perhaps this is not the best way to get the edited style data since
      //      it may try to load classes with jogl depedencies.  Previously,
      //      the registrar would just check for the edited style name.
      
      Class<? extends StyleGIS<?>> styleClass = (Class<? extends StyleGIS<?>>)Class.forName(styleName, 
      		true, this.getClass().getClassLoader());
      
      EditedStyleData<Object> data = null;
      
      if (EditedMarkStyle.class.isAssignableFrom(styleClass) ||
      		EditedSurfaceShapeStyle.class.isAssignableFrom(styleClass)) {
      	
      	String editedStyleName = descriptor.getEditedStyleName(agentName);
      	
      	data = EditedStyleUtils.getStyle(editedStyleName);    	
      }
    	
      else {
      	// TODO support other style class?
      	// TODO Handle unsupported style types
      }
       
      if (data != null) {	
      	ServerStyleGIS style = new ServerStyleGIS(data);
      	display.registerStyle(agentClass, style);
      }
    }
  }
  
  public void registerNetworkStyles(GISDisplayDescriptor descriptor, Context<?> context)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
  
    for (ProjectionData proj : descriptor.getProjections()) {
      if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {
      	Network<?> network = context.getProjection(Network.class, proj.getId());
        String styleName = descriptor.getNetworkStyleClassName(proj.getId());
  
        Class<? extends NetworkStyleGIS> styleClass = (Class<? extends NetworkStyleGIS>)Class.forName(styleName, 
        		true, this.getClass().getClassLoader());
        
        EditedEdgeStyleData<Object> data = null;
        
        if (EditedNetworkStyleGIS.class.isAssignableFrom(styleClass)) {
        	String netEditedStyleName = descriptor.getNetworkEditedStyleName(proj.getId());
        
        	data = EditedStyleUtils.getEdgeStyle(netEditedStyleName);
        }
            
        else  {
        	// TODO support other style class?
        	// TODO Handle unsupported style types
        }
        
        if (data != null) {
        	ServerNetStyleGIS style = new ServerNetStyleGIS(data);
        	display.registerNetworkStyle(network, style);
        }
      }
    }
  }
  
//  public void registerCoverageStyles(GISDisplayDescriptor descriptor)
//      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//    
//  	 // Iterate through layers in order and register
//    for (String coverageName : descriptor.getCoverageLayers().keySet()) {
//    	String styleName = descriptor.getCoverageLayers().get(coverageName);
//    	
//    	// TODO GIS coverage edited style
//      String editedStyleName = null;
////      editedStyleName = descriptor.getEditedStyleName(agentName);
//
//      CoverageStyle<?> style = null;
//      // Style editor references get priority over explicit style classes if
//      // both are specified in descriptor
//      if (editedStyleName != null) {
//      	style = createCoverageEditedStyle(editedStyleName);
//      } 
//      else if (styleName != null) {
//        Class<?> styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
//        style = (CoverageStyle<?>) styleClass.newInstance();
//      }
//      
////      display.registerCoverageStyle(coverageName, style);
//    }
//  }
  
  
//  protected StyleGIS<?> createAgentEditedStyle(String editedStyleName) {
//  	
//  	// TODO GIS provided edited style 
//  	
//  	return null;
//  }
//  
//  protected CoverageStyle<?> createCoverageEditedStyle(String editedStyleName) {
//  	
//  	// TODO GIS provided edited style 
//  	
//  	return null;
//  }
//  
//  protected NetworkStyleGIS createdNetworkEditedStyle(String editedStyleName) {
//  	
//  	// TODO GIS provided edited style 
//  	
//  	return null;
//  }
}
