package repast.simphony.gis.visualization.engine;

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
import repast.simphony.visualization.engine.BoundingBox;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.ProjectionDescriptor;
import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
import repast.simphony.visualization.gis3D.style.DefaultSurfaceShapeStyle;

/**
 * 
 * @author tatara
 *
 */
public class GISDisplayDescriptor extends AbstractDescriptor implements DisplayDescriptor {

  // TODO WWJ - handle multiple styles
  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultMarkStyle.class,
      DefaultSurfaceShapeStyle.class };

  
  private DisplayType type = DisplayType.TWO_D;

  private Map<String, String> styles = new HashMap<String, String>();

  private Map<String, String> editedStyles = new HashMap<String, String>();

  private Map<Object, String> netStyles = new HashMap<Object, String>();

  private Map<Object, String> editedNetStyles = new HashMap<Object, String>();

  private IDisplay.LayoutFrequency frequency = IDisplay.LayoutFrequency.ON_NEW;

  private ScheduleParameters schedParams = ScheduleParameters.createRepeating(1, 1,
      Double.NEGATIVE_INFINITY);

  private Map<String, Integer> layerOrder = new HashMap<String, Integer>();;

  private List<ProjectionData> projections = new ArrayList<ProjectionData>();

  private VisualizationProperties visualizationProperties = null;

  private Map<String, ProjectionDescriptor> projectionDescriptors = new HashMap<String, ProjectionDescriptor>();

  private BoundingBox boundingBox = null;

  private Map<String, Object> props = new HashMap<String, Object>();

  private Color backgroundColor = null;
  
  // TODO Projections: these fields are needed only to deserialize the 2.1 display descriptors
  private List<String> valueLayers = new ArrayList<String>();
  private int layoutInterval = 0;


  public GISDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
    set(descriptor);
  }

  public GISDisplayDescriptor(String name) {
    super(name);
  }

  public Iterable<ProjectionData> getProjections() {
    return projections;
  }

  @Override
  public int getProjectionCount() {
    return projections.size();
  }

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

  @Override
  public void clearValueLayerNames() {
   
  }


  @Override
  public int getValueLayerCount() {
    return 0;
  }

 
  public ProjectionDescriptor getProjectionDescriptor(String name) {
    return projectionDescriptors.get(name);
  }

 
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

  @Override
  public String getLayoutClassName() {
    return null;
  }

  @Override
  public void setLayoutClassName(String className) {
    
  }

  @Override
  public String getLayoutProjection() {
    return null;
  }

  @Override
  public void setLayoutProjection(String layoutProjection) {
   
  }

  @Override
  public void setLayoutInterval(int interval) {
   
  }

  @Override
  public int getLayoutInterval() {
    return 0;
  }

  @Override
  public Class<?>[] getDefaultStyles3D() {
    return null;
  }

  @Override
  public Class<?>[] getDefaultStyles2D() {
    return null;
  }

  @Override
  public Class<?>[] getDefaultNetStyles3D() {
    return null;
  }

  @Override
  public Class<?>[] getDefaultNetStyles2D() {
    return null;
  }
  
  public Class<?>[] getDefaultStylesGIS3D() {
  	return stylesGIS3D;
  }

  @Override
  public VisualizationProperties getLayoutProperties() {
    return visualizationProperties;
  }

  @Override
  public void setLayoutProperties(VisualizationProperties props) {
    visualizationProperties = props;
    scs.fireScenarioChanged(this, "layoutProperties");
  }

  @Override
  public void addValueLayerName(String name) {
   
  }

  @Override
  public Iterable<String> getValueLayerNames() {
    return null;
  }

  @Override
  public void setValueLayerStyleName(String name) {
    
  }

  @Override
  public String getValueLayerStyleName() {
    return null;
  }

  @Override
  public BoundingBox getBoundingBox() {
    return boundingBox;
  }

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

  @Override
  public Object getProperty(String name) {
    return props.get(name);
  }

  @Override
  public void setProperty(String name, Object value) {
    props.put(name, value);
    scs.fireScenarioChanged(this, "property");
  }

  @Override
  public Iterable<String> propertyNames() {
    return props.keySet();
  }

  public Map<String, String> getStyles() {
    return styles;
  }

  @Override
  public String getValueLayerEditedStyleName() {
    return null;
  }

  @Override
  public void setValueLayerEditedStyleName(String name) {
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
}