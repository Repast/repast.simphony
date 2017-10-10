package repast.simphony.gis.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.graph.Network;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gis3D.DisplayGIS3D;
import repast.simphony.visualization.gis3D.style.CoverageStyle;
import repast.simphony.visualization.gis3D.style.NetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.StyleGIS;


/**
 * Creates GIS3D styles from a style classname.  The GIS registrar implementation
 *   is separate from the r.s.visualization.engine.StyleRegistrar because GIS
 *   displays can handle more complex ordering of the various layer types
 * 
 * @author Eric Tatara
 */
public class GISStyleRegistrar {

	protected DisplayGIS3D display;
	
	public GISStyleRegistrar(DisplayGIS3D display) {
		this.display = display;
	}

	public void registerAllStyles(GISDisplayDescriptor descriptor, Context<?> context) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		// TODO GIS register all styles looking at layer orders etc
	
		registerAgentStyles(descriptor);
		registerNetworkStyles(descriptor, context);
		registerCoverageStyles(descriptor);
	}
	
  public void registerAgentStyles(GISDisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
    // Iterate through layers in order and register
    for (String agentName : descriptor.agentClassStyleNames()) {
      
    	String styleName = descriptor.getStyleClassName(agentName);
      
    	 // TODO GIS agent edited style
//    	String editedStyleName = descriptor.getEditedStyleName(agentName);
    	String editedStyleName = null;
  
      Class<?> agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());

      StyleGIS<?> style = null;
      // Style editor references get priority over explicit style classes if
      // both are specified in descriptor
      if (editedStyleName != null) {
      	style = createAgentEditedStyle(editedStyleName);
      } 
      else if (styleName != null) {
        Class<?> styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
        style = (StyleGIS<?>) styleClass.newInstance();
      }
      
      if (style != null) {
      	display.registerStyle(agentClass, style);
      }
    }
  }
  
  public void registerNetworkStyles(GISDisplayDescriptor descriptor, Context<?> context)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
  
    for (ProjectionData proj : descriptor.getProjections()) {
      if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {
        String styleName = descriptor.getNetworkStyleClassName(proj.getId());
  
        // TODO GIS network edited style
//        String netEditedStyleName = descriptor.getNetworkEditedStyleName(proj.getId());
        String netEditedStyleName = null;
   
        Network<?> network = context.getProjection(Network.class, proj.getId());
 
        NetworkStyleGIS style = null;
        // Style editor references get priority over explicit style classes if
        // both are specified in descriptor
        if (netEditedStyleName != null) {
        	style = createdNetworkEditedStyle(netEditedStyleName);
        } 
        else if (styleName != null) {
          Class<?> styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
          style = (NetworkStyleGIS) styleClass.newInstance();
        }
        
        if (style != null) {
        	display.registerNetworkStyle(network, style);
        }
      }
    }
  }
  
  public void registerCoverageStyles(GISDisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
  	 // Iterate through layers in order and register
    for (String coverageName : descriptor.getCoverageLayers().keySet()) {
    	String styleName = descriptor.getCoverageLayers().get(coverageName);
    	
    	// TODO GIS coverage edited style
      String editedStyleName = null;
//      editedStyleName = descriptor.getEditedStyleName(agentName);

      CoverageStyle<?> style = null;
      // Style editor references get priority over explicit style classes if
      // both are specified in descriptor
      if (editedStyleName != null) {
      	style = createCoverageEditedStyle(editedStyleName);
      } 
      else if (styleName != null) {
        Class<?> styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
        style = (CoverageStyle<?>) styleClass.newInstance();
      }
      
      display.registerCoverageStyle(coverageName, style);
    }
  }
  
  
  protected StyleGIS<?> createAgentEditedStyle(String editedStyleName) {
  	
  	// TODO GIS provided edited style 
  	
  	return null;
  }
  
  protected CoverageStyle<?> createCoverageEditedStyle(String editedStyleName) {
  	
  	// TODO GIS provided edited style 
  	
  	return null;
  }
  
  protected NetworkStyleGIS createdNetworkEditedStyle(String editedStyleName) {
  	
  	// TODO GIS provided edited style 
  	
  	return null;
  }
}
