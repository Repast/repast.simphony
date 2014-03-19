package repast.simphony.visualization.engine;


/**
 * Descriptor interface for descriptors with value layers.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public interface ValueLayerDescriptor {

  void clearValueLayerNames();

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
   * Gets the number of Value Layers in this display descriptor.
   * 
   * @return
   */
  int getValueLayerCount();
}