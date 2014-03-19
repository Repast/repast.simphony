package repast.simphony.gis.visualization.engine;

import repast.simphony.visualization.engine.BasicDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Display descriptor for GIS displays.
 * 
 * @author Eric Tatara
 *
 * TODO Projections: implement GIS-specific info like raster layers.
 */
public class GISDisplayDescriptor extends BasicDisplayDescriptor {

  // TODO WWJ - handle multiple styles
//  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultMarkStyle.class,
//      DefaultSurfaceShapeStyle.class };

  
  public GISDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
//    set(descriptor);
  }

  public GISDisplayDescriptor(String name) {
    super(name);
  }

  

//  public void set(DisplayDescriptor descriptor) {
//    setScheduleParameters(descriptor.getScheduleParameters());
//    setName(descriptor.getName());
//    setDisplayType(descriptor.getDisplayType());
//    setLayoutClassName(descriptor.getLayoutClassName());
//    setLayoutFrequency(descriptor.getLayoutFrqeuency());
//    setLayoutInterval(descriptor.getLayoutInterval());
//    setLayoutProjection(descriptor.getLayoutProjection());
//    setLayoutProperties(descriptor.getLayoutProperties());
//    styles.clear();
//    for (String name : descriptor.agentClassStyleNames()) {
//      addStyle(name, descriptor.getStyleClassName(name));
//    }
//    netStyles.clear();
//    for (Object name : descriptor.networkStyleIDs()) {
//      addNetworkStyle(name, descriptor.getNetworkStyleClassName(name));
//    }
//    editedStyles.clear();
//    // check for backwards compatibility with older display descriptors
//    if (descriptor.agentClassEditedStyleNames() != null)
//      for (String name : descriptor.agentClassEditedStyleNames())
//        addEditedStyle(name, descriptor.getEditedStyleName(name));
//
//    editedNetStyles.clear();
//    // check for backwards compatibility with older display descriptors
//    if (descriptor.networkEditedStyleIDs() != null)
//      for (Object name : descriptor.networkEditedStyleIDs())
//        addNetworkEditedStyle(name, descriptor.getNetworkEditedStyleName(name));
//
//    if (type.equals(DisplayType.TWO_D)) {
//      layerOrder.clear();
//      if (descriptor.agentClassLayerOrders() != null) {
//        for (String name : descriptor.agentClassLayerOrders()) {
//          addLayerOrder(name, descriptor.getLayerOrder(name));
//        }
//      }
//    }
//    for (ProjectionData proj : descriptor.getProjections()) {
//      addProjection(proj, descriptor.getProjectionDescriptor(proj.getId()));
//    }
//
//    for (String vlName : descriptor.getValueLayerNames()) {
//      addValueLayerName(vlName);
//    }
//
//    setValueLayerStyleName(descriptor.getValueLayerStyleName());
//
//    // check for backwards compatibility with older display descriptors
//    if (descriptor.getValueLayerEditedStyleName() != null)
//      setValueLayerEditedStyleName(descriptor.getValueLayerEditedStyleName());
//
//    for (String name : descriptor.propertyNames()) {
//      setProperty(name, descriptor.getProperty(name));
//    }
//
//    setBackgroundColor(descriptor.getBackgroundColor());
//  }

 
}