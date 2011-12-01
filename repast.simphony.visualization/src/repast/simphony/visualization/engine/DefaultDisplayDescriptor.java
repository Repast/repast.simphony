package repast.simphony.visualization.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.schedule.DefaultDescriptor;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.VisualizationProperties;
import repast.simphony.visualization.gis3D.style.DefaultEdgeStyleGIS3D;
import repast.simphony.visualization.gisnew.DefaultStyleGIS;
import repast.simphony.visualization.visualization3D.style.DefaultEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.DefaultStyle3D;
import repast.simphony.visualizationOGL2D.DefaultEdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

/**
 * @author Nick Collier
 */
public class DefaultDisplayDescriptor extends DefaultDescriptor implements DisplayDescriptor {

  // default styles.
  private static Class<?>[] styles3D = new Class<?>[] { DefaultStyle3D.class };

  private static Class<?>[] styles2D = new Class<?>[] { DefaultStyleOGL2D.class };

  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultStyleGIS.class };

  // //TODO ###################################################################
  // private static Class<?>[] editedStyles3D = new Class<?>[] { null };
  //
  // private static Class<?>[] editedStyles2D = new Class<?>[] { null };
  //	
  // // ###################################################################

  private static Class<?>[] netStyles3D = new Class<?>[] { DefaultEdgeStyle3D.class };

  private static Class<?>[] netStyles2D = new Class<?>[] { DefaultEdgeStyleOGL2D.class };

  private static Class<?>[] netStylesGIS3D = new Class<?>[] { DefaultEdgeStyleGIS3D.class };

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

  public DefaultDisplayDescriptor() {
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
  public List<ProjectionData> getProjections() {
    return projections;
    // TODO: for now this is left modifiable, some of the wizards require that
    // this be modifiable
    // return Collections.unmodifiableList(projections);
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
    this.type = type;
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
    }
  }

  public void addStyle(String objClassName, String styleClassName) {
    styles.put(objClassName, styleClassName);
  }

  public void addEditedStyle(String objClassName, String userStyleName) {
    editedStyles.put(objClassName, userStyleName);
  }

  public void addNetworkStyle(Object networkID, String networkClassName) {
    netStyles.put(networkID, networkClassName);
  }

  public void addNetworkEditedStyle(Object networkID, String networkClassName) {
    editedNetStyles.put(networkID, networkClassName);
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
    this.frequency = frequency;
  }

  public ScheduleParameters getScheduleParameters() {
    return schedParams;
  }

  public void setScheduleParameters(ScheduleParameters scheduleInfo) {
    this.schedParams = scheduleInfo;
  }

  public String getLayoutClassName() {
    return layoutClassName;
  }

  public void setLayoutClassName(String className) {
    this.layoutClassName = className;
  }

  public String getLayoutProjection() {
    return layoutProjection;
  }

  public void setLayoutProjection(String layoutProjection) {
    this.layoutProjection = layoutProjection;
  }

  public void setLayoutInterval(int interval) {
    this.layoutInterval = interval;
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

  public Class<?>[] getDefaultStylesGIS3D() {
    return stylesGIS3D;
  }

  public Class<?>[] getDefaultNetStyles3D() {
    return netStyles3D;
  }

  public Class<?>[] getDefaultNetStyles2D() {
    return netStyles2D;
  }

  public Class<?>[] getDefaultNetStylesGIS3D() {
    return netStylesGIS3D;
  }

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
  }

  /**
   * Adds the named value layer to the list of value layers to display.
   * 
   * @param name
   *          the name of the value layer to display.
   */
  public void addValueLayerName(String name) {
    valueLayers.add(name);
  }

  /**
   * Gets a List of all the names of the value layers to display.
   * 
   * @return a List of all the names of the value layers to display.
   */
  public List<String> getValueLayerNames() {
    return valueLayers;
  }

  /**
   * Sets the value layer style name.
   * 
   * @param name
   */
  public void setValueLayerStyleName(String name) {
    valueLayerStyleName = name;
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
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color color) {
    backgroundColor = color;
  }

	public Map<String, Integer> getLayerOrders() {
		return layerOrder;
	}

	public Map<String, String> getEditedStyles() {
		return editedStyles;
	}
}