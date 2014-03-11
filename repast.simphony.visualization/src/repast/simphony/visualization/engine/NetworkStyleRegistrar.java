/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.graph.Network;

/**
 * Registers network styles on a display.
 * 
 * @author Nick Collier
 */
public abstract class NetworkStyleRegistrar<T> {

  /**
   * Inteface for a closure that registers the created style on the display.
   * 
   * @author Nick Collier
   * @param <T>
   *          the style type
   */
  public interface Registrar<T> {
    void register(Network<?> network, T style);
  }

  protected abstract T createEditedEdgeStyle(String styleName);

  @SuppressWarnings("unchecked")
  public void registerNetworkStyles(Registrar<T> registrar, DisplayDescriptor descriptor,
      Context<?> context)  throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    for (ProjectionData proj : descriptor.getProjections()) {
      if (proj.getType() == ProjectionData.NETWORK_TYPE) {
        String netStyleName = descriptor.getNetworkStyleClassName(proj.getId());
        String netEditedStyleName = descriptor.getNetworkEditedStyleName(proj.getId());

        Network<?> network = context.getProjection(Network.class, proj.getId());

        // Style editor references get priority over explicit style classes if
        // both are specified in descriptor
        if (netEditedStyleName != null) {
          T style = createEditedEdgeStyle(netEditedStyleName);
          registrar.register(network, style);
        } else if (netStyleName != null) {
          Class<?> styleClass = Class.forName(netStyleName, true, this.getClass().getClassLoader());
          T style = (T) styleClass.newInstance();
          registrar.register(network, style);
        }
      }
    }
  }
}
