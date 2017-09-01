package repast.simphony.gis.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.NullLayout;
import repast.simphony.visualization.engine.DisplayCreationException;
import repast.simphony.visualization.engine.DisplayCreator;
import repast.simphony.visualization.gis3D.DisplayGIS3D;

/**
 * Creates 3D GIS displays.  Uses a different type of style registrar than the
 *   other DisplayCreator implementations that handles all of the display layer
 *   types together.
 * 
 * @author Eric Tatara
 */
public class DisplayCreator3DGIS implements DisplayCreator {
  
	 protected Context<?> context;
	 protected GISDisplayDescriptor descriptor; 
	
  public DisplayCreator3DGIS(Context<?> context, GISDisplayDescriptor descriptor) {
  	this.context = context;
    this.descriptor = descriptor;
  }

  protected GISDisplayData<?> createDisplayData() {
  	
  	// DisplayData contains only the info needed in the display constructor.  
  	// All other info is set below from the descriptor in createDisplay().
    GISDisplayData<?> data = new GISDisplayData(context);
   
    // Add data about projections
    for (ProjectionData pData : descriptor.getProjections()) {
      data.addProjection(pData.getId());
    }
    
    // Set the initial globe type (flat or sphere)
    data.setViewType(descriptor.getViewType());
    
    // Add static coverage filename and style
    for (String fileName : descriptor.getStaticCoverageMap().keySet()) {
    	data.addStaticCoverage(fileName, descriptor.getStaticCoverageMap().get(fileName));
    }
    
    return data;
  }
  
  public IDisplay createDisplay() throws DisplayCreationException {
    try {
    GISDisplayData<?> data = createDisplayData();
    Layout<?, ?> layout = new NullLayout();

    final DisplayGIS3D display = new DisplayGIS3D(data, layout);
    
    GISStyleRegistrar registrar = new GISStyleRegistrar(display);    
    registrar.registerAllStyles(descriptor);

    // TODO GIS display render quality
    // TODO GIS layer legend visible layers
    // TODO GIS initial view location
    
    display.setBackgroundColor(descriptor.getBackgroundColor());    
    display.setTrackAgents(descriptor.getTrackAgents());
    display.setLayoutFrequency(descriptor.getLayoutFrqeuency(), descriptor.getLayoutInterval());
    
    return display;
    } catch (Exception ex) {
      throw new DisplayCreationException(ex);
    }
  }
}