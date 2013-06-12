package repast.simphony.visualization.engine;

import java.awt.Color;
import java.util.Map;

import repast.simphony.engine.schedule.Descriptor;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.scenario.ScenarioChangedListener;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.VisualizationProperties;

/**
 * Descriptor for a 2D / 3D display.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public interface DisplayDescriptor extends Descriptor {

  void setLayoutInterval(int interval);

  int getLayoutInterval();

  Class<?>[] getDefaultStyles3D();

  Class<?>[] getDefaultStyles2D();

  Class<?>[] getDefaultStylesGIS3D();

  Class<?>[] getDefaultNetStyles3D();

  Class<?>[] getDefaultNetStyles2D();

  // TODO WWJ - networks
//  Class<?>[] getDefaultNetStylesGIS3D();

  /**
   * Removes any added projection descriptors.
   */
  void clearProjectionDescriptors();

  enum DisplayType {
    TWO_D("2D"), THREE_D("3D"), GIS("GIS"), GIS3D("GIS3D");

    private String name;

    DisplayType(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }

  DisplayType getDisplayType();

  void setDisplayType(DisplayType type);

  /**
   * Sets the display type of this descriptor. If reset is true, then the fields
   * in the descriptor that depend on type info will be reset.
   * 
   * @param type
   *          the display type
   * @param reset
   *          if true, then the fields in the descriptor that depend on type
   *          info will be reset.
   */
  void setDisplayType(DisplayType type, boolean reset);

  void addStyle(String objClassname, String styleClassName);

  void addEditedStyle(String objClassname, String userStyleName);

  void addLayerOrder(String objClassname, int order);

  void addNetworkStyle(Object networkID, String networkClassName);

  void addNetworkEditedStyle(Object networkID, String networkClassName);

  String getStyleClassName(String objClassName);

  String getEditedStyleName(String objClassName);

  Integer getLayerOrder(String objClassName);

  String getNetworkStyleClassName(Object networkID);

  String getNetworkEditedStyleName(Object networkID);

  Iterable<String> agentClassStyleNames();

  Iterable<String> agentClassEditedStyleNames();

  Iterable<String> agentClassLayerOrders();

  Iterable<Object> networkStyleIDs();

  Iterable<Object> networkEditedStyleIDs();

  IDisplay.LayoutFrequency getLayoutFrqeuency();

  void setLayoutFrequency(IDisplay.LayoutFrequency frequency);

  /**
   * Retrieves the parameters for when to activate the display update.
   * 
   * @return the parameters for when to activate the display update.
   */
  ScheduleParameters getScheduleParameters();

  /**
   * Sets the parameters for when to activate the display update.
   * 
   * @param scheduleInfo
   *          when to activate the display update
   */
  void setScheduleParameters(ScheduleParameters scheduleInfo);

  String getLayoutClassName();

  void setLayoutClassName(String className);

  /**
   * Sets the name of the projection to associate with the layout.
   * 
   * @param name
   */
  void setLayoutProjection(String name);

  /**
   * Gets the name of the projection associated with the layout.
   * 
   * @return the name of the projection associated with the layout.
   */
  String getLayoutProjection();

  /**
   * Gets the data for all the projections for which this is the display info.
   * 
   * @return the data for all the projections for which this is the display
   *         info.
   */
  Iterable<ProjectionData> getProjections();
  
  /**
   * Gets the number of projections in this display descriptor.
   * 
   * @return the number of projections in this display descriptor.
   */
  int getProjectionCount();

  /**
   * Adds the data for a projection for which this is the display info.
   * 
   * @param proj
   * @param descriptor
   */
  void addProjection(ProjectionData proj, ProjectionDescriptor descriptor);
  
  void clearProjections();
  
  void clearValueLayerNames();

  Iterable<ProjectionDescriptor> getProjectionDescriptors();

  /**
   * Gets the ProjectionDescriptor for the named projection.
   * 
   * @param name
   *          the name of the projection whose descriptor we want
   * 
   * @return the ProjectionDescriptor for the named projection.
   */
  ProjectionDescriptor getProjectionDescriptor(String name);

  /**
   * 
   * @return properties for the layout
   */
  VisualizationProperties getLayoutProperties();

  /**
   * Sets the layout properties for this display.
   */
  void setLayoutProperties(VisualizationProperties props);

  /**
   * Adds the named value layer to the list of value layers to display.
   * 
   * @param name
   *          the name of the value layer to display.
   */
  void addValueLayerName(String name);

  /**
   * Gets a List of all the names of the value layers to display.
   * 
   * @return a List of all the names of the value layers to display.
   */
  Iterable<String> getValueLayerNames();

  /**
   * Gets the name of the value layer style. Will return null if no value layer
   * style has been selected.
   * 
   * @return the name of the value layer style.
   */
  String getValueLayerStyleName();

  /**
   * Sets the value layer style name.
   * 
   * @param name
   */
  void setValueLayerStyleName(String name);

  /**
   * Gets the name of the edited value layer style. Will return null if no value
   * layer style has been selected.
   * 
   * @return the name of the value layer style.
   */
  String getValueLayerEditedStyleName();

  /**
   * Sets the value layer edited style name.
   * 
   * @param name
   */
  void setValueLayerEditedStyleName(String name);

  /**
   * Gets the Bounding Box for the display. This defines the extents of the
   * display
   * 
   * @return the BoundingBox
   */
  BoundingBox getBoundingBox();

  /**
   * Sets the BoundingBox for the display. This defines the extents of the
   * display.
   * 
   * @param boundingBox
   *          The BoundingBox
   */
  void setBoundingBox(BoundingBox boundingBox);

  /**
   * Gets the named property.
   * 
   * @param name
   *          the name of the property
   * 
   * @return the named property
   */
  Object getProperty(String name);

  /**
   * Sets the specified property.
   * 
   * @param name
   *          the property's name
   * @param value
   *          the property's value
   */
  void setProperty(String name, Object value);

  /**
   * Gets an iterable of names of the properties contained by this descriptor.
   * 
   * @return an iterable of names of the properties contained by this
   *         descriptor.
   */
  Iterable<String> propertyNames();

  Map<String, String> getStyles();

  /**
   * Gets the background color of the display.
   * 
   * @return the background color of the display.
   */
  Color getBackgroundColor();

  /**
   * Sets the background color of the display.
   * 
   * @param color
   *          the background color of the display.
   */
  void setBackgroundColor(Color color);

  Map<String, Integer> getLayerOrders();
  
  Map<String, String> getEditedStyles();
  
  void addScenarioChangedListener(ScenarioChangedListener listener);

  /**
   * Gets the number of Value Layers in this display descriptor.
   * 
   * @return
   */
  int getValueLayerCount();
}