/**
 * 
 */
package repast.simphony.visualization.engine;

import java.awt.Color;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.DefaultDisplayData;
import repast.simphony.visualization.IDisplay;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.NullLayout;
import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import repast.simphony.visualization.visualization2D.Display2D;
import repast.simphony.visualization.visualization2D.style.EdgeStyle2D;
import repast.simphony.visualization.visualization2D.style.Style2D;
import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;

/**
 * Creator for 2D displays.
 * 
 * @author Nick Collier
 */
public class DisplayCreator2D extends AbstractCartesianDisplayCreator {

  /**
   * @param context
   * @param descriptor
   */
  public DisplayCreator2D(Context<?> context, DisplayDescriptor descriptor) {
    super(context, descriptor);
  }

  @SuppressWarnings("unchecked")
  private void registerProjectionDecorators(Display2D display) {
    for (ProjectionDescriptor projDesc : descriptor.getProjectionDescriptors()) {
      String name = projDesc.getProjectionName();
      for (ProjectionDecorator2D deco : projDesc.get2DDecorators()) {
        Projection projection = context.getProjection(name);
        deco.setProjection(projection);
        display.registerDecorator(deco);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.engine.DisplayCreator#createDisplay()
   */
  public IDisplay createDisplay() throws DisplayCreationException {
    try {
      DefaultDisplayData<?> data = createDisplayData();
      Layout<?, ?> layout = null;
      if (data.getProjectionCount() > 0) {
        String layoutProj = descriptor.getLayoutProjection();
        ProjectionDescriptor projDesc = descriptor.getProjectionDescriptor(layoutProj);
        String layoutClassName = projDesc.getImpliedLayout2D();
        layout = createLayout(layoutClassName);
      } else
        layout = new NullLayout();

      final Display2D display = new Display2D(data, layout);

      // do vl style first so its at the back
      VLStyleRegistrar2D vlReg = new VLStyleRegistrar2D();
      vlReg.registerValueLayerStyle(new VLStyleRegistrar.Registrar<ValueLayerStyle>() {
        public void register(ValueLayerStyle style, ValueLayer layer) {
          style.addValueLayer(layer);
          display.registerValueLayerStyle(style);
        }
      }, descriptor, context);

      StyleRegistrar2D styleReg = new StyleRegistrar2D();
      styleReg.registerStyles(new StyleRegistrar.Registrar<Style2D<?>>() {
        public void register(Class<?> agentClass, Style2D<?> style) {
          display.registerStyle(agentClass, style);
        }
      }, descriptor);

      NetworkStyleRegistrar2D netReg = new NetworkStyleRegistrar2D();
      netReg.registerNetworkStyles(new NetworkStyleRegistrar.Registrar<EdgeStyle2D>() {
        public void register(Network<?> network, EdgeStyle2D style) {
          display.registerNetworkStyle(network, style);
        }
      }, descriptor, context);
      
      //registerProjectionDecorators(display);
      display.setLayoutFrequency(descriptor.getLayoutFrqeuency(), descriptor.getLayoutInterval());
      Color color = descriptor.getBackgroundColor();
      if (color != null)
        display.setBackgroundColor(color);
      return display;
    } catch (Exception ex) {
      throw new DisplayCreationException(ex);
    }
  }
}
