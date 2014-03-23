package repast.simphony.gis.visualization.engine;

import repast.simphony.visualization.engine.BasicDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Display descriptor for GIS displays.
 * 
 * @author Eric Tatara
 *
 * TODO Projections: implement GIS-specific info like raster layers.
 */
public class GISDisplayDescriptor extends BasicDisplayDescriptor {

  // TODO WWJ - handle multiple styles
//  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultMarkStyle.class,
//      DefaultSurfaceShapeStyle.class };
	
	private static final long serialVersionUID = 8349240641245552328L;

	public GISDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
    set(descriptor);
  }

  public GISDisplayDescriptor(String name) {
    super(name);
  }

  @Override
  public void set(DisplayDescriptor descriptor) {
  	super.set(descriptor);
  	
  }

  @Override
	public DisplayDescriptor makeCopy() {
  	return new GISDisplayDescriptor(this);
	}
 
}