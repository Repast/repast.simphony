package repast.simphony.visualization.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.scenario.AbstractDescriptor;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.VisualizationProperties;
//import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
//import repast.simphony.visualization.gis3D.style.DefaultSurfaceShapeStyle;
import repast.simphony.visualization.visualization3D.style.DefaultEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.DefaultStyle3D;
import repast.simphony.visualizationOGL2D.DefaultEdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

/**
 * @author Nick Collier
 */
public class DefaultDisplayDescriptor extends AbstractDescriptor implements DisplayDescriptor {

  // default styles.
  private static Class<?>[] styles3D = new Class<?>[] { DefaultStyle3D.class };

  private static Class<?>[] styles2D = new Class<?>[] { DefaultStyleOGL2D.class };

  // TODO WWJ - handle multiple styles
//  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultMarkStyle.class,
//      DefaultSurfaceShapeStyle.class };

  // //TODO ###################################################################
  // private static Class<?>[] editedStyles3D = new Class<?>[] { null };
  //
  // private static Class<?>[] editedStyles2D = new Class<?>[] { null };
  //
  // // ###################################################################

  private static Class<?>[] netStyles3D = new Class<?>[] { DefaultEdgeStyle3D.class };

  private static Class<?>[] netStyles2D = new Class<?>[] { DefaultEdgeStyleOGL2D.class };

  // TODO WWJ network
  // private static Class<?>[] netStylesGIS3D = new Class<?>[] {
  // DefaultEdgeStyleGIS3D.class };

  private DisplayType type = DisplayType.TWO_D;

  private Map<String, String> styles = new HashMap<String, String>();

  private Map<String, String> editedStyles = new HashMap<String, String>();

  private Map<Object, String> netStyles = new HashMap<Object, String>();

  private Map<Object, String> editedNetStyles = new HashMap<Object, String>();

  private IDisplay.LayoutFrequency frequency = IDisplay.LayoutFrequency.ON_NEW;

  private ScheduleParameters schedParams = ScheduleParameters.createRepeating(1, 1,
      Double.NEGATIVE_INFINITY);

  private String layoutClassName;

  private String valueLayerStyleName;

  private String valueLayerEditedStyleName;

  private String layoutProjection;

  private int layoutInterval = 0;

  private Map<String, Integer> layerOrder = new HashMap<String, Integer>();;

  private List<ProjectionData> projections = new ArrayList<ProjectionData>();

  private List<String> valueLayers = new ArrayList<String>();

  private VisualizationProperties visualizationProperties = null;

  private Map<String, ProjectionDescriptor> projectionDescriptors = new HashMap<String, ProjectionDescriptor>();

  private BoundingBox boundingBox = null;

  private Map<String, Object> props = new HashMap<String, Object>();

  private Color backgroundColor = null;

  public DefaultDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
    set(descriptor);
  }

  public DefaultDisplayDescriptor(String name) {
    super(name);
  }

  /**
   * Gets the data for all the projections for which this is the display info.
   * 
   * @return the data for all the projections for which this is the display
   *         info.
   */
  public Iterable<ProjectionData> getProjections() {
    return projections;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.engine.DisplayDescriptor#getProjectionCount()
   */
  @Override
  public int getProjectionCount() {
    return projections.size();
  }

  /**
   * Adds the data for a projection for which this is the display info.
   * 
   * @param data
   * @param descriptor
   */
  public void addProjection(ProjectionData data, ProjectionDescriptor descriptor) {
    this.projections.add(data);
    this.projectionDescriptors.put(descriptor.getProjectionName(), descriptor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.engine.DisplayDescriptor#clearProjections()
   */
  @Override
  public void clearProjections() {
    if (projections.size() > 0) {
      this.projections.clear();
      scs.fireScenarioChanged(this, "projections");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.engine.DisplayDescriptor#clearValueLayerNames
   * ()
   */
  @Override
  public void clearValueLayerNames() {
    if (valueLayers.size() > 0) {
      this.valueLayers.clear();
      scs.fireScenarioChanged(this, "valueLayers");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.engine.DisplayDescriptor#getValueLayerCount()
   */
  @Override
  public int getValueLayerCount() {
    return valueLayers.size();
  }

  /**
   * Gets the ProjectionDescriptor for the named projection.
   * 
   * @param name
   *          the name of the projection whose descriptor we want
   * @return the ProjectionDescriptor for the named projection.
   */
  public ProjectionDescriptor getProjectionDescriptor(String name) {
    return projectionDescriptors.get(name);
  }

  /**
   * Removes any added projection descriptors.
   */
  public void clearProjectionDescriptors() {
    projectionDescriptors.clear();
  }

  public Iterable<ProjectionDescriptor> getProjectionDescriptors() {
    return projectionDescriptors.values();
  }

  public void set(DisplayDescriptor descriptor) {
    setScheduleParameters(descriptor.getScheduleParameters());
    setName(descriptor.getName());
    setDisplayType(descriptor.getDisplayType());
    setLayoutClassName(descriptor.getLayoutClassName());
    setLayoutFrequency(descriptor.getLayoutFrqeuency());
    setLayoutInterval(descriptor.getLayoutInterval());
    setLayoutProjection(descriptor.getLayoutProjection());
    setLayoutProperties(descriptor.getLayoutProperties());
    styles.clear();
    for (String name : descriptor.agentClassStyleNames()) {
      addStyle(name, descriptor.getStyleClassName(name));
    }
    netStyles.clear();
    for (Object name : descriptor.networkStyleIDs()) {
      addNetworkStyle(name, descriptor.getNetworkStyleClassName(name));
    }
    editedStyles.clear();
    // check for backwards compatibility with older display descriptors
    if (descriptor.agentClassEditedStyleNames() != null)
      for (String name : descriptor.agentClassEditedStyleNames())
        addEditedStyle(name, descriptor.getEditedStyleName(name));

    editedNetStyles.clear();
    // check for backwards compatibility with older display descriptors
    if (descriptor.networkEditedStyleIDs() != null)
      for (Object name : descriptor.networkEditedStyleIDs())
        addNetworkEditedStyle(name, descriptor.getNetworkEditedStyleName(name));

    if (type == DisplayType.TWO_D) {
      layerOrder.clear();
      if (descriptor.agentClassLayerOrders() != null) {
        for (String name : descriptor.agentClassLayerOrders()) {
          addLayerOrder(name, descriptor.getLayerOrder(name));
        }
      }
    }
    for (ProjectionData proj : descriptor.getProjections()) {
      addProjection(proj, descriptor.getProjectionDescriptor(proj.getId()));
    }

    for (String vlName : descriptor.getValueLayerNames()) {
      addValueLayerName(vlName);
    }

    setValueLayerStyleName(descriptor.getValueLayerStyleName());

    // check for backwards compatibility with older display descriptors
    if (descriptor.getValueLayerEditedStyleName() != null)
      setValueLayerEditedStyleName(descriptor.getValueLayerEditedStyleName());

    for (String name : descriptor.propertyNames()) {
      setProperty(name, descriptor.getProperty(name));
    }

    setBackgroundColor(descriptor.getBackgroundColor());
  }

  public DisplayType getDisplayType() {
    return type;
  }

  public void setDisplayType(DisplayType type) {
    if (this.type != type) {
      this.type = type;
      scs.fireScenarioChanged(this, "displayType");
    }
  }

  public void setDisplayType(DisplayType type, boolean reset) {
    setDisplayType(type);
    if (reset) {
      styles.clear();
      editedStyles.clear();
      netStyles.clear();
      editedNetStyles.clear();
      layerOrder.clear();
      layoutClassName = null;
      scs.fireScenarioChanged(this, "displayTypeReset");
    }
  }

  public void addStyle(String objClassName, String styleClassName) {
    if (!mapContains(styles, objClassName, styleClassName)) {
      styles.put(objClassName, styleClassName);
      scs.fireScenarioChanged(this, "style");
    }
  }

  public void addEditedStyle(String objClassName, String userStyleName) {
    if (!mapContains(editedStyles, objClassName, userStyleName)) {
      editedStyles.put(objClassName, userStyleName);
      scs.fireScenarioChanged(this, "editedStyle");
    }
  }

  private boolean mapContains(Map<String, String> map, String key, String value) {
    String val = map.get(key);
    return val != null && val.equals(value);
  }

  public void addNetworkStyle(Object networkID, String networkClassName) {
    String val = netStyles.get(networkID);
    if (val == null || !val.equals(networkClassName)) {
      netStyles.put(networkID, networkClassName);
      scs.fireScenarioChanged(this, "netStyle");
    }
  }

  public void addNetworkEditedStyle(Object networkID, String networkClassName) {
    String val = editedNetStyles.get(networkID);
    if (val == null || !val.equals(networkClassName)) {
      editedNetStyles.put(networkID, networkClassName);
      scs.fireScenarioChanged(this, "editedNetStyle");
    }
  }

  public String getStyleClassName(String objClassName) {
    return styles.get(objClassName);
  }

  public String getEditedStyleName(String objClassName) {
    if (editedStyles == null) // for backwards compatibility with older display
      // descriptors
      return null;
    return editedStyles.get(objClassName);
  }

  public String getNetworkStyleClassName(Object networkID) {
    return netStyles.get(networkID);
  }

  public String getNetworkEditedStyleName(Object networkID) {
    if (editedNetStyles == null) // for backwards compatibility with older
      // display descriptors
      return null;
    return editedNetStyles.get(networkID);
  }

  public Iterable<String> agentClassStyleNames() {
    return styles.keySet();
  }

  public Iterable<String> agentClassEditedStyleNames() {
    if (editedStyles == null) // for backwards compatibility with older display
      // descriptors
      return null;
    return editedStyles.keySet();
  }

  public Iterable<Object> networkStyleIDs() {
    return netStyles.keySet();
  }

  public Iterable<Object> networkEditedStyleIDs() {
    if (editedNetStyles == null) // for backwards compatibility with older
      // display descriptors
      return null;
    return editedNetStyles.keySet();
  }

  public IDisplay.LayoutFrequency getLayoutFrqeuency() {
    return frequency;
  }

  public void setLayoutFrequency(IDisplay.LayoutFrequency frequency) {
    if (this.frequency != frequency) {
      this.frequency = frequency;
      scs.fireScenarioChanged(this, "layoutFrequency");
    }
  }

  public ScheduleParameters getScheduleParameters() {
    return schedParams;
  }

  public void setScheduleParameters(ScheduleParameters scheduleInfo) {
    if (!this.schedParams.equals(scheduleInfo)) {
      this.schedParams = scheduleInfo;
      scs.fireScenarioChanged(this, "scheduleParameters");
    }
  }

  public String getLayoutClassName() {
    return layoutClassName;
  }

  public void setLayoutClassName(String className) {
    this.layoutClassName = className;
    scs.fireScenarioChanged(this, "layoutClassName");
  }

  public String getLayoutProjection() {
    return layoutProjection;
  }

  public void setLayoutProjection(String layoutProjection) {
    this.layoutProjection = layoutProjection;
    scs.fireScenarioChanged(this, "layoutProjection");
  }

  public void setLayoutInterval(int interval) {
    this.layoutInterval = interval;
    scs.fireScenarioChanged(this, "layoutInterval");
  }

  public int getLayoutInterval() {
    return this.layoutInterval;
  }

  public Class<?>[] getDefaultStyles3D() {
    return styles3D;
  }

  public Class<?>[] getDefaultStyles2D() {
    return styles2D;
  }

//  public Class<?>[] getDefaultStylesGIS3D() {
//    return stylesGIS3D;
//  }

  public Class<?>[] getDefaultNetStyles3D() {
    return netStyles3D;
  }

  public Class<?>[] getDefaultNetStyles2D() {
    return netStyles2D;
  }

  // TODO WWJ network
  // public Class<?>[] getDefaultNetStylesGIS3D() {
  // return netStylesGIS3D;
  // }

  /**
   * @return hints for displaying a grid.
   */
  public VisualizationProperties getLayoutProperties() {
    return visualizationProperties;
  }

  /**
   * Sets the layout properties for this display.
   */
  public void setLayoutProperties(VisualizationProperties props) {
    visualizationProperties = props;
    scs.fireScenarioChanged(this, "layoutProperties");
  }

  /**
   * Adds the named value layer to the list of value layers to display.
   * 
   * @param name
   *          the name of the value layer to display.
   */
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
  public Iterable<String> getValueLayerNames() {
    return valueLayers;
  }

  /**
   * Sets the value layer style name.
   * 
   * @param name
   */
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
  public String getValueLayerStyleName() {
    return valueLayerStyleName;
  }

  /**
   * Gets the Bounding Box for the display. This defines the extents of the
   * display
   * 
   * @return the BoundingBox
   */
  public BoundingBox getBoundingBox() {
    return boundingBox;
  }

  /**
   * Sets the BoundingBox for the display. This defines the extents of the
   * display.
   * 
   * @param boundingBox
   *          The BoundingBox
   */
  public void setBoundingBox(BoundingBox boundingBox) {
    this.boundingBox = boundingBox;
    scs.fireScenarioChanged(this, "boundingBox");
  }

  public Iterable<String> agentClassLayerOrders() {
    if (layerOrder == null) // for backwards compatibility with older display
      // descriptors
      return null;
    return layerOrder.keySet();
  }

  public Integer getLayerOrder(String objClassName) {
    if (layerOrder == null) // for backwards compatibility with older display
      // descriptors
      return null;

    return layerOrder.get(objClassName);
  }

  public void addLayerOrder(String objClassName, int order) {
    layerOrder.put(objClassName, order);
    scs.fireScenarioChanged(this, "layerOrder");
  }

  /**
   * Gets the named property.
   * 
   * @param name
   *          the name of the property
   * @return the named property
   */
  public Object getProperty(String name) {
    return props.get(name);
  }

  /**
   * Sets the specified property.
   * 
   * @param name
   *          the property's name
   * @param value
   *          the property's value
   */
  public void setProperty(String name, Object value) {
    props.put(name, value);
    scs.fireScenarioChanged(this, "property");
  }

  /**
   * Gets an iterable of names of the properties contained by this descriptor.
   * 
   * @return an iterable of names of the properties contained by this
   *         descriptor.
   */
  public Iterable<String> propertyNames() {
    return props.keySet();
  }

  public Map<String, String> getStyles() {
    return styles;
  }

  /**
   * Gets the name of the value layer edited style. Will return null if no value
   * layer style has been selected.
   * 
   * @return the name of the value layer edited style.
   */
  public String getValueLayerEditedStyleName() {
    return valueLayerEditedStyleName;
  }

  /**
   * Sets the value layer edited style name.
   * 
   * @param name
   */
  public void setValueLayerEditedStyleName(String name) {
    valueLayerEditedStyleName = name;
    scs.fireScenarioChanged(this, "valueLayerEditedStyle");
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color color) {
    backgroundColor = color;
    scs.fireScenarioChanged(this, "backgroundColor");
  }

  public Map<String, Integer> getLayerOrders() {
    return layerOrder;
  }

  public Map<String, String> getEditedStyles() {
    return editedStyles;
  }
}