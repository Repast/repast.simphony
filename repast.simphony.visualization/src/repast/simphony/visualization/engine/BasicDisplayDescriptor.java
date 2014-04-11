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

/**
 * Basic Display descriptor implementation.
 * 
 * TODO Projections: also implement network descriptor?
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */

public abstract class BasicDisplayDescriptor extends AbstractDescriptor implements DisplayDescriptor {

	// TODO Projections: all fields should be initialized by another class that might be located in the viz registry.
  private static Class<?>[] defaultStyles; 
  private static Class<?>[] defaultNetStyles;

  private String type = DisplayType.TWO_D;

  private Map<String, String> styles = new HashMap<String, String>();

  private Map<String, String> editedStyles = new HashMap<String, String>();

  private Map<Object, String> netStyles = new HashMap<Object, String>();

  private Map<Object, String> editedNetStyles = new HashMap<Object, String>();

  private IDisplay.LayoutFrequency frequency = IDisplay.LayoutFrequency.ON_NEW;

  private ScheduleParameters schedParams = ScheduleParameters.createRepeating(1, 1,
      Double.NEGATIVE_INFINITY);

  // TODO Projections: refactor to use just this layout class name instead of
  //       storing the "implicit" layout class in a ProjectionDescriptor!
  private String layoutClassName;

  private String layoutProjection;

  private int layoutInterval = 0;

  private Map<String, Integer> layerOrder = new HashMap<String, Integer>();;

  private List<ProjectionData> projections = new ArrayList<ProjectionData>();

  private VisualizationProperties visualizationProperties = null;

  private Map<String, ProjectionDescriptor> projectionDescriptors = new HashMap<String, ProjectionDescriptor>();

  private BoundingBox boundingBox = null;

  private Map<String, Object> props = new HashMap<String, Object>();

  private Color backgroundColor = null;

  public BasicDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
  }

  public BasicDisplayDescriptor(String name) {
    super(name);
  }

  /**
   * Gets the data for all the projections for which this is the display info.
   * 
   * @return the data for all the projections for which this is the display
   *         info.
   */
  @Override
  public Iterable<ProjectionData> getProjections() {
    return projections;
  }


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
  @Override
  public void addProjection(ProjectionData data, ProjectionDescriptor descriptor) {
    this.projections.add(data);
    this.projectionDescriptors.put(descriptor.getProjectionName(), descriptor);
  }

  @Override
  public void clearProjections() {
    if (projections.size() > 0) {
      this.projections.clear();
      scs.fireScenarioChanged(this, "projections");
    }
  }

  /**
   * Gets the ProjectionDescriptor for the named projection.
   * 
   * @param name
   *          the name of the projection whose descriptor we want
   * @return the ProjectionDescriptor for the named projection.
   */
  @Override
  public ProjectionDescriptor getProjectionDescriptor(String name) {
    return projectionDescriptors.get(name);
  }

  /**
   * Removes any added projection descriptors.
   */
  @Override
  public void clearProjectionDescriptors() {
    projectionDescriptors.clear();
  }

  @Override
  public Iterable<ProjectionDescriptor> getProjectionDescriptors() {
    return projectionDescriptors.values();
  }

  /**
   * Sets the fields of this descriptor from another when making copies.
   * 
   * @param descriptor
   */
  public void set(DisplayDescriptor descriptor) {
  	
  	// TODO Projections: minimize duplicates with subclasses.
  	
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
    
    for (ProjectionData proj : descriptor.getProjections()) {
      addProjection(proj, descriptor.getProjectionDescriptor(proj.getId()));
    }

    for (String name : descriptor.propertyNames()) {
      setProperty(name, descriptor.getProperty(name));
    }

    setBackgroundColor(descriptor.getBackgroundColor());
  }

  @Override
  public String getDisplayType() {
    return type;
  }

  
  private void setDisplayType(String type) {
    if (this.type != type) {
      this.type = type;
      scs.fireScenarioChanged(this, "displayType");
    }
  }

  @Override
  public void setDisplayType(String type, boolean reset) {
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

  @Override
  public void addStyle(String objClassName, String styleClassName) {
    if (!mapContains(styles, objClassName, styleClassName)) {
      styles.put(objClassName, styleClassName);
      scs.fireScenarioChanged(this, "style");
    }
  }

  @Override
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

  @Override
  public void addNetworkStyle(Object networkID, String networkClassName) {
    String val = netStyles.get(networkID);
    if (val == null || !val.equals(networkClassName)) {
      netStyles.put(networkID, networkClassName);
      scs.fireScenarioChanged(this, "netStyle");
    }
  }

  @Override
  public void addNetworkEditedStyle(Object networkID, String networkClassName) {
    String val = editedNetStyles.get(networkID);
    if (val == null || !val.equals(networkClassName)) {
      editedNetStyles.put(networkID, networkClassName);
      scs.fireScenarioChanged(this, "editedNetStyle");
    }
  }

  @Override
  public String getStyleClassName(String objClassName) {
    return styles.get(objClassName);
  }

  @Override
  public String getEditedStyleName(String objClassName) {
    if (editedStyles == null) // for backwards compatibility with older display
      // descriptors
      return null;
    return editedStyles.get(objClassName);
  }

  @Override
  public String getNetworkStyleClassName(Object networkID) {
    return netStyles.get(networkID);
  }

  @Override
  public String getNetworkEditedStyleName(Object networkID) {
    if (editedNetStyles == null) // for backwards compatibility with older
      // display descriptors
      return null;
    return editedNetStyles.get(networkID);
  }

  @Override
  public Iterable<String> agentClassStyleNames() {
    return styles.keySet();
  }

  @Override
  public Iterable<String> agentClassEditedStyleNames() {
    if (editedStyles == null) // for backwards compatibility with older display
      // descriptors
      return null;
    return editedStyles.keySet();
  }

  @Override
  public Iterable<Object> networkStyleIDs() {
    return netStyles.keySet();
  }

  @Override
  public Iterable<Object> networkEditedStyleIDs() {
    if (editedNetStyles == null) // for backwards compatibility with older
      // display descriptors
      return null;
    return editedNetStyles.keySet();
  }

  @Override
  public IDisplay.LayoutFrequency getLayoutFrqeuency() {
    return frequency;
  }

  @Override
  public void setLayoutFrequency(IDisplay.LayoutFrequency frequency) {
    if (this.frequency != frequency) {
      this.frequency = frequency;
      scs.fireScenarioChanged(this, "layoutFrequency");
    }
  }

  @Override
  public ScheduleParameters getScheduleParameters() {
    return schedParams;
  }

  @Override
  public void setScheduleParameters(ScheduleParameters scheduleInfo) {
    if (!this.schedParams.equals(scheduleInfo)) {
      this.schedParams = scheduleInfo;
      scs.fireScenarioChanged(this, "scheduleParameters");
    }
  }

  @Override
  public String getLayoutClassName() {
    return layoutClassName;
  }

  @Override
  public void setLayoutClassName(String className) {
    this.layoutClassName = className;
    scs.fireScenarioChanged(this, "layoutClassName");
  }

  @Override
  public String getLayoutProjection() {
    return layoutProjection;
  }

  @Override
  public void setLayoutProjection(String layoutProjection) {
    this.layoutProjection = layoutProjection;
    scs.fireScenarioChanged(this, "layoutProjection");
  }

  @Override
  public void setLayoutInterval(int interval) {
    this.layoutInterval = interval;
    scs.fireScenarioChanged(this, "layoutInterval");
  }

  @Override
  public int getLayoutInterval() {
    return this.layoutInterval;
  }

  /**
   * @return hints for displaying a grid.
   */
  @Override
  public VisualizationProperties getLayoutProperties() {
    return visualizationProperties;
  }

  /**
   * Sets the layout properties for this display.
   */
  @Override
  public void setLayoutProperties(VisualizationProperties props) {
    visualizationProperties = props;
    scs.fireScenarioChanged(this, "layoutProperties");
  }

  /**
   * Gets the Bounding Box for the display. This defines the extents of the
   * display
   * 
   * @return the BoundingBox
   */
  @Override
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
  @Override
  public void setBoundingBox(BoundingBox boundingBox) {
    this.boundingBox = boundingBox;
    scs.fireScenarioChanged(this, "boundingBox");
  }

  @Override
  public Iterable<String> agentClassLayerOrders() {
    if (layerOrder == null) // for backwards compatibility with older display
      // descriptors
      return null;
    return layerOrder.keySet();
  }

  @Override
  public Integer getLayerOrder(String objClassName) {
    if (layerOrder == null) // for backwards compatibility with older display
      // descriptors
      return null;

    return layerOrder.get(objClassName);
  }

  @Override
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
  @Override
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
  @Override
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
  @Override
  public Iterable<String> propertyNames() {
    return props.keySet();
  }

  @Override
  public Map<String, String> getStyles() {
    return styles;
  }

  @Override
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  @Override
  public void setBackgroundColor(Color color) {
    backgroundColor = color;
    scs.fireScenarioChanged(this, "backgroundColor");
  }

  @Override
  public Map<String, Integer> getLayerOrders() {
    return layerOrder;
  }

  @Override
  public Map<String, String> getEditedStyles() {
    return editedStyles;
  }

	@Override
	public Class<?>[] getDefaultStyles() {
		return defaultStyles;
	}

	@Override
	public Class<?>[] getDefaultNetStyles() {
		return defaultNetStyles;
	}
}