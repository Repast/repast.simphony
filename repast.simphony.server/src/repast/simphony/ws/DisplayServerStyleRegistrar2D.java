package repast.simphony.ws;

import java.util.Collection;

import repast.simphony.context.Context;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.StyleRegistrar;
import repast.simphony.visualization.engine.StyleRegistrar.Registrar;



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
public class DisplayServerStyleRegistrar2D {

  public void registerStyles(Registrar<ServerStyle2D> registrar, DisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
  	Collection<String> agentNames = StyleRegistrar.getOrderedAgentCollection(descriptor);
    
    registerStyles(registrar, descriptor, agentNames);
  }
	
  public void registerStyles(Registrar<ServerStyle2D> registrar, 
  		DisplayDescriptor descriptor, Collection<String> agentNames)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
  	
    // Iterate through layers in order and register
    for (String agentName : agentNames) { 	
    	String styleName = descriptor.getStyleClassName(agentName);  
    	String editedStyleName = descriptor.getEditedStyleName(agentName);
    	
    	// The agent class for this style, e.g. org.MyAgent
      Class<?> agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());

      EditedStyleData<Object> data = null;
      
      // Style editor references get priority over explicit style classes if
      // both are specified in descriptor
      if (editedStyleName != null) {
      	data = EditedStyleUtils.getStyle(editedStyleName);   
      	
      	if (data != null) {
      		ServerStyle2D style = new ServerStyle2D(data);
      		registrar.register(agentClass, style);
      	}
      } 
      
      else if (styleName != null) {
        // TODO other styles are not currently supported.
      }
    }
  }
  
  public void registerNetworkStyles(GISDisplayDescriptor descriptor, Context<?> context)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
  
//    for (ProjectionData proj : descriptor.getProjections()) {
//      if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {
//      	Network<?> network = context.getProjection(Network.class, proj.getId());
//        String styleName = descriptor.getNetworkStyleClassName(proj.getId());
//  
//        Class<? extends NetworkStyleGIS> styleClass = (Class<? extends NetworkStyleGIS>)Class.forName(styleName, 
//        		true, this.getClass().getClassLoader());
//        
//        EditedEdgeStyleData<Object> data = null;
//        
//        if (EditedNetworkStyleGIS.class.isAssignableFrom(styleClass)) {
//        	String netEditedStyleName = descriptor.getNetworkEditedStyleName(proj.getId());
//        
//        	data = EditedStyleUtils.getEdgeStyle(netEditedStyleName);
//        }
//            
//        else  {
//        	// TODO support other style class?
//        	// TODO Handle unsupported style types
//        }
//        
//        if (data != null) {
////        	ServerNetStyle2D style = new ServerNetStyle2D(data);
////        	display.registerNetworkStyle(network, style);
//        }
//      }
//    }
  }
  


}
