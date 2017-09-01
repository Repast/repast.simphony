package repast.simphony.gis.visualization.engine;

import java.util.Collection;

import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.StyleRegistrar;
import repast.simphony.visualization.gis3D.style.CoverageStyle;

/**
 * Coverage Style registrar for 3D GIS displays.
 * 
 * @author Eric Tatara
 */
public class CoverageStyleRegistrar extends StyleRegistrar<CoverageStyle<?>> {

	public void registerCoverageStyles(Registrar<CoverageStyle<?>> registrar, DisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
		Collection<String> agentNames = getOrderedAgentCollection(descriptor);
    
		registerStyles(registrar, descriptor, agentNames);
  }

  @Override
  protected CoverageStyle<?> createdEditedStyle(String editedStyleName) {

  	// TODO GIS provide edited style for coverage
  	
  	return null;
  }
}
