package repast.simphony.gis.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.DefaultDisplayData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.NullLayout;
import repast.simphony.visualization.engine.DisplayCreationException;
import repast.simphony.visualization.engine.DisplayCreator;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.StyleRegistrar.Registrar;
import repast.simphony.visualization.gis3D.DisplayGIS3D;
import repast.simphony.visualization.gis3D.style.StyleGIS;

/**
 * Creates 3D GIS displays.
 * 
 * @author Nick Collier
 */
public class DisplayCreator3DGIS implements DisplayCreator {
  
	 protected Context<?> context;
	 protected DisplayDescriptor descriptor; 
	
  /**
   * @param context
   * @param descriptor
   */
  public DisplayCreator3DGIS(Context<?> context, DisplayDescriptor descriptor) {
  	this.context = context;
    this.descriptor = descriptor;
  }

  @SuppressWarnings("unchecked")
  protected DefaultDisplayData<?> createDisplayData() {
    DefaultDisplayData<?> data = new DefaultDisplayData(context);
    for (ProjectionData pData : descriptor.getProjections()) {
      data.addProjection(pData.getId());
    }
  
    return data;
  }
  
  public IDisplay createDisplay() throws DisplayCreationException {
    try {
    DefaultDisplayData<?> data = createDisplayData();
    Layout<?, ?> layout = new NullLayout();

    final DisplayGIS3D display = new DisplayGIS3D(data, layout);

 // register styles
    StyleRegistrarGIS3D styleReg = new StyleRegistrarGIS3D();
    styleReg.registerStyles(new Registrar<StyleGIS<?>>() {
      public void register(Class<?> agentClass, StyleGIS<?> style) {
        display.registerStyle(agentClass, style);
      }
    }, descriptor);

    // register network styles
//    NetworkStyleRegistrarGIS3D netStyleReg = new NetworkStyleRegistrarGIS3D();
//    netStyleReg.registerNetworkStyles(new NetworkStyleRegistrar.Registrar<EdgeStyleGIS3D<?>>() {
//      public void register(Network<?> network, EdgeStyleGIS3D<?> style) {
//        display.registerNetworkStyle(network, style);
//      }
//    }, descriptor, context);

    display.setLayoutFrequency(descriptor.getLayoutFrqeuency(), descriptor.getLayoutInterval());
    return display;
    } catch (Exception ex) {
      throw new DisplayCreationException(ex);
    }
  }

}
