/**
 * 
 */
package repast.simphony.visualization.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;


/**
 * Creates a style from a style classname.
 * 
 * 
 * @author Nick Collier
 */
public abstract class StyleRegistrar<T> {

  /**
   * Inteface for a closure that registers the created
   * style on the display.
   * 
   * @author Nick Collier
   * @param <T> the style type
   */
  public interface Registrar<T> {
    void register(Class<?> agentClass, T style);
  }

  public void registerStyles(Registrar<T> registrar, DisplayDescriptor descriptor)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Collection<String> agentNames = getOrderedAgentCollection(descriptor);
    registerStyles(registrar, descriptor, agentNames);
  }

  private Collection<String> getOrderedAgentCollection(DisplayDescriptor descriptor) {
    TreeMap<Integer, String> map = new TreeMap<Integer, String>();
    List<String> unsorted = new ArrayList<String>();

    // Styles need to be registered in the order in which they
    // are to be displayed, with 0 being the lowest (background).

    // First check available layer order info and add to map
    for (String agentName : descriptor.agentClassStyleNames()) {
      Integer layerOrder = descriptor.getLayerOrder(agentName);
      if (layerOrder != null)
        map.put(layerOrder, agentName);
      else
        unsorted.add(agentName); // if no order info for layer,
    }

    // Add the layers with no order info to the end of the map
    int index;
    if (map.size() > 0)
      index = map.lastKey() + 1;
    else
      index = 0;
    for (String s : unsorted) {
      map.put(index, s);
      index++;
    }

    return map.values();
  }
  
  /**
   * Creates an edited style object.
   * 
   * @param editedStyleName the name of the style
   * @return the created edited style.s
   */
  protected abstract T createdEditedStyle(String editedStyleName);

  @SuppressWarnings("unchecked")
  private void registerStyles(Registrar<T> registrar, DisplayDescriptor descriptor,
      Collection<String> agentNames) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException {

    // Iterate through layers in order and register
    for (String agentName : agentNames) {
      String styleName = descriptor.getStyleClassName(agentName);
      String editedStyleName = descriptor.getEditedStyleName(agentName);

      Class<?> agentClass;

      agentClass = Class.forName(agentName, true, this.getClass().getClassLoader());

      // Style editor references get priority over explicit style classes if
      // both are specified in descriptor
      if (editedStyleName != null) {
        T style = createdEditedStyle(editedStyleName);
        registrar.register(agentClass, style);
      } else if (styleName != null) {
        Class<?> styleClass = Class.forName(styleName, true, this.getClass().getClassLoader());
        T style = (T) styleClass.newInstance();
        registrar.register(agentClass, style);
      }

    }
  }

}
