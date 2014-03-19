package repast.simphony.visualization.engine;

import java.util.ArrayList;
import java.util.List;
//import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
//import repast.simphony.visualization.gis3D.style.DefaultSurfaceShapeStyle;

/**
 * Display descriptor for Cartesian (2D,3D) displays with value layers.
 * 
 * TODO Projections: also implement network descriptor?
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */

public class CartesianDisplayDescriptor extends BasicDisplayDescriptor implements ValueLayerDescriptor {

  private String valueLayerStyleName;

  private String valueLayerEditedStyleName;

  private List<String> valueLayers = new ArrayList<String>();

  public CartesianDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
//    set(descriptor);
  }

  public CartesianDisplayDescriptor(String name) {
    super(name);
  }

  @Override
  public void clearValueLayerNames() {
    if (valueLayers.size() > 0) {
      this.valueLayers.clear();
      scs.fireScenarioChanged(this, "valueLayers");
    }
  }

  @Override
  public int getValueLayerCount() {
    return valueLayers.size();
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

  /**
   * Adds the named value layer to the list of value layers to display.
   * 
   * @param name
   *          the name of the value layer to display.
   */
  @Override
  public void addValueLayerName(String name) {
    if (!valueLayers.contains(name)) {
      valueLayers.add(name);
      scs.fireScenarioChanged(this, "valueLayer");
    }
  }

  /**
   * Gets a List of all the names of the value layers to display.
   * 
   * @return a List of all the names of the value layers to display.
   */
  @Override
  public Iterable<String> getValueLayerNames() {
    return valueLayers;
  }

  /**
   * Sets the value layer style name.
   * 
   * @param name
   */
  @Override
  public void setValueLayerStyleName(String name) {
    valueLayerStyleName = name;
    scs.fireScenarioChanged(this, "valueLayerStyle");
  }

  /**
   * Gets the name of the value layer style. Will return null if no value layer
   * style has been selected.
   * 
   * @return the name of the value layer style.
   */
  @Override
  public String getValueLayerStyleName() {
    return valueLayerStyleName;
  }

  /**
   * Gets the name of the value layer edited style. Will return null if no value
   * layer style has been selected.
   * 
   * @return the name of the value layer edited style.
   */
  @Override
  public String getValueLayerEditedStyleName() {
    return valueLayerEditedStyleName;
  }

  /**
   * Sets the value layer edited style name.
   * 
   * @param name
   */
  @Override
  public void setValueLayerEditedStyleName(String name) {
    valueLayerEditedStyleName = name;
    scs.fireScenarioChanged(this, "valueLayerEditedStyle");
  }
}