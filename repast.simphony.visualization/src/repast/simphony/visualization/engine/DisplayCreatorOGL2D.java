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
import repast.simphony.visualizationOGL2D.DisplayOGL2D;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * Creator for 2D displays.
 * 
 * @author Nick Collier
 */
public class DisplayCreatorOGL2D extends AbstractDisplayCreator {

  /**
   * @param context
   * @param descriptor
   */
  public DisplayCreatorOGL2D(Context<?> context, DisplayDescriptor descriptor) {
    super(context, descriptor);
  }

  @SuppressWarnings("unchecked")
  private void registerProjectionDecorators(DisplayOGL2D display) {
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

      final DisplayOGL2D display = new DisplayOGL2D(data, layout);

     
      // do vl style first so its at the back
      VLStyleRegistrarOGL2D vlReg = new VLStyleRegistrarOGL2D();
      vlReg.registerValueLayerStyle(new VLStyleRegistrar.Registrar<ValueLayerStyleOGL>() {
        public void register(ValueLayerStyleOGL style, ValueLayer layer) {
          display.registerValueLayerStyle(layer, style);
        }
      }, descriptor, context);
      

      StyleRegistrarOGL2D styleReg = new StyleRegistrarOGL2D();
      styleReg.registerStyles(new StyleRegistrar.Registrar<StyleOGL2D<?>>() {
        public void register(Class<?> agentClass, StyleOGL2D<?> style) {
          display.registerStyle(agentClass, style);
        }
      }, descriptor);

    
      NetworkStyleRegistrarOGL2D netReg = new NetworkStyleRegistrarOGL2D();
      netReg.registerNetworkStyles(new NetworkStyleRegistrar.Registrar<EdgeStyleOGL2D>() {
        public void register(Network<?> network, EdgeStyleOGL2D style) {
          display.registerNetworkStyle(network, style);
        }
      }, descriptor, context);
    
      
      registerProjectionDecorators(display);
      
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
