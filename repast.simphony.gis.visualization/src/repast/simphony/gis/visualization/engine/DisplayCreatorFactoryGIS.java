package repast.simphony.gis.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.visualization.engine.DisplayCreator;
import repast.simphony.visualization.engine.DisplayCreatorFactory;

/**
 * 
 * @author Eric Tatara
 *
 * @deprecated Use 3D GIS display instead
 */
@Deprecated
public class DisplayCreatorFactoryGIS implements DisplayCreatorFactory<GISDisplayDescriptor> {

	@Override
	public DisplayCreator createDisplayCreator(Context context, GISDisplayDescriptor descriptor) {
		
		return new DisplayCreatorGIS(context, descriptor);
	}
}