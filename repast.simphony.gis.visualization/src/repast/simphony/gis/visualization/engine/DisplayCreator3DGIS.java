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
 * Creates 3D GIS displays.
 * 
 * @author Nick Collier
 */
public class DisplayCreator3DGIS implements DisplayCreator {
  
	 protected Context<?> context;
	 protected GISDisplayDescriptor descriptor; 
	
  /**
   * @param context
   * @param descriptor
   */
  public DisplayCreator3DGIS(Context<?> context, GISDisplayDescriptor descriptor) {
  	this.context = context;
    this.descriptor = descriptor;
  }

  @SuppressWarnings("unchecked")
  protected GISDisplayData<?> createDisplayData() {
    GISDisplayData<?> data = new GISDisplayData(context);
    for (ProjectionData pData : descriptor.getProjections()) {
      data.addProjection(pData.getId());
    }
  
    data.setViewType(descriptor.getViewType());
   
    return data;
  }
  
  public IDisplay createDisplay() throws DisplayCreationException {
    try {
    GISDisplayData<?> data = createDisplayData();
    Layout<?, ?> layout = new NullLayout();

    final DisplayGIS3D display = new DisplayGIS3D(data, layout);
    
    GISStyleRegistrar registrar = new GISStyleRegistrar(display);    
    registrar.registerAllStyles(descriptor);

    // TODO GIS set background color
    display.setBackgroundColor(descriptor.getBackgroundColor());
    
    // TODO GIS set globe type (move from init data)
    descriptor.getDisplayType();
    
    display.setTrackAgents(descriptor.getTrackAgents());
    
    display.setLayoutFrequency(descriptor.getLayoutFrqeuency(), descriptor.getLayoutInterval());
    
    return display;
    } catch (Exception ex) {
      throw new DisplayCreationException(ex);
    }
  }
}