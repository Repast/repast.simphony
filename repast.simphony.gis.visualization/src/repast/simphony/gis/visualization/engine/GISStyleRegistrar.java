package repast.simphony.gis.visualization.engine;

import java.lang.reflect.InvocationTargetException;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.graph.Network;
import repast.simphony.visualization.gis3D.DisplayGIS3D;
import repast.simphony.visualization.gis3D.style.CoverageStyle;
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
 * @author Eric Tatara
 */
public class GISStyleRegistrar {

	protected DisplayGIS3D display;
	
	public GISStyleRegistrar(DisplayGIS3D display) {
		this.display = display;
	}

	public void registerAllStyles(GISDisplayDescriptor descriptor, Context<?> context) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		// TODO GIS register all styles looking at layer orders etc
	
		registerAgentStyles(descriptor);
		registerNetworkStyles(descriptor, context);
		registerCoverageStyles(descriptor);
	}
	
  public void registerAgentStyles(GISDisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, 
      SecurityException {
    
    // Iterate through layers in order and register
    for (String agentName : descriptor.agentClassStyleNames()) {
    	
    	// The Style class name (e.g. org.MyStyle3D) for the agent class.
    	String styleName = descriptor.getStyleClassName(agentName);  
     
    	// The agent class for this style, e.g. org.MyAgent
      Class<?> agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());

      // The actual style class is either an edted style class...
      StyleGIS<?> style = null;
      
      Class<? extends StyleGIS<?>> styleClass = (Class<? extends StyleGIS<?>>)Class.forName(styleName, 
      		true, this.getClass().getClassLoader());
      
      if (EditedMarkStyle.class.isAssignableFrom(styleClass) ||
      		EditedSurfaceShapeStyle.class.isAssignableFrom(styleClass)) {
      	
      	String editedStyleName = descriptor.getEditedStyleName(agentName);
      	
      	// Construct the edited style with the input XML style name arg
      	style = styleClass.getConstructor(String.class).newInstance(editedStyleName);
      	
      }

      // ...or the style class is a user-defined class in the project classpath
      else  {
      	// Construct the no-arg user style class.
      	style = styleClass.getConstructor().newInstance();
      }
      
      if (style != null) {
      	display.registerStyle(agentClass, style);
      }
    }
  }
  
  public void registerNetworkStyles(GISDisplayDescriptor descriptor, Context<?> context)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, 
      SecurityException{
  
    for (ProjectionData proj : descriptor.getProjections()) {
      if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {
      	Network<?> network = context.getProjection(Network.class, proj.getId());
        String styleName = descriptor.getNetworkStyleClassName(proj.getId());
  
        // The actual style class is either an edted style class...
        NetworkStyleGIS style = null;
        
        Class<? extends NetworkStyleGIS> styleClass = (Class<? extends NetworkStyleGIS>)Class.forName(styleName, 
        		true, this.getClass().getClassLoader());
        
        if (EditedNetworkStyleGIS.class.isAssignableFrom(styleClass)) {
        	String netEditedStyleName = descriptor.getNetworkEditedStyleName(proj.getId());
        	
        	// Construct the edited style with the input XML style name arg
        	style = styleClass.getConstructor(String.class).newInstance(netEditedStyleName);
        }
        
        // ...or the style class is a user-defined class in the project classpath
        else {
        	// Construct the no-arg user style class.
        	style = styleClass.getConstructor().newInstance();
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
      	style = null;
      } 
      else if (styleName != null) {
        Class<?> styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
        style = (CoverageStyle<?>) styleClass.newInstance();
      }
      
      display.registerCoverageStyle(coverageName, style);
    }
  }
  

}
