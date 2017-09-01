package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gis3D.DisplayGIS3D;
import repast.simphony.visualization.gis3D.style.StyleGIS;


/**
 * Creates GIS styles from a style classname.  The GIS registrar implementation
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

	public void registerAllStyles(DisplayDescriptor descriptor) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		// TODO GIS register all styles looking at layer orders etc
	
		// The descriptor layer orders is just a map independent of other descriptor info
		Map<String,Integer> layerOrders = descriptor.getLayerOrders();
		
		for (String layerName : layerOrders.keySet()) {
			System.out.println("GIS Display layer: " + layerName + " - " + layerOrders.get(layerName));
		}
		
		registerAgentStyles(descriptor);
	}
	
  public void registerAgentStyles(DisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
    // Iterate through layers in order and register
    for (String agentName : descriptor.agentClassStyleNames()) {
      
    	String styleName = descriptor.getStyleClassName(agentName);
      String editedStyleName = descriptor.getEditedStyleName(agentName);

      Integer layerOrder = descriptor.getLayerOrder(agentName);
      
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
      	display.registerStyle(agentClass, style, layerOrder);
      }
    }
  }
  
  public void registerCoverageStyles(DisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
//  	Collection<String> agentNames = getOrderedAgentCollection(descriptor);
//  	registerAgentStyles(descriptor, agentNames);
  }
  
  public void registerNetworkStyles(DisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
//  	Collection<String> agentNames = getOrderedAgentCollection(descriptor);
//  	registerAgentStyles(descriptor, agentNames);
  }
  
  protected StyleGIS<?> createAgentEditedStyle(String editedStyleName) {
  	
  	// TODO GIS provided editid style 
  	
  	return null;
  }
  
  protected StyleGIS<?> createCoverageEditedStyle(String editedStyleName) {
  	
  	// TODO GIS provided editid style 
  	
  	return null;
  }
  
  protected StyleGIS<?> createdNetworkEditedStyle(String editedStyleName) {
  	
  	// TODO GIS provided editid style 
  	
  	return null;
  }
}
