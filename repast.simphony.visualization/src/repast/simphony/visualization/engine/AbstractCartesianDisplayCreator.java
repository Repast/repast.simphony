package repast.simphony.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.DefaultDisplayData;
import repast.simphony.visualization.Layout;

/**
 * Display creator for 2D/3D Cartesian displays with value layers.
 * 
 * @author Nick Collier
 *
 */
public abstract class AbstractCartesianDisplayCreator implements DisplayCreator{

	 protected Context<?> context;
	 protected CartesianDisplayDescriptor descriptor; 
	
  public AbstractCartesianDisplayCreator(Context<?> context, CartesianDisplayDescriptor descriptor) {
  	this.context = context;
    this.descriptor = descriptor;
  }

  @SuppressWarnings("unchecked")
  protected DefaultDisplayData<?> createDisplayData() {
    DefaultDisplayData<?> data = new DefaultDisplayData(context);
    for (ProjectionData pData : descriptor.getProjections()) {
      data.addProjection(pData.getId());
    }
  
    for (String vlName : descriptor.getValueLayerNames()) {
      data.addValueLayer(vlName);
    }
    return data;
  }

  @SuppressWarnings("unchecked")
  protected Layout<?, ? extends Projection<?>> createLayout(String layoutClassName) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {
        // get the layout class, checking if the projection to base
        // the layout on has an implied layout (i.e. like grid) that
        // overrides the layout in the display descriptor.
        if (layoutClassName == null || layoutClassName.length() == 0) {
          layoutClassName = descriptor.getLayoutClassName();
        }
      
        Class<?> clazz = Class.forName(layoutClassName, true, this.getClass().getClassLoader());
        if (!Layout.class.isAssignableFrom(clazz)) {
          // todo better errors.
          throw new IllegalArgumentException("Layout class must implements Layout interface.");
        }
        Layout<?, Projection<?>> layout = (Layout<?, Projection<?>>) clazz.newInstance();
        String layoutProj = descriptor.getLayoutProjection();
        Projection<?> projection = context.getProjection(layoutProj);
        if (projection == null) {
          throw new RuntimeException("Projection '" + layoutProj + "' not found.");
        }
        layout.setProjection(projection);
        layout.setLayoutProperties(descriptor.getLayoutProperties());
        return layout;
      }
}