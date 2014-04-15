/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.DefaultDisplayData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.NullLayout;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.engine.StyleRegistrar.Registrar;
import repast.simphony.visualization.visualization3D.Display3D;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

/**
 * DisplayCreator for creating a 3D display.
 * 
 * @author Nick Collier
 */
public class DisplayCreator3D extends AbstractCartesianDisplayCreator {

  /**
   * @param context
   * @param descriptor
   */
  public DisplayCreator3D(Context<?> context, CartesianDisplayDescriptor descriptor) {
    super(context, descriptor);
  }

  @SuppressWarnings("unchecked")
  private void registerProjectionDecorators(Display3D display) {
    for (ProjectionDescriptor projDesc : descriptor.getProjectionDescriptors()) {
      String name = projDesc.getProjectionName();
      for (ProjectionDecorator3D deco : projDesc.get3DDecorators()) {
        Projection projection = context.getProjection(name);
        deco.setProjection(projection);
        display.registerDecorator(deco);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.engine.DisplayCreator#createDisplay(repast
   * .simphony.visualization.engine.DisplayDescriptor)
   */
  public IDisplay createDisplay() throws DisplayCreationException {
    try {
      DefaultDisplayData<?> data = createDisplayData();
      Layout<?, ?> layout = null;
      if (data.getProjectionCount() > 0) {
        String layoutProj = descriptor.getLayoutProjection();
        ProjectionDescriptor projDesc = descriptor.getProjectionDescriptor(layoutProj);
        String layoutClassName = projDesc.getImpliedLayout3D();
        layout = createLayout(layoutClassName);
      } else
        layout = new NullLayout();

      final Display3D display = new Display3D(data, layout);

      // register styles
      StyleRegistrar3D styleReg = new StyleRegistrar3D();
      styleReg.registerStyles(new Registrar<Style3D<?>>() {
        public void register(Class<?> agentClass, Style3D<?> style) {
          display.registerStyle(agentClass, style);
        }
      }, descriptor);

      // register network styles
      NetworkStyleRegistrar3D netStyleReg = new NetworkStyleRegistrar3D();
      netStyleReg.registerNetworkStyles(new NetworkStyleRegistrar.Registrar<EdgeStyle3D<?>>() {
        public void register(Network<?> network, EdgeStyle3D<?> style) {
          display.registerNetworkStyle(network, style);
        }
      }, descriptor, context);

      // register the projection decorators
      registerProjectionDecorators(display);
      
      VLStyleRegistrar3D vlStyleReg = new VLStyleRegistrar3D();
      vlStyleReg.registerValueLayerStyle(new VLStyleRegistrar.Registrar<ValueLayerStyle3D>() {
        public void register(ValueLayerStyle3D style, ValueLayer layer) {
          style.addValueLayer(layer);
          display.registerValueLayerStyle(style);
        }
      } , descriptor, context);
      
      display.setLayoutFrequency(descriptor.getLayoutFrqeuency(), descriptor.getLayoutInterval());
      display.setBackgroundColor(descriptor.getBackgroundColor());

      return display;
    } catch (Exception ex) {
      throw new DisplayCreationException(ex);
    }
  }
}
