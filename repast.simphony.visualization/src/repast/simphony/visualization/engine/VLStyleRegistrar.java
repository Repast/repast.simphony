/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.valueLayer.ValueLayer;


/**
 * Registers value layer styles on a display.
 * 
 * @author Nick Collier
 */
public abstract class VLStyleRegistrar<T> {
  
  /**
   * Inteface for a closure that registers the created style on the display.
   * 
   * @author Nick Collier
   * @param <T>
   *          the style type
   */
  public interface Registrar<T> {
    void register(T style, ValueLayer layer);
  }
  
  /**
   * Creates an instance of the the appropriate edited value layer
   * style.
   * 
   * @param styleName
   * @return the created edited value layer style
   */
  public abstract T createEditedValueLayerStyle(String styleName);
  
  @SuppressWarnings("unchecked")
  public void registerValueLayerStyle(Registrar<T> registrar, CartesianDisplayDescriptor descriptor,
      Context<?> context)  throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
    String vlStyleName = descriptor.getValueLayerStyleName();
    String vlEditedStyleName = descriptor.getValueLayerEditedStyleName();

    T style = null;

    if (vlEditedStyleName != null)
      style = createEditedValueLayerStyle(vlEditedStyleName);

    else if (vlStyleName != null) {
      Class<?> styleClass = Class.forName(vlStyleName, true, this.getClass().getClassLoader());
      style = (T) styleClass.newInstance();
    }

    if (style == null)
      return;

    // only supports one value layer in display
    String vlLayer = descriptor.getValueLayerNames().iterator().next();
    ValueLayer layer = context.getValueLayer(vlLayer);
    registrar.register(style, layer);
  }
}
